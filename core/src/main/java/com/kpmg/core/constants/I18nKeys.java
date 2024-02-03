package com.kpmg.core.constants;

import com.kpmg.core.annotations.MigratedCodeExcludeFromCodeCoverageReportGenerated;

/**
 * This class provides i18n keys for various components and templates.
 *
 * <p>The keys are bucketed based on templates as it is done in the content model. For each of the
 * bucket, let the key constant be like:
 *
 * <pre>
 * String COMPONENTNAME_FIELDNAME = &quot;kpmg.componentname.fieldname&quot;;
 * </pre>
 *
 * <strong>Note: </strong>Please note that the actual keys are prefixed with <tt>kpmg.</tt>, so as
 * to keep them distict from other keys. The component name when added to the key makes it easier to
 * be tracked on the translator interface. Note that the actual keys are visible to Global Site
 * Admins.
 *
 * @author vgurug
 */
@MigratedCodeExcludeFromCodeCoverageReportGenerated // please read annotation documentation.
public interface I18nKeys {
  /** This interface holds I18n keys for ArticleDetail */
  interface ArticleDetail {
    String AUTHORS_CONTACT = "kpmg.authors.contact";
    String AUTHORS_SHARELABEL = "kpmg.share.shareLabel";
    String NEWSTOOL_NEWSTITLE = "kpmg.newstool.newsTitle";
    String NEWSTOOL_TOOLTITLE = "kpmg.newstool.toolTitle";
    String NEWSTOOL_NEWSCTA = "kpmg.newstool.cta";
    String NEWSTOOL_TOOLCTA = "kpmg.tools.cta";
    String RELCONTENT_TITLE = "kpmg.relatedcontent.title";
    String RELCONTENT_LABEL = "kpmg.relatedcontent.relatedContentLabel";
    String DOWNLOAD_TITLE = "kpmg.download.downloadText";
    String LOAD_MORE_LABEL = "kpmg.loadmore.loadMoreLabel";
    String ANCHORLINK_HEADLINE = "kpmg.anchorlink.headline";
    String RFP_TITLE = "kpmg.RFPTitle";
    String RFP_BUTTONTEXT = "kpmg.RFPButtonText";
    String PUBLICATIONSERIESLIST_ISSUE = "kpmg.publicationSeriesList.issue";
    String EVENT_TITLE = "kpmg.event.title";
    String EVENT_READMORE = "kpmg.event.readMore";
    String EVENT_REGISTRATION = "kpmg.event.registration";
    String EVENT_RESOURCES = "kpmg.event.resources";
    String PROMO_PRIMARY_LABEL = "kpmg.promotional.primary";
    String AUDIO_VIDEO_VISIT = "kpmg.audiovideo.visitBrightcove";
    String ARTICLE_CAROUSEL_CTA = "kpmg.articleCarousel.cta";
  }

  /** This interface holds I18n keys for LocaltionDetail */
  interface LocationDetail {
    String CONNECTANDRFP_TITLE = "kpmg.cwuTitle";
    String CONNECTANDRFP_SOCIALMEDIALABEL = "kpmg.socialMedia";
    String CONNECTANDRFP_FINDOFFICELOCATIONSLABEL = "kpmg.findOfficeLocations";
    String CONNECTANDRFP_EMAILUSLABEL = "kpmg.emailUs";
    String CONNECTANDRFP_RFPHEADER = "kpmg.connectAndRFP.cwuRfpHeader";
    String ADDRESS_PHONE_NUMBER = "kpmg.address.phone";
    String ADDRESS_FAX_NUMBER = "kpmg.address.fax";
    String ADDRESS_OFFICE_LOCATION = "kpmg.address.officeLocation";
    String ADDRESS_MAILING_ADDRESS = "kpmg.address.mailingAddress";
  }

  /**
   * Contact details i18N keys
   *
   * @author This interface holds I18n keys for ContactDetail
   */
  interface ContactDetail {
    String CONNECTWITHME_TITLE = "kpmg.connectwithmeTitle";
    String CONNECTWITH_TITLE = "kpmg.cwmTitle";
    String CONNECTWITHME_CONTACTLABEL = "kpmg.cwmContactLabel";
    String CONNECTWITHME_RFPHEADER = "kpmg.connectWithMe.cwmRfpHeader";
    String CONTACTTABS_CONNECTIONTAB = "kpmg.contacttabs.connectionTab";
    String CONTACTTABS_PUBLICATIONTAB = "kpmg.contacttabs.publicationTab";
    String CONTACTTABS_QUICKVIEW = "kpmg.contacttabs.quickView";
    String CONTACTTABS_LOADMORE = "kpmg.contacttabs.loadMore";
    String CONTACTTABS_RESULTS = "kpmg.contacttabs.results";
    String GOOGLE_MAP_TITLE_KEY = "kpmg.googleMapTitle";
    String SOCIALWIDGET_TITLE = "kpmg.socialwidget.socialWidgetTitle";
    String CONTACTDETAIL_TITLE = "kpmg.socialwidget.contactDetailTitle";
    String PUBLICATION_NORESULT_MESSAGE = "kpmg.contacttabs.publicationmessage";
    String CONNECTION_NORESULT_MESSAGE = "kpmg.contacttabs.connectionmessage";
    String TAGCLOUD_TITLE = "kpmg.tagcloud.subHeader";
    String READ_MORE = "kpmg.contacttabs.readMore";
  }

  /** This interface holds I18n keys for HomePage */
  interface HomePage {
    String ALL_NAVIGATION_LABEL = "kpmg.navigation.all";
    String READ_MORE = "kpmg.flyoutnavigation.readmore";
    String CAROUSEL_TITLE = "kpmg.articleeventcarousel.title";
    String SITE_SELECTOR_ICON_TOOL_TIP = "kpmg.navigation.siteselectoricontooltip";
    String ACCOUNT_FLYOUT_ICON_TOOL_TIP = "kpmg.navigation.accountflyouticontooltip";
    String SEARCH_ICON_TOOL_TIP = "kpmg.navigation.searchicontooltip";
    String OPEN_IN_NEW_WINDOW = "kpmg.navigation.openInNewWindow";
  }

