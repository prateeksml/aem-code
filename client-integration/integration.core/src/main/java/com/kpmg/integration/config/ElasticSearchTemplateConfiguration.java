package com.kpmg.integration.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - Elastic Search Template Configurations",
    description = "KPMG - Elastic Search Template Configuration")
public @interface ElasticSearchTemplateConfiguration {

  @AttributeDefinition(name = "documentType", description = "Elastic Search Document Type")
  String getDocumentType();

  @AttributeDefinition(
      name = "templates",
      description = "Enter Elastic Search Template for the Respective Document Type")
  String[] getTemplates();
}
