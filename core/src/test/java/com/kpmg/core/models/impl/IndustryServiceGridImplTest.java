package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableMap;
import com.kpmg.core.models.GridItem;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class IndustryServiceGridImplTest {

  private final AemContext context = AppAemContext.newAemContext();

  private IndustryServiceGridImpl industryServiceGrid;

  public static final String INDUSTRY_SERVICE_GRID_COMPONENT_PATH =
      "/content/kpmgpublic/us/en/service-landing/jcr:content/root/section_235815053/industryservicegrid";
  public static final String INDUSTRY_SERVICE_GRID_COMPONENT_WITH_AUTOMATED_DATA_JSON =
      "/IndustryServiceGridComponentAutomated.json";
  public static final String INDUSTRY_SERVICE_GRID_COMPONENT_WITH_MANUAL_DATA_JSON =
      "/IndustryServiceGridComponentManual.json";

  public static final String LINKED_PAGE_PATH =
      "/content/kpmgpublic/language-masters/en/adobe_qa_testing/articaltemp";

  @BeforeEach
  void setUp() {
    createPage(context, LINKED_PAGE_PATH);
  }

  @Test
  void testGetItemsWhenAutomatedDataEntrySelected() {
    createContextBasedOnDataEntry(INDUSTRY_SERVICE_GRID_COMPONENT_WITH_AUTOMATED_DATA_JSON);
    List<GridItem> items = industryServiceGrid.getItems();
    assertNotNull(items);
    assertEquals(6, items.size());
    assertEquals("Test Page", items.get(0).getTitle());
    assertNull(items.get(0).getDescription());
    assertEquals("materialIcon", items.get(0).getIconType());
    assertEquals("10k", items.get(0).getIcon());
    assertNull(items.get(0).getCustomIcon());
    assertEquals(
        "/content/kpmgpublic/language-masters/en/adobe_qa_testing/articaltemp",
        items.get(0).getLink());
    assertEquals("automated", industryServiceGrid.getDataEntryType());
    assertEquals("Read More", industryServiceGrid.getReadMore());
    assertEquals("true", industryServiceGrid.getExpandView());
    assertEquals("See All", industryServiceGrid.getSeeAll());
    assertEquals("See Less", industryServiceGrid.getSeeLess());
    assertEquals(6, industryServiceGrid.getAutoMatedItems().size());
    assertEquals(3, industryServiceGrid.getManualItems().size());
  }

  @Test
  void testGetItemsWhenManualDataEntrySelected() {
    createContextBasedOnDataEntry(INDUSTRY_SERVICE_GRID_COMPONENT_WITH_MANUAL_DATA_JSON);
    List<GridItem> items = industryServiceGrid.getItems();
    assertNotNull(items);
    assertEquals(3, items.size());
    assertEquals("Title 1", items.get(0).getTitle());
  }

  private static Page createPage(AemContext context, String path) {
    return context
        .create()
        .page(
            path, "", ImmutableMap.<String, Object>builder().put("jcr:title", "Test Page").build());
  }

  private void createContextBasedOnDataEntry(String jsonFile) {
    context.load().json(jsonFile, INDUSTRY_SERVICE_GRID_COMPONENT_PATH);
    context.currentResource(INDUSTRY_SERVICE_GRID_COMPONENT_PATH);
    industryServiceGrid = context.request().adaptTo(IndustryServiceGridImpl.class);
  }
}
