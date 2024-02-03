package com.kpmg.core.datasource.caconfig.providers;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.kpmg.core.caconfig.EventTypeConfig;
import com.kpmg.core.datasource.caconfig.CaConfigDatasourceProvider;
import com.kpmg.core.datasource.caconfig.CaDatasourceUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = CaConfigDatasourceProvider.class)
public class EventTypeDataSourceProvider implements CaConfigDatasourceProvider {

  public static final String NAME = "event-type";

  @Override
  public String getName() {
    return NAME;
  }

  /**
   * Gets the options.
   *
   * @param configurationBuilder the configuration builder
   * @return the options
   */
  @Override
  public List<DataSourceOption> getOptions(ConfigurationBuilder configurationBuilder) {
    return Optional.ofNullable(configurationBuilder)
        .map(b -> b.as(EventTypeConfig.class))
        .map(EventTypeConfig::items)
        .map(CaDatasourceUtil::toOptions)
        .orElse(Collections.emptyList());
  }
}
