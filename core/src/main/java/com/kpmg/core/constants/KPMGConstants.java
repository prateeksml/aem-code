package com.kpmg.core.constants;

public class KPMGConstants {

  private KPMGConstants() {}

  public static final String SLASH = "/";
  public static final String PATH_CONTENT_ROOT = "/content";
  public static final String PATH_KPMG_CONTENT_ROOT = PATH_CONTENT_ROOT + "/kpmgpublic";
  public static final String PATH_DAM_ROOT = PATH_CONTENT_ROOT + "/dam";
  public static final String URL_SUFFIX_HTML = ".html";
  public static final String REGULAR_EXP = "\\<.*?\\>";
  public static final String NN_KPMG_GLOBAL = "xx";

  public static final String ARTICLE_TEMPLATE = "/conf/kpmg/settings/wcm/templates/page-article";
  public static final String CONTENT_TEMPLATE = "/conf/kpmg/settings/wcm/templates/page-content";
  public static final String CONTACT_TEMPLATE = "/conf/kpmg/settings/wcm/templates/page-contact";
  public static final String ALL_SERVICE_LANDING_TEMPLATE =
      "/conf/kpmg/settings/wcm/templates/page-service-landing";
  public static final String ALL_INDUSTRIES_LANDING_TEMPLATE =
      "/conf/kpmg/settings/wcm/templates/page-industries";
  public static final String EVENT_DETAIL_TEMPLATE = "/conf/kpmg/settings/wcm/templates/page-event";
}
