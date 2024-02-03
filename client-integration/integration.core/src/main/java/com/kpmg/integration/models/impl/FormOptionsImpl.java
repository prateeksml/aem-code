package com.kpmg.integration.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.form.Options;
import com.kpmg.integration.models.FormOptions;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {FormOptions.class, ComponentExporter.class},
    resourceType = FormOptionsImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class FormOptionsImpl implements FormOptions {
  public static final String RESOURCE_TYPE = "kpmg/components/content/form/options";

  @Self // Indicates that we are resolving the current resource
  @Via(
      type =
          ResourceSuperType
              .class) // Resolve not as this model, but as the model of our supertype (ie: CC Image)
  private Options options;

  @Getter
  @ValueMapValue(name = "required")
  private boolean required;

  @Getter
  @ValueMapValue(name = "requiredMessage")
  private String requiredMessage;

  @Getter
  @ValueMapValue(name = "fragmentpath")
  private String fragmentPath;

  @Override
  public boolean getRequired() {
    return required;
  }

  @Override
  public String getRequiredMessage() {
    return requiredMessage;
  }

  @Override
  public String getFragmentPath() {
    return fragmentPath;
  }
}
