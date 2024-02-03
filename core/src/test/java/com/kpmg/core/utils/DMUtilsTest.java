package com.kpmg.core.utils;

import static com.day.cq.dam.scene7.api.S7ConfigResolver.PREVIEW_SERVER;
import static com.day.cq.dam.scene7.api.S7ConfigResolver.PUBLISH_SERVER;
import static com.day.cq.dam.scene7.api.S7ConfigResolver.ROOT_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.asset.api.Asset;
import com.kpmg.core.constants.GlobalConstants;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DMUtilsTest {

  private static String IMAGE_PATH = "/content/dam/kpmgsites/files/white-flowers.jpg";
  public static final String IMAGE_PREVIEW_URL = "https://preview1.assetsadobe.com";
  public static final String IMAGE_PUBLISH_URL = "https://assets.kpmg.com";
  public static final String IMAGE_ROOT_PATH = "rootPath";

  private ResourceResolver resourceResolver;

  @Mock private ResourceResolverFactory resourceResolverFactory;

  @Mock private Resource resource;

  @Mock private Asset asset;

  @Mock private ValueMap properties;

  @Test
  void testNoInstantiation() throws ReflectiveOperationException {

    final Constructor<DMUtils> constructor = DMUtils.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    constructor.newInstance();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
  }

  @BeforeEach
  void setUp() throws Exception {
    resourceResolver = mock(ResourceResolver.class);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetAssetForNullResource() {

    when(this.resourceResolver.getResource(IMAGE_PATH)).thenReturn(null);
    assertNull(DMUtils.getAsset(this.resourceResolver, IMAGE_PATH));
  }

  @Test
  void testPreviewUrl() throws LoginException {
    final ResourceResolver resourceResolver = this.getResourceResolver();
    final Resource resource = mock(Resource.class);
    final ValueMap valueMap = mock(ValueMap.class);

    when(resourceResolver.getResource(GlobalConstants.DM_CONFIG_PATH)).thenReturn(resource);
    when(resource.getValueMap()).thenReturn(valueMap);
    when(valueMap.get(PREVIEW_SERVER, String.class)).thenReturn(IMAGE_PREVIEW_URL);

    assertEquals(
        IMAGE_PREVIEW_URL,
        DMUtils.getDynamicMediaPreviewUrl(resourceResolverFactory),
        "Image Preview URL");

    when(valueMap.get(PUBLISH_SERVER, String.class)).thenReturn(IMAGE_PUBLISH_URL);
    assertEquals(
        IMAGE_PUBLISH_URL,
        DMUtils.getDynamicMediaPublishUrl(resourceResolverFactory),
        "Image Publish URL");

    when(valueMap.get(ROOT_PATH, String.class)).thenReturn(IMAGE_ROOT_PATH);
    assertEquals(
        IMAGE_ROOT_PATH,
        DMUtils.getDynamicMediaRootPath(resourceResolverFactory),
        "Image Root Path");
  }

  @Test
  void testEmptyPreviewUrl() throws LoginException {

    final ResourceResolver resourceResolver = this.getResourceResolver();
    when(resourceResolver.getResource(GlobalConstants.DM_CONFIG_PATH)).thenReturn(null);

    assertEquals(
        StringUtils.EMPTY,
        DMUtils.getDynamicMediaPreviewUrl(resourceResolverFactory),
        "Empty Image Preview URL");
  }

  private ResourceResolver getResourceResolver() throws LoginException {

    final Map<String, Object> authInfo = new HashMap<>();
    authInfo.put(ResourceResolverFactory.SUBSERVICE, "userpermissions");
    final ResourceResolver resourceResolver = mock(ResourceResolver.class);
    when(this.resourceResolverFactory.getServiceResourceResolver(authInfo))
        .thenReturn(resourceResolver);
    return resourceResolver;
  }

  @Test
  void testAppendSlash() {
    String expected = IMAGE_PUBLISH_URL.concat("/");
    assertEquals(expected, DMUtils.appendSlash(IMAGE_PUBLISH_URL), "Image Publish URL with slash");
  }
}
