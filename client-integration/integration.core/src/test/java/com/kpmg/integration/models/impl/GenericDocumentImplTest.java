package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class GenericDocumentImplTest {

  private final AemContext ctx = new AemContext();

  private GenericDocumentImpl genericDocumentImplModel;

  @BeforeEach
  void setUp() {
    ctx.addModelsForClasses(GenericDocumentImpl.class);
    ctx.load().json("src/test/resources/model-resources/GenericPageJcr.json", "/content/kpmg");
    ctx.currentPage("/content/kpmg");
    genericDocumentImplModel =
        ctx.currentPage().getContentResource().adaptTo(GenericDocumentImpl.class);
  }

  @Test
  void testInit() {
    genericDocumentImplModel.init();
  }

  @Test
  void testGetters() {
    assertEquals("Content Page", genericDocumentImplModel.getTitle());
    assertEquals("This is description", genericDocumentImplModel.getDescription());
    assertEquals("28a8eee8-2427-4e2a-b693-14de7ca903a1", genericDocumentImplModel.getDocumentId());
    assertNotNull(genericDocumentImplModel.getFilterDate());
    assertEquals(2023, genericDocumentImplModel.getFilterYear());
    assertNotNull(genericDocumentImplModel.getQualifiedUrl());
    assertNull(genericDocumentImplModel.getImageUrl());
    assertNull(genericDocumentImplModel.getImageAltText());
    assertEquals("GENERIC", genericDocumentImplModel.getDocumentType());
    assertNull(genericDocumentImplModel.getBodyContent());
    assertNull(genericDocumentImplModel.getKeywordsList());
    assertNotNull(genericDocumentImplModel.getLastModified());
    assertNull(genericDocumentImplModel.getIndexedTime());
    assertNull(genericDocumentImplModel.getSlContentZthesIds());
    assertNull(genericDocumentImplModel.getSlContentQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlContentHierarchy());
    assertNull(genericDocumentImplModel.getSlContentHierarchyId());
    assertNull(genericDocumentImplModel.getSlContentQualifiedName());
    assertNull(genericDocumentImplModel.getSlMediaZthesIds());
    assertNull(genericDocumentImplModel.getSlMediaQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlMediaHierarchy());
    assertNull(genericDocumentImplModel.getSlMediaHierarchyId());
    assertNull(genericDocumentImplModel.getSlMediaQualifiedName());
    assertNull(genericDocumentImplModel.getSlPersonaZthesIds());
    assertNull(genericDocumentImplModel.getSlPersonaQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlPersonaHierarchy());
    assertNull(genericDocumentImplModel.getSlPersonaHierarchyId());
    assertNull(genericDocumentImplModel.getSlPersonaQualifiedName());
    assertNull(genericDocumentImplModel.getSlIndustryZthesIds());
    assertNull(genericDocumentImplModel.getSlIndustryQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlIndustryHierarchy());
    assertNull(genericDocumentImplModel.getSlIndustryHierarchyId());
    assertNull(genericDocumentImplModel.getSlIndustryQualifiedName());
    assertNull(genericDocumentImplModel.getSlServiceZthesIds());
    assertNull(genericDocumentImplModel.getSlServiceQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlServiceHierarchy());
    assertNull(genericDocumentImplModel.getSlServiceHierarchyId());
    assertNull(genericDocumentImplModel.getSlServiceQualifiedName());
    assertNull(genericDocumentImplModel.getSlMarketZthesIds());
    assertNull(genericDocumentImplModel.getSlMarketQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlMarketHierarchy());
    assertNull(genericDocumentImplModel.getSlMarketHierarchyId());
    assertNull(genericDocumentImplModel.getSlMarketQualifiedName());
    assertNull(genericDocumentImplModel.getSlInsightZthesIds());
    assertNull(genericDocumentImplModel.getSlInsightQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlInsightHierarchy());
    assertNull(genericDocumentImplModel.getSlInsightHierarchyId());
    assertNull(genericDocumentImplModel.getSlInsightQualifiedName());
    assertNull(genericDocumentImplModel.getSlGeographyZthesIds());
    assertNull(genericDocumentImplModel.getSlGeographyQualifiedNameId());
    assertNull(genericDocumentImplModel.getSlGeographyHierarchy());
    assertNull(genericDocumentImplModel.getSlGeographyHierarchyId());
    assertNull(genericDocumentImplModel.getSlGeographyQualifiedName());
    assertNull(genericDocumentImplModel.getSlGeographyIso3166());
    assertNull(genericDocumentImplModel.getSlGeographyIso31663());
    assertNull(genericDocumentImplModel.getSlGeographyIso31662());
    assertNull(genericDocumentImplModel.getSlGeographyUnm49Region());
    assertNull(genericDocumentImplModel.getSlGeographyUnm49SubRegion());
    assertNull(genericDocumentImplModel.getSlGeographyUnm49SubSubRegion());
    assertNull(genericDocumentImplModel.getSlInsightCommonZthesIds());
    assertNull(genericDocumentImplModel.getSlIndustryCommonZthesIds());
    assertNull(genericDocumentImplModel.getSlServiceCommonZthesIds());
    assertNull(genericDocumentImplModel.getSlInsightCommonHierarchy());
    assertNull(genericDocumentImplModel.getSlIndustryCommonHierarchy());
    assertNull(genericDocumentImplModel.getSlServiceCommonHierarchy());
    assertNull(genericDocumentImplModel.getFormattedFilterDate());
    assertNull(genericDocumentImplModel.getFormattedLastModified());
    assertNull(genericDocumentImplModel.getScene7Url());
  }
}
