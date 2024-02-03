package com.kpmg.integration.services.impl;

import com.kpmg.integration.services.DocumentTypeService;
import com.kpmg.integration.services.TemplateMappingService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = DocumentTypeService.class, immediate = true)
public class DocumentTypeServiceImpl implements DocumentTypeService {

  private final List<TemplateMappingService> templateMappingServices = new CopyOnWriteArrayList<>();

  @Reference(
      name = "Mapping index for the Path",
      cardinality = ReferenceCardinality.MULTIPLE,
      policy = ReferencePolicy.DYNAMIC)
  protected final void bindTemplateMappingService(
      final TemplateMappingService templateMappingService) {
    this.templateMappingServices.add(templateMappingService);
  }

  protected synchronized void unbindTemplateMappingService(
      final TemplateMappingService templateMappingService) {
    this.templateMappingServices.remove(templateMappingService);
  }

  @Override
  public boolean isValidTemplate(String templatePath) {
    List<String> templatePaths = new ArrayList<>();
    for (var templateMappingService : templateMappingServices) {
      templatePaths.addAll(Arrays.asList(templateMappingService.getTemplates()));
    }
    return templatePaths.contains(templatePath);
  }

  public String getDocumentType(String templatePath) {
    List<String> templatePaths;
    String documentType = null;
    for (var templateMappingService : templateMappingServices) {
      templatePaths = Arrays.asList(templateMappingService.getTemplates());
      if (templatePaths.contains(templatePath)) {
        documentType = templateMappingService.getDocumentType();
      }
    }
    return documentType;
  }
}
