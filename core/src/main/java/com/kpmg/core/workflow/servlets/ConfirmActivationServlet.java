package com.kpmg.core.workflow.servlets;

import com.adobe.granite.workflow.event.WorkflowEvent;
import com.day.cq.wcm.workflow.api.WcmWorkflowService;
import com.day.cq.workflow.exec.Workflow;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.userpermission.MSMUserAccess;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfirmActivationService Check if page is blueprint and also checks if activation check is
 * enabled in work flow
 */
@Component(
    service = Servlet.class,
    property = {
      Constants.SERVICE_DESCRIPTION + "=KPMG - ConfirmActivationServlet",
      "sling.servlet.methods=" + HttpConstants.METHOD_GET,
      "sling.servlet.resourceTypes=kpmg/components/structure/page",
      "sling.servlet.extensions=json",
      "sling.servlet.selectors=confirmactivation"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class ConfirmActivationServlet extends SlingAllMethodsServlet {

  private static final long serialVersionUID = 8392443949L;

  private static final Logger LOG = LoggerFactory.getLogger(ConfirmActivationServlet.class);
  @Reference private transient MSMUserAccess msmUserAccess;
  @Reference private transient WcmWorkflowService wcmWorkflowService;

  @Override
  protected void doGet(
      final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

    final String pagePath = request.getParameter("path");
    final String activation = request.getParameter("activationCheck");
    final String bluePrint = request.getParameter("blueprintcheck");
    if (!StringUtils.isNotBlank(pagePath)) {
      return;
    }
    if (StringUtils.isNotBlank(bluePrint) && Boolean.TRUE.equals(Boolean.valueOf(bluePrint))) {
      writeBlueprintResponse(pagePath, response);
    }
    if (StringUtils.isNotBlank(activation) && Boolean.TRUE.equals(Boolean.valueOf(activation))) {
      writeActivationResponse(pagePath, request, response);
    }
  }

  /**
   * Return a activation check list from the work flow history node.
   *
   * @param workflowHistoryNode the workflow history node
   * @param resolver the resolver
   * @return the property values from history
   */
  private List<String> getPropertyValuesFromHistory(
      final Node workflowHistoryNode, final ResourceResolver resolver) {
    final List<String> activationCheckList = new ArrayList<>();
    try {
      getPropertyValues(workflowHistoryNode, resolver, activationCheckList);
    } catch (PathNotFoundException e) {
      LOG.error("PathNotFoundException", e);
    } catch (RepositoryException e) {
      LOG.error("RepositoryException", e);
    }
    return activationCheckList;
  }

  /**
   * Gets the property values.
   *
   * @param workflowHistoryNode the workflow history node
   * @param resolver the resolver
   * @param activationCheckList the activation check list
   * @return the property values
   * @throws RepositoryException the repository exception
   */
  private void getPropertyValues(
      final Node workflowHistoryNode,
      final ResourceResolver resolver,
      final List<String> activationCheckList)
      throws RepositoryException {
    final NodeIterator itr = workflowHistoryNode.getNodes();
    while (itr.hasNext()) {
      final String propertyValue;
      final Node childNode = itr.nextNode();
      if (childNode.hasProperty("event")
          && StringUtils.contains(
              childNode.getProperty("event").getString(), WorkflowEvent.WORKFLOW_COMPLETED_EVENT)) {
        final String wihMetaData = childNode.getPath() + "/workItem/metaData";
        final Resource metaDataresource = resolver.getResource(wihMetaData);
        if (metaDataresource != null) {
          final Node metaDataNode = metaDataresource.adaptTo(Node.class);
          if (metaDataNode != null
              && metaDataNode.hasProperty(WorkflowConstants.REVIEWER_GROUP)
              && metaDataNode.hasProperty(WorkflowConstants.ACTIVATION_ON_COMPLETE)) {
            propertyValue =
                metaDataNode.getProperty(WorkflowConstants.ACTIVATION_ON_COMPLETE).getString();
            activationCheckList.add(propertyValue);
          }
        }
      }
    }
  }

  /**
   * Writes an evaluated response
   *
   * @param pagePath
   * @param response
   */
  private void writeBlueprintResponse(
      final String pagePath, final SlingHttpServletResponse response) {
    try {
      if (msmUserAccess.isBlueprint(pagePath)) {
        response.getWriter().write("true");
      } else {
        response.getWriter().write(WorkflowConstants.FALSE);
      }
    } catch (IOException e) {
      LOG.error("IOException has occured: ", e);
    }
  }

  /**
   * Write an evaluated response
   *
   * @param pagePath
   * @param request
   * @param response
   */
  private void writeActivationResponse(
      final String pagePath,
      final SlingHttpServletRequest request,
      final SlingHttpServletResponse response) {
    try {
      final ResourceResolver resolver = request.getResourceResolver();
      final Workflow workflow = wcmWorkflowService.getWorkflowInstance(pagePath);
      if (null != workflow) {
        final String wfHistoryPath = workflow.getId() + "/history";
        final Resource wfHistory = resolver.getResource(wfHistoryPath);
        if (wfHistory != null) {
          final Node workflowHistoryNode = wfHistory.adaptTo(Node.class);
          final List<String> activationCheckList =
              getPropertyValuesFromHistory(workflowHistoryNode, resolver);
          if (!activationCheckList.isEmpty()) {
            response.getWriter().write(activationCheckList.get(activationCheckList.size() - 1));
          } else {
            response.getWriter().write(WorkflowConstants.FALSE);
          }
        }
      }
    } catch (IOException e) {
      LOG.error("IOException occured while writing the response: ", e);
    }
  }
}
