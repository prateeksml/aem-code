package com.kpmg.core.workflow.deactivate;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.event.WorkflowEvent;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
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
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to get the approver group dynamically. The work item will be assigned to the
 * approver group.
 */
@Component(
    service = ParticipantStepChooser.class,
    property = {WorkflowConstants.CHOOSER_LABEL + "=" + "KPMG Dynamic approver chooser process"})
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class WorkflowStepAssigne implements ParticipantStepChooser {

  private static final Logger LOG = LoggerFactory.getLogger(WorkflowStepAssigne.class);

  @Reference private ResourceResolverFactory resolverFactory;

  @Reference UserGroupActions userGroups;

  @Reference EmailService emailService;

  @Reference EmailTemplates emailTemplatesConfig;

  @Reference(target = "(component.name=org.apache.sling.i18n.impl.JcrResourceBundleProvider)")
  private ResourceBundleProvider resourceBundleProvider;

  @Reference private KPMGGlobal globalSystemSettings;

  /**
   * The method will get the deactivation approver group based on the pay load and assigns the
   * workflow to the group.
   *
   * @param workItem
   * @param workflowSession
   * @param metaDataMap
   * @throws WorkflowException
   */
  @Override
  public String getParticipant(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final String payload = workItem.getContentPath();
      final Session session = resolver.adaptTo(Session.class);
      if (resolver.getResource(payload) != null) {
        final Page page = WorkflowUtility.getPayloadPage(resolver, payload);
        final ResourceBundle resBundle =
            resourceBundleProvider.getResourceBundle(page.getLanguage(false));
        final String wfhPath = workItem.getWorkflow().getId() + "/history";
        final Node wfhNode = session.getNode(wfhPath);
        final NodeIterator itr = wfhNode.getNodes();
        final String approverGroup =
            WorkflowConstants.AEM_GROUPS_PREFIX
                + payload.split(WorkflowConstants.SLASH)[3]
                + "-livecopy-admin";
        String userComment = StringUtils.EMPTY;
        while (itr.hasNext()) {
          final Node childNode = itr.nextNode();
          final String wihMetaData = childNode.getPath() + "/workItem/metaData";
          if (childNode.hasProperty("event")
              && StringUtils.contains(
                  childNode.getProperty("event").getString(),
                  WorkflowEvent.WORKFLOW_COMPLETED_EVENT)) {
            final Node mdNode = session.getNode(wihMetaData);
            if (mdNode.hasProperty("comment")) {
              userComment = "" + mdNode.getProperty("comment").getValue();
            }
          }
        }
        final WorkflowData workflowData = workItem.getWorkflowData();
        workflowData
            .getMetaDataMap()
            .put("workflowTitle", resBundle.getString("kpmg.workflow.deactivateApprovalRequired"));
        workflowData.getMetaDataMap().put("startComment", userComment);
        workflowSession.updateWorkflowData(workItem.getWorkflow(), workflowData);
        notifyApprover(approverGroup, page, resBundle, workItem);
        return approverGroup;
      }
    } catch (RepositoryException e) {
      LOG.error("RepositoryException in WorkflowStepAssigne workflow : ", e);
    } catch (LoginException le) {
      LOG.error("LoginException in WorkflowStepAssigne workflow : ", le);
    }
    return WorkflowConstants.ADMINISTRATORS;
  }

  /**
   * This method will send an email notification to the approver group with the page details.
   *
   * @param approverGroup
   * @param initiator
   * @param payload
   */
  public void notifyApprover(
      final String approverGroup,
      final Page payloadPage,
      final ResourceBundle resourceBundle,
      final WorkItem workItem) {

    final String hostPrefix = globalSystemSettings.getDomainName();
    final Set<UserInfo> emailIds = userGroups.getUsersForGroup(approverGroup);
    final Set<String> emails = new HashSet<>();
    final Map<String, Object> pageInfo = new HashMap<>();
    emailIds.stream()
        .filter(
            (final UserInfo group) ->
                group.getEmail() != null && StringUtils.isNotBlank(group.getEmail()))
        .forEach((final UserInfo group) -> emails.add(group.getEmail()));
    pageInfo.put(
        WorkflowConstants.SUBJECT,
        resourceBundle.getString("kpmg.workflow.deactivateApprovalRequired"));
    pageInfo.put(
        WorkflowConstants.MESSAGE1,
        resourceBundle.getString("kpmg.workflow.approvalRequired.message1"));
    pageInfo.put(
        WorkflowConstants.MESSAGE2,
        resourceBundle.getString("kpmg.workflow.approvalRequired.message4"));
    pageInfo.put(
        WorkflowConstants.MESSAGE3,
        resourceBundle.getString("kpmg.workflow.approvalRequired.message3"));
    pageInfo.put(WorkflowConstants.WORKFLOW_INITIATOR, workItem.getWorkflow().getInitiator());
    pageInfo.put(WorkflowConstants.PAYLOAD, payloadPage.getPath());
    pageInfo.put(WorkflowConstants.HOST_PREFIX, hostPrefix);
    pageInfo.put(
        WorkflowConstants.MODEL_TITLE, workItem.getWorkflow().getWorkflowModel().getTitle());
    pageInfo.put(
        WorkflowConstants.AUTOGENERATE,
        resourceBundle.getString("kpmg.workflow.autoGeneratedText"));
    pageInfo.put(
        WorkflowConstants.VIEWINBOX, resourceBundle.getString("kpmg.workflow.viewInboxText"));
    if (!emails.isEmpty()) {
      try {
        emailService.sendEmailWithTextTemplate(
            emailTemplatesConfig.getEmailTemplatePath(
                WorkflowConstants.DEACTIVATION_APPROVER_NOTIFICATION),
            pageInfo,
            emails.toArray(new String[emails.size()]),
            new String[] {});
      } catch (EmailException e) {
        LOG.error("Error occured while sending the emails : ", e);
      }
    }
  }
}
