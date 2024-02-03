package com.kpmg.integration.models;

import com.adobe.cq.wcm.core.components.models.form.Options;

public interface FormOptions extends Options {

  public boolean getRequired();

  public String getRequiredMessage();

  public String getFragmentPath();
}
