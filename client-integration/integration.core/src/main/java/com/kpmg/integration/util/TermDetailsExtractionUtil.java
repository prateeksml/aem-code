package com.kpmg.integration.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kpmg.integration.constants.SmartLogicConstants;
import com.kpmg.integration.models.Tag;
import com.kpmg.integration.services.SmartLogic;
import com.smartlogic.ses.client.Associated;
import com.smartlogic.ses.client.Field;
import com.smartlogic.ses.client.Path;
import com.smartlogic.ses.client.SESClient;
import com.smartlogic.ses.client.SESClient.DetailLevel;
import com.smartlogic.ses.client.Synonym;
import com.smartlogic.ses.client.Synonyms;
import com.smartlogic.ses.client.Term;
import com.smartlogic.ses.client.exceptions.NoSuchTermException;
import com.smartlogic.ses.client.exceptions.SESException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class to get associated details
 *
 * @author nkund1,sroy15
 */
public class TermDetailsExtractionUtil {

  private static final Logger LOG = LoggerFactory.getLogger(TermDetailsExtractionUtil.class);
  @Reference private static SmartLogic smartlogicConfig;

  /** Private constructor */
  private TermDetailsExtractionUtil() {}

  /**
   * This method will fetch the pagePath based on the locale
   *
   * @param term term
   * @param localsite localsite
   * @param categoryType categoryType
   * @param pagePath pagePath
   * @return Map<String, String>
   */
  public static Map<String, String> getPath(Term term, String localsite) {

    String pathString = "";
    String relativepathString = "";
    String idPath = "";
    String idDisplayPath = "";
    Boolean flag = false;
    Path requiredPath = null;
    List<Path> termPath;
    termPath = term.getPaths();
    Map<String, String> pathMap = new HashMap<>();

    for (Path path : termPath) {
      requiredPath = getRequiredPath(localsite, requiredPath, path);
      if (null != requiredPath) {
        break;
      }
    }
    if (null != requiredPath) {

      List<Field> fields = requiredPath.getFields();
      List<String> alternateIDList = new ArrayList<>();
      for (Field field : requiredPath.getFields()) {
        alternateIDList.add(field.getId());
      }

      int index = alternateIDList.indexOf(localsite);
      if (index > 0) {
        for (int i = index; i < fields.size(); i++) {
          pathString =
              pathString
                  + fields.get(i).getName()
                  + SmartLogicConstants.TermDetailsExtractionUtil.FORWARD_IDENTIFIER;
          idPath =
              idPath
                  + fields.get(i).getId()
                  + SmartLogicConstants.TermDetailsExtractionUtil.FORWARD_IDENTIFIER;
        }

        for (int i = index + 1; i < fields.size(); i++) {
          relativepathString =
              relativepathString
                  + fields.get(i).getName()
                  + SmartLogicConstants.TermDetailsExtractionUtil.FORWARD_IDENTIFIER;
          idDisplayPath =
              idDisplayPath
                  + fields.get(i).getId()
                  + SmartLogicConstants.TermDetailsExtractionUtil.FORWARD_IDENTIFIER;
        }
        flag = true;
      }
    }

    if (Boolean.TRUE.equals(flag)) {

      if (!pathString.isEmpty() && !idPath.isEmpty()) {
        pathString = pathString.substring(0, pathString.length() - 1);
        idPath = idPath.substring(0, idPath.length() - 1);
      }
      if (pathString.split("/").length == 1 && idPath.split("/").length == 1) {
        relativepathString = pathString;
        idDisplayPath = idPath;
      } else {
        if (!relativepathString.isEmpty() && !idDisplayPath.isEmpty()) {
          relativepathString = relativepathString.substring(0, relativepathString.length() - 1);
          idDisplayPath = idDisplayPath.substring(0, idDisplayPath.length() - 1);
        }
      }
      pathMap.put(SmartLogicConstants.TermDetailsExtractionUtil.PATH, pathString);
      pathMap.put(SmartLogicConstants.TermDetailsExtractionUtil.DISPLAYPATH, relativepathString);
      pathMap.put(SmartLogicConstants.TermDetailsExtractionUtil.IDPATH, idPath);
      pathMap.put(SmartLogicConstants.TermDetailsExtractionUtil.IDDISPLAYPATH, idDisplayPath);
      return pathMap;
    }
    LOG.debug("returning a map having the path and display path {}", pathMap.size());
    return pathMap;
  }

