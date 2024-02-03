package com.kpmg.core.models.impl;

import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.kpmg.core.models.datalayer.impl.KPMGComponentDataImpl;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public abstract class AbstractKPMGComponentImpl extends AbstractComponentImpl {

  /**
   * Override of getData to provide an extension to the datalayer
   *
   * @return
   */
  public ComponentData getData() {
    if (ComponentUtils.isDataLayerEnabled(resource)) {
      ComponentData data = DataLayerBuilder.extending(super.getData()).asComponent().build();
      return new KPMGComponentDataImpl(data, getDatalayerExtension());
    } else {
      return null;
    }
  }

  // provided to be overridden by the extending class
  protected Object getDatalayerExtension() {
    return null;
  }
}
