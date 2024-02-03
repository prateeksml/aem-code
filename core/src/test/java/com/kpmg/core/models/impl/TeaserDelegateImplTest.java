package com.kpmg.core.models.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkBuilder;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.day.cq.wcm.api.Page;
import java.util.ArrayList;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TeaserDelegateImplTest {

  @Mock private Teaser mockedTeaser;

  @Mock private LinkManager linkManager;

  @Mock private Resource resource;

  @Mock private LinkBuilder linkBuilder;

  @Mock private Link link;

  @Mock private Page page;

  @Mock private ResourceResolver resolver;
  @Mock private List<ListItem> items;
  @Mock private ValueMap props;
  @Mock private ListItem listItem;

  private TeaserDelegateImpl teaserDelegate;

  private List<TeaserAction> actions;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    when(linkManager.get(any(Resource.class))).thenReturn(linkBuilder);
    when(linkBuilder.withLinkUrlPropertyName(anyString())).thenReturn(linkBuilder);
    when(linkBuilder.build()).thenReturn(link);
    actions = new ArrayList<>();
    actions.add(new TeaserAction());
    actions.add(new TeaserAction());
    teaserDelegate = new TeaserDelegateImpl();
    teaserDelegate.teaser = mockedTeaser;
    teaserDelegate.actions = actions;
    teaserDelegate.linkManager = linkManager;
    teaserDelegate.resource = resource;
    teaserDelegate.currentPage = page;
    teaserDelegate.resolver = resolver;
  }

  @Test
  public void testGetOverridenActions() {
    List<TeaserAction> overriddenActions = teaserDelegate.getOverridenActions();
    Assertions.assertEquals(actions, overriddenActions);
  }

  @Test
  public void testGetAccessibilityLabel() {
    TeaserAction teaserAction = new TeaserAction();
    teaserAction.accessibilityLabel = "Test Accessibility Label";
    Assertions.assertEquals("Test Accessibility Label", teaserAction.getAccessibilityLabel());
  }

  @Test
  public void testGetLink() {
    when(mockedTeaser.getLink()).thenReturn(link);
    when(link.isValid()).thenReturn(true);
    Assertions.assertEquals(link, teaserDelegate.getLink());
  }

  @Test
  void testGetImage() {
    when(mockedTeaser.getActions()).thenReturn(items);
    when(items.get(0)).thenReturn(listItem);
    when(listItem.getLink()).thenReturn(link);
    when(link.getURL()).thenReturn("/content/kpmgpublic/language-masters/en/test.html");
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getProperties()).thenReturn(props);
    when(props.get(anyString())).thenReturn("/content/dam/kpmg/test.jpg");
    Assertions.assertEquals("/content/dam/kpmg/test.jpg", teaserDelegate.getFeaturedImagePath());
  }
}
