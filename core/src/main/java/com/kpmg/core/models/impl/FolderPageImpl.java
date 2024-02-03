package com.kpmg.core.models.impl;

import com.adobe.cq.export.json.ExporterConstants;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(
    adaptables = {SlingHttpServletRequest.class},
    resourceType = FolderPageImpl.RESOURCE_TYPE)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class FolderPageImpl {
  public static final String RESOURCE_TYPE = "kpmg/components/structure/page-folder";

  @SlingObject SlingHttpServletResponse response;

  @PostConstruct
  void postConstruct() throws IOException {
    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Redirecting to 404 page");
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }
}
