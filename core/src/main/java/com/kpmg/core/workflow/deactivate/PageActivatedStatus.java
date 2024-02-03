package com.kpmg.core.workflow.deactivate;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.Replicator;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import java.util.List;
import javax.jcr.Session;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will check the activation status of AEM page.
 *
 * @author kmuth1
 */
@Component(
    service = WorkflowProcess.class,
    property = {
      WorkflowConstants.PROCESS_LABEL + "=" + "KPMG Page Activation Status Identification"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class PageActivatedStatus implements WorkflowProcess {

  private static final Logger LOG = LoggerFactory.getLogger(PageActivatedStatus.class);
  @Reference private ResourceResolverFactory resolverFactory;

  @Reference Replicator replicator;

  /**
   * The method identifies the activation status of a AEM page.
   *
   * @param workItem
   * @param session
   * @param metaData
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession session, final MetaDataMap metaData)
      throws WorkflowException {
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Session jcrSession = resourceResolver.adaptTo(Session.class);
      final List<Route> routes = session.getRoutes(workItem, false);
      final boolean activated =
          WorkflowUtility.isActivated(replicator, workItem.getContentPath(), jcrSession);
      if (activated) {
        session.complete(workItem, routes.get(0));
      } else {
        session.complete(workItem, routes.get(1));
      }
    } catch (LoginException e) {
      LOG.error("An error occured while getting the resolver", e);
    }
  }
}
