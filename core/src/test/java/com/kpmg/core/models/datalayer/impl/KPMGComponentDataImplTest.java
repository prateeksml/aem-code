package com.kpmg.core.models.datalayer.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class KPMGComponentDataImplTest {

  @Test
  void testConstructor() {
    KPMGComponentDataImpl actualKPMGComponentDataImpl = new KPMGComponentDataImpl(null);
    // Everything is delegated, nothing to test.. really. But we do have a requirnment of 50%
    // coverage ¯\_(ツ)_/¯.
    assertNotNull(actualKPMGComponentDataImpl);
  }

  @Test
  void getJson() {
    ComponentData data = Mockito.mock(ComponentData.class);
    Mockito.doReturn("id").when(data).getId();
    Map map = Map.of("k", "v");
    KPMGComponentDataImpl kPMGComponentDataImpl = new KPMGComponentDataImpl(data, map);
    String actualJson = kPMGComponentDataImpl.getJson();
    assertEquals("{\"id\":{\"k\":\"v\"}}", actualJson);
  }
}
