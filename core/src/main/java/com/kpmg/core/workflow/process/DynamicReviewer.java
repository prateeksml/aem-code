package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
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

/** This class is used for dynamic reviewer step. */
@Component(
    service = ParticipantStepChooser.class,
    property = {
      WorkflowConstants.CHOOSER_LABEL + "=" + "Dynamic Reviewer Participant Chooser Process"
    })
@ServiceDescription("Dynamic Reviewer chooser to get initiator.")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class DynamicReviewer implements ParticipantStepChooser {

  private static final Logger LOG = LoggerFactory.getLogger(DynamicReviewer.class);

  @Reference ResourceResolverFactory resolverFactory;

  @Override
  public String getParticipant(
      WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
      throws WorkflowException {
    Session session = null;
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver(
            KPMGSubservicesEnum.WORKFLOWS_ROLLOUT.returnVal(), resolverFactory)) {
      session = resourceResolver.adaptTo(Session.class);
      final String reviewerGroup =
          WorkflowUtility.getPropertyValueFromHistory(
              workItem, session, WorkflowConstants.REVIEWER_GROUP);
      if (StringUtils.isNotBlank(reviewerGroup)) {
        final UserManager userManager = AccessControlUtil.getUserManager(session);
        final Authorizable authorizable = userManager.getAuthorizable(reviewerGroup);
        if (null != authorizable) {
          return reviewerGroup;
        }
      }
    } catch (RepositoryException | LoginException e) {
      LOG.error("Repository error occured", e);
    }
    return WorkflowConstants.ADMINISTRATORS;
  }
}
