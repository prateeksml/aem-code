package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Property;

public @interface TitleValueItemConfig {

  @Property(label = "Title")
  String title();

  @Property(
      label = "Value",
      property = {"widgetType=textarea"})
  String value();
}
