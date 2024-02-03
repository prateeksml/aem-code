package com.kpmg.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.VariationDef;
import java.util.Iterator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CFVariationReportCellCSVExporterTest {

  private CFVariationReportCellCSVExporter exporter;

  @BeforeEach
  public void setup() {
    exporter = new CFVariationReportCellCSVExporter();
  }

  @Test
  void testGetValue() {
    Resource mockResource = mock(Resource.class);
    Resource cfmockResource = mock(Resource.class);
    ResourceResolver resourceResolver = mock(ResourceResolver.class);
    ContentFragment contentFragment = mock(ContentFragment.class);
    Iterator itr = mock(Iterator.class);
    VariationDef def = mock(VariationDef.class);

    String expectedValue = "TestValue";
    CFReportCellValue mockCFReportCellValue = mock(CFReportCellValue.class);
    when(mockCFReportCellValue.getValue()).thenReturn(expectedValue);
    when(mockResource.getPath())
        .thenReturn("/content/dam/kpmgsites/xx/content-fragments/lukas-marty/jcr:content/data/fr");
    when(mockResource.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(Mockito.anyString())).thenReturn(cfmockResource);
    when(cfmockResource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);
    when(contentFragment.listAllVariations()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.next()).thenReturn(def);
    when(def.getName()).thenReturn("fr");
    when(def.getTitle()).thenReturn("fr");

    String result = exporter.getValue(mockResource);
    assertEquals("fr", result);
  }

  @Test
  void testGetValueForMaster() {
    Resource mockResource = mock(Resource.class);
    Resource cfmockResource = mock(Resource.class);
    ResourceResolver resourceResolver = mock(ResourceResolver.class);
    ContentFragment contentFragment = mock(ContentFragment.class);
    Iterator itr = mock(Iterator.class);
    VariationDef def = mock(VariationDef.class);

    String expectedValue = "TestValue";
    CFReportCellValue mockCFReportCellValue = mock(CFReportCellValue.class);
    when(mockCFReportCellValue.getValue()).thenReturn(expectedValue);
    when(mockResource.getPath())
        .thenReturn(
            "/content/dam/kpmgsites/xx/content-fragments/lukas-marty/jcr:content/data/master");
    when(mockResource.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(Mockito.anyString())).thenReturn(cfmockResource);
    when(cfmockResource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);
    when(contentFragment.listAllVariations()).thenReturn(itr);
    when(itr.hasNext()).thenReturn(true, false);
    when(itr.next()).thenReturn(def);
    when(def.getName()).thenReturn("fr");
    when(def.getTitle()).thenReturn("fr");

    String result = exporter.getValue(mockResource);
    assertEquals("master", result);
  }
}
