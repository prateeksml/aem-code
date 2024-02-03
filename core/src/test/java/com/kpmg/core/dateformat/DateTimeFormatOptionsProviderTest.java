package com.kpmg.core.dateformat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateTimeFormatOptionsProviderTest {

  @Test
  void getDropdownOptions() {
    DateTimeFormatOptionsProvider dateTimeFormatOptionsProvider =
        new DateTimeFormatOptionsProvider();
    assertEquals(
        DateFormatHelper.getTimeFormats().size(),
        dateTimeFormatOptionsProvider.getDropdownOptions(null).size());
  }
}
