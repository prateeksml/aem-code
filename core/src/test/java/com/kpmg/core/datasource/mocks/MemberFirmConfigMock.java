package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.MemberFirmConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.lang.annotation.Annotation;

public class MemberFirmConfigMock implements MemberFirmConfig {

  TitleValueItemConfig[] items;

  public MemberFirmConfigMock(TitleValueItemConfig[] items) {
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
