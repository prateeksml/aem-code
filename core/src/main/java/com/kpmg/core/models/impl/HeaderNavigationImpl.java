package com.kpmg.core.models.impl;

import com.adobe.acs.commons.models.injectors.annotation.ChildResourceFromRequest;
import com.kpmg.core.models.HeaderNavigation;
import com.kpmg.core.models.MainNavigationItem;
import com.kpmg.core.utils.CollectionsUtil;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = HeaderNavigation.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderNavigationImpl extends AbstractKPMGComponentImpl implements HeaderNavigation {

  public static final String RESOURCE_TYPE = "kpmg/components/navigation/header/mainnavigation";
  @ChildResourceFromRequest MainNavigationItem nav1;
  @ChildResourceFromRequest MainNavigationItem nav2;
  @ChildResourceFromRequest MainNavigationItem nav3;
  @ChildResourceFromRequest MainNavigationItem nav4;
  @ChildResourceFromRequest MainNavigationItem nav5;
  @ChildResourceFromRequest MainNavigationItem nav6;

  @Override
  public List<MainNavigationItem> getItems() {
    return CollectionsUtil.unmodifiableListOrEmpty(List.of(nav1, nav2, nav3, nav4, nav5, nav6));
  }
}
