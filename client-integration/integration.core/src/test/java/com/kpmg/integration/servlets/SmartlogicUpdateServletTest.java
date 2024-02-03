package com.kpmg.integration.servlets;

import static io.wcm.testing.mock.wcmio.caconfig.ContextPlugins.WCMIO_CACONFIG;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.services.SmartLogic;
import com.kpmg.integration.services.impl.SmartLogicImpl;
import com.kpmg.integration.services.impl.SmartlogicServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.http.HttpStatus;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class SmartlogicUpdateServletTest {
  @Mock private SmartLogic smartLogicConfig;
  @Mock private SmartlogicServiceImpl service;
  @InjectMocks private SmartlogicUpdateServlet servlet = new SmartlogicUpdateServlet();
  private final AemContext ctx =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK)
          .plugin(CACONFIG)
          .plugin(WCMIO_CACONFIG)
          .<AemContext>afterSetUp(
              context -> {
                context.registerService(SiteSettingsConfig.class);
                context.registerInjectActivateService(SmartLogicImpl.class);
                context.registerInjectActivateService(SmartlogicServiceImpl.class);
              })
          .build();

  @BeforeEach
  void setUp() {
    ctx.load().json("src/test/resources/model-resources/smartlogic.json", "/content/kpmg");
    ctx.currentPage("/content/kpmg");
  }

  @Test
  void doGet() {
    MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();
    when(service.getUpdatedTags(ctx.request())).thenReturn(new JsonArray());
    servlet.doGet(ctx.request(), response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }
}
