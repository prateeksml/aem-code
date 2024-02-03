package com.kpmg.core.utils;

import com.day.cq.wcm.api.constants.NameConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ResourceUtilTest {

  @Test
  void findResourceWithResourceType() {}

  @Test
  void isPageResource() {
    Resource resource =
        new MockResource(
            "/test", Map.of(JcrConstants.JCR_PRIMARYTYPE, NameConstants.NT_PAGE), null);
    Assertions.assertTrue(ResourceUtil.isPageResource(resource));
    resource = new MockResource("/test", Map.of(JcrConstants.JCR_PRIMARYTYPE, "other"), null);
    Assertions.assertFalse(ResourceUtil.isPageResource(resource));
  }

  @Test
  void mapToPropertyList() {
    String property = "property";
    List<Resource> resources = new ArrayList<>();
    resources.add(new MockResource("/test", Map.of(property, "value"), null));
    resources.add(new MockResource("/test", Map.of(property, "value1"), null));
    List<String> expected = List.of("value", "value1");
    Assertions.assertEquals(
        expected, ResourceUtil.mapToPropertyList(resources, property, String.class));
    Assertions.assertEquals(
        Collections.emptyList(), ResourceUtil.mapToPropertyList(null, property, String.class));
  }

  @Test
  void getChildrenAsStream() {
    Resource resource = Mockito.mock(Resource.class);
    ResourceUtil.getChildrenAsStream(resource);
    Mockito.verify(resource, Mockito.times(1)).getChildren();
  }

  @Test
  void getResourceFromPathInfo() {
    String expectedPath = "/my/path";
    Resource expectedResource = Mockito.mock(Resource.class);
    ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
    Mockito.doReturn(expectedResource).when(resourceResolver).getResource(expectedPath);
    Assertions.assertEquals(
        expectedResource,
        ResourceUtil.getResourceFromPathInfo(
            resourceResolver, expectedPath + ".one.html/another/path"));
  }
}
