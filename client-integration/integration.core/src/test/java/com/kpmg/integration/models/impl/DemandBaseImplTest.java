package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.kpmg.integration.services.impl.DemandBaseServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class DemandBaseImplTest {
  private final AemContext ctx = new AemContext();
  final Map<String, Object> properties = new HashMap<>();

  private DemandBaseImpl demandBase;

  @BeforeEach
  void setup() {
    properties.put("getDemandbaseURL", "https://api.company-target.com/api/v2/ip.json");
    properties.put(
        "getDemandbaseCurl", "https://scripts.demandbase.com/adobeanalytics/pZy5a2F8.min.js");
    ctx.registerInjectActivateService(new DemandBaseServiceImpl(), properties);
    demandBase = ctx.request().adaptTo(DemandBaseImpl.class);
  }

  @Test
  void getDemandbaseURL() {
    assertEquals("https://api.company-target.com/api/v2/ip.json", demandBase.getDemandbaseURL());
  }

  @Test
  void getDemandbaseCurl() {
    assertEquals(
        "https://scripts.demandbase.com/adobeanalytics/pZy5a2F8.min.js",
        demandBase.getDemandbaseCurl());
  }
}
