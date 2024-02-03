package com.kpmg.integration.services;

import com.day.cq.wcm.api.Page;
import com.kpmg.integration.models.PageDocument;

public interface DocumentModelService {

  PageDocument getPageDocumentModel(Page page);
}
