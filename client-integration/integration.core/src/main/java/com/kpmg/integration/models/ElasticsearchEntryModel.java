package com.kpmg.integration.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class)
public class ElasticsearchEntryModel {

  private String suggestionText;

  public ElasticsearchEntryModel(SlingHttpServletRequest request) {
    ResourceResolver resourceResolver = request.getResourceResolver();
    String path = request.getParameter("path");

    if (path != null) {
      Resource entryResource = resourceResolver.getResource(path);
      if (entryResource != null) {
        ValueMap valueMap = entryResource.getValueMap();
        this.suggestionText = valueMap.get("suggestion_text", String.class);
      }
    }
  }

  public String getSuggestionText() {
    return suggestionText;
  }
}
