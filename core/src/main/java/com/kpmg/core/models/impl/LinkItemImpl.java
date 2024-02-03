package com.kpmg.core.models.impl;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.wcm.api.components.Component;
import com.kpmg.core.models.LinkItem;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class LinkItemImpl extends AbstractListItemImpl implements LinkItem {

  public final String PN_LINK_TITLE = "linkTitle";
  public final String PN_ALT_TEXT = "altText";

  @Getter String linkTitle;
  @Getter Link link;

  public LinkItemImpl(
      String parentId, final Resource itemResource, Component component, LinkManager linkManager) {
    super("footer", itemResource, component);
    ValueMap properties = itemResource.getValueMap();
    linkTitle = properties.get(PN_LINK_TITLE, String.class);
    link = linkManager.get(itemResource).build();
  }
}
