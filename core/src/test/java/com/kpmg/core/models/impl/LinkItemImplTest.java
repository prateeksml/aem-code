package com.kpmg.core.models.impl;

import static org.mockito.Mockito.*;

import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class LinkItemImplTest {

  AemContext context = AppAemContext.newAemContext();

  @BeforeEach
  void setUp() {
    context.load().json("/LinkItemImplTest.json", "/test");
    context.create().page("/content/link");
    context.currentResource("/test");
  }

  @Test
  void test() {
    LinkManager linkManager = context.request().adaptTo(LinkManager.class);
    LinkItemImpl actualLinkItemImpl =
        new LinkItemImpl("id", context.currentResource(), null, linkManager);
    Assertions.assertEquals("/content/link.html", actualLinkItemImpl.getLink().getURL());
    Assertions.assertEquals("Link Title", actualLinkItemImpl.getLinkTitle());
  }
}
