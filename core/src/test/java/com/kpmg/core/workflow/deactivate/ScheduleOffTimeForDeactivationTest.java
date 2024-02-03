package com.kpmg.core.workflow.deactivate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowTransition;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ScheduleOffTimeForDeactivationTest {

  private ScheduleOffTimeForDeactivation testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private Resource resource;
  private Page payloadPage;
  private Calendar scheduleOffTime;
  private Calendar currentTime;
  private List<Route> routes;
  private ModifiableValueMap pageProperties;
  private static final String CONTENT_PATH = "/content/kpmg/us/en";
  private static final String ARGS_GETOFFTIME = "getofftime";
  private static final String ARGS_SETOFFTIME = "setofftime";

  @BeforeEach
  void setUp() throws NoSuchFieldException, LoginException, WorkflowException {
    testClass = new ScheduleOffTimeForDeactivation();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    resource = mock(Resource.class);
    payloadPage = mock(Page.class);
    scheduleOffTime = Calendar.getInstance();
    currentTime = Calendar.getInstance();
    scheduleOffTime.setTimeInMillis(System.currentTimeMillis() + 5000);
    currentTime.setTimeInMillis(System.currentTimeMillis());
    routes = new ArrayList<>();
    addRoutes();
    pageProperties = mock(ModifiableValueMap.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
  }

  @Test
  void testScheduleOffTime() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(payloadPage);
    when(payloadPage.getOffTime()).thenReturn(scheduleOffTime);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class)).thenReturn(ARGS_GETOFFTIME);
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);

    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testPageOffTime() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(payloadPage);
    when(payloadPage.getOffTime()).thenReturn(scheduleOffTime);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class)).thenReturn(ARGS_SETOFFTIME);
    when(payloadPage.getContentResource()).thenReturn(resource);
    when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(pageProperties);

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

  @Test
  void testPersistenceException() throws LoginException, WorkflowException, PersistenceException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(payloadPage);
    when(payloadPage.getOffTime()).thenReturn(scheduleOffTime);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class)).thenReturn(ARGS_SETOFFTIME);
    when(payloadPage.getContentResource()).thenReturn(resource);
    when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(pageProperties);
    doThrow(PersistenceException.class).when(resolver).commit();

    testClass.execute(workItem, workflowSession, metaData);
    assertThrows(
        PersistenceException.class,
        () -> {
          throw new PersistenceException();
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
