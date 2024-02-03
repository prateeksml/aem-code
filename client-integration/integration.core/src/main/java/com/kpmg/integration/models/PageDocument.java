package com.kpmg.integration.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface PageDocument {

  String getDocumentId();

  String getTitle();

  String getDescription();

  int getFilterYear();

  String getFilterDate();

  String getQualifiedUrl();

  String getImageUrl();

  String getImageAltText();

  String getDocumentType();

  String getBodyContent();

  String getKeywordsList();

  String getLastModified();

  String getIndexedTime();

  String[] getSlContentZthesIds();

  String[] getSlContentQualifiedName();

  String[] getSlContentQualifiedNameId();

  String[] getSlContentHierarchy();

  String[] getSlContentHierarchyId();

  String[] getSlMediaZthesIds();

  String[] getSlMediaQualifiedName();

  String[] getSlMediaQualifiedNameId();

  String[] getSlMediaHierarchy();

  String[] getSlMediaHierarchyId();

  String[] getSlPersonaZthesIds();

  String[] getSlPersonaQualifiedName();

  String[] getSlPersonaQualifiedNameId();

  String[] getSlPersonaHierarchy();

  String[] getSlPersonaHierarchyId();

  String[] getSlIndustryZthesIds();

  String[] getSlIndustryQualifiedName();

  String[] getSlIndustryQualifiedNameId();

  String[] getSlIndustryHierarchy();

  String[] getSlIndustryHierarchyId();

  String[] getSlServiceZthesIds();

  String[] getSlServiceQualifiedName();

  String[] getSlServiceQualifiedNameId();

  String[] getSlServiceHierarchy();

  String[] getSlServiceHierarchyId();

  String[] getSlMarketZthesIds();

  String[] getSlMarketQualifiedName();

  String[] getSlMarketQualifiedNameId();

  String[] getSlMarketHierarchy();

  String[] getSlMarketHierarchyId();

  String[] getSlInsightZthesIds();

  String[] getSlInsightQualifiedName();

  String[] getSlInsightQualifiedNameId();

  String[] getSlInsightHierarchy();

  String[] getSlInsightHierarchyId();

  String[] getSlGeographyZthesIds();

  String[] getSlGeographyQualifiedName();

  String[] getSlGeographyQualifiedNameId();

  String[] getSlGeographyHierarchy();

  String[] getSlGeographyHierarchyId();

  String[] getSlGeographyIso3166();

  String[] getSlGeographyIso31663();

  String[] getSlGeographyIso31662();

  String[] getSlGeographyUnm49Region();

  String[] getSlGeographyUnm49SubRegion();

  String[] getSlGeographyUnm49SubSubRegion();

  String[] getSlInsightCommonZthesIds();

  String[] getSlIndustryCommonZthesIds();

  String[] getSlServiceCommonZthesIds();

  String[] getSlInsightCommonHierarchy();

  String[] getSlIndustryCommonHierarchy();

  String[] getSlServiceCommonHierarchy();

  String getFormattedFilterDate();

  String getFormattedLastModified();
}
