package com.kpmg.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KPMGGlobalConfigImplTest {

  @Mock KPMGGlobalConfigImpl.KPMGGlobalConfig config;

  @Test
  void testAll() {
    String domainName = "domainName";
    String enableEmailNotifications = "enableEmailNotifications";
    String peopleContactFormEmailFromAddress = "peopleContactFormEmailFromAddress";
    String genericContactFormEmailFromAddress = "genericContactFormEmailFromAddress";
    String socailMediaAdminGroups = "socailMediaAdminGroups";
    doReturn(domainName).when(config).getDomainName();
    doReturn(enableEmailNotifications).when(config).getEnableEmailNotifications();
    doReturn(peopleContactFormEmailFromAddress).when(config).getPeopleContactFormEmailFromAddress();
    doReturn(genericContactFormEmailFromAddress)
        .when(config)
        .getGenericContactFormEmailFromAddress();
    doReturn(socailMediaAdminGroups).when(config).getSocailMediaAdminGroups();

    KPMGGlobalConfigImpl kpmgGlobalConfig = new KPMGGlobalConfigImpl();
    kpmgGlobalConfig.activate(config);
    assertEquals(domainName, kpmgGlobalConfig.getDomainName());
    assertEquals(enableEmailNotifications, kpmgGlobalConfig.getEnableEmailNotifications());
    assertEquals(
        peopleContactFormEmailFromAddress, kpmgGlobalConfig.getPeopleContactFormEmailFromAddress());
    assertEquals(
        genericContactFormEmailFromAddress,
        kpmgGlobalConfig.getGenericContactFormEmailFromAddress());
    assertEquals(socailMediaAdminGroups, kpmgGlobalConfig.getSocailMediaAdminGroups());
  }
}
