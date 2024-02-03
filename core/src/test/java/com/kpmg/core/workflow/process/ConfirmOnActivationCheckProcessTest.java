package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowTransition;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ConfirmOnActivationCheckProcessTest {

  private ConfirmOnActivationCheckProcess testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private Session session;
  private Workflow workflow;
  private Node wfhNode;
  private Node childNode;
  private NodeIterator itr;
  private Property property;
  private Property p2;
  private List<Route> routes;
  private static String childNodePath = "/content/kmpg/us/en";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    routes = new ArrayList<>();
    addRoutes();
    testClass = new ConfirmOnActivationCheckProcess();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    session = mock(Session.class);
    workflow = mock(Workflow.class);
    wfhNode = mock(Node.class);
    itr = mock(NodeIterator.class);
    childNode = mock(Node.class);
    property = mock(Property.class);
    p2 = mock(Property.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
  }

  @Test
  void execute() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn("currentWorkflow");
    when(session.getNode(anyString())).thenReturn(wfhNode);
    when(wfhNode.getNodes()).thenReturn(itr);
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.getPath()).thenReturn(childNodePath);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn("WorkflowCompleted");
    when(childNode.hasProperty("event")).thenReturn(true);
    when(wfhNode.hasProperty("reviewedBy")).thenReturn(true);
    when(wfhNode.hasProperty(WorkflowConstants.CONFIRM_ACTIVATION)).thenReturn(true);
    when(wfhNode.getProperty(WorkflowConstants.CONFIRM_ACTIVATION)).thenReturn(p2);
    when(p2.getString()).thenReturn("true");
    when(wfhNode.hasProperty("confirmActivationText")).thenReturn(true);
    when(itr.hasNext()).thenReturn(true, false);
    assertNotNull(testClass);
    testClass.execute(workItem, workflowSession, metaData);
  }

  // when activationCheck value is false
  @Test
  void activationNotCheck() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn("currentWorkflow");
    when(session.getNode(anyString())).thenReturn(wfhNode);
    when(wfhNode.getNodes()).thenReturn(itr);
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    addRoutes();
    testClass.execute(workItem, workflowSession, metaData);
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

  private void addRoutes() {
    routes.add(
        new Route() {
          @Override
          public String getId() {
            return null;
          }

          @Override
          public String getName() {
            return null;
          }

          @Override
          public boolean hasDefault() {
            return false;
          }

          @Override
          public List<WorkflowTransition> getDestinations() {
            return null;
          }

          @Override
          public boolean isBackRoute() {
            return false;
          }
        });
  }
}
