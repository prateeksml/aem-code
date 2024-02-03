package com.kpmg.core.datasource.caconfig.providers;

import java.util.function.Consumer;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.mockito.Mockito;

public class AbstractProviderTest {

  <T> void doWithMockBuilder(
      T config, Class<T> configClass, Consumer<ConfigurationBuilder> consumer) {
    ConfigurationBuilder builder = Mockito.mock(ConfigurationBuilder.class);
    Mockito.when(builder.as(configClass)).thenReturn(config);
    consumer.accept(builder);
  }
}
