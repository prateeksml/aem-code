package com.kpmg.integration.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.kpmg.integration.services.IndexingService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(
    value = {
      "/bin/kpmg/es/getAllEsDocuments",
      "/bin/kpmg/es/getSuggestionsIndex",
      "/bin/kpmg/es/indexCustomDocument",
      "/bin/kpmg/es/editCustomDocument",
      "/bin/kpmg/es/deleteCustomDocumentById"
    })
public class EsMaintenanceCallsServlet extends SlingAllMethodsServlet {

  private static final Logger LOG = LoggerFactory.getLogger(EsMaintenanceCallsServlet.class);
  @Reference private transient IndexingService indexingService;

  @Override
  protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
      throws IOException {
    String path = req.getPathInfo();

    if ("/bin/kpmg/es/getAllEsDocuments".equals(path)) {
      getAllEsDocuments(req, resp);
    } else if ("/bin/kpmg/es/getSuggestionsIndex".equals(path)) {
      getSuggestionsIndex(req, resp);
    }
  }

  @Override
  protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
      throws IOException {
    String path = req.getPathInfo();

    if ("/bin/kpmg/es/indexCustomDocument".equals(path)) {
      indexCustomDocument(req, resp);
    } else if ("/bin/kpmg/es/editCustomDocument".equals(path)) {
      editCustomDocument(req, resp);
    }
  }

  @Override
  protected void doDelete(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
      throws IOException {
    String path = req.getPathInfo();

    if ("/bin/kpmg/es/deleteCustomDocumentById".equals(path)) {
      deleteCustomDocumentById(req, resp);
    }
  }

  private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9 ]{1,50}$");

  private boolean isValidSuggestionText(String text) {
    if (text == null || text.trim().isEmpty()) {
      return false;
    }

    return ALPHANUMERIC_PATTERN.matcher(text).matches();
  }

  private void deleteCustomDocumentById(
      final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
    try {
      String indexName = req.getParameter("indexName");
      String documentId = req.getParameter("documentId");

      if (indexName == null || documentId == null) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Error: Missing required parameters");
        return;
      }

      indexingService.deleteCustomDocumentById(documentId, indexName);
      resp.getWriter()
          .write(
              "Custom Document with ID "
                  + documentId
                  + " deleted successfully for index "
                  + indexName);
    } catch (IOException e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("Error processing IO request: " + e.getMessage());
    }
  }

  private void editCustomDocument(
      final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
    try {
      String jsonString = IOUtils.toString(req.getReader());
      if (jsonString == null || jsonString.trim().isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Error: No body content found");
        return;
      }

      LOG.info("Received JSON: " + jsonString);
      JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

      String index = json.has("index") ? json.get("index").getAsString() : null;
      String documentId = json.has("documentId") ? json.get("documentId").getAsString() : null;
      String suggestionText =
          json.has("suggestion_text") ? json.get("suggestion_text").getAsString() : null;

      if (index == null
          || index.isEmpty()
          || documentId == null
          || documentId.isEmpty()
          || suggestionText == null
          || suggestionText.isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Error: Missing required parameters");
        return;
      }

      if (!isValidSuggestionText(suggestionText)) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
            .write("Error: Only alphanumeric values are accepted with up to 50 characters long.");
        return;
      }
      LOG.info(
          "Parsed parameters: index={}, documentId={}, suggestionText={}, suggestionIdentifier={}",
          index,
          documentId,
          suggestionText);

      indexingService.editCustomDocument(index, documentId, suggestionText);
      resp.getWriter()
          .write(
              "Custom Document with ID " + documentId + " edited successfully for index " + index);
    } catch (JsonSyntaxException e) {
      LOG.error("Error parsing JSON request", e);
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("Error parsing JSON request: " + e.getMessage());
    } catch (NullPointerException e) {
      LOG.error("Null Pointer Exception", e);
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("Null Pointer Exception: " + e.getMessage());
    }
  }

  private void getAllEsDocuments(
      final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
    String indexName = req.getParameter("indexName");
    String pageParam = req.getParameter("page");
    String pageSizeParam = req.getParameter("pageSize");

    if (indexName == null) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("Error: indexName parameter is missing");

      return;
    }

    int page = 1;
    int pageSize = 10;

    if (pageParam != null) {
      try {
        page = Integer.parseInt(pageParam);
      } catch (NumberFormatException e) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Error: page must be a number");
        return;
      }
    }

    if (pageSizeParam != null) {
      try {
        pageSize = Integer.parseInt(pageSizeParam);
      } catch (NumberFormatException e) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Error: pageSize must be a number");
        return;
      }
    }

    List<Map<String, Object>> documents =
        indexingService.getAllDocumentsFromIndex(indexName, page, pageSize);
    ObjectMapper mapper = new ObjectMapper();
    String jsonDocuments = mapper.writeValueAsString(documents);
    resp.setContentType("application/json");
    resp.getWriter().write(jsonDocuments);
  }

  private void getSuggestionsIndex(
      final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
    List<String> suggestionsIndexes = indexingService.getSuggestionsIndex();
    ObjectMapper mapper = new ObjectMapper();
    String jsonSuggestionsIndexes = mapper.writeValueAsString(suggestionsIndexes);
    resp.setContentType("application/json");
    resp.getWriter().write(jsonSuggestionsIndexes);
  }

  private void indexCustomDocument(
      final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
    try {
      String jsonString = IOUtils.toString(req.getReader());
      if (jsonString == null || jsonString.trim().isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Error: No body content found");
        return;
      }

      LOG.info("Received JSON: " + jsonString);
      JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

      String index = json.has("index") ? json.get("index").getAsString() : null;
      String suggestionText =
          json.has("suggestion_text") ? json.get("suggestion_text").getAsString() : null;

      LOG.info(
          "Received index: "
              + index
              + "Received suggestionText "
              + suggestionText
              + "Received suggestionIdentifier");

      if (index == null || suggestionText == null || index.isEmpty() || suggestionText.isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Error: Missing required parameters");
        return;
      }

      if (!isValidSuggestionText(suggestionText)) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
            .write("Error: Only alphanumeric values are accepted with up to 50 characters long.");
        return;
      }

      indexingService.indexCustomDocument(index, suggestionText);
      resp.getWriter().write("Document indexed successfully for index " + index);
    } catch (NullPointerException e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("Null Pointer Exception: " + e.getMessage());
    }
  }
}
