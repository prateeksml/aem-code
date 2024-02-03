package com.kpmg.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class CollectionsUtilTest {

  @Test
  void unmodifiableListOrEmpty() {
    // test null
    assertEquals(Collections.emptyList(), CollectionsUtil.unmodifiableListOrEmpty(null));

    // test non null
    String item1 = "item1";
    List<String> list = new ArrayList<>();
    list.add(item1);

    List<String> unmodifiable = CollectionsUtil.unmodifiableListOrEmpty(list);
    assertEquals(list, unmodifiable);

    // error when trying to modify the list
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          unmodifiable.set(0, "new item 1");
        });
  }
}
