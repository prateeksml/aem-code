package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ReCaptchaV2ServiceImplTest {

  private final AemContext ctx = new AemContext();

  private ReCaptchaV2ServiceImpl reCaptchaV2Service;

  @BeforeEach
  void setUp() {}

  @Test
  void testConfig() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSiteKey", "anyKey");
    properties.put("getSiteSecret", "anySecret");
    ReCaptchaV2ServiceImpl reCaptchaService =
        ctx.registerInjectActivateService(new ReCaptchaV2ServiceImpl(), properties);
    assertEquals("anyKey", reCaptchaService.getSiteKey());
    assertEquals("anySecret", reCaptchaService.getSiteSecret());
  }
}
