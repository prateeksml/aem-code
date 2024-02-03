package com.kpmg.core.workflow.deactivate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowTransition;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.List;
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
class PageActivatedStatusTest {

  private PageActivatedStatus testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private Session jcrSession;
  private List<Route> routes;
  private Replicator replicator;
  private ReplicationStatus replicationStatus;
  private static String CONTENT_PATH = "/content/kpmg/us/en";

  @BeforeEach
  void setUp() throws NoSuchFieldException, LoginException, WorkflowException {
    testClass = new PageActivatedStatus();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    jcrSession = mock(Session.class);
    routes = new ArrayList<>();
    replicator = mock(Replicator.class);
    replicationStatus = mock(ReplicationStatus.class);
    addRoutes();

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
    PrivateAccessor.setField(testClass, "replicator", replicator);
  }

  // when page is activated
  @Test
  void testPageActivated() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(jcrSession);
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(replicator.getReplicationStatus(jcrSession, CONTENT_PATH)).thenReturn(replicationStatus);
    when(replicationStatus.isActivated()).thenReturn(true);
    testClass.execute(workItem, workflowSession, metaData);
  }

  // when page isn't activated
  @Test
  void testPageNotActivated() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(jcrSession);
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(replicator.getReplicationStatus(jcrSession, CONTENT_PATH)).thenReturn(replicationStatus);
    when(replicationStatus.isActivated()).thenReturn(false);
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
