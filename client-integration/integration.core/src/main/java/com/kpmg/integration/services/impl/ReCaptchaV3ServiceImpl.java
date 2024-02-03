package com.kpmg.integration.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kpmg.integration.config.ReCAPTCHAV3Configuration;
import com.kpmg.integration.services.ReCaptchaV3Service;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = ReCaptchaV3Service.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ReCAPTCHAV3Configuration.class)
public class ReCaptchaV3ServiceImpl implements ReCaptchaV3Service {

  private static final Logger LOG = LoggerFactory.getLogger(ReCaptchaV3ServiceImpl.class);

  private String captchaEndpoint;
  private String siteKey;
  private String siteSecret;
  private double scoreTolerance;
  private String captchaEnabled;

  @Activate
  @Modified
  protected void activate(ReCAPTCHAV3Configuration configuration) {
    this.siteKey = configuration.getSiteKey();
    this.siteSecret = configuration.getSiteSecret();
    this.captchaEndpoint = configuration.getCaptchaEndpoint();
    this.scoreTolerance = configuration.getScoreTolerance();
    this.captchaEnabled = configuration.getCaptchaEnabled();
  }

  @Override
  public String getCaptchaEndpoint() {
    return this.captchaEndpoint;
  }

  @Override
  public String getSiteKey() {
    return this.siteKey;
  }

  @Override
  public String getSiteSecret() {
    return this.siteSecret;
  }

  @Override
  public double getScoreTolerance() {
    return this.scoreTolerance;
  }

  @Override
  public String getCaptchaEnabled() {
    return captchaEnabled;
  }

  @Override
  public boolean isCaptchaVerified(String token) {
    LOG.debug("IS CAPTCHA ENABLED {}", getCaptchaEnabled());
    if (StringUtils.equalsIgnoreCase(getCaptchaEnabled(), "false")) {
      return true;
    }
    if (token == null || StringUtils.isEmpty(token)) {
      return false;
    }
    String captchaEndpointURL =
        new StringBuilder(
                this.captchaEndpoint
                    .concat("?secret=" + this.siteSecret)
                    .concat("&response=" + token))
            .toString();

    int defaultTimeout = 5;
    RequestConfig config =
        RequestConfig.custom()
            .setConnectTimeout(defaultTimeout * 1000)
            .setConnectionRequestTimeout(defaultTimeout * 1000)
            .setSocketTimeout(defaultTimeout * 1000)
            .build();
    try {
      HttpClient httpClient =
          HttpClientBuilder.create().useSystemProperties().setDefaultRequestConfig(config).build();
      HttpGet httpRequest = new HttpGet(captchaEndpointURL);
      HttpResponse httpResponse;
      httpResponse = httpClient.execute(httpRequest);
      String captchaResponse = EntityUtils.toString(httpResponse.getEntity());
      LOG.debug("CAPTCHA RESPONSE -- {}", captchaResponse);
      JsonObject captchaJson = new Gson().fromJson(captchaResponse, JsonObject.class);
      Boolean captchaSuccess =
          Optional.ofNullable(captchaJson.get("success"))
              .map(JsonElement::getAsBoolean)
              .orElse(false);
      double captchaScore =
          Optional.ofNullable(captchaJson.get("score")).map(JsonElement::getAsDouble).orElse(0.0);
      LOG.debug("CAPTCHA SUCCESS --> {}- SCORE {}", captchaSuccess, captchaScore);
      return (captchaSuccess && captchaScore >= this.scoreTolerance);
    } catch (IOException e) {
      LOG.error("AN ERROR OCCURRED IN CAPTCHA REQUEST {}", e.getMessage());
    }
    return false;
  }
}
