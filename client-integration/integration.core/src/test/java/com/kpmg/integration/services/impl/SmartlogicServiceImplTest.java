package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.models.Tag;
import com.kpmg.integration.services.SmartLogic;
import com.smartlogic.ses.client.DisplayName;
import com.smartlogic.ses.client.Id;
import com.smartlogic.ses.client.SESClient;
import com.smartlogic.ses.client.Term;
import com.smartlogic.ses.client.exceptions.SESException;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
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
class SmartlogicServiceImplTest {
  @Mock SmartLogic smartlogic;
  @Mock SESClient sesClient;
  @Mock ResourceResolverFactory resolverFactory;
  @Mock ResourceResolver resolver;
  @Mock Resource currentResource;
  @Mock SiteSettingsConfig sitesettings;
  private final AemContext ctx =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK).build();
  @InjectMocks SmartlogicServiceImpl smartlogicService = new SmartlogicServiceImpl();

  @BeforeEach
  public void setUp() {
    ctx.load()
        .json(
            "src/test/resources/model-resources/smartlogic.json",
            "/content/kpmg/language-masters/en");
    ctx.currentPage("/content/kpmg/language-masters/en");
  }

  @Test
  void getModelNameByPagePathTest() {
    assertEquals(
        "KPMGUS", smartlogicService.getModelNameByPagePath("/content/kpmg/us/en/test-page"));
  }

  @Test
  void getUpdatedTags() throws LoginException {
    when(smartlogic.getSesClientHostIP()).thenReturn("dev.kpmg.com");
    when(smartlogic.getSesClientPath()).thenReturn("/ses");
    when(smartlogic.getSesPort()).thenReturn("443");
    when(smartlogic.getSesClientProtocol()).thenReturn("https");
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.getResource(anyString())).thenReturn(currentResource);
    Map<String, Object> param = new HashMap<>();
    param.put("pagePath", "/content/kpmg/language-masters/en");
    ctx.request().setParameterMap(param);
    assertNotNull(smartlogicService.getUpdatedTags(ctx.request()));
  }

  @Test
  void createSESClient() {
    when(smartlogic.getSesClientHostIP()).thenReturn("dev.kpmg.com");
    when(smartlogic.getSesClientPath()).thenReturn("/ses");
    when(smartlogic.getSesPort()).thenReturn("443");
    when(smartlogic.getSesClientProtocol()).thenReturn("https");
    assertNotNull(smartlogicService.createSESClient("KPMGUS"));
  }

  @Test
  void getAllTagsByCategoryEmptySESClientTest() {
    assertEquals(
        0,
        smartlogicService.getAllTagsByCategory("/content/kpmg/us/en/test-page", "insight").size());
  }

  @Test
  void getSESLanguage() throws SESException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.getResource(anyString())).thenReturn(currentResource);
    assertEquals("en-us", smartlogicService.getSESLanguage("/content/kpmg/us/en/test-page"));
  }

  @Test
  void populateTagWithDetails() {
    Term term = new Term();
    term.setId(new Id());
    DisplayName displayname = new DisplayName();
    displayname.setValue("Audit");
    term.setDisplayName(displayname);
    Tag tag = new Tag();
    assertEquals(
        tag, smartlogicService.populateTagWithDetails("insight", term, "12423565332345778898765"));
  }

  @Test
  void populateTagWithDetailsGeography() {
    Term term = new Term();
    term.setId(new Id());
    Tag expected = new Tag();
    Tag actual =
        smartlogicService.populateTagWithDetails("geography", term, "12423565332345778898765");
    assertEquals(expected, actual);
  }

  @Test
  void getAllTagsDetailsFromSESByCategory() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("insight", "164000807485345111581424");
    JsonArray expected = new JsonArray();
    JsonArray actual = smartlogicService.getAllTagsDetailsFromSESByCategory(map, sesClient);
    assertEquals(expected, actual);
  }
}
