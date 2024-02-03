package com.kpmg.integration.services.impl;

import static com.kpmg.integration.constants.Constants.SITE_ROOT;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.services.ElasticClientService;
import com.kpmg.integration.services.IndexingService;
import com.kpmg.integration.util.KPMGUtilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = IndexingService.class, immediate = true)
public class IndexingServiceImpl implements IndexingService {

  private static final Logger log = LoggerFactory.getLogger(IndexingServiceImpl.class);

  @Reference ElasticClientService elasticClientService;

  ElasticsearchAsyncClient elasticsearchAsyncClient;

  @Override
  public void indexDocument(
      @NonNull Page page, @NonNull String index, @NonNull PageDocument pageDocument) {
    elasticsearchAsyncClient = elasticClientService.getESAsyncClient();
    elasticsearchAsyncClient
        .index(i -> i.index(index).id(page.getPath()).document(pageDocument))
        .whenComplete(
            (indexresponse, exception) -> {
              if (exception != null) {
                log.error("Failed to index {}", exception.getMessage());
              } else {
                log.info(
                    "Page {} Indexed with version {}", page.getPath(), indexresponse.version());
              }
            });
  }

  @Override
  public void bulkIndexDocuments(
      @NonNull HashMap<Page, PageDocument> pages, @NonNull String index) {
    Set<Page> keyset = pages.keySet();
    BulkRequest.Builder br = new BulkRequest.Builder();
    for (Page page : keyset) {
      br.operations(
          op -> op.index(idx -> idx.index(index).id(page.getPath()).document(pages.get(page))));
    }
    elasticsearchAsyncClient = elasticClientService.getESAsyncClient();
    CompletableFuture<BulkResponse> result = elasticsearchAsyncClient.bulk(br.build());
    result.whenComplete(
        (indexresponse, exception) -> {
          if (exception != null) {
            log.error("Failed to index {}", exception.getMessage());
          } else {
            log.info("{} Pages Indexed successfully", indexresponse.items().size());
          }
        });
  }

  @Override
  public void deleteDocumentById(@NonNull String path, @NonNull String indexName) {
    elasticsearchAsyncClient = elasticClientService.getESAsyncClient();
    log.debug("deleting page ..{}", path);
    DeleteRequest deleteRequest = DeleteRequest.of(d -> d.index(indexName).id(path));
    elasticsearchAsyncClient
        .delete(deleteRequest)
        .whenComplete(
            (indexresponse, exception) -> {
              if (exception != null) {
                log.error("Failed to delete {}", exception.getMessage());
              } else {
                log.info(
                    "deleted page {} from index {}", indexresponse.id(), indexresponse.index());
              }
            });
  }

  @Override
  public void bulkDeleteDocuments(@NonNull List<String> paths, @NonNull String index) {
    BulkRequest.Builder br = new BulkRequest.Builder();
    for (String path : paths) {
      br.operations(op -> op.delete(idx -> idx.index(index).id(path)));
    }
    elasticsearchAsyncClient = elasticClientService.getESAsyncClient();
    CompletableFuture<BulkResponse> result = elasticsearchAsyncClient.bulk(br.build());
    result.whenComplete(
        (response, exception) -> {
          if (exception != null) {
            log.error("Failed to delete documents {}", exception.getMessage());
          } else {
            log.info("{} Documents deleted successfully", response.items().size());
          }
        });
  }

  @Override
  public String getIndex(@NonNull String path, @NonNull ResourceResolver resourceResolver) {
    SiteSettingsConfig siteSettingsConfig =
        KPMGUtilities.getContextAwareConfig(path, resourceResolver);

    return Optional.ofNullable(siteSettingsConfig)
        .map(
            siteSettings ->
                new StringBuilder(siteSettings.siteCountry())
                    .append(("_"))
                    .append(siteSettingsConfig.siteLocale())
                    .toString()
                    .toLowerCase())
        .orElse(StringUtils.EMPTY);
  }

  @Override
  public String getGeoSiteRoot(@NonNull String path, @NonNull ResourceResolver resourceResolver) {
    SiteSettingsConfig siteSettingsConfig =
        KPMGUtilities.getContextAwareConfig(path, resourceResolver);

    return Optional.ofNullable(siteSettingsConfig)
        .map(
            siteSettings ->
                new StringBuilder(SITE_ROOT)
                    .append(siteSettings.siteCountry())
                    .append("/")
                    .append(siteSettingsConfig.siteLocale())
                    .append("/")
                    .toString()
                    .toLowerCase())
        .orElse(StringUtils.EMPTY);
  }

  @Override
  public void indexCustomDocument(@NonNull String index, @NonNull String suggestionText) {
    elasticsearchAsyncClient = elasticClientService.getESAsyncClient();

    long currentTimeMillis = System.currentTimeMillis();
    long randomPart = ThreadLocalRandom.current().nextLong(1, 999999999L);
    String suggestionIdentifier = String.valueOf(currentTimeMillis) + String.valueOf(randomPart);

    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("suggestion_text", suggestionText);
    jsonMap.put("suggestion_identifier", suggestionIdentifier);
    jsonMap.put("sl_category", "OTHERS");

    elasticsearchAsyncClient
        .index(i -> i.index(index).document(jsonMap))
        .whenComplete(
            (indexresponse, exception) -> {
              if (exception != null) {
                log.error("Failed to index custom document {}", exception.getMessage());
              } else {
                log.info("Custom document indexed with ID {}", indexresponse.id());
              }
            });
  }

