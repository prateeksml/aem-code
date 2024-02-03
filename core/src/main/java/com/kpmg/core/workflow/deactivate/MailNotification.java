package com.kpmg.core.workflow.deactivate;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.userpermission.UserInfo;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.apache.sling.jcr.base.util.AccessControlUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will send an email notification to initiator of the workflow and site administrators
 * of reference pages.
 *
 * @author kmuth1
 */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Deactivation Mail Notification"})
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class MailNotification implements WorkflowProcess {

  @Reference UserGroupActions userGroups;

  @Reference EmailService emailService;

  @Reference private ResourceResolverFactory resolverFactory;

  private static final Logger LOG = LoggerFactory.getLogger(MailNotification.class);

  @Reference(target = "(component.name=org.apache.sling.i18n.impl.JcrResourceBundleProvider)")
  private ResourceBundleProvider resourceBundleProvider;

  @Reference private KPMGGlobal globalSystemSettings;

  @Reference EmailTemplates emailTemplatesConfig;

  /**
   * This class will send an email notification to initiator of the workflow and site administrators
   * of reference pages.
   *
   * @param workItem
   * @param wfsession
   * @param metaData
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession wfsession, final MetaDataMap metaData)
      throws WorkflowException {
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final String payload = workItem.getContentPath();
      final Page page = WorkflowUtility.getPayloadPage(resourceResolver, payload);
      final Map<String, Object> pageInfo = new HashMap<>();
      final ResourceBundle resourceBundle =
          resourceBundleProvider.getResourceBundle(page.getLanguage(false));
      final String modelTitle = workItem.getWorkflow().getWorkflowModel().getTitle();
      final String hostPrefix = globalSystemSettings.getDomainName();
      pageInfo.put("payload", payload);
      pageInfo.put("host.prefix", hostPrefix);
      pageInfo.put(
          "subjectInitiator", resourceBundle.getString("kpmg.workflow.deactivateSubject2"));
      pageInfo.put(
          "subjectSiteAdmin", resourceBundle.getString("kpmg.workflow.deactivateSubject1"));
      pageInfo.put(
          "messageInitiator", resourceBundle.getString("kpmg.workflow.deactivateMessage2"));
      pageInfo.put(
          "messageSiteAdmin", resourceBundle.getString("kpmg.workflow.deactivateMessage1"));
      pageInfo.put("modelTitle", modelTitle);
      pageInfo.put(
          "autoGeneratedText", resourceBundle.getString("kpmg.workflow.autoGeneratedText"));
      pageInfo.put("viewInboxText", resourceBundle.getString("kpmg.workflow.viewInboxText"));
      final Session session = resourceResolver.adaptTo(Session.class);
      sendInitiatorNotification(workItem, pageInfo, session);
      sendSiteAdminNotification(pageInfo, payload, resourceResolver);
      LOG.debug("Execution of execute() method completed");
    } catch (LoginException e) {
      LOG.error("LoginException in send mail notification process  : ", e);
    }
  }

  /**
   * This method will send an email notification to the site administrators of reference pages.
   *
   * @param pageInfo
   * @param jcrPath
   * @param session
   * @param resourceResolver
   */
  private void sendSiteAdminNotification(
      final Map<String, Object> pageInfo,
      final String jcrPath,
      final ResourceResolver resourceResolver) {
    final Set<String> siteadminEmails = new HashSet<>();
    final Page page = resourceResolver.getResource(jcrPath).adaptTo(Page.class);
    String countryCode = null;
    if (page != null) {
      countryCode = page.getAbsoluteParent(2).getName();
    }
    try {
      Set<UserInfo> emailIds = null;
      emailIds =
          userGroups.getUsersForGroup(
              WorkflowConstants.AEM_GROUPS_PREFIX + countryCode + "-livecopy-admin");
      emailIds.forEach((final UserInfo group) -> siteadminEmails.add(group.getEmail()));
      if (!siteadminEmails.isEmpty()) {
        emailService.sendEmailWithTextTemplate(
            emailTemplatesConfig.getEmailTemplatePath(
                WorkflowConstants.DEACTIVATION_SITEADMIN_NOTIFICATION),
            pageInfo,
            siteadminEmails.toArray(new String[siteadminEmails.size()]),
            new String[] {});
      }
    } catch (EmailException e) {
      LOG.error("Exception in sendSiteAdminNotification : ", e);
    }
  }

  /**
   * This method will send an email notification to the initiator of the workflow.
   *
   * @param workItem
   * @param pageInfo
   * @param session
   */
  private void sendInitiatorNotification(
      final WorkItem workItem, final Map<String, Object> pageInfo, final Session session) {
    final String initiator = workItem.getWorkflow().getInitiator();
    final Set<String> emails = new HashSet<>();
    try {
      final UserManager userManager = AccessControlUtil.getUserManager(session);
      final Authorizable authorizable = userManager.getAuthorizable(initiator);
      if (null != authorizable) {
        final String userid = authorizable.getID();
        final String emailid = userGroups.getEmail(userid);
        if (StringUtils.isNotBlank(emailid)) {
          emails.add(emailid);
        }
      }
      LOG.debug("The initiator email id is{} ", emails);
      if (!emails.isEmpty()) {
        emailService.sendEmailWithTextTemplate(
            emailTemplatesConfig.getEmailTemplatePath(
                WorkflowConstants.DEACTIVATION_INITIATOR_NOTIFICATION),
            pageInfo,
            emails.toArray(new String[emails.size()]),
            new String[] {});
      }
    } catch (EmailException e) {
      LOG.error("EmailException while sending the email : ", e);
    } catch (RepositoryException e) {
      LOG.error("RepositoryException while sending the email : ", e);
    }
  }
}
