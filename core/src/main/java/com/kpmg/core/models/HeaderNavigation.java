package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.models.Component;
import java.util.List;

public interface HeaderNavigation extends Component {
  List<MainNavigationItem> getItems();
}
