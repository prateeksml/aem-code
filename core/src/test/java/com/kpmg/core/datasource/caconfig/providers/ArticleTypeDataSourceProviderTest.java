package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.*;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.ArticleTypeConfig;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.ArticleTypeConfigMock;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

class ArticleTypeDataSourceProviderTest extends AbstractProviderTest {

  @Test
  void getName() {
    ArticleTypeDataSourceProvider provider = new ArticleTypeDataSourceProvider();
    assertEquals(ArticleTypeDataSourceProvider.NAME, provider.getName());
  }

  @Test
  void getOptions() {
    TitleValueItemConfig[] items =
        ArrayUtils.toArray(new TitleValueItemConfigMock("title1", "value1"));
    ArticleTypeConfig config = new ArticleTypeConfigMock(items);

    doWithMockBuilder(
        config,
        ArticleTypeConfig.class,
        (builder) -> {
          ArticleTypeDataSourceProvider provider = new ArticleTypeDataSourceProvider();
          List<DataSourceOption> options = provider.getOptions(builder);
          assertEquals("title1", options.get(0).getText());
        });
  }
}
