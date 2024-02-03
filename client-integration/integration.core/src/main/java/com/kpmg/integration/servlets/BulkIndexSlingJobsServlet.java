package com.kpmg.integration.servlets;

import com.adobe.granite.rest.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.JobManager.QueryType;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.post.JSONResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/triggerclearoutexcessslingjobs")
public class BulkIndexSlingJobsServlet extends SlingAllMethodsServlet {

  private static final Logger LOG = LoggerFactory.getLogger(BulkIndexSlingJobsServlet.class);
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE = "message";

  @Reference transient JobManager jobManager;

  @Override
  protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    // Set response headers.
    response.setContentType(JSONResponse.RESPONSE_CONTENT_TYPE);
    response.setCharacterEncoding(Constants.DEFAULT_CHARSET);
    JsonObject jsonResponse = new JsonObject();

    String jobTopic = request.getParameter("jobTopic");
    if (StringUtils.isNotEmpty(jobTopic)) {
      cleanSlingJobs(jobTopic, QueryType.ACTIVE);
      cleanSlingJobs(jobTopic, QueryType.ALL);
      cleanSlingJobs(jobTopic, QueryType.CANCELLED);
      cleanSlingJobs(jobTopic, QueryType.DROPPED);
      cleanSlingJobs(jobTopic, QueryType.ERROR);
      cleanSlingJobs(jobTopic, QueryType.GIVEN_UP);
      cleanSlingJobs(jobTopic, QueryType.HISTORY);
      cleanSlingJobs(jobTopic, QueryType.QUEUED);
      cleanSlingJobs(jobTopic, QueryType.STOPPED);
      cleanSlingJobs(jobTopic, QueryType.SUCCEEDED);
      jsonResponse.addProperty(MESSAGE, "Jobs are cleared");
    } else {
      jsonResponse.addProperty(MESSAGE, "Job Topic is Empty");
    }
    try (PrintWriter out = response.getWriter()) {
      out.print(new Gson().toJson(jsonResponse));
    }
  }

  public void cleanSlingJobs(String jobTopic, QueryType type) {
    Collection<Job> allJobs =
        jobManager.findJobs(type, jobTopic, 1000, (Map<String, Object>[]) null);
    if (!allJobs.isEmpty()) {
      allJobs.stream()
          .forEach(
              childJob -> {
                jobManager.stopJobById(childJob.getId());
                jobManager.removeJobById(childJob.getId());
              });
      cleanSlingJobs(jobTopic, type);
    }
  }
}
