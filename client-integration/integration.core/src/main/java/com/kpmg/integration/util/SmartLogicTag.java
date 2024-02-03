package com.kpmg.integration.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartLogicTag {

  private static final Logger LOG = LoggerFactory.getLogger(SmartLogicTag.class);

  private static final String ZTHE_ID = "zthes-id";
  private static final String NAME_STRING = "name";
  private static final String LEVEL_STRING = "level";
  private static final String ENGLISH_NAME = "english-name";
  private static final String ORDER_STRING = "order";

  private Set<SmartLogicTag> children;
  private String name;
  private int level;
  private String englishName;
  private String zthesID;
  private int order;

  /**
   * SmartLogicTag
   *
   * @param children children
   * @param name name
   * @param level level
   * @param zthesID zthesID
   * @param englishName englishName
   * @param order order
   */
  public SmartLogicTag(
      Set<SmartLogicTag> children,
      String name,
      int level,
      String zthesID,
      String englishName,
      int order) {
    if (null != children) {
      this.children = new HashSet<>(children);
    }
    this.name = name;
    this.level = level;
    this.zthesID = zthesID;
    this.englishName = englishName;
    this.order = order;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public Set<SmartLogicTag> getChildren() {
    return new HashSet<>(children);
  }

  public void setChildren(Set<SmartLogicTag> children) {
    this.children = new HashSet<>(children);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getZthesID() {
    return zthesID;
  }

  public void setZthesID(String zthesID) {
    this.zthesID = zthesID;
  }

  public String getEnglishName() {
    return englishName;
  }

  public void setEnglishName(String englishName) {
    this.englishName = englishName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SmartLogicTag other = (SmartLogicTag) obj;
    return !name.equals(other.name) ? Boolean.FALSE : Boolean.TRUE;
  }

  @Override
  public String toString() {
    return "SmartLogicTag [children="
        + children
        + ", name="
        + name
        + ", level="
        + level
        + ", englishName="
        + englishName
        + ", zthesID="
        + zthesID
        + ", order="
        + order
        + "]";
  }

  /**
   * Method which returns SmartLogicTag values in an JSONArray Format
   *
   * @param parentSmartLogicTagSet parentSmartLogicTagSet
   * @param JSONArray jsonArray
   * @return JSONArray - return JSON Array of smart logic tags
   * @throws JSONException JSONException
   */
  public JsonArray toJSON(Set<SmartLogicTag> parentSmartLogicTagSet, JsonArray jsonArray) {
    parentSmartLogicTagSet.forEach(
        smartLogicTag -> {
          if (null != smartLogicTag) {
            JsonObject jsonValue = new JsonObject();
            jsonValue.addProperty(ZTHE_ID, smartLogicTag.getZthesID());
            jsonValue.addProperty(NAME_STRING, smartLogicTag.getName());
            jsonValue.addProperty(LEVEL_STRING, smartLogicTag.getLevel());
            if (smartLogicTag.getOrder() > 0) {
              jsonValue.addProperty(ENGLISH_NAME, smartLogicTag.getEnglishName());
              jsonValue.addProperty(ORDER_STRING, smartLogicTag.getOrder());
            }
            if (null != jsonArray) {
              if (smartLogicTag.getChildren() != null) {
                JsonArray tmpJSONArray = new JsonArray();
                jsonValue.add("tags", toJSON(smartLogicTag.getChildren(), tmpJSONArray));
              }
              jsonArray.add(jsonValue);
            }
          }
        });
    return jsonArray;
  }
}
