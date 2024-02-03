package com.kpmg.integration.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kpmg.integration.models.FormOptions;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
class CFDropdownServletTest {
  private CFDropdownServlet servlet;

  private final AemContext context = new AemContext();

  @BeforeEach
  void setUp() {
    servlet = new CFDropdownServlet() {};
    servlet = Mockito.spy(servlet);
  }

  @Mock private FormOptions fm;

  @Test
  void testDoGet() {
    String componentPath = "/content/dam/kpmg/contentfragment";
    context.build().resource(componentPath).commit();
    context.request().setResource(context.resourceResolver().getResource(componentPath));

    ValueMap vm = new ValueMapDecorator(new HashMap<>());
    context.request().setAttribute("key", "title");
    context.request().setAttribute("value", "value");
    vm.put("key", context.request().getAttribute("key"));
    vm.put("value", context.request().getAttribute("value"));

    SlingHttpServletRequest request = context.request();
    SlingHttpServletResponse response = context.response();

    servlet.doGet(request, response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }
}
