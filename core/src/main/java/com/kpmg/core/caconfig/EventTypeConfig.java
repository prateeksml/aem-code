package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Event Type Config",
    description =
        "A collection of event types used for dropdowns in page properties and possibly other dropdowns")
public @interface EventTypeConfig {

  @Property(label = "Event Type Items")
  TitleValueItemConfig[] items() default {};
}
