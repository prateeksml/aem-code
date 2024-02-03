package com.kpmg.integration.services.impl;

import com.day.cq.wcm.api.Page;
import com.kpmg.integration.constants.Constants;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.models.impl.*;
import com.kpmg.integration.models.impl.ArticleDocumentImpl;
import com.kpmg.integration.models.impl.PeopleDocumentImpl;
import com.kpmg.integration.services.DocumentModelService;
import com.kpmg.integration.services.DocumentTypeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = DocumentModelService.class, immediate = true)
public class DocumentModelServiceImpl implements DocumentModelService {

  private static final Logger log = LoggerFactory.getLogger(DocumentModelServiceImpl.class);

  @Reference DocumentTypeService documentTypeService;

  @Override
  public PageDocument getPageDocumentModel(Page page) {
    if (null != page.getTemplate()) {
      String templatePath = page.getTemplate().getPath();
      Resource pageResource = page.getContentResource();
      String documentType = documentTypeService.getDocumentType(templatePath);
      if (!ResourceUtil.isNonExistingResource(pageResource)) {
        if (StringUtils.equalsIgnoreCase(Constants.EVENT, documentType)) {
          return pageResource.adaptTo(EventDocumentImpl.class);
        }
        if (StringUtils.equalsIgnoreCase(Constants.INSIGHT, documentType)) {
          return pageResource.adaptTo(ArticleDocumentImpl.class);
        }
        if (StringUtils.equalsIgnoreCase(Constants.CONTACT, documentType)) {
          return pageResource.adaptTo(PeopleDocumentImpl.class);
        }
        if (StringUtils.equalsIgnoreCase(Constants.GENERIC, documentType)) {
          return pageResource.adaptTo(GenericDocumentImpl.class);
        } else {
          log.error("Could not adapt to model");
        }
      }
    }
    log.error("Resource {} Not Exist", page.getContentResource().getPath());
    return null;
  }
}