  /** This interface holds Global i18n keys */
  interface Global {
    String LEGAL_LABEL = "kpmg.footer.legalLabel";
    String PRIVACY_LABEL = "kpmg.footer.privacyLabel";
    String ACCESSIBILITY_LABEL = "kpmg.footer.accessibilityLabel";
    String SITEMAP_LABEL = "kpmg.footer.sitemapLabel";
    String HELP_LABEL = "kpmg.footer.helpLabel";
    String CONTACT_LABEL = "kpmg.footer.contactLabel";
    String CONTACT_US_LABEL = "kpmg.personalization.emailTemplate.contactus";
    String SOCIAL_LABEL = "kpmg.footer.socialLabel";
    String GLOSSARY_LABEL = "kpmg.footer.glossaryLabel";
    String PRIVACY_CANCEL_LABEL = "kpmg.privacyPolicy.cancelLabel";
    String PRIVACY_AGREE_LABEL = "kpmg.privacyPolicy.agreeLabel";
    String PRIVACY_MESSAGE = "kpmg.privacyPolicy.message";
    String PRIVACY_LINK_TEXT = "kpmg.privacyPolicy.linkText";
    String PRIVACY_BUTTON_TEXT = "kpmg.privacyPolicy.buttontext.";
    String PRIVACY_CHECKBOX_TEXT = "kpmg.privacyPolicy.checkboxtext.";
  }

  /** This interface holds I18n keys for CountryLangQuick */
  interface CountryLangQuick {
    String SELECT_MESSAGE = "kpmg.countryLangQuick.selectMsg";
    String ALL_COUNTRY_MSG = "kpmg.countryLangQuick.allCountryMsg";
    String ALL_COUNTRY_LANG_LINK = "kpmg.countryLangQuick.allCountryLangLink";
    String ENGLISH_SITE_LABEL_FOR_LANGUAGE = "kpmg.countryLangQuick.englishLabel";
    String GERMAN_SITE_LABEL_FOR_LANGUAGE = "kpmg.countryLangQuick.germanLabel";
    String SPANISH_SITE_LABEL_FOR_LANGUAGE = "kpmg.countryLangQuick.spanishLabel";
    String GLOBAL = "kpmg.countryLangQuick.global";
    String CLOSE_SITE_SELECTOR = "kpmg.countryLangQuick.closeSiteSelector";
    String SELECT_OTHER_LOCATION = "kpmg.countryLangQuick.selectotherlocation";
    String ENTER_COUNTRY = "kpmg.countryLangQuick.entercountry";
    String ERROR_MESSAGE = "kpmg.countryLangQuick.errormessage";
    String SITE = "kpmg.countryLangQuick.site";
  }

  /** This interface holds I18n keys for CountryLanguage */
  interface CountryLanguage {
    String MCLD_AFRICA = "kpmg.mcld.africa";
    String MCLD_AMERICAS = "kpmg.mcld.americas";
    String MCLD_ASIA = "kpmg.mcld.asia";
    String MCLD_EUROPE = "kpmg.mcld.europe";
    String MCLD_OCEANIA = "kpmg.mcld.oceania";
    String GLOBAL_SITE = "kpmg.mcld.globalSite";
  }

  /** This interface holds I18n keys for ContactList */
  interface ContactList {
    String CONTACT_TITLE = "kpmg.contactList.title";
    String CONTACT_FORM_LINK_LABEL = "kpmg.contactList.ContactFormLink";
  }

  /** This interface holds I18n keys for IndustryLanding */
  interface IndustryLanding {
    String SHORT_CAROUSEL_CTA_LABEL = "kpmg.shortcarousel.ctaLabel";
    String MY_PROMO_TITLE = "kpmg.mykpmgpromo.title";
    String MY_PROMO_DESCRIPTION = "kpmg.mykpmgpromo.description";
    String MY_PROMO_CTA = "kpmg.mykpmgpromo.cta";
    String TRENDINGLIST_TRENDING = "kpmg.trending.trending";
    String TRENDINGLIST_MOSTVIEWED = "kpmg.trending.mostviewed";
  }

  /** This interface holds I18n keys for Contactform */
  interface ContactForm {
    String REQUIRED_LABEL = "kpmg.contactForm.requiredLabel";
    String INQUIRY_LABEL = "kpmg.contactForm.inquiryLabel";
    String INQUIRY_ERROR = "kpmg.contactForm.inquiryError";
    String INQUIRY_ERROR_LABEL = "kpmg.contactForm.inquiryErrorLabel";
    String LOCATION_LABEL = "kpmg.contactForm.locationLabel";
    String LOCATION_HELP_LABEL = "kpmg.contactForm.locationHelpLabel";
    String LOCATION_ERROR = "kpmg.contactForm.locationError";
    String LOCATION_ERROR_LABEL = "kpmg.contactForm.locationErrorLabel";
    String NAME_LABEL = "kpmg.contactForm.nameLabel";
    String NAME_ERROR = "kpmg.contactForm.nameError";
    String NAME_ERROR_LABEL = "kpmg.contactForm.nameErrorLabel";
    String EMAIL_LABEL = "kpmg.contactForm.emailLabel";
    String EMAIL_HELP_LABEL = "kpmg.contactForm.emailHelpLabel";
    String EMAIL_ERROR = "kpmg.contactForm.emailError";
    String EMAIL_ERROR_LABEL = "kpmg.contactForm.emailErrorLabel";
    String PHONE_LABEL = "kpmg.contactForm.phoneLabel";
    String CITY_LABEL = "kpmg.contactForm.cityLabel";
    String CITY_ERROR = "kpmg.contactForm.cityError";
    String COMPANY_LABEL = "kpmg.contactForm.companyLabel";
    String ROLE_LABEL = "kpmg.contactForm.roleLabel";
    String MESSAGE_LABEL = "kpmg.contactForm.messageLabel";
    String MESSAGE_HELP_LABEL = "kpmg.contactForm.messageHelpLabel";
    String MESSAGE_ERROR = "kpmg.contactForm.messageError";
    String MESSAGE_ERROR_LABEL = "kpmg.contactForm.messageErrorLabel";
    String CAPTCHA_LABEL = "kpmg.contactForm.captchaLabel";
    String CAPTCHA_ERROR = "kpmg.contactForm.captchaError";
    String PRIVACTY_POLICY_LABEL = "kpmg.contactForm.privacyPolicyLabel";
    String PRIVACY_POLICY_ERROR = "kpmg.contactForm.privacyPolicyError";
    String TERMS_AND_CONDITIONS = "kpmg.contactForm.terms";
    String TERMS_AND_CONDITIONS_ERROR_LABEL = "kpmg.contactForm.termsErrorLabel";
    String SUBMITTEDDATE_LABEL = "kpmg.contactForm.submittedDateLabel";
    String SUBMITTEDVIA_LABEL = "kpmg.contactForm.submittedViaLabel";

