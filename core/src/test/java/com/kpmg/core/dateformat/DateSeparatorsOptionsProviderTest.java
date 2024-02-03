package com.kpmg.core.dateformat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateSeparatorsOptionsProviderTest {

  @Test
  void getDropdownOptions() {
    DateSeparatorsOptionsProvider dateSeparatorsOptionsProvider =
        new DateSeparatorsOptionsProvider();
    assertEquals(
        DateFormatHelper.getAllSeparators().size(),
        dateSeparatorsOptionsProvider.getDropdownOptions(null).size());
  }
}
