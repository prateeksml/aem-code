package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.SocialIconsConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class SocialIconsConfigMock implements SocialIconsConfig {

  TitleValueItemConfig[] items;

  public SocialIconsConfigMock(TitleValueItemConfig[] items) {
    this.items = items;
  }

  @Override
  public TitleValueItemConfig[] items() {
    return items;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return null;
  }
}
