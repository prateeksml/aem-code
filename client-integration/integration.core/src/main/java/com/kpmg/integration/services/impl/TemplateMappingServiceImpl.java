package com.kpmg.integration.services.impl;

import com.kpmg.integration.config.ElasticSearchTemplateConfiguration;
import com.kpmg.integration.services.TemplateMappingService;
import org.apache.commons.lang3.ArrayUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(
    service = TemplateMappingService.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = ElasticSearchTemplateConfiguration.class, factory = true)
public class TemplateMappingServiceImpl implements TemplateMappingService {

  private String documentType;
  private String[] templates;

  @Activate
  @Modified
  protected void activate(ElasticSearchTemplateConfiguration configuration) {
    this.documentType = configuration.getDocumentType();
    this.templates = configuration.getTemplates();
  }

  @Override
  public String getDocumentType() {
    return this.documentType;
  }

  @Override
  public String[] getTemplates() {
    return ArrayUtils.clone(this.templates);
  }
}
