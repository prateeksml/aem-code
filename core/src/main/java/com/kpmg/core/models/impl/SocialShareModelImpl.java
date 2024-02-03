package com.kpmg.core.models.impl;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.core.models.SocialShareModel;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {SocialShareModel.class},
    resourceType = {SocialShareModelImpl.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SocialShareModelImpl extends AbstractKPMGComponentImpl implements SocialShareModel {
  protected static final String RESOURCE_TYPE = "kpmg/components/content/social-share";

  static final String OG_TITLE = "metaOgTitle";
  static final String OG_URL = "og:url";
  static final String OG_TYPE = "og:type";
  static final String OG_SITE_NAME = "og:site_name";
  static final String OG_IMAGE = "og:image";
  static final String OG_DESCRIPTION = "metaOgDescription";

  private static final String FACEBOOK = "facebook";

  private static final String TWITTER = "twitter";

  private static final String LINKEDIN = "linkedin";

  @Getter @ValueMapValue private String label;

  private static final Logger LOG = LoggerFactory.getLogger(SocialShareModelImpl.class);

  private boolean facebookEnabled = false;
  private boolean twitterEnabled = false;
  private boolean linkedinEnabled = false;
  private boolean socialMediaEnabled = false;

  private String pageTitle;
  private String pageUrl;

  @ScriptVariable Page currentPage;
  @SlingObject public ResourceResolver resourceResolver;
  @OSGiService Externalizer externalizer;

  @PostConstruct
  void postConstruct() {

    pageTitle = currentPage.getProperties().get(OG_TITLE, "");
    externalizer = resourceResolver.adaptTo(Externalizer.class);
    pageUrl = externalizer.publishLink(resourceResolver, currentPage.getPath()) + ".html";
    final SiteSettingsConfig config = getContextAwareConfig();
    if (config == null) {
      return;
    }
    label = config.shareLabel();
    if (label.isEmpty()) {
      label = "Share";
    }
    facebookEnabled = config.enableFacebook();
    linkedinEnabled = config.enableLinkedin();
    twitterEnabled = config.enableTwitter();
    if (facebookEnabled || linkedinEnabled || twitterEnabled) {
      socialMediaEnabled = true;
    }
  }

  public SiteSettingsConfig getContextAwareConfig() {
    final Resource resource = resourceResolver.getResource(currentPage.getPath());
    if (resource == null) {
      LOG.error("Not able to get the resource from the page  {}", currentPage.getPath());
      return null;
    }
    final ConfigurationBuilder configurationBuilder = resource.adaptTo(ConfigurationBuilder.class);
    if (configurationBuilder != null) {
      return configurationBuilder.as(SiteSettingsConfig.class);
    }
    LOG.error("Not able to get the SiteSelectorConfig for the page{}", currentPage.getPath());
    return null;
  }

  @Override
  public String getPageUrl() {
    return pageUrl;
  }

  @Override
  public boolean isFacebookEnabled() {

    return facebookEnabled;
  }

  @Override
  public boolean isLinkedinEnabled() {
    return linkedinEnabled;
  }

  @Override
  public boolean isTwitterEnabled() {
    return twitterEnabled;
  }

  @Override
  public boolean isSocialMediaEnabled() {
    return socialMediaEnabled;
  }

  @Override
  public String getMetaOgTitle() {
    return pageTitle;
  }

  public static String getResourceType() {
    return RESOURCE_TYPE;
  }

  public static String getOgTitle() {
    return OG_TITLE;
  }

  public static String getOgUrl() {
    return OG_URL;
  }

  public static String getOgType() {
    return OG_TYPE;
  }

  public static String getOgSiteName() {
    return OG_SITE_NAME;
  }

  public static String getOgImage() {
    return OG_IMAGE;
  }

  public static String getOgDescription() {
    return OG_DESCRIPTION;
  }

  public static String getFacebook() {
    return FACEBOOK;
  }

  public static String getTwitter() {
    return TWITTER;
  }

  public static String getLinkedin() {
    return LINKEDIN;
  }

  public static Logger getLog() {
    return LOG;
  }

  public String getPageTitle() {
    return pageTitle;
  }

  public Page getCurrentPage() {
    return currentPage;
  }

  public ResourceResolver getResourceResolver() {
    return resourceResolver;
  }

  public Externalizer getExternalizer() {
    return externalizer;
  }
}
