package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Disclaimer Configuration",
    description =
        "A collection of disclaimer used for dropdowns in page properties and possibly other dropdowns")
public @interface DisclaimerConfig {

  @Property(label = "Disclaimer Items")
  TitleValueItemConfig[] items() default {};
}
