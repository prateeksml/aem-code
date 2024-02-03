package com.kpmg.core.email.internal;

import java.util.Map;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

/**
 * @author Pawan Mittal
 */
public abstract interface EMailBuilder {
  /**
   * @param tokens
   * @param to
   * @param cc
   * @return
   * @throws EmailException
   */
  public abstract Email build(Map<String, Object> tokens, String[] to, String[] cc)
      throws EmailException;
}
