package com.kpmg.integration.services;

import com.day.cq.wcm.api.Page;
import com.kpmg.integration.models.PageDocument;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.apache.sling.api.resource.ResourceResolver;

public interface IndexingService {

  void indexDocument(Page page, String index, PageDocument pageDocument);

  void deleteDocumentById(String path, String indexName);

  String getIndex(String path, ResourceResolver resourceResolver);

  String getGeoSiteRoot(String path, ResourceResolver resourceResolver);

  void indexCustomDocument(String index, String suggestionText);

  void editCustomDocument(
      @NonNull String index, @NonNull String documentId, @NonNull String suggestionText);

  void deleteCustomDocumentById(@NonNull String documentId, @NonNull String indexName);

  List<String> getSuggestionsIndex();

  List<Map<String, Object>> getAllDocumentsFromIndex(
      @NonNull String indexName, int page, int pageSize);

  void bulkIndexDocuments(@NonNull HashMap<Page, PageDocument> page, @NonNull String index);

  void bulkDeleteDocuments(@NonNull List<String> paths, @NonNull String index);
}
