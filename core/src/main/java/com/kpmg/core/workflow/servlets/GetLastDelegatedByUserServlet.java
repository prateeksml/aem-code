package com.kpmg.core.workflow.servlets;

import com.adobe.granite.workflow.event.WorkflowEvent;
import com.day.cq.wcm.workflow.api.WcmWorkflowService;
import com.day.cq.workflow.exec.Workflow;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This servlet is used to get the last delegated by user. */
@Component(
    service = Servlet.class,
    property = {
      Constants.SERVICE_DESCRIPTION + "=KPMG - GetLastDelegatedByUserServlet",
      "sling.servlet.methods=" + HttpConstants.METHOD_GET,
      "sling.servlet.resourceTypes=kpmg/components/structure/page",
      "sling.servlet.extensions=" + "json",
      "sling.servlet.selectors=delegate"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class GetLastDelegatedByUserServlet extends SlingSafeMethodsServlet {
  private static final Logger LOG = LoggerFactory.getLogger(GetLastDelegatedByUserServlet.class);
  private static final long serialVersionUID = 2354234935L;
  @Reference private transient ResourceResolverFactory resolverFactory;
  @Reference private transient WcmWorkflowService wcmWorkflowService;

  @Override
  protected void doGet(
      final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
    final String path = request.getParameter("path");
    try (ResourceResolver resolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final UserManager userManager = resolver.adaptTo(UserManager.class);
      String userLastName = getUserLastName(getLastDelegatedByUser(path, request), userManager);
      response.getWriter().write(userLastName);
    } catch (LoginException | IOException e) {
      LOG.error("A Exception has occured while running GetLastDelegatedByUserServlet ", e);
    }
  }

  /**
   * Gets the last delegated by user.
   *
   * @param workflow the workflow
   * @param request the request
   * @return the last delegated by user
   */
  private String getLastDelegatedByUser(final String path, final SlingHttpServletRequest request) {
    try {
      final Workflow workflow = wcmWorkflowService.getWorkflowInstance(path);
      if (null != workflow) {
        final String wfHistoryPath = workflow.getId() + "/history";
        final Resource wfHistoryResource = request.getResourceResolver().getResource(wfHistoryPath);
        if (wfHistoryResource != null) {
          final Node wfHistoryNode = wfHistoryResource.adaptTo(Node.class);
          final List<String> delegateByUsers = new ArrayList<>();
          final NodeIterator itr = wfHistoryNode.getNodes();
          while (itr.hasNext()) {
            final Node childNode = itr.nextNode();
            if (childNode.hasProperty("event")
                && StringUtils.equalsIgnoreCase(
                    WorkflowEvent.WORKITEM_DELEGATION_EVENT,
                    childNode.getProperty("event").getString())) {
              delegateByUsers.add(childNode.getProperty("user").getString());
            }
          }
          if (!delegateByUsers.isEmpty()) {
            return delegateByUsers.get(delegateByUsers.size() - 1);
          }
        }
      }
    } catch (RepositoryException e) {
      LOG.error("RepositoryException has occured: ", e);
    }
    return StringUtils.EMPTY;
  }

  /**
   * Gets the userlast name.
   *
   * @param user the user
   * @param userManager the user manager
   * @return the userlast name
   */
  private String getUserLastName(final String user, final UserManager userManager) {
    if (null != userManager && StringUtils.isNotBlank(user)) {
      try {
        final Authorizable authorizableUser = userManager.getAuthorizable(user);
        if (null != authorizableUser) {

          return authorizableUser.getProperty("./profile/familyName") != null
              ? authorizableUser.getProperty("./profile/familyName")[0].getString()
              : StringUtils.EMPTY;
        }
      } catch (IllegalStateException | RepositoryException e) {
        LOG.error("Exception has occured while fetching the user details: ", e);
      }
    }
    return StringUtils.EMPTY;
  }
}
