package com.kpmg.integration.models.impl;

import com.day.cq.commons.Externalizer;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterLink {

  @SlingObject ResourceResolver resourceResolver;

  @OSGiService Externalizer externalizer;

  @Getter @ValueMapValue private String linkPath;

  @Getter @ValueMapValue private String linkLabel;

  @Getter private String publishLinkPath;

  @PostConstruct
  protected void init() {
    this.publishLinkPath = externalizer.publishLink(resourceResolver, this.linkPath + ".html");
  }
}
