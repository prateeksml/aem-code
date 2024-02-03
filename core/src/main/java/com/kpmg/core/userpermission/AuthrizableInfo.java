package com.kpmg.core.userpermission;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AuthrizableInfo.
 *
 * @param <E> the element type
 */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public abstract class AuthrizableInfo<E extends Authorizable> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthrizableInfo.class);

  protected static final String PROFILE_EMAIL = "profile/email";
  protected static final String PREFERENCE_DISABLE_EMAIL_NOTIFICATION =
      "preferences/disableEmailNotifications";
  private final String id;
  private String emailId = "";

  /**
   * @param authorizable
   * @throws Exception
   */
  protected AuthrizableInfo(E authorizable) throws RepositoryException {
    this.id = authorizable.getID();
    init(authorizable);
  }

  /**
   * @param authorizable
   * @throws Exception
   */
  private void init(E authorizable) {
    this.emailId = getEmailId(authorizable);
  }

  /**
   * @param authorizable
   * @return
   */
  private String getEmailId(E authorizable) {
    String email = "";
    try {
      if (authorizable.hasProperty(PROFILE_EMAIL)) {
        if (!authorizable.hasProperty(PREFERENCE_DISABLE_EMAIL_NOTIFICATION)) {
          final Value[] values = authorizable.getProperty(PROFILE_EMAIL);
          email = values[0].getString();
        } else {
          LOGGER.debug(" user is opted for Disabling Email Notifications {}.", this.id);
        }
      } else {
        LOGGER.error("email is not provided for user {} : ", this.id);
      }
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException {} while getting the email ID for user {}: ", e, this.id);
    }

    return email;
  }

  public String getId() {
    return this.id;
  }

  public String getEmail() {
    return this.emailId;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result += ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return isAuthorizable(obj);
  }

  /**
   * @param obj
   * @return
   */
  private boolean isAuthorizable(Object obj) {
    final AuthrizableInfo<?> other = (AuthrizableInfo<?>) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }
}
