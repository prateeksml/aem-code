package com.kpmg.core.models.impl;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {Teaser.class, TeaserDelegateImpl.class},
    resourceType = TeaserDelegateImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class TeaserDelegateImpl implements Teaser {

  @Delegate
  @Self
  @Via(type = ResourceSuperType.class)
  Teaser teaser;

  @ChildResource List<TeaserAction> actions;

  @Self protected LinkManager linkManager;

  @SlingObject ResourceResolver resolver;

  protected Link link;

  @ScriptVariable protected Page currentPage;

  @SlingObject protected Resource resource;

  public static final String RESOURCE_TYPE = "kpmg/components/content/teaser";

  public List<TeaserAction> getOverridenActions() {
    return Optional.ofNullable(actions).map(ArrayList::new).orElseGet(ArrayList::new);
  }

  protected void initLink() {
    link = linkManager.get(resource).withLinkUrlPropertyName(Link.PN_LINK_URL).build();
  }

  @Override
  @Nullable
  public Link getLink() {
    initLink();
    return link.isValid() ? link : null;
  }

  public String getFeaturedImagePath() {
    return StringUtils.isNotBlank(getCtaFeaturedImagePath())
        ? getCtaFeaturedImagePath()
        : getCurrentPageFeaturedImagePath();
  }

  private String getCtaFeaturedImagePath() {
    return teaser.getActions().stream()
        .findFirst()
        .map(action -> action.getLink().getURL())
        .map(url -> StringUtils.substringBefore(url, ".html"))
        .map(resolver::getResource)
        .map(ctaResource -> ctaResource.adaptTo(Page.class))
        .map(
            page ->
                page != null
                    ? page.getProperties().get("cq:featuredimage/fileReference").toString()
                    : null)
        .orElse(StringUtils.EMPTY);
  }

  private String getCurrentPageFeaturedImagePath() {
    return currentPage.getProperties().get("cq:featuredimage/fileReference").toString();
  }
}
