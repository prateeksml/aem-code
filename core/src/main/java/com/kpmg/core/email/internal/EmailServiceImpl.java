package com.kpmg.core.email.internal;

import com.day.cq.mailer.MailService;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.core.services.ExternalEmailCommunication;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class EmailServiceImpl. */
@Component(service = EmailService.class)
@ServiceDescription("A Generic Email service that sends an email to a given list of recipients.")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class EmailServiceImpl implements EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

  public static final int SOCKET_CONNECTION_TIMEOUT = 60000;

  @Reference private KPMGGlobal globalSystemSettings;

  @Reference private ExternalEmailCommunication externalEmailCommunication;

  @Reference private SlingRepository slingRepository;

  @Reference EmailTemplates emailTemplatesConfig;

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
  private MailService mailService;

  /**
   * Send simple email.
   *
   * @param subject the subject
   * @param message the message
   * @param to the to
   * @param cc the cc
   */
  @Override
  public void sendSimpleEmail(
      final String subject, final String message, final String[] to, final String[] cc) {
    try {
      if (StringUtils.equalsIgnoreCase(
          globalSystemSettings.getEnableEmailNotifications(), "true")) {
        Email email = new SimpleEmail();
        email = updateEmailParameters(email, subject, message, to, cc);
        send(email);
      } else {
        LOGGER.debug(WorkflowConstants.EMAIL_NOTIFICATION_DISABLED);
      }
    } catch (EmailException e) {
      LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
    }
  }

  /**
   * Send html email.
   *
   * @param subject the subject
   * @param message the message
   * @param fromAddress the from address
   * @param to the to
   * @param cc the cc
   */
  @Override
  public void sendHtmlEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String[] to,
      final String[] cc) {
    sendHtmlEmail(subject, message, fromAddress, "", to, cc);
  }

  /**
   * Send html email.
   *
   * @param subject the subject
   * @param message the message
   * @param fromAddress the from address
   * @param fromAddressLabel the from address label
   * @param to the to
   * @param cc the cc
   */
  @Override
  public void sendHtmlEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String fromAddressLabel,
      final String[] to,
      final String[] cc) {
    try {
      if (StringUtils.equalsIgnoreCase(
          "true", globalSystemSettings.getEnableEmailNotifications())) {
        Email email = new HtmlEmail();
        email = updateEmailParameters(email, subject, message, to, cc);
        if (StringUtils.isNotBlank(fromAddress)) {
          if (StringUtils.isBlank(fromAddressLabel)) {
            email.setFrom(fromAddress, WorkflowConstants.DEFAULT_FROM_ADDRESS_LABEL);
          } else {
            email.setFrom(fromAddress, fromAddressLabel, WorkflowConstants.UTF_8);
          }
        }
        send(email);
      } else {
        LOGGER.debug(WorkflowConstants.EMAIL_NOTIFICATION_DISABLED);
      }
    } catch (EmailException e) {
      LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
    }
  }

  /**
   * Send simple email.
   *
   * @param subject the subject
   * @param message the message
   * @param fromAddress the from address
   * @param to the to
   * @param cc the cc
   */
  @Override
  public void sendSimpleEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String[] to,
      final String[] cc) {
    LOGGER.debug(
        "sendSimpleEmail | Disabled Email Notification  ---> {}",
        globalSystemSettings.getEnableEmailNotifications());
    try {
      if (StringUtils.equalsIgnoreCase(
          "true", globalSystemSettings.getEnableEmailNotifications())) {
        Email email = new SimpleEmail();
        email = updateEmailParameters(email, subject, message, to, cc);
        if (null != fromAddress) {
          email.setFrom(fromAddress);
        }
        send(email);
      } else {
        LOGGER.debug(WorkflowConstants.EMAIL_NOTIFICATION_DISABLED);
      }
    } catch (EmailException e) {
      LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
    }
  }

  /**
   * Send email with text template.
   *
   * @param templatePath the template path
   * @param tokens the tokens
   * @param to the to
   * @param cc the cc
   */
  @Override
  public void sendEmailWithTextTemplate(
      String templatePath, Map<String, Object> tokens, String[] to, String[] cc) {
    LOGGER.debug(
        "sendEmailWithTextTemplate | Disabled Email Notification  ---> {}",
        globalSystemSettings.getEnableEmailNotifications());
    if (StringUtils.equalsIgnoreCase("true", globalSystemSettings.getEnableEmailNotifications())) {
      final List<String> toList = new ArrayList<>(Arrays.asList(to));
      final List<String> ccList = new ArrayList<>(Arrays.asList(cc));
      validateEmailData(templatePath, toList, ccList);
      Session session = null;
      try {
        session = getSession();
        if (session == null) {
          throw new IllegalArgumentException("session is null.");
        }
        final EMailBuilder template = new TextTemplateEmailBuilder(templatePath, session);
        final Email email =
            template.build(
                tokens,
                toList.toArray(new String[toList.size()]),
                ccList.toArray(new String[ccList.size()]));
        email.setSocketConnectionTimeout(SOCKET_CONNECTION_TIMEOUT);
        email.setSocketTimeout(SOCKET_CONNECTION_TIMEOUT);
        send(email);

      } catch (EmailException e) {
        LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
      } finally {
        if (session != null && session.isLive()) {
          session.logout();
        }
      }
    } else {
      LOGGER.debug(WorkflowConstants.EMAIL_NOTIFICATION_DISABLED);
    }
  }

  /**
   * Validate email data.
   *
   * @param templatePath the template path
   * @param toList the to list
   * @param ccList the cc list
   */
  private void validateEmailData(
      final String templatePath, final List<String> toList, final List<String> ccList) {
    if (StringUtils.isBlank(templatePath)) {
      throw new IllegalArgumentException("Template path is null or empty");
    }
    validateToOrCCAddresses(toList, ccList);
  }

  /**
   * Gets the session.
   *
   * @return the session
   */
  private Session getSession() {
    Session session = null;
    try {
      session = slingRepository.loginService(WorkflowConstants.EMAIL, null);
    } catch (RepositoryException e) {
      LOGGER.error("Exception in getting session : ", e);
    }

    return session;
  }

  /**
   * Validate to or CC addresses.
   *
   * @param toList the to list
   * @param ccList the cc list
   */
  private void validateToOrCCAddresses(final List<String> toList, final List<String> ccList) {
    if ((toList == null) || (toList.isEmpty())) {
      throw new IllegalArgumentException("Invalid To Addresses.");
    }
    if (ccList == null) {
      throw new IllegalArgumentException("Invalid CC Addresses.");
    }

    for (Iterator<String> iterator = toList.iterator(); iterator.hasNext(); ) {
      String recipient = "";
      try {
        recipient = iterator.next();
        new InternetAddress(recipient);
      } catch (AddressException e) {
        LOGGER.error(
            "Invalid To email address '{}' passed to sendEmail(). Skipping.", recipient, e);
        iterator.remove();
      }
    }

    for (Iterator<String> iterator = ccList.iterator(); iterator.hasNext(); ) {
      String recipient = "";
      try {
        recipient = iterator.next();
        new InternetAddress(recipient);
      } catch (AddressException e) {
        LOGGER.error(
            "Invalid CC email address '{}' passed to sendEmail(). Skipping.", recipient, e);
        iterator.remove();
      }
    }
  }
  /**
   * Sends email.
   *
   * @param email the email
   * @throws EmailException the email exception
   */
  private void send(final Email email) throws EmailException {
    try {
      LOGGER.debug(
          "send email | isProduction  ---> {}",
          !org.apache.commons.lang3.StringUtils.equalsIgnoreCase(
                  "true", externalEmailCommunication.getProduction())
              && !externalEmailCommunication.getKpmgTestEmail().equals(StringUtils.EMPTY));
      if (!StringUtils.equalsIgnoreCase("true", externalEmailCommunication.getProduction())
          && !externalEmailCommunication.getKpmgTestEmail().equals(StringUtils.EMPTY)) {
        LOGGER.info("email.getCcAddresses(): {}:", email.getCcAddresses());
        email.getBccAddresses().clear();
        email.getToAddresses().clear();
        email.addTo(externalEmailCommunication.getKpmgTestEmail());
      }
      LOGGER.info("email.getCcAddresses():: outside if {}", email.getCcAddresses());
      this.mailService.send(email);
    } catch (EmailException e) {
      LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
    }
  }

  /**
   * Send html email.
   *
   * @param subject the subject
   * @param message the message
   * @param fromAddress the from address
   * @param to the to
   */
  @Override
  public void sendHtmlEmail(String subject, String message, String fromAddress, String[] to) {
    sendHtmlEmail(subject, message, fromAddress, "", to);
  }

  /**
   * Send html email.
   *
   * @param subject the subject
   * @param message the message
   * @param fromAddress the from address
   * @param fromAddressLabel the from address label
   * @param to the to
   */
  @Override
  public void sendHtmlEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String fromAddressLabel,
      final String[] to) {
    LOGGER.debug(
        "sendHtmlEmail | Disabled Email Notification  ---> {}",
        globalSystemSettings.getEnableEmailNotifications());
    try {
      if (StringUtils.equalsIgnoreCase(
          "true", globalSystemSettings.getEnableEmailNotifications())) {
        HtmlEmail email = new HtmlEmail();
        email = updateHtmlEmailParameters(email, subject, message, to);
        if (null != fromAddress) {
          if (StringUtils.isBlank(fromAddressLabel)) {
            email.setFrom(fromAddress, WorkflowConstants.DEFAULT_FROM_ADDRESS_LABEL);
          } else {
            email.setFrom(fromAddress, fromAddressLabel, WorkflowConstants.UTF_8);
          }
          send(email);
        }
      } else {
        LOGGER.debug(WorkflowConstants.EMAIL_NOTIFICATION_DISABLED);
      }
    } catch (EmailException e) {
      LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
    }
  }

  /**
   * Update email parameters.
   *
   * @param email the email
   * @param subject the subject
   * @param message the message
   * @param to the to
   * @param cc the cc
   * @return the email
   */
  private Email updateEmailParameters(
      Email email, String subject, final String message, final String[] to, final String[] cc) {
    try {
      final List<String> toList = new ArrayList<>(Arrays.asList(to));
      final List<String> ccList = new ArrayList<>(Arrays.asList(cc));
      email.setSubject(subject);
      email.setCharset(WorkflowConstants.UTF_8);
      email.setMsg(message);
      email.setSocketConnectionTimeout(SOCKET_CONNECTION_TIMEOUT);
      email.setSocketTimeout(SOCKET_CONNECTION_TIMEOUT);
      if (!toList.isEmpty()) {
        for (String toAddress : toList) {
          email.addTo(toAddress);
        }
      }
      if (!ccList.isEmpty()) {
        for (String ccAddress : ccList) {
          email.addCc(ccAddress);
        }
      }

    } catch (EmailException e) {
      LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
    }
    return email;
  }

  /**
   * Update html email parameters.
   *
   * @param email the email
   * @param subject the subject
   * @param message the message
   * @param to the to
   * @return the html email
   */
  private HtmlEmail updateHtmlEmailParameters(
      HtmlEmail email, String subject, final String message, final String[] to) {
    try {
      final List<String> toList = new ArrayList<>(Arrays.asList(to));
      email.setSubject(subject);
      email.setContent(message, "text/html");
      email.setCharset(WorkflowConstants.UTF_8);
      email.setMsg(message);
      email.setSocketConnectionTimeout(SOCKET_CONNECTION_TIMEOUT);
      email.setSocketTimeout(SOCKET_CONNECTION_TIMEOUT);
      if (!toList.isEmpty()) {
        for (String toAddress : toList) {
          email.addTo(toAddress);
        }
      }

    } catch (EmailException e) {
      LOGGER.error(WorkflowConstants.ERROR_IN_SENDING_EMAIL, e);
    }
    return email;
  }
}
