package com.kpmg.core.datasource;

import com.adobe.acs.commons.wcm.datasources.DataSourceBuilder;
import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.day.util.TimeZoneUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.servlet.Servlet;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {Servlet.class})
@SlingServletResourceTypes(
    resourceTypes = TimeZoneDataSource.RESOURCE_TYPE,
    methods = HttpConstants.METHOD_GET,
    extensions = "html")
@Log4j
public class TimeZoneDataSource extends AbstractDataSourceServlet {

  private static final long serialVersionUID = 1L;

  public static final String RESOURCE_TYPE = "kpmg/datasource/timezone";

  @Reference transient DataSourceBuilder dataSourceBuilder;

  @Override
  protected void doGet(
      final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
    dataSourceBuilder.addDataSource(request, getTimezone());
  }

  private List<DataSourceOption> getTimezone() {
    final TimeZone[] tzArray = TimeZoneUtil.getAvailableZones();
    return Arrays.stream(tzArray)
        .map(
            timeZone -> {
              final String displayName = TimeZoneUtil.getDisplayName(timeZone);
              if (StringUtils.contains(displayName, "GMT")) {
                log.debug("Value removed from Timezone list: " + displayName);
                return null;
              }
              final double decimalTime =
                  Double.parseDouble(StringUtils.substring(displayName, 7, 10));
              final double mins = decimalTime * 60;
              final String text =
                  String.format(
                      "(%s:%s)%s %s",
                      displayName.subSequence(1, 7),
                      String.format("%02d", (int) mins),
                      timeZone.getID(),
                      timeZone.getDisplayName(false, TimeZone.LONG));

              final String value = timeZone.getDisplayName(false, TimeZone.SHORT);
              return new DataSourceOption(text, value);
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
