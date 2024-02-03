package com.kpmg.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.models.Footer;
import java.util.Calendar;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/** The Class FooterImpl. */
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {Footer.class, ComponentExporter.class},
    resourceType = FooterImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class FooterImpl implements Footer {

  public static final String RESOURCE_TYPE = "kpmg/components/footer";
  private final String COPYRIGHT_DYNAMIC_YEAR = "COPYRIGHT_DYNAMIC_YEAR";
  @ScriptVariable private Page currentPage;
  @Getter @ValueMapValue private String copyrightText;
  @Getter private String homePage;

  @PostConstruct
  void postConstruct() {
    copyrightText =
        Optional.ofNullable(copyrightText)
            .map(t -> t.replace(COPYRIGHT_DYNAMIC_YEAR, getCurrentYear()))
            .orElse(null);
  }

  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }

  String getCurrentYear() {
    return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
  }
}
