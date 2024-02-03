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

class CFLinkReportCellCSVExporterTest {
  private CFLinkReportCellCSVExporter exporter;

  @BeforeEach
  public void setup() {
    exporter = new CFLinkReportCellCSVExporter();
  }

  @Test
  void testGetValue() {
    final Resource mockResource = mock(Resource.class);
    final Resource cfmockResource = mock(Resource.class);
    final ResourceResolver resourceResolver = mock(ResourceResolver.class);
    final ContentFragment contentFragment = mock(ContentFragment.class);
    final Iterator<VariationDef> itr = mock(Iterator.class);
    final VariationDef def = mock(VariationDef.class);

    final String expectedValue = "TestValue";
    final CFReportCellValue mockCFReportCellValue = mock(CFReportCellValue.class);
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

    final String result = exporter.getValue(mockResource);
    assertEquals("/content/dam/kpmgsites/xx/content-fragments/lukas-marty?variation=fr", result);
  }

  @Test
  void testGetValueForMaster() {
    final Resource mockResource = mock(Resource.class);
    final Resource cfmockResource = mock(Resource.class);
    final ResourceResolver resourceResolver = mock(ResourceResolver.class);
    final ContentFragment contentFragment = mock(ContentFragment.class);
    final Iterator<VariationDef> itr = mock(Iterator.class);
    final VariationDef def = mock(VariationDef.class);

    final String expectedValue = "TestValue";
    final CFReportCellValue mockCFReportCellValue = mock(CFReportCellValue.class);
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

    final String result = exporter.getValue(mockResource);
    assertEquals("/content/dam/kpmgsites/xx/content-fragments/lukas-marty", result);
  }
}
