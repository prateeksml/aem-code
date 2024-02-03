package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Business Owner Group Config",
    description =
        "A collection of business owner groups used for dropdowns in page properties and possibly other dropdowns")
public @interface BusinessGroupConfig {

  @Property(label = "Business Owner Group Items")
  TitleValueItemConfig[] items() default {};
}
