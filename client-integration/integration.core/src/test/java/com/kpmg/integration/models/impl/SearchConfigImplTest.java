package com.kpmg.integration.models.impl;

import static io.wcm.testing.mock.wcmio.caconfig.ContextPlugins.WCMIO_CACONFIG;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.services.SearchConsumer;
import com.kpmg.integration.util.KPMGUtilities;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AemContextExtension.class)
public class SearchConfigImplTest {

  private final AemContext context =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK)
          .plugin(CACONFIG)
          .plugin(WCMIO_CACONFIG)
          .<AemContext>afterSetUp(
              context -> {
                context.registerService(SiteSettingsConfig.class);
              })
          .build();

  @Mock private SearchConsumer searchConsumer;

  @Mock private Page currentPage;

  @Mock Resource resource;

  @Mock ConfigurationBuilder configurationBuilder;

  SiteSettingsConfig siteSettingsConfig;

  @InjectMocks private SearchConfigImpl searchImpl;

  private static final String ROOT_RESOURCE_PATH = "/content/kpmgpublic/ch/de";
  private static final String HOME_RESOURCE_PATH = "/content/kpmgpublic/ch/de/home";
  private static final String SLING_CONFIGREF_NODE_TYPE = "sling:configRef";
  private static final String SLING_CONFIGREF_PATH = "/conf/kpmg/wcm-io-configs/ch/de";
  private static final String SITE_SETTINGS_COUNTRY_PROP = "siteCountry";
  private static final String SITE_SETTINGS_LANGUSGE_PROP = "siteLocale";
  private static final String SITE_SETTINGS_SEARCH_PAGE_PATH_PROP = "searchPagePath";
  private static final String CURRENT_PAGE_COUNTRY_CODE = "ch";
  private static final String CURRENT_PAGE_LANGUAGE_CODE = "de";
  private static final String CURRENT_PAGE_SEARCH_PAGE_PATH =
      "/content/kpmgpublic/ch/de/home/misc/search";

  @BeforeEach
  public void setUp() {
    MockContextAwareConfig.registerAnnotationClasses(context, SiteSettingsConfig.class);
    context.create().resource(ROOT_RESOURCE_PATH, SLING_CONFIGREF_NODE_TYPE, SLING_CONFIGREF_PATH);
    Page page = context.create().page(HOME_RESOURCE_PATH);
    Resource resource = context.resourceResolver().getResource(HOME_RESOURCE_PATH);
    context.currentResource(resource);
    context.currentPage(page);
    MockContextAwareConfig.writeConfiguration(
        context,
        resource.getPath(),
        SiteSettingsConfig.class,
        Map.of(
            SITE_SETTINGS_SEARCH_PAGE_PATH_PROP,
            CURRENT_PAGE_SEARCH_PAGE_PATH,
            SITE_SETTINGS_COUNTRY_PROP,
            CURRENT_PAGE_COUNTRY_CODE,
            SITE_SETTINGS_LANGUSGE_PROP,
            CURRENT_PAGE_LANGUAGE_CODE));
    // context.load().json("src/test/resources/model-resources/ch_en.jcr.json","/content/kpmgpublic/ch/en");
    // context.addModelsForClasses(SearchConfigImpl.class);
  }

  @Test
  public void testGetExportedType() {
    assertEquals("kpmg/components/structure/search", searchImpl.getExportedType());
  }

  @Test
  public void testGetPaginationLimit() {
    when(searchConsumer.getPagination()).thenReturn(10);
    assertEquals(searchConsumer.getPagination(), 10);
  }

  @Test
  public void testGetSearchEndpoint() {
    when(searchConsumer.getSearchEndpoint()).thenReturn("search");
    assertEquals(searchConsumer.getSearchEndpoint(), "search");
  }

  @Test
  public void testGetCountryCodeFromSiteSettings() {
    siteSettingsConfig =
        KPMGUtilities.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());

    assertEquals(CURRENT_PAGE_COUNTRY_CODE, siteSettingsConfig.siteCountry());
  }

  @Test
  public void testGetLanguageCodeFromSiteSettings() {
    siteSettingsConfig =
        KPMGUtilities.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());

    assertEquals(CURRENT_PAGE_LANGUAGE_CODE, siteSettingsConfig.siteLocale());
  }

  @Test
  public void testGetSearchPagePathFromSiteSettings() {
    siteSettingsConfig =
        KPMGUtilities.getContextAwareConfig(
            context.currentPage().getPath(), context.currentResource().getResourceResolver());

    assertEquals(CURRENT_PAGE_SEARCH_PAGE_PATH, siteSettingsConfig.searchPagePath());
  }
}
