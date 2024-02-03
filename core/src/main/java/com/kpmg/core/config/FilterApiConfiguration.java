package com.kpmg.core.config;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - Filter And Listing Configuration",
    description = "Filter And Listing Configuration")
public @interface FilterApiConfiguration {
  @AttributeDefinition(
      name = "filterEndpoint",
      description = "Enter Mulesoft Endpoint for Filter API")
  String getFilterEndpoint() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "listingEndpoint",
      description = "Enter Mulesoft Endpoint for Listing API")
  String getListingEndpoint() default StringUtils.EMPTY;

  @AttributeDefinition(name = "pagination", description = "Enter Pagination Number")
  int getPagination() default 8;
}
