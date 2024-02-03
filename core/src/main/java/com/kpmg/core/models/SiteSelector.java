package com.kpmg.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.kpmg.core.models.impl.SiteSelectorBean;
import java.util.List;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface SiteSelector extends ComponentExporter {

  List<SiteSelectorBean> getSiteSelectorItems();

  String getCurrentCountry();

  String getCurrentLanguage();

  List<SiteSelectorBean> getRelevantCountryCode();

  Link getLink();
}
