package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import java.util.Calendar;
import java.util.List;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class is used to check scheduled off time. */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Schedule Off Time Check Process"})
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class ScheduleOffTimeCheckProcess implements WorkflowProcess {

  private static final Logger LOG = LoggerFactory.getLogger(ScheduleOffTimeCheckProcess.class);
  @Reference private ResourceResolverFactory resolverFactory;

  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Page payloadPage = WorkflowUtility.getPayloadPage(resolver, workItem.getContentPath());
      final Calendar scheduleOffTime = payloadPage.getOffTime();
      final Calendar scheduleOnTime =
          payloadPage.getOnTime() != null ? payloadPage.getOnTime() : Calendar.getInstance();
      final List<Route> routes = workflowSession.getRoutes(workItem, false);
      if (null != scheduleOffTime
          && scheduleOffTime.compareTo(Calendar.getInstance()) > 0
          && Calendar.getInstance().compareTo(scheduleOnTime) >= 0) {
        workflowSession.complete(workItem, routes.get(1));
      } else if (null != scheduleOnTime
          && Calendar.getInstance().compareTo(scheduleOnTime) < 0
          && scheduleOffTime != null
          && scheduleOffTime.compareTo(Calendar.getInstance()) > 0) {
        workflowSession.complete(workItem, routes.get(2));
      } else {
        workflowSession.complete(workItem, routes.get(0));
      }
    } catch (LoginException e) {
      LOG.error("LoginException occured while reading the payload node", e);
    }
  }
}
