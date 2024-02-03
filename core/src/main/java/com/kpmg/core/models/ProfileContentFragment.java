package com.kpmg.core.models;

import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import java.util.List;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface ProfileContentFragment {

  String getImage();

  String getImageDescription();

  String getImageAltText();

  String getContactType();

  String getMemberFirm();

  String getBusinessOwnerGroup();

  String getSalutation();

  String getFirstName();

  String getMiddleInitial();

  String getLastName();

  String getSuffix();

  String getJobTitle();

  String getEmail();

  String getPhoneNumber();

  String getCity();

  String getCountry();

  String getBiography();

  ComponentData getPhoneData();

  ComponentData getEmailData();

  String getTitle();

  List<SocialLink> getSocialLinks();
}
