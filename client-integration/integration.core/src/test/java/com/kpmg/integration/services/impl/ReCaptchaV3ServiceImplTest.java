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
class ReCaptchaV3ServiceImplTest {

  private final AemContext ctx = new AemContext();

  private ReCaptchaV3ServiceImpl reCaptchaService;

  @BeforeEach
  void setUp() {}

  @Test
  void testConfig() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSiteKey", "anyKey");
    properties.put("getSiteSecret", "anySecret");
    properties.put("getCaptchaEnabled", "true");
    properties.put("getCaptchaEndpoint", "https://www.google.com/recaptcha/api/siteverify");
    properties.put("getScoreTolerance", 0.5);
    ReCaptchaV3ServiceImpl reCaptchaService =
        ctx.registerInjectActivateService(new ReCaptchaV3ServiceImpl(), properties);
    assertEquals("anyKey", reCaptchaService.getSiteKey());
    assertEquals("anySecret", reCaptchaService.getSiteSecret());
    assertEquals(
        "https://www.google.com/recaptcha/api/siteverify", reCaptchaService.getCaptchaEndpoint());
    assertEquals(0.5, reCaptchaService.getScoreTolerance());
    assertEquals("true", reCaptchaService.getCaptchaEnabled());
    assertEquals(false, reCaptchaService.isCaptchaVerified("anyToken"));
  }
}
