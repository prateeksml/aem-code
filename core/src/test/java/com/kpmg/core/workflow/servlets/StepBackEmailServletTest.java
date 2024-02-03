package com.kpmg.core.workflow.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.workflow.api.WcmWorkflowService;
import com.day.cq.workflow.exec.Workflow;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.jcr.*;
import javax.servlet.ServletException;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class StepBackEmailServletTest {

  private StepBackEmailServlet testClass;
  private SlingHttpServletRequest request;
  private SlingHttpServletResponse response;
  private ResourceResolver resolver;
  private WcmWorkflowService wcmWorkflowService;
  private Workflow workflow;
  private UserGroupActions userGroupActions;
  private Resource resource;
  private Page page;
  private ResourceBundle resourceBundle;
  private ResourceBundleProvider resourceBundleProvider;
  private KPMGGlobal globalConfig;
  private EmailService emailService;
  private EmailTemplates emailTemplatesConfig;
  private Node workflowHistoryNode;
  private NodeIterator itr;
  private Node childNode;
  private Property property;
  private Session session;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testClass = new StepBackEmailServlet();
    request = mock(SlingHttpServletRequest.class);
    response = mock(SlingHttpServletResponse.class);
    resolver = mock(ResourceResolver.class);
    wcmWorkflowService = mock(WcmWorkflowService.class);
    workflow = mock(Workflow.class);
    userGroupActions = mock(UserGroupActions.class);
    resource = mock(Resource.class);
    page = mock(Page.class);
    resourceBundle =
        new ResourceBundle() {
          @Override
          protected Object handleGetObject(@NotNull String key) {
            return "test-value";
          }

          @NotNull
          @Override
          public Enumeration<String> getKeys() {
            return null;
          }
        };
    resourceBundleProvider = mock(ResourceBundleProvider.class);
    globalConfig = mock(KPMGGlobal.class);
    emailService = mock(EmailService.class);
    emailTemplatesConfig = mock(EmailTemplates.class);
    workflowHistoryNode = mock(Node.class);
    itr = mock(NodeIterator.class);
    childNode = mock(Node.class);
    property = mock(Property.class);
    session = mock(Session.class);

    PrivateAccessor.setField(testClass, "wcmWorkflowService", wcmWorkflowService);
    PrivateAccessor.setField(testClass, "userGroupActions", userGroupActions);
    PrivateAccessor.setField(testClass, "resourceBundleProvider", resourceBundleProvider);
    PrivateAccessor.setField(testClass, "globalConfig", globalConfig);
    PrivateAccessor.setField(testClass, "emailService", emailService);
    PrivateAccessor.setField(testClass, "emailTemplatesConfig", emailTemplatesConfig);
  }

  @Test
  void testDoGet() throws ServletException, IOException, RepositoryException {
    when(request.getResourceResolver()).thenReturn(resolver);
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(wcmWorkflowService.getWorkflowInstance(anyString())).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("test-initiator");
    when(userGroupActions.getEmail(anyString())).thenReturn("test@kpmg.com");
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resource.adaptTo(Node.class)).thenReturn(workflowHistoryNode);
    when(workflowHistoryNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.hasProperty("event")).thenReturn(true);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn("WorkflowCompleted");
    when(childNode.getPath()).thenReturn("/content/kmpg/us/en");
    when(resourceBundleProvider.getResourceBundle(any())).thenReturn(resourceBundle);
    when(globalConfig.getDomainName()).thenReturn("kpmg");
    testClass.doGet(request, response);
  }

  @Test
  void testRemoveConfirmActivationProperty()
      throws ServletException, IOException, RepositoryException {
    when(request.getResourceResolver()).thenReturn(resolver);
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(wcmWorkflowService.getWorkflowInstance(anyString())).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("test-initiator");
    when(userGroupActions.getEmail(anyString())).thenReturn("test@kpmg.com");
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resourceBundleProvider.getResourceBundle(any())).thenReturn(resourceBundle);
    when(globalConfig.getDomainName()).thenReturn("kpmg");
    when(workflow.getId()).thenReturn(PAGE_PATH);
    when(resource.adaptTo(Node.class)).thenReturn(workflowHistoryNode);
    when(workflowHistoryNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.hasProperty("event")).thenReturn(true);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn("WorkflowCompleted");
    when(childNode.getPath()).thenReturn("/content/kmpg/us/en");
    when(workflowHistoryNode.hasProperty(anyString())).thenReturn(true);
    when(workflowHistoryNode.getProperty(WorkflowConstants.ACTIVATION_ON_COMPLETE))
        .thenReturn(property);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    testClass.doGet(request, response);
  }
}
