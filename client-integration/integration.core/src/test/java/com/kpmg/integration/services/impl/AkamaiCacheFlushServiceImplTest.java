package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.kpmg.integration.services.CacheFlush;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
public class AkamaiCacheFlushServiceImplTest {
  @Mock CacheFlush cacheFlushConfig;
  @InjectMocks AkamaiCacheFlushServiceImpl service = new AkamaiCacheFlushServiceImpl();
  private final AemContext ctx =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK).build();
  Map<String, Object> properties = new HashMap<>();

  @BeforeEach
  public void setUp() {
    ctx.load()
        .json(
            "src/test/resources/model-resources/samplepage.json",
            "/content/kpmgpublic/ch/en/home/contact-page");
    ctx.currentPage("/content/kpmgpublic/ch/en/home/contact-page");
  }

  @Test
  void akamaiFastPurgeClientTest() {
    properties.put("getAkamaiAccessToken", "accesstoken");
    properties.put("getDefaultAkamaiDomain", "domain");
    properties.put("getAkamaiClientToken", "clienttoken");
    properties.put("getAkamaiHostToken", "hosttoken");
    properties.put("getAkamaiClientSecret", "secret");
    properties.put("getHtmlPurgeURLPrefix", "htmlprefix");
    properties.put("getAssetPurgeURLPrefix", "assetprefix");
    ctx.registerInjectActivateService(CacheFlushImpl.class, properties);
    service = ctx.registerInjectActivateService(new AkamaiCacheFlushServiceImpl());
    List<String> paths = new ArrayList<>();
    paths.add("/content/kpmgpublic/ch/en/home/contact-page");
    assertNull(service.akamaiFastPurgeClient(paths));
    ;
  }

  @Test
  void akamaiFastPurgeClientInvalidTest() {
    properties = new HashMap<String, Object>();
    ctx.registerInjectActivateService(CacheFlushImpl.class, properties);
    service = ctx.registerInjectActivateService(new AkamaiCacheFlushServiceImpl());
    List<String> paths = new ArrayList<>();
    paths.add("/content/kpmgpublic/us/en/test-page");
    assertNull(service.akamaiFastPurgeClient(paths));
  }
}
