package com.kpmg.core.dateformat;

import com.kpmg.core.annotations.MigratedCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.caconfig.DateFormatConfig;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.core.constants.GlobalDateForamtConstants;
import com.kpmg.core.constants.I18nKeys;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MigratedCodeExcludeFromCodeCoverageReportGenerated // please read annotation documentation.
@Component(service = DateFormatService.class, immediate = true)
public class DateFormatService {
  public static final String MONTH = "month";
  public static final String DAY = "day";
  public static final String YEAR = "year";
  /** Dafault Separtor */
  private static final String DEFAULT_SEPARATOR = " ";
  /** LOG */
  private static final Logger LOG = LoggerFactory.getLogger(DateFormatService.class);

  @Reference ConfigurationResolver configurationResolver;

  /**
   *
   *
   * <h1>Format Date</h1>
   *
   * Formatting the given {@link Date} as per {@link Locale}
   */
  public String formatDate(Date date, DateFormatsVO d, Locale locale) {
    return transformField(d.getField1(), date, d, locale)
        + d.getSeparator1()
        + transformField(d.getField2(), date, d, locale)
        + d.getSeparator2()
        + transformField(d.getField3(), date, d, locale)
        + d.getSeparator3();
  }

  /**
   *
   *
   * <h1>Format Date</h1>
   *
   * Formatting the given {@link Date} as per {@link Locale}
   */
  public String formatDate(Calendar date, DateFormatsVO d, Locale locale) {
    return transformField(d.getField1(), date, d, locale)
        + d.getSeparator1()
        + transformField(d.getField2(), date, d, locale)
        + d.getSeparator2()
        + transformField(d.getField3(), date, d, locale)
        + d.getSeparator3();
  }

  /**
   *
   *
   * <h1>Format Time</h1>
   *
   * This Method is used to format Time as per site setting selected values
   */
  public String formatTime(
      SlingHttpServletRequest request, Date date, DateFormatsVO d, Locale locale) {
    String format = "";
    if (d.getHourFormat().equals(GlobalDateForamtConstants.HOUR_12_HOUR_FORMAT)) {
      if (d.isShow_AM_PM_Flag()) {
        format = "hh" + d.getTimeSeparator() + "mm a";
      } else {
        format = "hh" + d.getTimeSeparator() + "mm";
      }

    } else if (d.getHourFormat().equals(GlobalDateForamtConstants.HOUR_24_HOUR_FORMAT)) {
      format = "HH" + d.getTimeSeparator() + "mm";
    }

    SimpleDateFormat s = new SimpleDateFormat(format);
    String formattedTime = s.format(date);
    if (d.getHourFormat().equals(GlobalDateForamtConstants.HOUR_12_HOUR_FORMAT)) {
      formattedTime = replaceAmPm(request, locale, formattedTime);
    }
    return formattedTime;
  }

  /** */
  public String formatTime(
      SlingHttpServletRequest request, Calendar date, DateFormatsVO d, Locale locale) {
    StringBuilder formattedTime = new StringBuilder();

    String hourOfDay =
        date.get(Calendar.HOUR_OF_DAY) == 0
            ? "00"
            : Integer.toString(date.get(Calendar.HOUR_OF_DAY));
    /* By default setting 12 hour format */
    String decoratedValue =
        formattedTime
            .append(date.get(Calendar.HOUR) == 0 ? hourOfDay : date.get(Calendar.HOUR))
            .append(d.getTimeSeparator())
            .append(date.get(Calendar.MINUTE) == 0 ? "00" : date.get(Calendar.MINUTE))
            .toString();
    if (d.getHourFormat().equals(GlobalDateForamtConstants.HOUR_12_HOUR_FORMAT)) {
      if (d.isShow_AM_PM_Flag()) {
        decoratedValue =
            formattedTime.append(date.get(Calendar.AM_PM) == 0 ? "AM" : "PM").toString();
      }
      decoratedValue = replaceAmPm(request, locale, decoratedValue);
    } else if (d.getHourFormat().equals(GlobalDateForamtConstants.HOUR_24_HOUR_FORMAT)) {
      StringBuilder formatted24HourTime = new StringBuilder();
      decoratedValue =
          formatted24HourTime
              .append(date.get(Calendar.HOUR_OF_DAY) == 0 ? "00" : date.get(Calendar.HOUR_OF_DAY))
              .append(d.getTimeSeparator())
              .append(date.get(Calendar.MINUTE) == 0 ? "00" : date.get(Calendar.MINUTE))
              .toString();
    }
    return decoratedValue;
  }

