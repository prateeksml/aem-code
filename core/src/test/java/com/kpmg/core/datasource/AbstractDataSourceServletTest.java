package com.kpmg.core.datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.adobe.granite.ui.components.Config;
import com.adobe.granite.ui.components.ExpressionResolver;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Map;
import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
class AbstractDataSourceServletTest {

  private AbstractDataSourceServlet servlet;

  private final AemContext context = AppAemContext.newAemContext();

  @Mock ExpressionResolver expressionResolver;

  @BeforeEach
  void setUp() {
    servlet =
        new AbstractDataSourceServlet() {
          @Override
          protected ExpressionResolver getExpressionResolver() {
            return expressionResolver;
          }
        };
    servlet = Mockito.spy(servlet);
  }

  @Test
  void testGetConfig() {
    String componentPath = "/component";
    context
        .build()
        .resource(componentPath + "/" + Config.DATASOURCE, Map.of("key", "value"))
        .commit();
    context.request().setResource(context.resourceResolver().getResource(componentPath));
    Config config = servlet.getConfig(context.request());
    assertEquals("value", config.get("key"));
  }

  @Test
  void testGetConfigNull() {
    String componentPath = "/component";
    context.build().resource(componentPath).commit();
    context.request().setResource(context.resourceResolver().getResource(componentPath));
    Config c = servlet.getConfig(context.request());
    assertNull(c);
  }

  @Test
  void testGetComponentResource() {
    SlingHttpServletRequest request = context.request();
    String path = "/my-path";
    context
        .build()
        .resource(path, Map.of(AbstractDataSourceServlet.PN_COMPONENT_PATH, path))
        .commit();
    Config config = new Config(context.resourceResolver().getResource(path));
    Mockito.when(servlet.getParameter(config, AbstractDataSourceServlet.PN_COMPONENT_PATH, request))
        .thenReturn(path);
    assertEquals(path, servlet.getComponentResource(config, request).getPath());
  }

  @Test
  void testGetComponentResourceNulls() {
    assertNull(servlet.getComponentResource(null, null));

    Config config = Mockito.mock(Config.class);

    Mockito.when(
            servlet.getParameter(
                config, AbstractDataSourceServlet.PN_COMPONENT_PATH, context.request()))
        .thenReturn(null);
    assertNull(servlet.getComponentResource(config, context.request()));
  }

  @Test
  void testIsComponentDialog() {
    assertFalse(servlet.isComponentDialog(null));

    context.request().setPathInfo("/test/_cq_dialog.html");
    assertTrue(servlet.isComponentDialog(context.request()));

    context.request().setPathInfo("/test/test.html");
    assertFalse(servlet.isComponentDialog(context.request()));
  }

  @Test
  void testIsPageProperties() {
    assertFalse(servlet.isPageProperties(null));

    context.request().setPathInfo("/test/properties.html");
    assertTrue(servlet.isPageProperties(context.request()));

    context.request().setPathInfo("/test/test.html");
    assertFalse(servlet.isPageProperties(context.request()));
  }

  @Test
  void testIsCreatePageWizard() {
    assertFalse(servlet.isCreatePageWizard(null));

    context.request().setPathInfo("/test/createpagewizard.html");
    assertTrue(servlet.isCreatePageWizard(context.request()));

    context.request().setPathInfo("/test/test.html");
    assertFalse(servlet.isCreatePageWizard(context.request()));
  }

  @Test
  void testGetRefererSuffixt() {
    assertNull(servlet.getRefererSuffix(null));

    String expectedSuffixPath = "/test";
    SlingHttpServletRequest request = Mockito.spy(context.request());
    Mockito.when(request.getHeader("Referer")).thenReturn("/content.html" + expectedSuffixPath);
    assertEquals(expectedSuffixPath, servlet.getRefererSuffix(request));
  }
}
