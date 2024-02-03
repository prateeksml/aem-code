package com.kpmg.core.email;

import java.util.Map;
import org.apache.commons.mail.EmailException;

/**
 * This interface provides the methods required for email services
 *
 * @author pmitt5
 */
public interface EmailService {

  /**
   * Sends a html email with CC list
   *
   * @param subject subject
   * @param message message
   * @param to to
   * @param cc cc
   * @throws EmailException EmailException
   */
  void sendSimpleEmail(
      final String subject, final String message, final String[] to, final String[] cc)
      throws EmailException;

  /**
   * Sends a html email with CC list and a sender's email address label
   *
   * @param subject subject
   * @param message message
   * @param fromAddress fromAddress
   * @param to to
   * @param cc cc
   * @throws EmailException EmailException
   */
  void sendHtmlEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String[] to,
      final String[] cc)
      throws EmailException;

  /**
   * Sends a html email with CC list and a sender's email address label
   *
   * @param subject subject
   * @param message message
   * @param fromAddress fromAddress
   * @param fromAddressLabel fromAddressLabel
   * @param to to
   * @param cc cc
   * @throws EmailException EmailException
   */
  void sendHtmlEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String fromAddressLabel,
      final String[] to,
      final String[] cc)
      throws EmailException;

  /**
   * Sends a simple email
   *
   * @param subject subject
   * @param message message
   * @param to to
   * @param fromAddress fromAddress
   * @param cc cc
   * @throws EmailException EmailException
   */
  void sendSimpleEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String[] to,
      final String[] cc)
      throws EmailException;

  /**
   * Sends an email with a text template
   *
   * @param templatePath templatePath
   * @param tokens tokens
   * @param to to
   * @param cc cc
   * @throws EmailException EmailException
   */
  void sendEmailWithTextTemplate(
      final String templatePath,
      final Map<String, Object> tokens,
      final String[] to,
      final String[] cc)
      throws EmailException;

  /**
   * Sends a html email
   *
   * @param subject subject
   * @param message message
   * @param fromAddress fromAddress
   * @param to to
   * @throws EmailException EmailException
   */
  void sendHtmlEmail(
      final String subject, final String message, final String fromAddress, final String[] to)
      throws EmailException;

  /**
   * Sends a html email with a sender's email address label
   *
   * @param subject subject
   * @param message message
   * @param fromAddress fromAddress
   * @param fromAddressLabel fromAddressLabel
   * @param to to
   * @throws EmailException EmailException
   */
  void sendHtmlEmail(
      final String subject,
      final String message,
      final String fromAddress,
      final String fromAddressLabel,
      final String[] to)
      throws EmailException;
}
