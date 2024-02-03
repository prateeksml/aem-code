package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.ArticleTypeConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class ArticleTypeConfigMock implements ArticleTypeConfig {

  TitleValueItemConfig[] items;

  public ArticleTypeConfigMock(TitleValueItemConfig[] items) {
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
