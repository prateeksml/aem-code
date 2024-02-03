package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.kpmg.integration.config.SmartLogicConfiguration;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(AemContextExtension.class)
class SmartLogicConfigurationServiceImplTest {
  private final AemContext context = new AemContext();
  @Mock private SmartLogicConfiguration config;
  @InjectMocks SmartLogicImpl service;
  final Map<String, Object> properties = new HashMap<>();

  @BeforeEach
  void setup() {
    properties.put("getContentTypeZthesID", "53054337506891119764226");
    properties.put("getCommonZthesID", "97508671404166678130047");
    properties.put("getGeopraphyZthesID", "93812326507033629793301");
  }

  @Test
  void testWithoutConfig() {
    final SmartLogicImpl service = context.registerInjectActivateService(new SmartLogicImpl());
    assertNull(service.getCommonZthesID());
  }

  @Test
  void getContentTypeZthesIDTest() {

    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("53054337506891119764226", service.getContentTypeZthesID());
  }

  @Test
  void getCommonZthesIDTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getCommonZthesID", "97508671404166678130047");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("97508671404166678130047", service.getCommonZthesID());
  }

  @Test
  void getGeopraphyZthesIDTest() {
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("93812326507033629793301", service.getGeopraphyZthesID());
  }

  @Test
  void getIndustryZthesIDTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getIndustryZthesID", "135320640047454042275950");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("135320640047454042275950", service.getIndustryZthesID());
  }

  @Test
  void getInsightZthesIDTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getInsightZthesID", "53054337506891119764226");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("53054337506891119764226", service.getInsightZthesID());
  }

  @Test
  void getMarketsZthesIDTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getMarketsZthesID", "93812326507033629793301");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("93812326507033629793301", service.getMarketsZthesID());
  }

  @Test
  void getMediaFormatsZthesIDTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getMediaFormatsZthesID", "53054337506891119764226");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("53054337506891119764226", service.getMediaFormatsZthesID());
  }

  @Test
  void getPersonasZthesIDTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getPersonasZthesID", "personazid");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("personazid", service.getPersonasZthesID());
  }

  @Test
  void getServiceZthesIDTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getServiceZthesID", "21234891119764226");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("21234891119764226", service.getServiceZthesID());
  }

  @Test
  void getSesClientProtocol() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSesClientProtocol", "https");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("https", service.getSesClientProtocol());
  }

  @Test
  void getSesClientHostIPTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSesClientHostIP", "ses.dev.author.kpmg");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("ses.dev.author.kpmg", service.getSesClientHostIP());
  }

  @Test
  void getSesClientPathTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSesClientPath", "/ses");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("/ses", service.getSesClientPath());
  }

  @Test
  void getSesPortTest() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSesPort", "443");
    final SmartLogicImpl service =
        context.registerInjectActivateService(new SmartLogicImpl(), properties);
    assertEquals("443", service.getSesPort());
  }
}
