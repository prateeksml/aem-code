package com.kpmg.integration.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BulkIndexingServletTest {

  @Mock MockSlingHttpServletRequest request;

  @Mock Job job;
  @Mock JobManager jobManager;
  @InjectMocks BulkIndexingServlet servlet;

  @BeforeEach
  void setUp() {
    servlet = Mockito.spy(servlet);
    Map<String, Object> params = new HashMap<>();
    params.put("isDryRun", "true");
    params.put("isDeep", "true");
    params.put("rootPage", "/content/kpmgpublic");
    request.setParameterMap(params);
  }

  @Test
  void doGetTest() throws IOException {
    when(jobManager.addJob(anyString(), anyMap())).thenReturn(job);
    MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();
    servlet.doGet(request, response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }
}
