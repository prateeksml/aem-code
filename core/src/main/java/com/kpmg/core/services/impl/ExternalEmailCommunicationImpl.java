package com.kpmg.core.services.impl;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.services.ExternalEmailCommunication;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExternalEmailCommunicationImpl
 *
 * @author chay
 */
@Component(service = ExternalEmailCommunication.class, immediate = true)
@Designate(ocd = ExternalEmailCommunicationImpl.ExternalEmailCommunicationConfig.class)
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class ExternalEmailCommunicationImpl implements ExternalEmailCommunication {

  private static final Logger LOG = LoggerFactory.getLogger(ExternalEmailCommunicationImpl.class);

  private String production;
  private String kpmgTestEmail;

  /**
   * ExternalEmailCommunicationConfig
   *
   * @author chay
   */
  @ObjectClassDefinition(
      name = "KPMG: External Email Communication Configuration",
      description = "This service is used to retrieve External Email Communication Configuration")
  public @interface ExternalEmailCommunicationConfig {

    /**
     * Gets the Is Production Flag.
     *
     * @return getProduction
     */
    @AttributeDefinition(
        name = "Is Production",
        description =
            "Is Production Flag. Note: The flag value is set as per the run mode. "
                + "Please be cautious before changing the value, it is not recommended to update the value.")
    String getProduction() default "false";

    /**
     * Gets the KPMG Test Email ID..
     *
     * @return getKpmgTestEmail
     */
    @AttributeDefinition(name = "KPMG Test Email", description = "KPMG Test Email")
    String getKpmgTestEmail() default "kpmgliveservice@gmail.com";
  }

  @Activate
  @Modified
  protected void activate(ExternalEmailCommunicationConfig externalEmailCommunicationConfig) {
    LOG.info("Inside activate method of ExternalEmailCommunicationConfig Service");
    this.production = externalEmailCommunicationConfig.getProduction();
    this.kpmgTestEmail = externalEmailCommunicationConfig.getKpmgTestEmail();
    LOG.info("External Email Communication Configuration saved successfully.");
  }

  @Override
  public String getProduction() {
    return this.production;
  }

  @Override
  public String getKpmgTestEmail() {
    return this.kpmgTestEmail;
  }
}
