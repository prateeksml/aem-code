package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.acs.commons.synth.children.impl.JSONModifiableValueMapDecorator;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.internal.link.LinkBuilderImpl;
import com.adobe.cq.wcm.core.components.internal.link.LinkManagerImpl;
import com.day.cq.wcm.foundation.model.responsivegrid.ResponsiveGrid;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.wrappers.ResourceResolverWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
class LinkItemWithIconImplTest {
  private Resource itemResource;
  private ResourceResolver resolver;
  private SlingHttpServletRequest request;
  private LinkManagerImpl linkManager;
  private LinkItemImpl actualLinkItemImpl;
  private Link link;
  private Link expectedLink;

  @BeforeEach
  void setUp() {
    itemResource = mock(Resource.class);
    when(itemResource.getPath()).thenReturn("Path");
    when(itemResource.getValueMap()).thenReturn(new JSONModifiableValueMapDecorator());
    resolver = mock(ResourceResolver.class);
    when(resolver.getResource(Mockito.<String>any())).thenReturn(new ResponsiveGrid());
    request = mock(SlingHttpServletRequest.class);
    when(request.getResourceResolver()).thenReturn(new ResourceResolverWrapper(resolver));
    linkManager = mock(LinkManagerImpl.class);
    when(linkManager.get(Mockito.<Resource>any()))
        .thenReturn(new LinkBuilderImpl(null, request, new ArrayList<>()));
  }

  @Test
  void test() {
    actualLinkItemImpl = new LinkItemImpl("id", itemResource, null, linkManager);
    assertEquals("linkTitle", actualLinkItemImpl.PN_LINK_TITLE);
    assertEquals("altText", actualLinkItemImpl.PN_ALT_TEXT);
    assertNull(actualLinkItemImpl.getLinkTitle());
    expectedLink = actualLinkItemImpl.link;
    link = actualLinkItemImpl.getLink();
    assertSame(expectedLink, link);
  }
}
