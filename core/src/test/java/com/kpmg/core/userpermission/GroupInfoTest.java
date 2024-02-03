package com.kpmg.core.userpermission;

import static org.junit.jupiter.api.Assertions.*;

import javax.jcr.RepositoryException;
import org.apache.jackrabbit.api.security.user.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupInfoTest {

  @Mock Group group;

  @Test
  void testToString() throws RepositoryException {
    GroupInfo groupInfo = new GroupInfo(group);
    assertEquals("[Group- Id: 'null', Email '']", groupInfo.toString());
  }
}
