package com.kpmg.core.datasource.caconfig;

import static org.junit.jupiter.api.Assertions.*;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import com.kpmg.core.datasource.mocks.TitleValueItemConfigMock;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CaDatasourceUtilTest {

  @Test
  void testToOptions() {
    TitleValueItemConfig[] array = new TitleValueItemConfig[2];
    array[0] = new TitleValueItemConfigMock("title1", "value1");
    array[1] = new TitleValueItemConfigMock("title2", "value2");

    List<DataSourceOption> result = CaDatasourceUtil.toOptions(array);
    assertEquals("title1", result.get(0).getText());
    assertEquals("value1", result.get(0).getValue());
  }

  @Test
  void testToOptionsGeneric() {
    String[] array = new String[2];
    array[0] = "string1";
    array[1] = "string2";

    List<DataSourceOption> expected = new ArrayList<>();
    expected.add(new DataSourceOption("string1", "string1"));
    expected.add(new DataSourceOption("string2", "string2"));

    List<DataSourceOption> result =
        CaDatasourceUtil.toOptionsGeneric(array, (config) -> new DataSourceOption(config, config));
    assertEquals("string1", result.get(0).getText());
    assertEquals("string1", result.get(0).getValue());
  }
}