    String SEND_LABEL = "kpmg.contactForm.sendLabel";
    String REFRESH_LABEL = "kpmg.contactForm.refresh";
    String RECEIPT_SHORT_TITLE = "kpmg.receipt.shortTitle";
    String RECEIPT_SHORT_DESCRIPTION = "kpmg.receipt.shortDescription";
    String RECEIPT_PRINT = "kpmg.receipt.print";
    String RECEIPT_CLOSE = "kpmg.receipt.close";
    String SORRY_MESSAGE_LABEL = "kpmg.contactForm.sorryMessage";
    String CHECK_FIELDS_MESSAGE_LABEL = "kpmg.contactForm.checkFieldsMessage";
    String GENERIC_CONTACT_MAIL_SUBJECT = "kpmg.genericContact.mailSubject";
    String GENERIC_CONTACT_MAILBODY_TITLE = "kpmg.genericContact.mailBodyTitle";
    String RECEIPIENT_LABEL = "kpmg.receipt.receipientLabel";
    String REMAINING_CHARACTERS_LABEL = "kpmg.contactForm.remainingCharLabel";
    String CAPTCHA_ERROR_TEXT = "kpmg.contactForm.Captcha";
    String CAPTCHA_DESCRIPTION = "kpmg.contactForm.captchaDescription";
    String CAPTCHA_HELPLABEL = "kpmg.contactForm.captchaHelpLabel";
    String CAPTCHA_REQUIRED = "kpmg.contactForm.captchaRequired";
    String FORM_REQUIRED = "kpmg.contactForm.required";

    String FORM_CONTACT_CANCEL_TITLE = "kpmg.contactForm.cancelTitle";
    String FORM_CONTACT_CANCEL_DESC1 = "kpmg.contactForm.cancelDesc1";
    String FORM_CONTACT_CANCEL_DESC2 = "kpmg.contactForm.cancelDesc2";
    String FORM_CONTACT_CANCEL_CONTINUE = "kpmg.contactForm.cancelContinue";
    String FORM_CONTACT_CANCEL_BACK = "kpmg.contactForm.cancelBack";

    String CAMPAIGN_CONTACT_MAIL_SUBJECT = "kpmg.campaignContact.mailSubject";
    String CAMPAIGN_TITLE = "kpmg.campaignContact.title";
    String CAMPAIGN_DESCRIPTION = "kpmg.campaignContact.description";
  }

  /** This interface holds I18n keys for Events */
  interface Events {
    String AGENDA = "kpmg.events.agenda";
    String SPEAKERS = "kpmg.events.speakers";
    String GUIDE = "kpmg.events.guide";
    String LOAD_MORE = "kpmg.events.loadmore";
    String READ_MORE = "kpmg.eventtabs.readMore";
  }

  /** This interface holds I18n keys for SubNavigation */
  interface SubNavigation {
    String ALL = "kpmg.subnavigation.all";
  }

  /** This interface holds I18n keys for SearchResults */
  interface SearchResults {

    String TAB_ZERO = "kpmg.search.tabZero";
    String TAB_ONE = "kpmg.search.tabOne";
    String TAB_TWO = "kpmg.search.tabTwo";
    String TAB_THREE = "kpmg.search.tabThree";
    String TAB_FOUR = "kpmg.search.searchresults.pressreleases";
    String TAB_FIVE = "kpmg.search.searchresults.blogs";
    String MORE_BUTTON_COPY = "kpmg.search.moreButton";
    String LOAD_ALL = "kpmg.search.loadAll";
    String NO_RESULTS_COPY_LINE_ONE = "kpmg.search.noResultsLineOne";
    String NO_RESULTS_COPY_LINE_TWO = "kpmg.search.noResultsLineTwo";
    String NO_RESULTS_COPY_LINE_THREE = "kpmg.search.noResultsCopyLineThree";
    String NO_RESULTS_COPY_LINE_FOUR = "kpmg.search.noResultsCopyLineFour";
    String NO_RESULTS_COPY_LINE_FIVE = "kpmg.search.noResultsCopyLineFive";
    String NO_RESULTS_COPY_LINE_SIX = "kpmg.search.noResultsCopyLineSix";
    String NO_RESULTS_COPY_LINE_TWO_PART_ONE = "kpmg.search.noResultsLineTwoPartOne";
    String NO_RESULTS_COPY_LINE_TWO_PART_TWO = "kpmg.search.noResultsLineTwoPartTwo";
    String Results_For_CopyOne = "kpmg.search.resultsForCopyOne";

    String BACK_BUTTON_COPY = "kpmg.search.backButton";
    String BROADEN_SEARCH_COPY = "kpmg.search.broadenSearch";
    String RADIO_BUTTON_ONE = "kpmg.search.radioOne";
    String RADIO_BUTTON_TWO = "kpmg.search.radioTwo";
    String SEARCH_BAR_DEFAULT_COPY = "kpmg.search.barDefaultCopy";

    String CLEAR_ALL_BUTTON_COPY = "kpmg.search.clearAll";
    String FACET_MORE_BUTTON_COPY = "kpmg.search.facetsMoreButton";
    String FACET_LESS_BUTTON_COPY = "kpmg.search.facetsLessButton";

    String CLEAR_ALL_SEARCH_RESULTS_BUTTON_COPY = "kpmg.search.searchresults.clearfacets";
    String MORE_SEARCH_RESULTS_BUTTON_COPY = "kpmg.search.searchresults.loadmoreresults";
    String NO_RESULTS_SEARCH_RESULTS_COPY_LINE_ONE = "kpmg.search.searchresults.zeroresultsfor";

    String QUICK_VIEW_DOWNLOAD_COPY = "kpmg.search.quickViewDownloadLink";
    String ALL = "kpmg.search.all";
    String ALL_IN_FACETS = "kpmg.search.allInFacets";

    String QUICK_VIEW_IN_TILE = "kpmg.search.quickViewTile";
    String CONTACT_FORM_LINK_COPY = "kpmg.search.contactFormLinkCopy";
    String VIEW_ICON_COPY = "kpmg.search.viewIcon";
    String SHARE_LABEL = "kpmg.search.shareLabel";