  @Override
  public void editCustomDocument(
      @NonNull String index, @NonNull String documentId, @NonNull String suggestionText) {
    elasticsearchAsyncClient = elasticClientService.getESAsyncClient();
    elasticsearchAsyncClient
        .get(g -> g.index(index).id(documentId), Map.class)
        .whenComplete(
            (getResponse, getException) -> {
              if (getException != null) {
                log.error("Failed to fetch document with ID {}", documentId, getException);
                return;
              }

              Map<String, Object> sourceMap = getResponse.source();
              if (!"OTHERS".equals(sourceMap.get("sl_category"))) {
                log.error(
                    "Cannot edit document with ID {} as its sl_category is not 'OTHERS'",
                    documentId);
                return;
              }

              Map<String, Object> updates = new HashMap<>();
              updates.put("suggestion_text", suggestionText);
              elasticsearchAsyncClient
                  .update(u -> u.index(index).id(documentId).doc(updates), Map.class)
                  .whenComplete(
                      (updateResponse, updateException) -> {
                        if (updateException != null) {
                          log.error(
                              "Failed to update custom document {}", documentId, updateException);
                        } else {
                          log.info(
                              "Custom document with ID {} updated successfully",
                              updateResponse.id());
                        }
                      });
            });
  }

  @Override
  public void deleteCustomDocumentById(@NonNull String documentId, @NonNull String indexName) {
    elasticsearchAsyncClient = elasticClientService.getESAsyncClient();

    elasticsearchAsyncClient
        .get(g -> g.index(indexName).id(documentId), Map.class)
        .whenComplete(
            (getResponse, getException) -> {
              if (getException != null) {
                log.error("Failed to fetch document with ID {}", documentId, getException);
                return;
              }

              Map<String, Object> sourceMap = getResponse.source();
              if (!"OTHERS".equals(sourceMap.get("sl_category"))) {
                log.error(
                    "Cannot delete document with ID {} as its sl_category is not 'OTHERS'",
                    documentId);
                return;
              }

              DeleteRequest deleteRequest =
                  new DeleteRequest.Builder().index(indexName).id(documentId).build();
              elasticsearchAsyncClient
                  .delete(deleteRequest)
                  .whenComplete(
                      (deleteResponse, deleteException) -> {
                        if (deleteException != null) {
                          log.error("Failed to delete {}", deleteException.getMessage());
                        } else {
                          log.info(
                              "deleted document {} from index {}",
                              deleteResponse.id(),
                              deleteResponse.index());
                        }
                      });
            });
  }

  @Override
  public List<String> getSuggestionsIndex() {
    List<String> suggestionIndexes = new ArrayList<>();

    try (RestClient restClient = elasticClientService.createRestClient()) {
      Request request = new Request("GET", "/_cat/indices");
      Response response = restClient.performRequest(request);
      String responseBody = EntityUtils.toString(response.getEntity());
      Pattern pattern = Pattern.compile("\\b\\S*_suggestions\\b");
      Matcher matcher = pattern.matcher(responseBody);

      while (matcher.find()) {
        String indexName = matcher.group();
        suggestionIndexes.add(indexName);
      }
    } catch (IOException e) {
      log.error("Failed to fetch indices", e);
    }
    return suggestionIndexes;
  }

  @Override
  public List<Map<String, Object>> getAllDocumentsFromIndex(
      @NonNull String indexName, int page, int pageSize) {
    List<Map<String, Object>> documents = new ArrayList<>();

    int from = (page - 1) * pageSize;

    try (RestClient restClient = elasticClientService.createRestClient()) {
      Request request = new Request("GET", "/" + indexName + "/_search");

      request.addParameter("size", String.valueOf(pageSize));
      request.addParameter("from", String.valueOf(from));

      request.setJsonEntity("{\"query\": {\"match_all\": {}}}");

      Response response = restClient.performRequest(request);
      String responseBody = EntityUtils.toString(response.getEntity());
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonResponse = mapper.readTree(responseBody);
      JsonNode hits = jsonResponse.get("hits").get("hits");

      for (JsonNode hit : hits) {
        JsonNode source = hit.get("_source");
        Map<String, Object> document = new HashMap<>();
        document.put("id", hit.get("_id") != null ? hit.get("_id").asText() : null);
        document.put(
            "sl_hierarchy",
            source.get("sl_hierarchy") != null ? source.get("sl_hierarchy").asText() : null);
        document.put(
            "suggestion_identifier",
            source.get("suggestion_identifier") != null
                ? source.get("suggestion_identifier").asText()
                : null);
        document.put(
            "suggestion_text",
            source.get("suggestion_text") != null ? source.get("suggestion_text").asText() : null);
        document.put(
            "sl_level", source.get("sl_level") != null ? source.get("sl_level").asText() : null);
        document.put(
            "sl_category",
            source.get("sl_category") != null ? source.get("sl_category").asText() : null);

        documents.add(document);
      }
    } catch (IOException e) {
      log.error("Failed to fetch data", e);
    }

    return documents;
  }
}
