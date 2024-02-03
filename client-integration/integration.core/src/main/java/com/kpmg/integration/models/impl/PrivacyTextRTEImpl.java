package com.kpmg.integration.models.impl;

import com.kpmg.integration.models.PrivacyTextRTE;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = PrivacyTextRTE.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PrivacyTextRTEImpl implements PrivacyTextRTE {

  @Getter @ValueMapValue private String privacytext;
  @Getter @ValueMapValue private String privacyid;

  @Override
  public String getText() {
    return privacytext;
  }

  @Override
  public String getId() {
    return privacyid;
  }
}
