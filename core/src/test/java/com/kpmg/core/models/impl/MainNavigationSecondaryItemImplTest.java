package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkBuilder;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.kpmg.core.models.MainNavigationTertiaryItem;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MainNavigationSecondaryItemImplTest {

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

    MainNavigationSecondaryItemImpl mainNavigationSecondaryItem =
        new MainNavigationSecondaryItemImpl();
    mainNavigationSecondaryItem.linkManager = linkManager;
    mainNavigationSecondaryItem.request = request;

    List<MainNavigationTertiaryItem> expectedItems =
        List.of(mock(MainNavigationTertiaryItemImpl.class));
    mainNavigationSecondaryItem.items = expectedItems;
    assertEquals(expectedItems, mainNavigationSecondaryItem.getItems());

    assertEquals(link, mainNavigationSecondaryItem.getLink());

    mainNavigationSecondaryItem.title = "title";
    assertEquals("title", mainNavigationSecondaryItem.getTitle());
  }
}
