package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class HeaderNavigationImplTest {

  @Test
  void getItems() {
    MainNavigationItemImpl item1 = new MainNavigationItemImpl();
    MainNavigationItemImpl item2 = new MainNavigationItemImpl();
    MainNavigationItemImpl item3 = new MainNavigationItemImpl();
    MainNavigationItemImpl item4 = new MainNavigationItemImpl();
    MainNavigationItemImpl item5 = new MainNavigationItemImpl();
    MainNavigationItemImpl item6 = new MainNavigationItemImpl();

    HeaderNavigationImpl header = new HeaderNavigationImpl();
    header.nav1 = item1;
    header.nav2 = item2;
    header.nav3 = item3;
    header.nav4 = item4;
    header.nav5 = item5;
    header.nav6 = item6;

    assertEquals(List.of(item1, item2, item3, item4, item5, item6), header.getItems());
  }
}
