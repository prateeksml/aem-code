package com.kpmg.integration.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - Elastic Search Configurations",
    description = "KPMG - Elastic Search Configurations")
public @interface ElasticSearchConfiguration {

  @AttributeDefinition(
      name = "Elastic Search Host",
      description = "Enter Elastic Search Host Details")
  String getElasticSearchHost();

  @AttributeDefinition(
      name = "Elastic Search Port",
      description = "Enter Elastic Search Port Details")
  int getElasticSearchPort();

  @AttributeDefinition(
      name = "Elastic Search API keys",
      description = "Enter Elastic Search API Keys ")
  String getEsApiKey();

  @AttributeDefinition(
      name = "Certificate Alias",
      description = "Enter Certificate Alias for Elastic Search Endpoint")
  String getEsCertsAlias();
}
