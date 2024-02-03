package com.kpmg.core.models.impl;

import com.adobe.acs.commons.models.injectors.annotation.ChildResourceFromRequest;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.commons.jcr.JcrConstants;
import com.kpmg.core.models.MainNavigationItem;
import com.kpmg.core.models.MainNavigationSecondaryItem;
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
    adapters = {MainNavigationItem.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Log4j
public class MainNavigationItemImpl extends AbstractNavigationItem implements MainNavigationItem {

  public static final String OPTION_SECONDARY = "secondary";
  public static final String OPTION_TERTIARY = "tertiary";
  public static final String OPTION_DIRECT = "direct";
  public static final String ALL_LINKS = "allLinks";

  @ValueMapValue(name = JcrConstants.JCR_TITLE)
  @Getter
  String title;

  @ValueMapValue @Getter String type;
  @ValueMapValue @Getter String allLinksText;

  @ChildResourceFromRequest List<MainNavigationSecondaryItem> items;

  @Self protected LinkManager linkManager;
  @Self protected SlingHttpServletRequest request;

  @Override
  public Link getLink() {
    return linkManager.get(request.getResource()).build();
  }

  @Override
  public Link getAllLinks() {
    return linkManager.get(request.getResource()).withLinkUrlPropertyName(ALL_LINKS).build();
  }

  @Override
  public List<MainNavigationSecondaryItem> getItems() {
    return CollectionsUtil.unmodifiableListOrEmpty(items);
  }

  public boolean isSecondary() {
    return OPTION_SECONDARY.equals(type);
  }

  public boolean isTertiary() {
    return OPTION_TERTIARY.equals(type);
  }

  public boolean isDirect() {
    return OPTION_DIRECT.equals(type);
  }
}
