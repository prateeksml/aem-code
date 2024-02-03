package com.kpmg.integration.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.kpmg.integration.models.RFPSubmission;
import javax.annotation.Nonnull;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {RFPSubmission.class, ComponentExporter.class},
    resourceType = RFPSubmissionImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class RFPSubmissionImpl implements RFPSubmission {

  public static final String RESOURCE_TYPE = "kpmg/components/content/rfpsubmission";

  @Getter @ValueMapValue String rfpTitle;

  @Getter @ValueMapValue String rfpReference;

  @Getter @ValueMapValue String belowListMessage;

  @Getter @ValueMapValue String contactInfoTitle;

  @Getter @ValueMapValue String requestDetailTitle;

  @Getter @ValueMapValue String buttonText;

  @Nonnull
  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }
}
