package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Article Primary Format Config",
    description =
        "A collection of article primary format typesused for dropdowns in page properties and possibly other dropdowns")
public @interface ArticlePrimaryFormatConfig {

  @Property(label = "Article Primary Format Items")
  TitleValueItemConfig[] items() default {};
}
