package com.kpmg.core.workflow.utility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.event.WorkflowEvent;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import javax.jcr.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class WorkflowUtilityTest {
  private WorkItem workItem;
  private Session session;
  private Workflow workflow;
  private Node node;
  private NodeIterator itr;
  private Property property;
  private WorkflowSession workflowSession;
  private ResourceResolver subServiceResolver;
  private Resource pageResource;
  private Page page;
  private Replicator replicator;
  private ReplicationStatus replicationStatus;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() {
    workItem = mock(WorkItem.class);
    session = mock(Session.class);
    workflow = mock(Workflow.class);
    node = mock(Node.class);
    itr = mock(NodeIterator.class);
    property = mock(Property.class);
    workflowSession = mock(WorkflowSession.class);
    subServiceResolver = mock(ResourceResolver.class);
    pageResource = mock(Resource.class);
    page = mock(Page.class);
    replicator = mock(Replicator.class);
    replicationStatus = mock(ReplicationStatus.class);
  }

  @Test
  void getPropertyValueFromHistory() throws RepositoryException {
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(session.getNode(anyString())).thenReturn(node);
    when(node.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(node);
    when(node.getPath()).thenReturn(PAGE_PATH);
    when(node.hasProperty(anyString())).thenReturn(true);
    when(node.getProperty(anyString())).thenReturn(property);
    when(property.getString()).thenReturn(WorkflowEvent.WORKFLOW_COMPLETED_EVENT);
    WorkflowUtility.getPropertyValueFromHistory(workItem, session, "test-property");
  }

  @Test
  void testRepositoryException() throws RepositoryException {
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(session.getNode(anyString())).thenThrow(RepositoryException.class);
    WorkflowUtility.getPropertyValueFromHistory(workItem, session, "test-property");
  }

  @Test
  void terminateWorkFlow() throws WorkflowException {
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(workflowSession.getWorkflow(anyString())).thenReturn(workflow);
    WorkflowUtility.terminateWorkFlow(workflowSession, workItem);
  }

  @Test
  void testWorkflowException() throws WorkflowException {
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(workflowSession.getWorkflow(anyString())).thenThrow(WorkflowException.class);
    WorkflowUtility.terminateWorkFlow(workflowSession, workItem);
  }

  @Test
  void getPayloadPage() {
    when(subServiceResolver.getResource(anyString())).thenReturn(pageResource);
    when(pageResource.adaptTo(Page.class)).thenReturn(page);
    WorkflowUtility.getPayloadPage(subServiceResolver, "/content/kmpg/us/en/test");
  }

  @Test
  void isActivated() {
    when(replicator.getReplicationStatus(session, PAGE_PATH)).thenReturn(replicationStatus);
    when(replicationStatus.isActivated()).thenReturn(true);
    WorkflowUtility.isActivated(replicator, PAGE_PATH, session);
  }
}
