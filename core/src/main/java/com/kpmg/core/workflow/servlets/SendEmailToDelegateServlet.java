package com.kpmg.core.workflow.servlets;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This servlet is used to send email to delegate. */
@Component(
    service = Servlet.class,
    property = {
      Constants.SERVICE_DESCRIPTION + "=KPMG - SendEmailToDelegateServlet",
      "sling.servlet.methods=" + HttpConstants.METHOD_GET,
      "sling.servlet.resourceTypes=kpmg/components/structure/page",
      "sling.servlet.extensions=json",
      "sling.servlet.selectors=sendEmailToDelegatee"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class SendEmailToDelegateServlet extends SlingAllMethodsServlet {

  /** Default serial version unique ID */
  private static final long serialVersionUID = 798871L;

  @Reference private transient UserGroupActions userGroupActions;
  @Reference private transient EmailService emailService;
  private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailToDelegateServlet.class);

  @Reference(target = "(component.name=org.apache.sling.i18n.impl.JcrResourceBundleProvider)")
  private transient ResourceBundleProvider resourceBundleProvider;

  @Reference private transient KPMGGlobal globalConfig;

  @Reference private transient EmailTemplates emailTemplatesConfig;

  @Override
  protected void doGet(
      final SlingHttpServletRequest request, final SlingHttpServletResponse response)
      throws ServletException, IOException {
    try {
      final String user = request.getParameter("user");
      final String path = request.getParameter("path");
      final ResourceResolver resolver = request.getResourceResolver();
      final String email = userGroupActions.getEmail(user);
      if (StringUtils.isNotEmpty(email)) {
        final Resource resource = resolver.getResource(path);
        if (resource != null) {
          final Page page = resource.adaptTo(Page.class);
          if (page != null) {
            final ResourceBundle resourceBundle =
                resourceBundleProvider.getResourceBundle(page.getLanguage(false));
            final Map<String, Object> tokens = new HashMap<>();
            tokens.put("delegateeEmail", resourceBundle.getString("kpmg.workflow.delegateeEmail"));
            tokens.put(
                "message1", resourceBundle.getString("kpmg.workflow.delegateeEmail.message1"));
            tokens.put(
                "message2", resourceBundle.getString("kpmg.workflow.delegateeEmail.message2"));
            tokens.put("payload", path);
            tokens.put("host.prefix", globalConfig.getDomainName());
            tokens.put(
                "autoGeneratedText", resourceBundle.getString("kpmg.workflow.autoGeneratedText"));
            tokens.put("viewInboxText", resourceBundle.getString("kpmg.workflow.viewInboxText"));
            emailService.sendEmailWithTextTemplate(
                emailTemplatesConfig.getEmailTemplatePath(WorkflowConstants.DELEGATEE_EMAIL),
                tokens,
                new String[] {email},
                new String[] {});
          }
        }
      }
    } catch (EmailException e) {
      LOGGER.error("EmailException {} while sending the email in {}: ", e, getClass());
    }
  }
}
