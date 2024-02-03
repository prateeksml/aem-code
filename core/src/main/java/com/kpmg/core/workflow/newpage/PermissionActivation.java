package com.kpmg.core.workflow.newpage;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
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

/** This workflow process step will check activation permission. */
@Component(
    service = WorkflowProcess.class,
    property = {
      WorkflowConstants.PROCESS_LABEL + "=" + "KPMG Modify Permission Activation WorkFlow"
    })
@ServiceDescription("A modify permission activation workflow step")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class PermissionActivation implements WorkflowProcess {

  @Reference ResourceResolverFactory resolverFactory;

  private static final Logger LOGGER = LoggerFactory.getLogger(PermissionActivation.class);

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