    String INDUSTRY_FACET_RAIL = "kpmg.search.industryFacetRail";
    String VIEW_BY = "kpmg.search.viewBy";
    String RESET_FILTERS = "kpmg.search.resetFilters";
    String RESET_FILTER_CLICK = "kpmg.search.resetFiltersClick";
    String SERVICE_FACET_RAIL = "kpmg.search.serviceFacetRail";
    String BLOG_FACET_RAIL = "kpmg.search.blogFacetRail";
    String TOPIC_FACET_RAIL = "kpmg.search.topicFacetRail";
    String GEO_REL_FACET_RAIL = "kpmg.search.geoRelFacetRail";
    String CONT_TYPE_PATH_FACET_RAIL = "kpmg.search.contTypeFacetRail";
    String MARKET_FACET_RAIL = "kpmg.search.marketFacetRail";
    String EVENT_LOCATION_FACET_RAIL = "kpmg.search.eventLocationFacetRail";
    String CONT_MEMBER_FIRM_FACET_RAIL = "kpmg.search.contMemberFirmFacetRail";
    String KPMG_FILTER_YEAR = "kpmg.search.kpmgFilterYear";
    String CANCEL_BUTTON_COPY = "kpmg.search.cancelButtonCopy";
    String APPLY_BUTTON_COPY = "kpmg.search.applyButtonCopy";
    String SORT_BY_COPY = "kpmg.search.sortByCopy";
    String REFINE_SEARCH_COPY = "kpmg.search.refineSearchCopy";
    String DATE_FACET_RAIL = "kpmg.search.dateCopy";

    // i18n for search suggestions
    String DID_YOU_MEAN_COPY = "kpmg.search.didYouMeanCopy";
    String RESULTS_FOR_COPY = "kpmg.search.resultsForCopy";
    String YOUR_RECENT_SEARCH_COPY = "kpmg.search.yourRecentSearchCopy";
    String CLOSE_SEARCH = "kpmg.search.closeSearch";
    String SORT = "kpmg.search.sort";
    String FILTER = "kpmg.search.filter";

    /** Sorting i18N keys */
    interface Sorting {

      String RELEVANCE = "kpmg.search.sorting.relevance";
      String SORTING_DATE = "kpmg.search.sorting.date";
      String POPULAR = "kpmg.search.sorting.popular";
      String VIEWED = "kpmg.search.sorting.viewed";
    }
  }

  /** ResultListingNoTab i18N keys */
  interface ResultListingNoTab {
    String READ_MORE = "kpmg.resultlistingnotabs.readMore";
    String TAB_ZERO = "kpmg.resultlistingnotabs.tabZero";
    String ALL_IN_FACETS = "kpmg.resultlistingnotabs.allInFacets";
    String ALL = "kpmg.resultlistingnotabs.all";
    String UPCOMING_EVENTS = "kpmg.events.resultslistingnotabs.facet.upcomingevents";
    String EVENTS_THIS_MONTH = "kpmg.events.resultslistingnotabs.facet.thismonth";
  }

  /** This interface holds I18n keys for Social */
  interface Social {
    String ALL = "kpmg.socialTabs.all";
    String BLOGS = "kpmg.socialTabs.blogs";
    String DASHBOARD = "kpmg.socialTabs.dashboard";
    String WIDGET_BUILDER = "kpmg.socialTabs.widgetBuilder";
  }

  /** This interface holds I18n keys for AlumniLinks */
  interface AlumniLinks {
    String ALUMNILINKS_ALUMNISITESTITLE = "kpmg.alumniLinks.alumniSitesTitle";
    String ALUMNILINKS_SITEDESCRIPTION = "kpmg.alumniLinks.alumniSitesTitleDescription";
    String ALUMNILINKS_SITESEARCH = "kpmg.alumnilinks.searchLocation";
    String NORESULTSFOUND = "kpmg.alumnilinks.noResultsFound";
  }

  /** This interface holds I18n keys for RFP */
  interface RFP {

    // Acording to JSON For RFP
    String FORM_RFP_START_TITLE = "kpmg.rfpForm.startTitle";
    String FORM_RFP_CONTINUE = "kpmg.rfpForm.continue";
    String FORM_REQUIRED = "kpmg.rfpForm.required";
    String FORM_RFP_NAME_FIRSTNAME = "kpmg.rfpForm.firstName";
    String FORM_RFP_NAME_FIRSTNAME_ERROR = "kpmg.rfpForm.firstNameError";
    String FORM_RFP_NAME_LASTNAME = "kpmg.rfpForm.lastName";
    String FORM_RFP_NAME_LASTNAME_ERROR = "kpmg.rfpForm.lastNameError";
    String FORM_EMAIL = "kpmg.rfpForm.email";
    String FORM_RFP_EMAIL_ERROR = "kpmg.rfpForm.emailError";
    String FORM_RFP_PHONE = "kpmg.rfpForm.phone";
    String FORM_RFP_PHONE_ERROR = "kpmg.rfpForm.phoneError";
    String FORM_COMPANY_JOBTITLE = "kpmg.rfpForm.jobTitle";
    String FORM_COMPANY_JOBTITLE_ERROR = "kpmg.rfpForm.jobTitleError";
    String FORM_COMPANY_LONG = "kpmg.rfpForm.companyLong";
    String FORM_COMPANY_ERROR = "kpmg.rfpForm.companyError";
    String FORM_COMPANY_INDUSTRY = "kpmg.rfpForm.industry";
    String FORM_COMPANY_INDUSTRY_ERROR = "kpmg.rfpForm.industryError";
    String FORM_COMPANY_SECTOR = "kpmg.rfpForm.sector";
    String FORM_COMPANY_SECTOR_ERROR = "kpmg.rfpForm.sectorError";
    String FORM_COUNTRY = "kpmg.rfpForm.country";
    String FORM_TERRITORY = "kpmg.rfpForm.territory";
    String FORM_TERRITORY_ERROR = "kpmg.rfpForm.territoryError";
    String FORM_MEASSAGES_CLIENT = "kpmg.rfpForm.client";
    String FORM_MEASSAGES_KPMGCLIENT = "kpmg.rfpForm.kpmgclient";
    String FORM_LOCATION = "kpmg.rfpForm.location";
    String FORM_LOCATION_ERROR = "kpmg.rfpForm.locationError";
    String FORM_SELECT = "kpmg.rfpForm.select";
    String FORM_PRINT = "kpmg.rfpForm.print";
    String FORM_BROWSE = "kpmg.rfpForm.browse";
    String FORM_MESSAGE = "kpmg.rfpForm.message";
    String FORM_ERRORMESSAGE_UPLOADING = "kpmg.rfpForm.errorUploading";
    String FORM_RFP_STEPS_START = "kpmg.rfpForm.stepsStart";
    String FORM_RFP_STEPS_STEP1 = "kpmg.rfpForm.stepsStep1";
    String FORM_RFP_STEPS_STEP2 = "kpmg.rfpForm.stepsStep2";
    String FORM_RFP_STEPS_STEP3 = "kpmg.rfpForm.stepsStep3";
    String FORM_RFP_NEXT_LABEL = "kpmg.rfpForm.nextLabel";
    String FORM_RFP_SUBMITRFP_LABEL = "kpmg.rfpForm.submitRfpLabel";
    String FORM_RFP_CLOSE_LABEL = "kpmg.rfpForm.closeLabel";
    String FORM_RFP_CANCEL_LABEL = "kpmg.rfpForm.cancelLabel";
    String FORM_RFP_FILEUPLOADCANCEL_LABEL = "kpmg.rfpForm.cancelFileUpload";
    String FORM_RFP_REMOVE_LABEL = "kpmg.rfpForm.removeLabel";
    String FORM_RFP_BACK_LABEL = "kpmg.rfpForm.backLabel";
    String FORM_RFP_CAPTCHA_REFRESH = "kpmg.rfpForm.captchaRefresh";
    String FORM_RFP_CAPTCHA_LABEL = "kpmg.rfpForm.captchaLabel";
    String FORM_RFP_CAPTCHA_ERROR = "kpmg.rfpForm.captchaError";
    String FORM_RFP_PRIVACTY_POLICY_LABEL = "kpmg.rfpForm.privacyPolicyLabel";
    String FORM_RFP_PRIVACY_POLICY_ERROR = "kpmg.rfpForm.privacyPolicyError";
    String FORM_RFP_PRIVACY_POLICY_TEXT = "kpmg.rfpForm.privacyPolicyText";
    String FORM_RFP_CLOSE_CONFIRMATION = "kpmg.rfpForm.closeRfp";
    String FORM_RFP_ATTACHED_DOCUMENT = "kpmg.rfpForm.attachedDocument";
    String FORM_RFP_REMAINING_CHAR_LABEL = "kpmg.rfpForm.remainingCharLabel";
    String ERRORMESSAGE_FORMS_UPLOADING = "kpmg.rfpForm.errorUploading";
    String FORM_RFP_STEP3_CONTACT_INFO = "kpmg.rfpForm.contactInfo";
    String FORM_RFP_STEP3_REQUEST_DETAILS = "kpmg.rfpForm.requestDetails";

