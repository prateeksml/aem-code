package com.kpmg.integration.models;

import com.adobe.cq.export.json.ComponentExporter;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface RFPSubmission extends ComponentExporter {

  String getRfpTitle();

  String getRfpReference();

  String getBelowListMessage();

  String getContactInfoTitle();

  String getRequestDetailTitle();

  String getButtonText();
}
