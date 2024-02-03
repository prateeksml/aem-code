package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.CountryConfig;
import java.lang.annotation.Annotation;

public class CountryConfigMock implements CountryConfig {

  CountryItemConfig[] items;

  public CountryConfigMock(CountryItemConfig[] items) {
    this.items = items;
  }

  @Override
  public CountryItemConfig[] items() {
    return items;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return null;
  }

  public static class CountryItemConfigMock implements CountryItemConfig {
    String country;
    String isoCode;
    String nodeName;

    public CountryItemConfigMock(String country, String isoCode, String nodeName) {
      this.country = country;
      this.isoCode = isoCode;
      this.nodeName = nodeName;
    }

    @Override
    public String country() {
      return country;
    }

    @Override
    public String isoCode() {
      return isoCode;
    }

    @Override
    public String nodeName() {
      return nodeName;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
      return null;
    }
  }
}
