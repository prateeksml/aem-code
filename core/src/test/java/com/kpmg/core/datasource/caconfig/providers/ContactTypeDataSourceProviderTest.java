package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.ContactTypeConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.ContactTypeConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class ContactTypeDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    ContactTypeDataSourceProvider provider = new ContactTypeDataSourceProvider();
    assertEquals(ContactTypeDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    ContactTypeConfig config = new ContactTypeConfigMock(items);

    doWithMockBuilder(
        config,
        ContactTypeConfig.class,
        (builder) -> {
          ContactTypeDataSourceProvider provider = new ContactTypeDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
