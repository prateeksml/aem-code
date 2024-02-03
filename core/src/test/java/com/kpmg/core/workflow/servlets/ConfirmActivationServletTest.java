package com.kpmg.core.workflow.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.workflow.api.WcmWorkflowService;
import com.day.cq.workflow.exec.Workflow;
import com.kpmg.core.userpermission.MSMUserAccess;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ConfirmActivationServletTest {

  private ConfirmActivationServlet testClass;
  private SlingHttpServletRequest request;
  private SlingHttpServletResponse response;
  private MSMUserAccess msmUserAccess;
  private PrintWriter writer;
  private ResourceResolver resolver;
  private WcmWorkflowService wcmWorkflowService;
  private Workflow workflow;
  private Resource wfHistory;
  private Node workflowHistoryNode;
  private NodeIterator itr;
  private Node childNode;
  private Property property;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException, IOException {
    testClass = new ConfirmActivationServlet();
    request = mock(SlingHttpServletRequest.class);
    response = mock(SlingHttpServletResponse.class);
    msmUserAccess = mock(MSMUserAccess.class);
    writer = mock(PrintWriter.class);
    resolver = mock(ResourceResolver.class);
    wcmWorkflowService = mock(WcmWorkflowService.class);
    workflow = mock(Workflow.class);
    wfHistory = mock(Resource.class);
    workflowHistoryNode = mock(Node.class);
    itr = mock(NodeIterator.class);
    childNode = mock(Node.class);
    property = mock(Property.class);

    PrivateAccessor.setField(testClass, "msmUserAccess", msmUserAccess);
    PrivateAccessor.setField(testClass, "wcmWorkflowService", wcmWorkflowService);
  }

  @Test
  void testPageIsBluePrint() throws IOException {
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(request.getParameter("activationCheck")).thenReturn("false");
    when(request.getParameter("blueprintcheck")).thenReturn("true");
    when(msmUserAccess.isBlueprint(PAGE_PATH)).thenReturn(true);
    when(response.getWriter()).thenReturn(writer);
    testClass.doGet(request, response);
  }

  @Test
  void testPageIsNotBluePrint() throws IOException {
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(request.getParameter("activationCheck")).thenReturn("false");
    when(request.getParameter("blueprintcheck")).thenReturn("true");
    when(msmUserAccess.isBlueprint(PAGE_PATH)).thenReturn(false);
    when(response.getWriter()).thenReturn(writer);
    testClass.doGet(request, response);
  }

  @Test
  void testActivationResponse() throws IOException, RepositoryException {
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(request.getParameter("blueprintcheck")).thenReturn("flase");
    when(request.getParameter("activationCheck")).thenReturn("true");
    when(response.getWriter()).thenReturn(writer);
    when(request.getResourceResolver()).thenReturn(resolver);
    when(wcmWorkflowService.getWorkflowInstance(anyString())).thenReturn(workflow);
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(resolver.getResource(anyString())).thenReturn(wfHistory);
    when(wfHistory.adaptTo(Node.class)).thenReturn(workflowHistoryNode);
    when(workflowHistoryNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.hasProperty("event")).thenReturn(true);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn("WorkflowCompleted");
    when(childNode.getPath()).thenReturn("/content/kmpg/us/en");
    when(workflowHistoryNode.hasProperty(WorkflowConstants.REVIEWER_GROUP)).thenReturn(true);
    when(workflowHistoryNode.hasProperty(WorkflowConstants.ACTIVATION_ON_COMPLETE))
        .thenReturn(true);
    when(workflowHistoryNode.getProperty(WorkflowConstants.ACTIVATION_ON_COMPLETE))
        .thenReturn(property);

    testClass.doGet(request, response);
  }

  @Test
  void testIOException() throws IOException {
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(request.getParameter("activationCheck")).thenReturn("false");
    when(request.getParameter("blueprintcheck")).thenReturn("true");
    when(msmUserAccess.isBlueprint(PAGE_PATH)).thenReturn(false);
    when(response.getWriter()).thenThrow(IOException.class);
    testClass.doGet(request, response);
  }
}
