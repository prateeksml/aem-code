package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.api.components.ComponentContext;
import com.kpmg.core.testhelpers.DataLayerTestHelper;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractComponentImplTest {

  @Mock Resource resource;
  @Mock ComponentContext componentContext;

  void setup() {

    Page page = mock(Page.class);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getPath()).thenReturn("/content/kpmg/us/en/home");
    when(componentContext.getPage()).thenReturn(page);
  }

  @Test
  void test() throws WCMException {
    DataLayerTestHelper.mockDatalayerEnabled(resource, true);
    Component component = new Component(resource, componentContext);
    assertNotNull(component.getData());
  }

  @Test
  void testNull() {
    DataLayerTestHelper.mockDatalayerEnabled(resource, false);
    Component component = new Component(resource, componentContext);
    assertNull(component.getData());
  }

  class Component extends AbstractKPMGComponentImpl {
    public Component(Resource resource, ComponentContext componentContext) {
      this.resource = resource;
      this.componentContext = componentContext;
    }
  }
}
