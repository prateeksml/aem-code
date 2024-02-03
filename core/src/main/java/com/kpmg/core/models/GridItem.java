package com.kpmg.core.models;

import com.day.cq.wcm.api.Page;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GridItem {
  private static final Logger log = LoggerFactory.getLogger(GridItem.class);

  @SlingObject private ResourceResolver resourceResolver;

  @Getter @ValueMapValue private String title;

  @Getter @ValueMapValue private String description;

  @Getter @ValueMapValue private String link;

  @Getter @ValueMapValue private String iconType;

  @Getter @ValueMapValue private String icon;

  @Getter @ValueMapValue private String customIcon;

  @PostConstruct
  void postConstruct() {
    Resource pageResource =
        StringUtils.isNotBlank(link) ? resourceResolver.getResource(link) : null;
    Page page = pageResource != null ? pageResource.adaptTo(Page.class) : null;
    if (page != null) {
      title = StringUtils.isEmpty(title) ? page.getTitle() : title;
      description = StringUtils.isEmpty(description) ? page.getDescription() : description;
    }
  }
}
