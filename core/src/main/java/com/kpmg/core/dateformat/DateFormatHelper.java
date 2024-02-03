package com.kpmg.core.dateformat;

import static com.kpmg.core.constants.GlobalDateForamtConstants.*;
import static com.kpmg.core.constants.GlobalDateForamtConstants.SeparatorCombinations.*;

import com.kpmg.core.annotations.MigratedCodeExcludeFromCodeCoverageReportGenerated;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;

@MigratedCodeExcludeFromCodeCoverageReportGenerated // please read annotation documentation.
public class DateFormatHelper {

  private static final Map<String, String> TWO_YEAR_SEPARATORS =
      Map.ofEntries(SLASH_SLASH, HYPHEN_HYPHEN, PERIOD_PERIOD, AMPERSAND_AMPERSAND, COMMA_COMMA);

  private static final Map<String, SeparatorVO> TWO_YEAR_FORMATS =
      getFormats(
          TWO_MM_DD_YY_MONTH_DAY_YEAR,
          TWO_DD_MM_YY_DAY_MONTH_YEAR,
          TWO_YY_MM_DD_YEAR_MONTH_DAY,
          TWO_YY_DDD_JULIAN);
  private static final Map<String, SeparatorVO> FOUR_YEAR_FORMATS =
      getFormats(
          FOUR_YYYY_MM_DD_INTERNATIONAL_STANDARDS_ORGANIZATION,
          FOUR_MM_DD_YYYY_USA_STANDARD,
          FOUR_DD_MM_YYYY_EUROPEAN_STANDARD,
          FOUR_YYYY_MM_DD_JAPANESE_INDUSTRIAL_STANDARD_CHRISTIAN_ERA);
  private static final Map<String, SeparatorVO> GREGORIAN_YEAR_FORMATS =
      getFormats(
          GREG_D_FULL_MONTH_YYYY_DAY_MONTH_YEAR,
          GREG_DD_MMM_YYYY_DAY_MONTH_YEAR,
          GREG_DD_MMM_YY_DAY_MONTH_YEAR_WITH_APOSTROPHE_OPTIONS,
          GREG_YYYY_FULL_MONTH_DD_YEAR_MONTH_DAY_MONTH_LONG_FORM,
          GREG_YYYY_FULL_MONTH_DD_YEAR_MONTH_DAY_MONTH_SHORT_FORM,
          GREG_YYYY_MM_DD_YEAR_MONTH_DAY,
          GREG_FULL_MONTH_DD_YYYY_MONTH_DAY_YEAR_MONTH_LONG_FORM,
          GREG_FULL_MONTH_DD_YYYY_MONTH_DAY_YEAR_MONTH_SHORT_FORM,
          GREG_MM_DD_YYYY_MONTH_DAY_YEAR);

  public static Map<String, String> getAllFormats() {
    Map<String, String> allFormats = new HashMap<>();
    for (Entry<String, SeparatorVO> entry : TWO_YEAR_FORMATS.entrySet()) {
      allFormats.put(entry.getKey(), entry.getKey());
    }
    for (Entry<String, SeparatorVO> entry : FOUR_YEAR_FORMATS.entrySet()) {
      allFormats.put(entry.getKey(), entry.getKey());
    }
    for (Entry<String, SeparatorVO> entry : GREGORIAN_YEAR_FORMATS.entrySet()) {
      allFormats.put(entry.getKey(), entry.getKey());
    }
    return allFormats;
  }

  public static Map<String, String> getAllSeparators() {
    return Map.ofEntries(
        SLASH_SLASH,
        HYPHEN_HYPHEN,
        PERIOD_PERIOD,
        AMPERSAND_AMPERSAND,
        COMMA_COMMA,
        SLASH,
        COMMA,
        HYPHEN,
        PERIOD,
        AMPERSAND,
        SPACE_SPACE,
        PERIOD_AND_SPACE_SPACE,
        SPACE_SPACE_AND_APHOSTROPHE,
        SPACE_PERIOD_AND_SPACE_AND_APHOSTROPHE,
        SLASH_SLASH_AND_APOSTROPHE,
        SPACE_PERIOD_AND_SPACE,
        SPACE_COMMA_AND_SPACE,
        PERIOD_AND_SPACE_COMMA_AND_SPACE,
        NOSPACE,
        PERIOD_AND_SPACE_SPACE_PERIOD);
  }

