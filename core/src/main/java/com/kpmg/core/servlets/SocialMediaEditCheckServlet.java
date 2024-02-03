package com.kpmg.core.servlets;

import com.kpmg.core.config.KPMGGlobal;
import java.util.Iterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/can-edit-social-media")
@Log4j
public class SocialMediaEditCheckServlet extends SlingAllMethodsServlet {

  private static final long serialVersionUID = 1L;

  @Reference private transient KPMGGlobal globalSystemSettings;

  @Override
  protected void doGet(
      final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

    final ResourceResolver resourceResolver = request.getResourceResolver();
    final Session session = resourceResolver.adaptTo(Session.class);
    final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
    final String allowedGroups = globalSystemSettings.getSocailMediaAdminGroups();
    if (ObjectUtils.allNotNull(session, userManager)) {
      try {
        final Authorizable currentAuthorizanble = userManager.getAuthorizable(session.getUserID());

        if (hasAnyGroup(
            currentAuthorizanble, StringUtils.splitByWholeSeparator(allowedGroups, "|"))) {
          response.setStatus(HttpServletResponse.SC_OK);
        } else {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
      } catch (RepositoryException e) {
        log.error("Error while checking user's access to edit socials", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    } else {
      log.error("Error getting session or user manager for current user");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  boolean hasAnyGroup(final Authorizable authorizable, final String[] expectedGroups)
      throws RepositoryException {
    if (null == authorizable) {
      return false;
    }
    final Iterator<Group> groups = authorizable.memberOf();
    while (groups.hasNext()) {
      if (StringUtils.equalsAny(groups.next().getID(), expectedGroups)) {
        return true;
      }
    }
    return false;
  }
}
