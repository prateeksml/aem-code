package com.kpmg.core.models;

import com.adobe.acs.commons.reports.api.ReportCellCSVExporter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CFLinkReportCellCSVExporter implements ReportCellCSVExporter {

  @Override
  public String getValue(Object result) {
    final Resource resource = (Resource) result;
    final CFReportCellValue val = new CFReportCellValue(resource);
    return val.getCfEditLink();
  }
}
