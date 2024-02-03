package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.kpmg.core.models.FooterCategory;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FooterCategoryImplTest {

  private final AemContext aemContext = AppAemContext.newAemContext();

  private FooterCategory category;

  @BeforeEach
  void setUp() {
    category =
        FooterTestHelpers.setup(aemContext, "/categories/items/category1", FooterCategory.class);
  }

  @Test
  void testGetExportedType() {
    assertNull(category.getExportedType());
  }

  @Test
  void testGetItems() {
    assertNotNull(category.getLinkItems().get(0));
  }

  @Test
  void testGetTitle() {
    assertNotNull("Contact", category.getTitle());
  }
}
