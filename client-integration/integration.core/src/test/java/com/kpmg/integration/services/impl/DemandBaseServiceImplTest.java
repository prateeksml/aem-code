package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

@ExtendWith(AemContextExtension.class)
class DemandBaseServiceImplTest {

  private final AemContext ctx = new AemContext();
  @InjectMocks private DemandBaseServiceImpl demandBaseService;

  final Map<String, Object> properties = new HashMap<>();

  @BeforeEach
  void setup() {}

  @Test
  void testConfig() {
    properties.put("getDemandbaseURL", "https://api.company-target.com/api/v2/ip.json");
    properties.put(
        "getDemandbaseCurl", "https://scripts.demandbase.com/adobeanalytics/pZy5a2F8.min.js");
    final DemandBaseServiceImpl demandBaseService1 =
        ctx.registerInjectActivateService(new DemandBaseServiceImpl(), properties);

    assertEquals(
        "https://scripts.demandbase.com/adobeanalytics/pZy5a2F8.min.js",
        demandBaseService1.getDemandbaseCurl());
    assertEquals(
        "https://api.company-target.com/api/v2/ip.json", demandBaseService1.getDemandbaseURL());
  }
}
