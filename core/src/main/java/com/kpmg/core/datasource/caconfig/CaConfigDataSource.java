package com.kpmg.core.datasource.caconfig;

import com.adobe.acs.commons.wcm.datasources.DataSourceBuilder;
import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.adobe.granite.ui.components.ExpressionResolver;
import com.kpmg.core.constants.KPMGConstants;
import com.kpmg.core.datasource.AbstractDataSourceServlet;
import com.kpmg.core.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {Servlet.class})
@SlingServletResourceTypes(
    resourceTypes = CaConfigDataSource.RESOURCE_TYPE,
    methods = HttpConstants.METHOD_GET,
    extensions = "html")
public class CaConfigDataSource extends AbstractDataSourceServlet {

  public static final String RESOURCE_TYPE = "kpmg/ca-config/datasource";
  public static final String P_CONFIG = "config";

  @Reference transient DataSourceBuilder dataSourceBuilder;
  @Reference transient ConfigurationResolver configurationResolver;
  @Reference private transient ExpressionResolver expressionResolver;

  transient List<CaConfigDatasourceProvider> providers = new ArrayList<>();

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
    ConfigurationBuilder configurationBuilder = getConfigurationBuilder(request);
    String name = getConfig(request).get(P_CONFIG);
    dataSourceBuilder.addDataSource(request, getOptionsFromSource(name, configurationBuilder));
  }

  @Override
  protected ExpressionResolver getExpressionResolver() {
    return expressionResolver;
  }

  ConfigurationBuilder getConfigurationBuilder(SlingHttpServletRequest request) {
    Optional<Resource> optionalResource = Optional.empty();

    if (isCreatePageWizard(request)) { // get resource form referer.
      optionalResource =
          Optional.ofNullable(getRefererSuffix(request))
              .map(pagePath -> request.getResourceResolver().getResource(pagePath));
    } else if (isComponentDialog(request)) { // get resource form suffix.
      optionalResource =
          Optional.ofNullable(request.getRequestPathInfo())
              .map(RequestPathInfo::getSuffix)
              .map(
                  suffix ->
                      ResourceUtil.getResourceFromPathInfo(request.getResourceResolver(), suffix));
    } else if (isPageProperties(request)) { // get resource form item parameter
      optionalResource =
          Optional.ofNullable(request.getParameter("item"))
              .map(item -> request.getResourceResolver().getResource(item));
    }
    // if no resource was found, default to content resource.
    Resource resource =
        optionalResource.orElseGet(
            () -> request.getResourceResolver().getResource(KPMGConstants.PATH_KPMG_CONTENT_ROOT));

    return configurationResolver.get(resource);
  }

  List<DataSourceOption> getOptionsFromSource(
      String name, ConfigurationBuilder configurationBuilder) {
    return providers.stream()
        .filter(p -> p.shouldHandle(name))
        .findFirst()
        .map(provider -> provider.getOptions(configurationBuilder))
        .orElse(Collections.emptyList());
  }

  @Reference(
      service = CaConfigDatasourceProvider.class,
      policy = ReferencePolicy.DYNAMIC,
      cardinality = ReferenceCardinality.MULTIPLE,
      bind = "bindProvider",
      unbind = "unbindProvider")
  protected void bindProvider(CaConfigDatasourceProvider provider, Map<?, ?> props) {
    providers.add(provider);
  }

  protected void unbindProvider(CaConfigDatasourceProvider provider, Map<?, ?> props) {
    providers.remove(provider);
  }
}
