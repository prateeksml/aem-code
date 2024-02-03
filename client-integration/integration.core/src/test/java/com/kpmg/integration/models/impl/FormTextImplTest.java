package com.kpmg.integration.models.impl;

import static io.wcm.testing.mock.wcmio.caconfig.ContextPlugins.WCMIO_CACONFIG;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import static org.junit.jupiter.api.Assertions.*;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AemContextExtension.class)
class FormTextImplTest {

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

  @Mock Resource resource;

  @Mock ConfigurationBuilder configurationBuilder;

  SiteSettingsConfig regexMappingConifg;
  FormTextImpl formText = new FormTextImpl();

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
    context.addModelsForClasses(FormTextImpl.class);
  }

  @Test
  void getAlphabets() {
    regexMappingConifg =
        formText.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());
    context
        .request()
        .addRequestParameter("alphabets", regexMappingConifg.regexMapping().alphabets());
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getAlphabets());
  }

  @Test
  void getAlphaNumeric() {
    regexMappingConifg =
        formText.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());
    context
        .request()
        .addRequestParameter("alphanumeric", regexMappingConifg.regexMapping().alphanumeric());
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getAlphaNumeric());
  }

  @Test
  void getEmail() {
    regexMappingConifg =
        formText.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());
    context.request().addRequestParameter("email", regexMappingConifg.regexMapping().email());
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getEmail());
  }

  @Test
  void getPhone() {
    regexMappingConifg =
        formText.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());
    context.request().addRequestParameter("phone", regexMappingConifg.regexMapping().phone());
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getPhone());
  }

  @Test
  void getNumeric() {
    regexMappingConifg =
        formText.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());
    context.request().addRequestParameter("numeric", regexMappingConifg.regexMapping().numeric());
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getNumeric());
  }

  @Test
  void getRegexCheckbox() {
    context.load().json("src/test/resources/model-resources/formText.json", "/content/options");
    context.currentResource("/content/options/text");
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getRegexCheckbox());
  }

  @Test
  void getCustomRegex() {
    context.load().json("src/test/resources/model-resources/formText.json", "/content/options");
    context.currentResource("/content/options/text");
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getCustomRegex());
  }

  @Test
  void getRegexMessage() {
    context.load().json("src/test/resources/model-resources/formText.json", "/content/options");
    context.currentResource("/content/options/text");
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getRegexMessage());
  }

  @Test
  void getRegex() {
    context.load().json("src/test/resources/model-resources/formText.json", "/content/options");
    context.currentResource("/content/options/text");
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getRegex());
  }

  @Test
  void getMaxCharLength() {
    context.load().json("src/test/resources/model-resources/formText.json", "/content/options");
    context.currentResource("/content/options/text");
    formText = context.request().adaptTo(FormTextImpl.class);
    assertNotNull(formText.getMaxCharLength());
  }
}
