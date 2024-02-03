package com.kpmg.integration.models;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import com.day.cq.wcm.api.Page;
import com.kpmg.integration.services.SmartLogic;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CategorizationTabRenderCondition {

  private static final Logger LOG = LoggerFactory.getLogger(CategorizationTabRenderCondition.class);
  private static final String PAGE_PROPERTIES = "wcm/core/content/sites/properties";

  @Self public SlingHttpServletRequest request;

  @ValueMapValue private String templatePath;

  @Inject public SmartLogic smartLogicConfig;

  @SlingObject public ResourceResolver resolver;

  @PostConstruct
  void postConstruct() {
    LOG.info("Entered into postConstruct");
    if (resolver == null) {
      return;
    }
    String pagePath;
    if (StringUtils.contains(request.getPathInfo(), PAGE_PROPERTIES)) {
      pagePath = request.getParameter("item");
    } else {
      pagePath =
          request
              .getAttribute("granite.ui.form.contentpath")
              .toString()
              .replace("/jcr:content", StringUtils.EMPTY);
    }
    LOG.debug("page {} ", pagePath);
    Resource resource = resolver.getResource(pagePath);
    try {
      if (resource != null) {
        Page currentPage = resource.adaptTo(Page.class);
        String template = currentPage.getTemplate().getPath();
        LOG.debug(template);
        LOG.info("render : {}", isValid(template));
        request.setAttribute(
            RenderCondition.class.getName(), new SimpleRenderCondition(isValid(template)));
      }
    } catch (RuntimeException e) {
      LOG.error("Exception occured {}", e.getMessage(), e);
    }
  }

  public boolean isValid(String template) {
    String[] disabledTemplates = smartLogicConfig.getDisabledTemplates();
    LOG.debug("disabled templates : {}", Arrays.asList(disabledTemplates));
    return !Arrays.asList(disabledTemplates).contains(template);
  }
}
