package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import com.adobe.cq.dam.cfm.ContentFragment;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Arrays;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
class EmailMappingServiceImplTest {

  private final AemContext ctx =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK).build();

  @Mock ResourceResolverFactory resourceResolverFactory;

  @Mock Resource resource;

  @InjectMocks private EmailMappingServiceImpl emailMappingService;

  @BeforeEach
  void setUp() throws LoginException {
    ctx.load().json("src/test/resources/model-resources/enquiry.json", "/content/dam/kpmgsites/cf");
    ResourceResolver resolver = ctx.resourceResolver();
    ContentFragment cf =
        ctx.currentResource("/content/dam/kpmgsites/cf").adaptTo(ContentFragment.class);
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
  }

  @Test
  void testGetEmailBasedOnEnquiryType() {
    String[] expected = {"k@gmail.com", "hello@gmail.com"};
    assertEquals(
        Arrays.toString(expected),
        Arrays.toString(
            emailMappingService.getEmailBasedOnEnquiryType("name1", "/content/dam/kpmgsites")));
  }
}