  /**
   * Sets Default Format for DateFormatVO Fallback Mechanism
   *
   * @return DateFormatsVO
   */
  public DateFormatsVO getDateAndTimeDefaultFormat() {
    LOG.debug("In  dateDefaultFormat");
    DateFormatsVO d = new DateFormatsVO();

    d.setField1(DAY);
    d.setField2(MONTH);
    d.setField3(YEAR);
    d.setSeparator1(DEFAULT_SEPARATOR);
    d.setSeparator2(DEFAULT_SEPARATOR);
    d.setSeparator3("");
    d.setDayType(GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITH_0_PREFIX);
    d.setMonthCase(GlobalDateForamtConstants.MONTHCASE_CAMEL_CASE);
    d.setMonthType(GlobalDateForamtConstants.MONTH_LONG_FORM);
    d.setYearType(GlobalDateForamtConstants.YEAR_4_DIGIT_NUMERIC);
    d.setHourFormat(GlobalDateForamtConstants.HOUR_12_HOUR_FORMAT);
    d.setIsFiled3Required("true");
    d.setIsSeparator3Required("false");
    d.setTimeFormat(GlobalDateForamtConstants.TIME_HH_MM_HOURS_MINUTES_SECONDS);
    d.setShow_AM_PM_Flag(false);
    d.setTimeSeparator(GlobalDateForamtConstants.COLON);

    return d;
  }

  /**
   * @param request
   * @param date
   * @return
   */
  public String getFormattedDate(SlingHttpServletRequest request, Date date) {
    DateFormatsVO d = getDateFormatVo(request);
    return formatDate(date, d, getLocale(request));
  }

  /** */
  public String getFormattedDate(SlingHttpServletRequest request, Calendar date) {
    DateFormatsVO d = getDateFormatVo(request);
    return formatDate(date, d, getLocale(request));
  }

  /** */
  public String getFormattedTime(SlingHttpServletRequest request, Date date) {
    DateFormatsVO d = getDateFormatVo(request);
    return formatTime(request, date, d, getLocale(request));
  }

  /**
   * @param request
   * @param date
   * @return
   */
  public String getFormattedTime(SlingHttpServletRequest request, Calendar date) {
    DateFormatsVO d = getDateFormatVo(request);
    return formatTime(request, date, d, getLocale(request));
  }

  public DateFormatsVO getDateFormatVo(SlingHttpServletRequest request) {
    return Optional.ofNullable(configurationResolver)
        .map(cr -> cr.get(request.getResource()))
        .map(cb -> cb.as(DateFormatConfig.class))
        .map(this::getDateFormatVo)
        .orElse(null);
  }

  public DateFormatsVO getDateFormatVo(DateFormatConfig config) {

    DateFormatsVO d =
        putValuesInToVO(
            config.dateFormat(),
            config.separators(),
            config.isZeroPrefixRequired(),
            config.timeFormat(),
            config.caseNotation(),
            config.hourFormat());
    if (null == d || (StringUtils.isBlank(d.getField1()) && StringUtils.isBlank(d.getField2()))) {
      d = getDateAndTimeDefaultFormat();
    }
    return d;
  }

