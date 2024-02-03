package com.kpmg.core.models.impl;

import com.adobe.cq.wcm.core.components.models.Download;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.commons.DownloadResource;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;
import com.drew.lang.annotations.NotNull;
import com.kpmg.core.models.datalayer.DataLayerExcludes;
import java.util.Optional;
import lombok.experimental.Delegate;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = Download.class,
    resourceType = DownloadImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DownloadImpl extends AbstractKPMGComponentImpl implements Download {
  public static final String RESOURCE_TYPE = "kpmg/components/content/download";
  public static final String THUMBNAIL_RENDITION = "cq5dam.thumbnail.140.100.png";
  public static final String DOWNLOADLINK = "downloadlink";

  @Delegate(excludes = DataLayerExcludes.class)
  @Self
  @Via(type = ResourceSuperType.class)
  Download download;

  @Self SlingHttpServletRequest request;

  @ScriptVariable ValueMap properties;

  public String getThumbnail() {
    String fileReference = properties.get(DownloadResource.PN_REFERENCE, String.class);
    return Optional.ofNullable(fileReference)
        .map(request.getResourceResolver()::getResource)
        .filter(DamUtil::isAsset)
        .map(assetResource -> assetResource.adaptTo(Asset.class))
        .map(a -> a.getRendition(THUMBNAIL_RENDITION))
        .map(Rendition::getPath)
        .orElse(null);
  }

  @Override
  @NotNull
  protected ComponentData getComponentData() {
    return DataLayerBuilder.extending(super.getComponentData())
        .asComponent()
        .withTitle(this::getTitle)
        .withLinkUrl(() -> download.getUrl())
        .build();
  }

  public ComponentData getLinkData() {
    String id = getId();
    String url = getUrl();
    if (id == null && url == null) {
      return null; // no id or url, no data
    } else {
      return DataLayerBuilder.forComponent()
          .withId(
              () -> ComponentUtils.generateId(id, DOWNLOADLINK + ComponentUtils.ID_SEPARATOR + url))
          .withParentId(this::getId)
          .withLinkUrl(this::getUrl)
          .withType(() -> RESOURCE_TYPE + "/link")
          .build();
    }
  }
}
