package com.kpmg.core.models.datalayer.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.adobe.cq.wcm.core.components.models.datalayer.PageData;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.constants.NameConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.core.constants.KPMGConstants;
import java.util.Calendar;
import java.util.Map;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.Test;

class KPMGPageDataImplTest {

  @Test
  void testConstructor() {
    KPMGPageDataImpl pageData = new KPMGPageDataImpl(null, null);
    assertNotNull(pageData);
  }

  @Test
  void testAllWithNull() {
    KPMGPageDataImpl kpmgPageData = new KPMGPageDataImpl(null, null);
    assertEquals("", kpmgPageData.getJson());
    assertEquals("", kpmgPageData.getPageName());
    assertEquals("", kpmgPageData.getExpiryDate());
    assertEquals("", kpmgPageData.getCountry());
    assertEquals("", kpmgPageData.getEffectiveDate());
    assertEquals("", kpmgPageData.getIssueDate());
    assertEquals("", kpmgPageData.getPrimaryCategory());
    assertEquals("", kpmgPageData.getSubCategory1());
    assertEquals("", kpmgPageData.getSubCategory2());
    assertEquals("", kpmgPageData.getSubCategory3());
  }

  @Test
  void testAllNotNull() throws JsonProcessingException {

    PageData pageData = mock(PageData.class);
    Page page = mock(Page.class);
    doReturn("id").when(pageData).getId();
    KPMGPageDataImpl kpmgPageData = new KPMGPageDataImpl(pageData, page);
    String relativePagePath = "/fr/en/test1/test2/test3/test4/test5";
    String absolutePath = KPMGConstants.PATH_KPMG_CONTENT_ROOT + relativePagePath;
    Calendar calendar = new Calendar.Builder().build();

    ValueMap properties =
        new ValueMapDecorator(
            Map.of(
                "articleTimeAndDate", calendar, NameConstants.PN_PAGE_LAST_REPLICATED, calendar));
    doReturn(properties).when(page).getProperties();

    doReturn(absolutePath).when(page).getPath();
    assertEquals("fr:en:test1:test2:test3:test4:test5", kpmgPageData.getPageName());

    doReturn(calendar).when(page).getOffTime();
    assertEquals(kpmgPageData.getDateFormat(calendar), kpmgPageData.getExpiryDate());

    assertEquals("fr", kpmgPageData.getCountry());

    assertEquals(kpmgPageData.getDateFormat(calendar), kpmgPageData.getEffectiveDate());
    assertEquals(kpmgPageData.getDateFormat(calendar), kpmgPageData.getIssueDate());

    String expected =
        String.format(
            "{\"%s\":%s}", pageData.getId(), (new ObjectMapper()).writeValueAsString(kpmgPageData));
    assertEquals(expected, kpmgPageData.getJson());
  }
}
