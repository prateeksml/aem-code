package com.kpmg.integration.services;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import org.elasticsearch.client.RestClient;

public interface ElasticClientService {

  ElasticsearchAsyncClient getESAsyncClient();

  RestClient createRestClient();

  String getEsApiKey();

  String getEsCertsAlias();

  String getEsHost();

  int getEsPort();
}
