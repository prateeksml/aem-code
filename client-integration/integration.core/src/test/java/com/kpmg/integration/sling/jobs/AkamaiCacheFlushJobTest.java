package com.kpmg.integration.sling.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.api.client.http.HttpResponse;
import com.kpmg.integration.services.AkamaiCacheFlushService;
import com.kpmg.integration.services.DocumentModelService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AkamaiCacheFlushJobTest {
  @Mock ResourceResolverFactory resourceResolverFactory;

  @Mock private DocumentModelService documentModelService;

  @Mock private AkamaiCacheFlushService service;
  ;

  @Mock Job job;

  @Mock PageManager pageManager;

  @Mock Page page;

  @InjectMocks private AkamaiCacheFlushJob akamaiJobConsumer;

  @Mock private ResourceResolver resourceResolver;

  private static final String DISTRIBUTION_PATH =
      "/content/kpmgpublic/language-masters/en/test-page";

  @BeforeEach
  public void setUp() throws LoginException, IOException {

    List<String> distributionPath = new ArrayList<>();
    distributionPath.add(DISTRIBUTION_PATH);
    when(job.getProperty("distribution_path")).thenReturn(distributionPath);
    lenient()
        .when(service.akamaiFastPurgeClient(distributionPath))
        .thenReturn(mock(HttpResponse.class));
  }

  @Test
  void testProcess_ActivateDistributionType() {

    when(job.getProperty("distribution_type")).thenReturn("activate");
    akamaiJobConsumer.process(job);
    assertEquals(JobConsumer.JobResult.OK, akamaiJobConsumer.process(job));
  }

  @Test
  void testProcess_DeleteDistributionType() throws IOException {
    when(job.getProperty("distribution_type")).thenReturn("delete");
    akamaiJobConsumer.process(job);
    List<String> urls = new ArrayList<String>();
    urls.add(DISTRIBUTION_PATH);
    verify(service).akamaiFastPurgeClient(urls);
    assertEquals(JobConsumer.JobResult.OK, akamaiJobConsumer.process(job));
  }
}
