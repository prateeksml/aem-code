package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.*;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.SocialIconsConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.SocialIconsConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class SocialIconsDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    SocialIconsDataSourceProvider provider = new SocialIconsDataSourceProvider();
    assertEquals(SocialIconsDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    SocialIconsConfig config = new SocialIconsConfigMock(items);

    doWithMockBuilder(
        config,
        SocialIconsConfig.class,
        (builder) -> {
          SocialIconsDataSourceProvider provider = new SocialIconsDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
