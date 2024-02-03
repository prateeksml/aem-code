package com.kpmg.core.models;

import com.kpmg.core.services.RenditionService;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

/** Model Class to read Custom Image Attributes */
@Model(
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Image {

  @RequestAttribute private String imagePath;

  @RequestAttribute private String componentName;

  @RequestAttribute private String imageAltText;

  @RequestAttribute private String aspectRatio;

  @SlingObject private ResourceResolver resourceResolver;

  @OSGiService private RenditionService renditionService;

  private Map<String, String> renditions;

  public String getAltText() {
    return imageAltText;
  }

  public Map<String, String> getRenditions() {
    return this.renditions;
  }

  @PostConstruct
  protected void init() {
    if (StringUtils.isNotBlank(this.componentName) && StringUtils.isNotBlank(this.imagePath)) {
      this.renditions =
          this.renditionService.getRenditions(
              this.componentName, this.imagePath, this.aspectRatio, this.resourceResolver);
    }
  }
}
