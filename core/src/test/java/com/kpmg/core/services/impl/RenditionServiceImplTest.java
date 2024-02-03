package com.kpmg.core.services.impl;

import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.day.cq.dam.api.DamConstants.ACTIVITY_TYPE_METADATA;
import static com.day.cq.dam.api.DamConstants.DC_FORMAT;
import static com.day.cq.dam.api.DamConstants.THUMBNAIL_MIMETYPE;
import static com.day.cq.dam.scene7.api.S7ConfigResolver.PREVIEW_SERVER;
import static com.day.cq.dam.scene7.api.constants.Scene7Constants.PN_S7_DOMAIN;
import static com.day.cq.dam.scene7.api.constants.Scene7Constants.PN_S7_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.asset.api.Asset;
import com.kpmg.core.constants.GlobalConstants;
import com.kpmg.core.constants.RenditionSize;
import com.kpmg.core.constants.Viewport;
import com.kpmg.core.services.RenditionServiceConfig;
import com.kpmg.core.services.RunmodeConfig;
import com.kpmg.core.services.RunmodeService;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

class RenditionServiceImplTest {

  private static final String IMAGE = "image";
  private static final String COMPONENT_NAME = "componentName";
  private static final String RENDITIONS = "renditions";
  private static final String IMAGE_PATH = "/content/dam/kpmgsites/files/white-flowers.jpg";
  private static final String SCENE7_IMAGE_PATH = "kpmg/white-flowers.jpg";
  public static final String IMAGE_PREVIEW_URL = "https://preview1.assetsadobe.com";
  public static final String IMAGE_SERVER_URL = "https://assets.kpmg.com/";

  private final String[] renditions = {
    "small;;crop;390;390", "medium;;crop;390;390", ";;570", ";;", "small;;crop;gg;390"
  };

  private final String[] runmodes = {"auhtor", "dev"};

  @InjectMocks private RenditionServiceImpl renditionServiceImpl;

  @Mock private ConfigurationAdmin configurationAdmin;

  @Mock private Dictionary<String, Object> configurationProperties;

  @Mock private RenditionServiceConfig renditionServiceConfig;

  @Mock private RunmodeConfig runmodeConfig;

  @Mock private RunmodeService runmodeService;

  @Mock private ResourceResolverFactory resourceResolverFactory;

  private ResourceResolver resourceResolver;

  @Mock private Asset asset;

  @Mock private Resource resource;

  @Mock private Resource imageResource;

  @Mock private RenditionSize renditionSize;

  @Mock private ValueMap valueMap;

  private AutoCloseable autoCloseable;

  @BeforeEach
  public void setup() throws IOException, InvalidSyntaxException, LoginException {
    resourceResolver = mock(ResourceResolver.class);
    this.autoCloseable = MockitoAnnotations.openMocks(this);

    final Configuration[] configurations = {mock(Configuration.class)};

    when(this.configurationAdmin.listConfigurations(anyString())).thenReturn(configurations);
    when(configurations[0].getProperties()).thenReturn(this.configurationProperties);
    when(configurationProperties.get(COMPONENT_NAME)).thenReturn(IMAGE);
    when(configurationProperties.get(RENDITIONS)).thenReturn(this.renditions);
    when(this.renditionServiceConfig.componentName()).thenReturn(IMAGE);
    when(this.renditionServiceConfig.renditions()).thenReturn(this.renditions);
    when(this.runmodeConfig.runmodes()).thenReturn(this.runmodes);
    when(this.runmodeService.isAuthor()).thenReturn(true);

    when(this.resourceResolverFactory.getServiceResourceResolver(anyMap()))
        .thenReturn(this.resourceResolver);

    this.renditionServiceImpl.activate(this.renditionServiceConfig);
  }

  @AfterEach
  public void closeMocks() throws Exception {
    this.autoCloseable.close();
  }

  @Test
  void testForNullRendition() {

    final Map<String, String> imageRenditions =
        this.renditionServiceImpl.getRenditions(IMAGE, IMAGE_PATH, null, this.resourceResolver);

    assertNotNull(imageRenditions, "{}");
  }

