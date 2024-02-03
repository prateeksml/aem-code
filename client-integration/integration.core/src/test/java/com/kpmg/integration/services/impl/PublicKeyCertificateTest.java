package com.kpmg.integration.services.impl;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;

import com.adobe.granite.keystore.KeyStoreService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PublicKeyCertificateTest {
  private final AemContext context = new AemContext();
  private PublicKeyCertificate publicKeyCertificate;
  private ResourceResolver resourceResolver;
  @Mock private KeyStoreService keyStoreService;

  @BeforeEach
  void setUp() {
    context.registerService(KeyStoreService.class, keyStoreService);
    context.registerInjectActivateService(new PublicKeyCertificate());
    resourceResolver = context.resourceResolver();
    publicKeyCertificate = context.getService(PublicKeyCertificate.class);
  }

  @Test
  void testGetPublicKeyFromAlias() {
    assertNull(publicKeyCertificate.getPublicKeyFromAlias(resourceResolver, anyString()));
  }
}
