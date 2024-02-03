package com.kpmg.core.workflow.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.workflow.api.WcmWorkflowService;
import com.day.cq.workflow.exec.Workflow;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.*;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class GetLastDelegatedByUserServletTest {

  private GetLastDelegatedByUserServlet testClass;
  private SlingHttpServletRequest request;
  private SlingHttpServletResponse response;
  private ResourceResolverFactory resolverFactory;
  private WcmWorkflowService wcmWorkflowService;
  private ResourceResolver resolver;
  private UserManager userManager;
  private Workflow workflow;
  private Resource resource;
  private Node wfHistoryNode;
  private NodeIterator itr;
  private Node childNode;
  private Property property;
  private PrintWriter writer;
  private Authorizable authorizableUser;
  private Value[] values;
  private Value value;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testClass = new GetLastDelegatedByUserServlet();
    request = mock(SlingHttpServletRequest.class);
    response = mock(SlingHttpServletResponse.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    wcmWorkflowService = mock(WcmWorkflowService.class);
    resolver = mock(ResourceResolver.class);
    userManager = mock(UserManager.class);
    workflow = mock(Workflow.class);
    resource = mock(Resource.class);
    wfHistoryNode = mock(Node.class);
    itr = mock(NodeIterator.class);
    childNode = mock(Node.class);
    property = mock(Property.class);
    writer = mock(PrintWriter.class);
    authorizableUser = mock(Authorizable.class);
    value = mock(Value.class);
    values = new Value[1];
    values[0] = value;

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
    PrivateAccessor.setField(testClass, "wcmWorkflowService", wcmWorkflowService);
  }

  @Test
  void testLastDelegatedByUser() throws LoginException, RepositoryException, IOException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(wcmWorkflowService.getWorkflowInstance(anyString())).thenReturn(workflow);
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(request.getResourceResolver()).thenReturn(resolver);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Node.class)).thenReturn(wfHistoryNode);
    when(wfHistoryNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.hasProperty(anyString())).thenReturn(true);
    when(childNode.getProperty(anyString())).thenReturn(property);
    when(property.getString()).thenReturn("WorkItemDelegated");
    when(response.getWriter()).thenReturn(writer);
    testClass.doGet(request, response);
  }

  @Test
  void testGetUserLastName() throws LoginException, RepositoryException, IOException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(wcmWorkflowService.getWorkflowInstance(anyString())).thenReturn(workflow);
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(request.getResourceResolver()).thenReturn(resolver);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Node.class)).thenReturn(wfHistoryNode);
    when(wfHistoryNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.hasProperty(anyString())).thenReturn(true);
    when(childNode.getProperty(anyString())).thenReturn(property);
    when(property.getString()).thenReturn("WorkItemDelegated");
    when(userManager.getAuthorizable(anyString())).thenReturn(authorizableUser);
    when(authorizableUser.getProperty(anyString())).thenReturn(values);
    when(value.getString()).thenReturn("test-value");
    when(response.getWriter()).thenReturn(writer);
    testClass.doGet(request, response);
  }

  @Test
  void testLoginException() throws LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenThrow(LoginException.class);
    testClass.doGet(request, response);
  }
}
