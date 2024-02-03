package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Country Config",
    description = "A collection of Countries, their ISO Codes and node names.")
public @interface CountryConfig {

  @Property(label = "Country Items")
  CountryItemConfig[] items() default {};

  @interface CountryItemConfig {

    @Property(label = "Country")
    String country();

    @Property(label = "ISO  Code")
    String isoCode();

    @Property(label = "Country Node Name")
    String nodeName();
  }
}
