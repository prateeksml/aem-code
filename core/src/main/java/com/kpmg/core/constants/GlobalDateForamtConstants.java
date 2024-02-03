package com.kpmg.core.constants;

import java.util.AbstractMap.SimpleEntry;

public interface GlobalDateForamtConstants {

  /** Constants */
  public static final String TEXT = "text";

  public static final String VALUE = "value";

  public static final SimpleEntry<String, String> DELIMITTER = new SimpleEntry<>(" | ", "And");
  /** List of Separators */
  public static final String MONTH_DAY_YEAR =
      "month" + DELIMITTER.getValue() + "day" + DELIMITTER.getValue() + "year";

  public static final String DAY_MONTH_YEAR =
      "day" + DELIMITTER.getValue() + "month" + DELIMITTER.getValue() + "year";
  public static final String YEAR_MONTH_DAY =
      "year" + DELIMITTER.getValue() + "month" + DELIMITTER.getValue() + "day";
  static final String YEAR_DAY = "year" + DELIMITTER.getValue() + "day";

  /** Gregorian Formats */
  /**
   * MDY Month Day Year mm/dd/yyyy Space / -  . 11/9/2014, 11-9-2014, 11.9.2014, 11.09.2014,
   * 11/09/2014 Numeric Prefix 4 Digit
   */
  public static final String GREG_MM_DD_YYYY_MONTH_DAY_YEAR = "mm/dd/yyyy:Month Day Year";

  /**
   * MDY Month Day Year [full month] dd, yyyy Space , . November 9, 2014, Nov. 9, 2014 Long Form
   * Prefix 4 Digit
   */
  public static final String GREG_FULL_MONTH_DD_YYYY_MONTH_DAY_YEAR_MONTH_LONG_FORM =
      "[full month] dd, yyyy:Month Day Year";
  /**
   * MDY Month Day Year mmm dd, yyyy Space , . November 9, 2014, Nov. 9, 2014 Short Form Prefix 4
   * Digit
   */
  public static final String GREG_FULL_MONTH_DD_YYYY_MONTH_DAY_YEAR_MONTH_SHORT_FORM =
      "mmm dd, yyyy:Month Day Year";

  /**
   * YMD Year Month Day yyyy-mm-dd / - . OR NoSpace 2003-11-09, 2003.11.9, 2003.11.09, 2003/11/09,
   * 20031109 Numeric Prefix 4 Digit
   */
  public static final String GREG_YYYY_MM_DD_YEAR_MONTH_DAY = "yyyy-mm-dd:Year Month Day";

  /**
   * YMD Year Month Day yyyy mmm dd Space - . OR NoSpace, OR Period and Space | Space | Period 2003
   * November 9, 2003Nov9, 2003Nov09, 2003-Nov-9, 2003-Nov-09, 2003. november 9. Short Form Prefix 4
   * Digit
   */
  public static final String GREG_YYYY_FULL_MONTH_DD_YEAR_MONTH_DAY_MONTH_SHORT_FORM =
      "yyyy mmm dd:Year Month Day";
  /**
   * YMD Year Month Day yyyy [full month] dd Space - . OR NoSpace, OR Period and Space | Space |
   * Period 2003 November 9, 2003Nov9, 2003Nov09, 2003-Nov-9, 2003-Nov-09, 2003. november 9. Long
   * Form Prefix 4 Digit
   */
  public static final String GREG_YYYY_FULL_MONTH_DD_YEAR_MONTH_DAY_MONTH_LONG_FORM =
      "yyyy [full month] dd:Year Month Day";

  /**
   * DMY Day Month Year (With Apostrophe Options) dd-mmm-yy Space - / ‘ . OR Space | Period, Space,
   * and Apostrophe 08-Nov-14, 8-Nov-14, 08 Nov ‘14, 8 Nov ‘14, 8 Nov. ‘14, 08/Nov/14, 08/Nov/’14,
   * 8/Nov/’15, 08/Nov/’15, 8 NOV ‘15 Short Form Prefix 2 Digit
   */
  public static final String GREG_DD_MMM_YY_DAY_MONTH_YEAR_WITH_APOSTROPHE_OPTIONS =
      "dd-mmm-yy:Day Month Year (With Apostrophe Options)";

  /**
   * DMY Day Month Year dd-mmm-yyyy Space - / . 08-Nov-2014, 8-Nov-2014, 08 Nov 2014, 8 Nov 2014, 8
   * Nov. 2014, 08/Nov/2014, 8/Nov/2015, 8 NOV 2015 Short Form Prefix 4 Digit
   */
  public static final String GREG_DD_MMM_YYYY_DAY_MONTH_YEAR = "dd-mmm-yyyy :Day Month Year";

  /**
   * DMY (no 0-prefix) Day Month Year d [full month] yyyy Space | Space OR Period | Space 8 November
   * 2014, 8. November 2014 Long Form No Preifx 4 Digit
   */
  public static final String GREG_D_FULL_MONTH_YYYY_DAY_MONTH_YEAR =
      "d [full month] yyyy:Day Month Year";

  /** Four Year Formats */

