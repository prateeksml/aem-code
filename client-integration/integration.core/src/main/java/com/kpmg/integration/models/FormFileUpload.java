package com.kpmg.integration.models;

public interface FormFileUpload {
  public String getFileLabel();

  public String getFileName();

  public String getDesktopTitle();

  public String getDesktopTitleLink();

  public String getMobileTitle();

  public String getMobileTitleLink();

  public String getFileMessage();

  public String getMaxFileSize();

  public String getMaxFiles();

  public String getAriaLabel();

  public String getFileRequired();

  public String getFileRequiredMessage();

  public String getFileHelpMessage();

  public String getFilePlaceholder();

  public String getHideLabel();

  public String getFileDropMessage();

  public String getMaxFilesMessage();

  public String getNoDuplicatesMessage();

  public String getSupportedFilesMessage();

  public String getFileSizeMessage();
}
