package com.kpmg.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface Footer extends ComponentExporter {
  String getCopyrightText();
}
