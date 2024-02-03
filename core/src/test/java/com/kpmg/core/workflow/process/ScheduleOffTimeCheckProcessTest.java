package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowTransition;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.jcr.RepositoryException;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ScheduleOffTimeCheckProcessTest {

  private ScheduleOffTimeCheckProcess testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private Resource resource;
  private ResourceResolver resolver;
  private ResourceResolverFactory resolverFactory;
  private MetaDataMap metaData;
  private Page page;
  private static final String TEST_PAGE_PATH = "/content/kpmgpublic/us/en/test";
  List<Route> routes = new ArrayList<>();

  @BeforeEach
  public void setup()
      throws NoSuchFieldException, LoginException, RepositoryException, WorkflowException {
    testClass = new ScheduleOffTimeCheckProcess();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    resolver = mock(ResourceResolver.class);
    resource = mock(Resource.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    metaData = mock(MetaDataMap.class);
    page = mock(Page.class);
    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
  }

  @Test
  void testExecute() throws LoginException, WorkflowException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(TEST_PAGE_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    final Calendar pageOffTime = Calendar.getInstance();
    pageOffTime.add(Calendar.DATE, 2);
    final Calendar pageOnTime = Calendar.getInstance();
    pageOnTime.add(Calendar.DATE, -15);
    when(page.getOffTime()).thenReturn(pageOffTime);
    when(page.getOnTime()).thenReturn(pageOnTime);
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testExecuteForNullOnTime() throws LoginException, WorkflowException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(TEST_PAGE_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    final Calendar pageOffTime = Calendar.getInstance();
    pageOffTime.add(Calendar.DATE, 2);
    when(page.getOffTime()).thenReturn(pageOffTime);
    when(page.getOnTime()).thenReturn(null);
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testExecuteForNullOffTime() throws LoginException, WorkflowException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(TEST_PAGE_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    final Calendar pageOffTime = Calendar.getInstance();
    pageOffTime.add(Calendar.DATE, 2);
    when(page.getOffTime()).thenReturn(null);
    when(page.getOnTime()).thenReturn(null);
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testExecuteForOnTime() throws LoginException, WorkflowException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(TEST_PAGE_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    final Calendar pageOffTime = Calendar.getInstance();
    pageOffTime.add(Calendar.DATE, 2);
    final Calendar pageOnTime = Calendar.getInstance();
    pageOnTime.add(Calendar.DATE, 15);
    when(page.getOffTime()).thenReturn(pageOffTime);
    when(page.getOnTime()).thenReturn(pageOnTime);
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testExecuteForOffTime() throws LoginException, WorkflowException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(TEST_PAGE_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    final Calendar pageOffTime = Calendar.getInstance();
    pageOffTime.add(Calendar.DATE, 2);
    final Calendar pageOnTime = Calendar.getInstance();
    pageOnTime.add(Calendar.DATE, -15);
    when(page.getOffTime()).thenReturn(pageOffTime);
    when(page.getOnTime()).thenReturn(pageOnTime);
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
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

  private void getRoutes() {
    Route r1 =
        new Route() {

          @Override
          public boolean isBackRoute() {
            return false;
          }

          @Override
          public boolean hasDefault() {
            return false;
          }

          @Override
          public String getName() {
            return "route1";
          }

          @Override
          public String getId() {
            return "route1";
          }

          @Override
          public List<WorkflowTransition> getDestinations() {
            return null;
          }
        };

    Route r2 =
        new Route() {

          @Override
          public boolean isBackRoute() {
            return false;
          }

          @Override
          public boolean hasDefault() {
            return false;
          }

          @Override
          public String getName() {
            return "route1";
          }

          @Override
          public String getId() {
            return "route1";
          }

          @Override
          public List<WorkflowTransition> getDestinations() {
            return null;
          }
        };
    Route r3 =
        new Route() {

          @Override
          public boolean isBackRoute() {
            return false;
          }

          @Override
          public boolean hasDefault() {
            return false;
          }

          @Override
          public String getName() {
            return "route1";
          }

          @Override
          public String getId() {
            return "route1";
          }

          @Override
          public List<WorkflowTransition> getDestinations() {
            return null;
          }
        };
    routes.add(r2);
    routes.add(r1);
    routes.add(r3);
  }
}