  /**
   * @param d
   * @param selectedDateFormat
   */
  private void breakDateFormatsInToPositions(DateFormatsVO d, String selectedDateFormat) {
    d.setField1("");
    d.setField2("");
    d.setField3("");
    if (selectedDateFormat != null) {
      d.setSelectedDateFormat(selectedDateFormat);
    }
    String fieldPositions = new DateFormatHelper().getFieldPositions(selectedDateFormat);

    if (null != fieldPositions) {

      String[] fields = fieldPositions.split(GlobalDateForamtConstants.DELIMITTER.getValue());

      d.setField1(fields[0]);
      d.setField2(fields[1]);

      if (fields.length > 2) {
        d.setField3(fields[2]);
      }
    }
  }

  /** */
  public DateFormatsVO putValuesInToVO(
      String selectedDateFormat,
      String separators,
      boolean hasZeroPrefix,
      String timeFormat,
      String monthCase,
      String hourFormat) {
    DateFormatsVO d = new DateFormatsVO();
    breakDateFormatsInToPositions(d, selectedDateFormat);

    if (StringUtils.isAnyBlank(d.getField1(), d.getField2())) {
      return new DateFormatService().getDateAndTimeDefaultFormat();
    }

    breakSeparatorsIntoList(d, separators);
    DateFormatHelper dateFormatHelper = new DateFormatHelper();
    if (selectedDateFormat.equals(GlobalDateForamtConstants.TWO_YY_DDD_JULIAN)) {
      d.setDayType(GlobalDateForamtConstants.DAY_3_DIGIT_NUMERIC);
    } else {
      if (null != DateFormatHelper.getSeparatorForFormat(selectedDateFormat)
          && !DateFormatHelper.getSeparatorForFormat(selectedDateFormat)
              .hideZeroFlagButSetValueToTrue) {
        if (dateFormatHelper.getZeroPrefixFlag(selectedDateFormat).equals("true")) {

          if (hasZeroPrefix) {
            d.setDayType(GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITH_0_PREFIX);
          } else {
            d.setDayType(GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITHOUT_0_PREFIX);
          }

        } else {
          d.setDayType(GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITHOUT_0_PREFIX);
        }
      } else {
        d.setDayType(GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITH_0_PREFIX);
      }
    }
    d.setMonthType(dateFormatHelper.getMonthOptions(selectedDateFormat).getValue());
    d.setYearType(dateFormatHelper.getYearOptions(selectedDateFormat).getValue());

    d.setMonthCase(monthCase);

    d.setHourFormat(hourFormat);
    d.setShow_AM_PM_Flag(
        timeFormat.equals(GlobalDateForamtConstants.TIME_HH_MM_AM_OR_HH_MM_PM_USA_STANDARD)
            && hourFormat.equals(GlobalDateForamtConstants.HOUR_12_HOUR_FORMAT));
    d.setTimeSeparator(dateFormatHelper.getTimeFormats().get(timeFormat));
    d.setTimeFormat(timeFormat);
    return d;
  }

  /**
   * This Method transforms fields
   *
   * @param field - filed which has to be transforemed
   * @param date - date object
   * @param d - Dateformat VO
   * @return formatted field
   */
  private String transformField(String field, Date date, DateFormatsVO d, Locale locale) {
    switch (field) {
      case DAY:
        return transformDay(date, d, locale);
      case MONTH:
        return transformMonth(date, d, locale);
      case YEAR:
        return transformYear(date, d, locale);
      default:
        return StringUtils.EMPTY;
    }
  }

  /**
   * This Method transforms fields
   *
   * @param field - filed which has to be transforemed
   * @param date - date object
   * @param d - Dateformat VO
   * @return formatted field
   */
  private String transformField(String field, Calendar date, DateFormatsVO d, Locale locale) {
    switch (field) {
      case DAY:
        return transformDay(date, d);
      case MONTH:
        return transformMonth(date, d, locale);
      case YEAR:
        return transformYear(date, d);
      default:
        return "";
    }
  }

