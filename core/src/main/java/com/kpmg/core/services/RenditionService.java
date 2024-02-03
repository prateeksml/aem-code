package com.kpmg.core.services;

import java.util.Map;
import org.apache.sling.api.resource.ResourceResolver;

/** Generates renditions */
public interface RenditionService {

  /**
   * Returns scaled image source links as per the view ports.
   *
   * @param componentName identifier for the component where the image is used
   * @param imagePath Image Path
   * @param imagePlacement Image Placement/Aspect Ratio
   * @param domain Dynamic Media domain
   * @param resolver Resource Resolver
   * @param isAuthor {@code true} if author instance * @param cropImage {@code true} if crop param
   *     is needed
   * @return map with image sources by viewport
   */
  Map<String, String> getRenditions(
      final String componentName,
      final String imagePath,
      final String imagePlacement,
      final ResourceResolver resolver);
}
