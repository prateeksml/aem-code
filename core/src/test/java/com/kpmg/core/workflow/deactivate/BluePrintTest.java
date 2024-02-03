package com.kpmg.core.workflow.deactivate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowTransition;
import com.kpmg.core.userpermission.MSMUserAccess;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.List;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class BluePrintTest {

  private BluePrint testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private WorkflowData workflowData;
  private List<Route> routes;
  private MSMUserAccess msmUserAccess;
  private static final String TYPE_JCR_PATH = "JCR_PATH";

  @BeforeEach
  void setUp() throws WorkflowException, NoSuchFieldException {
    testClass = new BluePrint();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    workflowData = mock(WorkflowData.class);
    msmUserAccess = mock(MSMUserAccess.class);
    routes = new ArrayList<>();
    addRoutes();

    PrivateAccessor.setField(testClass, "msmUserAccess", msmUserAccess);
  }

  // Test when payLoadType is JCR_PATH
  @Test
  void execute() throws WorkflowException {
    when(workItem.getWorkflowData()).thenReturn(workflowData);
    when(workflowData.getPayloadType()).thenReturn(TYPE_JCR_PATH);
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    when(msmUserAccess.isBlueprint(any())).thenReturn(true);
    testClass.execute(workItem, workflowSession, metaData);
  }

  // Test when payLoadType isn't equal to JCR_PATH
  @Test
  void testForPayLoadMismatch() throws WorkflowException {
    when(workItem.getWorkflowData()).thenReturn(workflowData);
    when(workflowData.getPayloadType()).thenReturn("jcr:created");
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testWorkflowException() throws WorkflowException {
    when(workItem.getWorkflowData()).thenReturn(workflowData);
    when(workflowData.getPayloadType()).thenReturn(TYPE_JCR_PATH);
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    doThrow(WorkflowException.class).when(workflowSession).complete(any(), any());
    assertThrows(
        WorkflowException.class,
        () -> {
          testClass.execute(workItem, workflowSession, metaData);
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
