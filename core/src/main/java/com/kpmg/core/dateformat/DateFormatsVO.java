package com.kpmg.core.dateformat;

import com.kpmg.core.annotations.MigratedCodeExcludeFromCodeCoverageReportGenerated;

@MigratedCodeExcludeFromCodeCoverageReportGenerated // please read annotation documentation.
public class DateFormatsVO {
  private String field1;
  private String field2;
  private String field3;
  private String separator1;
  private String separator2;
  private String separator3;
  private String dayType;
  private String monthType;
  private String monthCase;
  private String yearType;
  private String isFiled3Required;
  private String isSeparator3Required;
  private String hourFormat;
  private String timeSeparator;
  private String timeFormat;
  private boolean Show_AM_PM_Flag;
  private String selectedDateFormat;

  /**
   * @return the show_AM_PM_Flag
   */
  public boolean isShow_AM_PM_Flag() {
    return Show_AM_PM_Flag;
  }

  /**
   * @param show_AM_PM_Flag the show_AM_PM_Flag to set
   */
  public void setShow_AM_PM_Flag(boolean show_AM_PM_Flag) {
    Show_AM_PM_Flag = show_AM_PM_Flag;
  }

  /**
   * @return the field1
   */
  public String getField1() {
    return field1;
  }

  /**
   * @param field1 the field1 to set
   */
  public void setField1(String field1) {
    this.field1 = field1;
  }

  /**
   * @return the field2
   */
  public String getField2() {
    return field2;
  }

  /**
   * @param field2 the field2 to set
   */
  public void setField2(String field2) {
    this.field2 = field2;
  }

  /**
   * @return the field3
   */
  public String getField3() {
    return field3;
  }

  /**
   * @param field3 the field3 to set
   */
  public void setField3(String field3) {
    this.field3 = field3;
  }

  /**
   * @return the separator1
   */
  public String getSeparator1() {
    return separator1;
  }

  /**
   * @param separator1 the separator1 to set
   */
  public void setSeparator1(String separator1) {
    this.separator1 = separator1;
  }

  /**
   * @return the separator2
   */
  public String getSeparator2() {
    return separator2;
  }

  /**
   * @param separator2 the separator2 to set
   */
  public void setSeparator2(String separator2) {
    this.separator2 = separator2;
  }

  /**
   * @return the separator3
   */
  public String getSeparator3() {
    return separator3;
  }

  /**
   * @param separator3 the separator3 to set
   */
  public void setSeparator3(String separator3) {
    this.separator3 = separator3;
  }

  /**
   * @return the dayType
   */
  public String getDayType() {
    return dayType;
  }

  /**
   * @param dayType the dayType to set
   */
  public void setDayType(String dayType) {
    this.dayType = dayType;
  }

  /**
   * @return the monthType
   */
  public String getMonthType() {
    return monthType;
  }

  /**
   * @param monthType the monthType to set
   */
  public void setMonthType(String monthType) {
    this.monthType = monthType;
  }

  /**
   * @return the monthCase
   */
  public String getMonthCase() {
    return monthCase;
  }

  /**
   * @param monthCase the monthCase to set
   */
  public void setMonthCase(String monthCase) {
    this.monthCase = monthCase;
  }

  /**
   * @return the yearType
   */
  public String getYearType() {
    return yearType;
  }

  /**
   * @param yearType the yearType to set
   */
  public void setYearType(String yearType) {
    this.yearType = yearType;
  }

  /**
   * @return the isFiled3Required
   */
  public String getIsFiled3Required() {
    return isFiled3Required;
  }

  /**
   * @param isFiled3Required the isFiled3Required to set
   */
  public void setIsFiled3Required(String isFiled3Required) {
    this.isFiled3Required = isFiled3Required;
  }

  /**
   * @return the isSeparator3Required
   */
  public String getIsSeparator3Required() {
    return isSeparator3Required;
  }

  /**
   * @param isSeparator3Required the isSeparator3Required to set
   */
  public void setIsSeparator3Required(String isSeparator3Required) {
    this.isSeparator3Required = isSeparator3Required;
  }

  /**
   * @return the timeFormat
   */
  public String getHourFormat() {
    return hourFormat;
  }

  /**
   * @param timeFormat the timeFormat to set
   */
  public void setHourFormat(String timeFormat) {
    this.hourFormat = timeFormat;
  }

  /**
   * @return the timeSeparator
   */
  public String getTimeSeparator() {
    return timeSeparator;
  }

  /**
   * @param timeSeparator the timeSeparator to set
   */
  public void setTimeSeparator(String timeSeparator) {
    this.timeSeparator = timeSeparator;
  }

  /**
   * @return the timeFormat
   */
  public String getTimeFormat() {
    return timeFormat;
  }

  /**
   * @param timeFormat the timeFormat to set
   */
  public void setTimeFormat(String timeFormat) {
    this.timeFormat = timeFormat;
  }

  @Override
  public String toString() {
    return "DateFormatsVO [field1="
        + field1
        + ", field2="
        + field2
        + ", field3="
        + field3
        + ", separator1="
        + separator1
        + ", separator2="
        + separator2
        + ", separator3="
        + separator3
        + ", dayType="
        + dayType
        + ", monthType="
        + monthType
        + ", monthCase="
        + monthCase
        + ", yearType="
        + yearType
        + ", isFiled3Required="
        + isFiled3Required
        + ", isSeparator3Required="
        + isSeparator3Required
        + ", hourFormat="
        + hourFormat
        + ", timeSeparator="
        + timeSeparator
        + ", timeFormat="
        + timeFormat
        + ", Show_AM_PM_Flag="
        + Show_AM_PM_Flag
        + "]";
  }

  public String getSelectedDateFormat() {
    return selectedDateFormat;
  }

  public void setSelectedDateFormat(String selectedDateFormat) {
    this.selectedDateFormat = selectedDateFormat;
  }
}
