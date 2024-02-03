package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.*;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.CountryConfig;
import com.kpmg.core.datasource.mocks.CountryConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class CountryDataSourceProviderTest extends AbstractProviderTest {

  CountryDataSourceProvider provider = new CountryDataSourceProvider();

  @Test
  void getName() {
    assertEquals(CountryDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    CountryConfig.CountryItemConfig[] items =
        ArrayUtils.toArray(
            new CountryConfigMock.CountryItemConfigMock("country1", "isocode1", "nodename1"));
    CountryConfig config = new CountryConfigMock(items);

    doWithMockBuilder(
        config,
        CountryConfig.class,
        (builder) -> {
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("country1", options.get(0).getText());
        });
  }
}
