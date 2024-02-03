package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ArticleDocumentImplTest {

  private final AemContext ctx = new AemContext();

  private ArticleDocumentImpl articleDocumentImpl;

  @BeforeEach
  void setUp() {
    ctx.addModelsForClasses(ArticleDocumentImpl.class);
    ctx.load().json("src/test/resources/model-resources/article.json", "/content/kpmg");
    ctx.currentPage("/content/kpmg");
    articleDocumentImpl = ctx.currentPage().getContentResource().adaptTo(ArticleDocumentImpl.class);
  }

  @Test
  void testGetters() {
    assertNotNull(articleDocumentImpl.getArticleDate());
    assertEquals("audio", articleDocumentImpl.getArticlePrimaryFormat());
    assertNull(articleDocumentImpl.getArticleContributors());
    assertEquals("INSIGHT", articleDocumentImpl.getDocumentType());
    assertEquals("audit", articleDocumentImpl.getArticleType());
    assertNotNull(articleDocumentImpl.getFilterDate());
    assertEquals(2023, articleDocumentImpl.getFilterYear());
    assertNotNull(articleDocumentImpl.getInjectedArticleDate());
  }
}
