package com.kpmg.core.models;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.VariationDef;
import com.kpmg.core.constants.GlobalConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CFReportCellValue {

  @RequestAttribute private Resource result;

  private String value;
  private String cfEditLink;
  @SlingObject protected SlingHttpServletRequest request;

  public CFReportCellValue() {}

  public CFReportCellValue(Resource result) {
    this.result = result;
    init();
  }

  public String getValue() {
    return value;
  }

  public String getCfEditLink() {
    return cfEditLink;
  }

  @PostConstruct
  private void init() {
    final String cfPath =
        StringUtils.substringBeforeLast(result.getPath(), GlobalConstants.JCR_CONTENT);
    final ResourceResolver resourceResolver = result.getResourceResolver();
    final Resource cfResource = resourceResolver.getResource(cfPath);
    final ContentFragment cf = cfResource.adaptTo(ContentFragment.class);
    if (result.getPath().endsWith(GlobalConstants.CF_MASTER)) {
      value = "master";
      cfEditLink = cfPath;
    } else {
      final Iterator<VariationDef> variationIterator = cf.listAllVariations();
      final Stream<VariationDef> variationStream =
          StreamSupport.stream(
              Spliterators.spliteratorUnknownSize(variationIterator, Spliterator.ORDERED), false);

      variationStream
          .filter(def -> result.getPath().endsWith(def.getName()))
          .findFirst()
          .ifPresent(
              def -> {
                value = def.getTitle();
                cfEditLink = cfPath + "?variation=" + def.getName();
              });
    }
  }

  public String getAuthorDomain() throws MalformedURLException {
    final URL cfEditURL =
        new URL(
            request.getScheme(), request.getServerName(), request.getServerPort(), "/editor.html");
    return cfEditURL.toString();
  }
}
