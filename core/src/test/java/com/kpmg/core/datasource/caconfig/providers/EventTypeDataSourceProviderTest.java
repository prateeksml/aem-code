package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.EventTypeConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.EventTypeConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class EventTypeDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    EventTypeDataSourceProvider provider = new EventTypeDataSourceProvider();
    assertEquals(EventTypeDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    EventTypeConfig config = new EventTypeConfigMock(items);

    doWithMockBuilder(
        config,
        EventTypeConfig.class,
        (builder) -> {
          EventTypeDataSourceProvider provider = new EventTypeDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
