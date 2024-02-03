package com.kpmg.core.workflow.deactivate;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.constants.NameConstants;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import com.kpmg.core.workflow.utility.WorkflowUtility;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will set the schedule off time as current time when the page deactivation is
 * completed.
 */
@Component(
    service = WorkflowProcess.class,
    property = {
      WorkflowConstants.PROCESS_LABEL
          + "="
          + "KPMG Schedule Off Time process for deactivation workflow"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class ScheduleOffTimeForDeactivation implements WorkflowProcess {

  private static final Logger LOG = LoggerFactory.getLogger(ScheduleOffTimeForDeactivation.class);

  private static final String ARGS_GETOFFTIME = "getofftime";
  private static final String ARGS_SETOFFTIME = "setofftime";

  @Reference private ResourceResolverFactory resolverFactory;

  /**
   * The method compares the schedule off time of an AEM page with current time and selects the
   * workflow route accordingly. This will also set the schedule off time to current time for the
   * page that is deactivated.
   *
   * @param workItem
   * @param workflowSession
   * @param metaDataMap
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Page payloadPage = WorkflowUtility.getPayloadPage(resolver, workItem.getContentPath());
      final Calendar scheduleOffTime = payloadPage.getOffTime();
      final Calendar currentTime = Calendar.getInstance();
      final String timeType =
          StringUtils.isNotBlank(metaDataMap.get(WorkflowConstants.PROCESS_ARGS, String.class))
              ? metaDataMap.get(WorkflowConstants.PROCESS_ARGS, String.class)
              : StringUtils.EMPTY;
      if (StringUtils.equalsIgnoreCase(ARGS_GETOFFTIME, timeType)) {
        final List<Route> routes = workflowSession.getRoutes(workItem, false);
        if (null != scheduleOffTime && scheduleOffTime.compareTo(currentTime) > 0) {
          workflowSession.complete(workItem, routes.get(0));
        } else {
          workflowSession.complete(workItem, routes.get(1));
        }
      } else if (StringUtils.equalsIgnoreCase(ARGS_SETOFFTIME, timeType)) {
        final Resource pageContentResource = payloadPage.getContentResource();
        final ModifiableValueMap pageProperties =
            pageContentResource.adaptTo(ModifiableValueMap.class);
        pageProperties.put(NameConstants.PN_OFF_TIME, currentTime);
        resolver.commit();
      }
      LOG.debug("Execution of execute() method completed");
    } catch (LoginException e) {
      LOG.error("LoginException in ScheduleOffTime for deactivation workflow : ", e);
    } catch (PersistenceException e) {
      LOG.error(
          "Exception in ScheduleOffTime for deactivation workflow while setting the offtime to current time : ",
          e);
    }
  }
}
