package com.kpmg.core.workflow.deactivate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.userpermission.UserInfo;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MailNotificationTest {

  private MailNotification testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private Page page;
  private Resource resource;
  private ResourceBundleProvider resourceBundleProvider;
  private ResourceBundle resourceBundle;
  private Workflow workflow;
  private WorkflowModel workflowModel;
  private KPMGGlobal globalSystemSettings;
  private JackrabbitSession session;
  private UserManager userManager;
  private Authorizable authorizable;
  private UserGroupActions userGroups;
  private EmailService emailService;
  private EmailTemplates emailTemplatesConfig;
  private Page childPage;
  private Set<UserInfo> emailIds;
  private UserInfo userInfo;
  private static final String CONTENT_PATH = "/content/kpmg/us/en";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testClass = new MailNotification();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    page = mock(Page.class);
    resource = mock(Resource.class);
    resourceBundleProvider = mock(ResourceBundleProvider.class);
    resourceBundle =
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
    workflowModel = mock(WorkflowModel.class);
    globalSystemSettings = mock(KPMGGlobal.class);
    session = mock(JackrabbitSession.class, withSettings().extraInterfaces(Session.class));
    userManager = mock(UserManager.class);
    authorizable = mock(Authorizable.class);
    userGroups = mock(UserGroupActions.class);
    emailService = mock(EmailService.class);
    emailTemplatesConfig = mock(EmailTemplates.class);
    childPage = mock(Page.class);
    emailIds = new HashSet<>();
    userInfo = mock(UserInfo.class);
    addUserInfo();

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
    PrivateAccessor.setField(testClass, "resourceBundleProvider", resourceBundleProvider);
    PrivateAccessor.setField(testClass, "globalSystemSettings", globalSystemSettings);
    PrivateAccessor.setField(testClass, "userGroups", userGroups);
    PrivateAccessor.setField(testClass, "emailService", emailService);
    PrivateAccessor.setField(testClass, "emailTemplatesConfig", emailTemplatesConfig);
  }

  @Test
  void execute() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(workItem.getContentPath()).thenReturn(CONTENT_PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resourceBundleProvider.getResourceBundle(any())).thenReturn(resourceBundle);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflow.getWorkflowModel()).thenReturn(workflowModel);
    when(workflowModel.getTitle()).thenReturn("MailNotification Workflow");
    when(globalSystemSettings.getDomainName()).thenReturn("kpmg");
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workflow.getInitiator()).thenReturn("workflow-initiator");
    when(session.getUserManager()).thenReturn(userManager);
    when(userManager.getAuthorizable("workflow-initiator")).thenReturn(authorizable);
    when(authorizable.getID()).thenReturn("test-id");
    when(userGroups.getEmail("test-id")).thenReturn("test@kpmg.com");
    when(emailTemplatesConfig.getEmailTemplatePath(
            WorkflowConstants.DEACTIVATION_INITIATOR_NOTIFICATION))
        .thenReturn("/etc/workflow/notification/email");
    when(page.getAbsoluteParent(anyInt())).thenReturn(childPage);
    when(childPage.getName()).thenReturn("us");
    when(userGroups.getUsersForGroup("aem-us-livecopy-admin")).thenReturn(emailIds);
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

  private void addUserInfo() {
    emailIds.add(userInfo);
  }
}
