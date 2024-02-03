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
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DynamicInitiatorTest {

  private DynamicInitiator testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;

  private Resource resource;
  private Page page;
  private JackrabbitSession session;
  private Workflow workflow;
  private ValueMap valueMap;
  private static String CONTENT_PATH = "/content/kpmg/us/en/test";

  @BeforeEach
  void setUp() throws LoginException, NoSuchFieldException {
    testClass = new DynamicInitiator();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    resource = mock(Resource.class);
    page = mock(Page.class);
    session = mock(JackrabbitSession.class, withSettings().extraInterfaces(Session.class));
    workflow = mock(Workflow.class);
    valueMap = mock(ValueMap.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
  }

  // checks for getParticipant based on CREATEDBY argument
  @Test
  void getParticipant() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("workflow-initiator");
    when(page.getContentResource()).thenReturn(resource);
    when(resource.getValueMap()).thenReturn(valueMap);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.CREATEDBY);
    when(valueMap.get(JcrConstants.JCR_CREATED_BY, String.class)).thenReturn("test-user");
    String participant = testClass.getParticipant(workItem, workflowSession, metaData);
    assertEquals("test-user", participant);
  }

  // checks for getParticipant if argument is global-site-administrators
  @Test
  void getGlobalAdmins() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("workflow-initiator");
    UserManager userManager = mock(UserManager.class);
    Authorizable authorizable = mock(Authorizable.class);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.GLOBAL_SITE_ADMINISTRATORS);
    when(session.getUserManager()).thenReturn(userManager);
    when(userManager.getAuthorizable(anyString())).thenReturn(authorizable);
    String participant = testClass.getParticipant(workItem, workflowSession, metaData);
    assertEquals(WorkflowConstants.GLOBAL_SITE_ADMINISTRATORS, participant);
  }

  // checks for getParticipant if argument doesn't match above two
  @Test
  void getInitiator() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("workflow-initiator");
    String participant = testClass.getParticipant(workItem, workflowSession, metaData);
    assertEquals("workflow-initiator", participant);
  }

  // checks for NullPointerException
  @Test
  void throwsException() throws LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("workflow-initiator");
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.GLOBAL_SITE_ADMINISTRATORS);
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
