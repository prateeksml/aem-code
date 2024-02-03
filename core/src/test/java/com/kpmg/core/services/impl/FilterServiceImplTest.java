package com.kpmg.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kpmg.core.config.FilterApiConfiguration;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FilterServiceImplTest {

  private AemContext aemContext = new AemContext();
  private FilterServiceImpl configTest;
  private FilterApiConfiguration filterApiConfiguration;
  private static final String FILTER_END_POINT = "filter-endpoint";
  private static final String LISTING_END_POINT = "listing-endpoint";

  @BeforeEach
  void setUp() {
    configTest = aemContext.registerService(new FilterServiceImpl());
    filterApiConfiguration = mock(FilterApiConfiguration.class);
    when(filterApiConfiguration.getFilterEndpoint()).thenReturn(FILTER_END_POINT);
    when(filterApiConfiguration.getListingEndpoint()).thenReturn(LISTING_END_POINT);
    when(filterApiConfiguration.getPagination()).thenReturn(10);
    configTest.activate(filterApiConfiguration);
  }

  @Test
  void getFilterEndpoint() {
    assertEquals(FILTER_END_POINT, configTest.getFilterEndpoint());
  }

  @Test
  void getSuggestEndpoint() {
    assertEquals(LISTING_END_POINT, configTest.getListingEndpoint());
  }

  @Test
  void getPagination() {
    assertEquals(10, configTest.getPagination());
  }
}
