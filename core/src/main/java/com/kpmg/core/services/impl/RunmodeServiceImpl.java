package com.kpmg.core.services.impl;

import com.kpmg.core.services.RunmodeConfig;
import com.kpmg.core.services.RunmodeService;
import org.apache.commons.lang3.ArrayUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Run mode configuration service for the sling run modes of each environment. */
@Component(
    service = RunmodeService.class,
    configurationPolicy = ConfigurationPolicy.REQUIRE,
    immediate = true)
@Designate(ocd = RunmodeConfig.class)
public class RunmodeServiceImpl implements RunmodeService {

  private static final Logger LOG = LoggerFactory.getLogger(RunmodeServiceImpl.class);
  public static final String SLING_RUNMODE_AUTHOR = "author";

  private RunmodeConfig config;

  @Override
  public String[] getRunmodes() {

    return this.config.runmodes();
  }

  @Override
  public boolean isAuthor() {
    return ArrayUtils.contains(this.getRunmodes(), SLING_RUNMODE_AUTHOR);
  }

  @Activate
  @Modified
  protected void activate(final RunmodeConfig config) {

    LOG.debug("Inside Activate Method {}", config);
    this.config = config;
  }
}
