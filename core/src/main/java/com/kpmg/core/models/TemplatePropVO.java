package com.kpmg.core.models;

import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;

/**
 * @author ksha30
 */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class TemplatePropVO {

  private String templatePath;
  private String templateName;
  private String templateJcrTitle;

  /**
   * @return templatePath
   */
  public String getTemplatePath() {
    return templatePath;
  }

  /**
   * @param templatePath
   */
  public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
  }

  /**
   * @return templateName
   */
  public String getTemplateName() {
    return templateName;
  }

  /**
   * @param templateName
   */
  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  /**
   * @return templateJcrTitle
   */
  public String getTemplateJcrTitle() {
    return templateJcrTitle;
  }

  /**
   * @param templateJcrTitle
   */
  public void setTemplateJcrTitle(String templateJcrTitle) {
    this.templateJcrTitle = templateJcrTitle;
  }
}
