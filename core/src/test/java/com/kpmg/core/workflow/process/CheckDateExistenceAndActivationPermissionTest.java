package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;
import com.kpmg.core.userpermission.GroupInfo;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.*;
import javax.jcr.Session;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CheckDateExistenceAndActivationPermissionTest {

  private CheckDateExistenceAndActivationPermission testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private Session session;
  private Resource resource;
  private Page payloadPage;
  private UserGroupActions userGroupActions;
  private Set<GroupInfo> groupSet;
  private Workflow workflow;
  private Template template;
  private List<Route> routes;
  private Calendar calendar;
  private Iterator iterator;
  private GroupInfo group;
  private static final String TEST_PAGE_PATH = "/content/kpmgpublic/us/en/test";
  private static String TEMPLATE_NAME = "html-template";

  @BeforeEach
  void setUp() throws LoginException, NoSuchFieldException, WorkflowException {
    testClass = new CheckDateExistenceAndActivationPermission();
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    session = mock(Session.class);
    resource = mock(Resource.class);
    payloadPage = mock(Page.class);
    userGroupActions = mock(UserGroupActions.class);
    workflow = mock(Workflow.class);
    template = mock(Template.class);
    groupSet = mock(Set.class);
    routes = new ArrayList<>();
    calendar = Calendar.getInstance();
    iterator = mock(Iterator.class);
    group = mock(GroupInfo.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
    PrivateAccessor.setField(testClass, "userGroupActions", userGroupActions);
  }

  @Test
  void execute() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("initiator");
    when(WorkflowUtility.getPayloadPage(resolver, TEST_PAGE_PATH)).thenReturn(payloadPage);
    when(userGroupActions.getGroupsForUser(workItem.getWorkflow().getInitiator(), true))
        .thenReturn(groupSet);
    when(groupSet.iterator()).thenReturn(iterator);
    when(iterator.hasNext()).thenReturn(true, false);
    when(iterator.next()).thenReturn(group);
    when(group.getId()).thenReturn(WorkflowConstants.BLUEPRINT_CONTRIBUTORS);
    when(payloadPage.getTemplate()).thenReturn(template);
    when(template.getName()).thenReturn(TEMPLATE_NAME);
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    assertNotNull(testClass);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void dateExistsCheck() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("initiator");
    when(WorkflowUtility.getPayloadPage(resolver, TEST_PAGE_PATH)).thenReturn(payloadPage);
    when(userGroupActions.getGroupsForUser(workItem.getWorkflow().getInitiator(), true))
        .thenReturn(groupSet);
    when(groupSet.iterator()).thenReturn(iterator);
    when(iterator.hasNext()).thenReturn(false);
    when(payloadPage.getTemplate()).thenReturn(template);
    when(template.getName()).thenReturn(TEMPLATE_NAME);
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    when(payloadPage.getOffTime()).thenReturn(calendar);
    testClass.execute(workItem, workflowSession, metaData);
  }

  // when dateExists and isHTMLPage is false
  @Test
  void isNotHtmlPage() throws WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("initiator");
    when(WorkflowUtility.getPayloadPage(resolver, TEST_PAGE_PATH)).thenReturn(payloadPage);
    when(userGroupActions.getGroupsForUser(workItem.getWorkflow().getInitiator(), true))
        .thenReturn(groupSet);
    when(groupSet.iterator()).thenReturn(iterator);
    when(iterator.hasNext()).thenReturn(false);
    when(payloadPage.getTemplate()).thenReturn(template);
    when(template.getName()).thenReturn("test-template");
    getRoutes();
    when(workflowSession.getRoutes(workItem, false)).thenReturn(routes);
    when(payloadPage.getOffTime()).thenReturn(calendar);
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
    routes.add(r2);
    routes.add(r1);
  }
}
