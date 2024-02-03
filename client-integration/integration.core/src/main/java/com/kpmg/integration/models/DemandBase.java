package com.kpmg.integration.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface DemandBase {

  String getDemandbaseURL();

  String getDemandbaseCurl();
}
