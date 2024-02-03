package com.kpmg.integration.services.impl;

import com.day.cq.wcm.api.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.constants.SmartLogicConstants;
import com.kpmg.integration.helpers.TagSort;
import com.kpmg.integration.helpers.TermDetailsExtractionUtilVo;
import com.kpmg.integration.models.Tag;
import com.kpmg.integration.services.SmartLogic;
import com.kpmg.integration.services.SmartlogicService;
import com.kpmg.integration.util.KPMGUtilities;
import com.kpmg.integration.util.SmartlogicUtil;
import com.kpmg.integration.util.TermDetailsExtractionUtil;
import com.smartlogic.ses.client.SESClient;
import com.smartlogic.ses.client.SESClient.DetailLevel;
import com.smartlogic.ses.client.SESFilter;
import com.smartlogic.ses.client.Term;
import com.smartlogic.ses.client.exceptions.SESException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = SmartlogicService.class, immediate = true)
public class SmartlogicServiceImpl implements SmartlogicService {

  private static final Logger LOG = LoggerFactory.getLogger(SmartlogicServiceImpl.class);
  private static final String MODEL_PREFIX = "KPMG";
  private static final String MODEL_COMMON = "XX";
  private static final String MASTER = "/language-masters/";

  @Reference private SmartLogic smartLogicConfig;
  @Reference private ResourceResolverFactory resolverFactory;

  @Override
  public SESClient createSESClient(String modelName) {
    if (null != smartLogicConfig
        && null != smartLogicConfig.getSesClientHostIP()
        && null != smartLogicConfig.getSesClientPath()
        && null != smartLogicConfig.getSesPort()
        && null != smartLogicConfig.getSesClientProtocol()) {
      KPMGSESClient sesClient = new KPMGSESClient();
      sesClient.setHost(smartLogicConfig.getSesClientHostIP());
      sesClient.setPort(smartLogicConfig.getSesPort());
      sesClient.setOntology(modelName);
      sesClient.setPath(smartLogicConfig.getSesClientPath());
      sesClient.setProtocol(smartLogicConfig.getSesClientProtocol());
      sesClient.setConnectionTimeoutMS(10000);
      sesClient.setSocketTimeoutMS(10000);
      LOG.debug("sesclient :: {}", sesClient);
      return sesClient;
    }
    return null;
  }

  @Override
  public String getModelNameByPagePath(String pagePath) {
    LOG.debug("Inside method getSesClientByPagePath()");

    String modelName =
        null != pagePath && pagePath.split("/").length > 3 && !pagePath.contains(MASTER)
            ? (MODEL_PREFIX + (pagePath.split("/")[3].toUpperCase()))
            : (MODEL_PREFIX + MODEL_COMMON);
    LOG.debug("Model Name:{}", modelName);
    return modelName;
  }

  @Override
  public List<JsonObject> getAllTagsByCategory(String pagePath, String categoryType) {
    LOG.debug("inside getalltags");
    JsonArray jsonArray = new JsonArray();
    List<JsonObject> tagsList = new ArrayList<>();
    try {
      if (null != categoryType && null != smartLogicConfig) {
        String modelname = getModelNameByPagePath(pagePath);
        SESClient sesClient = createSESClient(modelname);
        if (sesClient != null) {
          sesClient.setLanguage(getSESLanguage(pagePath));
          if (categoryType.equalsIgnoreCase("all")) {
            return tagsList;
          } else {
            Map<String, String> zidMap = new HashMap<>();
            zidMap.put(
                categoryType,
                SmartlogicUtil.getZid(categoryType, pagePath, smartLogicConfig, resolverFactory));
            LOG.debug("*** zthesID map:: {}", zidMap);
            if (!(zidMap.get(categoryType).isBlank())) {
              jsonArray = getAllTagsDetailsFromSESByCategory(zidMap, sesClient);
              tagsList = new TagSort().sortSmartLogicTags(categoryType, jsonArray);
            } else {
              LOG.debug("zthesid is empty in config");
            }
          }
        } else {
          LOG.debug(
              "ses hostip: {} sespath : {} sesport: {} sesprotocol :{}",
              smartLogicConfig.getSesClientHostIP(),
              smartLogicConfig.getSesClientPath(),
              smartLogicConfig.getSesPort(),
              smartLogicConfig.getSesClientProtocol());
          LOG.debug("SES client is not created, empty values in SmartLogic configuration");
        }
      } else {
        LOG.debug("category type or smartlogic configuration object is null");
      }

    } catch (JsonParseException e) {
      LOG.error("JSON Parse Exception: {}", e.getMessage(), e);
    }
    return tagsList;
  }

