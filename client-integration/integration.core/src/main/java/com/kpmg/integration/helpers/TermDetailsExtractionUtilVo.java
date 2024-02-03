package com.kpmg.integration.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kpmg.integration.constants.SmartLogicConstants;
import com.kpmg.integration.models.Tag;
import com.smartlogic.ses.client.Synonym;
import com.smartlogic.ses.client.Synonyms;
import com.smartlogic.ses.client.Term;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Util class to get associated details */
public class TermDetailsExtractionUtilVo {

  private static final Logger LOG = LoggerFactory.getLogger(TermDetailsExtractionUtilVo.class);

  /** TermDetailsExtractionUtilVo */
  private TermDetailsExtractionUtilVo() {}

  /**
   * Get the associated keywords for a given term
   *
   * @param term term
   * @return String
   */
  public static String getSynonyms(Term term) {
    Set<String> alternateKeywords = new HashSet<>();
    List<Synonyms> termKeywords = term.getSynonymsList();
    // get all the associated keywords
    for (Synonyms keywords : termKeywords) {
      String type = keywords.getType();
      if (type.indexOf(SmartLogicConstants.TermDetailsExtractionUtil.ISO) == -1
          && type.indexOf(SmartLogicConstants.TermDetailsExtractionUtil.UNM49) == -1) {
        List<Synonym> synonyms = keywords.getSynonyms();
        for (Synonym alternateKeyWord : synonyms) {
          alternateKeywords.add(alternateKeyWord.getValue());
        }
      }
    }
    return String.join(",", alternateKeywords);
  }

  /**
   * This method will fetch all the Synonyms Related to Category Type Geography.
   *
   * @param term term
   * @return Map<String, String>
   */
  public static Map<String, String> getGeoMetaData(Term term) {
    Map<String, String> metaDataMap = new HashMap<>();
    List<Synonyms> termKeywords = term.getSynonymsList();
    // get all the associated keywords
    for (Synonyms keywords : termKeywords) {
      String type = keywords.getType();
      if (type.indexOf(SmartLogicConstants.TermDetailsExtractionUtil.ISO) != -1
          || type.indexOf(SmartLogicConstants.TermDetailsExtractionUtil.UNM49) != -1) {
        List<Synonym> synonyms = keywords.getSynonyms();
        for (Synonym alternateKeyWord : synonyms) {
          metaDataMap.put(type, alternateKeyWord.getValue());
        }
      }
    }

    return metaDataMap;
  }

  /**
   * The getGeographyTagDetails Method will fetch all the Meta Data for geo term.
   *
   * @param term term
   * @return Tag
   */
  public static Tag getGeographyTagDetails(Term term) {

    Tag tag = new Tag();
    Map<String, String> geoTagMap = TermDetailsExtractionUtilVo.getGeoMetaData(term);
    for (Map.Entry<String, String> entry : geoTagMap.entrySet()) {

      if (entry.getKey().equalsIgnoreCase(SmartLogicConstants.CommonUtil.ISO3166)) {
        tag.setIso3166(entry.getValue());
      } else if (entry.getKey().equalsIgnoreCase(SmartLogicConstants.CommonUtil.ISO31662)) {
        tag.setIso31662(entry.getValue());
      } else if (entry.getKey().equalsIgnoreCase(SmartLogicConstants.CommonUtil.ISO31663)) {
        tag.setIso31663(entry.getValue());
      } else if (entry.getKey().equalsIgnoreCase(SmartLogicConstants.CommonUtil.UNM49_REGION)) {
        tag.setUnm49region(entry.getValue());
      } else if (entry.getKey().equalsIgnoreCase(SmartLogicConstants.CommonUtil.UNM49_SUB_REGION)) {
        tag.setUnm49subregion(entry.getValue());
      } else if (entry
          .getKey()
          .equalsIgnoreCase(SmartLogicConstants.CommonUtil.UNM49_SUB_SUB_REGION)) {
        tag.setUnm49subsubregion(entry.getValue());
      }
    }
    return tag;
  }

