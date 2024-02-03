package com.kpmg.integration.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.kpmg.integration.services.impl.SmartlogicServiceImpl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.http.HttpStatus;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SmartlogicTagsReportServletTest {
  @Mock SmartlogicServiceImpl service;
  @Mock MockSlingHttpServletRequest request;
  @InjectMocks private SmartlogicTagsReportServlet servlet;

  @BeforeEach
  void setUp() {
    servlet = Mockito.spy(servlet);
    Map<String, Object> params = new HashMap<>();
    params.put("page", "/content/kpmgpublic/ch/en");
    request.setParameterMap(params);
  }

  @Test
  void doGet() throws ServletException, IOException {
    MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();
    when(request.getParameter("page")).thenReturn("/content/kpmgpublic/ch/en");
    servlet.doGet(request, response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }

  @Test
  void escapeCommaTest() {
    String expected1 = "\"string that , contains a comma\"";
    String expected2 = "string that doesn't contain a comma";
    assertEquals(expected1, servlet.escapeComma("string that , contains a comma"));
    assertEquals(expected2, servlet.escapeComma("string that doesn't contain a comma"));
  }
}
