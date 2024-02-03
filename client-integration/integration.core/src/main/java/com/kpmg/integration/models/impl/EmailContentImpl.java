package com.kpmg.integration.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.kpmg.integration.models.EmailContent;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
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
    adapters = {EmailContent.class, ComponentExporter.class},
    resourceType = EmailContentImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class EmailContentImpl implements EmailContent {

  public static final String RESOURCE_TYPE = "kpmg/components/content/emailcontent";

  @Getter @ValueMapValue private String emailSubject;

  @Getter @ValueMapValue private String emailContent;

  @Getter @ValueMapValue private String footerText;

  @Getter @ChildResource private List<FooterLink> footerLink;

  @Getter private int linklimit;

  private interface Exclusions {
    String getExportedType();
  }

  @Delegate(excludes = Exclusions.class)
  @Self
  @Via(type = ResourceSuperType.class)
  private Image image;

  @Override
  public String getExportedType() {
    return EmailContentImpl.RESOURCE_TYPE;
  }

  @PostConstruct
  protected void init() {
    this.linklimit = Optional.ofNullable(footerLink).map(fL -> fL.size()).orElse(0);
  }
}
