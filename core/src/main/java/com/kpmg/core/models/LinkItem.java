package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.ListItem;

public interface LinkItem extends ListItem {
  String getLinkTitle();

  Link getLink();
}
