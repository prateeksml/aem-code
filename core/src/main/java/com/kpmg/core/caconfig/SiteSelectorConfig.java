package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "SiteSelector Config",
    description = "A collection of Countries, their ISO Codes and locales.")
public @interface SiteSelectorConfig {
  @Property(label = "SiteSelector Items")
  SiteSelectorItemConfig[] items() default {};

  @interface SiteSelectorItemConfig {

    @Property(label = "Country")
    String country();

    @Property(label = "Language Code")
    String locale();

    @Property(label = "CountrySiteLink")
    String siteLink();
  }
}
