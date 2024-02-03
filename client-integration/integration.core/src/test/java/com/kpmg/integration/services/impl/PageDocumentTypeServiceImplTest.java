package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class PageDocumentTypeServiceImplTest {

  private final AemContext context = new AemContext();

  @Test
  public void testWithoutConfig() {
    final DocumentTypeServiceImpl templateTypeService =
        context.registerInjectActivateService(new DocumentTypeServiceImpl());
    assertNull(templateTypeService.getDocumentType("any"));
  }

  @Test
  public void testWithConfig() {
    String[] templateArray = {"/conf/kpmg/settings/wcm/templates/page-content"};
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getDocumentType", "Generic");
    properties.put("getTemplates", templateArray);
    final TemplateMappingServiceImpl templateMappingService =
        context.registerInjectActivateService(new TemplateMappingServiceImpl(), properties);
    DocumentTypeServiceImpl templateTypeService =
        context.registerInjectActivateService(new DocumentTypeServiceImpl());
    assertEquals(
        "Generic",
        templateTypeService.getDocumentType("/conf/kpmg/settings/wcm/templates/page-content"));
    assertTrue(
        templateTypeService.isValidTemplate("/conf/kpmg/settings/wcm/templates/page-content"));
    templateTypeService.bindTemplateMappingService(templateMappingService);
    templateTypeService.unbindTemplateMappingService(templateMappingService);
  }
}
