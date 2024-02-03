package com.kpmg.core.config;

/**
 * KPMG: OSGi Configuration Based Global Settings.
 *
 * <p>This is a OSGi Configuration based global settings, which will provided a application-wide
 * configurations as an OSGi Configurations.These configurations do not make much sense to the GSA
 * or LSA, but are technically required for the application to function.
 */
public interface KPMGGlobal {

  String getDomainName();

  String getPeopleContactFormEmailFromAddress();

  String getGenericContactFormEmailFromAddress();

  String getEnableEmailNotifications();

  String getSocailMediaAdminGroups();
}
