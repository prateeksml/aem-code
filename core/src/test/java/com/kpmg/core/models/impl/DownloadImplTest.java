package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.adobe.cq.wcm.core.components.models.Download;
import com.day.cq.commons.DownloadResource;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.api.Rendition;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
class DownloadImplTest {

  private final AemContext context = AppAemContext.newAemContext();

  @Mock private SlingHttpServletRequest request;

  private ValueMap properties = new ValueMapDecorator(new HashMap<>());

  @Mock private Asset asset;

  @Mock private Rendition rendition;

  @Mock private ResourceResolver resolver;

  @Mock private Resource resource;

  @Test
  void getThumbnailTest() {
    String assetPath = "/content/dam/kpmgsites/abc.pdf";
    String expectedRenditionPath =
        "/content/dam/kpmgsites/abc/renditions/cq5dam.thumbnail.140.100.png";
    DownloadImpl download = new DownloadImpl();
    properties.put(DownloadResource.PN_REFERENCE, assetPath);
    download.properties = properties;
    download.request = request;
    doReturn(DamConstants.NT_DAM_ASSET).when(resource).getResourceType();
    doReturn(resolver).when(request).getResourceResolver();
    doReturn(resource).when(resolver).getResource(assetPath);
    doReturn(asset).when(resource).adaptTo(Asset.class);
    doReturn(rendition).when(asset).getRendition(DownloadImpl.THUMBNAIL_RENDITION);
    doReturn(expectedRenditionPath).when(rendition).getPath();

    assertEquals(expectedRenditionPath, download.getThumbnail());
  }

  @Test
  void getComponentData() {
    // test for non-null

    String path = "/content/kpmg/us/en";
    context.build().resource(path, "sling:resourceType", DownloadImpl.RESOURCE_TYPE).commit();
    Resource resource = spy(context.resourceResolver().getResource(path));
    context.currentResource(resource);
    Download download = context.request().adaptTo(Download.class);
    DownloadImpl downloadImpl = (DownloadImpl) spy(download);
    doReturn("123").when(downloadImpl).getId();

    assertNull(downloadImpl.getComponentData().getJson());
  }

  @Test
  void getLinkData() {
    // test for null
    DownloadImpl download = new DownloadImpl();
    download.download = mock(Download.class);
    doReturn(null).when(download.download).getUrl();
    assertNull(download.getLinkData());

    // test for non-null
    download = new DownloadImpl();
    download.download = mock(Download.class);
    // mock getId
    doReturn("123").when(download.download).getId();
    doReturn("https://www.kpmg.com").when(download.download).getUrl();
    assertEquals("https://www.kpmg.com", download.getLinkData().getLinkUrl());
    // assert that getLinkData has json
    assertEquals(
        "{\"123-d541694b6d\":{\"parentId\":\"123\",\"@type\":\"kpmg/components/content/download/link\",\"xdm:linkURL\":\"https://www.kpmg.com\"}}",
        download.getLinkData().getJson());
  }
}
