package com.kpmg.core.services.impl;

import com.kpmg.core.config.FilterApiConfiguration;
import com.kpmg.core.services.FilterService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(
    service = FilterService.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = FilterApiConfiguration.class)
public class FilterServiceImpl implements FilterService {

  private String filterEndpoint;
  private String listingEndpoint;
  private int pagination;

  @Activate
  @Modified
  protected void activate(FilterApiConfiguration filterApiConfiguration) {
    this.filterEndpoint = filterApiConfiguration.getFilterEndpoint();
    this.listingEndpoint = filterApiConfiguration.getListingEndpoint();
    this.pagination = filterApiConfiguration.getPagination();
  }

  @Override
  public String getFilterEndpoint() {
    return filterEndpoint;
  }

  @Override
  public String getListingEndpoint() {
    return listingEndpoint;
  }

  @Override
  public int getPagination() {
    return pagination;
  }
}
