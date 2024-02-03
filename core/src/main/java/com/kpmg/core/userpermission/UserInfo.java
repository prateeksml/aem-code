package com.kpmg.core.userpermission;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import javax.jcr.RepositoryException;
import org.apache.jackrabbit.api.security.user.User;

/** The Class UserInfo. */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class UserInfo extends AuthrizableInfo<User> {
  /**
   * @param user
   * @throws RepositoryException
   * @throws Exception
   */
  public UserInfo(User user) throws RepositoryException {
    super(user);
  }

  @Override
  public String toString() {
    final String userInfo = "Id: " + "'" + getId() + "', Email '" + getEmail() + "'";
    return "[User- " + userInfo + "]";
  }
}
