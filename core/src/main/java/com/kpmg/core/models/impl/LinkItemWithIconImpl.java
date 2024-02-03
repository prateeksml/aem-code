package com.kpmg.core.models.impl;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.wcm.api.components.Component;
import com.kpmg.core.models.LinkItemWithIcon;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class LinkItemWithIconImpl extends LinkItemImpl implements LinkItemWithIcon {

  public final String PN_ICON = "icon";

  @Getter String icon;

  @Getter String altText;

  @Getter Link link;

  public LinkItemWithIconImpl(
      String parentId, final Resource itemResource, Component component, LinkManager linkManager) {
    super(parentId, itemResource, component, linkManager);
    ValueMap properties = itemResource.getValueMap();
    icon = properties.get(PN_ICON, String.class);
    altText = properties.get(PN_ALT_TEXT, String.class);
    link = linkManager.get(itemResource).build();
  }
}
