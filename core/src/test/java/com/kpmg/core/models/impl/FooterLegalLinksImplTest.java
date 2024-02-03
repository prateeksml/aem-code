package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kpmg.core.models.FooterLegalLinks;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FooterLegalLinksImplTest {
  private final AemContext context = AppAemContext.newAemContext();

  private FooterLegalLinks footerLegalLinks;

  @BeforeEach
  void setUp() throws Exception {
    footerLegalLinks = FooterTestHelpers.setup(context, "/legal-links", FooterLegalLinks.class);
  }

  @Test
  void testGetLinkItems() throws Exception {
    assertNotNull(footerLegalLinks.getLinkItems().get(0));
  }

  @Test
  void testConstructor() {
    assertEquals(FooterLegalLinksImpl.RESOURCE_TYPE, footerLegalLinks.getExportedType());
  }
}
