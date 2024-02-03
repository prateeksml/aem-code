package com.kpmg.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
    label = "Site Settings Configuration",
    description =
        "A collection of configurations defined at a site-level to support dynamic component and service behaviours across the platform")
public @interface SiteSettingsConfig {

  // General Configuration
  @Property(label = "Country Code", description = "Country code for current site", order = 0)
  String siteCountry() default "";

  @Property(label = "Language Code", description = "Language Code for current site", order = 1)
  String siteLocale() default "";
  // Search
  @Property(
      label = "Search Page Path Location",
      description = "The content path reference for Search Page",
      property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
      order = 2)
  String searchPagePath() default "";

  @Property(
      label = "Social Media Label",
      description = "Provide the label for Social Share",
      order = 3)
  String shareLabel() default "Share";

  @Property(label = "Enable Facebook share", order = 4)
  boolean enableFacebook();

  @Property(label = "Enable Twitter share", order = 5)
  boolean enableTwitter();

  @Property(label = "Enable Linkedin share", order = 6)
  boolean enableLinkedin();

  @Property(
      label = "One Trust Script",
      description = "One Trust cookies consent script",
      property = {"widgetType=textarea"},
      order = 7)
  String oneTrust() default "";

  @Property(label = "One Trust Configuration", order = 8)
  CookieMappingConfig[] items();

  public @interface CookieMappingConfig {

    @Property(
        label = "Attribute",
        description = "Select cookie attribute ID",
        property = {
          "widgetType=dropdown",
          "dropdownOptions=["
              + "{'text':'Google Recaptcha','value':'google-recaptcha','description':'Google Recaptcha'},"
              + "{'text':'DemandBase','value':'dmdbase','description':'DemandBase'},"
              + "{'text':'PodBean','value':'podbean','description':'PodBean'},"
              + "{'text':'Adobe Launch','value':'launch','description':'Adobe Launch' }"
              + "]"
        })
    String attribute() default "google-recaptcha";

    @Property(
        label = "Category Name",
        description = "Select a category name that mapped to cookie attribute",
        property = {
          "widgetType=dropdown",
          "dropdownOptions=["
              + "{'text':'Strictly Necessary','value':'C0001','description':'Strictly Necessary'},"
              + "{'text':'Performance','value':'C0002','description':'Performance'},"
              + "{'text':'Functionality','value':'C0003','description':'Functionality'},"
              + "{'text':'Targeting/Advertising','value':'C0004','description':'Targeting/Advertising'},"
              + "{'text':'Social','value':'C0005','description':'Social'}"
              + "]"
        })
    String categoryName() default "C0001";
  }
  // Taxonomy
  @Property(label = "Smart Logic Configuration", order = 9)
  SmartLogic smartLogic();

  public @interface SmartLogic {

    @Property(
        label = "Smartlogic - Local Country ZThesID",
        description =
            "Provide the ZthesID for Local Model Country. Copy this value from Smartlogic product.",
        order = 1)
    String localModelCountryZthesID() default "";

    @Property(
        label = "Smartlogic - Local Industry ZThesID",
        description =
            "Provide the ZthesID for Local Model Industry. Copy this value from Smartlogic product.",
        order = 2)
    String localModelIndustryZthesID() default "";

    @Property(
        label = "Smartlogic - Local Service ZThesID",
        description =
            "Provide the ZthesID for Local Model Service. Copy this value from Smartlogic product.",
        order = 3)
    String localModelServiceZthesID() default "";

    @Property(
        label = "Smartlogic - Local Insight ZThesID",
        description =
            "Provide the ZthesID for Local Model Insight. Copy this value from Smartlogic product.",
        order = 4)
    String localModelInsightZthesID() default "";

    @Property(
        label = "Smartlogic Language",
        description = "Provide the language used in SmartLogic Taxonomy. For eg: en_us",
        order = 5)
    String sesLanguage() default "en_us";
  }

  @Property(label = "Contact Form Configuration", order = 10)
  ContactForm contactForm();

  public @interface ContactForm {
    @Property(
        label = "Location/Path",
        description = "The content path reference for Contact Form Page",
        property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
        order = 1)
    String contactFormPath();

