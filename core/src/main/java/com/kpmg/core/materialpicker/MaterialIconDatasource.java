package com.kpmg.core.materialpicker;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

@Component(service = {Servlet.class})
@SlingServletResourceTypes(
    resourceTypes = MaterialIconDatasource.RESOURCE_TYPE,
    methods = HttpConstants.METHOD_GET,
    extensions = "html")
public class MaterialIconDatasource extends SlingSafeMethodsServlet {
  public static final String RESOURCE_TYPE = "kpmg/components/editor/iconfield/icondatasource";
  public static final String P_QUERY = "query";
  public static final String P_ICON_VARIANT = "iconVariant";

  public static final String MATERIAL_ICON_RESOURCE = "";
  static Gson gson = new Gson();
  transient MaterialIcons materialIcons;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws IOException {
    String query = request.getParameter(P_QUERY);
    int offset = getIntegerSelectorAtIndex(2, request, 0);
    final String iconVariant = request.getParameter(P_ICON_VARIANT);
    final DataSource ds = searchIcon(query, request.getResourceResolver(), offset, iconVariant);
    request.setAttribute(DataSource.class.getName(), ds);
  }

  /**
   * Parse Material Icons Json File.
   *
   * @return MaterialIcons representation of the json file.
   */
  MaterialIcons getMaterialIcons() throws IOException {
    if (materialIcons == null) {
      try (InputStream materialJsonFileIs = getFileAsIOStream("material-icons.json"); ) {
        materialIcons =
            gson.fromJson(new InputStreamReader(materialJsonFileIs), MaterialIcons.class);
      }
    }
    return materialIcons;
  }

  /** Get sling request selector at specified index. */
  int getIntegerSelectorAtIndex(int index, SlingHttpServletRequest request, int defaultResult) {
    return Optional.ofNullable(request)
        .map(SlingHttpServletRequest::getRequestPathInfo)
        .map(RequestPathInfo::getSelectors)
        .filter(selectors -> selectors.length >= index + 1)
        .map(selectors -> selectors[index])
        .map(selector -> Integer.parseInt(selector))
        .orElse(defaultResult);
  }

  /** Search for icon by query. Query will match the icon name, catigory or tag. */
  public DataSource searchIcon(
      String query, ResourceResolver resourceResolver, int offset, String iconVariant)
      throws IOException {
    Stream<Icon> resourceStream = Arrays.stream(getMaterialIcons().icons);
    if (StringUtils.isNoneBlank(query)) {
      resourceStream = resourceStream.filter(i -> filterIcon(query, i));
    }

    Iterator<Resource> iconIterator =
        resourceStream
            .skip(offset)
            .map(
                icon ->
                    (Resource)
                        new MaterialIconResource(
                            resourceResolver, icon.name, icon.name, iconVariant))
            .collect(Collectors.toList())
            .iterator();

    return new SimpleDataSource(iconIterator);
  }
  /** returns true if query match any part of the string in icon name, categories or tags. */
  boolean filterIcon(String query, Icon icon) {
    boolean include =
        StringUtils.contains(query, icon.name)
            || matchItemInArray(query, icon.tags)
            || matchItemInArray(query, icon.categories);
    return include;
  }

  /** match query againts any part of the string in the provided array. */
  boolean matchItemInArray(String query, String[] array) {
    if (array == null || array.length == 0) return false;
    return Arrays.stream(array).anyMatch(arrayItem -> StringUtils.contains(arrayItem, query));
  }

  static class MaterialIconResource extends ValueMapResource {

    public static final String ICON_TITLE = "icon-title";
    public static final String ICON_NAME = "icon-name";
    public static final String ICON_VARIANT = "icon-variant";

    public MaterialIconResource(
        ResourceResolver resourceResolver, String title, String name, String variant) {
      super(resourceResolver, name, MATERIAL_ICON_RESOURCE);
      getValueMap().put(ICON_TITLE, title);
      getValueMap().put(ICON_NAME, name);
      getValueMap().put(ICON_VARIANT, variant);
    }
  }

  InputStream getFileAsIOStream(final String fileName) {
    InputStream ioStream =
        MaterialIconDatasource.class.getClassLoader().getResourceAsStream(fileName);

    if (ioStream == null) {
      throw new IllegalArgumentException(fileName + " is not found");
    }
    return ioStream;
  }
}
