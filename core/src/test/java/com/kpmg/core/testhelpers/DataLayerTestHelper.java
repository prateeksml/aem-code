package com.kpmg.core.testhelpers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.cq.wcm.core.components.internal.DataLayerConfig;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;

public class DataLayerTestHelper {
  public static void mockDatalayerEnabled(Resource resource, boolean enabled) {
    if (enabled) {
      ConfigurationBuilder configBuilderMock = mock(ConfigurationBuilder.class);
      DataLayerConfig dataLayerConfigMock = mock(DataLayerConfig.class);
      when(resource.adaptTo(ConfigurationBuilder.class)).thenReturn(configBuilderMock);
      when(configBuilderMock.as(DataLayerConfig.class)).thenReturn(dataLayerConfigMock);
      when(dataLayerConfigMock.enabled()).thenReturn(true);
    }
  }
}
