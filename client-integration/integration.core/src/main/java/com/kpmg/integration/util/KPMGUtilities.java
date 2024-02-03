package com.kpmg.integration.util;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.DateFormatConfig;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KPMGUtilities {

  private KPMGUtilities() {}

  public static final String KPMG_SEARCH_INDEXER_SERVICE_USER = "kpmgsearchindexerserviceuser";

  private static final Logger LOG = LoggerFactory.getLogger(KPMGUtilities.class);

  public static ResourceResolver getResourceResolverFromPool(
      ResourceResolverFactory resourceResolverFactory) throws LoginException {
    ResourceResolver resolver =
        resourceResolverFactory.getServiceResourceResolver(
            Collections.singletonMap(
                ResourceResolverFactory.SUBSERVICE, KPMG_SEARCH_INDEXER_SERVICE_USER));
    LOG.debug(
        "ResourceResolver {} obtained successfully through service user {} ",
        resolver.getUserID(),
        KPMG_SEARCH_INDEXER_SERVICE_USER);
    return resolver;
  }

  public static SiteSettingsConfig getContextAwareConfig(
      String path, ResourceResolver resourceResolver) {

    Resource contentResource = resourceResolver.getResource(path);
    if (contentResource != null) {
      ConfigurationBuilder configurationBuilder =
          contentResource.adaptTo(ConfigurationBuilder.class);
      if (configurationBuilder != null) {
        return configurationBuilder.as(SiteSettingsConfig.class);
      }
    }
    return null;
  }

  public static SiteSettingsConfig getSiteSettings(Page page) {
    return Optional.ofNullable(page)
        .map(Page::getContentResource)
        .map(pageResource -> (pageResource.adaptTo(ConfigurationBuilder.class)))
        .map(cb -> cb.as((SiteSettingsConfig.class)))
        .orElse(null);
  }

  /*public static FormfieldMappingConfig getFormfieldMappingConfig(Page formPage) {
    return Optional.ofNullable(formPage)
        .map(Page::getContentResource)
        .map(formResource -> formResource.adaptTo(ConfigurationBuilder.class))
        .map(cb -> cb.as((FormfieldMappingConfig.class)))
        .orElse(null);
  }*/

  public static DateFormatConfig getDateFormatConfig(Page page) {
    return Optional.ofNullable(page)
        .map(Page::getContentResource)
        .map(formResource -> formResource.adaptTo(ConfigurationBuilder.class))
        .map(cb -> cb.as((DateFormatConfig.class)))
        .orElse(null);
  }

  public static String processDate(Date date) {
    SimpleDateFormat GENERIC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    if (null != date) {
      return GENERIC_DATE_FORMAT.format(date);
    }
    return StringUtils.EMPTY;
  }

  public static int processYear(@NonNull String date) {
    final LocalDate localDate = LocalDate.parse(date.split("T")[0]);
    return localDate.getYear();
  }

  public static Locale getLocale(Page page) {
    return Optional.ofNullable(page)
        .map(Page::getContentResource)
        .map(formResource -> formResource.adaptTo(ConfigurationBuilder.class))
        .map(cb -> cb.as((SiteSettingsConfig.class)))
        .map(siteSettings -> new Locale(siteSettings.siteLocale(), siteSettings.siteCountry()))
        .orElse(Locale.ENGLISH);
  }

  public static Boolean getHideInSearch(Page page) {
    return Optional.ofNullable(page)
        .map(pg -> pg.getContentResource())
        .map(res -> res.getValueMap())
        .map(vm -> vm.get("hideInSearch", Boolean.class))
        .orElse(Boolean.FALSE);
  }
}
