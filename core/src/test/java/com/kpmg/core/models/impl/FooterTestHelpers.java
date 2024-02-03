package com.kpmg.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;

public class FooterTestHelpers {

  public static final String HOME_PAGE = "/content/kpmgpublic/ca/fr";
  public static final String PAGE_PATH = HOME_PAGE + "/my-page";
  public static final String FOOTER_COMPONENT_PATH = PAGE_PATH + "/jcr:content/root/footer";

  public static <T> T setup(AemContext context, String childResource, Class<T> clazz) {
    createPage(context, HOME_PAGE);
    createPage(context, PAGE_PATH);
    context.currentPage(PAGE_PATH);

    context.load().json("/FooterImpl.json", FOOTER_COMPONENT_PATH);
    context.currentResource(FOOTER_COMPONENT_PATH + childResource);
    return context.request().adaptTo(clazz);
  }

  private static Page createPage(AemContext context, String path) {
    return context
        .create()
        .page(
            path, "", ImmutableMap.<String, Object>builder().put("jcr:title", "Test Page").build());
  }
}
