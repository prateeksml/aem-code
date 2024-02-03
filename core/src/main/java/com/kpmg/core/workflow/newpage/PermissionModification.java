package com.kpmg.core.workflow.newpage;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a process step class for modifying the permission . It find the page created user name
 * and then List of group where page created by belongs.It find all groups of system. It removes all
 * group where page created belongs from system group list. There is some restricted group for which
 * permission should not modified. These fixed group . It also remove restricted group list from
 * system group. Finally it makes modify permission false for remaining system group list
 */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Modify Permission work flow"})
@ServiceDescription("A modify permission workflow step")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class PermissionModification implements WorkflowProcess {

  @Reference ResourceResolverFactory resolverFactory;

  private static final Logger LOGGER = LoggerFactory.getLogger(PermissionModification.class);

  /**
   * This method is used for modifying the page activation permission
   *
   * @param item
   * @param session
   * @param metaDataMap
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem item, final WorkflowSession workFlowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {

    LOGGER.info("Executing {} execute() method.", getClass());
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
    } catch (LoginException e) {
      LOGGER.error(
          "A Login Exception has occured while setting the hide in nav/search property", e);
    }
    LOGGER.info("Completed {} execute() method.", getClass());
  }
}
