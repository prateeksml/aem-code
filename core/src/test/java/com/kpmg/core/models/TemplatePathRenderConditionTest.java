package com.kpmg.core.models;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.acs.commons.synth.children.impl.JSONModifiableValueMapDecorator;
import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.foundation.model.responsivegrid.ResponsiveGrid;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.wrappers.ResourceResolverWrapper;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class TemplatePathRenderConditionTest {
  @Mock private ResourceResolver resourceResolver;
  @Mock private Resource resource;
  @Mock private Page page;
  @Mock private PageManager pm;
  @Mock private Template template;
  @Mock private MockSlingHttpServletRequest request;
  @Mock private ModifiableValueMap valueMap;

  private TemplatePathRenderCondition render;
  public static final String CONTENTPATH_ATTRIBUTE = "granite.ui.form.contentpath";
  String tempalate = "/conf/kpmg/settings/wcm/templates/page-article";
  String path = "/content/kpmgpublic/us/en/test-article";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(resourceResolver.getResource(anyString())).thenReturn(mock(Resource.class));
    when(resource.getPath()).thenReturn("Path");
    when(resource.getValueMap()).thenReturn(new JSONModifiableValueMapDecorator());
    when(resourceResolver.getResource(Mockito.<String>any())).thenReturn(new ResponsiveGrid());
    when(request.getResourceResolver()).thenReturn(new ResourceResolverWrapper(resourceResolver));
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pm);
    when((String) request.getAttribute(CONTENTPATH_ATTRIBUTE)).thenReturn("test");
    when(pm.getContainingPage(resource)).thenReturn(page);
    when(resourceResolver.getResource("test")).thenReturn(resource);
    request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(true));
    when(page.getTemplate()).thenReturn(template);
    when(template.getPath()).thenReturn("/conf/kpmg/settings/wcm/templates/page-article");
    render = new TemplatePathRenderCondition();
    render.request = request;
    render.resolver = resourceResolver;

    render.postConstruct();
  }

  @Test
  void testPostConstruct() {
    render.postConstruct();
  }
}
