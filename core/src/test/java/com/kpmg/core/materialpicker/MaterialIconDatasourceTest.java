package com.kpmg.core.materialpicker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.adobe.granite.ui.components.ds.DataSource;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaterialIconDatasourceTest {

  private static final int OFFSET = 10;

  @Mock private ResourceResolver resolver;

  @Mock private SlingHttpServletRequest request;

  @Mock private RequestPathInfo pathInfo;

  @Mock private SlingHttpServletResponse response;

  @Mock private DataSource dataSource;

  MaterialIconDatasource materialIconDatasource;

  @BeforeEach
  void setUp() {
    materialIconDatasource = new MaterialIconDatasource();
  }

  @Test
  void testDoGet() throws IOException {
    String query = "query";
    String variant = "filled";
    when(request.getParameter(MaterialIconDatasource.P_QUERY)).thenReturn(query);
    when(request.getParameter(MaterialIconDatasource.P_ICON_VARIANT)).thenReturn(variant);

    when(request.getResourceResolver()).thenReturn(resolver);
    when(request.getRequestPathInfo()).thenReturn(pathInfo);
    when(pathInfo.getSelectors())
        .thenReturn(new String[] {"test", "100", Integer.toString(OFFSET)});
    materialIconDatasource = spy(materialIconDatasource);
    materialIconDatasource.doGet(request, response);

    verify(materialIconDatasource).searchIcon(query, resolver, OFFSET, variant);
  }

  @Test
  void testSearchIconWithoutQuery() throws IOException {
    DataSource result = materialIconDatasource.searchIcon(null, resolver, OFFSET, "filled");
    // search does return a result, realistically, all results are expected,
    // but ensuring at least one is returned is sufficient
    assertTrue(StringUtils.isNoneBlank(result.iterator().next().getName()));
  }

  @Test
  void testSearchIconWithExistingQuery() throws IOException {
    // it must at least have chevrons, right?
    DataSource result = materialIconDatasource.searchIcon("chevron", resolver, OFFSET, "filled");
    // search does return more than one result for this
    // but ensuring at least one is returned is sufficient
    assertTrue(StringUtils.isNoneBlank(result.iterator().next().getName()));
  }

  @Test
  void testSearchIconWithNonExistingQuery() throws IOException {
    // it must at least have chevrons, right?
    DataSource result =
        materialIconDatasource.searchIcon("this does not exist", resolver, OFFSET, "filled");
    // search does return more than one result for this
    // but ensuring at least one is returned is sufficient
    assertFalse(result.iterator().hasNext());
  }

  @Test
  void testFilterIconWithName() {
    Icon icon = new Icon();
    icon.name = "icon1";
    boolean result = materialIconDatasource.filterIcon("icon1", icon);
    assertEquals(true, result);
  }

  @Test
  void testFilterIconWithTag() {
    Icon icon = new Icon();
    icon.tags = new String[] {"tag1", "tag2"};
    boolean result = materialIconDatasource.filterIcon("tag1", icon);
    assertEquals(true, result);
  }

  @Test
  void testFilterIconWithCategory() {
    Icon icon = new Icon();
    icon.categories = new String[] {"category1", "category2"};
    boolean result = materialIconDatasource.filterIcon("category2", icon);
    assertEquals(true, result);
  }
}
