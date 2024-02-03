package com.kpmg.integration.services;

public interface DocumentTypeService {
  boolean isValidTemplate(String templatePath);

  String getDocumentType(String templatePath);
}
