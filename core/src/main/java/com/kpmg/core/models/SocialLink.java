package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;

public interface SocialLink {
  ComponentData getData();

  String getLink();

  String getHostName();
}
