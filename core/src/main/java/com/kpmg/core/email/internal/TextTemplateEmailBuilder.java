package com.kpmg.core.email.internal;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import java.util.HashMap;
import java.util.Map;
import javax.jcr.Session;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.text.StringSubstitutor;

/** The Class TextTemplateEmailBuilder. */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class TextTemplateEmailBuilder extends AbstractEmailBuilder implements EMailBuilder {

  /**
   * @param path
   * @param session
   * @throws EmailException
   */
  public TextTemplateEmailBuilder(String path, Session session) throws EmailException {
    super(path, session);
  }

  @Override
  public Email build(Map<String, Object> tokens, String[] to, String[] cc) throws EmailException {
    Email email = new SimpleEmail();
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", "text/plain; charset=utf-8");

    email.setHeaders(headerMap);
    email.setCharset("utf-8");

    for (String toAddress : to) {
      email.addTo(toAddress);
    }

    for (String ccAddress : cc) {
      email.addCc(ccAddress);
    }

    StringSubstitutor repl = new StringSubstitutor(tokens);
    final Object subject = props.get("subject");
    email.setSubject(repl.replace(subject == null ? "" : subject));
    StringBuilder msg = new StringBuilder();
    final Object header = props.get("header");
    msg.append(repl.replace(header == null ? "" : header));
    final Object messageObj = props.get("message");
    final String message = repl.replace(messageObj == null ? "" : messageObj);
    msg.append(message);
    final Object footer = props.get("footer");
    msg.append(repl.replace(footer == null ? "" : footer));
    email.setMsg(msg.toString());

    return email;
  }
}
