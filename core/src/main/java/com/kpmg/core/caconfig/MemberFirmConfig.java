package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Member Firm Config",
    description =
        "A collection of member firm types used for dropdowns in page properties and possibly other dropdowns")
public @interface MemberFirmConfig {

  @Property(label = "Member Firm Items")
  TitleValueItemConfig[] items() default {};
}