  @Override
  public String getSESLanguage(String pagePath) {
    SiteSettingsConfig sitesettings = null;
    try {
      sitesettings =
          KPMGUtilities.getContextAwareConfig(
              pagePath, KPMGUtilities.getResourceResolverFromPool(resolverFactory));
      if (sitesettings == null) {
        LOG.debug("site settings null");
      }
    } catch (LoginException e) {
      LOG.error(
          "Login Exception occured while trying to fetch resource resolver {}", e.getMessage(), e);
    }
    String sesLanguage =
        null != sitesettings ? sitesettings.smartLogic().sesLanguage() : ""; // site settings
    return !sesLanguage.isEmpty() ? sesLanguage.replace("_", "-") : "en-us";
  }

  @Override
  public JsonArray getAllTagsDetailsFromSESByCategory(
      Map<String, String> zidMap, SESClient sesClient) {
    Set<Tag> completeTagList = new HashSet<>();
    JsonArray jsonArray = new JsonArray();
    try {
      LOG.debug("zidmap :: {}", zidMap.entrySet());
      for (Map.Entry<String, String> tagentry : zidMap.entrySet()) {
        String categoryName = tagentry.getKey();
        if ((null != categoryName) && (!categoryName.isEmpty()) && null != tagentry.getValue()) {
          SESFilter sesFilter = new SESFilter();
          sesFilter.setStartTermZthesIds(new String[] {zidMap.get(categoryName)});
          LOG.debug("::::::sesFilter:::::{}", sesFilter);
          Map<String, Term> terms = sesClient.getAllTerms(sesFilter);
          for (Map.Entry<String, Term> entry : terms.entrySet()) {

            Term term = entry.getValue();

            if (term != null) {
              Tag tagWithDetails =
                  populateTagWithDetails(categoryName, term, zidMap.get(categoryName));
              completeTagList.add(tagWithDetails);
            }
          }
        }
      }
      jsonArray =
          TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(completeTagList);
    } catch (SESException e) {
      LOG.error("Exception occured while getting term details from SES {}", e.getMessage(), e);
    }

    return jsonArray;
  }

  @Override
  public JsonArray getUpdatedTags(SlingHttpServletRequest request) {
    String pagePath = request.getParameter(SmartLogicConstants.CommonUtil.PAGE_PATH);
    Map<String, String> allZIDsMap =
        SmartlogicUtil.getStartTermIDs(pagePath, smartLogicConfig, resolverFactory);
    Page currentPage = request.getResourceResolver().getResource(pagePath).adaptTo(Page.class);
    return getAllTagsDetailsFromSESByIDs(currentPage, pagePath, allZIDsMap);
  }