  /**
   * *JIS Japanese Industrial Standard Christian Era yyyy-mm-dd - 1/15/1996 Numeric No Preifx 4
   * Digit
   */
  public static final String FOUR_YYYY_MM_DD_JAPANESE_INDUSTRIAL_STANDARD_CHRISTIAN_ERA =
      "yyyy-mm-dd:Japanese Industrial Standard Christian Era";

  /**
   * *EUR European Standard dd.mm.yyyy . or dash (-) 08.01.1996, 08-01-1996 Numeric No Preifx 4
   * Digit
   */
  public static final String FOUR_DD_MM_YYYY_EUROPEAN_STANDARD = "dd.mm.yyyy:European Standard";

  /** *USA USA Standard mm/dd/yyyy / 1/15/1996 Numeric No Preifx 4 Digit */
  public static final String FOUR_MM_DD_YYYY_USA_STANDARD = "mm/dd/yyyy:USA Standard";
  /** *ISO International Standards Organization yyyy-mm-dd - 1/15/1996 Numeric No Preifx 4 Digit */
  public static final String FOUR_YYYY_MM_DD_INTERNATIONAL_STANDARDS_ORGANIZATION =
      "yyyy-mm-dd:International Standards Organization";

  /** Two Year Formats */
  /** *JUL Julian yy/ddd / - . , '&' 96/015 Numeric No Preifx 2 Digit */
  static final String TWO_YY_DDD_JULIAN = "yy/ddd:Julian";

  /** *YMD Year/Month/Day yy/mm/dd / - . , '&' 96/01/15 Numeric No Preifx 2 Digit */
  static final String TWO_YY_MM_DD_YEAR_MONTH_DAY = "yy/mm/dd:Year/Month/Day";

  /** *DMY Day/Month/Year dd/mm/yy / - . , '&' 15/01/96 Numeric No Preifx 2 Digit */
  static final String TWO_DD_MM_YY_DAY_MONTH_YEAR = "dd/mm/yy:Day/Month/Year";

  /** *MDY Month/Day/Year mm/dd/yy / - . , '&' 1/15/1996 Numeric No Preifx 4 Digit */
  static final String TWO_MM_DD_YY_MONTH_DAY_YEAR = "mm/dd/yy:Month/Day/Year";

  /** Time Formats */
  static final String TIME_HH_MM_JAPANESE_INDUSTRIAL_STANDARD_CHRISTIAN_ERA =
      "hh:mm:Japanese Industrial Standard Christian Era";

  static final String TIME_HH_MM_EUROPEAN_STANDARD = "hh.mm:European Standard";
  static final String TIME_HH_MM_AM_OR_HH_MM_PM_USA_STANDARD = "hh:mm AM or hh:mm PM:USA Standard";
  static final String TIME_HH_MM_INTERNATIONAL_STANDARDS_ORGANIZATION =
      "hh.mm:International Standards Organization";
  static final String TIME_HH_MM_HOURS_MINUTES_SECONDS = "hh:mm:Hours:Minutes:Seconds";

  public static final SimpleEntry<String, String> SLASH =
      new SimpleEntry<String, String>("Slash", "/");
  public static final SimpleEntry<String, String> HYPHEN =
      new SimpleEntry<String, String>("Hyphen", "-");
  public static final SimpleEntry<String, String> PERIOD =
      new SimpleEntry<String, String>("Period", ".");
  public static final SimpleEntry<String, String> COMMA =
      new SimpleEntry<String, String>("Comma", ",");
  public static final SimpleEntry<String, String> AMPERSAND =
      new SimpleEntry<String, String>("Ampersand", "&");
  public static final SimpleEntry<String, String> SPACE =
      new SimpleEntry<String, String>("Space", " ");
  public static final SimpleEntry<String, String> SPACE_APHOSTROPHE =
      new SimpleEntry<String, String>("Space and Apostrophe", " '");
  public static final SimpleEntry<String, String> PERIOD_SPACE_APHOSTROPHE =
      new SimpleEntry<String, String>(" Period, Space, and Apostrophe", ". '");
  public static final SimpleEntry<String, String> PERIOD_SPACE =
      new SimpleEntry<String, String>("Period and Space", ". ");
  public static final SimpleEntry<String, String> COMMA_SPACE =
      new SimpleEntry<String, String>("Comma and Space", ", ");
  public static final SimpleEntry<String, String> NOSPACE =
      new SimpleEntry<String, String>("NoSpace", "NoSpace");
  public static final SimpleEntry<String, String> SLASH_APHOSTROPHE =
      new SimpleEntry<String, String>("Slash and Apostrophe", "/'");
  static final String COLON = ":";

  /**
   * @author dboddu Transformation Constants
   */
  static final String HOUR_24_HOUR_FORMAT = "24 Hour Format";

