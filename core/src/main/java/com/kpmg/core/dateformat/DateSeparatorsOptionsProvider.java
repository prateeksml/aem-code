package com.kpmg.core.dateformat;

import com.kpmg.core.annotations.MigratedCodeExcludeFromCodeCoverageReportGenerated;
import io.wcm.caconfig.editor.DropdownOptionItem;
import io.wcm.caconfig.editor.DropdownOptionProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;

@MigratedCodeExcludeFromCodeCoverageReportGenerated // please read annotation documentation.
@Component(
    immediate = true,
    service = DropdownOptionProvider.class,
    property = {DropdownOptionProvider.PROPERTY_SELECTOR + "=kpmg.dateseparatorsprovider"})
public class DateSeparatorsOptionsProvider implements DropdownOptionProvider {

  @Override
  public List<DropdownOptionItem> getDropdownOptions(Resource contextResource) {
    return DateFormatHelper.getAllSeparators().entrySet().stream()
        .map(entry -> new DropdownOptionItem(entry.getValue(), entry.getKey()))
        .collect(Collectors.toList());
  }
}
