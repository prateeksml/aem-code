package com.kpmg.integration.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - SmartLogic Configuration",
    description = "KPMG - Smart Logic Configuration")
public @interface SmartLogicConfiguration {

  @AttributeDefinition(
      name = "Content Type ZthesID",
      description = "Enter Content Type ZthesID",
      type = AttributeType.STRING)
  String getContentTypeZthesID();

  @AttributeDefinition(
      name = "Media Formats ZthesID",
      description = "Enter Media Formats ZthesID",
      type = AttributeType.STRING)
  String getMediaFormatsZthesID();

  @AttributeDefinition(
      name = "Personas ZthesID",
      description = "Enter Personas ZthesID",
      type = AttributeType.STRING)
  String getPersonasZthesID();

  @AttributeDefinition(
      name = "Geopraphy ZthesID",
      description = "Enter Geopraphy ZthesID",
      type = AttributeType.STRING)
  String getGeopraphyZthesID();

  @AttributeDefinition(
      name = "Industry ZthesID",
      description = "Enter Industry ZthesID",
      type = AttributeType.STRING)
  String getIndustryZthesID();

  @AttributeDefinition(
      name = "Service ZthesID",
      description = "Enter Service ZthesID",
      type = AttributeType.STRING)
  String getServiceZthesID();

  @AttributeDefinition(
      name = "Insight ZthesID",
      description = "Enter Insight ZthesID",
      type = AttributeType.STRING)
  String getInsightZthesID();

  @AttributeDefinition(
      name = "Markets ZthesID",
      description = "Enter Markets ZthesID",
      type = AttributeType.STRING)
  String getMarketsZthesID();

  @AttributeDefinition(
      name = "Ses Client HostIP",
      description = "Enter Ses Client HostIP",
      type = AttributeType.STRING)
  String getSesClientHostIP();

  @AttributeDefinition(
      name = "Ses Port",
      description = "Enter Ses Port",
      type = AttributeType.STRING)
  String getSesPort();

  @AttributeDefinition(
      name = "Ses Client Path",
      description = "Enter Ses Client Path",
      type = AttributeType.STRING)
  String getSesClientPath();

  @AttributeDefinition(
      name = "Ses Client Protocol",
      description = "Enter Ses Client Protocol",
      type = AttributeType.STRING)
  String getSesClientProtocol();

  @AttributeDefinition(
      name = "Common ZthesID",
      description = "Enter Common ZthesID",
      type = AttributeType.STRING)
  String getCommonZthesID();

  @AttributeDefinition(
      name = "Disabled Templates",
      description = "Enter the Templates where Smartlogic Tab must be disabled",
      type = AttributeType.STRING)
  String[] getDisabledTemplates();
}
