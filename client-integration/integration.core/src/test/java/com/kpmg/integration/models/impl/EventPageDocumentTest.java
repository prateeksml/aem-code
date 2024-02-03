package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class EventPageDocumentTest {

  private final AemContext ctx = new AemContext();

  private EventDocumentImpl eventDocument;

  @BeforeEach
  void setUp() {
    ctx.addModelsForClasses(EventDocumentImpl.class);
    ctx.load().json("src/test/resources/model-resources/event_model.json", "/content/kpmg");
    ctx.currentPage("/content/kpmg");
    eventDocument = ctx.currentPage().getContentResource().adaptTo(EventDocumentImpl.class);
  }

  @Test
  void testGetters() {
    assertEquals("EVENT", eventDocument.getDocumentType());
    assertNotNull(eventDocument.getEventStartTime());
    assertNotNull(eventDocument.getEventEndTime());
    assertEquals("training", eventDocument.getEventType());
    assertNull(eventDocument.getEventLocation());
    assertNotNull(eventDocument.getEventEndTimeAndDate());
    assertNotNull(eventDocument.getEventStartTimeAndDate());
  }
}
