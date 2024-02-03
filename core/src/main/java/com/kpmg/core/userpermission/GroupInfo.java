/**
 * This class is used to
 *
 * @author Pawan Mittal
 */
package com.kpmg.core.userpermission;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import javax.jcr.RepositoryException;
import org.apache.jackrabbit.api.security.user.Group;

/** The Class GroupInfo. */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class GroupInfo extends AuthrizableInfo<Group> {
  /**
   * @param group
   * @throws RepositoryException
   * @throws Exception
   */
  public GroupInfo(Group group) throws RepositoryException {
    super(group);
  }

  @Override
  public String toString() {
    final String groupInfo = "Id: " + "'" + getId() + "', Email '" + getEmail() + "'";
    return "[Group- " + groupInfo + "]";
  }
}