    @Property(
        label = "External Email Template Path",
        description = "The contact form email template path",
        property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
        order = 2)
    String contactFormEmailTemplatePath();

    @Property(
        label = "Internal  Email Template Path",
        description = "The internal contact form email template path",
        property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
        order = 3)
    String internalContactEmailTemplate();

    @Property(
        label = "Email address list for Contact Form",
        description = "A list of email address to submit contact form submissions for this locale",
        order = 4)
    String[] contactFormEmailAddresses() default {};
  }

  @Property(label = "People contact Form Configuration", order = 11)
  PeopleContactForm peopleForm();

  public @interface PeopleContactForm {
    @Property(
        label = "Location / Path",
        description = "The content path reference for People Contact Form Page",
        property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
        order = 1)
    String peopleContactFormPath();

    @Property(
        label = "External Email Template Path",
        description = "The people contact form email template path",
        property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
        order = 2)
    String peopleContactFormEmailTemplatePath();

    @Property(
        label = "Internal Email Template Path",
        description = "The internal people contact form email template path",
        property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
        order = 3)
    String internalPeopleContactEmailTemplate();

    @Property(
        label = "Email address list for People Contact Form",
        description =
            "A list of email address to submit People Contact Form submissions for this locale",
        order = 4)
    String[] peoplecontactFormEmailAddresses() default {};
  }

  @Property(label = "RFP Form Configuration", order = 12)
  RFPForm rfpForm();

  public @interface RFPForm {

    @Property(label = "RFP Country ID", description = "RFP Country ID for current site", order = 1)
    String rfpcountryID() default "";

    @Property(
        label = "Location / Path",
        description = "The content path reference for RFP Page",
        property = {"widgetType=pathbrowser", "pathbrowserRootPathContext=true"},
        order = 2)
    String rfpFormPath() default "";
  }

  @Property(
      label = "Form Fields Mapping Configuration",
      description =
          "A collection of configurations defined at a site-level for the forms to add Form Mapping fields across the platform",
      order = 13)
  FormfieldMappingConfig formMapping();

  public @interface FormfieldMappingConfig {

    @Property(label = "First Name", order = 0)
    String firstName() default "first_name";

    @Property(label = "Last Name", order = 1)
    String lastName() default "last_name";

    @Property(label = "Email Address", order = 2)
    String email() default "email_address";

    @Property(label = "Country", order = 3)
    String country() default "country";

    @Property(label = "City", order = 4)
    String city() default "city";

    @Property(label = "Phone Number", order = 5)
    String phone() default "phone";

    @Property(label = "Company", order = 6)
    String company() default "company";

    @Property(label = "Role", order = 7)
    String role() default "role";

    @Property(label = "Message", order = 8)
    String message() default "message";

    @Property(label = "Inquiry", order = 9)
    String inquiry() default "inquiry";

    @Property(label = "State", order = 10)
    String state() default "state";

    @Property(label = "Industry", order = 11)
    String industry() default "industry";

    @Property(label = "IsKpmgClient", order = 12)
    String iskpmgclient() default "IsKpmgClient";

    @Property(label = "Attachments", order = 13)
    String attachment() default "attachment";
  }

  @Property(
      label = "Regular Expression Mapping Configuration",
      description =
          "A collection of configurations defined at a site-level for the forms to add regular expressions to the form fields across the platform",
      order = 14)
  RegexMappingConifg regexMapping();

  public @interface RegexMappingConifg {

    @Property(label = "Alphabets", order = 0)
    String alphabets() default "^[a-zA-Z ]*$";

    @Property(label = "Alphanumerics", order = 1)
    String alphanumeric() default "^[A-Za-z0-9]+$";

    @Property(label = "Numerics", order = 2)
    String numeric() default "^[0-9]*$";

    @Property(label = "Email", order = 3)
    String email() default "[a-zA-Z0-9\\._\\+\\-]+@[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z0-9\\.\\-]+";

    @Property(label = "Phone", order = 4)
    String phone() default "[0-9-]+";
  }
}
