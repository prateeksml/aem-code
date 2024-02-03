package com.kpmg.integration.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.models.SearchConfig;
import com.kpmg.integration.services.SearchConsumer;
import com.kpmg.integration.util.KPMGUtilities;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {SearchConfig.class, ComponentExporter.class},
    resourceType = SearchConfigImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchConfigImpl implements SearchConfig {

  private static final Logger LOG = LoggerFactory.getLogger(SearchConfigImpl.class);

  public static final String RESOURCE_TYPE = "kpmg/components/structure/search";

  @SlingObject protected Resource resource;

  @OSGiService SearchConsumer searchConsumer;

  @ScriptVariable private Page currentPage;

  private SiteSettingsConfig siteSettingsConfig;

  @Nonnull
  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }

  @PostConstruct
  public void postConstruct() {
    siteSettingsConfig =
        KPMGUtilities.getContextAwareConfig(currentPage.getPath(), resource.getResourceResolver());
    if (siteSettingsConfig != null) {
      LOG.debug("Successfully initialized site settings config" + siteSettingsConfig);
    } else {
      LOG.debug("Site Settings configurations could not be loaded.");
    }
  }

  @Override
  public String getCountryCode() {
    if (siteSettingsConfig != null) {
      return siteSettingsConfig.siteCountry();
    }
    return null;
  }

  @Override
  public String getLanguageCode() {
    if (siteSettingsConfig != null) {
      return siteSettingsConfig.siteLocale();
    }
    return null;
  }

  @Override
  public String getSearchPagePath() {
    if (siteSettingsConfig != null) {
      String searchPagePath = siteSettingsConfig.searchPagePath();
      LOG.debug("Search page path before resolution: " + searchPagePath);
      searchPagePath = resource.getResourceResolver().map(searchPagePath);
      if (!searchPagePath.endsWith(".html")) {
        searchPagePath = searchPagePath.concat(".html");
      }
      LOG.debug("Search page path after resolution: " + searchPagePath);
      return searchPagePath;
    }
    return null;
  }

  @Override
  public String getSearchEndpoint() {
    return searchConsumer.getSearchEndpoint();
  }

  @Override
  public String getSuggestEndpoint() {
    return searchConsumer.getSuggestEndpoint();
  }

  @Override
  public String getPeopleContactFormPath() {
    if (siteSettingsConfig != null) {
      return siteSettingsConfig.peopleForm().peopleContactFormPath();
    }
    return null;
  }

  @Override
  public int getPaginationLimit() {
    return searchConsumer.getPagination();
  }

  @Override
  public String getRunMode() {
    return searchConsumer.getWcmMode();
  }
}
