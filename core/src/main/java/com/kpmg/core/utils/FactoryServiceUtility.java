package com.kpmg.core.utils;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Utility class to read the factory service configurations. */
public final class FactoryServiceUtility {

  private static final Logger LOGGER = LoggerFactory.getLogger(FactoryServiceUtility.class);

  private FactoryServiceUtility() {}

  /**
   * This method returns the service configurations
   *
   * @param configurationAdmin - {@link ConfigurationAdmin} object
   * @param serviceName - Service class name
   * @return service configurations
   */
  public static Configuration[] getConfigurations(
      final ConfigurationAdmin configurationAdmin, final String serviceName) {
    Configuration[] configurations = null;
    if ((null == configurationAdmin) || StringUtils.isBlank(serviceName)) {
      return configurations;
    }
    final String filter = '(' + ConfigurationAdmin.SERVICE_FACTORYPID + '=' + serviceName + ')';

    try {
      configurations = configurationAdmin.listConfigurations(filter);
    } catch (final IOException e) {
      LOGGER.error("IOException while fetching factory configuration List", e);
    } catch (final InvalidSyntaxException e) {
      LOGGER.error("InvalidSyntaxException while fetching factory config list", e);
    }
    return configurations;
  }
}
