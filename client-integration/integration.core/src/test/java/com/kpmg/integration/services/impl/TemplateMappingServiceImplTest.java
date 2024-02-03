package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class TemplateMappingServiceImplTest {

  private final AemContext context = new AemContext();

  @Test
  public void testWithoutConfig() {
    final TemplateMappingServiceImpl templateMappingService =
        context.registerInjectActivateService(new TemplateMappingServiceImpl());
    assertNull(templateMappingService.getDocumentType());
  }

  @Test
  public void testWithConfig() {
    String[] templateArray = {"/conf/kpmg/settings/wcm/templates/page-content"};
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getDocumentType", "Generic");
    properties.put("getTemplates", templateArray);
    final TemplateMappingServiceImpl templateMappingService =
        context.registerInjectActivateService(new TemplateMappingServiceImpl(), properties);
    assertEquals("Generic", templateMappingService.getDocumentType());
    assertEquals(
        Arrays.asList(templateArray), Arrays.asList(templateMappingService.getTemplates()));
  }
}
