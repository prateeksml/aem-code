package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;
import com.kpmg.integration.services.DocumentTypeService;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class PageDocumentServiceImplTest {

  @Mock Resource resource;

  @Mock private Page page;

  @Mock Template template;

  @Mock DocumentTypeService documentTypeService;

  @InjectMocks DocumentModelServiceImpl documentModelService;

  private static final String TEMPLATE_PATH = "/conf/kpmg/settings/wcm/templates/page-content";

  @BeforeEach
  public void setUp() {
    when(page.getContentResource()).thenReturn(resource);
    when(page.getTemplate()).thenReturn(template);
    when(template.getPath()).thenReturn(TEMPLATE_PATH);
  }

  @Test
  public void testWithGeneric() {
    when(documentTypeService.getDocumentType(TEMPLATE_PATH)).thenReturn("Generic");
    documentModelService.getPageDocumentModel(page);
    assertNull(documentModelService.getPageDocumentModel(page));
  }

  @Test
  public void testWithEvent() {
    when(documentTypeService.getDocumentType(TEMPLATE_PATH)).thenReturn("Events");
    documentModelService.getPageDocumentModel(page);
    assertNull(documentModelService.getPageDocumentModel(page));
  }

  @Test
  public void testWithArticle() {
    when(documentTypeService.getDocumentType(TEMPLATE_PATH)).thenReturn("Article");
    documentModelService.getPageDocumentModel(page);
    assertNull(documentModelService.getPageDocumentModel(page));
  }

  @Test
  public void testWithoutTemplate() {
    documentModelService.getPageDocumentModel(page);
    assertNull(documentModelService.getPageDocumentModel(page));
  }
}
