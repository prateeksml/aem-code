package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith({AemContextExtension.class})
public class FormOptionsImplTest {

  private final AemContext ctx = new AemContext();

  FormOptionsImpl formOptionsnew = new FormOptionsImpl();

  @BeforeEach
  void setUp() {
    ctx.addModelsForClasses(FormOptionsImpl.class);
    ctx.load().json("src/test/resources/model-resources/FormOption.json", "/content");
    ctx.currentResource("/content/options");
    formOptionsnew = ctx.request().adaptTo(FormOptionsImpl.class);
  }

  @Test
  void getRequired() {
    assertNotNull(formOptionsnew.getRequired());
  }

  @Test
  void getRequiredMessage() {
    assertEquals("required message", formOptionsnew.getRequiredMessage());
  }

  @Test
  void getFragmentPath() {
    assertNotNull(formOptionsnew.getFragmentPath());
  }

  @Test
  void getText() {
    ctx.currentResource("/content/privacy");
    PrivacyTextRTEImpl privacy = ctx.request().adaptTo(PrivacyTextRTEImpl.class);
    assertNotNull(privacy.getText());
  }

  @Test
  void getId() {
    ctx.currentResource("/content/privacy");
    PrivacyTextRTEImpl privacy = ctx.request().adaptTo(PrivacyTextRTEImpl.class);
    assertNotNull(privacy.getId());
  }
}
