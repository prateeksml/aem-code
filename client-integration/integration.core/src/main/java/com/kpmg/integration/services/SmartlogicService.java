package com.kpmg.integration.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kpmg.integration.models.Tag;
import com.smartlogic.ses.client.SESClient;
import com.smartlogic.ses.client.Term;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.SlingHttpServletRequest;

public interface SmartlogicService {
  public SESClient createSESClient(String modelName);

  public List<JsonObject> getAllTagsByCategory(String pagePath, String categoryType);

  public JsonArray getAllTagsDetailsFromSESByCategory(
      Map<String, String> zidMap, SESClient sesClient);

  public String getModelNameByPagePath(String pagePath);

  public String getSESLanguage(String pagePath);

  Tag populateTagWithDetails(String categoryName, Term term, String zid);

  String[] getLocalSiteIDs(String pagePath);

  public JsonArray getUpdatedTags(SlingHttpServletRequest request);
}
