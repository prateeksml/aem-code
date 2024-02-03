package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkBuilder;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.kpmg.core.models.MainNavigationSecondaryItem;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MainNavigationItemImplTest {

  @Mock LinkManager linkManager;
  @Mock LinkBuilder linkBuilder;
  @Mock Link link;
  @Mock SlingHttpServletRequest request;
  @Mock Resource resource;

  @Test
  void testAll() {
    doReturn(linkBuilder).when(linkManager).get(any(Resource.class));
    doReturn(link).when(linkBuilder).build();
    doReturn(resource).when(request).getResource();

    MainNavigationItemImpl mainNavigationItem = new MainNavigationItemImpl();
    mainNavigationItem.linkManager = linkManager;
    mainNavigationItem.request = request;

    List<MainNavigationSecondaryItem> expectedItems =
        List.of(mock(MainNavigationSecondaryItemImpl.class));
    mainNavigationItem.items = expectedItems;
    assertEquals(expectedItems, mainNavigationItem.getItems());

    assertEquals(link, mainNavigationItem.getLink());

    mainNavigationItem.type = MainNavigationItemImpl.OPTION_DIRECT;
    assertTrue(mainNavigationItem.isDirect());

    mainNavigationItem.type = MainNavigationItemImpl.OPTION_SECONDARY;
    assertTrue(mainNavigationItem.isSecondary());

    mainNavigationItem.type = MainNavigationItemImpl.OPTION_TERTIARY;
    assertTrue(mainNavigationItem.isTertiary());
  }
}
