package com.kpmg.core.userpermission.internal;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.userpermission.GroupInfo;
import com.kpmg.core.userpermission.UserGroupActions;
import com.kpmg.core.userpermission.UserInfo;
import com.kpmg.core.utils.ResourceResolverUtility;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.jcr.RepositoryException;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class UserGroupActionImpl. */
@Component(service = UserGroupActions.class)
@ServiceDescription("KPMG - User Group Action Service")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class UserGroupActionImpl implements UserGroupActions {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupActionImpl.class);

  @Reference private ResourceResolverFactory resolverFactory;

  @Override
  public Set<UserInfo> getUsersForGroup(final String groupId) {
    return getUsersForGroup(groupId, false);
  }

  /**
   * This method will fetch the set of users in a group on the basis of group id.
   *
   * @param groupId
   * @param allOrDirectUsers
   * @return users
   */
  @Override
  public Set<UserInfo> getUsersForGroup(final String groupId, final boolean allOrDirectUsers) {
    LOGGER.debug("Start of getUsersForGroup method of UserGroupActionImpl");
    final Set<UserInfo> users = new HashSet<>();
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver =
          ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory);
      final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
      if (userManager != null) {
        final Authorizable groupAuthrizable = userManager.getAuthorizable(groupId);
        if (null != groupAuthrizable && !"everyone".equals(groupAuthrizable.getID())) {
          populateUsers(users, groupAuthrizable, allOrDirectUsers);
        } else {
          LOGGER.warn("Group does not exist with ID: {}", groupId);
        }
      }
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException in UserGroupActionImpl : ", e);
    } catch (LoginException e) {
      LOGGER.error("LoginException in UserGroupActionImpl : ", e);
    } finally {
      ResourceResolverUtility.closeServiceResourceResolver(resourceResolver);
    }
    return users;
  }

  @Override
  public <E> Set<UserInfo> getUsersForGroups(final Set<E> groupIds, final Class<E> clazz) {
    return getUsersForGroups(groupIds, clazz, false);
  }

  /**
   * This method will fetch the set of users for a set of groups.
   *
   * @param groupIds
   * @param clazz
   * @param allOrDirectUsers
   * @return users
   */
  @Override
  public <E> Set<UserInfo> getUsersForGroups(
      final Set<E> groupIds, Class<E> clazz, final boolean allOrDirectUsers) {
    LOGGER.debug("Start of getUsersForGroups method of UserGroupActionImpl");
    final Set<UserInfo> users = new HashSet<>();
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver =
          ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory);
      final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
      for (E group : groupIds) {
        String groupId = "";
        if (String.class.equals(clazz)) {
          groupId = String.class.cast(group);
        } else if (GroupInfo.class.equals(clazz)) {
          final GroupInfo groupInfo = GroupInfo.class.cast(group);
          groupId = groupInfo.getId();
        } else {
          throw new IllegalArgumentException("Class type is not supported for : " + clazz);
        }
        if (userManager != null) {
          final Authorizable groupAuthrizable = userManager.getAuthorizable(groupId);
          if (null != groupAuthrizable) {
            populateUsers(users, groupAuthrizable, allOrDirectUsers);
          } else {
            LOGGER.warn("Group does not exist");
          }
        }
      }
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException in UserGroupActionImpl : ", e);
    } catch (LoginException e) {
      LOGGER.error("LoginException in UserGroupActionImpl : ", e);
    } finally {
      ResourceResolverUtility.closeServiceResourceResolver(resourceResolver);
    }
    return users;
  }

  @Override
  public Set<GroupInfo> getGroupsForUser(String userId) {
    return getGroupsForUser(userId, false);
  }

  /**
   * This method will return the set of user groups which a user belongs to based on the user id..
   *
   * @param userId
   * @param allOrDirectGroups
   * @return groups
   */
  @Override
  public Set<GroupInfo> getGroupsForUser(final String userId, final boolean allOrDirectGroups) {
    LOGGER.debug("Start of getGroupsForUser method of UserGroupActionImpl");
    final Set<GroupInfo> groups = new HashSet<>();
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver =
          ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory);
      final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
      if (userManager != null) {
        final Authorizable userAuthorizable = userManager.getAuthorizable(userId);
        final Iterator<Group> groupIterator =
            allOrDirectGroups ? userAuthorizable.memberOf() : userAuthorizable.declaredMemberOf();
        while (groupIterator.hasNext()) {
          final Group group = groupIterator.next();
          if ("everyone".equals(group.getID())) {
            continue;
          }
          groups.add(new GroupInfo(group));
        }
      }
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException in UserGroupActionImpl : ", e);
    } catch (LoginException e) {
      LOGGER.error("LoginException in UserGroupActionImpl : ", e);
    } finally {
      ResourceResolverUtility.closeServiceResourceResolver(resourceResolver);
    }
    return groups;
  }

  /**
   * This method will fetch the email id for a given user.
   *
   * @param id
   * @return emailId
   */
  @Override
  public String getEmail(final String id) {
    LOGGER.debug("Start of getEmail method of UserGroupActionImpl");
    String emailId = "";
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver =
          ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory);
      final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
      emailId = getEmailId(id, userManager);
    } catch (LoginException e) {
      LOGGER.error("LoginException in UserGroupActionImpl : ", e);
    } finally {
      ResourceResolverUtility.closeServiceResourceResolver(resourceResolver);
    }
    return emailId;
  }

  /**
   * This method will return a set of email id's.
   *
   * @param ids
   * @param clazz
   * @return emailIds
   */
  @Override
  public <E> Set<String> getEmailIds(final Set<E> ids, final Class<E> clazz) {
    LOGGER.debug("Start of getEmailIds method of UserGroupActionImpl");
    final Set<String> emailIds = new HashSet<>();
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver =
          ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory);
      final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
      ids.forEach(
          id -> {
            String emailId = "";
            if (String.class.equals(clazz)) {
              final String idObject = String.class.cast(id);
              emailId = getEmailId(idObject, userManager);
            } else if (GroupInfo.class.equals(clazz)) {
              final GroupInfo groupInfo = GroupInfo.class.cast(id);
              emailId = groupInfo.getEmail();
            } else if (UserInfo.class.equals(clazz)) {
              final UserInfo userInfo = UserInfo.class.cast(id);
              emailId = userInfo.getEmail();
            } else {
              LOGGER.error("Class type is not supported for {}", clazz);
            }

            if (StringUtils.isNotEmpty(emailId)) {
              emailIds.add(emailId);
            }
          });
    } catch (LoginException e) {
      LOGGER.error("LoginException in UserGroupActionImpl : ", e);
    } finally {
      ResourceResolverUtility.closeServiceResourceResolver(resourceResolver);
    }
    return emailIds;
  }

  /**
   * @param users
   * @param group
   * @param allOrDirectUsers
   */
  private void populateUsers(
      final Set<UserInfo> users, final Authorizable group, final boolean allOrDirectUsers) {
    LOGGER.debug("Start of populateUsers method of UserGroupActionImpl");
    Iterator<Authorizable> groupAuthrizableIterator = null;
    try {
      if (group.isGroup()) {
        groupAuthrizableIterator =
            allOrDirectUsers ? ((Group) group).getMembers() : ((Group) group).getDeclaredMembers();
        while (groupAuthrizableIterator.hasNext()) {
          final Authorizable authorizable = groupAuthrizableIterator.next();
          if (authorizable.isGroup()) {
            continue;
          } else {
            users.add(new UserInfo((User) authorizable));
          }
        }
      } else {
        LOGGER.error("illegal argument {} is not a group.", group.getID());
      }
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException in populateUsers : ", e);
    }
  }

  /**
   * This method will fetch the email id for a given user.
   *
   * @param id
   * @param userManager
   * @return
   */
  private String getEmailId(final String id, final UserManager userManager) {
    LOGGER.debug("Start of populateUsers method of UserGroupActionImpl");
    String emailId = "";
    try {
      final Authorizable authorizable = userManager.getAuthorizable(id);
      if (authorizable.isGroup()) {
        emailId = new GroupInfo((Group) authorizable).getEmail();
      } else {
        emailId = new UserInfo((User) authorizable).getEmail();
      }
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException {} in getEmailId for id {} : ", e, id);
    }
    LOGGER.debug("Exiting populateUsers method of UserGroupActionImpl");
    return emailId;
  }
}
