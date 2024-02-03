package com.kpmg.integration.models;

import com.adobe.cq.wcm.core.components.models.form.Text;

public interface FormText extends Text {

  public String getAlphabets();

  public String getAlphaNumeric();

  public String getNumeric();

  public String getEmail();

  public String getPhone();

  public Boolean getRegexCheckbox();

  public String getCustomRegex();

  public String getRegexMessage();

  public String getRegex();

  public String getRegexMapping();

  public String getMaxCharLength();
}
