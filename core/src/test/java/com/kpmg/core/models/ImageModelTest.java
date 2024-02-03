package com.kpmg.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.adobe.granite.asset.api.Asset;
import com.kpmg.core.services.RenditionService;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ImageModelTest {

  private static final String IMAGE_PATH = "/content/dam/kpmgsites/files/white-flowers.jpg";
  private static final String IMAGE_PATH_SVG = "/content/dam/kpmgsites/files/white-flowers.svg";
  private static final String IMAGE_ALT_TEXT = "White Flower";
  private static final String COMPONENT_NAME = "image";
  private static final String ASPECT_RATIO = "4:3";

  @InjectMocks private Image image;

  @Mock private ResourceResolver resourceResolver;

  @Mock private Resource resource;

  @Mock private RenditionService renditionService;

  @Mock private Asset asset;

  @SuppressWarnings("deprecation")
  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    PrivateAccessor.setField(this.image, "imagePath", IMAGE_PATH);
    PrivateAccessor.setField(this.image, "aspectRatio", ASPECT_RATIO);
    PrivateAccessor.setField(this.image, "componentName", COMPONENT_NAME);
    PrivateAccessor.setField(this.image, "imageAltText", IMAGE_ALT_TEXT);

    when(this.resourceResolver.getResource(IMAGE_PATH_SVG)).thenReturn(this.resource);
    when(this.resource.adaptTo(Asset.class)).thenReturn(this.asset);
  }

  @Test
  void testGetAltText() {

    assertEquals(IMAGE_ALT_TEXT, this.image.getAltText(), "Expects Value");
  }

  @Test
  void testGetRendition() {
    this.image.init();
    assertNotNull(this.image.getRenditions(), "Expects Non Null");
  }

  @Test
  void testGetRenditionsWithEmptyComponent() throws NoSuchFieldException {

    PrivateAccessor.setField(this.image, "componentName", null);

    this.image.init();

    assertNull(this.image.getRenditions(), "Expects Null");
  }

  @Test
  void testGetRenditionsWithEmptyImage() throws NoSuchFieldException {

    PrivateAccessor.setField(this.image, "imagePath", null);

    this.image.init();

    assertNull(this.image.getRenditions(), "Expects Null");
  }
}
