package com.kpmg.core.services.impl;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.KPMGGlobal;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = KPMGGlobal.class, immediate = true)
@Designate(ocd = KPMGGlobalConfigImpl.KPMGGlobalConfig.class)
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class KPMGGlobalConfigImpl implements KPMGGlobal {

  private static final Logger LOG = LoggerFactory.getLogger(KPMGGlobalConfigImpl.class);

  private String peopleContactFormEmailFromAddress;
  private String genericContactFormEmailFromAddress;
  private String domainName;
  private String enableEmailNotifications;
  private String socailMediaAdminGroups;

  @ObjectClassDefinition(
      name = "KPMG: Global Config Configuration",
      description = "This service is used to retrieve Global Configuration")
  public @interface KPMGGlobalConfig {

    @AttributeDefinition(name = "domainName", description = "This is used to Set Domain Name")
    public String getDomainName();

    @AttributeDefinition(
        name = "enableEmailNotifications",
        description = "Enable Email Notifications")
    public String getEnableEmailNotifications() default "true";

    @AttributeDefinition(
        name = "peopleContactFormEmailFromAddress",
        description = "From Address of the email")
    public String getPeopleContactFormEmailFromAddress() default "noreply-contactus@mailkpmg.com";

    @AttributeDefinition(
        name = "genericContactFormEmailFromAddress",
        description = "From Address for the Generaic Contact Us Form email")
    public String getGenericContactFormEmailFromAddress() default "noreply-rfp@mailkpmg.com";

    @AttributeDefinition(
        name = "getSocailMediaAdminGroups",
        description = "Group that are allowed to edit social media account links in CF.")
    public String getSocailMediaAdminGroups() default
        "administrators|aem-global-site-administrators";
  }

  @Activate
  @Modified
  protected void activate(KPMGGlobalConfig config) {
    LOG.info("Inside activate method of KPMGGlobalConfig Service");
    this.peopleContactFormEmailFromAddress = config.getPeopleContactFormEmailFromAddress();
    this.genericContactFormEmailFromAddress = config.getGenericContactFormEmailFromAddress();
    this.domainName = config.getDomainName();
    this.enableEmailNotifications = config.getEnableEmailNotifications();
    this.socailMediaAdminGroups = config.getSocailMediaAdminGroups();
    LOG.info("KPMGGlobalConfig Configuration saved successfully.");
  }

  @Override
  public String getDomainName() {
    return this.domainName;
  }

  @Override
  public String getPeopleContactFormEmailFromAddress() {
    return this.peopleContactFormEmailFromAddress;
  }

  @Override
  public String getGenericContactFormEmailFromAddress() {
    return this.genericContactFormEmailFromAddress;
  }

  @Override
  public String getEnableEmailNotifications() {
    return this.enableEmailNotifications;
  }

  @Override
  public String getSocailMediaAdminGroups() {
    return this.socailMediaAdminGroups;
  }
}
