package com.kpmg.core.utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CollectionsUtil {

  public static <T> List<T> unmodifiableListOrEmpty(List<? extends T> list) {
    return Optional.ofNullable(list)
        .map(l -> (List<T>) Collections.unmodifiableList(l))
        .orElse(Collections.emptyList());
  }
}
