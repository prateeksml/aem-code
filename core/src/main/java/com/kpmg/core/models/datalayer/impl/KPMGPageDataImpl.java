package com.kpmg.core.models.datalayer.impl;

import com.adobe.cq.wcm.core.components.models.datalayer.PageData;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.constants.NameConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kpmg.core.constants.KPMGConstants;
import com.kpmg.core.models.datalayer.KPMGPageData;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/** Datalayer Page Data Impl to support custom datalayer properties for KPMG */
@Slf4j
public class KPMGPageDataImpl extends KPMGComponentDataImpl implements KPMGPageData {

  private static final String EFFECTIVE_DATE = "articleTimeAndDate";
  private static final String OWNER = "owner";

  private static final String SLASH = "/";
  private static final String COLON = ":";

  private interface Exclusions {
    String getJson();
  }

  @Delegate(excludes = Exclusions.class)
  private PageData pageData;

  Optional<Page> currentPage;

  public KPMGPageDataImpl(PageData pageData, Page currentPage) {
    super(pageData);
    this.pageData = pageData;
    this.currentPage = Optional.ofNullable(currentPage);
  }

  public String getPageName() {
    return currentPage
        .map(Page::getPath)
        .map(path -> StringUtils.removeStart(path, KPMGConstants.PATH_KPMG_CONTENT_ROOT + SLASH))
        .map(path -> StringUtils.replace(path, SLASH, COLON))
        .orElse(StringUtils.EMPTY);
  }

  public String getExpiryDate() {
    Calendar offTime = currentPage.map(Page::getOffTime).orElse(null);
    return getDateFormat(offTime);
  }

  public String getIssueDate() {
    return getFormattedDatePageProperty(NameConstants.PN_PAGE_LAST_REPLICATED);
  }

  public String getEffectiveDate() {
    return getFormattedDatePageProperty(EFFECTIVE_DATE);
  }

  public String getOwner() {
    return getPageProperty(OWNER, String.class);
  }

  /*
   * Get country from current page path
   * Country is the first path segment after /content/kpmgpublic.
   * Ex: /content/kpmgpublic/de/en/home.html -> "de"
   * EX: /content/kpmgpublic/ca/en/home.html -> "ca"
   * EX: /content/somethingelse/us/en/home.html -> ""
   */
  public String getCountry() {
    return currentPage
        .map(Page::getPath)
        .map(
            path ->
                StringUtils.substringBetween(
                    path, KPMGConstants.PATH_KPMG_CONTENT_ROOT + SLASH, SLASH))
        .orElse(StringUtils.EMPTY);
  }

  public String getPrimaryCategory() {
    return getAbsoluteParentName(4);
  }

  public String getSubCategory1() {
    return getAbsoluteParentName(5);
  }

  public String getSubCategory2() {
    return getAbsoluteParentName(6);
  }

  public String getSubCategory3() {
    return getAbsoluteParentName(7);
  }

  /** Get property as a formatted date */
  public String getFormattedDatePageProperty(String key) {
    Calendar cal = getPageProperty(key, Calendar.class);
    return getDateFormat(cal);
  }

  /** Page property from current page */
  public <T> T getPageProperty(String key, Class<T> type) {
    return currentPage
        .map(Page::getProperties)
        .map(properties -> properties.get(key, type))
        .orElse(null);
  }

  /** Get current page parent name at absolute level */
  public String getAbsoluteParentName(int level) {
    return currentPage
        .map(p -> p.getAbsoluteParent(level))
        .map(Page::getName)
        .orElse(StringUtils.EMPTY);
  }

  /** Simple format Date (migrated to be the same as on-prem for analytics) */
  public String getDateFormat(Calendar cal) {
    return Optional.ofNullable(cal)
        .map(Calendar::getTime)
        .map(date -> new SimpleDateFormat("MM/dd/yyyy").format(date))
        .orElse(StringUtils.EMPTY);
  }

  /** Overrides the default implementation to provide custom data layer properties for KPMG Page. */
  public final String getJson() {
    if (pageData == null) return StringUtils.EMPTY;
    try {
      return String.format(
          "{\"%s\":%s}", pageData.getId(), getObjectMapper().writeValueAsString(this));
    } catch (JsonProcessingException var2) {
      log.error("Unable to generate dataLayer JSON string", var2);
      return null;
    }
  }
}
