package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SocialShareModelImplTest {
  private Resource resource;

  private static final String TEST_DATA = "/content/test";
  private SocialShareModelImpl socialShareImpl;

  @Mock private SiteSettingsConfig socialShareConfig;

  @Mock private ConfigurationBuilder configurationBuilder;

  @Mock private ResourceResolver resourceResolver;

  @Mock private PageManager pageManager;

  @Mock private Page currentPage;

  @Mock private Externalizer externalizer;
  @Mock private Resource res;
  @Mock private ValueMap map;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    when(resourceResolver.getResource(anyString())).thenReturn(mock(Resource.class));
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(resourceResolver.adaptTo(Externalizer.class)).thenReturn(externalizer);
    when(externalizer.publishLink(resourceResolver, Externalizer.PUBLISH, "test"))
        .thenReturn("test");
    when(currentPage.getPath()).thenReturn(TEST_DATA);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(resourceResolver.getResource(TEST_DATA)).thenReturn(res);
    when(pageManager.getPage(anyString())).thenReturn(currentPage);
    when(currentPage.getProperties()).thenReturn(map);
    when(map.get("metaOgTitle", "")).thenReturn("test");
    when(currentPage.getPath()).thenReturn("/content/kpmgpublic/country/language");
    socialShareImpl = new SocialShareModelImpl();
    socialShareImpl.resourceResolver = resourceResolver;
    socialShareImpl.currentPage = currentPage;
    socialShareImpl.postConstruct();
  }

  @Test
  void testConstructor() {
    SocialShareModelImpl actualSocialShareModelImpl = new SocialShareModelImpl();
    assertNull(actualSocialShareModelImpl.getMetaOgTitle());
    assertNull(actualSocialShareModelImpl.getPageUrl());
    assertFalse(actualSocialShareModelImpl.isFacebookEnabled());
    assertFalse(actualSocialShareModelImpl.isLinkedinEnabled());
    assertFalse(actualSocialShareModelImpl.isSocialMediaEnabled());
    assertFalse(actualSocialShareModelImpl.isTwitterEnabled());
  }

  @Test
  void testGetContextAwareConfig_WithNullContentResource() {
    when(resourceResolver.getResource(anyString())).thenReturn(null);
    SiteSettingsConfig result = socialShareImpl.getContextAwareConfig();
    assertNull(result);
  }

  @Test
  void testGetContextAwareConfig_WithNonNullContentResourceAndConfigurationBuilder() {
    Resource contentResource = mock(Resource.class);
    ConfigurationBuilder configurationBuilder = mock(ConfigurationBuilder.class);
    SiteSettingsConfig socialShareConfig = mock(SiteSettingsConfig.class);
    when(resourceResolver.getResource(anyString())).thenReturn(contentResource);
    when(contentResource.adaptTo(ConfigurationBuilder.class)).thenReturn(configurationBuilder);
    when(configurationBuilder.as(SiteSettingsConfig.class)).thenReturn(socialShareConfig);

    SiteSettingsConfig result = socialShareImpl.getContextAwareConfig();
    assertEquals(socialShareConfig, result);
  }

  @Test
  void testIsLinkedinEnabled() {
    assertEquals(false, socialShareImpl.isLinkedinEnabled());
  }

  @Test
  void testIsTwitterEnabled() {
    assertEquals(false, socialShareImpl.isTwitterEnabled());
  }

  @Test
  void testIsFacebookEnabled() {
    assertEquals(false, socialShareImpl.isFacebookEnabled());
  }
}
