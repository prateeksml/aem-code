package com.kpmg.core.dateformat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateFormatOptionsProviderTest {

  @Test
  void getDropdownOptions() {
    DateFormatOptionsProvider dateFormatOptionsProvider = new DateFormatOptionsProvider();
    assertEquals(
        DateFormatHelper.getAllFormats().size(),
        dateFormatOptionsProvider.getDropdownOptions(null).size());
  }
}
