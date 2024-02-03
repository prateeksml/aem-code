package com.kpmg.integration.sling.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.services.DocumentModelService;
import com.kpmg.integration.services.IndexingService;
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
public class ElasticSearchConfigJobConsumerTest {

  @Mock ResourceResolverFactory resourceResolverFactory;

  @Mock private DocumentModelService documentModelService;

  @Mock private IndexingService indexingService;

  @Mock Job job;

  @Mock PageManager pageManager;

  @Mock Page page;

  @InjectMocks private ElasticSearchJobConsumer elasticSearchJobConsumer;

  @Mock private ResourceResolver resourceResolver;

  private static final String DISTRIBUTION_PATH =
      "/content/kpmgpublic/language-masters/en/test-page";

  @BeforeEach
  public void setUp() throws LoginException {
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);

    List<String> distributionPath = new ArrayList<>();
    distributionPath.add(DISTRIBUTION_PATH);
    when(job.getProperty("distribution_path")).thenReturn(distributionPath);
    lenient()
        .when(indexingService.getIndex(DISTRIBUTION_PATH, resourceResolver))
        .thenReturn("ch_en");
  }

  @Test
  public void testProcess_AddDistributionType() {

    when(job.getProperty("distribution_type")).thenReturn("ADD");
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getPage(anyString())).thenReturn(page);
    PageDocument pageDocument = mock(PageDocument.class);
    when(documentModelService.getPageDocumentModel(page)).thenReturn(pageDocument);

    elasticSearchJobConsumer.process(job);

    verify(indexingService).indexDocument(page, "ch_en", pageDocument);
    verify(indexingService, never()).deleteDocumentById(DISTRIBUTION_PATH, "ch_en");

    assertEquals(JobConsumer.JobResult.OK, elasticSearchJobConsumer.process(job));
  }

  @Test
  public void testProcess_DeleteDistributionType() {
    when(job.getProperty("distribution_type")).thenReturn("DELETE");
    elasticSearchJobConsumer.process(job);
    verify(indexingService, never()).indexDocument(page, "ch_en", null);
    assertEquals(JobConsumer.JobResult.OK, elasticSearchJobConsumer.process(job));
  }
}
