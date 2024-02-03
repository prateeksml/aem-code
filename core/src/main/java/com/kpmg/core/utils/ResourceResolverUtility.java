package com.kpmg.core.utils;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import java.util.Collections;
import javax.jcr.Session;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;

/**
 * Utility to instantiate resource resolvers and other convenience methods around Sling resource
 * resolution.
 */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public final class ResourceResolverUtility {

  private ResourceResolverUtility() {}

  /**
   * Get resource resolver from the session
   *
   * @param resourceResolverFactory
   * @param session
   * @return the resource resolver
   * @throws LoginException
   */
  public static ResourceResolver getResourceResolver(
      final Session session, final ResourceResolverFactory resourceResolverFactory)
      throws LoginException {
    return resourceResolverFactory.getResourceResolver(
        Collections.<String, Object>singletonMap(
            JcrResourceConstants.AUTHENTICATION_INFO_SESSION, session));
  }

  /**
   * This method is responsible for getting the resource resolver.
   *
   * @param subServiceName {@link String} the sub service name
   * @return the service resolver {@link ResourceResolver}
   * @throws LoginException {@link LoginException} the login exception
   */
  public static ResourceResolver getServiceResourceResolver(
      final String subServiceName, final ResourceResolverFactory resourceResolverFactory)
      throws LoginException {
    return resourceResolverFactory.getServiceResourceResolver(
        Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, subServiceName));
  }

  /**
   * This method is responsible for closing the active resource resolver.
   *
   * @param resourceResolver {@link ResourceResolver} the resource resolver
   */
  public static void closeServiceResourceResolver(final ResourceResolver resourceResolver) {

    if (resourceResolver != null && resourceResolver.isLive()) {
      resourceResolver.close();
    }
  }
}
