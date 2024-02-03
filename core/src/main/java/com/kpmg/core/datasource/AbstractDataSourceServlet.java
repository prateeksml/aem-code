package com.kpmg.core.datasource;

import com.adobe.granite.ui.components.Config;
import com.adobe.granite.ui.components.ExpressionHelper;
import com.adobe.granite.ui.components.ExpressionResolver;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

/**
 * Abstract data source providing common logic. a copy from core components:
 * https://github.com/adobe/aem-core-wcm-components/blob/main/bundles/core/src/main/java/com/adobe/cq/wcm/core/components/internal/servlets/contentfragment/AbstractDataSourceServlet.java#L46
 */
public abstract class AbstractDataSourceServlet extends SlingSafeMethodsServlet {

  /**
   * Name of the resource property containing the path to a component. The servlet uses the content
   * fragment referenced by the component. The value may contain expressions.
   */
  public static final String PN_COMPONENT_PATH = "componentPath";

  /**
   * Returns an expression resolver to be used to resolve expressions in the configuration
   * properties (see {@link #PN_COMPONENT_PATH}).
   *
   * @return an expression resolver
   */
  protected ExpressionResolver getExpressionResolver() {
    return null;
  }

  /**
   * Returns datasource configuration.
   *
   * @param request the request
   * @return datasource configuration.
   */
  public Config getConfig(SlingHttpServletRequest request) {
    // get datasource configuration
    Resource datasource = request.getResource().getChild(Config.DATASOURCE);
    if (datasource == null) {
      return null;
    }
    return new Config(datasource);
  }

  /**
   * Get resource of the component.
   *
   * @param config datasource configuration
   * @param request the request
   * @return value map.
   */
  public Resource getComponentResource(Config config, SlingHttpServletRequest request) {
    if (config == null) {
      return null;
    }
    String componentPath = getParameter(config, PN_COMPONENT_PATH, request);
    if (componentPath == null) {
      return null;
    }
    // get component resource
    return request.getResourceResolver().getResource(componentPath);
  }

  /**
   * Reads a parameter from the specified datasource configuration, resolving expressions using the
   * {@link ExpressionResolver}. If the parameter is not found, {@code null} is returned.
   */
  public String getParameter(Config config, String name, SlingHttpServletRequest request) {
    // get value from configuration
    String value = config.get(name, String.class);
    if (value != null) {
      // evaluate value using the expression helper
      ExpressionHelper expressionHelper = new ExpressionHelper(getExpressionResolver(), request);
      return expressionHelper.getString(value);
    }
    return null;
  }

  public boolean isComponentDialog(SlingHttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(SlingHttpServletRequest::getPathInfo)
        .map(path -> StringUtils.contains(path, "_cq_dialog.html"))
        .orElse(false);
  }

  public boolean isPageProperties(SlingHttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(SlingHttpServletRequest::getPathInfo)
        .map(path -> StringUtils.contains(path, "properties.html"))
        .orElse(false);
  }

  public boolean isCreatePageWizard(SlingHttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(SlingHttpServletRequest::getPathInfo)
        .map(path -> StringUtils.contains(path, "createpagewizard"))
        .orElse(false);
  }

  public String getRefererSuffix(SlingHttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(r -> r.getHeader("Referer")) // get referer
        .map(referer -> StringUtils.substringAfter(referer, ".html")) // get suffix
        .orElse(null);
  }
}
