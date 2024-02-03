package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.BusinessGroupConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.BusinessGroupConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class BusinessOwnerDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    BusinessOwnerDataSourceProvider provider = new BusinessOwnerDataSourceProvider();
    assertEquals(BusinessOwnerDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    BusinessGroupConfig config = new BusinessGroupConfigMock(items);

    doWithMockBuilder(
        config,
        BusinessGroupConfig.class,
        (builder) -> {
          BusinessOwnerDataSourceProvider provider = new BusinessOwnerDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
