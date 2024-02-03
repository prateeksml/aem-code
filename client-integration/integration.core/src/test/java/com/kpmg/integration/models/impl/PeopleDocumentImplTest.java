package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PeopleDocumentImplTest {

  private final AemContext ctx = new AemContext();

  private PeopleDocumentImpl peopleDocumentImpl;

  @BeforeEach
  void setUp() {
    ctx.addModelsForClasses(PeopleDocumentImpl.class);
    ctx.load().json("src/test/resources/model-resources/contact-page.json", "/content/kpmg");
    ctx.currentPage("/content/kpmg");
    peopleDocumentImpl = ctx.currentPage().getContentResource().adaptTo(PeopleDocumentImpl.class);
  }

  @Test
  void testGetter() {
    assertEquals(
        "/content/dam/kpmgsites/ch/content-fragments/contacts/kunal-dhak",
        peopleDocumentImpl.getContentFragmentPath());
    assertEquals("CONTACT", peopleDocumentImpl.getDocumentType());
    assertNull(peopleDocumentImpl.getFirstName());
    assertNull(peopleDocumentImpl.getLastName());
    assertNull(peopleDocumentImpl.getEmailAddress());
    assertNull(peopleDocumentImpl.getPhoneNumber());
    assertNull(peopleDocumentImpl.getCity());
    assertNull(peopleDocumentImpl.getState());
    assertNull(peopleDocumentImpl.getSalutation());
    assertNull(peopleDocumentImpl.getCountry());
    assertNull(peopleDocumentImpl.getBiography());
    assertNull(peopleDocumentImpl.getContactType());
    assertNull(peopleDocumentImpl.getMemberFirm());
    assertNull(peopleDocumentImpl.getMiddleInitial());
    assertNull(peopleDocumentImpl.getJobTitle());
  }
}
