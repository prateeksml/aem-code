package com.kpmg.core.datasource.caconfig;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.ConfigurationBuilder;

public interface CaConfigDatasourceProvider {

  String getName();

  List<DataSourceOption> getOptions(ConfigurationBuilder configurationBuilder);

  default boolean shouldHandle(String name) {
    return StringUtils.equalsIgnoreCase(name, getName());
  }
}
