package com.kpmg.core.workflow.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.userpermission.UserGroupActions;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import junitx.util.PrivateAccessor;
import org.apache.commons.mail.EmailException;
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
class SendEmailToDelegateServletTest {

  private SendEmailToDelegateServlet testClass;
  private SlingHttpServletRequest request;
  private SlingHttpServletResponse response;
  private ResourceResolver resolver;
  private UserGroupActions userGroupActions;
  private Resource resource;
  private Page page;
  private ResourceBundleProvider resourceBundleProvider;
  private ResourceBundle resourceBundle;
  private KPMGGlobal globalConfig;
  private EmailService emailService;
  private EmailTemplates emailTemplatesConfig;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testClass = new SendEmailToDelegateServlet();
    request = mock(SlingHttpServletRequest.class);
    response = mock(SlingHttpServletResponse.class);
    resolver = mock(ResourceResolver.class);
    userGroupActions = mock(UserGroupActions.class);
    resource = mock(Resource.class);
    page = mock(Page.class);
    resourceBundleProvider = mock(ResourceBundleProvider.class);
    resourceBundle =
        new ResourceBundle() {
          @Override
          protected Object handleGetObject(@NotNull String key) {
            return "test-string";
          }

          @NotNull
          @Override
          public Enumeration<String> getKeys() {
            return null;
          }
        };
    globalConfig = mock(KPMGGlobal.class);
    emailService = mock(EmailService.class);
    emailTemplatesConfig = mock(EmailTemplates.class);

    when(request.getParameter("user")).thenReturn("test-user");
    when(request.getParameter("path")).thenReturn(PAGE_PATH);
    when(request.getResourceResolver()).thenReturn(resolver);
    when(userGroupActions.getEmail("test-user")).thenReturn("test@kpmg.com");
    when(resolver.getResource(PAGE_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(resourceBundleProvider.getResourceBundle(any())).thenReturn(resourceBundle);
    when(globalConfig.getDomainName()).thenReturn("kpmg");

    PrivateAccessor.setField(testClass, "userGroupActions", userGroupActions);
    PrivateAccessor.setField(testClass, "resourceBundleProvider", resourceBundleProvider);
    PrivateAccessor.setField(testClass, "globalConfig", globalConfig);
    PrivateAccessor.setField(testClass, "emailService", emailService);
    PrivateAccessor.setField(testClass, "emailTemplatesConfig", emailTemplatesConfig);
  }

  @Test
  void doGet() throws ServletException, IOException {
    testClass.doGet(request, response);
  }

  @Test
  void testEmailException() throws ServletException, IOException, EmailException {
    doThrow(EmailException.class)
        .when(emailService)
        .sendEmailWithTextTemplate(any(), any(), any(), any());
    testClass.doGet(request, response);
  }
}
