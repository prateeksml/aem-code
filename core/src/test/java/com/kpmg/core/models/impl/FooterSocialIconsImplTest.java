package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kpmg.core.models.FooterSocialIcons;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FooterSocialIconsImplTest {
  private final AemContext context = AppAemContext.newAemContext();
  private FooterSocialIcons footerSocialIcons;

  @BeforeEach
  void setUp() {
    footerSocialIcons = FooterTestHelpers.setup(context, "/social-icons", FooterSocialIcons.class);
  }

  @Test
  void testGetIconLinkItems() {
    assertNotNull(footerSocialIcons.getIconLinkItems().get(0));
  }

  @Test
  void testConstructor() {
    assertEquals(FooterSocialIconsImpl.RESOURCE_TYPE, footerSocialIcons.getExportedType());
  }
}
