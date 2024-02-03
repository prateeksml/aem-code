package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.constants.NameConstants;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.userpermission.GroupInfo;
import com.kpmg.core.userpermission.MSMUserAccess;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.userpermission.UserInfo;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to send emails for approve activate roll out and activate rollout work flows.
 */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "KPMG Approve Activate - Email Process"})
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class SendEmailProcess implements WorkflowProcess {

  private static final Logger LOG = LoggerFactory.getLogger(SendEmailProcess.class);
  @Reference private UserGroupActions userGroupActions;
  @Reference private EmailService emailService;
  @Reference private MSMUserAccess msmUserAccess;
  @Reference private ResourceResolverFactory resolverFactory;

  @Reference(target = "(component.name=org.apache.sling.i18n.impl.JcrResourceBundleProvider)")
  private ResourceBundleProvider resourceBundleProvider;

  @Reference private KPMGGlobal globalSystemSettings;
  @Reference private ResourceResolverFactory resourceFactory;
  @Reference EmailTemplates emailTemplatesConfig;

  /**
   * This method is used to send an email.
   *
   * @param workItem
   * @param workflowSession
   * @param metaDataMap
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Session session = resourceResolver.adaptTo(Session.class);

      final String reviewerGroup =
          WorkflowUtility.getPropertyValueFromHistory(
              workItem, session, WorkflowConstants.REVIEW_USER);
      final String arguments = metaDataMap.get(WorkflowConstants.PROCESS_ARGS, String.class);

      sendEmail(arguments, reviewerGroup, workItem, workflowSession, resourceResolver);

    } catch (RepositoryException | LoginException e) {
      LOG.error("Exception in send email process : ", e);
    }
  }

  private List<String> getUsers(final WorkItem workItem, ResourceResolver resourceResolver)
      throws RepositoryException {

    final Session session = resourceResolver.adaptTo(Session.class);

    final String reviewerGroups =
        WorkflowUtility.getPropertyValueFromHistory(
            workItem, session, WorkflowConstants.REVIEWER_GROUP);
    final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
    final Authorizable authorizable = userManager.getAuthorizable(reviewerGroups);

    if (authorizable == null) {
      return Collections.emptyList();
    }

    final List<String> usersList = new ArrayList<>();

    final Iterator<Authorizable> groupAuthrizableIterator =
        ((Group) authorizable).getDeclaredMembers();

    while (groupAuthrizableIterator.hasNext()) {
      final Authorizable auth = groupAuthrizableIterator.next();
      if (auth.isGroup()) {
        continue;
      }
      final String userProfilePath =
          userManager.getAuthorizable(auth.getID()).getPath() + "/profile";
      final Node userProfilePathNode = session.getNode(userProfilePath);
      if (userProfilePathNode.hasProperty("email")) {
        usersList.add(userProfilePathNode.getProperty("email").getString());
      }
    }
    return usersList;
  }

  private void handleLiveCopyEmails(
      String arguments,
      WorkItem workItem,
      ResourceBundle resourceBundle,
      ResourceResolver resourceResolver)
      throws RepositoryException {

    final String payloadPath = workItem.getContentPath();

    final Session session = resourceResolver.adaptTo(Session.class);
    final Node jcrPayloadNode =
        session.getNode(payloadPath + WorkflowConstants.SLASH + JcrConstants.JCR_CONTENT);

    boolean isLiveCopy = false;
    if (jcrPayloadNode.hasProperty(JcrConstants.JCR_MIXINTYPES)) {
      final javax.jcr.Property property = jcrPayloadNode.getProperty(JcrConstants.JCR_MIXINTYPES);
      isLiveCopy = hasLiveRelationship(property);
    }

    if ((isLiveCopy && StringUtils.equalsIgnoreCase("approved-activated-livecopy", arguments))
        || (!isLiveCopy
            && StringUtils.equalsIgnoreCase("approved-activated-language-copy", arguments))) {
      /*
       * Live copy email notification. Check if it is a live copy and trigger an email to live copy content
       * authors
       */
      final Page payloadPage = WorkflowUtility.getPayloadPage(resourceResolver, payloadPath);

      final String lastModifiedByUser = payloadPage.getLastModifiedBy();

      sendApprovedActivatedEmailToLiveCopyAuthors(workItem, resourceBundle, lastModifiedByUser);
    }

    if ((isLiveCopy && StringUtils.equalsIgnoreCase("activated-livecopy", arguments))
        || (!isLiveCopy && StringUtils.equalsIgnoreCase("activated-language-copy", arguments))) {
      /*
       * Live copy email notification. Check if it is a live copy and trigger an email to live copy content
       * authors
       */
      final String[] pathArray = payloadPath.split(WorkflowConstants.SLASH);

      final String countryCodeAuthorGroup = pathArray[3] + "-content-authors";

      sendActivatedEmailToLiveCopyAuthors(workItem, resourceBundle, countryCodeAuthorGroup);
    }
  }

  private String getPagePropertyValue(WorkItem workItem, Session session, String propertyName)
      throws RepositoryException {
    final String jcrContentPath =
        workItem.getContentPath() + WorkflowConstants.SLASH + JcrConstants.JCR_CONTENT;
    final Node jcrContentNode = session.getNode(jcrContentPath);

    return getPropertyValue(jcrContentNode, propertyName);
  }

  private ResourceBundle getResourceBundle(WorkItem workItem, ResourceResolver resourceResolver) {

    final String payloadPath = workItem.getContentPath();

    final Page payloadPage = WorkflowUtility.getPayloadPage(resourceResolver, payloadPath);
    final Locale locale = payloadPage.getLanguage(false);
    return resourceBundleProvider.getResourceBundle(locale);
  }

  private void updateWorkflowTitle(
      WorkItem workItem, WorkflowSession workflowSession, String workflowTitle) {
    final WorkflowData workflowData = workItem.getWorkflowData();
    workflowData.getMetaDataMap().put(WorkflowConstants.WORKFLOW_TITLE, workflowTitle);
    workflowSession.updateWorkflowData(workItem.getWorkflow(), workflowData);
  }

  private void sendEmail(
      String arguments,
      String reviewerGroup,
      WorkItem workItem,
      WorkflowSession workflowSession,
      ResourceResolver resourceResolver)
      throws RepositoryException {

    final ResourceBundle resourceBundle = getResourceBundle(workItem, resourceResolver);

    if (StringUtils.equalsIgnoreCase("approval-required-email", arguments)) {
      final List<String> usersList = getUsers(workItem, resourceResolver);
      if (StringUtils.isNotEmpty(reviewerGroup) || !usersList.isEmpty()) {
        sendEmailToReviewers(workItem, reviewerGroup, resourceBundle, usersList);

        updateWorkflowTitle(
            workItem, workflowSession, resourceBundle.getString("kpmg.workflow.approvalRequired"));
      }
    } else if ("approve-activate-blueprint-owner-email".equals(arguments)) {
      sendEmailToBluePrintOwner(workItem, resourceResolver);
      updateWorkflowTitle(
          workItem, workflowSession, resourceBundle.getString("kpmg.workflow.manualRollout"));
    } else if (StringUtils.equalsIgnoreCase("approved-activated", arguments)) {
      sendApprovedActivatedEmail(workItem, resourceBundle);
    }

    handleLiveCopyEmails(arguments, workItem, resourceBundle, resourceResolver);

    if (StringUtils.equalsIgnoreCase("approved-not-activated", arguments)) {
      sendApprovedNotActivatedEmail(workItem, resourceBundle);
    } else if ("page-expired".equals(arguments)) {
      sendScheduleOffTimeExpiredEmail(workItem, resourceBundle);
    } else if ("scheduled-for-activation".equals(arguments)) {
      sendScheduledActivationEmail(workItem, resourceResolver);
    } else if ("page-date-missing".equals(arguments)) {
      sendDateMail(workItem, resourceResolver);
    } else if ("activate-blueprint-owner-email".equals(arguments)) {
      manualRolloutEmailToBluePrintOwner(workItem, resourceResolver);
      updateWorkflowTitle(
          workItem,
          workflowSession,
          resourceBundle.getString("kpmg.workflow.activate.manualRollout"));
    } else if (WorkflowConstants.ACTIVATED.equals(arguments)) {
      sendActivated(workItem, resourceBundle);
    } else if (StringUtils.equalsIgnoreCase("not-activated", arguments)) {
      sendNotActivated(workItem, resourceBundle);
    }
  }

  private void sendDateMail(WorkItem workItem, ResourceResolver resourceResolver)
      throws RepositoryException {
    final Session session = resourceResolver.adaptTo(Session.class);
    final String payloadPath = workItem.getContentPath();
    final Node jcrContentNode =
        session.getNode(payloadPath + WorkflowConstants.SLASH + JcrConstants.JCR_CONTENT);

    final ResourceBundle resourceBundle = getResourceBundle(workItem, resourceResolver);

    final String offTime = getPropertyValue(jcrContentNode, NameConstants.PN_OFF_TIME);
    if (StringUtils.isNotBlank(offTime)) {
      // If date exists then issue is with the activation permission
      sendActivatePermissionDeniedEmail(workItem, resourceBundle);
    } else {
      sendONorOFFDateMissingEmail(workItem, resourceBundle);
    }
  }

  /**
   * Send an email notification to reviewer group for approval
   *
   * @param workItem
   * @param reviewUser
   * @param resourceBundle
   * @param usersList
   */
  private void sendEmailToReviewers(
      final WorkItem workItem,
      final String reviewUser,
      final ResourceBundle resourceBundle,
      final List<String> usersList) {
    final String[] emailTo;
    if (!usersList.isEmpty()) {
      final String[] arr = new String[usersList.size()];
      emailTo = usersList.toArray(arr);
    } else {
      final String email = userGroupActions.getEmail(reviewUser);
      emailTo = new String[] {email};
    }
    if (emailTo.length <= 0) {
      return;
    }

    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, false);
    mailTokens.put("approvalRequired", resourceBundle.getString("kpmg.workflow.approvalRequired"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvalRequired.message1"));
    mailTokens.put(
        WorkflowConstants.MESSAGE2,
        resourceBundle.getString("kpmg.workflow.approvalRequired.message2"));
    mailTokens.put(
        WorkflowConstants.MESSAGE3,
        resourceBundle.getString("kpmg.workflow.approvalRequired.message3"));
    mailTokens.put("initiator", workItem.getWorkflow().getInitiator());
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(WorkflowConstants.APPROVAL_REQUIRED);
    try {
      emailService.sendEmailWithTextTemplate(emailTemplate, mailTokens, emailTo, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email to reviewers : ", e);
    }
  }

  /**
   * Approve activate work flow.
   *
   * <p>Send an email notification to perform manual roll-out to country specific content authors
   *
   * @param workItem
   * @param resourceResolver
   * @throws RepositoryException
   */
  private void sendEmailToBluePrintOwner(final WorkItem workItem, ResourceResolver resourceResolver)
      throws RepositoryException {
    final String pageOwner =
        getPagePropertyValue(
            workItem, resourceResolver.adaptTo(Session.class), JcrConstants.JCR_CREATED_BY);

    final String email = userGroupActions.getEmail(pageOwner);
    if (StringUtils.isEmpty(email)) {
      return;
    }

    final ResourceBundle resourceBundle = getResourceBundle(workItem, resourceResolver);

    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put("manualRollout", resourceBundle.getString("kpmg.workflow.manualRollout"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.manualRollout.message1"));
    mailTokens.put(
        WorkflowConstants.MESSAGE2,
        resourceBundle.getString("kpmg.workflow.manualRollout.message2"));
    mailTokens.put(
        WorkflowConstants.MESSAGE3,
        resourceBundle.getString("kpmg.workflow.manualRollout.message3"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.APPROVAL_ROLLOUT_BLUE_PRINT_OWNERS);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email to BP Owner : ", e);
    }
  }

  /**
   * Activate work flow.
   *
   * <p>Send an email notification to perform manual roll-out to country specific content authors
   *
   * @param workItem
   * @param resourceResolver
   * @throws RepositoryException
   */
  private void manualRolloutEmailToBluePrintOwner(
      final WorkItem workItem, ResourceResolver resourceResolver) throws RepositoryException {

    final String pageOwner =
        getPagePropertyValue(
            workItem, resourceResolver.adaptTo(Session.class), JcrConstants.JCR_CREATED_BY);

    final String email = userGroupActions.getEmail(pageOwner);
    if (!StringUtils.isNoneEmpty(email)) {
      return;
    }

    final ResourceBundle resourceBundle = getResourceBundle(workItem, resourceResolver);

    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        "manualRollout", resourceBundle.getString("kpmg.workflow.activate.manualRollout"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.activate.manualRollout.message1"));
    mailTokens.put(
        WorkflowConstants.MESSAGE2,
        resourceBundle.getString("kpmg.workflow.manualRollout.message2"));
    mailTokens.put(
        WorkflowConstants.MESSAGE3,
        resourceBundle.getString("kpmg.workflow.manualRollout.message3"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.ROLLOUT_FOR_BLLUE_PRINT_OWNERS_FOR_ACTIVATION_WORKFLOW);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email to BP Owner for manual rollout : ", e);
    }
  }

  /**
   * Send approved and activated email to initiator
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendApprovedActivatedEmail(
      final WorkItem workItem, final ResourceBundle resourceBundle) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      return;
    }
    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        "approvedActivated", resourceBundle.getString("kpmg.workflow.approvedActivated"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvedActivated.message1"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.APPROVED_ACTIVATED_FOR_INITIATOR);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Send Email Process :  {} | {} ", e, e.getMessage());
    }
  }

  /**
   * Send approved and activated email to content authors if the page is a live copy
   *
   * @param workItem
   * @param resourceBundle
   * @param lastModifiedByUser
   */
  private void sendApprovedActivatedEmailToLiveCopyAuthors(
      final WorkItem workItem,
      final ResourceBundle resourceBundle,
      final String lastModifiedByUser) {
    final Set<GroupInfo> groups = userGroupActions.getGroupsForUser(lastModifiedByUser, false);
    final Set<UserInfo> users = userGroupActions.getUsersForGroups(groups, GroupInfo.class);
    final Set<String> emails = userGroupActions.getEmailIds(users, UserInfo.class);
    if (emails.isEmpty()) {
      return;
    }
    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        "approvedActivated", resourceBundle.getString("kpmg.workflow.approvedActivated"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvedActivated.message1"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.APPROVED_ACTIVATED_FOR_INITIATOR);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, emails.toArray(new String[emails.size()]), new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email to live copy authors : ", e);
    }
  }

  /**
   * Send approval complete - page not activated email notification to initiator
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendApprovedNotActivatedEmail(
      final WorkItem workItem, final ResourceBundle resourceBundle) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      return;
    }

    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        "approvedNotActivated", resourceBundle.getString("kpmg.workflow.approvedNotActivated"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvedNotActivated.message1"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(WorkflowConstants.APPROVED_NOT_ACTIVATED);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendApprovedNotActivatedEmail  : ", e);
    }
  }

  /**
   * Send Scheduled off time expired email to initiator
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendScheduleOffTimeExpiredEmail(
      final WorkItem workItem, final ResourceBundle resourceBundle) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      return;
    }

    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        WorkflowConstants.ACTIVATION_COMPLETE,
        resourceBundle.getString("kpmg.workflow.approvedScheduleTimeExpired"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvedScheduleTimeExpired.message1"));
    mailTokens.put(
        WorkflowConstants.MESSAGE2,
        resourceBundle.getString("kpmg.workflow.approvedScheduleTimeExpired.message2"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(WorkflowConstants.PAGE_EXPIRED);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendScheduleOffTimeExpiredEmail  : ", e);
    }
  }

  /**
   * Send email to initiator if the activation permission is missing
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendActivatePermissionDeniedEmail(
      final WorkItem workItem, final ResourceBundle resourceBundle) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      LOG.error("Initiator {} email is empty:", initiator);
      return;
    }
    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, false);
    mailTokens.put(
        WorkflowConstants.ACTIVATION_COMPLETE,
        resourceBundle.getString("kpmg.workflow.activatePermissionDenied"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.activatePermissionDenied.message1"));
    mailTokens.put(
        "checkDateORActivationPermissionMessage",
        resourceBundle.getString("kpmg.workflow.activatePermissionDenied.message2"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.PAGE_DATE_MISSING_ACTIVIATION_PERMISSION);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendActivatePermissionDeniedEmail  : ", e);
    }
  }

  /**
   * Send ON/OFF date missing email to initiator
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendONorOFFDateMissingEmail(
      final WorkItem workItem, final ResourceBundle resourceBundle) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      LOG.error("Initiator {} email is empty :", initiator);
      return;
    }

    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, false);
    mailTokens.put(
        WorkflowConstants.ACTIVATION_COMPLETE,
        resourceBundle.getString("kpmg.workflow.ScheduleTimeMissing"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvedScheduleTimeExpired.message1"));
    mailTokens.put(
        "checkDateORActivationPermissionMessage",
        resourceBundle.getString("kpmg.workflow.ScheduleTimeMissing.message"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.PAGE_DATE_MISSING_ACTIVIATION_PERMISSION);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendONorOFFDateMissingEmail  : ", e);
    }
  }

  /**
   * Send Scheduled activation email to initiator
   *
   * @param workItem
   * @param resourceResolver
   * @throws RepositoryException
   */
  private void sendScheduledActivationEmail(
      final WorkItem workItem, final ResourceResolver resourceResolver) throws RepositoryException {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      LOG.error("Initiator {} email is empty :", initiator);
    }

    final String schdOnTime =
        getPagePropertyValue(
            workItem, resourceResolver.adaptTo(Session.class), NameConstants.PN_ON_TIME);

    final ResourceBundle resourceBundle = getResourceBundle(workItem, resourceResolver);
    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        "approvedScheduledActivation",
        resourceBundle.getString("kpmg.workflow.approvedScheduledActivation"));
    mailTokens.put(
        WorkflowConstants.MODEL_TITLE, workItem.getWorkflow().getWorkflowModel().getTitle());
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvedScheduledActivation.message1"));
    mailTokens.put(
        WorkflowConstants.MESSAGE2,
        resourceBundle.getString("kpmg.workflow.approvedScheduledActivation.message2"));
    mailTokens.put(NameConstants.PN_ON_TIME, schdOnTime);
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.APPROVED_SCHEDULED_FOR_ACTIVATION);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendScheduledActivationEmail  : ", e);
    }
  }

  /**
   * Send activated email notification to initiator
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendActivated(final WorkItem workItem, final ResourceBundle resourceBundle) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      LOG.error("Initiator {} email is empty : ", initiator);
    }
    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        WorkflowConstants.ACTIVATED,
        resourceBundle.getString("kpmg.workflow.activate.pageActivated"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.activate.pageActivated.message1"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(WorkflowConstants.ACTIVATED_EMAIL_FOR_INITIATOR);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendActivated  : ", e);
    }
  }

  /**
   * Send activated email notification to live copy content authors
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendActivatedEmailToLiveCopyAuthors(
      final WorkItem workItem,
      final ResourceBundle resourceBundle,
      final String countryCodeAuthorGroup) {
    final Set<UserInfo> users = userGroupActions.getUsersForGroup(countryCodeAuthorGroup);
    final Set<String> emails = userGroupActions.getEmailIds(users, UserInfo.class);
    if (emails.isEmpty()) {
      LOG.error("LiveCopyAuthors email is empty");
      return;
    }
    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        WorkflowConstants.ACTIVATED,
        resourceBundle.getString("kpmg.workflow.activate.pageActivated"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.activate.pageActivated.message1"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(WorkflowConstants.ACTIVATED_EMAIL_FOR_INITIATOR);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, emails.toArray(new String[emails.size()]), new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendActivatedEmailToLiveCopyAuthors  : ", e);
    }
  }

  /**
   * Send page is not activated email notification to initiator
   *
   * @param workItem
   * @param resourceBundle
   */
  private void sendNotActivated(final WorkItem workItem, final ResourceBundle resourceBundle) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final String email = userGroupActions.getEmail(initiator);
    if (StringUtils.isEmpty(email)) {
      LOG.error("Initiator {} email is empty : ", initiator);
      return;
    }

    final Map<String, Object> mailTokens = getGenericMailToken(resourceBundle, workItem, true);
    mailTokens.put(
        "notActivated", resourceBundle.getString("kpmg.workflow.activate.pageNotActivated"));
    mailTokens.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.activate.pageNotActivated.message1"));
    final String emailTemplate =
        emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.NOT_ACTIVATED_EMAIL_FOR_INITIATOR);
    try {
      emailService.sendEmailWithTextTemplate(
          emailTemplate, mailTokens, new String[] {email}, new String[] {});
    } catch (final EmailException e) {
      LOG.error("Error occured while sending email for sendNotActivated  : ", e);
    }
  }

  /**
   * @param property
   * @return
   * @throws ValueFormatException
   * @throws RepositoryException
   */
  private boolean hasLiveRelationship(final Property property) throws RepositoryException {
    boolean isPresent = false;
    final Value[] values = property.getValues();
    for (final Value value : values) {
      final String liveRelationShip = value.getString();
      if (WorkflowConstants.CQ_LIVE_RELATIONSHIP.equals(liveRelationShip)) {
        isPresent = true;
        break;
      }
    }
    return isPresent;
  }

  /**
   * Gets the property.
   *
   * @param node the node
   * @param property the property
   * @return the property value
   */
  private String getPropertyValue(final Node node, final String property) {
    try {
      return node.hasProperty(property)
          ? node.getProperty(property).getString()
          : StringUtils.EMPTY;
    } catch (final RepositoryException e) {
      LOG.error("Repository Exception occured while fething the property {}", property, e);
    }
    return StringUtils.EMPTY;
  }

  /**
   * Gets the generic mail token.
   *
   * @param resourceBundle the resource bundle
   * @param workItem the work item
   * @param includeTitle the include title
   * @return the generic mail token
   */
  private Map<String, Object> getGenericMailToken(
      final ResourceBundle resourceBundle, final WorkItem workItem, final boolean includeTitle) {
    final Map<String, Object> mailTokens = new HashMap<>();
    final WorkflowData workflowData = workItem.getWorkflowData();
    final String hostPrefix = globalSystemSettings.getDomainName();

    mailTokens.put(WorkflowConstants.PAYLOAD, workflowData.getPayload().toString());
    mailTokens.put(WorkflowConstants.HOST_PREFIX, hostPrefix);
    if (includeTitle) {
      mailTokens.put(
          WorkflowConstants.MODEL_TITLE, workItem.getWorkflow().getWorkflowModel().getTitle());
    }
    mailTokens.put(
        WorkflowConstants.AUTO_GENERATEDTEXT,
        resourceBundle.getString(WorkflowConstants.KPMG_WORKFLOW_AUTOGENERATE));
    mailTokens.put(
        WorkflowConstants.VIEWINBOX,
        resourceBundle.getString(WorkflowConstants.KPMG_WORKFLOW_VIEWINBOX));
    return mailTokens;
  }
}
