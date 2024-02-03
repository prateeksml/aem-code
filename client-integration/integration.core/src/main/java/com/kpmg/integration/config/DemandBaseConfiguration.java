package com.kpmg.integration.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "DemandBase Integration",
    description = "KPMG- Demandbase integration")
public @interface DemandBaseConfiguration {

  @AttributeDefinition(name = "demandbaseurl", description = "Please add demandbase url")
  String getDemandbaseURL();

  @AttributeDefinition(name = "demandbasecdcurl", description = "Please add demandbasecurl URL")
  String getDemandbaseCurl();
}
