package com.kpmg.integration.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "KPMG - reCAPTCHA V3 Configurations",
    description = "KPMG - reCAPTCHA V3 Configurations")
public @interface ReCAPTCHAV3Configuration {
  @AttributeDefinition(
      name = "isCaptchaEnabled",
      description = "Enable Captcha For The Environment")
  String getCaptchaEnabled();

  @AttributeDefinition(
      name = "captchaEndpoint",
      description = "Please add google captcha V3 endpoint")
  String getCaptchaEndpoint();

  @AttributeDefinition(name = "siteKey", description = "Please add sitekey for reCAPTCHA V3")
  String getSiteKey();

  @AttributeDefinition(name = "siteSecret", description = "Please add siteSecret for reCAPTCHA V3")
  String getSiteSecret();

  @AttributeDefinition(
      name = "scoreTolerance",
      description = "Please add score tolerance for reCAPTCHA")
  double getScoreTolerance();
}
