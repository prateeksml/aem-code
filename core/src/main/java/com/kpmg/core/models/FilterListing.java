package com.kpmg.core.models;

public interface FilterListing {
  String getTitle();

  String getDescription();

  String getFilterEndpoint();

  String getListingEndpoint();

  String getCountryCode();

  String getLangCode();

  int getPagination();
}
