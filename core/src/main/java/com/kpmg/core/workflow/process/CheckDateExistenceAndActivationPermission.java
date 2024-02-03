package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.userpermission.GroupInfo;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.jcr.Session;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class makes a null check on the start and expiry date of the page */
@Component(
    service = WorkflowProcess.class,
    property = {
      WorkflowConstants.PROCESS_LABEL
          + "="
          + "KPMG Check Start&Expiry date existance / activation permission"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class CheckDateExistenceAndActivationPermission implements WorkflowProcess {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CheckDateExistenceAndActivationPermission.class);

  @Reference private UserGroupActions userGroupActions;

  @Reference ResourceResolverFactory resolverFactory;

  @Reference Replicator replicator;

  private static final List<String> TEMPLATE_LIST =
      Collections.unmodifiableList(new ArrayList<>(Arrays.asList("html-template")));

  /**
   * This method makes a null check on the start and expiry date of the page
   *
   * @param WorkItem
   * @param WorkflowSession
   * @param MetaDataMap
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {

    final String resourceType = null;
    try (ResourceResolver resolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Session session = resolver.adaptTo(Session.class);
      final String payload = workItem.getContentPath();
      final String auditResourcePath =
          WorkflowConstants.AUDIT_LOG_PATH
              + WorkflowConstants.SLASH
              + WorkflowConstants.AUDIT_LOG_REPLICATION
              + payload;
      ResourceUtil.getOrCreateResource(
          resolver, auditResourcePath, resourceType, resourceType, true);
      final Page payloadPage = WorkflowUtility.getPayloadPage(resolver, payload);
      final boolean dateExists = payloadPage.getOffTime() != null;
      final boolean isMemberFirmUser = isMemberFirmUser(workItem);
      final boolean isActivated =
          WorkflowUtility.isActivated(replicator, payloadPage.getPath(), session);
      final boolean isHTMLPage = isHTMLPage(payloadPage);
      final List<Route> routes = workflowSession.getRoutes(workItem, false);
      if (!dateExists) {
        workflowSession.complete(workItem, routes.get(0));
      } else if (isHTMLPage) {
        if (isMemberFirmUser && !isActivated) {
          workflowSession.complete(workItem, routes.get(0));
        } else {
          workflowSession.complete(workItem, routes.get(1));
        }
      } else {
        workflowSession.complete(workItem, routes.get(1));
      }
    } catch (LoginException | PersistenceException e) {
      LOGGER.error(
          "A Exception has occured while running CheckDateExistenceAndActivationPermission workflow ",
          e);
    }
  }

  /**
   * Checks if is HTML page.
   *
   * @param page the page
   * @return true, if is HTML page
   */
  private boolean isHTMLPage(final Page page) {
    return TEMPLATE_LIST.contains(page.getTemplate().getName());
  }

  /**
   * Checks if is member firm user.
   *
   * @param workItem the work item
   * @return true, if is member firm user
   */
  private boolean isMemberFirmUser(final WorkItem workItem) {
    final Set<GroupInfo> groupSet =
        userGroupActions.getGroupsForUser(workItem.getWorkflow().getInitiator(), true);
    final Iterator<GroupInfo> groupIterator = groupSet.iterator();
    while (groupIterator.hasNext()) {
      final GroupInfo group = groupIterator.next();
      if (WorkflowConstants.BLUEPRINT_CONTRIBUTORS.equals(group.getId())) {
        return true;
      }
    }
    return false;
  }
}
