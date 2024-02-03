package com.kpmg.core.services.impl;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.EmailTemplates;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class EmailTemplatesConfigImpl. */
@Component(service = EmailTemplates.class, immediate = true)
@Designate(ocd = EmailTemplatesConfigImpl.EmailTemplatesConfig.class)
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class EmailTemplatesConfigImpl implements EmailTemplates {

  private static final Logger LOG = LoggerFactory.getLogger(EmailTemplatesConfigImpl.class);

  private String[] emailTemplates;

  @ObjectClassDefinition(
      name = "KPMG: Email Templates Configuration",
      description = "This service is used to retrieve email templates.")
  public @interface EmailTemplatesConfig {

    @AttributeDefinition(
        name = "Email Templates Paths",
        description = "This is used to set the path of the email templates.")
    public String[] emailTemplates();
  }

  /**
   * Activate.
   *
   * @param config the config
   */
  @Activate
  @Modified
  protected void activate(EmailTemplatesConfig config) {
    LOG.info("Inside activate method of EmailTemplatesConfig Service");
    this.emailTemplates = config.emailTemplates();
    LOG.info("EmailTemplatesConfig Configuration saved successfully.");
  }

  /**
   * Gets the email templates.
   *
   * @return the email templates
   */
  private Map<String, String> getEmailTemplates() {
    final Map<String, String> values = new HashMap<>();
    if (ArrayUtils.isNotEmpty(emailTemplates)) {
      for (int i = 0; i <= emailTemplates.length - 1; i++) {
        final String[] paths = StringUtils.split(emailTemplates[i], "|");
        values.put(paths[0], paths[1]);
      }
    }

    return values;
  }

  /**
   * Gets the email template path.
   *
   * @param key the key
   * @return the email template path
   */
  @Override
  public String getEmailTemplatePath(String key) {
    final Map<String, String> emailTemplatePaths = getEmailTemplates();
    return emailTemplatePaths.containsKey(key) ? emailTemplatePaths.get(key) : StringUtils.EMPTY;
  }
}
