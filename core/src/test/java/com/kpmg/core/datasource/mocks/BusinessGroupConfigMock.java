package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.BusinessGroupConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class BusinessGroupConfigMock implements BusinessGroupConfig {

  TitleValueItemConfig[] items;

  public BusinessGroupConfigMock(TitleValueItemConfig[] items) {
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
