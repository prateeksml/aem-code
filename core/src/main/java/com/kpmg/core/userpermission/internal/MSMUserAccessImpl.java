package com.kpmg.core.userpermission.internal;

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.BlueprintManager;
import com.day.cq.wcm.msm.api.BlueprintManagerFactory;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.userpermission.MSMUserAccess;
import com.kpmg.core.utils.ResourceResolverUtility;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class MSMUserAccessImpl. */
@Component(immediate = true, service = MSMUserAccess.class)
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class MSMUserAccessImpl implements MSMUserAccess {
  @Reference private LiveRelationshipManager relationshipManager;

  @Reference private ResourceResolverFactory resolverFactory;

  @Reference private BlueprintManagerFactory blueprintManagerFactory;

  public static final int ABSOLUTE_PARENT_OF = 2;

  public static final int NUMBER_OF_GROUPS = 5;

  public static final String USERPERMISSIONS = "userpermissions";

  private static final Logger LOG = LoggerFactory.getLogger(MSMUserAccessImpl.class);

  /**
   * This method is used to identify a blueprint page.
   *
   * @param pagePath
   * @return
   */
  @Override
  public boolean isBlueprint(final String pagePath) {
    LOG.debug("Start of isBlueprint method of MSMUserAccessImpl");
    boolean isBluePrint = false;
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver =
          ResourceResolverUtility.getServiceResourceResolver(USERPERMISSIONS, resolverFactory);
      final Resource objResource = resourceResolver.getResource(pagePath);
      final BlueprintManager objBluePrintManger =
          this.blueprintManagerFactory.getBlueprintManager(resourceResolver);
      isBluePrint =
          objResource != null && objBluePrintManger.getContainingBlueprint(pagePath) != null;
    } catch (WCMException | LoginException e) {
      LOG.error("An exception{} occured while checking blueprint {}", e, pagePath);
    } finally {
      ResourceResolverUtility.closeServiceResourceResolver(resourceResolver);
    }
    return isBluePrint;
  }
}
