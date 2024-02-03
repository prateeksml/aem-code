package com.kpmg.integration.services.impl;

import com.kpmg.integration.config.ReCAPTCHAV2Configuration;
import com.kpmg.integration.config.ReCAPTCHAV3Configuration;
import com.kpmg.integration.services.ReCaptchaV2Service;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = ReCaptchaV2Service.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ReCAPTCHAV2Configuration.class)
public class ReCaptchaV2ServiceImpl implements ReCaptchaV2Service {

  private static final Logger LOG = LoggerFactory.getLogger(ReCaptchaV2ServiceImpl.class);

  private String siteKey;
  private String siteSecret;

  @Activate
  @Modified
  protected void activate(ReCAPTCHAV3Configuration configuration) {
    this.siteKey = configuration.getSiteKey();
    this.siteSecret = configuration.getSiteSecret();
  }

  @Override
  public String getSiteKey() {
    return this.siteKey;
  }

  @Override
  public String getSiteSecret() {
    return this.siteSecret;
  }
}
