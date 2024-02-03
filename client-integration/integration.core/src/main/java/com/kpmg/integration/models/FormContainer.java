package com.kpmg.integration.models;

import com.adobe.cq.wcm.core.components.models.form.Container;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface FormContainer extends Container {

  String getSiteKeyV3();

  String getSiteSecretV3();

  String getSiteKeyV2();

  String getSiteSecretV2();

  String getResponseType();

  String getFormType();

  String getSuccessTitle();

  String getSuccessDescription();

  boolean isFirstName();

  String getSysError();

  String getValidationError();

  String getCtaLink();

  String getCtaLabel();

  String getCtaPublishLink();

  String getFormTitle();

  String getCloseButtonTitle();

  String getLoadingTitle();

  String getLoadingSubtitle();

  String getLoadingText();

  String getResourcePath();

  String getAnalyticId();

  String getCountryId();

  Boolean isCaptchaEnabled();

  String getCaptchaScript();

  String getCaptchaVersion();

  String getCaptchaSize();

  String getFormfieldMappingConfig();
}