    String ERRORMESSAGE_UPLOAD_LIMIT = "kpmg.rfpForm.errorUploadLimit";
    String ERRORMESSAGE_CONNECTION_ERROR = "kpmg.rfpForm.errorConnectionError";
    String ERRORMESSAGE_INVALID_RFP = "kpmg.rfpForm.errorInvalidRfp";
    String ERRORMESSAGE_INVALID_FILE_TYPE = "kpmg.rfpForm.errorInvalidFileType";
    String ERRORMESSAGE_DUPLICATE_FILE = "kpmg.rfpForm.errorDuplicateFile";
    String ERRORMESSAGE_INVALID_TOKEN = "kpmg.rfpForm.errorInvalidToken";
    String ERRORMESSAGE_GENERAL = "kpmg.rfpForm.errorGeneral";
    String ERRORMESSAGE_FILENAME_SPECIAL_CHAR = "kpmg.rfpForm.errorFileNameSpecialChar";
    String ERRORMESSAGE_EMPTY_FILE = "kpmg.rfpForm.errorEmptyFile";

    String FORM_RFP_CANCEL_TITLE = "kpmg.rfpForm.cancelTitle";
    String FORM_RFP_CANCEL_DESC1 = "kpmg.rfpForm.cancelDesc1";
    String FORM_RFP_CANCEL_DESC2 = "kpmg.rfpForm.cancelDesc2";
    String FORM_RFP_CANCEL_CONTINUE = "kpmg.rfpForm.cancelContinue";
    String FORM_RFP_CANCEL_BACK = "kpmg.rfpForm.cancelBack";
    String FORM_RFP_ALREADYCLIENT_YES = "kpmg.rfpForm.alreadyClientYes";
    String FORM_RFP_ALREADYCLIENT_NO = "kpmg.rfpForm.alreadyClientNo";

    String FORM_RFP_PRINT_INFO_SUBMITTED = "kpmg.rfpForm.printInfoSubmitted";
    String FORM_RFP_RECEIPT_REFERENCE_TEXT = "kpmg.rfpForm.receiptReferenceText";

    String FORM_RFP_IE9_FILEUPLOADMESSAGE = "kpmg.rfpForm.ie9FileUploadMessage";
    String FORM_RFP_IE9_FILEUPLOADTITLE = "kpmg.rfpForm.ie9FileUploadTitle";
    String FORM_RFP_NOFILESELECTED = "kpmg.rfpForm.noFileSelected";
    String FORM_RFP_CAPTCHAHELPLABEL = "kpmg.rfpForm.captchaHelpLabel";
    String FORM_RFP_CAPTCHA_DESCRIPTION = "kpmg.rfpForm.captchaDescription";
    String FORM_RFP_REQUIRED_LABEL = "kpmg.rfpForm.requiredLabel";

    String SORRY_MESSAGE_LABEL = "kpmg.rfpForm.sorryMessage";
    String CHECK_FIELDS_MESSAGE_LABEL = "kpmg.rfpForm.checkFieldsMessage";
    String FORM_RFP_NAME_FIRSTNAME_ERROR_LABEL = "kpmg.rfpForm.firstNameErrorLabel";
    String FORM_RFP_NAME_LASTNAME_ERROR_LABEL = "kpmg.rfpForm.lastNameErrorLabel";
    String FORM_RFP_EMAIL_ERROR_LABEL = "kpmg.rfpForm.emailErrorLabel";

    String FORM_RFP_PHONE_ERROR_LABEL = "kpmg.rfpForm.phoneErrorLabel";
    String FORM_COMPANY_JOBTITLE_ERROR_LABEL = "kpmg.rfpForm.jobTitleErrorLabel";
    String FORM_COMPANY_ERROR_LABEL = "kpmg.rfpForm.companyErrorLabel";
    String FORM_COMPANY_INDUSTRY_ERROR_LABEL = "kpmg.rfpForm.industryErrorLabel";
    String FORM_LOCATION_ERROR_LABEL = "kpmg.rfpForm.locationErrorLabel";
    String FORM_RFP_CAPTCHA_ERROR_LABEL = "kpmg.rfpForm.captchaErrorLabel";

