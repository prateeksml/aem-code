package com.kpmg.integration.services.impl;

import com.kpmg.integration.config.SearchConsumerConfiguration;
import com.kpmg.integration.services.SearchConsumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(
    service = SearchConsumer.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SearchConsumerConfiguration.class)
public class SearchConsumerImpl implements SearchConsumer {

  private String searchEndpoint;
  private int pagination;
  private String wcmMode;
  private String suggestEndpoint;

  @Activate
  @Modified
  protected void activate(SearchConsumerConfiguration configuration) {
    this.searchEndpoint = configuration.getSearchEndpoint();
    this.pagination = configuration.getPagination();
    this.wcmMode = configuration.getWcmMode();
    this.suggestEndpoint = configuration.getSuggestEndpoint();
  }

  @Override
  public String getSearchEndpoint() {
    return this.searchEndpoint;
  }

  @Override
  public String getSuggestEndpoint() {
    return this.suggestEndpoint;
  }

  @Override
  public int getPagination() {
    return this.pagination;
  }

  @Override
  public String getWcmMode() {
    return this.wcmMode;
  }
}
