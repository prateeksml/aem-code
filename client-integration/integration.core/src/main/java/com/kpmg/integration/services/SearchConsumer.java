package com.kpmg.integration.services;

public interface SearchConsumer {

  String getSearchEndpoint();

  String getSuggestEndpoint();

  int getPagination();

  String getWcmMode();
}
