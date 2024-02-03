package com.kpmg.core.dateformat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kpmg.core.caconfig.DateFormatConfig;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class DateFormatServiceTest {

  @Mock SlingHttpServletRequest request;

  @Mock ConfigurationResolver configurationResolver;

  @Mock ConfigurationBuilder configurationBuilder;

  @Mock Resource resource;

  @Mock DateFormatConfig dateFormatConfig;

  DateFormatService dateFormatService;

  @BeforeEach
  void setUp() {
    doReturn(resource).when(request).getResource();
    doReturn(configurationResolver).when(request).adaptTo(ConfigurationResolver.class);
    doReturn(configurationBuilder).when(configurationResolver).get(resource);
    doReturn(dateFormatConfig).when(configurationBuilder).as(DateFormatConfig.class);

    dateFormatService = new DateFormatService();
    dateFormatService.configurationResolver = configurationResolver;
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/dateFormat.csv", numLinesToSkip = 1)
  void getFormattedDate(
      String dateformat,
      String separators,
      boolean isZeroPrefixRequired,
      String timeFormat,
      String hourFormat,
      String caseNotation,
      String date,
      String expectedDate,
      String expectedTime)
      throws ParseException {
    mockDateFormatConfig(
        dateformat, separators, isZeroPrefixRequired, timeFormat, hourFormat, caseNotation);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    Date convertedDate = formatter.parse(date);
    assertEquals(expectedDate, dateFormatService.getFormattedDate(request, convertedDate));
    assertEquals(expectedTime, dateFormatService.getFormattedTime(request, convertedDate));
  }

  void mockDateFormatConfig(
      String dateformat,
      String separators,
      boolean isZeroPrefixRequired,
      String timeFormat,
      String hourFormat,
      String caseNotation) {
    doReturn(dateformat).when(dateFormatConfig).dateFormat();
    doReturn(separators).when(dateFormatConfig).separators();
    doReturn(isZeroPrefixRequired).when(dateFormatConfig).isZeroPrefixRequired();
    doReturn(timeFormat).when(dateFormatConfig).timeFormat();
    doReturn(hourFormat).when(dateFormatConfig).hourFormat();
    doReturn(caseNotation).when(dateFormatConfig).caseNotation();
  }
}
