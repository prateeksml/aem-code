package com.kpmg.integration.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.kpmg.integration.services.SmartLogic;
import com.kpmg.integration.services.impl.SmartLogicImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.osgi.context.OsgiContextImpl;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
public class CategorizationTabRenderConditionTest {
  @Mock private ResourceResolver resolver;
  @Mock private SmartLogic smartLogicConfig;
  @Mock private MockSlingHttpServletRequest request;
  @InjectMocks CategorizationTabRenderCondition render;

  String templatePath = "/conf/kpmg/settings/wcm/templates/page-article";
  String path = "/content/kpmgpublic/us/en/test-article";
  private final AemContext ctx =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK).build();

  @BeforeEach
  void setUp() {
    ctx.load().json("src/test/resources/model-resources/smartlogic.json", path);
    ctx.currentPage(path);
    final OsgiContextImpl context = new OsgiContextImpl();
    String[] disabled = {
      "/conf/kpmg/settings/wcm/templates/page-error",
      "/conf/kpmg/settings/wcm/templates/page-year",
      "/conf/kpmg/settings/wcm/templates/page-alphabet",
      "/conf/kpmg/settings/wcm/templates/search-results",
      "/conf/kpmg/settings/wcm/templates/page-month",
      "/conf/kpmg/settings/wcm/templates/digital-forms-xf",
      "/conf/kpmg/settings/wcm/templates/email-template",
      "/conf/kpmg/settings/wcm/templates/xf-web-variation"
    };
    smartLogicConfig =
        context.registerInjectActivateService(
            new SmartLogicImpl(), "getDisabledTemplates", disabled);

    render.request = request;
    render.resolver = resolver;
    render.smartLogicConfig = smartLogicConfig;
  }

  @Test
  void testPostConstruct() {
    when(request.getAttribute(anyString())).thenReturn(path);
    when(resolver.getResource(anyString())).thenReturn(ctx.currentResource(path));
    render.postConstruct();
    assertEquals(true, render.isValid(templatePath));
  }
}
