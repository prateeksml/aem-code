package com.kpmg.integration.servlets;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import java.io.IOException;
import javax.servlet.ServletException;
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
class BulkIndexSlingJobsServletTest {
  @Mock MockSlingHttpServletRequest request;

  @Mock Job job;
  @Mock JobManager jobManager;
  @InjectMocks BulkIndexSlingJobsServlet servlet;

  private final AemContext context = new AemContext();
  String jobTopic = null;

  @BeforeEach
  void setUp() {
    servlet = Mockito.spy(servlet);
    jobTopic = "ref-updater/delete";
    request.setAttribute("jobTopic", "ref-updater/delete");
  }

  @Test
  void doPostTest() throws IOException, ServletException {
    MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();
    jobTopic = "ref-updater/delete";
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.ACTIVE);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.ALL);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.CANCELLED);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.DROPPED);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.ERROR);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.GIVEN_UP);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.HISTORY);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.QUEUED);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.STOPPED);
    servlet.cleanSlingJobs(jobTopic, JobManager.QueryType.SUCCEEDED);
    servlet.doPost(request, response);
    assertEquals(HttpStatus.SC_OK, response.getStatus());
  }
}
