package com.kpmg.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LinkUtilsTest {
  @Test
  void testGetLinkUrl() {
    assertEquals(
        "https://example.org/example", LinkUtils.getLinkUrl("https://example.org/example"));
    assertEquals("/content.html", LinkUtils.getLinkUrl("/content"));
    assertEquals(".html", LinkUtils.getLinkUrl(".html"));
    assertEquals("/content/dam", LinkUtils.getLinkUrl("/content/dam"));
    assertEquals("/content/", LinkUtils.getLinkUrl("/content/"));
    assertEquals("/content.html", LinkUtils.getLinkUrl("/content.html"));
  }
}
