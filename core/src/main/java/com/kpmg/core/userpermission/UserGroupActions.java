package com.kpmg.core.userpermission;

import java.util.Set;

public interface UserGroupActions {
  /**
   * @param groupId
   * @return
   */
  public Set<UserInfo> getUsersForGroup(final String groupId);

  /**
   * @param groupId
   * @param allOrDirectUsers
   * @return
   */
  public Set<UserInfo> getUsersForGroup(final String groupId, final boolean allOrDirectUsers);

  /**
   * @param groupIds
   * @param clazz
   * @return
   */
  public <E> Set<UserInfo> getUsersForGroups(final Set<E> groupIds, final Class<E> clazz);

  /**
   * @param groupIds
   * @param clazz
   * @param allOrDirectUsers
   * @return
   */
  public <E> Set<UserInfo> getUsersForGroups(
      final Set<E> groupIds, Class<E> clazz, final boolean allOrDirectUsers);

  /**
   * @param userId
   * @return
   */
  public Set<GroupInfo> getGroupsForUser(final String userId);

  /**
   * @param userId
   * @param allOrDirectGroups
   * @return
   */
  public Set<GroupInfo> getGroupsForUser(final String userId, final boolean allOrDirectGroups);

  /**
   * @param id
   * @return
   */
  public String getEmail(final String id);

  /**
   * @param ids
   * @param clazz
   * @return
   */
  public <E> Set<String> getEmailIds(final Set<E> ids, final Class<E> clazz);
}
