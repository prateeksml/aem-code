package com.kpmg.integration.models;

import com.adobe.cq.export.json.ComponentExporter;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface SearchConfig extends ComponentExporter {
  String getCountryCode();

  String getLanguageCode();

  String getSearchPagePath();

  String getSearchEndpoint();

  int getPaginationLimit();

  String getRunMode();

  String getSuggestEndpoint();

  String getPeopleContactFormPath();
}
