package com.kpmg.core.models.impl;

import com.kpmg.core.models.GridItem;
import com.kpmg.core.models.IndustryServiceGrid;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {IndustryServiceGrid.class},
    resourceType = {IndustryServiceGridImpl.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IndustryServiceGridImpl extends AbstractKPMGComponentImpl
    implements IndustryServiceGrid {
  protected static final String RESOURCE_TYPE = "kpmg/components/content/industryservicegrid";

  private static final Logger log = LoggerFactory.getLogger(IndustryServiceGridImpl.class);

  @Getter @ChildResource private List<Resource> manualItems;

  @Getter @ChildResource private List<Resource> autoMatedItems;

  @Getter @ValueMapValue private String seeAll;

  @Getter @ValueMapValue private String seeLess;

  @Getter @ValueMapValue private String readMore;

  @Getter @ValueMapValue private String expandView;

  @Getter @ValueMapValue private String dataEntryType;

  @Self protected SlingHttpServletRequest request;

  List<GridItem> items = new ArrayList<>();

  @PostConstruct
  void postConstruct() {
    if ("manual".equals(dataEntryType)) {
      manualItems.stream().forEach(manualItem -> items.add(manualItem.adaptTo(GridItem.class)));
    } else if ("automated".equals(dataEntryType)) {
      autoMatedItems.stream()
          .forEach(autoMatedItem -> items.add(autoMatedItem.adaptTo(GridItem.class)));
    }
  }

  @Override
  public List<GridItem> getItems() {
    return items;
  }
}
