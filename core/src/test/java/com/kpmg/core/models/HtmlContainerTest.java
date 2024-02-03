package com.kpmg.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class HtmlContainerTest {
  private final AemContext context = AppAemContext.newAemContext();

  private HtmlContainer htmlContainer;

  @BeforeEach
  void setUp() {
    context.load().json("/htmlContainer.json", "/content/htmlContainer");
    context.currentResource("/content/htmlContainer");
    htmlContainer = context.request().adaptTo(HtmlContainer.class);
  }

  @Test
  void testGetListItems() {
    List<Resource> jsItems = htmlContainer.getJsItems();
    List<Resource> cssItems = htmlContainer.getCssItems();
    assertNotNull(jsItems);
    assertNotNull(cssItems);
    assertEquals(2, jsItems.size());
    assertEquals(2, cssItems.size());
    assertEquals("<h1>Enter html here</h1>", htmlContainer.getHtmlText());

    String[] extectedCSSPaths = {
      "/content/dam/kpmgsites/css/mycss.css", "/content/dam/kpmgsites/css/mycss.css"
    };
    String[] expectedJsPaths = {
      "/content/dam/kpmgsites/js/test-main.js", "/content/dam/kpmgsites/js/test-main.js"
    };
    assertEquals(extectedCSSPaths[0], htmlContainer.getCssPaths().get(0));
    assertEquals(expectedJsPaths[0], htmlContainer.getJsPaths().get(0));

    HtmlContainer.HtmlContainerDataLayer datalayerExtension =
        (HtmlContainer.HtmlContainerDataLayer) htmlContainer.getDatalayerExtension();
    assertEquals(extectedCSSPaths[0], datalayerExtension.getCssPaths().get(0));
    assertEquals(expectedJsPaths[0], datalayerExtension.getJsPaths().get(0));
  }
}
