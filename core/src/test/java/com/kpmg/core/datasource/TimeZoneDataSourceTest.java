package com.kpmg.core.datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adobe.acs.commons.wcm.datasources.DataSourceBuilder;
import com.adobe.acs.commons.wcm.datasources.impl.DataSourceBuilderImpl;
import com.adobe.granite.ui.components.ExpressionResolver;
import com.adobe.granite.ui.components.ds.DataSource;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Iterator;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
public class TimeZoneDataSourceTest {
  private final AemContext context = AppAemContext.newAemContext();

  private TimeZoneDataSource timeZoneDataSource;

  @Mock ExpressionResolver expressionResolver;

  @BeforeEach
  void setUp() {
    timeZoneDataSource =
        new TimeZoneDataSource() {
          @Override
          protected ExpressionResolver getExpressionResolver() {
            return expressionResolver;
          }
        };
    timeZoneDataSource = Mockito.spy(timeZoneDataSource);
  }

  @Test
  void testTimeZoneDataSource() {
    timeZoneDataSource.dataSourceBuilder = new DataSourceBuilderImpl();
    timeZoneDataSource.doGet(context.request(), context.response());
    DataSource requestDs = (DataSource) context.request().getAttribute(DataSource.class.getName());
    Iterator<Resource> iterator = requestDs.iterator();
    assertEquals(
        "(UTC-11:00)Pacific/Midway Samoa Standard Time",
        iterator.next().getValueMap().get(DataSourceBuilder.TEXT, String.class));
    assertEquals("NUT", iterator.next().getValueMap().get(DataSourceBuilder.VALUE, String.class));
  }
}
