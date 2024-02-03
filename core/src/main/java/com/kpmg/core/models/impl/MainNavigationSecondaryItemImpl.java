package com.kpmg.core.models.impl;

import com.adobe.acs.commons.models.injectors.annotation.ChildResourceFromRequest;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.commons.jcr.JcrConstants;
import com.kpmg.core.models.MainNavigationSecondaryItem;
import com.kpmg.core.models.MainNavigationTertiaryItem;
import com.kpmg.core.utils.CollectionsUtil;
import java.util.List;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {MainNavigationSecondaryItem.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Log4j
public class MainNavigationSecondaryItemImpl extends AbstractNavigationItem
    implements MainNavigationSecondaryItem {

  @ValueMapValue(name = JcrConstants.JCR_TITLE)
  @Getter
  String title;

  @ChildResourceFromRequest List<MainNavigationTertiaryItem> items;

  @Self protected LinkManager linkManager;
  @Self protected SlingHttpServletRequest request;

  @Override
  public Link getLink() {
    return linkManager.get(request.getResource()).build();
  }

  @Override
  public List<MainNavigationTertiaryItem> getItems() {
    return CollectionsUtil.unmodifiableListOrEmpty(items);
  }
}
