package com.kpmg.integration.services.impl;

import com.adobe.granite.keystore.KeyStoreService;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    name = "PublicKey Certificate Service",
    service = PublicKeyCertificate.class,
    immediate = true)
public class PublicKeyCertificate {

  private static final Logger LOG = LoggerFactory.getLogger(PublicKeyCertificate.class);
  @Reference private KeyStoreService keyStoreService;

  public Certificate getPublicKeyFromAlias(ResourceResolver resourceResolver, String certAlias) {
    KeyStore trustStore = this.keyStoreService.getTrustStore(resourceResolver);
    X509Certificate crt = null;
    try {
      if (trustStore != null) {
        crt = (X509Certificate) trustStore.getCertificate(certAlias);
      }
    } catch (KeyStoreException ex) {
      LOG.error("Exception in getting the certificate:{}", ExceptionUtils.getStackTrace(ex));
    }
    return crt;
  }
}
