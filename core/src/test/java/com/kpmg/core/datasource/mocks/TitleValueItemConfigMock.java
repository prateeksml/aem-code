package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class TitleValueItemConfigMock implements TitleValueItemConfig {

  String title;
  String value;

  public TitleValueItemConfigMock(String title, String value) {
    this.title = title;
    this.value = value;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public String value() {
    return value;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return null;
  }
}
