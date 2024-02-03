package com.kpmg.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.core.caconfig.SiteSelectorConfig;
import com.kpmg.core.constants.KPMGConstants;
import com.kpmg.core.models.SiteSelector;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {SiteSelector.class, ComponentExporter.class},
    resourceType = {SiteSelectorImpl.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SiteSelectorImpl implements SiteSelector {
  protected static final String RESOURCE_TYPE = "kpmg/components/content/siteselector";
  protected static final String PATTERN = "/content/kpmgpublic/([^/]+)/([^/]+)/";
  @SlingObject public ResourceResolver resourceResolver;
  @ScriptVariable public Page currentPage;
  private static final Logger LOG = LoggerFactory.getLogger(SiteSelectorImpl.class);
  private List<SiteSelectorConfig.SiteSelectorItemConfig> countryItems;
  @Self protected LinkManager linkManager;
  @Self protected SlingHttpServletRequest request;

  @Override
  public Link getLink() {
    return linkManager.get(request.getResource()).build();
  }

  @PostConstruct
  public void postConstruct() {
    final SiteSelectorConfig config = getContextAwareConfig();
    if (config != null) {
      countryItems = List.of(config.items());
    }
  }

  public SiteSelectorConfig getContextAwareConfig() {
    final Resource contentResource = resourceResolver.getResource(currentPage.getPath());
    if (contentResource == null) {
      LOG.error("Not able to get the resource from the page @ {}", currentPage.getPath());
      return null;
    }
    final ConfigurationBuilder configurationBuilder =
        contentResource.adaptTo(ConfigurationBuilder.class);
    if (configurationBuilder != null) {
      return configurationBuilder.as(SiteSelectorConfig.class);
    }
    LOG.error("Not able to get the SiteSelectorConfig for the page @ {}", currentPage.getPath());
    return null;
  }

  @Override
  public List<SiteSelectorBean> getSiteSelectorItems() {
    if (countryItems == null) {
      return List.of();
    }
    List<SiteSelectorBean> siteSelectorItems =
        countryItems.stream()
            .map(this::createSiteSelectorBean)
            .sorted(
                Comparator.comparing(
                    bean -> bean.getSiteLink().contains(KPMGConstants.NN_KPMG_GLOBAL),
                    Comparator.reverseOrder()))
            .collect(Collectors.toList());
    return siteSelectorItems;
  }

  protected SiteSelectorBean createSiteSelectorBean(
      final SiteSelectorConfig.SiteSelectorItemConfig item) {
    final SiteSelectorBean siteBean = new SiteSelectorBean();
    siteBean.setCountry(item.country());
    siteBean.setLocale(StringUtils.upperCase(item.locale()));
    siteBean.setSiteLink(linkManager.get(item.siteLink()).build().getURL());
    return siteBean;
  }

  @Override
  public String getCurrentCountry() {
    final String pagePath =
        KPMGConstants.PATH_KPMG_CONTENT_ROOT
            + KPMGConstants.SLASH
            + extractPath(currentPage.getPath(), 1);
    final PageManager pageManager = currentPage.getPageManager();
    return pageManager.getPage(pagePath).getTitle();
  }

  @Override
  public String getCurrentLanguage() {
    return StringUtils.upperCase(extractPath(currentPage.getPath(), 2));
  }

  private static String extractPath(final String path, final int index) {
    final Matcher matcher = Pattern.compile(PATTERN).matcher(path);
    return matcher.find() ? matcher.group(index) : "";
  }

  @Override
  public List<SiteSelectorBean> getRelevantCountryCode() {
    final String currentCountry = extractPath(currentPage.getPath(), 1);
    final String currentLang = extractPath(currentPage.getPath(), 2);
    return getSiteSelectorItems().stream()
        .filter(
            siteBean ->
                StringUtils.startsWith(
                        siteBean.getSiteLink(),
                        KPMGConstants.PATH_KPMG_CONTENT_ROOT + KPMGConstants.SLASH + currentCountry)
                    && !currentLang.equals(extractPath(siteBean.getSiteLink(), 2)))
        .sorted(Comparator.comparing(SiteSelectorBean::getCountry))
        .collect(Collectors.toList());
  }

  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }
}
