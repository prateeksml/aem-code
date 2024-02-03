package com.kpmg.core.models;

import com.kpmg.core.models.impl.AbstractKPMGComponentImpl;
import com.kpmg.core.utils.ResourceUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = {SlingHttpServletRequest.class},
    resourceType = {HtmlContainer.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HtmlContainer extends AbstractKPMGComponentImpl {
  protected static final String RESOURCE_TYPE = "kpmg/components/content/htmlcontainer";
  public static final String P_CSS_PATH = "cssPath";
  public static final String P_JS_PATH = "jsPath";

  @Getter @ValueMapValue private String htmlText;

  @Getter @ChildResource private List<Resource> cssItems;

  @Getter @ChildResource private List<Resource> jsItems;

  @Self SlingHttpServletRequest request;

  @Override
  public Object getDatalayerExtension() {
    return new HtmlContainerDataLayer(htmlText, getCssPaths(), getJsPaths());
  }

  List<String> getCssPaths() {
    return ResourceUtil.mapToPropertyList(this.cssItems, P_CSS_PATH, String.class);
  }

  List<String> getJsPaths() {
    return ResourceUtil.mapToPropertyList(this.jsItems, P_JS_PATH, String.class);
  }

  @Getter
  @AllArgsConstructor
  class HtmlContainerDataLayer {
    private String htmlText;
    private List<String> cssPaths;
    private List<String> jsPaths;
  }
}