  /**
   * This method will fetch the required path from local site settings.
   *
   * @param localsite localsite
   * @param requiredPath requiredPath
   * @param path path
   * @return Path -Path
   */
  private static Path getRequiredPath(String localsite, Path requiredPath, Path path) {
    String pathid;
    List<Field> fields = path.getFields();
    for (Field field : fields) {
      pathid = field.getId();
      if (null != localsite && !localsite.isEmpty()) {
        if (pathid.equals(localsite)) {
          requiredPath = path;
          break;
        }
      } else {
        requiredPath = path;
        break;
      }
    }
    return requiredPath;
  }

  /**
   * Get the associated keywords for a given term
   *
   * @param term term
   * @return String
   */
  public static String getSynonyms(Term term) {
    Set<String> alternateKeywords = new HashSet<>();
    List<Synonyms> termKeywords = term.getSynonymsList();
    LOG.debug("entering method to get all the associated keywords");
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
    LOG.debug("entered method to get the geo metadata");
    Map<String, String> metaDataMap = new HashMap<String, String>();
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
   * This is a helper method to get the associated details for a term Then extract the term id
   * associated with the common and extract all the term details for the term present in common
   *
   * @param term term
   * @param sesClient sesClient
   * @param semaphoreConfigs semaphoreConfigs
   * @return Map<String, String>
   */
  public static Map<String, String> getTermAssociatedDetails(Term term, SESClient sesClient) {

    LOG.debug("entering method to get all the term associated details");
    Term commonTerm;
    String type = "";
    Set<String> alternateKeywordsCommon = new HashSet<>();
    String[] synonyms;
    StringBuilder pathStringCommon = new StringBuilder();
    StringBuilder idPathCommon = new StringBuilder();
    StringBuilder termTitleCommon = new StringBuilder();
    String key = StringUtils.EMPTY;

    Map<String, String> termAsociatedDetailsMap = new HashMap<>();
    try {
      List<Associated> associated = term.getAssociateds();
      for (Associated associatedMapping : associated) {
        type = associatedMapping.getType();
        if (SmartLogicConstants.CommonUtil.MAPS_TO_COMMON.equalsIgnoreCase(type)) {
          Map<String, Field> fieldsMap = associatedMapping.getFields();
          for (Map.Entry<String, Field> entry : fieldsMap.entrySet()) {
            key = entry.getKey();
            commonTerm = sesClient.getTermDetails(entry.getKey(), DetailLevel.FULL);
            if (null != commonTerm) {
              Map<String, String> pathStringMap =
                  getPath(commonTerm, smartlogicConfig.getCommonZthesID());
              for (Map.Entry<String, String> path : pathStringMap.entrySet()) {
                if ("path".equalsIgnoreCase(path.getKey())) {
                  if (commonTerm.getClass().toString().contains("Industries")) {
                    pathStringCommon.append("Industries/");
                  } else {
                    pathStringCommon.append("Services/");
                  }
                  pathStringCommon
                      .append(pathStringCommon.toString())
                      .append(path.getValue())
                      .append("|");
                  idPathCommon.append(idPathCommon.toString()).append(path.getValue()).append("|");
                }
              }
              synonyms = TermDetailsExtractionUtil.getSynonyms(commonTerm).split(",");
              alternateKeywordsCommon.addAll(Set.of(synonyms));
              termTitleCommon
                  .append(termTitleCommon.toString())
                  .append(String.valueOf(commonTerm.getName()))
                  .append("|");
            }
          }
        }
        if (!pathStringCommon.toString().isEmpty() && pathStringCommon.toString().endsWith("|")) {
          pathStringCommon.append(pathStringCommon.substring(0, pathStringCommon.length() - 1));
        }
        if (idPathCommon != null
            && !idPathCommon.toString().isEmpty()
            && idPathCommon.toString().endsWith("|")) {

          idPathCommon.append(idPathCommon.substring(0, idPathCommon.length() - 1));
        }

        if (!termTitleCommon.toString().isEmpty() && termTitleCommon.toString().endsWith("|")) {
          termTitleCommon.append(termTitleCommon.substring(0, termTitleCommon.length() - 1));
        }
        if (!key.isEmpty()) {
          termAsociatedDetailsMap.put(SmartLogicConstants.TermDetailsExtractionUtil.ID, key);
          termAsociatedDetailsMap.put(
              SmartLogicConstants.TermDetailsExtractionUtil.PATH_STRING,
              pathStringCommon.toString());
          termAsociatedDetailsMap.put(
              SmartLogicConstants.TermDetailsExtractionUtil.IDPATH, idPathCommon.toString());

          termAsociatedDetailsMap.put(
              SmartLogicConstants.TermDetailsExtractionUtil.KEYWORD,
              String.join(",", alternateKeywordsCommon));
        }
      }

    } catch (NoSuchTermException e) {
      LOG.error("No such term exists in SES", e);
    } catch (SESException e) {
      LOG.error("SESException: could not connect to SES", e);
    }

    return termAsociatedDetailsMap;
  }

  /**
   * The getGeographyTagDetails Method will fetch all the Meta Data for geo term.
   *
   * @param term term
   * @return Tag Tag
   */
  public static Tag getGeographyTagDetails(Term term) {

    LOG.debug("entering getGeographyTagDetails to get meta data for geo");

    Tag tag = new Tag();
    Map<String, String> geoTagMap = TermDetailsExtractionUtil.getGeoMetaData(term);
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
   * The gererateJsonFromTagList Method will generate Json from Tag List
   *
   * @param tagList tagList
   * @return JSONArray JSONArray
   */
  public static JsonArray generateJsonFromTagList(List<Tag> tagList) {
    JsonArray jsonArray = new JsonArray();
    JsonObject termJson;
    LOG.debug("entering gererateJsonFromTagList to generate JSON from tag list");
    try {
      for (Tag tag : tagList) {

        termJson = new JsonObject();

        if (tag.getRuleBaseClass()
            .equalsIgnoreCase(
                SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_GEOGRAPHY)) {

          termJson.addProperty(
              SmartLogicConstants.AutoClassification.TAG_CATEGORY_TYPE, tag.getRuleBaseClass());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_ID,
              tag.getTagID());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_KEYWORDS,
              tag.getKeywords());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_TITLE_PATH,
              tag.getPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDPATH,
              tag.getIdPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_QUALIFIED_PATH,
              tag.getQualifiedName());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDDISPLAYPATH,
              tag.getIdDisplayPath());

          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_ISO3166,
              tag.getIso3166());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_ISO31662,
              tag.getIso31662());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_ISO31663,
              tag.getIso31663());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_UNM49_REGION,
              tag.getUnm49region());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_UNM49_SUB_REGION,
              tag.getUnm49subregion());
          termJson.addProperty(
              tag.getRuleBaseClass()
                  + SmartLogicConstants.AutoClassification.TAG_UNM49_SUB_SUB_REGION,
              tag.getUnm49subsubregion());
        } else if (tag.getRuleBaseClass()
                .equalsIgnoreCase(
                    SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY)
            || tag.getRuleBaseClass()
                .equalsIgnoreCase(
                    SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE)) {

          termJson.addProperty(
              SmartLogicConstants.AutoClassification.TAG_CATEGORY_TYPE, tag.getRuleBaseClass());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCAL_TAG_ID,
              tag.getLocalTagID());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCAL_TAG_KEYWORDS,
              tag.getLocalKeywords());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCAL_TAG_PATH,
              tag.getLocalPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCALIDPATH,
              tag.getLocalIdPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_QUALIFIED_PATH,
              tag.getQualifiedName());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDDISPLAYPATH,
              tag.getIdDisplayPath());

          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_ID,
              tag.getTagID());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_KEYWORDS,
              tag.getKeywords());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_TITLE_PATH,
              tag.getPath());

          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDPATH,
              tag.getIdPath());

        } else if (tag.getRuleBaseClass()
            .equalsIgnoreCase(
                SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MARKET)) {
          termJson.addProperty(
              SmartLogicConstants.AutoClassification.TAG_CATEGORY_TYPE, tag.getRuleBaseClass());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_ID,
              tag.getTagID());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_KEYWORDS,
              tag.getKeywords());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_TITLE_PATH,
              tag.getPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDPATH,
              tag.getIdPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_QUALIFIED_PATH,
              tag.getQualifiedName());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDDISPLAYPATH,
              tag.getIdDisplayPath());

        } else if (tag.getRuleBaseClass()
            .equalsIgnoreCase(
                SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT)) {
          termJson.addProperty(
              SmartLogicConstants.AutoClassification.TAG_CATEGORY_TYPE, tag.getRuleBaseClass());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCAL_TAG_ID,
              tag.getLocalTagID());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCAL_TAG_KEYWORDS,
              tag.getLocalKeywords());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCAL_TAG_PATH,
              tag.getLocalPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.LOCALIDPATH,
              tag.getLocalIdPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_ID,
              tag.getTagID());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_KEYWORDS,
              tag.getKeywords());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_TITLE_PATH,
              tag.getPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDPATH,
              tag.getIdPath());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.TAG_QUALIFIED_PATH,
              tag.getQualifiedName());
          termJson.addProperty(
              tag.getRuleBaseClass() + SmartLogicConstants.AutoClassification.IDDISPLAYPATH,
              tag.getIdDisplayPath());
        }
        jsonArray.add(termJson);
      }
    } catch (RuntimeException e) {
      LOG.error(
          "Exception occured in gererateJsonFromTagList() of UpdateServiceImpl" + e.getMessage(),
          e);
    }
    return jsonArray;
  }
}
