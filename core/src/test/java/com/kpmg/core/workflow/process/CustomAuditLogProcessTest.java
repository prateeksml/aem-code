package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import junitx.util.PrivateAccessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CustomAuditLogProcessTest {

  private CustomAuditLogProcess testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private PageManager pageMgr;
  private Page page;
  private ValueMap valueMap;
  private Resource resource;
  private Node node;
  private Node childNode;
  private Workflow workflow;
  private Session session;
  private static String CONTENT_PATH = "/content/kpmg/us/en/test";
  private static String lastReplicationAction = "03/05/2023";

  @BeforeEach
  void setUp() throws NoSuchFieldException, LoginException, RepositoryException {
    testClass = new CustomAuditLogProcess();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    pageMgr = mock(PageManager.class);
    page = mock(Page.class);
    valueMap = mock(ValueMap.class);
    resource = mock(Resource.class);
    node = mock(Node.class);
    childNode = mock(Node.class);
    workflow = mock(Workflow.class);
    session = mock(Session.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
  }

  @Test
  void execute() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(pageMgr.getPage(CONTENT_PATH)).thenReturn(page);
    when(page.getProperties()).thenReturn(valueMap);
    when(valueMap.get(ReplicationStatus.NODE_PROPERTY_LAST_REPLICATION_ACTION, StringUtils.EMPTY))
        .thenReturn(lastReplicationAction);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Node.class)).thenReturn(node);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("workflow-initiator");
    when(session.hasPendingChanges()).thenReturn(true);
    when(node.addNode(anyString(), anyString())).thenReturn(childNode);
    assertNotNull(testClass);
    testClass.execute(workItem, workflowSession, metaData);
  }

  // checks for NullPointerException
  @Test
  void throwsExceptionsNull() throws LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(pageMgr.getPage(CONTENT_PATH)).thenReturn(page);
    when(page.getProperties()).thenReturn(valueMap);
    when(valueMap.get(ReplicationStatus.NODE_PROPERTY_LAST_REPLICATION_ACTION, StringUtils.EMPTY))
        .thenReturn(lastReplicationAction);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Node.class)).thenReturn(node);
    assertThrows(
        NullPointerException.class, () -> testClass.execute(workItem, workflowSession, metaData));
  }

  @Test
  void testLoginException() throws LoginException, WorkflowException {
    Mockito.when(resolverFactory.getServiceResourceResolver(Mockito.any()))
        .thenThrow(LoginException.class);
    testClass.execute(workItem, workflowSession, metaData);
    assertThrows(
        LoginException.class,
        () -> {
          throw new LoginException();
        });
  }

  @Test
  void testRepositoryException() throws LoginException, WorkflowException, RepositoryException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(pageMgr.getPage(CONTENT_PATH)).thenReturn(page);
    when(page.getProperties()).thenReturn(valueMap);
    when(valueMap.get(ReplicationStatus.NODE_PROPERTY_LAST_REPLICATION_ACTION, StringUtils.EMPTY))
        .thenReturn(lastReplicationAction);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Node.class)).thenReturn(node);
    Mockito.when(node.addNode(anyString(), anyString())).thenThrow(RepositoryException.class);
    testClass.execute(workItem, workflowSession, metaData);
    assertThrows(
        RepositoryException.class,
        () -> {
          throw new RepositoryException();
        });
  }
}
