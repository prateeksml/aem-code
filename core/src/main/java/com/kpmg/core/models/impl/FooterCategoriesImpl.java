package com.kpmg.core.models.impl;

import com.adobe.acs.commons.models.injectors.annotation.ChildResourceFromRequest;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.kpmg.core.models.FooterCategories;
import com.kpmg.core.models.FooterCategory;
import com.kpmg.core.utils.CollectionsUtil;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {FooterCategories.class, ComponentExporter.class},
    resourceType = FooterCategoriesImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
@Log4j
public class FooterCategoriesImpl implements FooterCategories {

  public static final String RESOURCE_TYPE = "kpmg/components/navigation/footer/categories";

  @ChildResourceFromRequest List<FooterCategory> items;

  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }

  @Override
  public List<FooterCategory> getItems() {
    return CollectionsUtil.unmodifiableListOrEmpty(items);
  }
}
