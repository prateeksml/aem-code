package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class EmailContentImplTest {

  private final AemContext ctx = new AemContext();

  private EmailContentImpl emailContent;

  @BeforeEach
  void setUp() {
    ctx.load().json("src/test/resources/model-resources/emailcontent.json", "/content/kpmg/email");
    ctx.currentResource("/content/kpmg/email");
    emailContent = ctx.request().adaptTo(EmailContentImpl.class);
  }

  @Test
  void testGetters() {
    assertEquals("EMAIL CONTENT", emailContent.getEmailContent());
    assertEquals("Your contact form submission", emailContent.getEmailSubject());
    assertEquals("KPMG FOOTER", emailContent.getFooterText());
    assertEquals("kpmg/components/content/emailcontent", emailContent.getExportedType());
    assertEquals("Tax", emailContent.getFooterLink().get(0).getLinkLabel());
    assertEquals(
        "/content/kpmgpublic/us/en/Tax", emailContent.getFooterLink().get(0).getLinkPath());
    assertEquals(
        "http://localhost:4503/content/kpmgpublic/us/en/Tax.html",
        emailContent.getFooterLink().get(0).getPublishLinkPath());
    assertNotNull(emailContent.getLinklimit());
  }
}
