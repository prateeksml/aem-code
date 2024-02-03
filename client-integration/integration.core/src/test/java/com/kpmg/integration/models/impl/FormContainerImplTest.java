package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.kpmg.integration.services.impl.ReCaptchaV3ServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class FormContainerImplTest {

  private final AemContext ctx = new AemContext();

  private FormContainerImpl formContainer;

  @BeforeEach
  void setUp() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("getSiteKey", "anyKey");
    properties.put("getSiteSecret", "anySecret");
    ctx.load()
        .json(
            "src/test/resources/model-resources/formContainer.json", "/content/kpmg/formcontainer");
    ctx.currentResource("/content/kpmg/formcontainer");
    ctx.registerInjectActivateService(new ReCaptchaV3ServiceImpl(), properties);
    formContainer = ctx.request().adaptTo(FormContainerImpl.class);
  }

  @Test
  void testGetters() {
    assertEquals("/bin/kpmg/formhandler", formContainer.getAction());
    assertEquals("rfp", formContainer.getFormType());
    assertEquals("Thank You, Test", formContainer.getSuccessTitle());
    assertEquals("success Description", formContainer.getSuccessDescription());
    assertEquals(true, formContainer.isFirstName());
    assertEquals("System Error", formContainer.getSysError());
    assertEquals("Form validation Error", formContainer.getValidationError());
    assertEquals("stayOnSame", formContainer.getResponseType());
    assertEquals("anyKey", formContainer.getSiteKeyV3());
    assertEquals("anySecret", formContainer.getSiteSecretV3());
    assertNotNull(formContainer.getFormTitle());
    assertNotNull(formContainer.getCloseButtonTitle());
    assertNotNull(formContainer.getLoadingTitle());
    assertNotNull(formContainer.getLoadingSubtitle());
    assertNotNull(formContainer.getLoadingText());
    assertNotNull(formContainer.getAnalyticId());
    assertNotNull(formContainer.isCaptchaEnabled());
    assertNotNull(formContainer.getCaptchaScript());
    assertNotNull(formContainer.getCaptchaVersion());
    assertNotNull(formContainer.getCaptchaSize());
  }
}
