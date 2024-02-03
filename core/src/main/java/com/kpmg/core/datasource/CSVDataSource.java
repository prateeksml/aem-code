package com.kpmg.core.datasource;

import com.adobe.acs.commons.wcm.datasources.DataSourceBuilder;
import com.adobe.acs.commons.wcm.datasources.DataSourceOption;
import com.day.text.csv.Csv;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.Servlet;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/** A datasource for A CSV text. */
@Component(service = {Servlet.class})
@SlingServletResourceTypes(
    resourceTypes = CSVDataSource.RESOURCE_TYPE,
    methods = HttpConstants.METHOD_GET,
    extensions = "html")
@Log4j
public class CSVDataSource extends AbstractDataSourceServlet {

  public static final String RESOURCE_TYPE = "kpmg/datasource/csv";
  public static final String P_CSV = "csv";
  public static final String P_CSV_FILE = "csvFile";

  @Reference transient DataSourceBuilder dataSourceBuilder;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
    String csv = getConfig(request).get(P_CSV);
    String csvFile = getConfig(request).get(P_CSV_FILE);
    if (StringUtils.isNotBlank(csv)) {
      // handle csv string
      dataSourceBuilder.addDataSource(request, getOptionsFromCSV(csv));
    } else if (StringUtils.isNotBlank(csvFile)) {
      // handle csv file
      dataSourceBuilder.addDataSource(request, getOptionsFromCSVFile(csvFile, request));
    }
  }

  private List<DataSourceOption> getOptionsFromCSVFile(
      String csvFile, SlingHttpServletRequest request) {

    return Optional.ofNullable(csvFile)
        .map(filePath -> request.getResourceResolver().getResource(filePath))
        .map(fileResource -> fileResource.adaptTo(InputStream.class))
        .map(this::getOptionsFromCSVInputStream)
        .orElse(new ArrayList<>());
  }

  private List<DataSourceOption> getOptionsFromCSVInputStream(InputStream fileInputStream) {
    List<DataSourceOption> options = new ArrayList<>();
    try (fileInputStream) {

      Csv csv = new Csv();
      csv.read(fileInputStream, StandardCharsets.UTF_8.name())
          .forEachRemaining(
              row -> {
                // assume first column is text, second column is value
                String text = ArrayUtils.get(row, 0);
                String value = ArrayUtils.get(row, 1);
                options.add(new DataSourceOption(text, value));
              });

      csv.close();
    } catch (IOException e) {
      log.error("Error reading CSV file", e);
    }
    return options;
  }

  private List<DataSourceOption> getOptionsFromCSV(String csv) {
    return Optional.ofNullable(csv)
        .map(c -> StringUtils.split(c, ","))
        .map(this::toOptions)
        .orElseGet(ArrayList::new);
  }

  private List<DataSourceOption> toOptions(String[] items) {
    return Arrays.stream(items)
        .map(String::trim)
        .map(this::newDataSourceFromCSVItem)
        .collect(Collectors.toList());
  }

  private DataSourceOption newDataSourceFromCSVItem(String item) {
    String text = StringUtils.substringBefore(item, ":");
    String value = StringUtils.substringAfter(item, ":");
    return new DataSourceOption(text, value);
  }
}
