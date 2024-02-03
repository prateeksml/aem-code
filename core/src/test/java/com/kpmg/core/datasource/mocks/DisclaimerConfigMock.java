package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.DisclaimerConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class DisclaimerConfigMock implements DisclaimerConfig {

  TitleValueItemConfig[] items;

  public DisclaimerConfigMock(TitleValueItemConfig[] items) {
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
