package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.event.WorkflowEvent;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class selects the route based on the activation check from the previous step */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Confirm On Activation Check"})
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class ConfirmOnActivationCheckProcess implements WorkflowProcess {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConfirmOnActivationCheckProcess.class);

  @Reference ResourceResolverFactory resolverFactory;

  /**
   * This method selects the route based the workflow history activation check property.
   *
   * @param WorkItem
   * @param WorkflowSession
   * @param MetaDataMap
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Session session = resourceResolver.adaptTo(Session.class);
      final String booleanValue = getActivationCheckValue(workItem, session);
      if (StringUtils.isNotEmpty(booleanValue)) {
        final Boolean activationCheck = Boolean.parseBoolean(booleanValue);
        final List<Route> routes = workflowSession.getRoutes(workItem, false);
        if (Boolean.TRUE.equals(activationCheck)) {
          workflowSession.complete(workItem, routes.get(0));
        } else {
          workflowSession.complete(workItem, routes.get(1));
        }
      }
    } catch (LoginException e) {
      LOGGER.error("A LoginException has occured", e);
    }
  }

  /**
   * This method will search the workflow instance history nodes to retrieve the last confirm
   * activation check
   *
   * @param workItem
   * @param session
   * @return a boolean value, if confirm activation is checked returns true, else returns false
   */
  private String getActivationCheckValue(final WorkItem workItem, final Session session) {
    try {
      final List<String> confirmActivationList = new ArrayList<>();
      final String wfhPath = workItem.getWorkflow().getId() + "/history";
      final Node wfhNode = session.getNode(wfhPath);
      final NodeIterator itr = wfhNode.getNodes();

      while (itr.hasNext()) {
        final Node childNode = itr.nextNode();
        final String wihMetaData = childNode.getPath() + "/workItem/metaData";
        if (childNode.hasProperty("event")
            && StringUtils.contains(
                childNode.getProperty("event").getString(),
                WorkflowEvent.WORKFLOW_COMPLETED_EVENT)) {
          final Node mdNode = session.getNode(wihMetaData);
          String flag = "false";
          boolean addToListFlag = false;
          if (mdNode.hasProperty("reviewedBy")
              && mdNode.hasProperty(WorkflowConstants.CONFIRM_ACTIVATION)) {
            addToListFlag = true;
            flag = mdNode.getProperty(WorkflowConstants.CONFIRM_ACTIVATION).getString();
          }
          if (mdNode.hasProperty("confirmActivationText")
              && mdNode.hasProperty(WorkflowConstants.CONFIRM_ACTIVATION)) {
            addToListFlag = true;
            flag = mdNode.getProperty(WorkflowConstants.CONFIRM_ACTIVATION).getString();
          }
          if (addToListFlag) {
            confirmActivationList.add(flag);
          }
        }
      }
      if (!confirmActivationList.isEmpty()) {
        return confirmActivationList.get(confirmActivationList.size() - 1);
      }
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException occured while getting the activation check value: ", e);
    }
    return "false";
  }
}
