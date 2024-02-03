package com.kpmg.integration.servlets;

import static io.wcm.testing.mock.wcmio.caconfig.ContextPlugins.WCMIO_CACONFIG;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.foundation.forms.FormsHandlingRequest;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.wrappers.ResourceResolverWrapper;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.scripting.core.impl.ScriptingResourceResolver;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AemContextExtension.class)
class FormMappingFieldServletTest {

  private final AemContext context =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK)
          .plugin(CACONFIG)
          .plugin(WCMIO_CACONFIG)
          .<AemContext>afterSetUp(
              context -> {
                context.registerService(SiteSettingsConfig.class);
              })
          .build();

  @Mock private Page currentPage;

  FormMappingFieldServlet servlet = new FormMappingFieldServlet();
  List<String> parameters = new ArrayList<>();
  private static final String ROOT_RESOURCE_PATH = "/content/kpmgpublic/ch/de";
  private static final String HOME_RESOURCE_PATH = "/content/kpmgpublic/ch/de/home";
  private static final String SLING_CONFIGREF_NODE_TYPE = "sling:configRef";
  private static final String SLING_CONFIGREF_PATH = "/conf/kpmg/wcm-io-configs/ch/de";

  @BeforeEach
  public void setUp() {
    MockContextAwareConfig.registerAnnotationClasses(context, SiteSettingsConfig.class);
    context.create().resource(ROOT_RESOURCE_PATH, SLING_CONFIGREF_NODE_TYPE, SLING_CONFIGREF_PATH);
    Page page = context.create().page(HOME_RESOURCE_PATH);
    Resource resource = context.resourceResolver().getResource(HOME_RESOURCE_PATH);
    context.currentResource(resource);
    context.currentPage(page);
  }

  @Test
  void getTextProperties() {
    SiteSettingsConfig fm =
        servlet.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());
    List<String> properties =
        servlet.getTextProperties(
            context.currentPage(), context.currentResource().getResourceResolver());
    parameters.add(fm.formMapping().firstName());
    parameters.add(fm.formMapping().lastName());
    assertNotNull(properties);
    assertNotNull(parameters);
  }

  @Test
  void doGet() {
    org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest wrappedRequest =
        new org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest(
            new ResourceResolverWrapper(new ScriptingResourceResolver(true, null)), null);
    wrappedRequest.addRequestParameter("Name", "name");
    wrappedRequest.addRequestParameter("Inquiry", "inquiry");

    FormsHandlingRequest request =
        new FormsHandlingRequest(new SlingHttpServletRequestWrapper(wrappedRequest));
    MockSlingHttpServletResponse response = context.response();
    servlet.doGet(request, response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }
}
