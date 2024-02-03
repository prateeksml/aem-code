package com.kpmg.integration.servlets;

import com.kpmg.integration.constants.Constants;
import com.kpmg.integration.services.IndexingService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Servlet;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = {"/bin/kpmg/es/bulkIndex", "/bin/kpmg/es/deleteFromIndex"})
public class BulkIndexingServlet extends SlingSafeMethodsServlet {
  /** */
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(BulkIndexingServlet.class);
  @Reference private transient ResourceResolverFactory resolverfactory;
  @Reference private transient IndexingService indexingService;
  @Reference transient JobManager jobManager;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws IOException {
    LOG.debug("inside do get");
    String rootPage = request.getParameter("rootPage");
    boolean isDeep = Boolean.parseBoolean(request.getParameter("isDeep"));
    boolean isDryRun = Boolean.parseBoolean(request.getParameter("isDryRun"));
    Map<String, Object> jobProperties = new HashMap<>();
    jobProperties.put("is_dry_run", isDryRun);
    jobProperties.put("is_deep", isDeep);
    jobProperties.put("servlet_path", request.getPathInfo());
    jobProperties.put("page_path", rootPage);
    LOG.debug("creating job {}", Constants.ES_BULK_ACTION_JOBS_QUEUE);
    Job job = jobManager.addJob(Constants.ES_BULK_ACTION_JOBS_QUEUE, jobProperties);
    if (null != job) {

      LOG.debug(
          "Sling Event Job {} created successfully at queue {} with payload {}",
          job.getCreatedInstance(),
          job.getQueueName(),
          rootPage);
      response.setStatus(HttpStatus.SC_OK);
      response.getWriter().print("Job created successfully");
    }
  }
}