  /**
   * @param date - Date Object
   * @param d - DateFormat VO
   * @return Transformed Day
   */
  private String transformDay(Date date, DateFormatsVO d, Locale locale) {
    LOG.debug("In transformDay");
    String format;
    switch (d.getDayType()) {
      case GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITH_0_PREFIX:
        format = "dd";
        break;
      case GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITHOUT_0_PREFIX:
        format = "d";
        break;
      case GlobalDateForamtConstants.DAY_3_DIGIT_NUMERIC:
        format = "D";
        break;
      default:
        format = "";
        break;
    }
    SimpleDateFormat s = new SimpleDateFormat(format, locale);
    return s.format(date);
  }

  /**
   * @param date - Date Object
   * @param d - DateFormat VO
   * @return Transformed Day
   */
  private String transformDay(Calendar date, DateFormatsVO d) {
    LOG.debug("In transformDay");
    String formattedDay;
    switch (d.getDayType()) {
      case GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITH_0_PREFIX:
        formattedDay =
            Integer.toString(date.get(Calendar.DAY_OF_MONTH)).length() == 1
                ? "0" + date.get(Calendar.DAY_OF_MONTH)
                : Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        break;
      case GlobalDateForamtConstants.DAY_2_DIGIT_NUMERIC_WITHOUT_0_PREFIX:
        formattedDay = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        break;
      case GlobalDateForamtConstants.DAY_3_DIGIT_NUMERIC:
        formattedDay = Integer.toString(date.get(Calendar.DAY_OF_YEAR));
        break;
      default:
        formattedDay = "";
    }
    return formattedDay;
  }

  /**
   * @param date - Date Object
   * @param d - Date Formats VO
   * @return - Transformed Month
   */
  private String transformMonth(Date date, DateFormatsVO d, Locale locale) {
    LOG.debug("In transformMonth");
    String format;
    switch (d.getMonthType()) {
      case GlobalDateForamtConstants.MONTH_LONG_FORM:
        format = "MMMM";
        break;
      case GlobalDateForamtConstants.MONTH_SHORT_FORM:
        format = "MMM";
        break;
      case GlobalDateForamtConstants.MONTH_NUMERIC:
        format = "MM";
        break;
      default:
        format = "";
        break;
    }
    SimpleDateFormat s = new SimpleDateFormat(format, locale);
    String formattedMonth = s.format(date);
    return getFormattedMonth(d, format, formattedMonth);
  }

  /**
   * @param date - Date Object
   * @param d - Date Formats VO
   * @return - Transformed Month
   */
  private String transformMonth(Calendar date, DateFormatsVO d, Locale locale) {
    LOG.debug("In transformMonth");
    String format = StringUtils.EMPTY;
    String formattedMonth;
    switch (d.getMonthType()) {
      case GlobalDateForamtConstants.MONTH_LONG_FORM:
        format = "MMMM";
        formattedMonth = new DateFormatSymbols(locale).getMonths()[date.get(Calendar.MONTH)];
        break;
      case GlobalDateForamtConstants.MONTH_SHORT_FORM:
        format = "MMM";
        formattedMonth = new DateFormatSymbols(locale).getShortMonths()[date.get(Calendar.MONTH)];
        break;
      case GlobalDateForamtConstants.MONTH_NUMERIC:
        format = "MM";
        formattedMonth =
            Integer.toString(date.get(Calendar.MONTH) + 1).length() == 1
                ? "0" + (date.get(Calendar.MONTH) + 1)
                : Integer.toString(date.get(Calendar.MONTH) + 1);
        break;
      default:
        formattedMonth = StringUtils.EMPTY;
    }
    return getFormattedMonth(d, format, formattedMonth);
  }

