package com.kpmg.core.config;

import com.day.cq.wcm.api.Page;
import java.util.List;

/** The Interface KPMGTemplates. */
public interface KPMGTemplates {

  List<String> getAllowedTemplates(final Page currentPage);

  List<String> getHideInSearchTemplates();

  List<String> getHideInNavTemplates();
}
