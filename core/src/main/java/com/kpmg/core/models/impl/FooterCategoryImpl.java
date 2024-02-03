package com.kpmg.core.models.impl;

import com.adobe.acs.commons.models.injectors.annotation.I18N;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.components.Component;
import com.kpmg.core.models.FooterCategory;
import com.kpmg.core.models.LinkItem;
import com.kpmg.core.utils.CollectionsUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {FooterCategory.class, ComponentExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class FooterCategoryImpl implements FooterCategory {
  @I18N private I18n i18n;

  @Getter @ValueMapValue String title;

  @SlingObject ResourceResolver resourceResolver;
  List<LinkItem> linkItems;

  @ChildResource List<Resource> items;

  @Self protected LinkManager linkManager;

  @ScriptVariable private Component component;
  private static final String CONTACT_KEY = "kpmg.footer.category.contact.label";
  private static final String MEDIA_KEY = "kpmg.footer.category.media.label";
  private static final String CONTACT = "contact";
  private static final String MEDIA = "media";

  @PostConstruct
  void postConstruct() {
    linkItems =
        CollectionUtils.emptyIfNull(items).stream()
            .map(childResource -> new LinkItemImpl("footer", childResource, component, linkManager))
            .collect(Collectors.toList());

    if (title.equalsIgnoreCase(CONTACT)) {
      title = i18n.get(CONTACT_KEY);
    } else if (title.equalsIgnoreCase(MEDIA)) {
      title = i18n.get(MEDIA_KEY);
    } else {
      title = getTitle();
    }
  }

  @Override
  public String getExportedType() {
    return null;
  }

  @Override
  public List<LinkItem> getLinkItems() {
    return CollectionsUtil.unmodifiableListOrEmpty(linkItems);
  }
}
