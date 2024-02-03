package com.kpmg.integration.services.impl;

import com.akamai.edgegrid.signer.ClientCredential;
import com.akamai.edgegrid.signer.exceptions.RequestSigningException;
import com.akamai.edgegrid.signer.googlehttpclient.GoogleHttpClientEdgeGridRequestSigner;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.constants.NameConstants;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kpmg.integration.services.AkamaiCacheFlushService;
import com.kpmg.integration.services.CacheFlush;
import com.kpmg.integration.util.KPMGUtilities;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = AkamaiCacheFlushService.class)
public class AkamaiCacheFlushServiceImpl implements AkamaiCacheFlushService {
  private static final Logger LOG = LoggerFactory.getLogger(AkamaiCacheFlushServiceImpl.class);
  private static final String BASE_URL = "https://api.ccu.akamai.com";
  private static final String DEFAULT_REQUEST_QUEUE_RESOURCE = "/ccu/v3/invalidate/url";
  private static final String CONTENT_ROOT = "/content";
  private static final String KPMG_SITES_ROOT = "/content/kpmgpublic";
  private static final String KPMG_EXPERIENCE_FRAGMENT_ROOT = "/content/experience-fragments";

  private static final String PDF_MIME_TYPE = "application/pdf";
  @Reference private CacheFlush cacheFlushConfig;
  @Reference private ResourceResolverFactory resolverfactory;

  @Override
  public HttpResponse akamaiFastPurgeClient(List<String> path) {
    LOG.info("Started Akamai Fast Purge Client Impl");
    HttpResponse response = null;
    if (validateConfig()) {
      String flushUrlJson;
      try {
        flushUrlJson = getFlushJson(path);
        if (!flushUrlJson.isBlank()) {
          LOG.debug("Flush URL Json : {}", flushUrlJson);
          ClientCredential credential =
              ClientCredential.builder()
                  .accessToken(cacheFlushConfig.getAkamaiAccessToken())
                  .clientToken(cacheFlushConfig.getAkamaiClientToken())
                  .clientSecret(cacheFlushConfig.getAkamaiClientSecret())
                  .host(cacheFlushConfig.getAkamaiHostToken())
                  .build();
          HttpTransport httpTransport = new ApacheHttpTransport();
          HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
          URI uri =
              URI.create(
                  BASE_URL
                      + DEFAULT_REQUEST_QUEUE_RESOURCE
                      + "/"
                      + cacheFlushConfig.getDefaultAkamaiDomain().toLowerCase());
          LOG.info("Started FP Client Impl : URI {}", uri);
          HttpRequest request =
              requestFactory.buildPostRequest(
                  new GenericUrl(uri),
                  ByteArrayContent.fromString("application/json", flushUrlJson));
          GoogleHttpClientEdgeGridRequestSigner requestSigner =
              new GoogleHttpClientEdgeGridRequestSigner(credential);
          LOG.debug(
              "request: {} headers : {} url : {} content : {}",
              request,
              request.getHeaders(),
              request.getUrl(),
              request.getContent());
          requestSigner.sign(request);
          response = request.execute();
          LOG.debug("akamai response : {}", processAkamaiResponse(response));
        }
      } catch (IOException | RequestSigningException e) {
        LOG.error("Error occured in akamaiFastPurgeClient method {}", e.getMessage());
      } catch (RuntimeException e) {
        LOG.error("RuntimeException occured in akamaiFastPurgeClient method {}", e.getMessage());
      }
    }
    return response;
  }

  private String processAkamaiResponse(HttpResponse response) throws IOException {

    String processedResponse = null;

    try (Scanner scanner = new Scanner(response.getContent(), StandardCharsets.UTF_8.name())) {
      processedResponse = scanner.useDelimiter("\\A").next();
    }
    return processedResponse;
  }

  private String getFlushJson(List<String> paths) {
    Set<String> flushUrlJson = new HashSet<>();
    JsonObject json = new JsonObject();
    if (!paths.isEmpty()) {
      try {
        ResourceResolver resolver = KPMGUtilities.getResourceResolverFromPool(resolverfactory);
        if (null != resolver) {
          for (String path : paths) {
            flushUrlJson.addAll(getUrlsForPath(path, resolver));
          }
          Set<String> flushUrls = getFullUrl(flushUrlJson, resolver);
          if (!flushUrls.isEmpty()) {
            JsonElement array = new Gson().toJsonTree(flushUrls);
            json.add("objects", array);
            return json.toString();
          }
        }
      } catch (LoginException e) {
        LOG.error("Login exception : {}", e.getMessage(), e);
      }
    }
    return StringUtils.EMPTY;
  }

  private Set<String> getUrlsForPath(String path, ResourceResolver resolver) {
    LOG.debug("Entering getUrlsforPath: {}", path);
    Set<String> urls = new HashSet<>();
    if (path.contains("/i18n")) {
      LOG.debug("ignoring i18n label path {}", path);
      return Collections.emptySet();
    }
    if (isNodeType(path, DamConstants.NT_DAM_ASSET, resolver)) {
      path = path.replace("%20", " ");
      Asset asset = resolver.getResource(path).adaptTo(Asset.class);
      if (null != asset && PDF_MIME_TYPE.equals(asset.getMimeType())) {
        LOG.debug("resource is a pdf.");
        urls.add(asset.getPath().replace(" ", "%20"));
        return urls;
      } // ignoring image types as we use dynamic media
    }
    if (path.startsWith(CONTENT_ROOT)) {
      if (path.startsWith(KPMG_SITES_ROOT)) {
        LOG.debug("resource is a html page");
        urls.add(path);
        urls.add(path.concat(".html?nocache=true"));
      } else if (path.startsWith(KPMG_EXPERIENCE_FRAGMENT_ROOT)) {
        LOG.debug("resource is an expfrag");
        urls.add(path);
      }
      urls.addAll(getAliasUrls(path, resolver));
    } else {
      LOG.debug("....ignoring for.... {}", path);
    }

    return urls;
  }

