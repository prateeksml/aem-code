package com.kpmg.core.workflow.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.userpermission.UserInfo;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class UserListServletTest {

  private UserListServlet testClass;
  private SlingHttpServletRequest request;
  private SlingHttpServletResponse response;
  private ResourceResolverFactory resolverFactory;
  private UserGroupActions userGroupActions;
  private ResourceResolver resolver;
  private Session session;
  private UserManager userManager;
  private Set<UserInfo> usersInfo;
  private UserInfo user;
  private Authorizable authorizable;
  private Node node;
  private Property property;
  private PrintWriter writer;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException, LoginException, RepositoryException, IOException {
    testClass = new UserListServlet();
    request = mock(SlingHttpServletRequest.class);
    response = mock(SlingHttpServletResponse.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    userGroupActions = mock(UserGroupActions.class);
    resolver = mock(ResourceResolver.class);
    session = mock(Session.class);
    userManager = mock(UserManager.class);
    user = mock(UserInfo.class);
    usersInfo = new HashSet<>();
    usersInfo.add(user);
    authorizable = mock(Authorizable.class);
    property = mock(Property.class);
    node = mock(Node.class);
    writer = mock(PrintWriter.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
    PrivateAccessor.setField(testClass, "userGroupActions", userGroupActions);
  }

  @Test
  void testDoGet() throws LoginException, RepositoryException, IOException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
    when(request.getParameter("groupID")).thenReturn("kpmg-id");
    when(userGroupActions.getUsersForGroup("kpmg-id", false)).thenReturn(usersInfo);
    when(user.getId()).thenReturn("test-id");
    when(userManager.getAuthorizable(anyString())).thenReturn(authorizable);
    when(authorizable.getPath()).thenReturn(PAGE_PATH);
    when(session.getNode(anyString())).thenReturn(node);
    when(node.getProperty(anyString())).thenReturn(property);
    when(property.getString()).thenReturn("kpmg-family");
    when(response.getWriter()).thenReturn(writer);
    testClass.doGet(request, response);
  }

  @Test
  void testLoginException() throws LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenThrow(LoginException.class);
    testClass.doGet(request, response);
  }
}
