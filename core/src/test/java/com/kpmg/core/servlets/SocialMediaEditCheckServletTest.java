package com.kpmg.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.kpmg.core.config.KPMGGlobal;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Iterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SocialMediaEditCheckServletTest {
  AemContext context = new AemContext();
  SocialMediaEditCheckServlet socialMediaEditCheckServlet;
  @Mock Session session;

  @Mock UserManager userManager;
  @Mock Authorizable authorizable;
  @Mock KPMGGlobal globalSystemSettings;
  SlingHttpServletRequest request;
  SlingHttpServletResponse response;
  ResourceResolver resourceResolver;
  @Mock Iterator itr;
  @Mock Group group;

  @BeforeEach
  void setup() throws NoSuchFieldException {
    request = spy(context.request());
    response = spy(context.response());
    resourceResolver = spy(context.resourceResolver());
    socialMediaEditCheckServlet = new SocialMediaEditCheckServlet();
    doReturn(resourceResolver).when(request).getResourceResolver();
    doReturn(session).when(resourceResolver).adaptTo(Session.class);
    doReturn(userManager).when(resourceResolver).adaptTo(UserManager.class);
    PrivateAccessor.setField(
        socialMediaEditCheckServlet, "globalSystemSettings", globalSystemSettings);
    doReturn("administrators|aem-global-site-administrators")
        .when(globalSystemSettings)
        .getSocailMediaAdminGroups();
  }

  @Test
  void doGet_403() throws RepositoryException {
    String user = "test";
    doReturn(user).when(session).getUserID();
    doReturn(authorizable).when(userManager).getAuthorizable(user);
    doReturn(itr).when(authorizable).memberOf();
    doReturn(true, false).when(itr).hasNext();
    doReturn(group).when(itr).next();
    doReturn("everyone").when(group).getID();
    socialMediaEditCheckServlet.doGet(request, response);
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }

  @Test
  void doGet_200() throws RepositoryException {
    String user = "test";
    doReturn(user).when(session).getUserID();
    doReturn(authorizable).when(userManager).getAuthorizable(user);
    doReturn(itr).when(authorizable).memberOf();
    doReturn(true, false).when(itr).hasNext();
    doReturn(group).when(itr).next();
    doReturn("administrators").when(group).getID();
    socialMediaEditCheckServlet.doGet(request, response);
    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }
}
