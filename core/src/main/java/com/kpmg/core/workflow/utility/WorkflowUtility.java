package com.kpmg.core.workflow.utility;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.event.WorkflowEvent;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This is a utility class for workflow process steps. */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class WorkflowUtility {
  private static final Logger LOG = LoggerFactory.getLogger(WorkflowUtility.class);

  private WorkflowUtility() {}

  /**
   * This method is used to get property values from metadata for workitem history.
   *
   * @param workItem
   * @param session
   * @param propertyName
   * @return
   */
  public static String getPropertyValueFromHistory(
      final WorkItem workItem, final Session session, final String propertyName) {
    try {
      final String wfhPath = workItem.getWorkflow().getId() + "/history";
      final Node wfhNode = session.getNode(wfhPath);
      final NodeIterator itr = wfhNode.getNodes();
      final List<String> confirmActivationList = new ArrayList<>();
      while (itr.hasNext()) {
        final Node childNode = itr.nextNode();
        final String wihMetaData = childNode.getPath() + "/workItem/metaData";
        if (childNode.hasProperty("event")
            && StringUtils.contains(
                childNode.getProperty("event").getString(),
                WorkflowEvent.WORKFLOW_COMPLETED_EVENT)) {
          final Node mdNode = session.getNode(wihMetaData);
          if (mdNode.hasProperty(propertyName)) {
            String propertyValue = "";
            if (mdNode.hasProperty(propertyName)) {
              propertyValue = mdNode.getProperty(propertyName).getString();
            }
            confirmActivationList.add(propertyValue);
          }
        }
      }
      if (!confirmActivationList.isEmpty()) {
        return confirmActivationList.get(confirmActivationList.size() - 1);
      }
    } catch (PathNotFoundException e) {
      LOG.error("PathNotFoundException has occured ", e);
    } catch (RepositoryException e) {
      LOG.error("RepositoryException has occured: ", e);
    }
    return StringUtils.EMPTY;
  }

  /**
   * Terminate work flow.
   *
   * @param workflowSession the workflow session
   * @param workItem the work item
   */
  public static void terminateWorkFlow(
      final WorkflowSession workflowSession, final WorkItem workItem) {
    try {
      final Workflow workflow = workflowSession.getWorkflow(workItem.getWorkflow().getId());
      workflowSession.terminateWorkflow(workflow);
    } catch (WorkflowException e) {
      LOG.error("error ocurred while terminating the workflow", e);
    }
  }

  /**
   * Gets the payload page.
   *
   * @param subServiceResolver the sub service resolver
   * @param workItem the work item
   * @return the payload page
   */
  public static Page getPayloadPage(
      final ResourceResolver subServiceResolver, final String payloadPath) {
    final Resource pageResource = subServiceResolver.getResource(payloadPath);
    return pageResource != null ? pageResource.adaptTo(Page.class) : null;
  }

  /**
   * Checks if is activated.
   *
   * @param replicator the replicator
   * @param page the page
   * @param session the session
   * @return true, if is activated
   */
  public static boolean isActivated(
      final Replicator replicator, final String path, final Session session) {
    if (path != null) {
      return replicator.getReplicationStatus(session, path).isActivated();
    }
    return false;
  }
}
