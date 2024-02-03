package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Contact Type Config",
    description =
        "A collection of contact types used for dropdowns in page properties and possibly other dropdowns")
public @interface ContactTypeConfig {

  @Property(label = "Contact Type Items")
  TitleValueItemConfig[] items() default {};
}
