package com.kpmg.integration.services.impl;

import com.kpmg.integration.config.CacheFlushConfiguration;
import com.kpmg.integration.services.CacheFlush;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = CacheFlush.class, immediate = true)
@Designate(ocd = CacheFlushConfiguration.class)
public class CacheFlushImpl implements CacheFlush {
  private String akamaiAccessToken;
  private String akamaiClientSecret;
  private String akamaiHostToken;
  private String defaultAkamaiDomain;
  private String akamaiClientToken;
  private String htmlPurgeURLPrefix;
  private String assetPurgeURLPrefix;

  @Activate
  protected void activate(CacheFlushConfiguration configuration) {
    this.akamaiAccessToken = configuration.getAkamaiAccessToken();
    this.akamaiClientSecret = configuration.getAkamaiClientSecret();
    this.akamaiClientToken = configuration.getAkamaiClientToken();
    this.akamaiHostToken = configuration.getAkamaiHostToken();
    this.defaultAkamaiDomain = configuration.getDefaultAkamaiDomain();
    this.htmlPurgeURLPrefix = configuration.getHtmlPurgeURLPrefix();
    this.assetPurgeURLPrefix = configuration.getAssetPurgeURLPrefix();
  }

  @Override
  public String getAkamaiAccessToken() {
    return akamaiAccessToken;
  }

  @Override
  public String getAkamaiClientSecret() {
    return akamaiClientSecret;
  }

  @Override
  public String getAkamaiHostToken() {
    return akamaiHostToken;
  }

  @Override
  public String getDefaultAkamaiDomain() {
    return defaultAkamaiDomain;
  }

  @Override
  public String getAkamaiClientToken() {
    return akamaiClientToken;
  }

  @Override
  public String getAssetPurgeURLPrefix() {
    return assetPurgeURLPrefix;
  }

  @Override
  public String getHtmlPurgeURLPrefix() {
    return htmlPurgeURLPrefix;
  }
}
