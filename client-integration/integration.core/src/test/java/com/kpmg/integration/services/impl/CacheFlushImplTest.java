package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kpmg.integration.config.CacheFlushConfiguration;
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
public class CacheFlushImplTest {
  private final AemContext context = new AemContext();

  @Mock private CacheFlushConfiguration config;
  @InjectMocks private CacheFlushImpl flush = new CacheFlushImpl();
  final Map<String, Object> properties = new HashMap<>();

  @BeforeEach
  void setup() {
    properties.put("getAkamaiAccessToken", "test_access_token");
    properties.put("getAkamaiClientToken", "test_client_token");
    properties.put("getAkamaiClientSecret", "test_client_secret");
    properties.put("getAkamaiHostToken", "test_host");
    properties.put("getDefaultAkamaiDomain", "test_akamai_domain");
    flush = context.registerInjectActivateService(new CacheFlushImpl(), properties);
  }

  @Test
  void getAkamaiAccessTokenTest() {
    assertEquals("test_access_token", flush.getAkamaiAccessToken());
  }

  @Test
  void getAkamaiClientSecretTest() {
    assertEquals("test_client_secret", flush.getAkamaiClientSecret());
  }

  @Test
  void getAkamaiClientTokenTest() {
    assertEquals("test_client_token", flush.getAkamaiClientToken());
  }

  @Test
  void getAkamaiHostTokenTest() {
    assertEquals("test_host", flush.getAkamaiHostToken());
  }

  @Test
  void getDefaultAkamaiDomainTest() {
    assertEquals("test_akamai_domain", flush.getDefaultAkamaiDomain());
  }
}
