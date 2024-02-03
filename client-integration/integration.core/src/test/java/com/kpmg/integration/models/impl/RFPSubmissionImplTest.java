package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kpmg.integration.models.RFPSubmission;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class RFPSubmissionImplTest {

  private final AemContext ctx = new AemContext();

  private RFPSubmission rfpSubmission;

  @BeforeEach
  void setUp() {
    ctx.load()
        .json(
            "src/test/resources/model-resources/rfpSuubmission.json",
            "/content/kpmgpublic/rfpsubmission");
    ctx.currentResource("/content/kpmgpublic/rfpsubmission");
    rfpSubmission = ctx.request().adaptTo(RFPSubmission.class);
  }

  @Test
  void testGetters() {
    assertEquals("Thank You", rfpSubmission.getRfpTitle());
    assertEquals("Thanks you Description", rfpSubmission.getRfpReference());
    assertEquals("Contact Title", rfpSubmission.getContactInfoTitle());
    assertEquals("Print Button", rfpSubmission.getButtonText());
    assertEquals("Request", rfpSubmission.getRequestDetailTitle());
    assertEquals("List Title", rfpSubmission.getBelowListMessage());
    assertEquals(RFPSubmissionImpl.RESOURCE_TYPE, rfpSubmission.getExportedType());
  }
}