  private Set<String> getAliasUrls(String path, ResourceResolver resolver) {
    PageManager pageManager = resolver.adaptTo(PageManager.class);
    Set<String> aliasFlushUrls = new HashSet<>();
    if (pageManager != null) {
      Page flushPage = pageManager.getPage(path);
      if (flushPage != null) {
        if (flushPage.getProperties().containsKey("sling:alias")) {
          String pageName = flushPage.getName();
          String pageAliasName = flushPage.getProperties().get("sling:alias").toString();
          aliasFlushUrls.add(resolver.map(path));
          aliasFlushUrls.add(StringUtils.replace(path, pageName, pageAliasName));
          LOG.debug("AliasFlushUrls {}", aliasFlushUrls);
        }
        if (flushPage.getProperties().containsKey(NameConstants.PN_SLING_VANITY_PATH)) {
          aliasFlushUrls.addAll(getVanityPaths(flushPage));
        }
      }
    }
    return aliasFlushUrls;
  }

  private Set<String> getVanityPaths(Page flushPage) {
    Set<String> aliasFlushUrls = new HashSet<>();
    Object pageVanityName = flushPage.getProperties().get(NameConstants.PN_SLING_VANITY_PATH);
    if (pageVanityName instanceof String[]) {
      for (String tmpVanity : (String[]) pageVanityName) {
        aliasFlushUrls.add(tmpVanity);
      }
    } else if (pageVanityName instanceof String) {
      aliasFlushUrls.add(pageVanityName.toString());
    }
    return aliasFlushUrls;
  }

  private boolean validateConfig() {
    boolean isValid = true;
    if (null == cacheFlushConfig) {
      LOG.debug("cache flush config object is null");
      return false;
    }
    if (StringUtils.isBlank(cacheFlushConfig.getAkamaiAccessToken())) {
      LOG.debug("Akamai Access Token is blank");
      isValid = false;
    }
    if (StringUtils.isBlank(cacheFlushConfig.getAkamaiClientSecret())) {
      LOG.debug("Akamai Client Secret is blank");
      isValid = false;
    }
    if (StringUtils.isBlank(cacheFlushConfig.getAkamaiHostToken())) {
      LOG.debug("Akamai Host Token is blank");
      isValid = false;
    }
    if (StringUtils.isBlank(cacheFlushConfig.getAkamaiClientToken())) {
      LOG.debug("Akamai Client Token is blank");
      isValid = false;
    }
    if (StringUtils.isBlank(cacheFlushConfig.getDefaultAkamaiDomain())) {
      LOG.debug("Akamai Default Domain is blank");
      isValid = false;
    }
    if (StringUtils.isBlank(cacheFlushConfig.getHtmlPurgeURLPrefix())) {
      LOG.debug("Html Purge URL Prefix blank");
      isValid = false;
    }
    if (StringUtils.isBlank(cacheFlushConfig.getDefaultAkamaiDomain())) {
      LOG.debug("Asset Purge URL Prefix is blank");
      isValid = false;
    }

    return isValid;
  }

  private Set<String> getFullUrl(Set<String> paths, ResourceResolver resolver) {
    Set<String> fullUrl = new HashSet<>();
    if (paths.isEmpty()) {
      return fullUrl;
    }
    for (String path : paths) {
      if (isNodeType(path, DamConstants.NT_DAM_ASSET, resolver)) {
        fullUrl.add(cacheFlushConfig.getAssetPurgeURLPrefix() + path);
      } else if (path.startsWith(KPMG_SITES_ROOT)) {
        if (path.endsWith("nocache=true")) {
          fullUrl.add(path.replace(KPMG_SITES_ROOT, cacheFlushConfig.getHtmlPurgeURLPrefix()));
        } else {
          fullUrl.add(
              path.replace(KPMG_SITES_ROOT, cacheFlushConfig.getHtmlPurgeURLPrefix())
                  .concat(".html"));
        }
      } else {
        fullUrl.add(cacheFlushConfig.getHtmlPurgeURLPrefix() + path.concat(".html"));
      }
    }
    return fullUrl;
  }

  private boolean isNodeType(String path, String type, ResourceResolver resolver) {
    Resource replicatedResource = resolver.getResource(path.replace("%20", " "));
    if (replicatedResource != null) {
      try {
        Node replicatedNode = replicatedResource.adaptTo(Node.class);
        if (replicatedNode != null && replicatedNode.getPrimaryNodeType().getName().equals(type)) {
          return true;
        }
      } catch (RuntimeException e) {
        LOG.error(
            "Exception in identifying the replicated resource's primary type while applying resource rules.",
            e);
      } catch (RepositoryException e) {
        LOG.error("RepositoryException {}", e.getMessage(), e);
      }
    }
    return false;
  }
}
