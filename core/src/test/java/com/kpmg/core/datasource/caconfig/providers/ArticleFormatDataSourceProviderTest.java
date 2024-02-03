package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.ArticlePrimaryFormatConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.ArticlePrimaryFormatConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class ArticleFormatDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    ArticleFormatDataSourceProvider provider = new ArticleFormatDataSourceProvider();
    assertEquals(ArticleFormatDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    ArticlePrimaryFormatConfig config = new ArticlePrimaryFormatConfigMock(items);

    doWithMockBuilder(
        config,
        ArticlePrimaryFormatConfig.class,
        (builder) -> {
          ArticleFormatDataSourceProvider provider = new ArticleFormatDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
