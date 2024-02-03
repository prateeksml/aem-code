package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.EmailTemplates;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.Calendar;
import java.util.UUID;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Custom Audit Log Process"})
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class CustomAuditLogProcess implements WorkflowProcess {

  private static final Logger LOG = LoggerFactory.getLogger(CustomAuditLogProcess.class);

  @Reference ResourceResolverFactory resolverFactory;

  @Reference EmailTemplates config;

  /**
   * This class will add a custom audit log for page replication user. The user ID for the
   * replication process will be set as workflow initiator
   *
   * @param workItem
   * @param session
   * @param metaData
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession wfsession, final MetaDataMap metaData)
      throws WorkflowException {
    final String resourceType = null;
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final PageManager pageMgr = resourceResolver.adaptTo(PageManager.class);
      final String payloadPath = workItem.getContentPath();
      final Page page = pageMgr.getPage(payloadPath);
      final String lastReplicationAction =
          page.getProperties()
              .get(ReplicationStatus.NODE_PROPERTY_LAST_REPLICATION_ACTION, StringUtils.EMPTY);
      final String auditResourcePath =
          WorkflowConstants.AUDIT_LOG_PATH
              + WorkflowConstants.SLASH
              + WorkflowConstants.AUDIT_LOG_REPLICATION
              + payloadPath;
      final Resource resource =
          ResourceUtil.getOrCreateResource(
              resourceResolver, auditResourcePath, resourceType, resourceType, true);
      final Node parentNode = resource.adaptTo(Node.class);
      if (parentNode != null) {
        final Session session = resourceResolver.adaptTo(Session.class);
        final Node auditNode = parentNode.addNode(UUID.randomUUID().toString(), "cq:AuditEvent");
        auditNode.setProperty("cq:time", Calendar.getInstance());
        auditNode.setProperty("cq:userid", workItem.getWorkflow().getInitiator());
        auditNode.setProperty("cq:path", payloadPath);
        auditNode.setProperty("cq:type", lastReplicationAction);
        auditNode.setProperty("cq:category", "com/day/cq/replication");
        if (session.hasPendingChanges()) {
          session.save();
        }
      }
    } catch (LoginException | PersistenceException e) {
      LOG.error("A Exception has occured while setting the audit node", e);
    } catch (RepositoryException e) {
      LOG.error("A RepositoryException has occured", e);
    }
  }
}
