package com.kpmg.integration.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.models.FormText;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {FormText.class, ComponentExporter.class},
    resourceType = FormTextImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FormTextImpl implements FormText {
  public static final String RESOURCE_TYPE = "kpmg/components/content/form/text";

  @Inject Page currentPage;

  @Inject ResourceResolver resourceResolver;

  private String alphabets;
  private String alphanumeric;
  private String numeric;
  private String email;
  private String phone;

  @ValueMapValue(name = "validationRegex")
  private String regex;

  @ValueMapValue(name = "isCustomRegex")
  private boolean regexcheckbox;

  @ValueMapValue(name = "customRegex")
  private String customRegex;

  @ValueMapValue(name = "regexMessage")
  private String regexMessage;

  @ValueMapValue(name = "maxlength")
  private String maxcharlength;

  public SiteSettingsConfig getContextAwareConfig(
      String currentPage, ResourceResolver resourceresolver) {
    String currentpath = StringUtils.isNotBlank(currentPage) ? currentPage : StringUtils.EMPTY;
    Resource currentresource = resourceresolver.getResource(currentpath);
    if (currentresource != null) {
      ConfigurationBuilder confBuilder = currentresource.adaptTo(ConfigurationBuilder.class);
      if (confBuilder != null) {
        return confBuilder.as(SiteSettingsConfig.class);
      }
    }
    return null;
  }

  @PostConstruct
  public void init() {
    SiteSettingsConfig fm = getContextAwareConfig(currentPage.getPath(), resourceResolver);
    alphabets = fm.regexMapping().alphabets();
    alphanumeric = fm.regexMapping().alphanumeric();
    email = fm.regexMapping().email();
    phone = fm.regexMapping().phone();
    numeric = fm.regexMapping().numeric();
  }

  @Override
  public String getAlphabets() {
    return alphabets;
  }

  @Override
  public String getAlphaNumeric() {
    return alphanumeric;
  }

  @Override
  public String getNumeric() {
    return numeric;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getPhone() {
    return phone;
  }

  @Override
  public Boolean getRegexCheckbox() {
    return regexcheckbox;
  }

  @Override
  public String getCustomRegex() {
    return customRegex;
  }

  @Override
  public String getRegexMessage() {
    return regexMessage;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  private String value;

  @Override
  public String getRegexMapping() {
    if (regex.equals("alphabets")) {
      value = getAlphabets();
    } else if (regex.equals("numeric")) {
      value = getNumeric();
    } else if (regex.equals("alphanumeric")) {
      value = getAlphaNumeric();
    }
    return value;
  }

  @Override
  public String getMaxCharLength() {
    return maxcharlength;
  }
}
