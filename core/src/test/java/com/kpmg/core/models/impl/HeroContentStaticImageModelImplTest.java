package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HeroContentStaticImageModelImplTest {

  @Mock private ResourceResolver resourceResolver;
  @Mock private Resource resource;
  @Mock private Page page;
  @Mock private PageManager pm;
  @Mock private Template template;
  @Mock private ModifiableValueMap valueMap;

  HeroContentStaticImageModelImpl hero = new HeroContentStaticImageModelImpl();

  String path = "/content/kpmgpublic/us/en/test-article";
  String articleTimeAndDateProperty = "articleTimeAndDate";
  Date date = new Date();
  String currentDate = new SimpleDateFormat("dd MMM, yyyy").format(date);
  String tempalate = "/conf/kpmg/settings/wcm/templates/page-article";
  public final String HERO_COMPONENT_PATH =
      "/content/kpmgpublic/us/en/hero-test/jcr:content/root/container/container/hero_csi";

  @BeforeEach
  void setUp() throws Exception {

    MockitoAnnotations.openMocks(this);

    when(resourceResolver.getResource(anyString())).thenReturn(mock(Resource.class));
    when(resourceResolver.getResource(path + "/" + "jcr:content")).thenReturn(resource);
    when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(valueMap);
    when(page.getProperties()).thenReturn(valueMap);

    when(valueMap.get(articleTimeAndDateProperty, Date.class)).thenReturn(date);
    when(page.getPath()).thenReturn(path);
    when(page.getTemplate()).thenReturn(template);
    when(template.getPath()).thenReturn("/conf/kpmg/settings/wcm/templates/page-article");

    hero.articlePublishedDate = new SimpleDateFormat("dd MMM, yyyy").format(date);
    hero.currentPage = page;
    hero.resourceResolver = resourceResolver;

    hero.postConstruct();
  }

  @Test
  void testArticlePublishedDate() throws Exception {
    String expectedFormattedDate = new SimpleDateFormat("dd MMM, yyyy").format(date);
    String actualFormattedDate = hero.getArticlePublishedDate();
    assertEquals(expectedFormattedDate, actualFormattedDate);
  }
}