  /**
   * Method Generates JSON for category
   *
   * @param tagList tagList
   * @return JSONArray
   */
  public static JsonArray generateCategorySpecificJSONFromTagList(Set<Tag> tagList) {
    JsonArray jsonArray = new JsonArray();
    JsonObject termJson;
    if (tagList.isEmpty()) {
      return jsonArray;
    }
    for (Tag tag : tagList) {

      termJson = new JsonObject();

      if (tag.getRuleBaseClass()
              .equalsIgnoreCase(
                  SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_CONTENTTYPE)
          || tag.getRuleBaseClass()
              .equalsIgnoreCase(
                  SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MEDIAFORMATS)
          || tag.getRuleBaseClass()
              .equalsIgnoreCase(
                  SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_PERSONA)
          || tag.getRuleBaseClass()
              .equalsIgnoreCase(
                  SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_GEOGRAPHY)
          || tag.getRuleBaseClass()
              .equalsIgnoreCase(
                  SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY)
          || tag.getRuleBaseClass()
              .equalsIgnoreCase(
                  SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE)
          || tag.getRuleBaseClass()
              .equalsIgnoreCase(SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MARKET)
          || tag.getRuleBaseClass()
              .equalsIgnoreCase(
                  SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT)) {
        String categoryType = tag.getRuleBaseClass();

        switch (categoryType) {
          case SmartLogicConstants.SmartLogicClassification.CONTENT_TYPE:
            termJson.addProperty("contenttypeID", tag.getTagID());
            termJson.addProperty("contenttypePath", tag.getPath());
            termJson.addProperty("contenttypeidPath", tag.getIdPath());

            termJson.addProperty("contenttypeQualifiedName", tag.getQualifiedName());
            termJson.addProperty("contenttypeidDisplayPath", tag.getIdDisplayPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());
            break;
          case SmartLogicConstants.SmartLogicClassification.MEDIA_FORMATS:
            termJson.addProperty("mediaformatsID", tag.getTagID());
            termJson.addProperty("mediaformatsPath", tag.getPath());
            termJson.addProperty("mediaformatsidPath", tag.getIdPath());

            termJson.addProperty("mediaformatsQualifiedName", tag.getQualifiedName());
            termJson.addProperty("mediaformatsidDisplayPath", tag.getIdDisplayPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());
            break;
          case SmartLogicConstants.SmartLogicClassification.PERSONA:
            termJson.addProperty("personaID", tag.getTagID());
            termJson.addProperty("personaPath", tag.getPath());
            termJson.addProperty("personaidPath", tag.getIdPath());
            termJson.addProperty("personaQualifiedName", tag.getQualifiedName());
            termJson.addProperty("personaidDisplayPath", tag.getIdDisplayPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());
            break;

          case SmartLogicConstants.SmartLogicClassification.GEOGRAPHY:
            String geographyIso31662Value = tag.getIso31662();
            String geographyIso31663Value = tag.getIso31663();
            String geographyIso3166Value = tag.getIso3166();
            String geographyUnm49RegionValue = tag.getUnm49region();
            String geographyUnm49SubRegionValue = tag.getUnm49subregion();
            String geographyUnm49SubSubRegionValue = tag.getUnm49subsubregion();

            termJson.addProperty("geographyId", tag.getTagID());
            termJson.addProperty("geographyPath", tag.getPath());
            termJson.addProperty("geographyidPath", tag.getIdPath());
            termJson.addProperty("geographyKeywords", tag.getKeywords());
            termJson.addProperty(
                "geographyIso31662", geographyIso31662Value == null ? " " : geographyIso31662Value);
            termJson.addProperty(
                "geographyUnm49Region",
                geographyUnm49RegionValue == null ? " " : geographyUnm49RegionValue);
            termJson.addProperty(
                "geographyUnm49SubRegion",
                geographyUnm49SubRegionValue == null ? " " : geographyUnm49SubRegionValue);
            termJson.addProperty(
                "geographyIso31663", geographyIso31663Value == null ? " " : geographyIso31663Value);
            termJson.addProperty(
                "geographyIso3166", geographyIso3166Value == null ? " " : geographyIso3166Value);
            termJson.addProperty(
                "geographyUnm49SubSubRegion",
                geographyUnm49SubSubRegionValue == null ? " " : geographyUnm49SubSubRegionValue);
            termJson.addProperty("geographyQualifiedName", tag.getQualifiedName());
            termJson.addProperty("geographyidDisplayPath", tag.getIdDisplayPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());

            break;
          case SmartLogicConstants.SmartLogicClassification.INDUSTRY:
            String industryID = tag.getTagID();
            String industryPath = tag.getPath();
            String industryidPath = tag.getIdPath();
            String industryKeywords = tag.getLocalKeywords();
            String industryidDisplayPath = tag.getIdDisplayPath();

            termJson.addProperty("industryId", industryID);
            termJson.addProperty("industryPath", industryPath);
            termJson.addProperty("industryidPath", industryidPath);
            termJson.addProperty("industryCommonId", industryID);
            termJson.addProperty("industryCommonKeywords", industryKeywords);
            termJson.addProperty("industryKeywords", industryKeywords);
            termJson.addProperty("industryCommonPath", industryPath);
            termJson.addProperty("industryCommonidPath", industryidPath);
            termJson.addProperty("industryQualifiedName", tag.getQualifiedName());
            termJson.addProperty("industryidDisplayPath", industryidDisplayPath);
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());

            break;
          case SmartLogicConstants.SmartLogicClassification.SERVICE:
            String serviceID = tag.getTagID();
            String servicePath = tag.getPath();
            String serviceidPath = tag.getIdPath();
            String serviceKeywords = tag.getLocalKeywords();
            String serviceidDisplayPath = tag.getIdDisplayPath();

            termJson.addProperty("serviceId", serviceID);
            termJson.addProperty("servicePath", servicePath);
            termJson.addProperty("serviceidPath", serviceidPath);
            termJson.addProperty("serviceCommonId", serviceID);
            termJson.addProperty("serviceKeywords", serviceKeywords);
            termJson.addProperty("serviceCommonKeywords", serviceKeywords);
            termJson.addProperty("serviceCommonPath", servicePath);
            termJson.addProperty("serviceCommonidPath", serviceidPath);
            termJson.addProperty("serviceQualifiedName", tag.getQualifiedName());
            termJson.addProperty("serviceidDisplayPath", serviceidDisplayPath);
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());

            break;
          case SmartLogicConstants.SmartLogicClassification.INSIGHT:
            String insightID = tag.getTagID();
            String insightPath = tag.getPath();
            String insightidPath = tag.getIdPath();
            String insightKeywords = tag.getLocalKeywords();
            String insightidDisplayPath = tag.getIdDisplayPath();

            termJson.addProperty("insightId", insightID);
            termJson.addProperty("insightPath", insightPath);
            termJson.addProperty("insightidPath", insightidPath);
            termJson.addProperty("insightKeywords", insightKeywords);
            termJson.addProperty("insightQualifiedName", tag.getQualifiedName());
            termJson.addProperty("insightidDisplayPath", insightidDisplayPath);
            termJson.addProperty("insightCommonId", insightID);
            termJson.addProperty("insightCommonPath", insightPath);
            termJson.addProperty("insightCommonKeywords", insightKeywords);
            termJson.addProperty("insightCommonidPath", insightidPath);
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());

            break;
          case SmartLogicConstants.SmartLogicClassification.MARKETS:
            termJson.addProperty("marketId", tag.getTagID());
            termJson.addProperty("marketPath", tag.getPath());
            termJson.addProperty("marketidPath", tag.getIdPath());
            termJson.addProperty("marketKeywords", tag.getKeywords());
            termJson.addProperty("marketQualifiedName", tag.getQualifiedName());
            termJson.addProperty("marketidDisplayPath", tag.getIdDisplayPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE, tag.getPath());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TAG_ID, tag.getTagID());
            termJson.addProperty(
                SmartLogicConstants.TermDetailsExtractionUtil.TITLE_PATH, tag.getPath());
            break;

          default:
            LOG.debug("invalid category type");
            break;
        }
      }
      jsonArray.add(termJson);
    }
    return jsonArray;
  }
}
