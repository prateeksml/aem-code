package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkBuilder;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import java.util.Calendar;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EventCardImplTest {

  @InjectMocks private EventCardImpl testClass;
  @Mock private ResourceResolver resourceResolver;
  @Mock private PageManager pm;
  @Mock private Page page;
  @Mock private ValueMap valueMap;
  @Mock private Calendar calendar;
  @Mock LinkManager linkManager;
  @Mock LinkBuilder linkBuilder;
  @Mock Link link;
  @Mock Resource imageResource;

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    MockitoAnnotations.initMocks(this);
    PrivateAccessor.setField(
        this.testClass, "linkURL", "/content/kpmgpublic/language-masters/en/event-1");
    PrivateAccessor.setField(this.testClass, "overrideTitle", false);
    PrivateAccessor.setField(this.testClass, "overrideDescription", false);
    PrivateAccessor.setField(this.testClass, "eventTitle", "Event 1");
    PrivateAccessor.setField(this.testClass, "eventDescription", "Event Descr 1");
    when(this.resourceResolver.adaptTo(PageManager.class)).thenReturn(pm);
    when(resourceResolver.getResource(anyString())).thenReturn(imageResource);
    when(imageResource.getValueMap()).thenReturn(valueMap);
    when(valueMap.get("fileReference")).thenReturn("test.jpg");
    when(pm.getPage(anyString())).thenReturn(page);
    when(linkManager.get(page)).thenReturn(linkBuilder);
    when(linkBuilder.build()).thenReturn(link);
    when(link.getURL()).thenReturn("/content/kpmgpublic/language-masters/en/event-1.html");
    when(page.getProperties()).thenReturn(valueMap);
    when(valueMap.get("jcr:title", String.class)).thenReturn("Event 1");
    when(valueMap.get("jcr:description", String.class)).thenReturn("Event Descr 1");
    when(valueMap.get("eventStartTimeAndDate", Calendar.class)).thenReturn(calendar);
    when(valueMap.get("eventEndTimeAndDate", Calendar.class)).thenReturn(calendar);
    when(valueMap.get("timeZone", String.class)).thenReturn("PST");
    testClass.init();
  }

  @Test
  void getEventTitle() {
    assertEquals("Event 1", testClass.getEventTitle());
  }

  @Test
  void getEventDescription() {
    assertEquals("Event Descr 1", testClass.getEventDescription());
  }

  @Test
  void getEventTimeZone() {
    assertEquals("PST", testClass.getEventTimeZone());
  }

  @Test
  void getPageURL() {
    assertEquals("/content/kpmgpublic/language-masters/en/event-1.html", testClass.getPageURL());
  }
}
