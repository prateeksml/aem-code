package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface MainNavigationItem
    extends NavigationItemWithChildren<MainNavigationSecondaryItem> {

  String getType();

  boolean isSecondary();

  boolean isTertiary();

  boolean isDirect();

  Link getAllLinks();

  String getAllLinksText();
}
