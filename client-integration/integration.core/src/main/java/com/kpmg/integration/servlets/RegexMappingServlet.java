package com.kpmg.integration.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import java.lang.reflect.Method;
import java.util.*;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/kpmg/regexmapping")
public class RegexMappingServlet extends SlingSafeMethodsServlet {

  private static final Logger LOG = LoggerFactory.getLogger(RegexMappingServlet.class);

  private static final String NT_UNSTRUCTURED = "nt:unstructured";
  transient Resource pathResource;
  transient ValueMap valueMap;
  transient List<Resource> resourceList;

  public List<String> getTextProperties(Page currentPage, ResourceResolver resourceResolver) {
    List<String> properties = new ArrayList<>();
    SiteSettingsConfig regexFields = getContextAwareConfig(currentPage.getPath(), resourceResolver);
    if (regexFields.regexMapping() != null) {
      properties.add("Select Regex");
      Method[] allMethods = regexFields.regexMapping().getClass().getDeclaredMethods();
      Arrays.stream(allMethods)
          .forEach(
              method -> {
                LOG.info("Return Type: " + method.getReturnType().toString());

                if (method.getGenericReturnType().getTypeName().equalsIgnoreCase("java.lang.String")
                    && !(method.getName().equalsIgnoreCase("toString"))) {
                  properties.add(method.getName());
                }
              });
      LOG.debug("Successfully initialized site settings config" + regexFields.regexMapping());
    } else {
      LOG.debug("Form fields could not be loaded.");
    }

    if (properties != null) {
      return properties;
    }
    return null;
  }

  public SiteSettingsConfig getContextAwareConfig(
      String currentPage, ResourceResolver resourceresolver) {
    String currentpath = StringUtils.isNotBlank(currentPage) ? currentPage : StringUtils.EMPTY;
    Resource currentresource = resourceresolver.getResource(currentpath);
    if (currentresource != null) {
      ConfigurationBuilder confBuilder = currentresource.adaptTo(ConfigurationBuilder.class);
      if (confBuilder != null) {
        return confBuilder.as(SiteSettingsConfig.class);
      }
    }
    return null;
  }

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

    ResourceResolver resourceResolver;
    resourceResolver = request.getResourceResolver();
    pathResource = request.getResource();
    resourceList = new ArrayList<>();
    String pagePath = null;

    try {
      String[] path = getRefererSuffix(request).split(".html");

      Resource pageResource = resourceResolver.getResource(path[0].toString());

      Page currentPage = pageResource.adaptTo(Page.class);
      String[] modifiedPath = currentPage.getPath().replace("experience-fragments/", "").split("/");
      String currentPagePath =
          modifiedPath[0]
              + "/"
              + modifiedPath[1]
              + "/"
              + modifiedPath[2]
              + "/"
              + modifiedPath[3]
              + "/"
              + modifiedPath[4];

      SiteSettingsConfig regexFields = getContextAwareConfig(currentPagePath, resourceResolver);

      List<String> properties = getTextProperties(currentPage, resourceResolver);
      if (!properties.isEmpty() && properties != null) {
        for (String fields : properties) {
          valueMap = new ValueMapDecorator(new HashMap<>());
          String val =
              (fields.equals("alphabets"))
                  ? regexFields.regexMapping().alphabets()
                  : (fields.equals("numeric")
                      ? regexFields.regexMapping().numeric()
                      : (fields.equals("alphanumeric")
                          ? regexFields.regexMapping().alphanumeric()
                          : (fields.equals("email")
                              ? regexFields.regexMapping().email()
                              : (fields.equals("phone")
                                  ? regexFields.regexMapping().phone()
                                  : ""))));

          valueMap.put("text", fields);
          valueMap.put("value", val);

          resourceList.add(
              new ValueMapResource(
                  resourceResolver, new ResourceMetadata(), NT_UNSTRUCTURED, valueMap));
        }
      }
      DataSource dataSource = new SimpleDataSource(resourceList.iterator());
      request.setAttribute(DataSource.class.getName(), dataSource);

    } catch (NullPointerException e) {
      LOG.error("Exception:" + e.toString());
    }
  }

  public String getRefererSuffix(SlingHttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(r -> r.getHeader("Referer")) // get referer
        .map(referer -> StringUtils.substringAfter(referer, ".html")) // get suffix
        .orElse(null);
  }
}