  public JsonArray getAllTagsDetailsFromSESByIDs(
      Page currentPage, String pagePath, Map<String, String> allZIDsMap) {
    String localModelCountryZthesID = getLocalSiteIDs(pagePath)[2];
    LOG.debug("getLocalSiteIDs(pagePath)[2] {}", localModelCountryZthesID);
    SESClient sesClient = createSESClient(getModelNameByPagePath(pagePath));
    List<String> tagIds = SmartlogicUtil.getTagIDsFromPageProperties(currentPage);
    LOG.debug("tagids : {}", tagIds);
    String[] tagIdArray = SmartlogicUtil.getTagsArray(tagIds);
    List<Tag> completeTagList = new ArrayList<>();
    try {
      if (tagIds.isEmpty()) {
        return new JsonArray();
      }
      sesClient.setLanguage(getSESLanguage(pagePath));
      Map<String, Term> termsFromSES = sesClient.getTermDetails(tagIdArray, DetailLevel.FULL);
      for (Map.Entry<String, Term> termEntry : termsFromSES.entrySet()) {
        Term term = termEntry.getValue();
        if (term != null) {
          String category =
              SmartlogicUtil.getTermClass(term.getTermClasses().getTermClasses().get(0).toString());

          Map<String, String> pathMap =
              TermDetailsExtractionUtil.getPath(
                  term, allZIDsMap.get(SmartlogicUtil.getRuleBaseClass(category)));
          if (tagIds.contains(pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.IDPATH))) {
            Tag tagWithDetails = new Tag();

            tagWithDetails.setTagID(term.getId().toString());
            tagWithDetails.setPath(pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.PATH));
            tagWithDetails.setKeywords(TermDetailsExtractionUtil.getSynonyms(term));
            tagWithDetails.setRuleBaseClass(
                SmartlogicUtil.getRuleBaseClass(
                    SmartlogicUtil.getTermClass(
                        term.getTermClasses().getTermClasses().get(0).toString())));
            tagWithDetails.setQualifiedName(
                pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.DISPLAYPATH));
            tagWithDetails.setIdDisplayPath(
                pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.IDDISPLAYPATH));
            tagWithDetails.setIdPath(
                pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.IDPATH));
            tagWithDetails.setTitle(term.getName().toString());

            /*
             * Only for Service and Industry, override certain details with locale specific
             * details
             */
            if (SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY.equals(
                    tagWithDetails.getRuleBaseClass())
                || SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE.equals(
                    tagWithDetails.getRuleBaseClass())
                || SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT.equals(
                    tagWithDetails.getRuleBaseClass())) {
              Map<String, String> termAssociatedDetails =
                  TermDetailsExtractionUtil.getTermAssociatedDetails(term, sesClient);
              if (StringUtils.isNotBlank(
                  termAssociatedDetails.get(SmartLogicConstants.TermDetailsExtractionUtil.ID))) {
                tagWithDetails.setTagID(
                    termAssociatedDetails.get(SmartLogicConstants.TermDetailsExtractionUtil.ID));
              }
              if (StringUtils.isNotBlank(
                  termAssociatedDetails.get(
                      SmartLogicConstants.TermDetailsExtractionUtil.PATH_STRING))) {
                tagWithDetails.setPath(
                    termAssociatedDetails.get(
                        SmartLogicConstants.TermDetailsExtractionUtil.PATH_STRING));
              }
              if (StringUtils.isNotBlank(
                  termAssociatedDetails.get(
                      SmartLogicConstants.TermDetailsExtractionUtil.IDPATH))) {
                tagWithDetails.setIdPath(
                    termAssociatedDetails.get(
                        SmartLogicConstants.TermDetailsExtractionUtil.IDPATH));
              }
              if (StringUtils.isNotBlank(
                  termAssociatedDetails.get(
                      SmartLogicConstants.TermDetailsExtractionUtil.KEYWORD))) {
                tagWithDetails.setKeywords(
                    termAssociatedDetails.get(
                        SmartLogicConstants.TermDetailsExtractionUtil.KEYWORD));
              }
              if (StringUtils.isNotBlank(
                  termAssociatedDetails.get(SmartLogicConstants.CommonUtil.TITLE))) {
                tagWithDetails.setTitle(
                    termAssociatedDetails.get(SmartLogicConstants.CommonUtil.TITLE));
              }

              tagWithDetails.setLocalTagID(term.getId().toString());
              tagWithDetails.setLocalKeywords(tagWithDetails.getKeywords());
              tagWithDetails.setLocalPath(tagWithDetails.getPath());
              tagWithDetails.setLocalIdPath(tagWithDetails.getIdPath());
            } else if (tagWithDetails
                .getRuleBaseClass()
                .equalsIgnoreCase(SmartLogicConstants.AutoClassification.TAG_GEOGRAPHY)) {
              /* Geo tags bear additional details */
              Tag tagWithGeoDetails = TermDetailsExtractionUtil.getGeographyTagDetails(term);
              tagWithDetails.setIso3166(tagWithGeoDetails.getIso3166());
              tagWithDetails.setIso31662(tagWithGeoDetails.getIso31662());
              tagWithDetails.setIso31663(tagWithGeoDetails.getIso31663());
              tagWithDetails.setUnm49region(tagWithGeoDetails.getUnm49region());
              tagWithDetails.setUnm49subregion(tagWithGeoDetails.getUnm49subregion());
              tagWithDetails.setUnm49subsubregion(tagWithGeoDetails.getUnm49subsubregion());
            }

