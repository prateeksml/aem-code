package com.kpmg.core.models.impl;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.kpmg.core.models.NavigationItem;
import com.kpmg.core.utils.ResourceUtil;
import java.util.Optional;
import org.apache.sling.api.resource.Resource;

abstract class AbstractNavigationItem extends AbstractKPMGComponentImpl implements NavigationItem {

  @Override
  public String getTitle() {
    return null;
  }

  public String getMainNavigationParentId() {
    Resource navParentResource =
        ResourceUtil.findResourceWithResourceType(
            resource, HeaderNavigationImpl.RESOURCE_TYPE, ResourceUtil::isPageResource);
    return ComponentUtils.getId(navParentResource, getCurrentPage(), null, componentContext);
  }

  @Override
  protected ComponentData getComponentData() {
    return DataLayerBuilder.extending(super.getComponentData())
        .asComponent()
        .withId(() -> ComponentUtils.generateId("mainnavigation-link", resource.getPath()))
        .withParentId(this::getMainNavigationParentId)
        .withTitle(this::getTitle)
        .withLinkUrl(() -> Optional.ofNullable(this.getLink()).map(Link::getMappedURL).orElse(null))
        .build();
  }
}