  @Test
  void testGetDynamicRenditions() {

    this.mockImageRenditionObjects();
    when(this.valueMap.get(PN_S7_DOMAIN, String.class)).thenReturn(IMAGE_SERVER_URL);
    when(this.valueMap.get(PN_S7_FILE, String.class)).thenReturn(SCENE7_IMAGE_PATH);
    when(this.valueMap.get(DC_FORMAT, String.class)).thenReturn(THUMBNAIL_MIMETYPE);
    final Map<String, String> imageRenditions =
        this.renditionServiceImpl.getRenditions(IMAGE, IMAGE_PATH, null, this.resourceResolver);

    assertNotNull(imageRenditions);
    assertEquals(9, imageRenditions.size());
  }

  @Test
  void testGetDynamicRenditionsForPreviewDomain() {

    this.mockImageRenditionObjects();
    when(this.valueMap.get(PN_S7_DOMAIN, String.class)).thenReturn(IMAGE_SERVER_URL);
    when(this.valueMap.get(PN_S7_FILE, String.class)).thenReturn(SCENE7_IMAGE_PATH);
    when(this.resourceResolver.getResource(GlobalConstants.DM_CONFIG_PATH))
        .thenReturn(this.resource);
    when(this.resource.getValueMap()).thenReturn(this.valueMap);
    when(this.valueMap.get(PREVIEW_SERVER, String.class)).thenReturn(IMAGE_PREVIEW_URL);
    final Map<String, String> imageRenditions =
        this.renditionServiceImpl.getRenditions(IMAGE, IMAGE_PATH, null, resourceResolver);

    assertNotNull(imageRenditions);
    assertEquals(9, imageRenditions.size());
  }

  @Test
  void testGetDynamicRenditionsForPublish() {

    this.mockImageRenditionObjects();
    when(this.valueMap.get(PN_S7_DOMAIN, String.class)).thenReturn(IMAGE_SERVER_URL);
    when(this.valueMap.get(PN_S7_FILE, String.class)).thenReturn(SCENE7_IMAGE_PATH);
    when(this.valueMap.containsKey(PN_S7_DOMAIN)).thenReturn(true);
    final Map<String, String> imageRenditions =
        this.renditionServiceImpl.getRenditions(IMAGE, IMAGE_PATH, null, resourceResolver);

    assertNotNull(imageRenditions);
    assertEquals(9, imageRenditions.size());
  }

  @Test
  void testGetRenditionsForNullAsset() {

    when(this.resourceResolver.getResource(IMAGE_PATH)).thenReturn(this.resource);
    when(this.resource.adaptTo(Asset.class)).thenReturn(null);
    when(this.asset.getPath()).thenReturn(IMAGE_PATH);
    final Map<String, String> imageRenditions =
        this.renditionServiceImpl.getRenditions(IMAGE, IMAGE_PATH, null, resourceResolver);

    assertNotNull(imageRenditions);
  }

  @Test
  void testGetRenditionsForStaticRenditions() {

    when(this.resourceResolver.getResource(IMAGE_PATH)).thenReturn(this.resource);
    when(this.resource.adaptTo(Asset.class)).thenReturn(this.asset);
    when(this.asset.getPath()).thenReturn(IMAGE_PATH);
    final Map<String, String> imageRenditions =
        this.renditionServiceImpl.getRenditions(IMAGE, IMAGE_PATH, null, resourceResolver);

    assertEquals(IMAGE_PATH, imageRenditions.get(Viewport.DESKTOP_M.getLabel()));
    assertEquals(9, imageRenditions.size());
  }

  private void mockImageRenditionObjects() {

    when(this.resourceResolver.getResource(IMAGE_PATH)).thenReturn(this.imageResource);
    when(this.imageResource.adaptTo(Asset.class)).thenReturn(this.asset);
    when(this.asset.getChild(JCR_CONTENT + "/" + ACTIVITY_TYPE_METADATA)).thenReturn(this.resource);
    when(ResourceUtil.getValueMap(this.resource)).thenReturn(this.valueMap);

    when(this.renditionSize.getWidth()).thenReturn(390);
    when(this.renditionSize.getHeight()).thenReturn(390);
  }

  @Test
  void testForNullConfig() throws IOException, InvalidSyntaxException {
    when(this.configurationAdmin.listConfigurations(anyString())).thenReturn(null);
    this.renditionServiceImpl.activate(this.renditionServiceConfig);
    final Map<String, String> imageRenditions =
        this.renditionServiceImpl.getRenditions(IMAGE, IMAGE_PATH, null, this.resourceResolver);
    assertNotNull(imageRenditions, "{}");
  }
}
