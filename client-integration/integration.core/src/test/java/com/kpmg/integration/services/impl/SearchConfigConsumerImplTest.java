package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class SearchConfigConsumerImplTest {

  private final AemContext context = new AemContext();

  @Test
  public void testWithoutConfig() {
    final SearchConsumerImpl searchConsumerImpl =
        context.registerInjectActivateService(new SearchConsumerImpl());
    assertNull(searchConsumerImpl.getSearchEndpoint());
  }

  @Test
  public void testWithConfig() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSuggestEndpoint", "www.kpmg.com/suggest");
    properties.put("getPagination", 10);
    final SearchConsumerImpl searchConsumerImpl =
        context.registerInjectActivateService(new SearchConsumerImpl(), properties);
    assertEquals("www.kpmg.com/suggest", searchConsumerImpl.getSuggestEndpoint());
    assertEquals(10, searchConsumerImpl.getPagination());
  }
}
