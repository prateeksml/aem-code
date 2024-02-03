package com.kpmg.integration.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.kpmg.integration.services.IndexingService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EsMaintenanceCallsServletTest {

  @InjectMocks private EsMaintenanceCallsServlet servlet;

  @Mock private IndexingService indexingService;

  @Mock private SlingHttpServletRequest request;

  @Mock private SlingHttpServletResponse response;

  private StringWriter stringWriter;
  private PrintWriter writer;

  @BeforeEach
  void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    stringWriter = new StringWriter();
    writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
  }

  @Test
  void testGetAllEsDocuments() throws IOException {
    String indexName = "testIndex";
    Map<String, Object> document = new HashMap<>();
    document.put("sl_hierarchy", "testHierarchy");
    document.put("suggestion_identifier", "testIdentifier");
    document.put("suggestion_text", "testText");
    document.put("sl_level", "testLevel");
    document.put("id", "testId");
    document.put("sl_category", "testCategory");
    List<Map<String, Object>> documents = Arrays.asList(document);
    when(request.getParameter("indexName")).thenReturn(indexName);
    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/getAllEsDocuments");
    // Assuming page and pageSize parameters are not provided, using default values
    when(indexingService.getAllDocumentsFromIndex(eq(indexName), eq(1), eq(10)))
        .thenReturn(documents);

    servlet.doGet(request, response);

    String expectedJson =
        "[{\"sl_hierarchy\":\"testHierarchy\",\"suggestion_identifier\":\"testIdentifier\",\"suggestion_text\":\"testText\",\"sl_level\":\"testLevel\",\"id\":\"testId\",\"sl_category\":\"testCategory\"}]";
    assertEquals(expectedJson, stringWriter.toString());
  }

  @Test
  void testDoGetWithMissingIndexName() throws IOException {
    when(request.getParameter("indexName")).thenReturn(null);
    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/getAllEsDocuments");

    servlet.doGet(request, response);

    assertEquals("Error: indexName parameter is missing", stringWriter.toString());
  }

  @Test
  void testGetSuggestionsIndex() throws IOException {
    List<String> suggestionsIndexes =
        Arrays.asList("localeCode1_suggestions", "localeCode2_suggestions");
    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/getSuggestionsIndex");
    when(indexingService.getSuggestionsIndex()).thenReturn(suggestionsIndexes);

    servlet.doGet(request, response);

    String expectedJson = "[\"localeCode1_suggestions\",\"localeCode2_suggestions\"]";
    assertEquals(expectedJson, stringWriter.toString());
  }

  @Test
  void testDeleteCustomDocumentById() throws IOException {
    String indexName = "testIndex";
    String documentId = "documentId";

    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/deleteCustomDocumentById");
    when(request.getParameter("indexName")).thenReturn(indexName);
    when(request.getParameter("documentId")).thenReturn(documentId);

    servlet.doDelete(request, response);

    verify(indexingService).deleteCustomDocumentById(documentId, indexName);
    assertEquals(
        "Custom Document with ID documentId deleted successfully for index " + indexName,
        stringWriter.toString());
  }

  @Test
  void testDeleteCustomDocumentByIdWithMissingParameters() throws IOException {
    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/deleteCustomDocumentById");
    when(request.getParameter("indexName")).thenReturn(null);
    when(request.getParameter("documentId")).thenReturn(null);

    servlet.doDelete(request, response);

    assertEquals("Error: Missing required parameters", stringWriter.toString());
  }

  @Test
  void testIndexCustomDocument() throws IOException {
    String indexName = "testIndex";
    String jsonRequest = "{\"index\":\"testIndex\",\"suggestion_text\":\"testText\"}";

    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/indexCustomDocument");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));
    when(request.getParameter("indexName")).thenReturn(indexName);

    servlet.doPost(request, response);

    verify(indexingService).indexCustomDocument(indexName, "testText");
    assertEquals("Document indexed successfully for index " + indexName, stringWriter.toString());
  }

  @Test
  void testIndexCustomDocument_MissingParameters() throws IOException {
    String jsonRequestMissingIndex = "{\"suggestion_text\":\"testText\"}";
    String jsonRequestMissingText = "{\"index\":\"testIndex\"}";

    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/indexCustomDocument");

    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(jsonRequestMissingIndex)));
    servlet.doPost(request, response);
    assertEquals("Error: Missing required parameters", stringWriter.toString());

    stringWriter.getBuffer().setLength(0);

    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(jsonRequestMissingText)));
    servlet.doPost(request, response);
    assertEquals("Error: Missing required parameters", stringWriter.toString());

    stringWriter.getBuffer().setLength(0);
  }

  @Test
  void testIndexCustomDocument_InvalidSuggestionText() throws IOException {
    String jsonRequest = "{\"index\":\"testIndex\", \"suggestion_text\":\"testText!!\"}";

    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/indexCustomDocument");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

    servlet.doPost(request, response);

    assertEquals(
        "Error: Only alphanumeric values are accepted with up to 50 characters long.",
        stringWriter.toString());
  }

  @Test
  void testIndexCustomDocument_SuggestionTextLength() throws IOException {
    String textOverFiftyCharacters = "A".repeat(51);
    String jsonRequest =
        "{\"index\":\"testIndex\", \"suggestion_text\":\"" + textOverFiftyCharacters + "\"}";

    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/indexCustomDocument");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

    servlet.doPost(request, response);

    assertEquals(
        "Error: Only alphanumeric values are accepted with up to 50 characters long.",
        stringWriter.toString());
  }

  @Test
  void testEditCustomDocument() throws Exception {
    String indexName = "testIndex";
    String documentId = "doc1";
    String jsonRequest =
        "{\"index\":\"testIndex\",\"documentId\":\"doc1\",\"suggestion_text\":\"testText\"}";
    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/editCustomDocument");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));
    when(request.getParameter("indexName")).thenReturn(indexName);
    when(request.getParameter("documentId")).thenReturn(documentId);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(indexingService, times(1)).editCustomDocument(indexName, documentId, "testText");
    assertEquals(
        "Custom Document with ID " + documentId + " edited successfully for index " + indexName,
        stringWriter.toString());
  }

  @Test
  void testEditCustomDocumentMissingParameters() throws Exception {
    String jsonRequest = "{\"index\":\"testIndex\"}";
    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/editCustomDocument");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    assertEquals("Error: Missing required parameters", stringWriter.toString());
  }

  @Test
  void testEditCustomDocument_InvalidSuggestionText() throws IOException {
    String jsonRequest =
        "{\"index\":\"testIndex\",\"documentId\":\"doc1\",\"suggestion_text\":\"testText!!\"}";

    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/editCustomDocument");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

    servlet.doPost(request, response);

    assertEquals(
        "Error: Only alphanumeric values are accepted with up to 50 characters long.",
        stringWriter.toString());
  }

  @Test
  void testEditCustomDocument_SuggestionTextLength() throws IOException {
    String textOverFiftyCharacters = "A".repeat(51);
    String jsonRequest =
        "{\"index\":\"testIndex\",\"documentId\":\"doc1\",\"suggestion_text\":\""
            + textOverFiftyCharacters
            + "\"}";

    when(request.getPathInfo()).thenReturn("/bin/kpmg/es/editCustomDocument");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

    servlet.doPost(request, response);

    assertEquals(
        "Error: Only alphanumeric values are accepted with up to 50 characters long.",
        stringWriter.toString());
  }
}
