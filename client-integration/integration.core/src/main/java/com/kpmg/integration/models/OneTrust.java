package com.kpmg.integration.models;

import java.util.HashMap;
import java.util.List;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface OneTrust {

  String getOneTrustScript();

  List<String> getAttributeOptions();

  List<String> getCategoryOptions();

  HashMap<String, List<String>> getMapOptions();
}
