package com.kpmg.core.models.impl;

import com.adobe.acs.commons.models.injectors.annotation.HierarchicalPageProperty;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Page;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.PageData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.commons.Externalizer;
import com.kpmg.core.models.datalayer.impl.KPMGPageDataImpl;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
    adaptables = {SlingHttpServletRequest.class},
    adapters = {Page.class, ContainerExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = PageImpl.RESOURCE_TYPE)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class PageImpl implements Page {
  private static final String HTML_EXTENSION = ".html";
  public static final String RESOURCE_TYPE = "kpmg/components/structure/page";
  public static final String PN_DIRECTION = "direction";
  public static final String DEFAULT_DIRECTION = "ltr";

  Boolean dataLayerEnabled;
  ComponentData componentData;
  private String pageUrl;

  @OSGiService Externalizer externalizer;
  @SlingObject public ResourceResolver resourceResolver;

  @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
  private com.day.cq.wcm.api.Page currentPage;

  @SlingObject protected Resource resource;

  private interface DelegateExceptions {
    ComponentData getData();
  }

  @Delegate(excludes = DelegateExceptions.class)
  @Self
  @Via(type = ResourceSuperType.class)
  Page delegate;

  @HierarchicalPageProperty(PN_DIRECTION)
  @Optional
  String direction;

  public String getDirection() {
    return StringUtils.isBlank(direction) ? DEFAULT_DIRECTION : direction;
  }

  /** Overrides the default implementation to provide custom data layer properties for KPMG Page. */
  public ComponentData getData() {
    if (this.componentData == null) {
      if (this.dataLayerEnabled == null) {
        if (delegate != null) {
          this.dataLayerEnabled =
              java.util.Optional.ofNullable(currentPage)
                  .map(com.day.cq.wcm.api.Page::getContentResource)
                  .map(ComponentUtils::isDataLayerEnabled)
                  .orElse(false);
        } else {
          this.dataLayerEnabled =
              java.util.Optional.ofNullable(resource)
                  .map(ComponentUtils::isDataLayerEnabled)
                  .orElse(false);
        }
      }
      if (this.dataLayerEnabled) {
        this.componentData = this.getComponentData();
      }
    }

    return this.componentData;
  }

  /** Overrides the default implementation to provide custom data layer properties for KPMG Page. */
  protected ComponentData getComponentData() {
    PageData pageData = DataLayerBuilder.extending(delegate.getData()).asPage().build();
    return new KPMGPageDataImpl(pageData, currentPage);
  }

  public String getPageUrl() {
    externalizer = resourceResolver.adaptTo(Externalizer.class);
    pageUrl = externalizer.publishLink(resourceResolver, currentPage.getPath()) + HTML_EXTENSION;
    return pageUrl;
  }
}
