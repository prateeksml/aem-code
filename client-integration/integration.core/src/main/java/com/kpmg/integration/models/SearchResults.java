package com.kpmg.integration.models;

import com.adobe.cq.wcm.core.components.models.Image;
import com.kpmg.integration.models.impl.FilterType;
import java.util.List;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface SearchResults extends Image {

  String getResultsCountMessage();

  String getMResultsCountMessage();

  String getUpcomingLabel();

  String getPastEventLabel();

  String getNoResultsMessage();

  List<FilterType> getFilterList();

  String getSystemErrorMessage();

  String getCancelButtonText();
}
