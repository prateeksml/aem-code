package com.kpmg.integration.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({AemContextExtension.class})
class FormFileUploadImplTest {

  private final AemContext ctx = new AemContext();

  FormFileUploadImpl formFileUpload = new FormFileUploadImpl();

  @BeforeEach
  void setUp() {
    ctx.addModelsForClasses(FormFileUploadImpl.class);
    ctx.load().json("src/test/resources/model-resources/FormFileupload.json", "/content");
    ctx.currentResource("/content/file");
    formFileUpload = ctx.request().adaptTo(FormFileUploadImpl.class);
  }

  @Test
  void getFileLabel() {
    assertNotNull(formFileUpload.getFileLabel());
  }

  @Test
  void getFileName() {
    assertNotNull(formFileUpload.getFileName());
  }

  @Test
  void getDesktopTitle() {
    assertNotNull(formFileUpload.getDesktopTitle());
  }

  @Test
  void getDesktopTitleLink() {
    assertNotNull(formFileUpload.getDesktopTitleLink());
  }

  @Test
  void getMobileTitle() {
    assertNotNull(formFileUpload.getMobileTitle());
  }

  @Test
  void getMobileTitleLink() {
    assertNotNull(formFileUpload.getMobileTitleLink());
  }

  @Test
  void getFileMessage() {
    assertNotNull(formFileUpload.getFileMessage());
  }

  @Test
  void getMaxFileSize() {
    assertNotNull(formFileUpload.getMaxFileSize());
  }

  @Test
  void getMaxFiles() {
    assertNotNull(formFileUpload.getMaxFiles());
  }

  @Test
  void getAriaLabel() {
    assertNotNull(formFileUpload.getAriaLabel());
  }

  @Test
  void getFileRequired() {
    assertNotNull(formFileUpload.getFileRequired());
  }

  @Test
  void getFileRequiredMessage() {
    assertNotNull(formFileUpload.getFileRequiredMessage());
  }

  @Test
  void getFileHelpMessage() {
    assertNotNull(formFileUpload.getFileHelpMessage());
  }

  @Test
  void getFilePlaceholder() {
    assertNotNull(formFileUpload.getFilePlaceholder());
  }

  @Test
  void getHideLabel() {
    assertNotNull(formFileUpload.getHideLabel());
  }

  @Test
  void getFileDropMessage() {
    assertNotNull(formFileUpload.getFileDropMessage());
  }

  @Test
  void getMaxFilesMessage() {
    assertNotNull(formFileUpload.getMaxFilesMessage());
  }

  @Test
  void getNoDuplicatesMessage() {
    assertNotNull(formFileUpload.getNoDuplicatesMessage());
  }

  @Test
  void getSupportedFilesMessage() {
    assertNotNull(formFileUpload.getSupportedFilesMessage());
  }

  @Test
  void getFileSizeMessage() {
    assertNotNull(formFileUpload.getFileSizeMessage());
  }
}
