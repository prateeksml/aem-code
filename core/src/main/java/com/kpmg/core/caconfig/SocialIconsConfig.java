package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Social Icons Config",
    description = "A collection of Social Icons types used for dropdowns in footer ")
public @interface SocialIconsConfig {

  @Property(label = "Social Icons")
  TitleValueItemConfig[] items() default {};
}
