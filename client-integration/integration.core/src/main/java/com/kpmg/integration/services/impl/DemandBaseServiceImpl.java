package com.kpmg.integration.services.impl;

import com.kpmg.integration.config.DemandBaseConfiguration;
import com.kpmg.integration.services.DemandBaseService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = DemandBaseService.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DemandBaseConfiguration.class)
public class DemandBaseServiceImpl implements DemandBaseService {

  private static final Logger LOG = LoggerFactory.getLogger(DemandBaseServiceImpl.class);

  private String demandBaseUrl;

  private String demandbasecdcurl;

  @Activate
  @Modified
  protected void activate(DemandBaseConfiguration configuration) {
    this.demandBaseUrl = configuration.getDemandbaseURL();
    this.demandbasecdcurl = configuration.getDemandbaseCurl();
  }

  @Override
  public String getDemandbaseURL() {
    return demandBaseUrl;
  }

  @Override
  public String getDemandbaseCurl() {
    return demandbasecdcurl;
  }
}
