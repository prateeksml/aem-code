package com.kpmg.core.userpermission;

import static org.junit.jupiter.api.Assertions.*;

import javax.jcr.RepositoryException;
import org.apache.jackrabbit.api.security.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserInfoTest {

  @Mock User user;

  @Test
  void testToString() throws RepositoryException {
    UserInfo userInfo = new UserInfo(user);
    assertEquals("[User- Id: 'null', Email '']", userInfo.toString());
  }
}
