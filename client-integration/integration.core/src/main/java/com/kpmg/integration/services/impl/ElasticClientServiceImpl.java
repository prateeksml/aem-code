package com.kpmg.integration.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.kpmg.integration.config.ElasticSearchConfiguration;
import com.kpmg.integration.services.ElasticClientService;
import com.kpmg.integration.util.KPMGUtilities;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import lombok.Getter;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.elasticsearch.client.RestClient;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = ElasticClientService.class,
    immediate = true,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ElasticSearchConfiguration.class)
public class ElasticClientServiceImpl implements ElasticClientService {

  private static final Logger log = LoggerFactory.getLogger(ElasticClientServiceImpl.class);

  @Getter private String esHost;

  @Getter private int esPort;

  @Getter private String esApiKey;

  @Getter private String esCertsAlias;

  @Reference ResourceResolverFactory resourceResolverFactory;

  @Reference PublicKeyCertificate publicKeyCertificate;

  @Activate
  protected void activate(ElasticSearchConfiguration configuration) {
    this.esHost = configuration.getElasticSearchHost();
    this.esPort = configuration.getElasticSearchPort();
    this.esApiKey = configuration.getEsApiKey();
    this.esCertsAlias = configuration.getEsCertsAlias();
  }

  @Override
  public ElasticsearchAsyncClient getESAsyncClient() {
    ElasticsearchAsyncClient asyncClient = null;
    try {
      asyncClient = new ElasticsearchAsyncClient(getElasticsearchTransport());
    } catch (NoSuchAlgorithmException
        | KeyStoreException
        | KeyManagementException
        | LoginException
        | IOException
        | CertificateException e) {
      log.error("An Exception Occurred at getESAsyncClient {}", e.getMessage());
    }
    return asyncClient;
  }

  private ElasticsearchTransport getElasticsearchTransport()
      throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, LoginException,
          IOException, CertificateException {
    log.debug("Elastic Search Host {} And Port {}", esHost, esPort);
    Certificate cert =
        publicKeyCertificate.getPublicKeyFromAlias(
            KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory), esCertsAlias);

    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null, null);
    trustStore.setCertificateEntry("ca", cert);
    SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, null).build();

    Header[] defaultHeaders = new Header[] {new BasicHeader("Authorization", "ApiKey " + esApiKey)};
    RestClient restClient =
        RestClient.builder(new HttpHost(esHost, esPort, "https"))
            .setDefaultHeaders(defaultHeaders)
            .setHttpClientConfigCallback(hc -> hc.setSSLContext(sslContext))
            .setHttpClientConfigCallback(sp -> sp.useSystemProperties())
            .build();

    return new RestClientTransport(restClient, new JacksonJsonpMapper());
  }

  public RestClient createRestClient() {
    try {
      Certificate cert =
          publicKeyCertificate.getPublicKeyFromAlias(
              KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory), esCertsAlias);

      KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
      trustStore.load(null, null);
      trustStore.setCertificateEntry("ca", cert);
      SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, null).build();

      Header[] defaultHeaders =
          new Header[] {new BasicHeader("Authorization", "ApiKey " + esApiKey)};
      RestClient restClient =
          RestClient.builder(new HttpHost(esHost, esPort, "https"))
              .setDefaultHeaders(defaultHeaders)
              .setHttpClientConfigCallback(hc -> hc.setSSLContext(sslContext))
              .setHttpClientConfigCallback(sp -> sp.useSystemProperties())
              .build();

      return restClient;
    } catch (NoSuchAlgorithmException
        | KeyStoreException
        | KeyManagementException
        | IOException
        | CertificateException e) {
      log.error("Error creating RestClient", e);
      return null;
    } catch (LoginException e) {
      throw new RuntimeException(e);
    }
  }
}
