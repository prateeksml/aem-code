package com.kpmg.core.datasource.mocks;

import com.kpmg.core.caconfig.SiteSettingsConfig;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class SiteSettingsConfigMock implements SiteSettingsConfig {

  public static final String COUNTRY_VALUE = "US";
  public static final String LOCALE_VALUE = "en";
  public static final String RFP_FORM_PATH = "";
  public static final String CONTACT_FORM_PATH = "";
  public static final String[] CONTACT_FORM_EMAIL_ADDRESS = new String[] {"abc@yopmail.com"};
  public static final String SEARCH_PAGE_PATH = "";
  public static final String LOCAL_MODEL_COUNTRY_ID = "28192982892892";
  public static final String LOCAL_MODEL_INDUSTRY_ID = "1129299292222";
  public static final String LOCAL_MODEL_SERVICE_ID = "239219292929";
  public static final String LOCAL_MODEL_INSIGHT_ID = "1288121289218998219";
  public static final String SES_LANGUAGE = "en_us";
  public static final String CONTACT_EMAIL_TEMPLATEPATH = "/content/kpmgpublic/us/en/home/email";
  public static final String INTERNAL_EMAIL_TEMPLATEPATH =
      "/content/kpmgpublic/us/en/home/email_internal";
  public static final String PEOPLE_CONTACT_EMAIL_TEMPLATEPATH =
      "/content/kpmgpublic/us/en/home/people_email";
  public static final String PEOPLE_CONTACT_FORM_PATH =
      "/content/experience-fragments/us/en/forms/people-contact-form";

  public static final String ONETRUST = "script";

  public static final String RFPCOUNTRYID = "countryid";

  CookieMappingConfig[] items;

  FormfieldMappingConfig formfieldMappingConfig;

  RegexMappingConifg regexMappingConifg;

  RFPForm rfpForm;

  ContactForm contactForm;

  PeopleContactForm peopleContactForm;

  SmartLogic smartLogic;

  @Override
  public String siteCountry() {
    return SiteSettingsConfigMock.COUNTRY_VALUE;
  }

  @Override
  public String siteLocale() {
    return SiteSettingsConfigMock.LOCALE_VALUE;
  }

  @Override
  public String searchPagePath() {
    return SiteSettingsConfigMock.SEARCH_PAGE_PATH;
  }

  @Override
  public SmartLogic smartLogic() {
    return smartLogic;
  }

  public SiteSettingsConfigMock(SiteSettingsConfig.SmartLogic smartLogic) {
    this.smartLogic = smartLogic;
  }

  public SiteSettingsConfigMock(SiteSettingsConfig.RFPForm rfpForm) {
    this.rfpForm = rfpForm;
  }

  String localModelCountryZthesID;
  String localModelIndustryZthesID;
  String localModelServiceZthesID;

  String localModelInsightZthesID;
  String sesLanguage;
  String rfpFormPath;
  String contactFormPath;
  String contactFormEmailAddresses;

  public SiteSettingsConfigMock(
      String localModelCountryZthesID,
      String localModelIndustryZthesID,
      String localModelServiceZthesID,
      String localModelInsightZthesID,
      String sesLanguage,
      String rfpFormPath,
      String contactFormPath,
      String contactFormEmailAddresses) {
    this.localModelCountryZthesID = localModelCountryZthesID;
    this.localModelIndustryZthesID = localModelIndustryZthesID;
    this.localModelServiceZthesID = localModelServiceZthesID;
    this.localModelInsightZthesID = localModelInsightZthesID;
    this.sesLanguage = sesLanguage;
    this.rfpFormPath = rfpFormPath;
    this.contactFormPath = contactFormPath;
    this.contactFormEmailAddresses = contactFormEmailAddresses;
  }

  public String localModelCountryZthesID() {
    return SiteSettingsConfigMock.LOCAL_MODEL_COUNTRY_ID;
  }

  public String localModelIndustryZthesID() {
    return SiteSettingsConfigMock.LOCAL_MODEL_INDUSTRY_ID;
  }

  public String localModelServiceZthesID() {
    return SiteSettingsConfigMock.LOCAL_MODEL_SERVICE_ID;
  }

  public String localModelInsightZthesID() {
    return SiteSettingsConfigMock.LOCAL_MODEL_INSIGHT_ID;
  }

  public String sesLanguage() {
    return SiteSettingsConfigMock.SES_LANGUAGE;
  }

  public String rfpFormPath() {
    return SiteSettingsConfigMock.RFP_FORM_PATH;
  }

  public String contactFormPath() {
    return SiteSettingsConfigMock.CONTACT_FORM_PATH;
  }

  public String[] contactFormEmailAddresses() {
    return SiteSettingsConfigMock.CONTACT_FORM_EMAIL_ADDRESS;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return null;
  }

  @Override
  public String shareLabel() {
    return "Share";
  }

  @Override
  public boolean enableFacebook() {
    return false;
  }

  @Override
  public boolean enableTwitter() {
    return false;
  }

  @Override
  public boolean enableLinkedin() {
    return false;
  }

  public SiteSettingsConfigMock() {}

  public SiteSettingsConfigMock(
      CookieMappingConfig[] items,
      ContactForm contactForm,
      PeopleContactForm peopleContactForm,
      RegexMappingConifg regexMappingConifg,
      FormfieldMappingConfig formfieldMappingConfig,
      RFPForm rfpForm) {
    this.items = items;
    this.contactForm = contactForm;
    this.peopleContactForm = peopleContactForm;
    this.regexMappingConifg = regexMappingConifg;
    this.formfieldMappingConfig = formfieldMappingConfig;
    this.regexMappingConifg = regexMappingConifg;
    this.rfpForm = rfpForm;
  }

  @Override
  public String oneTrust() {
    return ONETRUST;
  }

  @Override
  public CookieMappingConfig[] items() {
    return items;
  }

  @Override
  public ContactForm contactForm() {
    return contactForm;
  }

  @Override
  public PeopleContactForm peopleForm() {
    return peopleContactForm;
  }

  @Override
  public RFPForm rfpForm() {
    return rfpForm;
  }

  @Override
  public FormfieldMappingConfig formMapping() {
    return formfieldMappingConfig;
  }

  @Override
  public RegexMappingConifg regexMapping() {
    return regexMappingConifg;
  }
}
