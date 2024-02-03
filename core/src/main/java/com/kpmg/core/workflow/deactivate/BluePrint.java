package com.kpmg.core.workflow.deactivate;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.userpermission.MSMUserAccess;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.List;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

/** This class is used to check blue print copy status. */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Check blue print copy process"})
@ServiceDescription("A blue print workflow step")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class BluePrint implements WorkflowProcess {

  @Reference MSMUserAccess msmUserAccess;

  private static final String TYPE_JCR_PATH = "JCR_PATH";

  /**
   * Execute.
   *
   * @param item the item
   * @param session the session
   * @param metaDataMap the meta data map
   * @throws WorkflowException the workflow exception
   */
  @Override
  public void execute(
      final WorkItem item, final WorkflowSession session, final MetaDataMap metaDataMap)
      throws WorkflowException {
    final WorkflowData workflowData = item.getWorkflowData();
    if (!workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
      return;
    }
    final List<Route> routes = session.getRoutes(item, false);
    final int routeSelected = msmUserAccess.isBlueprint(item.getContentPath()) ? 0 : 1;
    session.complete(item, routes.get(routeSelected));
  }
}
