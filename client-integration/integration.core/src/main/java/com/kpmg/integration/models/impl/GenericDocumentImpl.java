package com.kpmg.integration.models.impl;

import static com.day.cq.commons.jcr.JcrConstants.JCR_DESCRIPTION;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;
import static com.day.cq.wcm.api.constants.NameConstants.PN_PAGE_LAST_MOD;
import static com.kpmg.integration.constants.Constants.GENERIC;

import com.day.cq.commons.Externalizer;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpmg.core.caconfig.DateFormatConfig;
import com.kpmg.core.dateformat.DateFormatService;
import com.kpmg.core.dateformat.DateFormatsVO;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.services.GetHTMLService;
import com.kpmg.integration.util.KPMGUtilities;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.jcr.query.Query;
import javax.servlet.ServletException;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = Resource.class,
    adapters = {PageDocument.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GenericDocumentImpl implements PageDocument {

  private static final Logger log = LoggerFactory.getLogger(GenericDocumentImpl.class);

  @SlingObject ResourceResolver resourceResolver;

  @SlingObject private SlingHttpServletRequest request;

  @SlingObject Resource resource;

  @OSGiService Externalizer externalizer;

  @OSGiService private GetHTMLService getHTMLService;

  @OSGiService DateFormatService dateFormatService;

  @Getter
  @ValueMapValue(name = JCR_TITLE)
  private String title;

  @Getter
  @ValueMapValue(name = JCR_DESCRIPTION)
  private String description;

  @Getter
  @JsonProperty("document_id")
  @ValueMapValue(name = "documentId")
  private String documentId;

  @Getter
  @JsonProperty("filter_year")
  private int filterYear;

  @Getter
  @JsonProperty("filter_date")
  private String filterDate;

  @Getter
  @JsonProperty("formatted_filter_date")
  private String formattedFilterDate;

  @Getter
  @JsonProperty("qualified_url")
  private String qualifiedUrl;

  @JsonIgnore
  @Getter
  @ValueMapValue(name = "cq:featuredimage/fileReference")
  private String imageUrl;

  @Getter
  @JsonProperty("image_url")
  private String scene7Url;

  @Getter
  @ValueMapValue(name = "cq:featuredimage/alt")
  @JsonProperty("image_alt_text")
  private String imageAltText;

  @JsonProperty("document_type")
  private String documentType;

  @Getter
  @JsonProperty("body_content")
  private String bodyContent;

  @Getter
  @JsonProperty("keywords_list")
  private String keywordsList;

  @Getter
  @JsonProperty("last_modified")
  private String lastModified;

  @Getter
  @JsonProperty("formatted_last_modified")
  private String formattedLastModified;

  @Getter
  @JsonProperty("indexed_time")
  private String indexedTime;

  @Getter
  @ValueMapValue(name = "sl-content-zthesid")
  @JsonProperty("sl_content_zthesids")
  private String[] slContentZthesIds;

  @Getter
  @ValueMapValue(name = "sl-content-qualified-name")
  @JsonProperty("sl_content_qualified_name")
  private String[] slContentQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-content-qualified-name-id")
  @JsonProperty("sl_content_qualified_name_id")
  private String[] slContentQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-content-hierarchy")
  @JsonProperty("sl_content_hierarchy")
  private String[] slContentHierarchy;

  @Getter
  @ValueMapValue(name = "sl-content-hierarchy-id")
  @JsonProperty("sl_content_hierarchy_id")
  private String[] slContentHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-media-zthesid")
  @JsonProperty("sl_media_zthesids")
  private String[] slMediaZthesIds;

  @Getter
  @ValueMapValue(name = "sl-media-qualified-name")
  @JsonProperty("sl_media_qualified_name")
  private String[] slMediaQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-media-qualified-name-id")
  @JsonProperty("sl_media_qualified_name_id")
  private String[] slMediaQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-media-hierarchy")
  @JsonProperty("sl_media_hierarchy")
  private String[] slMediaHierarchy;

  @Getter
  @ValueMapValue(name = "sl-media-hierarchy-id")
  @JsonProperty("sl_media_hierarchy_id")
  private String[] slMediaHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-persona-zthesid")
  @JsonProperty("sl_persona_zthesids")
  private String[] slPersonaZthesIds;

  @Getter
  @ValueMapValue(name = "sl-persona-qualified-name")
  @JsonProperty("sl_persona_qualified_name")
  private String[] slPersonaQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-persona-qualified-name-id")
  @JsonProperty("sl_persona_qualified_name_id")
  private String[] slPersonaQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-persona-hierarchy")
  @JsonProperty("sl_persona_hierarchy")
  private String[] slPersonaHierarchy;

  @Getter
  @ValueMapValue(name = "sl-persona-hierarchy-id")
  @JsonProperty("sl_persona_hierarchy_id")
  private String[] slPersonaHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-industry-zthesid")
  @JsonProperty("sl_industry_zthesids")
  private String[] slIndustryZthesIds;

  @Getter
  @ValueMapValue(name = "sl-industry-common-zthesid")
  @JsonProperty("sl_industry_common_zthesids")
  private String[] slIndustryCommonZthesIds;

  @Getter
  @ValueMapValue(name = "sl-industry-qualified-name")
  @JsonProperty("sl_industry_qualified_name")
  private String[] slIndustryQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-industry-qualified-name-id")
  @JsonProperty("sl_industry_qualified_name_id")
  private String[] slIndustryQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-industry-hierarchy")
  @JsonProperty("sl_industry_hierarchy")
  private String[] slIndustryHierarchy;

  @Getter
  @ValueMapValue(name = "sl-industry-common-hierarchy")
  @JsonProperty("sl_industry_common_hierarchy")
  private String[] slIndustryCommonHierarchy;

  @Getter
  @ValueMapValue(name = "sl-industry-hierarchy-id")
  @JsonProperty("sl_industry_hierarchy_id")
  private String[] slIndustryHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-service-zthesid")
  @JsonProperty("sl_service_zthesids")
  private String[] slServiceZthesIds;

  @Getter
  @ValueMapValue(name = "sl-service-common-zthesid")
  @JsonProperty("sl_service_common_zthesids")
  private String[] slServiceCommonZthesIds;

  @Getter
  @ValueMapValue(name = "sl-service-qualified-name")
  @JsonProperty("sl_service_qualified_name")
  private String[] slServiceQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-service-qualified-name-id")
  @JsonProperty("sl_service_qualified_name_id")
  private String[] slServiceQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-service-hierarchy")
  @JsonProperty("sl_service_hierarchy")
  private String[] slServiceHierarchy;

  @Getter
  @ValueMapValue(name = "sl-service-common-hierarchy")
  @JsonProperty("sl_service_common_hierarchy")
  private String[] slServiceCommonHierarchy;

  @Getter
  @ValueMapValue(name = "sl-service-hierarchy-id")
  @JsonProperty("sl_service_hierarchy_id")
  private String[] slServiceHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-market-zthesid")
  @JsonProperty("sl_market_zthesids")
  private String[] slMarketZthesIds;

  @Getter
  @ValueMapValue(name = "sl-market-qualified-name")
  @JsonProperty("sl_market_qualified_name")
  private String[] slMarketQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-market-qualified-name-id")
  @JsonProperty("sl_market_qualified_name_id")
  private String[] slMarketQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-market-hierarchy")
  @JsonProperty("sl_market_hierarchy")
  private String[] slMarketHierarchy;

  @Getter
  @ValueMapValue(name = "sl-market-hierarchy-id")
  @JsonProperty("sl_market_hierarchy_id")
  private String[] slMarketHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-insight-zthesid")
  @JsonProperty("sl_insight_zthesids")
  private String[] slInsightZthesIds;

  @Getter
  @ValueMapValue(name = "sl-insight-common-zthesid")
  @JsonProperty("sl_insight_common_zthesids")
  private String[] slInsightCommonZthesIds;

  @Getter
  @ValueMapValue(name = "sl-insight-qualified-name")
  @JsonProperty("sl_insight_qualified_name")
  private String[] slInsightQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-insight-qualified-name-id")
  @JsonProperty("sl_insight_qualified_name_id")
  private String[] slInsightQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-insight-hierarchy")
  @JsonProperty("sl_insight_hierarchy")
  private String[] slInsightHierarchy;

  @Getter
  @ValueMapValue(name = "sl-insight-common-hierarchy")
  @JsonProperty("sl_insight_common_hierarchy")
  private String[] slInsightCommonHierarchy;

  @Getter
  @ValueMapValue(name = "sl-insight-hierarchy-id")
  @JsonProperty("sl_insight_hierarchy_id")
  private String[] slInsightHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-geography-zthesid")
  @JsonProperty("sl_geography_zthesids")
  private String[] slGeographyZthesIds;

  @Getter
  @ValueMapValue(name = "sl-geography-qualified-name")
  @JsonProperty("sl_geography_qualified_name")
  private String[] slGeographyQualifiedName;

  @Getter
  @ValueMapValue(name = "sl-geography-qualified-name-id")
  @JsonProperty("sl_geography_qualified_name_id")
  private String[] slGeographyQualifiedNameId;

  @Getter
  @ValueMapValue(name = "sl-geography-hierarchy")
  @JsonProperty("sl_geography_hierarchy")
  private String[] slGeographyHierarchy;

  @Getter
  @ValueMapValue(name = "sl-geography-hierarchy-id")
  @JsonProperty("sl_geography_hierarchy_id")
  private String[] slGeographyHierarchyId;

  @Getter
  @ValueMapValue(name = "sl-geography-iso3166")
  @JsonProperty("sl_geography_iso3166")
  private String[] slGeographyIso3166;

  @Getter
  @ValueMapValue(name = "sl-geography-iso3166-2")
  @JsonProperty("sl_geography_iso3166_2")
  private String[] slGeographyIso31662;

  @Getter
  @ValueMapValue(name = "sl-geography-iso3166-3")
  @JsonProperty("sl_geography_iso3166_3")
  private String[] slGeographyIso31663;

  @Getter
  @ValueMapValue(name = "sl-geography-unm49-region")
  @JsonProperty("sl_geography_unm49_region")
  private String[] slGeographyUnm49Region;

  @Getter
  @ValueMapValue(name = "sl-geography-unm49-subregion")
  @JsonProperty("sl_geography_unm49_subregion")
  private String[] slGeographyUnm49SubRegion;

  @Getter
  @ValueMapValue(name = "sl-geography-unm49-subsubregion")
  @JsonProperty("sl_geography_unm49_subsubregion")
  private String[] slGeographyUnm49SubSubRegion;

  @JsonIgnore
  @ValueMapValue(name = PN_PAGE_LAST_MOD)
  private Date genericLastModified;

  @Override
  public String getDocumentType() {
    return GENERIC;
  }

  @PostConstruct
  protected void init() {
    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    Page page =
        Optional.ofNullable(pageManager).map(pm -> pm.getContainingPage(resource)).orElse(null);

    String pagePath = Optional.ofNullable(page).map(Page::getPath).orElse(StringUtils.EMPTY);

    String htmlContent = getBodyContent(page, resourceResolver);
    if (StringUtils.isNotEmpty(htmlContent) && null != htmlContent) {
      this.bodyContent = htmlContent;
    }

    this.qualifiedUrl = externalizer.publishLink(resourceResolver, pagePath + ".html");
    this.lastModified = KPMGUtilities.processDate(genericLastModified);
    this.filterDate = lastModified;
    this.filterYear = KPMGUtilities.processYear(lastModified);
    Locale locale = KPMGUtilities.getLocale(page);
    DateFormatConfig dateFormatConfig = KPMGUtilities.getDateFormatConfig(page);
    DateFormatsVO dateFormatsVO =
        Optional.ofNullable(dateFormatService)
            .map(df -> df.getDateFormatVo(dateFormatConfig))
            .orElse(null);
    if (null != dateFormatsVO) {
      this.formattedFilterDate =
          Optional.ofNullable(dateFormatService)
              .map(df -> df.formatDate(genericLastModified, dateFormatsVO, locale))
              .orElse(null);
      this.formattedLastModified = formattedFilterDate;
    }

    Asset asset =
        Optional.ofNullable(resourceResolver.getResource(this.imageUrl))
            .map(res -> res.adaptTo(Asset.class))
            .orElse(null);
    if (null != asset) {
      String scene7Domain =
          Optional.ofNullable(asset.getMetadataValue("dam:scene7Domain")).orElse(StringUtils.EMPTY);
      String scene7File =
          Optional.ofNullable(asset.getMetadataValue("dam:scene7File")).orElse(StringUtils.EMPTY);
      if (StringUtils.isNotEmpty(scene7Domain) && StringUtils.isNotEmpty(scene7File)) {
        this.scene7Url =
            new StringBuilder(scene7Domain).append("is/image/").append(scene7File).toString();
      }
    }
  }

  private String getBodyContent(@NonNull Page actionPage, @NonNull ResourceResolver resolver) {
    final String query =
        "SELECT * FROM [nt:base] AS t WHERE ISDESCENDANTNODE(["
            + actionPage.getContentResource().getPath()
            + "]) AND (t.[searchIndexable] LIKE 'true')";

    final Iterator<Resource> pageContentResources = resolver.findResources(query, Query.JCR_SQL2);
    final StringBuilder sbAllText = new StringBuilder();
    while (pageContentResources.hasNext()) {
      final Resource fragmentResource = pageContentResources.next();
      String tempStr;
      try {
        tempStr = getHTMLService.getHTML(fragmentResource.getPath() + ".html", resourceResolver);
        if (StringUtils.isNotBlank(tempStr)) {
          sbAllText.append(tempStr);
        }
        sbAllText.trimToSize();
      } catch (ServletException | IOException e) {
        log.error("An error occurred during getBodyContent process => {}", e.getMessage());
      }
    }
    return Jsoup.parse(sbAllText.toString()).text();
  }
}
