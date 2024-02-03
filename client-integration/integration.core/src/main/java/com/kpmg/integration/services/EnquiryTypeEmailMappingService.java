package com.kpmg.integration.services;

public interface EnquiryTypeEmailMappingService {

  String[] getEmailBasedOnEnquiryType(String enquiryName, String contentFragmentPath);
}
