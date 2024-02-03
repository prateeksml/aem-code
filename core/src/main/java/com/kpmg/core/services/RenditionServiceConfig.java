package com.kpmg.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "KPMG Rendition Service Configuration")
public @interface RenditionServiceConfig {

  @AttributeDefinition(
      name = "Component Name",
      description = "Name of the Component",
      type = AttributeType.STRING)
  String componentName();

  @AttributeDefinition(
      name = "Renditions",
      description = "Rendition for Various Tenants",
      type = AttributeType.STRING)
  String[] renditions();
}
