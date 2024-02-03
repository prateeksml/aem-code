package com.kpmg.core.models.impl;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.wcm.api.components.Component;
import org.apache.sling.api.resource.Resource;

public class AbstractListItemImpl extends AbstractKPMGComponentImpl implements ListItem {

  /** Prefix prepended to the item ID. */
  private static final String ITEM_ID_PREFIX = "item";

  /** The ID of the component that contains this list item. */
  protected String parentId;

  /** The path of this list item. */
  protected String path;

  /** Data layer type. */
  protected String dataLayerType;

  /**
   * Construct a list item.
   *
   * @param parentId The ID of the containing component.
   * @param resource The resource of the list item.
   * @param component The component that contains this list item.
   */
  protected AbstractListItemImpl(String parentId, Resource resource, Component component) {
    this.parentId = parentId;
    if (resource != null) {
      this.path = resource.getPath();
    }
    if (component != null) {
      this.dataLayerType = component.getResourceType() + "/" + ITEM_ID_PREFIX;
    }
    this.resource = resource;
  }
}
