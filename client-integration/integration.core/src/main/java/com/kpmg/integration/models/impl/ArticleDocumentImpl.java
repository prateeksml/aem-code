package com.kpmg.integration.models.impl;

import static com.day.cq.wcm.api.constants.NameConstants.PN_PAGE_LAST_MOD;
import static com.kpmg.integration.constants.Constants.INSIGHT;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpmg.core.caconfig.DateFormatConfig;
import com.kpmg.core.dateformat.DateFormatService;
import com.kpmg.core.dateformat.DateFormatsVO;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = Resource.class,
    adapters = {PageDocument.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleDocumentImpl extends GenericDocumentImpl {

  @SlingObject private SlingHttpServletRequest request;

  @OSGiService DateFormatService dateFormatService;

  @JsonIgnore
  @Getter
  @ValueMapValue(name = "articleTimeAndDate")
  private Date injectedArticleDate;

  @JsonProperty("article_date")
  @Getter
  private String articleDate;

  @JsonProperty("formatted_article_date")
  @Getter
  private String formattedArticleDate;

  @JsonProperty("article_type")
  @Getter
  @ValueMapValue(name = "articleType")
  private String articleType;

  @JsonProperty("article_primary_format")
  @Getter
  @ValueMapValue(name = "articlePrimaryFormat")
  private String articlePrimaryFormat;

  @JsonProperty("article_contributors")
  @Getter
  @ValueMapValue(name = "contactPath")
  private String[] articleContributors;

  @JsonIgnore
  @ValueMapValue(name = PN_PAGE_LAST_MOD)
  private Date genericLastModified;

  @Getter
  @JsonProperty("filter_date")
  private String filterDate;

  @JsonIgnore
  @ValueMapValue(name = PN_PAGE_LAST_MOD)
  private Date articleLastModified;

  @Getter
  @JsonProperty("filter_year")
  private int filterYear;

  @Override
  public String getDocumentType() {
    return INSIGHT;
  }

  @PostConstruct
  protected void init() {
    super.init();
    this.articleDate = KPMGUtilities.processDate(injectedArticleDate);
    if (null != articleDate) {
      this.filterDate = articleDate;
      this.filterYear = KPMGUtilities.processYear(filterDate);
    }
    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    Page page =
        Optional.ofNullable(pageManager).map(pm -> pm.getContainingPage(resource)).orElse(null);
    Locale locale = KPMGUtilities.getLocale(page);
    DateFormatConfig dateFormatConfig = KPMGUtilities.getDateFormatConfig(page);
    DateFormatsVO dateFormatsVO =
        Optional.ofNullable(dateFormatService)
            .map(df -> df.getDateFormatVo(dateFormatConfig))
            .orElse(null);
    if (null != dateFormatsVO) {
      this.formattedArticleDate =
          Optional.ofNullable(dateFormatService)
              .map(df -> df.formatDate(injectedArticleDate, dateFormatsVO, locale))
              .orElse(null);
    }
  }
}
