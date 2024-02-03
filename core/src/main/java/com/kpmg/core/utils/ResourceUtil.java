package com.kpmg.core.utils;

import com.day.cq.commons.PathInfo;
import com.day.cq.wcm.api.constants.NameConstants;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class ResourceUtil {

  private ResourceUtil() {}

  public static Stream<Resource> getChildrenAsStream(Resource resource) {
    return Optional.ofNullable(resource)
        .map(Resource::getChildren)
        .map(Iterable::spliterator)
        .map(s -> StreamSupport.stream(s, false))
        .orElseGet(Stream::empty);
  }

  public static Resource getResourceFromPathInfo(ResourceResolver resourceResolver, String path) {
    return Optional.ofNullable(path)
        .map(p -> new PathInfo(path))
        .map(PathInfo::getResourcePath)
        .map(resourceResolver::getResource)
        .orElse(null);
  }

  public static <T> List<T> mapToPropertyList(
      List<Resource> resources, String property, Class<T> type) {
    if (null == resources) {
      return Collections.emptyList();
    } else {
      return resources.stream()
          .map(Resource::getValueMap)
          .map(vm -> vm.get(property, type))
          .collect(Collectors.toList());
    }
  }

  /**
   * Recursive method to traverse the resource tree looking for a resource with a specific
   * resourceType.
   *
   * @param resource The starting resource from where to begin the traversal.
   * @param targetType The resourceType to look for.
   * @return The resource with the specified resourceType if found, otherwise null.
   */
  public static Resource findResourceWithResourceType(
      Resource resource, String targetType, Function<Resource, Boolean> shouldStop) {

    if (shouldStop.apply(resource)) { // stop condition was met, stop here, return null
      return null;
    }
    // Check if the current resource matches the target resourceType
    if (resource.isResourceType(targetType)) {
      return resource;
    }

    // Get the parent resource and traverse up the resource tree recursively
    Resource parentResource = resource.getParent();
    if (parentResource != null) {
      return findResourceWithResourceType(parentResource, targetType, shouldStop);
    }

    // If no match is found in the entire tree, return null
    return null;
  }

  public static boolean isPageResource(Resource resource) {
    return Optional.ofNullable(resource)
        .map(Resource::getValueMap)
        .map(vm -> vm.get(JcrConstants.JCR_PRIMARYTYPE, String.class))
        .map(primaryType -> primaryType.equals(NameConstants.NT_PAGE))
        .orElse(false);
  }
}
