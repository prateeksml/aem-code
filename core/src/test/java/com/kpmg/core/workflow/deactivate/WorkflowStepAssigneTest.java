package com.kpmg.core.workflow.deactivate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.event.WorkflowEvent;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.services.impl.EmailTemplatesConfigImpl;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.userpermission.UserInfo;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.*;
import javax.jcr.*;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class WorkflowStepAssigneTest {

  private WorkflowStepAssigne testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private Session session;
  private Resource resource;
  private Page page;
  private Locale locale;
  private ResourceBundleProvider resourceBundleProvider;
  private ResourceBundle resBundle;
  private Workflow workflow;
  private Node wfhNode;
  private NodeIterator itr;
  private Node childNode;
  private Property property;
  private WorkflowData workflowData;
  private KPMGGlobal globalSystemSettings;
  private UserGroupActions userGroups;
  private Set<UserInfo> emailIds;
  private UserInfo userInfo;
  private WorkflowModel workflowModel;
  private EmailService emailService;
  private EmailTemplates emailTemplatesConfig;

  private static final String CONTENT_PATH = "/content/kpmg/us/en";

  @BeforeEach
  void setUp() throws NoSuchFieldException, RepositoryException {
    testClass = spy(WorkflowStepAssigne.class);
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    session = mock(Session.class);
    resource = mock(Resource.class);
    page = mock(Page.class);
    locale = new Locale("en", "us");
    resourceBundleProvider = mock(ResourceBundleProvider.class);
    resBundle =
        new ResourceBundle() {
          @Override
          protected Object handleGetObject(@NotNull String key) {
            return "Test";
          }

          @NotNull
          @Override
          public Enumeration<String> getKeys() {
            return null;
          }
        };
    workflow = mock(Workflow.class);
    wfhNode = mock(Node.class);
    itr = mock(NodeIterator.class);
    childNode = mock(Node.class);
    property = mock(Property.class);
    workflowData = mock(WorkflowData.class);
    globalSystemSettings = mock(KPMGGlobal.class);
    userGroups = mock(UserGroupActions.class);
    emailIds = new HashSet<>();
    userInfo = mock(UserInfo.class);
    workflowModel = mock(WorkflowModel.class);
    emailService = mock(EmailService.class);
    emailTemplatesConfig = mock(EmailTemplatesConfigImpl.class);
    addUserInfo();

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
    PrivateAccessor.setField(testClass, "resourceBundleProvider", resourceBundleProvider);
    PrivateAccessor.setField(testClass, "globalSystemSettings", globalSystemSettings);
    PrivateAccessor.setField(testClass, "userGroups", userGroups);
    PrivateAccessor.setField(testClass, "emailService", emailService);
    PrivateAccessor.setField(testClass, "emailTemplatesConfig", emailTemplatesConfig);
  }

  @Test
  void getParticipant() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(resolver.getResource(CONTENT_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getLanguage(false)).thenReturn(locale);
    when(resourceBundleProvider.getResourceBundle(locale)).thenReturn(resBundle);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn(CONTENT_PATH);
    when(session.getNode(anyString())).thenReturn(wfhNode);
    when(wfhNode.getNodes()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.nextNode()).thenReturn(childNode);
    when(childNode.getPath()).thenReturn("content/kpmg/us");
    when(childNode.hasProperty("event")).thenReturn(true);
    when(childNode.getProperty("event")).thenReturn(property);
    when(property.getString()).thenReturn(WorkflowEvent.WORKFLOW_COMPLETED_EVENT);
    when(wfhNode.hasProperty("comment")).thenReturn(true);
    when(wfhNode.getProperty(anyString())).thenReturn(property);
    when(workItem.getWorkflowData()).thenReturn(workflowData);
    when(workflowData.getMetaDataMap()).thenReturn(metaData);
    doNothing().when(testClass).notifyApprover(any(), any(), any(), any());
    testClass.getParticipant(workItem, workflowSession, metaData);
  }

  @Test
  void notifyApprover() {
    when(userGroups.getUsersForGroup("us-site-admin")).thenReturn(emailIds);
    when(userInfo.getEmail()).thenReturn("test@kpmg.com");
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getInitiator()).thenReturn("initiator");
    when(page.getPath()).thenReturn(CONTENT_PATH);
    when(workflow.getWorkflowModel()).thenReturn(workflowModel);
    when(workflowModel.getTitle()).thenReturn("Test-title");
    when(emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.DEACTIVATION_APPROVER_NOTIFICATION))
        .thenReturn("/etc/workflow/notification/email");
    testClass.notifyApprover("us-site-admin", page, resBundle, workItem);
  }

  @Test
  void testRepositoryException() throws RepositoryException, WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(resolver.getResource(CONTENT_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getLanguage(false)).thenReturn(locale);
    when(resourceBundleProvider.getResourceBundle(locale)).thenReturn(resBundle);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getId()).thenReturn(CONTENT_PATH);
    when(session.getNode(anyString())).thenThrow(RepositoryException.class);
    testClass.getParticipant(workItem, workflowSession, metaData);
  }

  @Test
  void testLoginException() throws RepositoryException, WorkflowException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenThrow(LoginException.class);
    testClass.getParticipant(workItem, workflowSession, metaData);
  }

  private void addUserInfo() throws RepositoryException {
    emailIds.add(userInfo);
  }
}
