package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.EventTypeConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class EventTypeConfigMock implements EventTypeConfig {

  TitleValueItemConfig[] items;

  public EventTypeConfigMock(TitleValueItemConfig[] items) {
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
