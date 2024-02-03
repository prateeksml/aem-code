package com.kpmg.integration.models.impl;

import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.form.Container;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.models.FormContainer;
import com.kpmg.integration.services.ReCaptchaV2Service;
import com.kpmg.integration.services.ReCaptchaV3Service;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.*;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {FormContainer.class, ContainerExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = FormContainerImpl.RESOURCE_TYPE)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class FormContainerImpl implements FormContainer {

  public static final String RESOURCE_TYPE = "kpmg/components/content/form/container";

  public static final String DIGITAL_FORM_HANDLING_PATH = "/bin/kpmg/formhandler";

  private static final Logger LOG = LoggerFactory.getLogger(FormContainerImpl.class);

  @ScriptVariable private Page currentPage;

  @SlingObject ResourceResolver resourceResolver;

  @SlingObject Resource resource;

  @OSGiService ReCaptchaV3Service reCaptchaV3Service;

  @OSGiService ReCaptchaV2Service reCaptchaV2Service;

  @OSGiService Externalizer externalizer;

  @Getter private String formfieldMappingConfig;

  @Delegate(excludes = DelegateExclusions.class)
  @Self
  private Container container;

  @Getter @ValueMapValue private String formType;

  @Getter @ValueMapValue private String responseType;

  @Getter @ValueMapValue private String successTitle;
  @Getter @ValueMapValue private String successDescription;

  @Getter @ValueMapValue private boolean isFirstName;
  @Getter @ValueMapValue private String sysError;
  @Getter @ValueMapValue private String validationError;

  @Getter @ValueMapValue private String ctaLink;
  @Getter @ValueMapValue private String ctaLabel;

  @Getter @ValueMapValue private String formTitle;
  @Getter @ValueMapValue private String closeButtonTitle;
  @Getter @ValueMapValue private String loadingTitle;
  @Getter @ValueMapValue private String loadingSubtitle;
  @Getter @ValueMapValue private String loadingText;
  @Getter @ValueMapValue private String captchaScript;
  @Getter @ValueMapValue private String captchaVersion;
  @Getter @ValueMapValue private String captchaSize;

  @Getter private String ctaPublishLink;

  public String getSiteKeyV3() {
    if (null != reCaptchaV3Service) {
      return reCaptchaV3Service.getSiteKey();
    }
    return StringUtils.EMPTY;
  }

  public String getSiteSecretV3() {
    return reCaptchaV3Service.getSiteSecret();
  }

  @Override
  public String getSiteKeyV2() {
    return reCaptchaV2Service.getSiteKey();
  }

  @Override
  public String getSiteSecretV2() {
    return reCaptchaV2Service.getSiteSecret();
  }

  private interface DelegateExclusions {
    String getAction();
  }

  @Override
  public String getAction() {
    return DIGITAL_FORM_HANDLING_PATH;
  }

  @Override
  public String getResourcePath() {
    return this.resource.getPath();
  }

  @Override
  public String getAnalyticId() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  SiteSettingsConfig siteSettingsConfig;

  @Getter private String countryId;

  @PostConstruct
  protected void init() {
    Resource pageResource = resource;
    Map<String, String> configMap = new HashMap<>();
    Page formPage = null;
    this.ctaPublishLink = externalizer.publishLink(resourceResolver, this.ctaLink + ".html");

    if (pageResource.getPath().contains("experience-fragments")) {
      String[] currentPagePath =
          pageResource.getPath().replace("experience-fragments/", "").split("/", 6);
      String pagePath =
          String.join("/", Arrays.copyOf(currentPagePath, currentPagePath.length - 1))
              .concat("/home");
      LOG.debug("Form Resource Path 1: {}", pagePath);
      PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
      formPage = pageManager.getPage(pagePath);
    }
    if (null != formPage) {
      LOG.debug("Default Form Page Path : {}", formPage.getPath());
      SiteSettingsConfig.FormfieldMappingConfig formMapping =
          KPMGUtilities.getSiteSettings(formPage).formMapping();
      if (null != formMapping) {
        configMap.put("first_name", formMapping.firstName());
        configMap.put("last_name", formMapping.lastName());
        configMap.put("email_address", formMapping.email());
        configMap.put("country", formMapping.country());
        configMap.put("city", formMapping.city());
        configMap.put("phone", formMapping.phone());
        configMap.put("company", formMapping.company());
        configMap.put("role", formMapping.role());
        configMap.put("message", formMapping.message());
        configMap.put("inquiry", formMapping.inquiry());
        configMap.put("state", formMapping.state());
        configMap.put("industry", formMapping.industry());
        configMap.put("iskpmgclient", formMapping.iskpmgclient());
        configMap.put("attachment", formMapping.attachment());

        this.formfieldMappingConfig = configMap.toString();
        this.countryId =
            Optional.ofNullable(KPMGUtilities.getSiteSettings(formPage))
                .map(SiteSettingsConfig::rfpForm)
                .map(SiteSettingsConfig.RFPForm::rfpcountryID)
                .orElse(StringUtils.EMPTY);
      }
    }
  }

  @Override
  public Boolean isCaptchaEnabled() {
    if (null != reCaptchaV3Service
        && StringUtils.equalsIgnoreCase(reCaptchaV3Service.getCaptchaEnabled(), "true")) {
      return true;
    }
    return false;
  }
}
