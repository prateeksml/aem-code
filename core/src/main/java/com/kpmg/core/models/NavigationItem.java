package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;

public interface NavigationItem {
  String getTitle();

  Link getLink();
}
