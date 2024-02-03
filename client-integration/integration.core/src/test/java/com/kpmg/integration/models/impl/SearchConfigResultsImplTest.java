package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.kpmg.integration.models.SearchResults;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class SearchConfigResultsImplTest {
  private final AemContext ctx = new AemContext();
  private SearchResults searchResults;

  @BeforeEach
  void setUp() {
    ctx.load()
        .json("src/test/resources/model-resources/search_results.json", "/content/search-results");
    ctx.currentResource("/content/search-results");
    searchResults = ctx.request().adaptTo(SearchResults.class);
  }

  @Test
  void testGetters() {
    assertEquals("Results", searchResults.getMResultsCountMessage());
    assertEquals("Desktop Count", searchResults.getResultsCountMessage());
    assertEquals("Upcoming", searchResults.getUpcomingLabel());
    assertEquals("Past Event", searchResults.getPastEventLabel());
    assertEquals("No Results", searchResults.getNoResultsMessage());
    assertNotNull(searchResults.getSystemErrorMessage());
    assertNotNull(searchResults.getCancelButtonText());
    assertEquals(SearchResultsImpl.RESOURCE_TYPE, searchResults.getExportedType());
    assertEquals("All", searchResults.getFilterList().get(0).getFilterLabel());
    assertEquals("all", searchResults.getFilterList().get(0).getFilterType());
  }
}
