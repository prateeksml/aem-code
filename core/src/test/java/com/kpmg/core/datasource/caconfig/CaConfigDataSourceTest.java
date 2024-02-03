package com.kpmg.core.datasource.caconfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.adobe.acs.commons.wcm.datasources.DataSourceBuilder;
import com.adobe.granite.ui.components.Config;
import com.adobe.granite.ui.components.ExpressionResolver;
import com.kpmg.core.datasource.caconfig.providers.ArticleTypeDataSourceProvider;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.apache.sling.servlethelpers.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.builder.ImmutableValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CaConfigDataSourceTest {
  private CaConfigDataSource servlet;

  private final AemContext context = AppAemContext.newAemContext();

  static final String PATH = "/content/kpmgpublic";

  @Mock ExpressionResolver expressionResolver;
  @Mock ConfigurationResolver configurationResolver;
  @Mock ConfigurationBuilder configurationBuilder;
  @Mock Config config;
  @Mock DataSourceBuilder dataSourceBuilder;

  @Spy
  ArticleTypeDataSourceProvider articleTypeDataSourceProvider = new ArticleTypeDataSourceProvider();

  // not using beforeEach because one of the methods does not require the setup/stubs.
  void setUp() {
    context.build().resource(PATH).commit().resource(PATH + "/" + Config.DATASOURCE).commit();
    Resource resource = context.resourceResolver().getResource(PATH);
    context.currentResource(resource);
    context.request().setResource(resource);
    @NotNull MockSlingHttpServletRequest request = context.request();

    servlet = new CaConfigDataSource();
    servlet = spy(servlet);

    servlet.configurationResolver = configurationResolver;
    List<CaConfigDatasourceProvider> providers = new ArrayList<>();
    providers.add(articleTypeDataSourceProvider);
    servlet.providers = providers;
    servlet.dataSourceBuilder = dataSourceBuilder;
    doReturn(config).when(servlet).getConfig(context.request());
    doReturn(ArticleTypeDataSourceProvider.NAME).when(config).get(CaConfigDataSource.P_CONFIG);
    doReturn(configurationBuilder).when(configurationResolver).get(any());
    MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
    requestPathInfo.setResourcePath(PATH);
    requestPathInfo.setSuffix(PATH);
  }

  @Test
  void doGetCreatePageWizard() {
    setUp();
    doReturn(true).when(servlet).isCreatePageWizard(context.request());
    doReturn(PATH).when(servlet).getRefererSuffix(context.request());
    verifyProviderDidRun();
  }

  @Test
  void doGetComponentDialog() {
    setUp();
    SlingHttpServletRequest request = spy(context.request());
    doReturn(true).when(servlet).isComponentDialog(context.request());
    verifyProviderDidRun();
  }

  @Test
  void doGetPageProperties() {
    setUp();
    doReturn(true).when(servlet).isPageProperties(context.request());
    context.request().setParameterMap(ImmutableValueMap.of("item", PATH));
    verifyProviderDidRun();
  }

  @Test
  void bindUnbindProviders() {
    servlet = new CaConfigDataSource();
    servlet.bindProvider(articleTypeDataSourceProvider, ImmutableValueMap.of());
    assertEquals(1, servlet.providers.size());
    servlet.unbindProvider(articleTypeDataSourceProvider, ImmutableValueMap.of());
    assertEquals(0, servlet.providers.size());
  }

  void verifyProviderDidRun() {
    SlingHttpServletRequest request = context.request();
    SlingHttpServletResponse response = context.response();

    servlet.doGet(request, response);
    verify(articleTypeDataSourceProvider, times(1)).getOptions(configurationBuilder);
  }
}
