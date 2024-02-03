package com.kpmg.integration.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - Cache Flush Configuration",
    description = "KPMG - Cache Flush Configuration")
public @interface CacheFlushConfiguration {

  @AttributeDefinition(
      name = "Default Akamai Domain",
      description = "Enter Default Akamai Domain",
      type = AttributeType.STRING)
  String getDefaultAkamaiDomain();

  @AttributeDefinition(
      name = "Akamai Host Token",
      description = "Enter Akamai Host Token",
      type = AttributeType.STRING)
  String getAkamaiHostToken();

  @AttributeDefinition(
      name = "Akamai Client Secret",
      description = "Enter Akamai Client Secret",
      type = AttributeType.STRING)
  String getAkamaiClientSecret();

  @AttributeDefinition(
      name = "Akamai Client Token",
      description = "Enter Akamai Client Token",
      type = AttributeType.STRING)
  String getAkamaiClientToken();

  @AttributeDefinition(
      name = "Akamai Access Token",
      description = "Enter Akamai Access Token",
      type = AttributeType.STRING)
  String getAkamaiAccessToken();

  @AttributeDefinition(
      name = "Html Purge URL Prefix",
      description = "Enter Html Purge URL Prefix",
      type = AttributeType.STRING)
  String getHtmlPurgeURLPrefix();

  @AttributeDefinition(
      name = "Asset Purge URL Prefix",
      description = "Enter Asset Purge URL Prefix",
      type = AttributeType.STRING)
  String getAssetPurgeURLPrefix();
}
