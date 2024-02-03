package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface MainNavigationTertiaryItem extends NavigationItem {
  String getTitle();

  Link getLink();
}