  static final String HOUR_12_HOUR_FORMAT = "12 Hour Format";
  static final String YEAR_4_DIGIT_NUMERIC = "4 Digit Numeric";
  static final String YEAR_2_DIGIT_NUMERIC = "2 Digit Numeric";
  static final String MONTHCASE_UPPER_CASE = "Upper Case";
  static final String MONTHCASE_LOWER_CASE = "Lower Case";
  static final String MONTHCASE_CAMEL_CASE = "Camel Case";
  static final String MONTH_NUMERIC = "Numeric";
  static final String MONTH_SHORT_FORM = "Short Form";
  static final String MONTH_LONG_FORM = "Long Form";
  static final String DAY_3_DIGIT_NUMERIC = "3 Digit Numeric";
  static final String DAY_2_DIGIT_NUMERIC_WITHOUT_0_PREFIX = "2 Digit Numeric without 0-prefix";
  static final String DAY_2_DIGIT_NUMERIC_WITH_0_PREFIX = "2 Digit Numeric with 0-prefix";

  public interface SeparatorCombinations {
    /** Slash | Slash */
    public static final SimpleEntry<String, String> SLASH_SLASH =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.SLASH.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.SLASH.getKey(),
            GlobalDateForamtConstants.SLASH.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.SLASH.getValue());
    /** Hyphen | Hyphen */
    public static final SimpleEntry<String, String> HYPHEN_HYPHEN =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.HYPHEN.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.HYPHEN.getKey(),
            GlobalDateForamtConstants.HYPHEN.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.HYPHEN.getValue());
    /** Period | Period */
    public static final SimpleEntry<String, String> PERIOD_PERIOD =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.PERIOD.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.PERIOD.getKey(),
            GlobalDateForamtConstants.PERIOD.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.PERIOD.getValue());
    /** Comma | Comma */
    public static final SimpleEntry<String, String> COMMA_COMMA =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.COMMA.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.COMMA.getKey(),
            GlobalDateForamtConstants.COMMA.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.COMMA.getValue());

    /** Ampersand | Ampersand */
    public static final SimpleEntry<String, String> AMPERSAND_AMPERSAND =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.AMPERSAND.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.AMPERSAND.getKey(),
            GlobalDateForamtConstants.AMPERSAND.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.AMPERSAND.getValue());

    /** Space | Space */
    public static final SimpleEntry<String, String> SPACE_SPACE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.SPACE.getKey(),
            GlobalDateForamtConstants.SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.SPACE.getValue());

    /** Space | Space and Apostrophe (Example: 08 Nov '14) */
    public static final SimpleEntry<String, String> SPACE_SPACE_AND_APHOSTROPHE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.SPACE_APHOSTROPHE.getKey(),
            GlobalDateForamtConstants.SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.SPACE_APHOSTROPHE.getValue());

    /** Space | Period, Space, and Apostrophe (Example: 8 Nov. '14) */
    public static final SimpleEntry<String, String> SPACE_PERIOD_AND_SPACE_AND_APHOSTROPHE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.PERIOD_SPACE_APHOSTROPHE.getKey(),
            GlobalDateForamtConstants.SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.PERIOD_SPACE_APHOSTROPHE.getValue());

    /** Period and Space | Comma and Space (Example: Nov. 9, 2014) */
    public static final SimpleEntry<String, String> PERIOD_AND_SPACE_COMMA_AND_SPACE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.PERIOD_SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.COMMA_SPACE.getKey(),
            GlobalDateForamtConstants.PERIOD_SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.COMMA_SPACE.getValue());
    /** Period and Space | Space */
    public static final SimpleEntry<String, String> PERIOD_AND_SPACE_SPACE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.PERIOD_SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.SPACE.getKey(),
            GlobalDateForamtConstants.PERIOD_SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.SPACE.getValue());

    /** Period and Space | Space | Period (Example: 2003. november 9.) */
    public static final SimpleEntry<String, String> PERIOD_AND_SPACE_SPACE_PERIOD =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.PERIOD_SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.PERIOD.getKey(),
            GlobalDateForamtConstants.PERIOD_SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.PERIOD.getValue());

    /** Slash | Slash and Apostrophe (Example: 8/Nov/'15) */
    public static final SimpleEntry<String, String> SLASH_SLASH_AND_APOSTROPHE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.SLASH.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.SLASH_APHOSTROPHE.getKey(),
            GlobalDateForamtConstants.SLASH.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.SLASH_APHOSTROPHE.getValue());
    /** Space | Period and Space (Example: 8 Nov. 15) */
    public static final SimpleEntry<String, String> SPACE_PERIOD_AND_SPACE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.PERIOD_SPACE.getKey(),
            GlobalDateForamtConstants.SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.PERIOD_SPACE.getValue());
    /** Space | Comma and Space (Example: 8 Nov. 15) */
    public static final SimpleEntry<String, String> SPACE_COMMA_AND_SPACE =
        new SimpleEntry<String, String>(
            GlobalDateForamtConstants.SPACE.getKey()
                + GlobalDateForamtConstants.DELIMITTER.getKey()
                + GlobalDateForamtConstants.COMMA_SPACE.getKey(),
            GlobalDateForamtConstants.SPACE.getValue()
                + GlobalDateForamtConstants.DELIMITTER.getValue()
                + GlobalDateForamtConstants.COMMA_SPACE.getValue());
  }
}
