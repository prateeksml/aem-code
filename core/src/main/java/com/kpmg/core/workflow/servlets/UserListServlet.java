package com.kpmg.core.workflow.servlets;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.userpermission.UserInfo;
import com.kpmg.core.utils.ResourceResolverUtility;
import java.io.IOException;
import java.util.Set;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This service class will return a list of users as a string by comma separated */
@Component(
    service = Servlet.class,
    property = {
      Constants.SERVICE_DESCRIPTION + "=KPMG - UserListServlet",
      "sling.servlet.methods=" + HttpConstants.METHOD_GET,
      "sling.servlet.resourceTypes=kpmg/components/structure/page",
      "sling.servlet.extensions=json",
      "sling.servlet.selectors=getUserListService"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class UserListServlet extends SlingAllMethodsServlet {

  private static final long serialVersionUID = 598930373L;
  private static final Logger LOG = LoggerFactory.getLogger(UserListServlet.class);
  @Reference private transient UserGroupActions userGroupActions;
  @Reference private transient ResourceResolverFactory resolverFactory;

  /**
   * Do get.
   *
   * @param request the request
   * @param response the response
   */
  @Override
  public void doGet(
      final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
    try (ResourceResolver resolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Session session = resolver.adaptTo(Session.class);
      final UserManager userManager = resolver.adaptTo(UserManager.class);
      final String groupID = request.getParameter("groupID");
      String usersList = StringUtils.EMPTY;
      if (StringUtils.isNotBlank(groupID)) {
        final Set<UserInfo> usersInfo = userGroupActions.getUsersForGroup(groupID, false);
        for (final UserInfo userInfo : usersInfo) {
          if (userManager != null && session != null) {
            final String userProfilePath =
                userManager.getAuthorizable(userInfo.getId()).getPath() + "/profile";
            final String userLastName =
                session.getNode(userProfilePath).getProperty("familyName").getString();
            usersList = usersList + userInfo.getId() + ":" + userLastName + ",";
          }
        }
      }
      if (StringUtils.isNotEmpty(usersList)) {
        response.getWriter().write(StringUtils.substring(usersList, 0, usersList.length() - 1));
      }
    } catch (LoginException | RepositoryException | IOException e) {
      LOG.error("A Exception has occured while running UserListServlet ", e);
    }
  }
}
