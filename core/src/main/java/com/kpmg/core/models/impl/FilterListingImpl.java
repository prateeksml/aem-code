package com.kpmg.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.models.FilterListing;
import com.kpmg.core.services.FilterService;
import com.kpmg.core.utils.NodeUtils;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = FilterListing.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterListingImpl extends AbstractKPMGComponentImpl implements FilterListing {

  private static final Logger LOG = LoggerFactory.getLogger(FilterListingImpl.class);

  @ScriptVariable private Page currentPage;

  @ValueMapValue @Getter private String title;

  @ValueMapValue @Getter private String description;

  @OSGiService private FilterService filterService;

  @Getter private String filterEndpoint;

  @Getter private String listingEndpoint;

  @Getter private int pagination;

  @Getter private String countryCode;

  @Getter private String langCode;

  @PostConstruct
  protected void init() {
    filterEndpoint = filterService.getFilterEndpoint();
    listingEndpoint = filterService.getListingEndpoint();
    pagination = filterService.getPagination();
    countryCode = NodeUtils.extractCountryLang(currentPage.getPath(), 1);
    langCode = NodeUtils.extractCountryLang(currentPage.getPath(), 2);
  }
}