            completeTagList.add(tagWithDetails);
          }
        }
      }
    } catch (RuntimeException | SESException e) {
      LOG.error(
          "Exception occured during update smart logic tags on page: " + pagePath + e.getMessage(),
          e);
    }

    return TermDetailsExtractionUtil.generateJsonFromTagList(completeTagList);
  }

  @Override
  public String[] getLocalSiteIDs(String pagePath) {
    String[] localSettings = new String[4];
    SiteSettingsConfig sitesettings;
    try {
      sitesettings =
          KPMGUtilities.getContextAwareConfig(
              pagePath, KPMGUtilities.getResourceResolverFromPool(resolverFactory));
      if (sitesettings != null) {
        localSettings[0] = sitesettings.smartLogic().localModelIndustryZthesID();
        localSettings[1] = sitesettings.smartLogic().localModelServiceZthesID();
        localSettings[2] = sitesettings.smartLogic().localModelCountryZthesID();
        localSettings[3] = sitesettings.smartLogic().localModelInsightZthesID();
      }
    } catch (LoginException e) {
      LOG.error(
          "Login Exception occured while trying to fetch site settings {}", e.getMessage(), e);
    }
    return localSettings;
  }

  @Override
  public Tag populateTagWithDetails(String categoryName, Term term, String zid) {
    Map<String, String> pathMap;
    String pathStringList;
    String relativepathStringList;
    String idDisplayPathList;
    pathMap = TermDetailsExtractionUtil.getPath(term, zid);
    pathStringList = pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.PATH);
    relativepathStringList = pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.DISPLAYPATH);
    idDisplayPathList = pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.IDDISPLAYPATH);
    Tag tagWithDetails = new Tag();

    String keywords = TermDetailsExtractionUtilVo.getSynonyms(term);
    if (keywords.isEmpty()) {
      keywords = " ";
    }
    tagWithDetails.setTagID(term.getId().toString());
    tagWithDetails.setKeywords(keywords);
    tagWithDetails.setPath(pathStringList);
    tagWithDetails.setRuleBaseClass(categoryName);
    tagWithDetails.setQualifiedName(relativepathStringList);
    tagWithDetails.setIdDisplayPath(idDisplayPathList);
    tagWithDetails.setIdPath(pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.IDPATH));
    if (SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY.equals(categoryName)
        || SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE.equals(categoryName)
        || SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT.equals(
            categoryName)) {
      tagWithDetails.setLocalTagID(term.getId().toString());
      tagWithDetails.setLocalKeywords(keywords);
      tagWithDetails.setLocalPath(pathStringList);
      tagWithDetails.setLocalIdPath(
          pathMap.get(SmartLogicConstants.TermDetailsExtractionUtil.IDPATH));
    }

    if (categoryName.equalsIgnoreCase(SmartLogicConstants.AutoClassification.TAG_GEOGRAPHY)) {
      Tag tagWithGeoDetails = TermDetailsExtractionUtilVo.getGeographyTagDetails(term);
      tagWithDetails.setIso3166(tagWithGeoDetails.getIso3166());
      tagWithDetails.setIso31662(tagWithGeoDetails.getIso31662());
      tagWithDetails.setIso31663(tagWithGeoDetails.getIso31663());
      tagWithDetails.setUnm49region(tagWithGeoDetails.getUnm49region());
      tagWithDetails.setUnm49subregion(tagWithGeoDetails.getUnm49subregion());
      tagWithDetails.setUnm49subsubregion(tagWithGeoDetails.getUnm49subsubregion());
    }
    return tagWithDetails;
  }
}
