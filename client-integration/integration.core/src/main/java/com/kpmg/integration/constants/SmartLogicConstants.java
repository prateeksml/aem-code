package com.kpmg.integration.constants;

/**
 * Constants for AutoClassification,SmartLogicClassification,CommonUtil, TermDetailsExtractionUtil
 *
 * @author jmarti
 */
public interface SmartLogicConstants {

  public interface AutoClassification {

    String TAG_GEOGRAPHY = "Geography";
    String TAG_INDUSTRIES = "Industries";
    String TAG_MARKETS = "Markets";
    String TAG_MEDIA = "Media";
    String TAG_PERSONA = "Persona";
    String TAG_CONTENTTYPE = "Content";
    String TAG_SERVICES = "Services";
    String TAG_INSIGHTS = "Insights";
    String TAG_INSIGHT = "Insight";
    String TAG_ISO3166 = "Iso3166";
    String TAG_ISO31662 = "Iso31662";
    String TAG_ISO31663 = "Iso31663";
    String TAG_UNM49_REGION = "Unm49Region";
    String TAG_UNM49_SUB_REGION = "Unm49SubRegion";
    String TAG_UNM49_SUB_SUB_REGION = "Unm49SubSubRegion";
    String TAG_ID = "Id";
    String TAG_KEYWORDS = "Keywords";
    String TAG_TITLE_PATH = "Path";
    String TAG_QUALIFIED_PATH = "QualifiedName";
    String TAG_TITLE = "Title";
    String CS_CONFIGURATION_THRESHOLD = "threshold";
    String CS_CONFIGURATION_LANGUAGE = "language";
    String CONTENT_TYPE_RESPONCE = "application/json";
    String CONTENT_TYPE_CHARACTER_ENCODING = "utf-8";
    String TAG_CATEGORY_TYPE = "categoryType";
    String LOCAL_TAG_ID = "LocalId";
    String LOCAL_TAG_KEYWORDS = "LocalKeywords";
    String LOCAL_TAG_PATH = "LocalPath";
    String IDPATH = "idPath";
    String IDDISPLAYPATH = "idDisplayPath";
    String LOCALIDPATH = "localIdPath";
  }

  public interface SmartLogicClassification {
    String CONTENT_TYPE = "contenttype";
    String MEDIA_FORMATS = "mediaformats";
    String PERSONA = "persona";
    String ALL = "all";
    String GEOGRAPHY = "geography";
    String INDUSTRY = "industry";
    String SERVICE = "service";
    String INSIGHT = "insight";
    String MARKETS = "market";
    String RULE_BASE_CLASS_CONTENTTYPE = "contenttype";
    String RULE_BASE_CLASS_MEDIAFORMATS = "mediaformats";
    String RULE_BASE_CLASS_PERSONA = "persona";
    String RULE_BASE_CLASS_GEOGRAPHY = "geography";
    String RULE_BASE_CLASS_INDUSTRY = "industry";
    String RULE_BASE_CLASS_SERVICE = "service";
    String RULE_BASE_CLASS_INSIGHT = "insight";
    String RULE_BASE_CLASS_MARKET = "market";
  }

  public interface CommonUtil {
    String PAGE_PATH = "pagePath";
    String ISO3166 = "UF - ISO-3166-1-Numeric";
    String ISO31662 = "UF - ISO-3166-1-Alpha-2";
    String ISO31663 = "UF - ISO-3166-1-Alpha-3";
    String UNM49_REGION = "UF - UNM49-1";
    String UNM49_SUB_REGION = "UF - UNM49-2";
    String UNM49_SUB_SUB_REGION = "UF - UNM49-3";
    String TITLE = "title";
    String MASTER = "/language-masters/";
    String MAPS_TO_COMMON = "maps to common";
  }

  public interface TermDetailsExtractionUtil {
    String UNM49 = "UNM49";
    String ISO = "ISO";
    String LOCAL = "Local";
    String FORWARD_IDENTIFIER = "/";
    String PATH = "path";
    String DISPLAYPATH = "displaypath";
    String KEYWORD = "keyword";
    String PATH_STRING = "pathString";
    String ID = "id";
    String IDPATH = "idPath";
    String IDDISPLAYPATH = "idDisplayPath";
    String TITLE_PATH = "titlePath";
    String TITLE = "title";
    String TAG_ID = "tagID";
  }
}
