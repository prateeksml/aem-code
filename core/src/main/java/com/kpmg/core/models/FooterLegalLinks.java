package com.kpmg.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import java.util.List;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface FooterLegalLinks extends ComponentExporter {
  List<LinkItem> getLinkItems();
}
