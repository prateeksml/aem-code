package com.kpmg.integration.models.impl;

import com.kpmg.integration.models.DemandBase;
import com.kpmg.integration.services.DemandBaseService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = DemandBase.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DemandBaseImpl implements DemandBase {

  @OSGiService DemandBaseService demandBaseService;

  @Override
  public String getDemandbaseURL() {
    return demandBaseService.getDemandbaseURL();
  }

  @Override
  public String getDemandbaseCurl() {
    return demandBaseService.getDemandbaseCurl();
  }
}
