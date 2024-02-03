package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.models.Component;
import java.util.List;

public interface TextList extends Component {

  List<TextListItem> getListItems();

  String getListType();
}
