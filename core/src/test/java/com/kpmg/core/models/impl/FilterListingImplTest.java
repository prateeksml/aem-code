package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.services.FilterService;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FilterListingImplTest {

  private static final String TITLE = "Filter Component";
  private static final String DESCRIPTION = "Filter Component's Description";
  private static final String FILTER_END_POINT = "filter-endpoint";
  private static final String LISTING_END_POINT = "listing-endpoint";
  private static final String COUNTRY_CODE = "ch";
  private static final String LANGUAGE_CODE = "de";

  @InjectMocks private FilterListingImpl testClass;

  @Mock private FilterService filterService;

  @Mock private Page page;

  @SuppressWarnings("deprecation")
  @BeforeEach
  void setUp() throws NoSuchFieldException {
    MockitoAnnotations.initMocks(this);
    PrivateAccessor.setField(this.testClass, "title", TITLE);
    PrivateAccessor.setField(this.testClass, "description", DESCRIPTION);
    PrivateAccessor.setField(this.testClass, "countryCode", COUNTRY_CODE);
    PrivateAccessor.setField(this.testClass, "currentPage", page);
    PrivateAccessor.setField(this.testClass, "langCode", LANGUAGE_CODE);
    when(this.filterService.getFilterEndpoint()).thenReturn(FILTER_END_POINT);
    when(this.filterService.getListingEndpoint()).thenReturn(LISTING_END_POINT);
    when(this.filterService.getPagination()).thenReturn(8);
    when(page.getPath()).thenReturn("/content/kpmgpublic/ch/en/home");
  }

  @Test
  void getDescription() {
    assertEquals(DESCRIPTION, testClass.getDescription());
  }

  @Test
  void getCountryCode() {
    assertEquals(COUNTRY_CODE, testClass.getCountryCode());
  }

  @Test
  void getLangCode() {
    assertEquals(LANGUAGE_CODE, testClass.getLangCode());
  }

  @Test
  void getFilterEndpoint() {
    this.testClass.init();
    assertEquals(FILTER_END_POINT, testClass.getFilterEndpoint());
  }

  @Test
  void getSuggestedEndpoint() {
    this.testClass.init();
    assertEquals(LISTING_END_POINT, testClass.getListingEndpoint());
  }

  @Test
  void getPagination() {
    this.testClass.init();
    assertEquals(8, testClass.getPagination());
  }
}
