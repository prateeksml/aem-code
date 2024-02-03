package com.kpmg.integration.util;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.constants.SmartLogicConstants;
import com.kpmg.integration.services.SmartLogic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartlogicUtil {
  private static final Logger LOG = LoggerFactory.getLogger(SmartlogicUtil.class);

  public static String getRuleBaseClass(String termClass) {
    String ruleBaseClass = "";
    switch (termClass) {
      case SmartLogicConstants.AutoClassification.TAG_INDUSTRIES:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY;
        break;
      case SmartLogicConstants.AutoClassification.TAG_SERVICES:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE;
        break;
      case SmartLogicConstants.AutoClassification.TAG_GEOGRAPHY:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_GEOGRAPHY;
        break;
      case SmartLogicConstants.AutoClassification.TAG_INSIGHT:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT;
        break;
      case SmartLogicConstants.AutoClassification.TAG_MARKETS:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MARKET;
        break;
      case SmartLogicConstants.AutoClassification.TAG_MEDIA:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MEDIAFORMATS;
        break;
      case SmartLogicConstants.AutoClassification.TAG_PERSONA:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_PERSONA;
        break;
      case SmartLogicConstants.AutoClassification.TAG_CONTENTTYPE:
        ruleBaseClass = SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_CONTENTTYPE;
        break;
      default:
        break;
    }
    return ruleBaseClass;
  }

  public static String getTermClass(String termClass) {
    if (StringUtils.isNotBlank(termClass)) {
      termClass = StringUtils.remove(termClass, " (common)");
      termClass = StringUtils.remove(termClass, " (local)");
      if ("Content Type".equalsIgnoreCase(termClass)) {
        termClass = SmartLogicConstants.AutoClassification.TAG_CONTENTTYPE;
      } else if ("Personas".equalsIgnoreCase(termClass)) {
        termClass = SmartLogicConstants.AutoClassification.TAG_PERSONA;
      } else if ("Media format type".equalsIgnoreCase(termClass)) {
        termClass = SmartLogicConstants.AutoClassification.TAG_MEDIA;
      }
    }
    return termClass;
  }

  public static List<String> getTagIDsFromPageProperties(Page currentPage) {
    List<String> propertyValues = new ArrayList<>();

    if (null != currentPage.getProperties().get("sl-geography-hierarchy-id", String[].class)) {
      propertyValues.addAll(
          Arrays.asList(
              currentPage.getProperties().get("sl-geography-hierarchy-id", String[].class)));
    }
    if (null != currentPage.getProperties().get("sl-industry-hierarchy-id", String[].class)) {
      propertyValues.addAll(
          Arrays.asList(
              currentPage.getProperties().get("sl-industry-hierarchy-id", String[].class)));
    }
    if (null != currentPage.getProperties().get("sl-market-hierarchy-id", String[].class)) {
      propertyValues.addAll(
          Arrays.asList(currentPage.getProperties().get("sl-market-hierarchy-id", String[].class)));
    }
    if (null != currentPage.getProperties().get("sl-service-hierarchy-id", String[].class)) {
      propertyValues.addAll(
          Arrays.asList(
              currentPage.getProperties().get("sl-service-hierarchy-id", String[].class)));
    }
    if (null != currentPage.getProperties().get("sl-insight-hierarchy-id", String[].class)) {
      propertyValues.addAll(
          Arrays.asList(
              currentPage.getProperties().get("sl-insight-hierarchy-id", String[].class)));
    }

    return propertyValues;
  }

  public static String[] getTagsArray(List<String> propertyValues) {
    List<String> ids = new ArrayList<>();
    for (String property : propertyValues) {
      ids.addAll(Arrays.asList(property.split("/")));
    }
    return ids.toArray(new String[ids.size()]);
  }

  /*
   * Returns zthesid of given category
   */
  public static String getZid(
      String categoryType,
      String pagePath,
      SmartLogic smartLogicConfig,
      ResourceResolverFactory resolverFactory) {
    SiteSettingsConfig sitesettings;
    try {
      sitesettings =
          KPMGUtilities.getContextAwareConfig(
              pagePath, KPMGUtilities.getResourceResolverFromPool(resolverFactory));
      LOG.debug("page path :{}", pagePath);
      String zthesID = "";
      switch (categoryType) {
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_CONTENTTYPE:
          zthesID = smartLogicConfig.getContentTypeZthesID();
          break;
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MEDIAFORMATS:
          zthesID = smartLogicConfig.getMediaFormatsZthesID();
          break;
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_PERSONA:
          zthesID = smartLogicConfig.getPersonasZthesID();
          break;
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_GEOGRAPHY:
          zthesID = smartLogicConfig.getGeopraphyZthesID();
          break;
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MARKET:
          zthesID = smartLogicConfig.getMarketsZthesID();
          break;
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY:
          zthesID =
              pagePath.contains(SmartLogicConstants.CommonUtil.MASTER)
                  ? smartLogicConfig.getIndustryZthesID()
                  : sitesettings.smartLogic().localModelIndustryZthesID(); // site settings
          break;
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE:
          zthesID =
              pagePath.contains(SmartLogicConstants.CommonUtil.MASTER)
                  ? smartLogicConfig.getServiceZthesID()
                  : sitesettings.smartLogic().localModelServiceZthesID(); // site settings
          break;
        case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT:
          zthesID = smartLogicConfig.getInsightZthesID();
          break;

        default:
          break;
      }
      return zthesID;
    } catch (LoginException e) {
      LOG.error(
          "Login Exception occured while trying to fetch resource resolver {}", e.getMessage(), e);
    }
    return StringUtils.EMPTY;
  }

  public static Map<String, String> getStartTermIDs(
      String pagePath, SmartLogic smartLogicConfig, ResourceResolverFactory resolverFactory) {
    Map<String, String> startTermIDs = new HashMap<>();

    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.CONTENT_TYPE,
        getZid(
            SmartLogicConstants.SmartLogicClassification.CONTENT_TYPE,
            pagePath,
            smartLogicConfig,
            resolverFactory));
    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MEDIAFORMATS,
        getZid(
            SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MEDIAFORMATS,
            pagePath,
            smartLogicConfig,
            resolverFactory));
    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_PERSONA,
        getZid(
            SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_PERSONA,
            pagePath,
            smartLogicConfig,
            resolverFactory));
    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_GEOGRAPHY,
        getZid(
            SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_GEOGRAPHY,
            pagePath,
            smartLogicConfig,
            resolverFactory));
    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MARKET,
        getZid(
            SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MARKET,
            pagePath,
            smartLogicConfig,
            resolverFactory));
    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY,
        getZid(
            SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY,
            pagePath,
            smartLogicConfig,
            resolverFactory));
    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE,
        getZid(
            SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE,
            pagePath,
            smartLogicConfig,
            resolverFactory));
    startTermIDs.put(
        SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT,
        getZid(
            SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT,
            pagePath,
            smartLogicConfig,
            resolverFactory));

    return startTermIDs;
  }
}
