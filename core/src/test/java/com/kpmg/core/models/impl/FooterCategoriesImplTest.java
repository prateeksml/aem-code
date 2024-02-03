package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.kpmg.core.models.FooterCategories;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FooterCategoriesImplTest {
  private final AemContext context = AppAemContext.newAemContext();

  private FooterCategories footerCategories;

  @BeforeEach
  void setUp() {
    footerCategories = FooterTestHelpers.setup(context, "/categories", FooterCategories.class);
  }

  @Test
  void testGetExportedType() {
    assertEquals(FooterCategoriesImpl.RESOURCE_TYPE, footerCategories.getExportedType());
  }

  @Test
  void testGetItems() {
    assertEquals(3, footerCategories.getItems().size());
  }
}
