package com.kpmg.core.models.datalayer;

import com.adobe.cq.wcm.core.components.models.datalayer.PageData;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface KPMGPageData extends PageData {
  @JsonProperty("pageName")
  String getPageName();

  @JsonProperty("expiryDate")
  String getExpiryDate();

  @JsonProperty("issueDate")
  String getIssueDate();

  @JsonProperty("effectiveDate")
  String getEffectiveDate();

  @JsonProperty("country")
  String getCountry();

  @JsonProperty("primaryCategory")
  String getPrimaryCategory();

  @JsonProperty("subCategory1")
  String getSubCategory1();

  @JsonProperty("subCategory2")
  public String getSubCategory2();

  @JsonProperty("subCategory3")
  String getSubCategory3();

  @JsonProperty("publisher")
  String getOwner();
}
