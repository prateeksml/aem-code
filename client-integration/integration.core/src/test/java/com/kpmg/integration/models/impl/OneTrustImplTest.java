package com.kpmg.integration.models.impl;

import static io.wcm.testing.mock.wcmio.caconfig.ContextPlugins.WCMIO_CACONFIG;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import static org.junit.jupiter.api.Assertions.*;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AemContextExtension.class)
class OneTrustImplTest {

  private final AemContext context =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK)
          .plugin(CACONFIG)
          .plugin(WCMIO_CACONFIG)
          .<AemContext>afterSetUp(
              context -> {
                context.registerService(SiteSettingsConfig.class);
              })
          .build();
  SiteSettingsConfig siteSettingsConfig;
  OneTrustImpl oneTrust = new OneTrustImpl();
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
  void getOneTrustScript() {
    siteSettingsConfig =
        oneTrust.getContextConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());
    context.request().addRequestParameter("oneTrust", siteSettingsConfig.oneTrust());
    oneTrust = context.request().adaptTo(OneTrustImpl.class);
    assertNotNull(oneTrust.getOneTrustScript());
  }

  @Test
  void getAttributeOptions() {
    List<String> attributeList = new ArrayList<>();
    attributeList =
        oneTrust.getContextAwareConfig(
            context.currentPage().getPath(), context.resourceResolver(), "attribute");
    context.request().addRequestParameter("attributeList", attributeList.toString());
    oneTrust = context.request().adaptTo(OneTrustImpl.class);
    assertNotNull(oneTrust.getAttributeOptions());
  }

  @Test
  void getCategoryOptions() {
    List<String> categoryList = new ArrayList<>();
    categoryList =
        oneTrust.getContextAwareConfig(
            context.currentPage().getPath(), context.resourceResolver(), "categoryName");
    context.request().addRequestParameter("categoryList", categoryList.toString());
    oneTrust = context.request().adaptTo(OneTrustImpl.class);
    assertNotNull(oneTrust.getCategoryOptions());
  }

  @Test
  void getMapOptions() {
    HashMap<String, List<String>> mapObj = new HashMap<>();
    List<String> list = new ArrayList<>();
    mapObj.put("dmdbase", Collections.singletonList("C0001"));
    mapObj.put("podbean", Collections.singletonList("C0004"));
    context.request().addRequestParameter("mapObj", mapObj.toString());
    oneTrust = context.request().adaptTo(OneTrustImpl.class);
    assertNotNull(oneTrust.getMapOptions());
  }
}
