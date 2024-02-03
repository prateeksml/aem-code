package com.kpmg.integration.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import java.util.*;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/kpmg/newmapping")
public class FormMappingFieldServlet extends SlingSafeMethodsServlet {

  private static final Logger LOG = LoggerFactory.getLogger(FormMappingFieldServlet.class);

  private static final String NT_UNSTRUCTURED = "nt:unstructured";
  transient Resource pathResource;
  transient ValueMap valueMap;
  transient List<Resource> resourceList;

  public List<String> getTextProperties(Page currentPage, ResourceResolver resourceResolver) {
    List<String> properties = new ArrayList<>();

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
    SiteSettingsConfig formfieldMapping = getContextAwareConfig(currentPagePath, resourceResolver);

    if (formfieldMapping != null) {
      properties.add(formfieldMapping.formMapping().firstName());
      properties.add(formfieldMapping.formMapping().lastName());
      properties.add(formfieldMapping.formMapping().email());
      properties.add(formfieldMapping.formMapping().country());
      properties.add(formfieldMapping.formMapping().state());
      properties.add(formfieldMapping.formMapping().city());
      properties.add(formfieldMapping.formMapping().phone());
      properties.add(formfieldMapping.formMapping().company());
      properties.add(formfieldMapping.formMapping().role());
      properties.add(formfieldMapping.formMapping().message());
      properties.add(formfieldMapping.formMapping().inquiry());
      properties.add(formfieldMapping.formMapping().iskpmgclient());
      properties.add(formfieldMapping.formMapping().industry());
      properties.add(formfieldMapping.formMapping().attachment());

      LOG.debug("Successfully initialized site settings config" + formfieldMapping);
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

      List<String> disable = DisableFields(currentPage);
      List<String> properties = getTextProperties(currentPage, resourceResolver);
      assert properties != null;
      if (!properties.isEmpty() && properties != null) {
        for (String fields : properties) {
          valueMap = new ValueMapDecorator(new HashMap<>());
          for (String val : disable) {
            if (val.equalsIgnoreCase(fields)) {
              valueMap.put("disabled", "true");
            }
          }
          valueMap.put("text", fields);
          valueMap.put("value", fields);

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

  public List<String> DisableFields(Page Currentpage) {
    List<String> name = new ArrayList<>();
    Iterable<Resource> iterator = Currentpage.getContentResource().getChildren();
    for (Resource r : iterator) {
      Iterable<Resource> iteratorChild = r.getChildren();
      for (Resource res : iteratorChild) {
        Iterable<Resource> rootChild = res.getChildren();
        for (Resource innerRes : rootChild) {
          if (innerRes.getValueMap().get("name") != null) {
            name.add(innerRes.getValueMap().get("name").toString());
          }
          Iterable<Resource> rootChildRes = innerRes.getChildren();
          for (Resource innerResChild : rootChildRes) {
            if (innerResChild.getValueMap().get("name") != null) {
              name.add(innerResChild.getValueMap().get("name").toString());
            }
          }
        }
      }
    }
    return name;
  }
}
