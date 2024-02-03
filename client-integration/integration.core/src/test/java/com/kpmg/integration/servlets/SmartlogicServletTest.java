package com.kpmg.integration.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.foundation.forms.FormsHandlingRequest;
import com.google.gson.JsonObject;
import com.kpmg.integration.services.SmartlogicService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.apache.http.HttpStatus;
import org.apache.sling.api.wrappers.ResourceResolverWrapper;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.scripting.core.impl.ScriptingResourceResolver;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SmartlogicServletTest {

  @Mock private SmartlogicService service;
  @InjectMocks SmartlogicServlet smartlogicServlet = new SmartlogicServlet();

  @BeforeEach
  void setup() {}

  @Test
  void testDoGet() throws IOException, ServletException {
    org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest wrappedRequest =
        new org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest(
            new ResourceResolverWrapper(new ScriptingResourceResolver(true, null)), null);
    wrappedRequest.addRequestParameter("category", "insights");
    wrappedRequest.addRequestParameter("page", "/content/kpmg/us/en/test-page");
    List<JsonObject> list = new ArrayList<>();
    JsonObject json = new JsonObject();
    json.addProperty("titlePath", "Advisory/Deal Advisory");
    list.add(json);
    when(service.getAllTagsByCategory(anyString(), anyString())).thenReturn(list);
    FormsHandlingRequest request =
        new FormsHandlingRequest(new SlingHttpServletRequestWrapper(wrappedRequest));
    MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();
    smartlogicServlet.doGet(request, response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }

  @Test
  void testDoGet2() throws IOException, ServletException {
    org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest wrappedRequest =
        new org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest(
            new ResourceResolverWrapper(new ScriptingResourceResolver(true, null)), null);
    wrappedRequest.addRequestParameter("category", "insights");
    wrappedRequest.addRequestParameter("page", "/content/kpmg/us/en/test-page");
    List<JsonObject> list = new ArrayList<>();
    JsonObject json = new JsonObject();
    json.addProperty("tagID", "83240608170062883780427");
    list.add(json);
    when(service.getAllTagsByCategory(anyString(), anyString())).thenReturn(list);
    FormsHandlingRequest request =
        new FormsHandlingRequest(new SlingHttpServletRequestWrapper(wrappedRequest));
    MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();
    smartlogicServlet.doGet(request, response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }
}
