package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMException;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
public class AbstractNavigationItemTest {

  private static final String BASE_PATH = "/navigation";
  private final AemContext context = AppAemContext.newAemContext();

  private MockNavItem navigationItem;
  private Resource resource;

  @Mock Link link = mock(Link.class);

  @BeforeEach
  void setUp() {
    navigationItem = spy(new MockNavItem());
  }

  @Test
  void testGetMainNavigationParentId() throws WCMException {

    context
        .build()
        .resource(BASE_PATH)
        .resource(BASE_PATH + "/main", "sling:resourceType", HeaderNavigationImpl.RESOURCE_TYPE)
        .resource(BASE_PATH + "/main/header", "sling:resourceType", "some/type")
        .commit();

    navigationItem.setResource(context.resourceResolver().getResource(BASE_PATH + "/main/header"));

    String mainNavigationParentId = navigationItem.getMainNavigationParentId();
    assertEquals("mainnavigation-c3758ae5e6", mainNavigationParentId);
  }

  @Test
  void testGetComponentData() {

    context
        .build()
        .resource(BASE_PATH)
        .resource(BASE_PATH + "/main", "sling:resourceType", HeaderNavigationImpl.RESOURCE_TYPE)
        .resource(BASE_PATH + "/main/header", "sling:resourceType", "some/type")
        .commit();

    navigationItem.setResource(context.resourceResolver().getResource(BASE_PATH + "/main/header"));

    String mainNavigationParentId = navigationItem.getMainNavigationParentId();
    assertEquals(
        "{\"mainnavigation-link-a667fb7e58\":{\"parentId\":\"mainnavigation-c3758ae5e6\",\"@type\":\"some/type\"}}",
        navigationItem.getComponentData().getJson());
  }

  class MockNavItem extends AbstractNavigationItem {

    void setResource(Resource resource) {
      this.resource = resource;
    }

    @Override
    protected Page getCurrentPage() {
      return super.getCurrentPage();
    }

    @Override
    public Link getLink() {
      return link;
    }
  }
}
