package com.kpmg.core.models;

import java.util.List;

public interface NavigationItemWithChildren<T> extends NavigationItem {
  List<T> getItems();
}
