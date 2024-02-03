package com.kpmg.integration.models;

/**
 * Value Object for Tags
 *
 * @author jmarti
 */
public class Tag {

  private String tagID;
  private String title;
  private String path;
  /* SL Hierarchy CR */
  private String idPath;
  private String idDisplayPath;
  private String localIdPath;
  private String keywords;
  private String ruleBaseClass;
  private String localTagID;
  private String localPath;
  private String qualifiedName;
  private String localKeywords;
  private String iso3166;
  private String iso31662;
  private String iso31663;
  private String unm49region;
  private String unm49subregion;
  private String unm49subsubregion;
  private String categoryType;
  private String contenttype;
  private String mediaformats;
  private String persona;
  private String geography;
  private String industry;
  private String service;
  private String insight;
  private String market;
  private String localIdDisplayPath;

  public String getLocalIdPath() {
    return localIdPath;
  }

  public void setLocalIdPath(String localIdPath) {
    this.localIdPath = localIdPath;
  }

  public String getLocalIdDisplayPath() {
    return localIdDisplayPath;
  }

  public void setLocalIdDisplayPath(String localIdDisplayPath) {
    this.localIdDisplayPath = localIdDisplayPath;
  }

  public String getLocalTagID() {
    return localTagID;
  }

  public void setLocalTagID(String localTagID) {
    this.localTagID = localTagID;
  }

  public String getLocalPath() {
    return localPath;
  }

  public void setLocalPath(String localPath) {
    this.localPath = localPath;
  }

  public String getLocalKeywords() {
    return localKeywords;
  }

  public void setLocalKeywords(String localKeywords) {
    this.localKeywords = localKeywords;
  }

  public String getIdPath() {

    return idPath;
  }

  public void setIdPath(String idPath) {
    this.idPath = idPath;
  }

  public String getIdDisplayPath() {
    return idDisplayPath;
  }

  public void setIdDisplayPath(String idDisplayPath) {
    this.idDisplayPath = idDisplayPath;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((localTagID == null) ? 0 : localTagID.hashCode());
    result = prime * result + ((tagID == null) ? 0 : tagID.hashCode());
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
    Tag other = (Tag) obj;
    if (localTagID == null) {
      if (other.localTagID != null) {
        return false;
      }
    } else if (!localTagID.equals(other.localTagID)) {
      return false;
    }
    if (tagID == null) {
      if (other.tagID != null) {
        return false;
      }
    } else if (!tagID.equals(other.tagID)) {
      return false;
    }
    return true;
  }

  /**
   * @return
   */
  public String getRuleBaseClass() {
    return ruleBaseClass;
  }

  /**
   * @param ruleBaseClass
   */
  public void setRuleBaseClass(String ruleBaseClass) {
    this.ruleBaseClass = ruleBaseClass;
  }

  /**
   * @return
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return
   */
  public String getPath() {
    return path;
  }

  /**
   * @param path
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * @return
   */
  public String getTagID() {
    return tagID;
  }

  /**
   * @param tagID
   */
  public void setTagID(String tagID) {
    this.tagID = tagID;
  }

  /** */

  /**
   * @return
   */
  public String getKeywords() {
    return keywords;
  }

  /**
   * @param keywords
   */
  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  /** */

  /**
   * /**
   *
   * @return
   */
  public String getIso3166() {
    return iso3166;
  }

  /**
   * @param iso3166
   */
  public void setIso3166(String iso3166) {
    if (iso3166 == null) {
      this.iso3166 = " ";
    } else {
      this.iso3166 = iso3166;
    }
  }

  /**
   * @return
   */
  public String getIso31662() {
    return iso31662;
  }

  /**
   * @param iso31662
   */
  public void setIso31662(String iso31662) {
    if (iso31662 == null) {
      this.iso31662 = " ";
    } else {
      this.iso31662 = iso31662;
    }
  }

  /**
   * @return
   */
  public String getIso31663() {
    return iso31663;
  }

  /**
   * @param iso31663
   */
  public void setIso31663(String iso31663) {
    if (iso31663 == null) {
      this.iso31663 = " ";
    } else {
      this.iso31663 = iso31663;
    }
  }

  /**
   * @return
   */
  public String getUnm49region() {
    return unm49region;
  }

  /**
   * @param unm49region
   */
  public void setUnm49region(String unm49region) {
    if (unm49region == null) {
      this.unm49region = " ";
    } else {
      this.unm49region = unm49region;
    }
  }

  /**
   * @return
   */
  public String getUnm49subregion() {
    return unm49subregion;
  }

  /**
   * @param unm49subregion
   */
  public void setUnm49subregion(String unm49subregion) {
    if (unm49subregion == null) {
      this.unm49subregion = " ";
    } else {
      this.unm49subregion = unm49subregion;
    }
  }

  /**
   * @return
   */
  public String getUnm49subsubregion() {
    return unm49subsubregion;
  }

  /**
   * @param unm49subsubregion
   */
  public void setUnm49subsubregion(String unm49subsubregion) {
    if (unm49subsubregion == null) {
      this.unm49subsubregion = " ";
    } else {
      this.unm49subsubregion = unm49subsubregion;
    }
  }

  /**
   * @return
   */
  public String getCategoryType() {
    return categoryType;
  }

  /**
   * @param categoryType
   */
  public void setCategoryType(String categoryType) {
    this.categoryType = categoryType;
  }

  /**
   * @return
   */
  public String getQualifiedName() {
    return qualifiedName;
  }

  /**
   * @param qualifiedName
   */
  public void setQualifiedName(String qualifiedName) {
    this.qualifiedName = qualifiedName;
  }

  public String getContenttype() {
    return contenttype;
  }

  public void setContenttype(String contenttype) {
    this.contenttype = contenttype;
  }

  public String getMediaformats() {
    return mediaformats;
  }

  public void setMediaformats(String mediaformats) {
    this.mediaformats = mediaformats;
  }

  public String getPersona() {
    return persona;
  }

  public void setPersona(String persona) {
    this.persona = persona;
  }

  public String getGeography() {
    return geography;
  }

  public void setGeography(String geography) {
    this.geography = geography;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getInsight() {
    return insight;
  }

  public void setInsight(String insight) {
    this.insight = insight;
  }

  public String getMarket() {
    return market;
  }

  public void setMarket(String market) {
    this.market = market;
  }

  @Override
  public String toString() {
    return "Tag [tagID="
        + tagID
        + ", title="
        + title
        + ", path="
        + path
        + ", idPath="
        + idPath
        + ", idDisplayPath="
        + idDisplayPath
        + ", localIdPath="
        + localIdPath
        + ", keywords="
        + keywords
        + ", ruleBaseClass="
        + ruleBaseClass
        + ", localTagID="
        + localTagID
        + ", localPath="
        + localPath
        + ", qualifiedName="
        + qualifiedName
        + ", localKeywords="
        + localKeywords
        + ", iso3166="
        + iso3166
        + ", iso31662="
        + iso31662
        + ", iso31663="
        + iso31663
        + ", unm49region="
        + unm49region
        + ", unm49subregion="
        + unm49subregion
        + ", unm49subsubregion="
        + unm49subsubregion
        + ", categoryType="
        + categoryType
        + ", contenttype="
        + contenttype
        + ", mediaformats="
        + mediaformats
        + ", persona="
        + persona
        + ", geography="
        + geography
        + ", industry="
        + industry
        + ", service="
        + service
        + ", insight="
        + insight
        + ", market="
        + market
        + ", localIdDisplayPath="
        + localIdDisplayPath
        + "]";
  }
}
