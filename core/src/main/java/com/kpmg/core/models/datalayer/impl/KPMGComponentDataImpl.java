package com.kpmg.core.models.datalayer.impl;

import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerSupplier;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/** {@link DataLayerSupplier} backed component data implementation. */
@Slf4j
public class KPMGComponentDataImpl implements ComponentData {

  private ObjectMapper objectMapper = new ObjectMapper(); // Thread safe

  private interface Exclusions {
    String getJson();
  }

  @Delegate(excludes = Exclusions.class)
  private ComponentData componentData;

  private Object datalayerExtension;

  protected ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  /**
   * Construct the data layer model.
   *
   * @param componentData The component data.
   */
  public KPMGComponentDataImpl(ComponentData componentData) {
    this.componentData = componentData;
  }

  /**
   * Construct the data layer model.
   *
   * @param componentData The component data.
   */
  public <T> KPMGComponentDataImpl(ComponentData componentData, Object datalayerExtension) {
    this.componentData = componentData;
    this.datalayerExtension = datalayerExtension;
  }

  /** Overrides the default implementation to provide custom data layer properties for KPMG Page. */
  public String getJson() {
    if (componentData == null) return StringUtils.EMPTY;
    try {
      ObjectNode componentDatalayerObjectNode = objectMapper.valueToTree(this);
      if (datalayerExtension != null) {
        // merge the datalayerExtension object with the existing datalayer
        componentDatalayerObjectNode =
            objectMapper
                .readerForUpdating(componentDatalayerObjectNode)
                .readValue((JsonNode) objectMapper.valueToTree(datalayerExtension));
      }

      return String.format(
          "{\"%s\":%s}",
          componentData.getId(), objectMapper.writeValueAsString(componentDatalayerObjectNode));
    } catch (IOException e) {
      log.error("Unable to generate dataLayer JSON string", e);
      return null;
    }
  }
}
