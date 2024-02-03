package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.adobe.cq.wcm.core.components.models.Page;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PageImplTest {

  @ParameterizedTest
  @CsvSource(
      value = {"null,ltr", "ltr,ltr", "rtl,rtl"},
      nullValues = {"null"})
  void testGetDirection(String direction, String expectedDirection) {
    PageImpl page = new PageImpl();
    page.direction = direction;
    assertEquals(expectedDirection, page.getDirection());
  }

  @Test
  void getData() {
    PageImpl page = new PageImpl();
    assertEquals(null, page.getData());

    ComponentData componentData = mock(ComponentData.class);
    page.componentData = componentData;
    assertEquals(componentData, page.getData());

    page.dataLayerEnabled = true;
    assertEquals(componentData, page.getData());
  }

  @Test
  void getComponentData() {
    ComponentData componentData = mock(ComponentData.class);
    Page delegate = mock(Page.class);
    when(delegate.getData()).thenReturn(componentData);
    PageImpl page = new PageImpl();

    page.delegate = delegate;
    page.componentData = componentData;

    assertNotNull(page.getComponentData());
  }
}
