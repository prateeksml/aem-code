package com.kpmg.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/** Configuration object for the sling run modes. */
@ObjectClassDefinition(name = "KPMG Runmode Configuration")
public @interface RunmodeConfig {

  @AttributeDefinition(
      name = "Sling Runmode",
      description = "Enter the sling run mode for the environment",
      type = AttributeType.STRING)
  String[] runmodes();
}
