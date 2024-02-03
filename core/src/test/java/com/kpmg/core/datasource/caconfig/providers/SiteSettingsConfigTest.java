package com.kpmg.core.datasource.caconfig.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kpmg.core.caconfig.*;
import com.kpmg.core.datasource.mocks.*;
import org.junit.jupiter.api.Test;

class SiteSettingsConfigTest extends AbstractProviderTest {

  @Test
  void getCountry() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.COUNTRY_VALUE, mock.siteCountry());
  }

  @Test
  void getLocale() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.LOCALE_VALUE, mock.siteLocale());
  }

  @Test
  void getRFPFormPath() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.RFP_FORM_PATH, mock.rfpFormPath());
  }

  @Test
  void getContactFormPath() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.CONTACT_FORM_PATH, mock.contactFormPath());
  }

  @Test
  void getSearchPagePath() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.SEARCH_PAGE_PATH, mock.searchPagePath());
  }

  @Test
  void getContactFormEmailAddress() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(
        SiteSettingsConfigMock.CONTACT_FORM_EMAIL_ADDRESS, mock.contactFormEmailAddresses());
  }

  @Test
  void getLocalModelCountryID() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.LOCAL_MODEL_COUNTRY_ID, mock.localModelCountryZthesID());
  }

  @Test
  void getLocalModelIndustryID() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.LOCAL_MODEL_INDUSTRY_ID, mock.localModelIndustryZthesID());
  }

  @Test
  void getLocalModelServiceID() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.LOCAL_MODEL_SERVICE_ID, mock.localModelServiceZthesID());
  }

  @Test
  void getLocalModelInsightID() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.LOCAL_MODEL_INSIGHT_ID, mock.localModelInsightZthesID());
  }

  @Test
  void getSESLanguage() {
    SiteSettingsConfigMock mock = new SiteSettingsConfigMock();
    assertEquals(SiteSettingsConfigMock.SES_LANGUAGE, mock.sesLanguage());
  }
}
