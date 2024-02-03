package com.kpmg.integration.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kpmg.integration.constants.SmartLogicConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** This class used for sorting the tags */
public class TagSort {

  /**
   * Sort Smart Logic Tags method used to sort the tags
   *
   * @param category category
   * @param tagsArray tagsArray
   * @return tagsList tagsList
   */
  public List<JsonObject> sortSmartLogicTags(String category, JsonArray tagsArray) {
    String keyname = "";
    List<JsonObject> tagsList = new ArrayList<>();
    for (int i = 0; i < tagsArray.size(); i++) {
      tagsList.add(tagsArray.get(i).getAsJsonObject());
    }

    switch (category) {
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_CONTENTTYPE:
        keyname = "contenttypeQualifiedName";
        sort(tagsList, keyname);
        break;
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MEDIAFORMATS:
        keyname = "mediaformatsQualifiedName";
        sort(tagsList, keyname);
        break;
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_PERSONA:
        keyname = "personaQualifiedName";
        sort(tagsList, keyname);
        break;
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_GEOGRAPHY:
        keyname = "geographyQualifiedName";
        sort(tagsList, keyname);
        break;
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INDUSTRY:
        keyname = "industryQualifiedName";
        sort(tagsList, keyname);
        break;
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_SERVICE:
        keyname = "serviceQualifiedName";
        sort(tagsList, keyname);
        break;
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_INSIGHT:
        keyname = "insightQualifiedName";
        sort(tagsList, keyname);
        break;
      case SmartLogicConstants.SmartLogicClassification.RULE_BASE_CLASS_MARKET:
        keyname = "marketQualifiedName";
        sort(tagsList, keyname);
        break;

      case SmartLogicConstants.SmartLogicClassification.ALL:
        keyname = "titlePath";
        sort(tagsList, keyname);
        break;

      default:
        // do nothing
        break;
    }
    return tagsList;
  }

  private void sort(List<JsonObject> tagsList, String keyname) {
    Collections.sort(
        tagsList,
        new Comparator<JsonObject>() {

          @Override
          public int compare(JsonObject a, JsonObject b) {
            String valA = new String();
            String valB = new String();

            if (a.has(keyname) && b.has(keyname)) {
              valA = a.get(keyname).getAsString();
              valB = b.get(keyname).getAsString();
            }

            return valA.compareTo(valB);
          }
        });
  }
}
