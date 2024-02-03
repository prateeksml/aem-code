package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.MemberFirmConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.MemberFirmConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class MemberFirmTypeDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    MemberFirmTypeDataSourceProvider provider = new MemberFirmTypeDataSourceProvider();
    assertEquals(MemberFirmTypeDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    MemberFirmConfig config = new MemberFirmConfigMock(items);

    doWithMockBuilder(
        config,
        MemberFirmConfig.class,
        (builder) -> {
          MemberFirmTypeDataSourceProvider provider = new MemberFirmTypeDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
