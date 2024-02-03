package com.kpmg.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.wcm.api.components.Component;
import com.kpmg.core.models.FooterSocialIcons;
import com.kpmg.core.models.LinkItemWithIcon;
import com.kpmg.core.utils.CollectionsUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {FooterSocialIcons.class, ComponentExporter.class},
    resourceType = FooterSocialIconsImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class FooterSocialIconsImpl implements FooterSocialIcons {

  public static final String RESOURCE_TYPE = "kpmg/components/navigation/footer/legal-links";

  @Getter @ValueMapValue String title;

  List<LinkItemWithIcon> iconLinkItems;

  @ChildResource List<Resource> items;

  @Self protected LinkManager linkManager;

  @ScriptVariable private Component component;

  @PostConstruct
  void postConstruct() {
    iconLinkItems =
        CollectionUtils.emptyIfNull(items).stream()
            .map(
                childResource ->
                    new LinkItemWithIconImpl("footer", childResource, component, linkManager))
            .collect(Collectors.toList());
  }

  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }

  public List<LinkItemWithIcon> getIconLinkItems() {
    return CollectionsUtil.unmodifiableListOrEmpty(iconLinkItems);
  }
}
