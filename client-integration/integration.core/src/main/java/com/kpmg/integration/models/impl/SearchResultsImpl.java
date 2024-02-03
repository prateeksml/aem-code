package com.kpmg.integration.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.kpmg.integration.models.SearchResults;
import java.util.List;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {SearchResults.class, ComponentExporter.class},
    resourceType = SearchResultsImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchResultsImpl implements SearchResults {

  public static final String RESOURCE_TYPE = "kpmg/components/content/search-results";

  @Getter @ValueMapValue private String resultsCountMessage;

  @Getter @ValueMapValue private String mResultsCountMessage;

  @Getter @ValueMapValue private String upcomingLabel;

  @Getter @ValueMapValue private String pastEventLabel;

  @Getter @ValueMapValue private String noResultsMessage;

  @Getter @ChildResource private List<FilterType> filterList;

  @Getter @ValueMapValue private String systemErrorMessage;

  @Getter @ValueMapValue private String cancelButtonText;

  private interface Exclusions {
    String getExportedType();
  }

  @Delegate(excludes = Exclusions.class)
  @Self
  @Via(type = ResourceSuperType.class)
  private Image image;

  @Override
  public String getExportedType() {
    return SearchResultsImpl.RESOURCE_TYPE;
  }
}
