package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.KPMGSubservicesEnum;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.base.util.AccessControlUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class is used for dynamic initiator step. */
@Component(
    service = ParticipantStepChooser.class,
    property = {
      WorkflowConstants.CHOOSER_LABEL + "=" + "Dynamic Initiator Participant Chooser Process"
    })
@ServiceDescription("Dynamic participant chooser to get initiator.")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class DynamicInitiator implements ParticipantStepChooser {

  private static final Logger LOG = LoggerFactory.getLogger(DynamicInitiator.class);

  @Reference ResourceResolverFactory resolverFactory;

  @Override
  public String getParticipant(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver(
            KPMGSubservicesEnum.WORKFLOWS_ROLLOUT.returnVal(), resolverFactory)) {
      final String payload = workItem.getContentPath();
      final Page payloadPage = WorkflowUtility.getPayloadPage(resourceResolver, payload);
      final Session session = resourceResolver.adaptTo(Session.class);
      final String initiator = workItem.getWorkflow().getInitiator();
      final String arguments = metaDataMap.get(WorkflowConstants.PROCESS_ARGS, String.class);
      if (StringUtils.isNotBlank(arguments) && WorkflowConstants.CREATEDBY.equals(arguments)) {
        return payloadPage
            .getContentResource()
            .getValueMap()
            .get(JcrConstants.JCR_CREATED_BY, String.class);
      } else if (StringUtils.isNotBlank(arguments)
          && StringUtils.equals(WorkflowConstants.GLOBAL_SITE_ADMINISTRATORS, arguments)) {
        final UserManager userManager = AccessControlUtil.getUserManager(session);
        final Authorizable authorizable = userManager.getAuthorizable(arguments);
        if (null != authorizable) {
          LOG.debug("Authorizable group is {}", authorizable);
          return arguments;
        }
      } else if (StringUtils.isNotBlank(initiator)) {
        return initiator;
      }
    } catch (RepositoryException | LoginException e) {
      LOG.error("A Exception has occured while getting dymanic participant", e);
    }
    return WorkflowConstants.ADMINISTRATORS;
  }
}