    String FORM_RFP_OVERVIEW_DESCRIPTION = "kpmg.rfpForm.stepsStartDesc";
    String FORM_RFP_OVERVIEW_TERMSANDCONDITIONS = "kpmg.rfpForm.termsAndConditions";
    String FORM_RFP_CONTACT_INFORMATION_DESCRIPTION = "kpmg.rfpForm.stepsStep1Desc";
    String FORM_RFP_REQUEST_DETAILS_DESCRIPTION = "kpmg.rfpForm.stepsStep2Desc";
    String FORM_RFP_REVIEWRFP_DESCRIPTION = "kpmg.rfpForm.stepsStep3Desc";
    String FORM_RFP_REVIEWRFP_DESCRIPTION2 = "kpmg.rfpForm.stepsStep3Desc2";
    String FORM_RFP_CONGRATULATIONS = "kpmg.rfpForm.successTitle";
    String FORM_RFP_SUCCESS_DESC1 = "kpmg.rfpForm.successDesc";
    String FORM_RFP_SUCCESS_DESC2 = "kpmg.rfpForm.successDesc2";
    String FORM_RFP_SUCCESS_DESC3 = "kpmg.rfpForm.successDesc3";
    String FORM_RFP_OVERVIEW_ALTTEXT = "kpmg.rfpForm.stepsStartAltText";
    String FORM_RFP_CONTACT_INFORMATION_ALTTEXT = "kpmg.rfpForm.stepsStep1AltText";
    String FORM_RFP_REQUEST_DETAILS_ALTTEXT = "kpmg.rfpForm.stepsStep2AltText";
    String FORM_RFP_REVIEWRFP_ALTEXT = "kpmg.rfpForm.stepsStep3AltText";
    String FORM_RFP_MESSAGE_LABEL = "kpmg.contactForm.messageLabel";
    String FORM_RFP_TERRITORY_ERROR_LABEL = "kpmg.rfpForm.territoryErrorLabel";
    String FORM_RFP_ORTEXT = "kpmg.rfpform.ortext";
  }

  /** This interface holds I18n keys for Global Date Formats */
  interface GlobalDateFormatMeridiems {
    String GLOBALDATEFORMAT_AM = "kpmg.GlobalDateFormats.AM";
    String GLOBALDATEFORMAT_PM = "kpmg.GlobalDateFormats.PM";
  }

  /** This interface holds I18n keys for the MyKPMGFlyout Component */
  interface MyKpmgFlyout {
    String MY_KPMG = "kpmg.personalization.homepage.mykpmgflyout.mykpmg";
    String LOGIN = "kpmg.personalization.homepage.mykpmgflyout.login";
    String REGISTER = "kpmg.personalization.homepage.mykpmgflyout.register";
    String DASHBOARD = "kpmg.personalization.homepage.mykpmgflyout.dashboard";
    String LIBRARY = "kpmg.personalization.homepage.mykpmgflyout.library";
    String INTERESTS = "kpmg.personalization.homepage.mykpmgflyout.interests";
    String ABOUTMYKPMG = "kpmg.personalization.homepage.mykpmgflyout.aboutMyKpmg";
    String SUBSCRIPTIONS = "kpmg.personalization.homepage.mykpmgflyout.subscriptions";
    String PROFILE = "kpmg.personalization.homepage.mykpmgflyout.profile";
    String LOGOUT = "kpmg.personalization.homepage.mykpmgflyout.logout";
    String LEARNMORE = "kpmg.personalization.homepage.mykpmgflyout.learnMore";
    String CLOSE_MY_KPMG_MENU = "kpmg.mykpmgflyout.closeMyKPMGmenu";
    String MANAGEPROFILE = "kpmg.personalization.homepage.mykpmgflyout.manageprofile";
  }

  /** MyInterests i18N Keys */
  interface MyInterests {
    String SL_INDUSTRIES_LABEL = "kpmg.personalization.myinterests.interests.industriesLabel";
    String SL_INSIGHTS_LABEL = "kpmg.personalization.myinterests.interests.insightsLabel";
    String SL_SERVICES_LABEL = "kpmg.personalization.myinterests.interests.servicesLabel";
    String SL_COUNTRIES_LABEL = "kpmg.personalization.myinterests.interests.countriesLabel";
  }

  /** PartnerLocator i18N keys */
  interface PartnerLocator {
    String COMPONENT_TITLE = "kpmg.partnerlocator.title";
    String COMPONENT_DESC1 = "kpmg.partnerlocator.description1";
    String COMPONENT_DESC2 = "kpmg.partnerlocator.description2";
    String CTA_LABEL = "kpmg.partnerlocator.ctalabel";
    String SELECT_COUNTRIES = "kpmg.partnerlocator.selectcountrytext";
    String SELECT_INDUSTRIES = "kpmg.partnerlocator.selectindustriestext";
    String SELECT_SERVICES = "kpmg.partnerlocator.selectservicestext";
    String NORESULTS = "kpmg.partnerlocator.noresultstext";
    String LOADMORE = "kpmg.partnerlocator.loadmoretext";
    String SEEALLCONTACTS = "kpmg.partnerlocator.seeAllContacts";
  }

  /** OfficeLocator i18N keys */
  interface OfficeLocator {
    String COMPONENT_TITLE = "kpmg.officelocator.title";
    String COMPONENT_DESC = "kpmg.officelocator.description";
    String GEOLOCATION_LABEL = "kpmg.officelocator.geolocationlabel";
    String CTA_LABEL = "kpmg.officelocator.ctalabel";
    String SELECT_COUNTRY = "kpmg.officelocator.selectcountrytext";
    String SELECT_CITY = "kpmg.officelocator.selectcitytext";
    String OR_TEXT = "kpmg.officelocator.ortext";
    String NO_RESULTS = "kpmg.officelocator.noresultstext";
    String LOAD_MORE = "kpmg.officelocator.loadmoretext";
    String LANGUAGE_OVERLAY_TITLE = "kpmg.officelocator.overlaytitletext";
    String LANGUAGE_OVERLAY_DESC = "kpmg.officelocator.overlaydescriptiontext";
    String GEOLOCATION_ERROR = "kpmg.officelocator.geolocationerror";
    String LANGUAGE_OVERLAY_SELECT_TEXT = "kpmg.officelocator.overlayselecttext";
    String NO_CITIES_FOUND = "kpmg.officelocator.nocitiesfoundtext";
  }

