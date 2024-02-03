package com.kpmg.integration.models.impl;

import static com.kpmg.integration.constants.Constants.CONTACT;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpmg.integration.models.PageDocument;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = Resource.class,
    adapters = {PageDocument.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PeopleDocumentImpl extends GenericDocumentImpl {
  private static final Logger LOG = LoggerFactory.getLogger(PeopleDocumentImpl.class);

  @JsonProperty("contact_first_name")
  @Getter
  private String firstName;

  @JsonProperty("contact_last_name")
  @Getter
  private String lastName;

  @JsonProperty("contact_email_address")
  @Getter
  private String emailAddress;

  @JsonProperty("contact_telephone_number")
  @Getter
  private String phoneNumber;

  @JsonProperty("contact_city")
  @Getter
  private String city;

  @JsonProperty("contact_state")
  @Getter
  private String state;

  @JsonProperty("contact_country")
  @Getter
  private String country;

  @JsonProperty("contact_bio")
  @Getter
  private String biography;

  @JsonProperty("contact_member_firm")
  @Getter
  private String memberFirm;

  @JsonProperty("contact_type")
  @Getter
  private String contactType;

  @JsonProperty("contact_salutation")
  @Getter
  private String salutation;

  @JsonProperty("contact_suffix")
  @Getter
  private String suffix;

  @JsonProperty("contact_middle_initial")
  @Getter
  private String middleInitial;

  @JsonProperty("contact_job_title")
  @Getter
  private String jobTitle;

  @Getter
  @ValueMapValue(name = "fragmentPath")
  private String contentFragmentPath;

  @Override
  public String getDocumentType() {
    return CONTACT;
  }

  @PostConstruct
  protected void init() {
    super.init();
    LOG.info("entered into init {}", contentFragmentPath);
    Resource cfResource =
        Optional.ofNullable(resourceResolver)
            .map(resolver -> resolver.getResource(this.contentFragmentPath))
            .orElse(null);
    if (null != cfResource) {
      ContentFragment contentFragment = cfResource.adaptTo(ContentFragment.class);
      if (null != contentFragment) {
        this.firstName = getCFValue(contentFragment, "firstName");
        this.lastName = getCFValue(contentFragment, "lastName");
        this.emailAddress = getCFValue(contentFragment, "email");
        this.phoneNumber = getCFValue(contentFragment, "phoneNumber");
        this.city = getCFValue(contentFragment, "city");
        this.state = getCFValue(contentFragment, "state");
        this.country = getCFValue(contentFragment, "country");
        this.biography = getCFValue(contentFragment, "biography");
        this.memberFirm = getCFValue(contentFragment, "kpmgMemberFirm");
        this.contactType = getCFValue(contentFragment, "contactType");
        this.salutation = getCFValue(contentFragment, "salutation");
        this.suffix = getCFValue(contentFragment, "suffix");
        this.middleInitial = getCFValue(contentFragment, "middleInitial");
        this.jobTitle = getCFValue(contentFragment, "jobTitle");
      }
    }
  }

  private String getCFValue(ContentFragment contentFragment, String element) {
    if (null != contentFragment.getElement(element)) {
      return contentFragment.getElement(element).getContent();
    }
    return null;
  }
}
