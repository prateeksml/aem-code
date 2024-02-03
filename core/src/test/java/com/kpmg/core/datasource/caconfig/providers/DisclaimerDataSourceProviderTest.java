package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.*;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.DisclaimerConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.DisclaimerConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class DisclaimerDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    DisclaimerDataSourceProvider provider = new DisclaimerDataSourceProvider();
    assertEquals(DisclaimerDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    DisclaimerConfig config = new DisclaimerConfigMock(items);

    doWithMockBuilder(
        config,
        DisclaimerConfig.class,
        (builder) -> {
          DisclaimerDataSourceProvider provider = new DisclaimerDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
