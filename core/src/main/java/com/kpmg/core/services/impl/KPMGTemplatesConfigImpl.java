package com.kpmg.core.services.impl;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.KPMGTemplates;
import com.kpmg.core.utils.NodeUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class KPMGTemplatesConfigImpl. */
@Component(service = KPMGTemplates.class, immediate = true)
@Designate(ocd = KPMGTemplatesConfigImpl.KPMGTemplatesConfig.class)
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class KPMGTemplatesConfigImpl implements KPMGTemplates {

  private static final Logger LOG = LoggerFactory.getLogger(KPMGTemplatesConfigImpl.class);

  private String[] allowedTemplates;
  private String[] hideInNavTemplates;
  private String[] hideInSearchTemplates;

  @ObjectClassDefinition(
      name = "KPMG: Templates Configuration",
      description = "This service is used to retrieve KPMG templates.")
  public @interface KPMGTemplatesConfig {

    @AttributeDefinition(
        name = "KPMG Allowed Templates Config",
        description = "This is used to set the allowed templates.")
    public String[] allowedTemplates();

    @AttributeDefinition(
        name = "KPMG Allowed Config For Hide In Nav Pages",
        description = "This is used to set the hide in nav property")
    public String[] hideInNavTemplates();

    @AttributeDefinition(
        name = "KPMG Allowed Config For Hide In Search Pages",
        description = "This is used to set the hide in search property.")
    public String[] hideInSearchTemplates();
  }

  /**
   * Activate.
   *
   * @param config the config
   */
  @Activate
  @Modified
  protected void activate(KPMGTemplatesConfig config) {
    LOG.info("Inside activate method of KPMGTemplatesConfig Service");
    this.allowedTemplates = config.allowedTemplates();
    this.hideInNavTemplates = config.hideInNavTemplates();
    this.hideInSearchTemplates = config.hideInSearchTemplates();
    LOG.info("KPMGTemplatesConfig Configuration saved successfully.");
  }

  /**
   * Gets the allowed templates.
   *
   * @param grandParentTempaltePath the grand parent tempalte path
   * @param parentTemplate the parent template
   * @param childTemplate the child template
   * @return the allowed templates
   */
  public List<String> getTemplates(final Page currentPage) {
    List<String> allowedTemplatesList = null;
    final String TEMPLATE_SEPARATOR = "&";
    if (currentPage != null) {
      final String currentPageTemplate = NodeUtils.getTemplatePath(currentPage);
      final Page currentPageParent = currentPage.getParent();
      final String currentPageParentTemplate = NodeUtils.getTemplatePath(currentPageParent);
      final Page currentPageGrandParent =
          currentPageParent != null ? currentPageParent.getParent() : null;
      final String currentPageGrandParentTemplate =
          NodeUtils.getTemplatePath(currentPageGrandParent);
      final String key1 =
          currentPageGrandParentTemplate
              + TEMPLATE_SEPARATOR
              + currentPageParentTemplate
              + TEMPLATE_SEPARATOR
              + currentPageTemplate;
      final String key2 = currentPageParentTemplate + TEMPLATE_SEPARATOR + currentPageTemplate;
      final Map<String, List<String>> templatesMapping = getTemplatesList();
      if (templatesMapping.containsKey(key1)) {
        allowedTemplatesList = templatesMapping.get(key1);
      } else if (templatesMapping.containsKey(key2)) {
        allowedTemplatesList = templatesMapping.get(key2);
      } else {
        LOG.debug("Allowed templates not applicable for page @ {}", currentPage.getPath());
      }
    }
    return allowedTemplatesList;
  }

  /**
   * Gets the templates list.
   *
   * @return the templates list
   */
  private Map<String, List<String>> getTemplatesList() {
    final Map<String, List<String>> templatesMapping = new HashMap<>();
    final String TEMPLATE_SEPARATOR = "&";
    final String ALLOWED_TEMPLATE_SEPARATOR = ",";
    final String VALUE_SEPARATOR = ":";
    if (allowedTemplates != null) {
      for (String entry : allowedTemplates) {
        if (StringUtils.contains(entry, VALUE_SEPARATOR)
            && StringUtils.contains(entry, TEMPLATE_SEPARATOR)) {
          final String[] templates = entry.split(VALUE_SEPARATOR);
          templatesMapping.put(
              templates[0], Arrays.asList(templates[1].split(ALLOWED_TEMPLATE_SEPARATOR)));
        }
      }
    }
    return templatesMapping;
  }

  @Override
  public List<String> getAllowedTemplates(final Page currentPage) {
    return getTemplates(currentPage);
  }

  /**
   * Gets the hide in search templates.
   *
   * @return the hide in search templates
   */
  @Override
  public List<String> getHideInSearchTemplates() {
    List.of(hideInSearchTemplates);
    return List.of(hideInSearchTemplates);
  }

  /**
   * Gets the hide in nav templates.
   *
   * @return the hide in nav templates
   */
  @Override
  public List<String> getHideInNavTemplates() {
    return List.of(hideInNavTemplates);
  }
}
