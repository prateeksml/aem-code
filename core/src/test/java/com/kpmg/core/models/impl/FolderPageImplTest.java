package com.kpmg.core.models.impl;

import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FolderPageImplTest {
  private final AemContext context = AppAemContext.newAemContext();
  private FolderPageImpl testClass;

  @BeforeEach
  void setUp() {
    context.addModelsForClasses(FolderPageImpl.class);
    context.load().json("/TextListImpl.json", "/content/folderpage");
    context.currentResource("/content/folderpage");
    testClass = context.request().adaptTo(FolderPageImpl.class);
  }

  @Test
  void testPostConstruct() throws IOException {
    testClass.postConstruct();
  }
}