  private String getFormattedMonth(DateFormatsVO d, String format, String formattedMonth) {
    if (format.equals("MMM") || format.equals("MMMM")) {
      switch (d.getMonthCase()) {
        case GlobalDateForamtConstants.MONTHCASE_CAMEL_CASE:
          formattedMonth = WordUtils.capitalizeFully(formattedMonth);
          break;
        case GlobalDateForamtConstants.MONTHCASE_LOWER_CASE:
          formattedMonth = formattedMonth.toLowerCase();
          break;
        case GlobalDateForamtConstants.MONTHCASE_UPPER_CASE:
          formattedMonth = formattedMonth.toUpperCase();
          break;
        default:
          break;
      }
    }
    return formattedMonth;
  }

  /**
   * @param date - Date Object
   * @param d - Date Formats VO
   * @return Transformed Year
   */
  private String transformYear(Date date, DateFormatsVO d, Locale locale) {
    LOG.debug("In  transformYear:");
    String format = "";
    if (d.getYearType().equals(GlobalDateForamtConstants.YEAR_2_DIGIT_NUMERIC)) {
      format = "YY";
    } else if (d.getYearType().equals(GlobalDateForamtConstants.YEAR_4_DIGIT_NUMERIC)) {
      format = "YYYY";
    }
    SimpleDateFormat s = new SimpleDateFormat(format, locale);
    return s.format(date);
  }

  /**
   * @param date - Date Object
   * @param d - Date Formats VO
   * @return Transformed Year
   */
  private String transformYear(Calendar date, DateFormatsVO d) {
    LOG.debug("In  transformYear:");
    String formattedYear = "";
    if (d.getYearType().equals(GlobalDateForamtConstants.YEAR_2_DIGIT_NUMERIC)) {
      formattedYear = Integer.toString(date.get(Calendar.YEAR)).substring(2);
    } else if (d.getYearType().equals(GlobalDateForamtConstants.YEAR_4_DIGIT_NUMERIC)) {
      formattedYear = Integer.toString(date.get(Calendar.YEAR));
    }
    return formattedYear;
  }

  private String replaceAmPm(SlingHttpServletRequest request, Locale locale, String formattedTime) {
    boolean containsAM = formattedTime.contains("AM");
    boolean containsPM = formattedTime.contains("PM");
    ResourceBundle resBundle =
        Optional.ofNullable(request).map(r -> r.getResourceBundle(locale)).orElse(null);
    if (null != resBundle) {
      if (containsAM) {
        formattedTime =
            formattedTime.replace(
                "AM", resBundle.getString(I18nKeys.GlobalDateFormatMeridiems.GLOBALDATEFORMAT_AM));
      } else if (containsPM) {
        formattedTime =
            formattedTime.replace(
                "PM", resBundle.getString(I18nKeys.GlobalDateFormatMeridiems.GLOBALDATEFORMAT_PM));
      }
    }
    return formattedTime;
  }

  /**
   * @param d
   * @param separators
   */
  private void breakSeparatorsIntoList(DateFormatsVO d, String separators) {
    d.setSeparator1("");
    d.setSeparator2("");
    d.setSeparator3("");
    if (!StringUtils.isBlank(separators)) {

      String[] separatorsList = separators.split(GlobalDateForamtConstants.DELIMITTER.getValue());

      if (separatorsList.length >= 1 && !(separatorsList[0].equals("NoSpace"))) {
        d.setSeparator1(separatorsList[0]);
      }
      if (separatorsList.length >= 2) d.setSeparator2(separatorsList[1]);
      if (separatorsList.length >= 3) d.setSeparator3(separatorsList[2]);
    }
  }

  private Locale getLocale(SlingHttpServletRequest request) {
    return Optional.ofNullable(configurationResolver)
        .map(cr -> cr.get(request.getResource()))
        .map(cb -> cb.as(SiteSettingsConfig.class))
        .map(siteSettings -> new Locale(siteSettings.siteLocale(), siteSettings.siteCountry()))
        .orElse(Locale.ENGLISH);
  }
}
