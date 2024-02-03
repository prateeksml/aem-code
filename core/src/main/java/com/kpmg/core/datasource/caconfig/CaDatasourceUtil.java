package com.kpmg.core.datasource.caconfig;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.TitleValueItemConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;

public class CaDatasourceUtil {

  private CaDatasourceUtil() {} // utility class

  public static List<DataSourceOption> toOptions(TitleValueItemConfig[] array) {
    return toOptionsGeneric(array, config -> new DataSourceOption(config.title(), config.value()));
  }

  public static <T> List<DataSourceOption> toOptionsGeneric(
      T[] array, Function<T, DataSourceOption> transformer) {
    if (ArrayUtils.isEmpty(array)) return Collections.emptyList();
    else {
      return Arrays.stream(array).map(transformer::apply).collect(Collectors.toList());
    }
  }
}
