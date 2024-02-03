package com.kpmg.integration.sling.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.services.DocumentModelService;
import com.kpmg.integration.services.IndexingService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
class ESBulkActionJobTest {

  @Mock ResourceResolverFactory resourceResolverFactory;

  @Mock private DocumentModelService documentModelService;

  @Mock private IndexingService indexingService;

  @Mock Job job;

  @Mock PageManager pageManager;

  @Mock Page page;

  @Mock PageDocument pagedoc;
  @InjectMocks private ESBulkActionJob esbulkjob;

  @Mock private ResourceResolver resourceResolver;
  private final AemContext ctx =
      new AemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK).build();
  private static final String DISTRIBUTION_PATH = "/content/kpmgpublic";

  @BeforeEach
  public void setUp() throws LoginException {
    ctx.load().json("src/test/resources/model-resources/parentpage.json", DISTRIBUTION_PATH);
    ctx.currentPage(DISTRIBUTION_PATH);
    when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(pageManager.getContainingPage(anyString())).thenReturn(ctx.currentPage());
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(indexingService.getIndex(DISTRIBUTION_PATH, resourceResolver)).thenReturn("ch_en");
    when(job.getProperty("is_dry_run")).thenReturn(false);
    when(job.getProperty("is_deep")).thenReturn(true);
    when(job.getProperty("page_path")).thenReturn(DISTRIBUTION_PATH);
  }

  @Test
  void testProcess_AddToIndex() {
    when(job.getProperty("servlet_path")).thenReturn("/bin/kpmg/es/bulkIndex");
    esbulkjob.process(job);
    verify(indexingService).bulkIndexDocuments(new HashMap<>(), "ch_en");
    verify(indexingService, never()).bulkDeleteDocuments(null, "ch_en");

    assertEquals(JobConsumer.JobResult.OK, esbulkjob.process(job));
  }

  @Test
  void testProcess_DeleteDistributionType() {
    when(job.getProperty("servlet_path")).thenReturn("/bin/kpmg/es/deleteFromIndex");
    esbulkjob.process(job);
    List<String> expected = new ArrayList<>();
    expected.add("/content/kpmgpublic");
    expected.add("/content/kpmgpublic/test-1100");
    verify(indexingService).bulkDeleteDocuments(expected, "ch_en");
    verify(indexingService, never()).indexDocument(page, "ch_en", null);
    assertEquals(JobConsumer.JobResult.OK, esbulkjob.process(job));
  }
}
