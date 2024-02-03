package com.kpmg.integration.services;

public interface CacheFlush {

  public String getAkamaiAccessToken();

  public String getAkamaiClientToken();

  public String getAkamaiClientSecret();

  public String getAkamaiHostToken();

  public String getDefaultAkamaiDomain();

  public String getAssetPurgeURLPrefix();

  public String getHtmlPurgeURLPrefix();
}