  public static Map<String, String> getTimeFormats() {
    Map<String, String> timeFormats = new HashMap<>();
    timeFormats.put(TIME_HH_MM_HOURS_MINUTES_SECONDS, COLON);
    timeFormats.put(TIME_HH_MM_INTERNATIONAL_STANDARDS_ORGANIZATION, PERIOD.getValue());
    timeFormats.put(TIME_HH_MM_AM_OR_HH_MM_PM_USA_STANDARD, COLON);
    timeFormats.put(TIME_HH_MM_EUROPEAN_STANDARD, PERIOD.getValue());
    timeFormats.put(TIME_HH_MM_JAPANESE_INDUSTRIAL_STANDARD_CHRISTIAN_ERA, COLON);
    return timeFormats;
  }

  public static SeparatorVO getSeparatorForFormat(String formatName) {

    SeparatorVO.SeparatorVOBuilder builder = SeparatorVO.builder();
    switch (formatName) {
      case TWO_DD_MM_YY_DAY_MONTH_YEAR:
        return builder
            .format(DAY_MONTH_YEAR)
            .separators(TWO_YEAR_SEPARATORS)
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_2_DIGIT_NUMERIC, YEAR_2_DIGIT_NUMERIC))
            .hideZeroFlagButSetValueToTrue(true)
            .build();
      case TWO_MM_DD_YY_MONTH_DAY_YEAR:
        return builder
            .format(MONTH_DAY_YEAR)
            .separators(TWO_YEAR_SEPARATORS)
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_2_DIGIT_NUMERIC, YEAR_2_DIGIT_NUMERIC))
            .hideZeroFlagButSetValueToTrue(true)
            .build();
      case TWO_YY_MM_DD_YEAR_MONTH_DAY:
        return builder
            .format(YEAR_MONTH_DAY)
            .separators(TWO_YEAR_SEPARATORS)
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_2_DIGIT_NUMERIC, YEAR_2_DIGIT_NUMERIC))
            .hideZeroFlagButSetValueToTrue(true)
            .build();
      case TWO_YY_DDD_JULIAN:
        return builder
            .format(YEAR_DAY)
            .separators(Map.ofEntries(SLASH, COMMA, HYPHEN, PERIOD, AMPERSAND))
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(StringUtils.EMPTY, StringUtils.EMPTY))
            .yearFormatOptions(Map.of(YEAR_2_DIGIT_NUMERIC, YEAR_2_DIGIT_NUMERIC))
            .hideZeroFlagButSetValueToTrue(true)
            .build();
      case FOUR_YYYY_MM_DD_INTERNATIONAL_STANDARDS_ORGANIZATION:
      case FOUR_YYYY_MM_DD_JAPANESE_INDUSTRIAL_STANDARD_CHRISTIAN_ERA:
        return builder
            .format(YEAR_MONTH_DAY)
            .separators(Map.ofEntries(HYPHEN_HYPHEN))
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .hideZeroFlagButSetValueToTrue(true)
            .build();
      case FOUR_MM_DD_YYYY_USA_STANDARD:
        return builder
            .format(MONTH_DAY_YEAR)
            .separators(Map.ofEntries(SLASH_SLASH))
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .hideZeroFlagButSetValueToTrue(true)
            .build();
      case FOUR_DD_MM_YYYY_EUROPEAN_STANDARD:
        return builder
            .format(DAY_MONTH_YEAR)
            .separators(Map.ofEntries(HYPHEN_HYPHEN, PERIOD_PERIOD))
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .hideZeroFlagButSetValueToTrue(true)
            .build();
      case GREG_D_FULL_MONTH_YYYY_DAY_MONTH_YEAR:
        return builder
            .format(DAY_MONTH_YEAR)
            .separators(Map.ofEntries(SPACE_SPACE, PERIOD_AND_SPACE_SPACE))
            .showZeroPrefixFlag(false)
            .monthFormatOptions(Map.of(MONTH_LONG_FORM, MONTH_LONG_FORM))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .build();
      case GREG_DD_MMM_YY_DAY_MONTH_YEAR_WITH_APOSTROPHE_OPTIONS:
        return builder
            .format(DAY_MONTH_YEAR)
            .separators(
                Map.ofEntries(
                    HYPHEN_HYPHEN,
                    SPACE_SPACE_AND_APHOSTROPHE,
                    SPACE_PERIOD_AND_SPACE_AND_APHOSTROPHE,
                    SLASH_SLASH,
                    SLASH_SLASH_AND_APOSTROPHE))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_SHORT_FORM, MONTH_SHORT_FORM))
            .yearFormatOptions(Map.of(YEAR_2_DIGIT_NUMERIC, YEAR_2_DIGIT_NUMERIC))
            .build();
      case GREG_DD_MMM_YYYY_DAY_MONTH_YEAR:
        return builder
            .format(DAY_MONTH_YEAR)
            .separators(Map.ofEntries(HYPHEN_HYPHEN, SPACE_SPACE, SPACE_PERIOD_AND_SPACE))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_SHORT_FORM, MONTH_SHORT_FORM))
            .yearFormatOptions(Map.of(YEAR_2_DIGIT_NUMERIC, YEAR_2_DIGIT_NUMERIC))
            .build();
      case GREG_FULL_MONTH_DD_YYYY_MONTH_DAY_YEAR_MONTH_LONG_FORM:
        return builder
            .format(DAY_MONTH_YEAR)
            .separators(Map.ofEntries(SPACE_COMMA_AND_SPACE, PERIOD_AND_SPACE_COMMA_AND_SPACE))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_LONG_FORM, MONTH_LONG_FORM))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .build();
      case GREG_FULL_MONTH_DD_YYYY_MONTH_DAY_YEAR_MONTH_SHORT_FORM:
        return builder
            .format(MONTH_DAY_YEAR)
            .separators(Map.ofEntries(SPACE_COMMA_AND_SPACE, PERIOD_AND_SPACE_COMMA_AND_SPACE))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_SHORT_FORM, MONTH_SHORT_FORM))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .build();
      case GREG_MM_DD_YYYY_MONTH_DAY_YEAR:
        return builder
            .format(MONTH_DAY_YEAR)
            .separators(Map.ofEntries(SLASH_SLASH, HYPHEN_HYPHEN, PERIOD_PERIOD))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .build();
      case GREG_YYYY_FULL_MONTH_DD_YEAR_MONTH_DAY_MONTH_SHORT_FORM:
        return builder
            .format(YEAR_MONTH_DAY)
            .separators(
                Map.ofEntries(SPACE_SPACE, NOSPACE, HYPHEN_HYPHEN, PERIOD_AND_SPACE_SPACE_PERIOD))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_SHORT_FORM, MONTH_SHORT_FORM))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .build();
      case GREG_YYYY_FULL_MONTH_DD_YEAR_MONTH_DAY_MONTH_LONG_FORM:
        return builder
            .format(YEAR_MONTH_DAY)
            .separators(
                Map.ofEntries(SPACE_SPACE, NOSPACE, HYPHEN_HYPHEN, PERIOD_AND_SPACE_SPACE_PERIOD))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_LONG_FORM, MONTH_LONG_FORM))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .build();
      case GREG_YYYY_MM_DD_YEAR_MONTH_DAY:
        return builder
            .format(YEAR_MONTH_DAY)
            .separators(Map.ofEntries(NOSPACE, HYPHEN_HYPHEN, SLASH_SLASH, PERIOD_PERIOD))
            .showZeroPrefixFlag(true)
            .monthFormatOptions(Map.of(MONTH_NUMERIC, MONTH_NUMERIC))
            .yearFormatOptions(Map.of(YEAR_4_DIGIT_NUMERIC, YEAR_4_DIGIT_NUMERIC))
            .build();
      default:
        return builder.build();
    }
  }

  public Map<String, String> getSeparators(String selectedDateFormat) {
    return getSeparatorForFormat(selectedDateFormat).getSeparators();
  }

  public String getFieldPositions(String selectedDateFormat) {
    return getSeparatorForFormat(selectedDateFormat).getFormat();
  }

  public Entry<String, String> getMonthOptions(String selectedDateFormat) {
    return getFirstElementInMap(getSeparatorForFormat(selectedDateFormat).getMonthFormatOptions());
  }

  public Entry<String, String> getYearOptions(String selectedDateFormat) {
    return getFirstElementInMap(getSeparatorForFormat(selectedDateFormat).getYearFormatOptions());
  }

  public String getZeroPrefixFlag(String selectedDateFormat) {
    if (getSeparatorForFormat(selectedDateFormat).isShowZeroPrefixFlag()) return "true";
    return "false";
  }

  public Entry<String, String> getFirstElementInMap(Map<String, String> al) {
    Entry<String, String> entry = al.entrySet().iterator().next();
    return entry;
  }

  private static Map<String, SeparatorVO> getFormats(String... formatNames) {
    return List.of(formatNames).stream()
        .map(DateFormatHelper::getSeparatorFormatEntry)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  @Builder
  @Getter
  public static class SeparatorVO {
    String format;
    @Singular Map<String, String> separators;
    @Singular Map<String, String> monthFormatOptions;
    @Singular Map<String, String> yearFormatOptions;
    @Singular Map<String, String> monthCaseOptions;
    boolean showZeroPrefixFlag;
    boolean hideZeroFlagButSetValueToTrue;
  }

  private static Entry<String, SeparatorVO> getSeparatorFormatEntry(String name) {
    return new AbstractMap.SimpleEntry<String, SeparatorVO>(name, getSeparatorForFormat(name));
  }
}
