package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.ArticlePrimaryFormatConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class ArticlePrimaryFormatConfigMock implements ArticlePrimaryFormatConfig {

  TitleValueItemConfig[] items;

  public ArticlePrimaryFormatConfigMock(TitleValueItemConfig[] items) {
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
