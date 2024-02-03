package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkBuilder;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.core.caconfig.SiteSelectorConfig;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SiteSelectorImplTest {

  private SiteSelectorImpl siteSelectorImpl;

  @Mock private SiteSelectorConfig siteSelectorConfig;

  @Mock private ConfigurationBuilder configurationBuilder;

  @Mock private ResourceResolver resourceResolver;

  @Mock private PageManager pageManager;

  @Mock private Page currentPage;

  @Mock private LinkManager linkManager;
  @Mock private LinkBuilder linkBuilder;
  @Mock private Link link;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    when(resourceResolver.getResource(anyString())).thenReturn(mock(Resource.class));
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getPage(anyString())).thenReturn(currentPage);
    when(configurationBuilder.as(SiteSelectorConfig.class)).thenReturn(siteSelectorConfig);
    when(currentPage.getPath()).thenReturn("/content/kpmgpublic/country/language");

    siteSelectorImpl = new SiteSelectorImpl();
    siteSelectorImpl.resourceResolver = resourceResolver;
    siteSelectorImpl.currentPage = currentPage;
    siteSelectorImpl.linkManager = linkManager;
    siteSelectorImpl.postConstruct();
  }

  @Test
  void testGetContextAwareConfig_WithNullContentResource() {
    when(resourceResolver.getResource(anyString())).thenReturn(null);
    SiteSelectorConfig result = siteSelectorImpl.getContextAwareConfig();
    assertNull(result);
  }

  @Test
  void testGetContextAwareConfig_WithNonNullContentResourceAndConfigurationBuilder() {
    Resource contentResource = mock(Resource.class);
    ConfigurationBuilder configurationBuilder = mock(ConfigurationBuilder.class);
    SiteSelectorConfig siteSelectorConfig = mock(SiteSelectorConfig.class);
    when(resourceResolver.getResource(anyString())).thenReturn(contentResource);
    when(contentResource.adaptTo(ConfigurationBuilder.class)).thenReturn(configurationBuilder);
    when(configurationBuilder.as(SiteSelectorConfig.class)).thenReturn(siteSelectorConfig);
    SiteSelectorConfig result = siteSelectorImpl.getContextAwareConfig();
    assertEquals(siteSelectorConfig, result);
  }

  @Test
  void testGetSiteSelectorItems() {
    SiteSelectorConfig.SiteSelectorItemConfig itemConfig =
        mock(SiteSelectorConfig.SiteSelectorItemConfig.class);
    when(siteSelectorConfig.items())
        .thenReturn(new SiteSelectorConfig.SiteSelectorItemConfig[] {itemConfig});
    List<SiteSelectorBean> siteSelectorItems = siteSelectorImpl.getSiteSelectorItems();
    assertEquals(0, siteSelectorItems.size());
  }

  @Test
  void testGetRelevantCountryCode() {
    SiteSelectorConfig.SiteSelectorItemConfig itemConfig =
        mock(SiteSelectorConfig.SiteSelectorItemConfig.class);
    when(siteSelectorConfig.items())
        .thenReturn(new SiteSelectorConfig.SiteSelectorItemConfig[] {itemConfig});
    when(itemConfig.siteLink()).thenReturn("/content/kpmgpublic/country/language");
    List<SiteSelectorBean> relevantCountryCodes = siteSelectorImpl.getRelevantCountryCode();
    assertEquals(0, relevantCountryCodes.size());
  }

  @Test
  public void testGetCurrentLanguage() {
    String pagePath = "/content/kpmgpublic/country/language/page";
    String expectedLanguage = "LANGUAGE";
    when(currentPage.getPath()).thenReturn(pagePath);
    String language = siteSelectorImpl.getCurrentLanguage();
    assertEquals(expectedLanguage, language);
  }

  @Test
  void testCreateSiteSelectorBean() {
    SiteSelectorConfig.SiteSelectorItemConfig itemConfig =
        mock(SiteSelectorConfig.SiteSelectorItemConfig.class);
    when(itemConfig.country()).thenReturn("Country");
    when(itemConfig.locale()).thenReturn("en_US");
    when(itemConfig.siteLink()).thenReturn("/content/country");
    doReturn(linkBuilder).when(linkManager).get(anyString());
    doReturn(link).when(linkBuilder).build();
    doReturn("/content/country.html").when(link).getURL();
    SiteSelectorBean siteSelectorBean = siteSelectorImpl.createSiteSelectorBean(itemConfig);
    assertEquals("Country", siteSelectorBean.getCountry());
    assertEquals("EN_US", siteSelectorBean.getLocale());
    assertEquals("/content/country.html", siteSelectorBean.getSiteLink());
  }
}