  /** PPC i18N keys */
  interface PPC {
    String COMMUNICATIONPREFERENCE_CANCEL = "kpmg.personalization.communicationpreference.cancel";
    String COMMUNICATIONPREFERENCE_SAVE =
        "kpmg.personalization.communicationpreference.saveChanges";
    String EMAIL_DESCRIPTION = "kpmg.personalization.communicationpreference.email";
    String SSMODAL_OPPCHANGE_SALUTATION = "kpmg.personalization.ssmodal.oppchange.salutation";
    String SSMODAL_OPPCHANGE_TEXT1 = "kpmg.personalization.ssmodal.oppchange.text1";
    String SSMODAL_OPPCHANGE_TEXT2 = "kpmg.personalization.ssmodal.oppchange.text2";
    String SSMODAL_CONTINUE_LABEL = "kpmg.personalization.gigya.registration.continue";
    String SSMODAL_CLOSE_LABEL = "kpmg.personalization.myaccount.profile.close";
    String NO_SUBSCRIPTION_TITLE =
        "kpmg.personalization.communicationpreference.nosubscriptions.title";
    String NO_SUBSCRIPTION_DESC =
        "kpmg.personalization.communicationpreference.nosubscriptions.desc";
    String NO_SUBSCRIPTION_DESC1 =
        "kpmg.personalization.communicationpreference.nosubscriptions.desc1";
    String NO_SUBSCRIPTION_DESC2 =
        "kpmg.personalization.communicationpreference.nosubscriptions.desc2";
    String NO_SUBSCRIPTION_DESC3 =
        "kpmg.personalization.communicationpreference.nosubscriptions.desc3";
  }

  /** SiteSelectorPromo i18N keys */
  interface SiteSelectorPromo {

    String SITE_SELECTOR_FEATURE_ITEM_TITLE = "kpmg.siteselectorpromo.featureitemtitle";
    String SITE_SELECTOR_FEATURE_ITEM_DESCRIPTION = "kpmg.siteselectorpromo.featureitemdescription";
  }

  /** FooterCallOut i18N keys */
  interface FooterCallOut {

    String FOOTER_RFP_TITLE = "kpmg.footercallout.footerrfptitle";
    String FOOTER_RFP_DESCRIPTION = "kpmg.siteselectorpromo.footerrfpdescription";
    String FOOTER_SUBSCRIPTION_TITLE = "kpmg.footercallout.footersubscriptiontitle";
    String FOOTER_SUBSCRIPTION_DESCRIPTION = "kpmg.siteselectorpromo.footersubscriptiondescription";
    String FOOTER_SUBSCRIPTION_EMAILTOOLTIP =
        "kpmg.siteselectorpromo.footersubscriptionemailtooltip";
    String FOOTER_SUBSCRIPTION_CTALABEL = "kpmg.siteselectorpromo.footersubscriptionctalabel";
    String FOOTER_RFP_TITLE_MOBILE = "kpmg.footercallout.footerrfptitlemobile";
    String FOOTER_SUBSCRIPTION_TITLE_MOBILE = "kpmg.footercallout.footersubscriptiontitlemobile";
    String FOOTER_SUBSCRIPTION_SUBSCRIBE_NOW = "kpmg.footercallout.subscribenow";
  }

  /** RegistrationPromo i18N Keys */
  interface RegistrationPromo {
    String REGISTRATION_PROMO_TITLE = "kpmg.personalization.registrationpromo.title";
    String REGISTRATION_PROMO_DESCRIPTION = "kpmg.personalization.registrationpromo.description";
    String REGISTRATION_PROMO_OVERLAY_TITLE =
        "kpmg.personalization.registrationpromo.overlay.title";
    String REGISTRATION_PROMO_OVERLAY_DESCRIPTION =
        "kpmg.personalization.registrationpromo.overlay.description";
    String REGISTRATION_PROMO_OVERLAY_REGISTERNOW =
        "kpmg.personalization.registrationpromo.overlay.registernow";
    String KPMG_PERSONALIZATION_REGISTRATION_GIGYA_EMAIL_ADDRESS_IS_INVALID =
        "kpmg.personalization.registration.gigya.email_address_is_invalid";
  }

  /** FormBuilder i18N keys */
  interface FormBuilder {
    String CTA_TITLE = "kpmg.formbuilder.ctatitle";
    String CTA_SHORT_TITLE = "kpmg.formbuilder.shorttitle";
    String CTA_DESCRIPTION = "kpmg.formbuilder.ctadescription";
    String CTA_LABEL = "kpmg.formbuilder.ctalabel";
    String FORM_DEFAULT_MESSAGE = "kpmg.formbuilder.instruction.default";
    String FORM_LOGIN_PROMPT_MESSAGE = "kpmg.formbuilder.instruction.login";
    String FORM_READY_FOR_DOWNLOAD_MESSAGE = "kpmg.formbuilder.instruction.readyfordownload";
    String FORM_READY_TO_REGISTER_MESSAGE = "kpmg.formbuilder.instruction.readytoregister";
    String FORM_READY_TO_SUBSCRIBE_MESSAGE = "kpmg.formbuilder.instruction.readytosubscribe";
    String FORM_OPTIONAL_FIELD_LABEL = "kpmg.formbuilder.optionalfieldlabel";
    String FORM_GENERIC_VALIDATION_ERROR = "kpmg.formbuilder.genericerror";
    String FORM_EMAIL_VALIDATION_ERROR = "kpmg.formbuilder.emailerror";
    String FORM_DOWNLOAD_NOW = "kpmg.formbuilder.downloadnow";
    String FORM_EVENT_CTA_LABEL = "kpmg.formbuilder.formeventctalabel";
    String FORM_GATED_CONTENT_CTA_LABEL = "kpmg.formbuilder.formgatedcontentctalabel";
    String FORM_FAST_SUBSCRIPTION_CTA_LABEL = "kpmg.formbuilder.formfastsubscriptionctalabel";
    String CONGRATULATIONS_MODAL_TITLE = "kpmg.formbuilder.congratulationstitle";
    String CONGRATULATIONS_MODAL_DESCRIPTION = "kpmg.formbuilder.congratulationsdescription";
    String CONGRATULATIONS_MODAL_CLOSE_BUTTON = "kpmg.formbuilder.congratulationsclosebutton";
    String FORM_RELATED_SUBSCRIPTIONS_LABEL = "kpmg.formbuilder.relatedsubscriptionslabel";
    String FORM_DOCUMENT_TITLE_LABEL = "kpmg.formbuilder.documenttitle";
    String FORM_DOCUMENT_STATIC_DESCRIPTION1 =
        "kpmg.personalization.myaccount.profile.static-description1";
    String FORM_DOCUMENT_STATIC_DESCRIPTION2 =
        "kpmg.personalization.myaccount.profile.static-description2";
    String EXPIRED_TOKEN_MODAL_TITLE = "kpmg.formbuilder.expiredtokenmodaltitle";
    String EXPIRED_TOKEN_MODAL_DESCRIPTION = "kpmg.formbuilder.expiredtokenmodaldescription";
    String EXPIRED_TOKEN_MODAL_CLOSE_BUTTON = "kpmg.formbuilder.expiredtokenmodalclosebutton";
    String CORPORATE_MAIL_VALIDATION =
        "kpmg.personalization.myaccount.profile.corporatemailvalidator";
    String SUBSCRIBE_DESCRIPTION1 = "kpmg.formbuilder.subscribedescription1";
    String SUBSCRIBE_DESCRIPTION2 = "kpmg.formbuilder.subscribedescription2";
    String FS_MSG_FOR_ALREADY_REGISTERED_USER =
        "kpmg.formbuilder.fs.messageforalreadyregistereduser";
    String ER_MSG_FOR_ALREADY_REGISTERED_USER =
        "kpmg.formbuilder.er.messageforalreadyregistereduser";
    String GC_MSG_FOR_ALREADY_REGISTERED_USER =
        "kpmg.formbuilder.gc.messageforalreadyregistereduser";
    String GC_VIDEO_MSG_FOR_ALREADY_REGISTERED_USER =
        "kpmg.formbuilder.gc.videomessageforalreadyregistereduser";
    String GC_OTHERASSETS_MSG_FOR_EMAILVERIFICATION =
        "kpmg.formbuilder.gc.otherassets.messageforemailverification";
    String GC_OTHERASSETS_MSG_FOR_ALREADY_REGISTERED_USER =
        "kpmg.formbuilder.gc.otherassets.messageforalreadyregistereduser";
  }

