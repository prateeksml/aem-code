package com.kpmg.core.constants;

/** Global Constants Interface */
public final class GlobalConstants {

  public static final String PATH_CONTENT_ROOT = "/content";
  public static final String PATH_DAM_ROOT = PATH_CONTENT_ROOT + "/dam";
  public static final String SLASH = "/";
  public static final String SCENE7_BASE_URL = "is/image/";
  public static final String SCENE7_CONTENT_URL = "is/content/";
  public static final String IMAGE_PRESET_FORMAT = "?wid=%s&hei=%s";
  public static final String PLUS = "+";
  public static final String ENCODED_PLUS_CHARS = "%20";
  public static final int INTEGER_FOUR = 4;
  public static final String SEMICOLON = ";";
  public static final String URL_SUFFIX_HTML = ".html";
  public static final String DM_CONFIG_PATH =
      "/conf/kpmg/settings/cloudconfigs/dmscene7/jcr:content";
  public static final String SVG_EXTENSION = ".svg";
  public static final String COLON = ":";

  public static final char HYPHEN = '-';
  public static final String JCR_CONTENT = "/jcr:content";
  public static final String CF_MASTER = "/master";
  public static final String FEATURED_IMAGE_PATH = "/jcr:content/cq:featuredimage";
  public static final String PN_FILE_REFERENCE = "fileReference";
  public static final String PN_EVENT_START_TIME = "eventStartTimeAndDate";
  public static final String PN_EVENT_END_TIME = "eventEndTimeAndDate";
  public static final String PN_EVENT_TIMEZONE = "timeZone";

  private GlobalConstants() {}
}
