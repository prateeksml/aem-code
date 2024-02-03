package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.ContactTypeConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class ContactTypeConfigMock implements ContactTypeConfig {

  TitleValueItemConfig[] items;

  public ContactTypeConfigMock(TitleValueItemConfig[] items) {
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
