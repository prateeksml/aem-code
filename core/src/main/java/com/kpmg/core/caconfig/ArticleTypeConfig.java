package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Article Type Config",
    description =
        "A collection of article types used for dropdowns in page properties and possibly other dropdowns")
public @interface ArticleTypeConfig {

  @Property(label = "Article Type Items")
  TitleValueItemConfig[] items() default {};
}
