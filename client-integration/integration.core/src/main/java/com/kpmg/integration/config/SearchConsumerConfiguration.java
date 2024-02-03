package com.kpmg.integration.config;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - Search Consumer Configurations",
    description = "Search Consumer Configurations")
public @interface SearchConsumerConfiguration {

  @AttributeDefinition(name = "searchEndpoint", description = "Enter Mulesoft Endpoint for Search")
  String getSearchEndpoint() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "suggestEndpoint",
      description = "Enter Mulesoft Endpoint for Suggestive Search")
  String getSuggestEndpoint() default StringUtils.EMPTY;

  @AttributeDefinition(name = "pagination", description = "Enter Pagination Number For Search")
  int getPagination() default 10;

  @AttributeDefinition(name = "wcmmode", description = "Enter AEM WCM Mode")
  String getWcmMode() default StringUtils.EMPTY;
}
