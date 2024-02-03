package com.kpmg.integration.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface PrivacyTextRTE {

  String getText();

  String getId();
}
