package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Date Format Config",
    description = "Configuration for formatting all Dates in the current site context")
public @interface DateFormatConfig {

  @Property(
      label = "1)   Date Format",
      description = "Date Format",
      property = {"widgetType=dropdown", "dropdownOptionsProvider=kpmg.dateformatprovider"})
  String dateFormat();

  @Property(
      label = "2)   Separators",
      description = "Separators",
      property = {"widgetType=dropdown", "dropdownOptionsProvider=kpmg.dateseparatorsprovider"})
  String separators();

  @Property(label = "3)   '0' prefix required?")
  boolean isZeroPrefixRequired();

  @Property(
      label = "5)   Case Notation",
      description = "Case Notation",
      property = {
        "widgetType=dropdown",
        "dropdownOptions=["
            + "{'value':'Camel Case','description':'Camel Case'},"
            + "{'value':'Lower Case','description':'Lower Case'},"
            + "{'value':'Upper Case','description':'Upper Case'}"
            + "]"
      })
  String caseNotation();

  @Property(
      label = "4)   Time Format",
      description = "Time Format",
      property = {"widgetType=dropdown", "dropdownOptionsProvider=kpmg.datetimeformatprovider"})
  String timeFormat();

  @Property(
      label = "5)   Hour Format",
      description = "Hour Format",
      property = {
        "widgetType=dropdown",
        "dropdownOptions=["
            + "{'value':'12 Hour Format','description':'12 Hour Format'},"
            + "{'value':'24 Hour Format','description':'24 Hour Format'}"
            + "]"
      })
  String hourFormat();
}
