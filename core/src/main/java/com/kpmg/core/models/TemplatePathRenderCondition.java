package com.kpmg.core.models;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TemplatePathRenderCondition {

  public static final String CONTENTPATH_ATTRIBUTE = "granite.ui.form.contentpath";
  @Self public SlingHttpServletRequest request;

  @ValueMapValue private String templatePath;

  @SlingObject public ResourceResolver resolver;

  @PostConstruct
  void postConstruct() {
    boolean isArticleTemplate = false;
    String contentPath = (String) request.getAttribute(CONTENTPATH_ATTRIBUTE);
    if (resolver == null) {
      return;
    }
    PageManager pageManger = resolver.adaptTo(PageManager.class);
    Resource currentResource = resolver.getResource(contentPath);
    Page currentPage = pageManger.getContainingPage(currentResource);
    isArticleTemplate = currentPage.getTemplate().getPath().equals(templatePath);

    request.setAttribute(
        RenderCondition.class.getName(), new SimpleRenderCondition(isArticleTemplate));
  }
}
