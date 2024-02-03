package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import javax.jcr.*;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DynamicReviewerTest {

  private DynamicReviewer testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private JackrabbitSession session;
  private Workflow workflow;
  private Node wfhNode;
  private NodeIterator itr;
  private Node childNode;
  private Property property;
  private Property p2;
  private UserManager userManager;
  private Authorizable authorizable;
  private static String childNodePath = "/content/kmpg/us/en";

  @BeforeEach
  void setUp() throws LoginException, NoSuchFieldException, RepositoryException {
    testClass = new DynamicReviewer();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    session = mock(JackrabbitSession.class, withSettings().extraInterfaces(Session.class));
    workflow = mock(Workflow.class);
    wfhNode = mock(Node.class);
    itr = mock(NodeIterator.class);
    childNode = mock(Node.class);
    property = mock(Property.class);
    p2 = mock(Property.class);
    userManager = mock(UserManager.class);
    authorizable = mock(Authorizable.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
  }

  // participant when reviewerGroup and authorizable are not null
  @Test
  void getParticipant() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn("currentWorkflow");
    when(session.getNode(anyString())).thenReturn(wfhNode);
    when(wfhNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.getPath()).thenReturn(childNodePath);
    when(childNode.hasProperty("event")).thenReturn(true);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn("WorkflowCompleted");
    when(wfhNode.hasProperty("reviewerGroup")).thenReturn(true);
    when(wfhNode.getProperty("reviewerGroup")).thenReturn(p2);
    when(p2.getString()).thenReturn("reviewers");
    when(session.getUserManager()).thenReturn(userManager);
    when(userManager.getAuthorizable(anyString())).thenReturn(authorizable);
    String participant = testClass.getParticipant(workItem, workflowSession, metaData);
    assertEquals("reviewers", participant);
  }

  @Test
  void getAdministrators() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn("currentWorkflow");
    when(session.getNode(anyString())).thenReturn(wfhNode);
    when(wfhNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.getPath()).thenReturn(childNodePath);
    when(childNode.hasProperty("event")).thenReturn(true);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn("WorkflowCompleted");
    when(wfhNode.hasProperty("reviewerGroup")).thenReturn(true);
    when(wfhNode.getProperty("reviewerGroup")).thenReturn(p2);
    when(p2.getString()).thenReturn("reviewers");
    when(session.getUserManager()).thenReturn(userManager);
    String participant = testClass.getParticipant(workItem, workflowSession, metaData);
    assertEquals(WorkflowConstants.ADMINISTRATORS, participant);
  }

  // checks for NullPointerException
  @Test
  void throwsException() throws LoginException, RepositoryException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn("currentWorkflow");
    when(session.getNode(anyString())).thenReturn(wfhNode);
    when(wfhNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.getPath()).thenReturn(childNodePath);
    when(childNode.hasProperty("event")).thenReturn(true);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn("WorkflowCompleted");
    when(wfhNode.hasProperty("reviewerGroup")).thenReturn(true);
    when(wfhNode.getProperty("reviewerGroup")).thenReturn(p2);
    when(p2.getString()).thenReturn("reviewers");
    assertThrows(
        NullPointerException.class,
        () -> testClass.getParticipant(workItem, workflowSession, metaData));
  }

  @Test
  void testLoginException() throws LoginException, WorkflowException {
    Mockito.when(resolverFactory.getServiceResourceResolver(Mockito.any()))
        .thenThrow(LoginException.class);
    testClass.getParticipant(workItem, workflowSession, metaData);
    assertThrows(
        LoginException.class,
        () -> {
          throw new LoginException();
        });
  }
}
