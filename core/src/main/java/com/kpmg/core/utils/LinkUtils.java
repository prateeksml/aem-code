package com.kpmg.core.utils;

import com.kpmg.core.constants.KPMGConstants;
import org.apache.commons.lang3.StringUtils;

/** LinkUtils class. */
public final class LinkUtils {

  private LinkUtils() {}

  /**
   * Returns the content path URL with .html extension
   *
   * @param linkUrl
   * @return updatedLinkUrl
   */
  public static String getLinkUrl(final String linkUrl) {
    return (StringUtils.startsWith(linkUrl, KPMGConstants.PATH_CONTENT_ROOT)
            && !StringUtils.contains(linkUrl, KPMGConstants.PATH_DAM_ROOT)
            && !StringUtils.endsWith(linkUrl, KPMGConstants.URL_SUFFIX_HTML)
            && !StringUtils.endsWith(linkUrl, KPMGConstants.SLASH))
        ? StringUtils.join(linkUrl, KPMGConstants.URL_SUFFIX_HTML)
        : linkUrl;
  }
}