  /** This interface holds I18n keys for unsubscribe pages */
  interface Unsubscribe {
    String UNSUBSCRIBE_VALIDATIONLABEL = "kpmg.personalization.unsubscribe.validationlabel";
    String UNSUBSCRIBE_OPTOUTSUBSCRIPTION = "kpmg.personalization.unsubscribe.optoutsubscription";
    String UNSUBSCRIBE_OPTOUTELECTRONICCOMMUNICATION =
        "kpmg.personalization.unsubscribe.optoutelectroniccommunication";
    String UNSUBSCRIBE_OPTOUTCONFIRMATION = "kpmg.personalization.unsubscribe.optoutconfirmation";
    String UNSUBSCRIBE_OPTOUTCONFIRMATIONDESC =
        "kpmg.personalization.unsubscribe.optoutconfirmationdesc";
    String UNSUBSCRIBE_SUBMIT = "kpmg.personalization.unsubscribe.submit";
    String UNSUBSCRIBE_CANCEL = "kpmg.personalization.unsubscribe.cancel";
    String UNSUBSCRIBE_CLOSE = "kpmg.personalization.myaccount.profile.close";
  }

  /** Industries i18N keys */
  interface Industries {
    String ALL_INDUSTRIES_TITLE = "kpmg.industries.industriesGrid.allIndustriesCTATitle";
    String INDUSTRIES_TITLE = "kpmg.industries.industriesGrid.industriesTitle";
    String INDUSTRIES_CTA_TITLE = "kpmg.industries.industriesGrid.landingPageCTATitle";
    String INDUSTRIES_MINIMIZE = "kpmg.industries.industriesGrid.minIndustries";
  }

  /** Services i18N keys */
  interface Services {
    String ALL_SERVICES_TITLE = "kpmg.services.serviceGrid.allServicesCTATitle";
    String SERVICES_TITLE = "kpmg.services.serviceGrid.servicesTitle";
    String SERVICES_CTA_TITLE = "kpmg.services.serviceGrid.landingPageCTATitle";
    String SERVICES_MINIMIZE = "kpmg.services.serviceGrid.minServices";
  }

  /** EngagementPromo i18N keys */
  interface EngagementPromo {
    String MANAGE_SUBSCRIPTIONS = "kpmg.engagementpromo.loggedin.managesubscriptions";
    String NEW_USER_MESSAGE = "kpmg.engagementpromo.loggedin.newusermessage";
    String LOGIN_REGISTER = "kpmg.engagementpromo.login.loginregistercta";
    String LOGIN_TITLE = "kpmg.engagementpromo.login.logintitle";
    String UNKNOWN_USER_MESSAGE = "kpmg.engagementpromo.login.unknownusermessage";
    String WELCOME = "kpmg.engagementpromo.loggedin.welcome";
    String NON_NEW_USER_MESSAGE = "kpmg.engagementpromo.loggedin.nonnewusermessage";
    String SUBSCRIPTION_TITLE = "kpmg.engagementpromo.subscriptiontitle";
    String EMAIL_TOOLTIP = "kpmg.engagementpromo.subscriptionemailtooltip";
    String SUBSCRIPTION_CTA_LABEL = "kpmg.engagementpromo.subscriptionctalabel";
    String EMAIL_ADDRESS_INVALID = "kpmg.engagementpromo.email_address_is_invalid";
    String SUBSCRIPTION_DESCRIPTION = "kpmg.engagementpromo.subscriptiondescription";
    String DASHBOARD_CTA = "kpmg.engagementpromo.dashboardCTA";
    String TITLE = "kpmg.engagementpromo.rfptitle";
    String DESCRIPTION = "kpmg.engagementpromo.rfptext";
    String CTA_TEXT = "kpmg.engagementpromo.rfpbuttontext";
  }

  /**
   * Site map i18N Keys
   *
   * @author gajakhil
   */
  interface SiteMap {
    String NO_RESULTS_FOUND = "kpmg.sitemap.noResultsFound";
    String SEARCH_SITEMAP = "kpmg.sitemap.searchSiteMap";
  }

  /**
   * Add to library i18N Keys
   *
   * @author gajakhil
   */
  interface AddToLibrary {
    String ADD_TO_LIBRARY = "kpmg.personalization.library.sharedlist.addtolibrary";
  }
}
