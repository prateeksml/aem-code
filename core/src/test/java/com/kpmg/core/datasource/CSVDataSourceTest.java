package com.kpmg.core.datasource;

import static org.junit.jupiter.api.Assertions.*;

import com.adobe.acs.commons.wcm.datasources.DataSourceBuilder;
import com.adobe.acs.commons.wcm.datasources.impl.DataSourceBuilderImpl;
import com.adobe.granite.ui.components.ds.DataSource;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.builder.ImmutableValueMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class CSVDataSourceTest {

  private final AemContext context = AppAemContext.newAemContext();

  CSVDataSource csvDataSource = new CSVDataSource();

  String title1 = "Title 1";
  String title2 = "Title 2";
  String value1 = "value1";
  String value2 = "value1";

  @Test
  void doGetCSV() {
    String path = "/content/csv";
    String CSV = title1 + ":" + value1 + "," + title2 + ":" + value2;
    context
        .build()
        .resource(path)
        .commit()
        .resource(path + "/datasource", ImmutableValueMap.of("csv", CSV))
        .commit();
    context.currentResource(path);
    csvDataSource.dataSourceBuilder = new DataSourceBuilderImpl();
    csvDataSource.doGet(context.request(), context.response());

    DataSource requestDs = (DataSource) context.request().getAttribute(DataSource.class.getName());
    Iterator<Resource> iterator = requestDs.iterator();
    assertDatasourceItem(getNextValueMap(iterator), title1, value1);
    assertDatasourceItem(getNextValueMap(iterator), title2, value2);
  }

  @Test
  void doGetCSVFile() {
    String path = "/content/csv2";
    String csvPath = "file.csv";
    String CSV = title1 + "," + value1 + "\n" + title2 + "," + value2;
    context.build().file(csvPath, new ByteArrayInputStream(CSV.getBytes()));
    context
        .build()
        .resource(path)
        .commit()
        .resource(path + "/datasource", ImmutableValueMap.of("csvFile", "/" + csvPath))
        .commit();
    context.currentResource(path);
    csvDataSource.dataSourceBuilder = new DataSourceBuilderImpl();
    csvDataSource.doGet(context.request(), context.response());

    DataSource requestDs = (DataSource) context.request().getAttribute(DataSource.class.getName());
    Iterator<Resource> iterator = requestDs.iterator();
    assertDatasourceItem(getNextValueMap(iterator), title1, value1);
    assertDatasourceItem(getNextValueMap(iterator), title2, value2);
  }

  private ValueMap getNextValueMap(Iterator<Resource> iterator) {
    return iterator.next().getValueMap();
  }

  private void assertDatasourceItem(ValueMap dsItem, String title, String value) {
    assertEquals(title, dsItem.get(DataSourceBuilder.TEXT, String.class));
    assertEquals(value, dsItem.get(DataSourceBuilder.VALUE, String.class));
  }
}
