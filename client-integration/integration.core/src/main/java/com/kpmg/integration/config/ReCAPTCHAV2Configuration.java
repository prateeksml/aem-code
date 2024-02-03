package com.kpmg.integration.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - reCAPTCHA V2 Configurations",
    description = "KPMG - reCAPTCHA V2 Configurations")
public @interface ReCAPTCHAV2Configuration {

  @AttributeDefinition(name = "siteKey", description = "Please add sitekey for reCAPTCHA V2")
  String getSiteKey();

  @AttributeDefinition(name = "siteSecret", description = "Please add siteSecret for reCAPTCHA V2")
  String getSiteSecret();
}
