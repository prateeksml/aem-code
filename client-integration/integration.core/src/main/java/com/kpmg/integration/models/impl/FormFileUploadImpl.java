package com.kpmg.integration.models.impl;

import com.kpmg.integration.models.FormFileUpload;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = FormFileUpload.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FormFileUploadImpl implements FormFileUpload {
  @ValueMapValue(name = "filetitle")
  private String filetitle;

  @ValueMapValue(name = "filename")
  private String filename;

  @ValueMapValue(name = "desktoptitle")
  private String desktoptitle;

  @ValueMapValue(name = "desktoptitlelink")
  private String desktoptitlelink;

  @ValueMapValue(name = "mobiletitle")
  private String mobiletitle;

  @ValueMapValue(name = "mobiletitlelink")
  private String mobiletitlelink;

  @ValueMapValue(name = "filemessage")
  private String filemessage;

  @ValueMapValue(name = "maxfilesize")
  private String maxfilesize;

  @ValueMapValue(name = "maxfiles")
  private String maxfiles;

  @ValueMapValue(name = "arialabel")
  private String arialabel;

  @ValueMapValue(name = "filerequired")
  private String filerequired;

  @ValueMapValue(name = "filerequiredMessage")
  private String filerequiredMessage;

  @ValueMapValue(name = "filehelpMessage")
  private String filehelpMessage;

  @ValueMapValue(name = "fileusePlaceholder")
  private String fileusePlaceholder;

  @ValueMapValue(name = "filehideTitle")
  private String filehideTitle;

  @ValueMapValue(name = "fileDropMessage")
  private String fileDropMessage;

  @ValueMapValue(name = "maxFilesReached")
  private String maxFilesReached;

  @ValueMapValue(name = "noDuplicates")
  private String noDuplicates;

  @ValueMapValue(name = "supportedFileMessage")
  private String supportedFileMessage;

  @ValueMapValue(name = "fileSizeExceeds")
  private String fileSizeExceeds;

  @Override
  public String getFileLabel() {
    return filetitle;
  }

  @Override
  public String getFileName() {
    return filename;
  }

  @Override
  public String getDesktopTitle() {
    return desktoptitle;
  }

  @Override
  public String getDesktopTitleLink() {
    return desktoptitlelink;
  }

  @Override
  public String getMobileTitle() {
    return mobiletitle;
  }

  @Override
  public String getMobileTitleLink() {
    return mobiletitlelink;
  }

  @Override
  public String getFileMessage() {
    return filemessage;
  }

  @Override
  public String getMaxFileSize() {
    return maxfilesize;
  }

  @Override
  public String getMaxFiles() {
    return maxfiles;
  }

  @Override
  public String getAriaLabel() {
    return arialabel;
  }

  @Override
  public String getFileRequired() {
    return filerequired;
  }

  @Override
  public String getFileRequiredMessage() {
    return filerequiredMessage;
  }

  @Override
  public String getFileHelpMessage() {
    return filehelpMessage;
  }

  @Override
  public String getFilePlaceholder() {
    return fileusePlaceholder;
  }

  @Override
  public String getHideLabel() {
    return filehideTitle;
  }

  @Override
  public String getFileDropMessage() {
    return fileDropMessage;
  }

  @Override
  public String getMaxFilesMessage() {
    return maxFilesReached;
  }

  @Override
  public String getNoDuplicatesMessage() {
    return noDuplicates;
  }

  @Override
  public String getSupportedFilesMessage() {
    return supportedFileMessage;
  }

  @Override
  public String getFileSizeMessage() {
    return fileSizeExceeds;
  }
}
