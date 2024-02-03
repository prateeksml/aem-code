package com.kpmg.core.workflow.utility;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;

/**
 * @author atya24 - ENUM class that defines a list of sub_services to be used to get system-user
 *     session
 */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public enum KPMGSubservicesEnum {
  WORKFLOWS_DAM("dam"),
  WORKFLOWS_CONTENT("content"),
  WORKFLOWS_ROLLOUT("rollout"),
  SCHEDULER("scheduler"),
  USER_SERVICE(JcrResourceConstants.AUTHENTICATION_INFO_SESSION);

  private final String subService;

  private KPMGSubservicesEnum(String value) {
    subService = value;
  }

  public String returnVal() {
    return subService;
  }
}
