package com.kpmg.integration.models;

import com.adobe.cq.wcm.core.components.models.Image;
import com.kpmg.integration.models.impl.FooterLink;
import java.util.List;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface EmailContent extends Image {

  String getEmailSubject();

  String getEmailContent();

  String getFooterText();

  List<FooterLink> getFooterLink();

  int getLinklimit();
}
