/** */
package com.kpmg.core.workflow.utility;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;

/**
 * @author atya24 - ENUM class that defines a list of system users to be used in authInfo to get
 *     ResourceResolvers
 */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public enum KPMGSystemUserEnum {
  WORKFLOW_ADMIN("workflow-admin");

  private final String system_user;

  private KPMGSystemUserEnum(String value) {
    system_user = value;
  }

  public String returnVal() {
    return system_user;
  }
}
