package com.kpmg.integration.sling.jobs;

import com.kpmg.integration.constants.Constants;
import com.kpmg.integration.services.AkamaiCacheFlushService;
import java.io.IOException;
import java.util.List;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = JobConsumer.class,
    immediate = true,
    property = {JobConsumer.PROPERTY_TOPICS + "=" + Constants.AKAMAI_CACHE_JOBS_QUEUE})
public class AkamaiCacheFlushJob implements JobConsumer {
  private static final Logger LOG = LoggerFactory.getLogger(AkamaiCacheFlushJob.class);
  @Reference AkamaiCacheFlushService service;

  @Override
  public JobResult process(Job job) {
    LOG.info("........entering into job.........");
    try {
      String type = (String) job.getProperty("distribution_type");
      @SuppressWarnings("unchecked")
      List<String> paths = (List<String>) job.getProperty("distribution_path");
      if (service != null) {
        service.akamaiFastPurgeClient(paths);
        LOG.info("\n Job executing for path : {} , type: {}", paths, type);
        LOG.debug("properties : {}", job.getPropertyNames());
      }
      return JobResult.OK;
    } catch (IOException e) {
      LOG.error("\n Error in Job Consumer : {}  ", e.getMessage());
      return JobResult.FAILED;
    }
  }
}
