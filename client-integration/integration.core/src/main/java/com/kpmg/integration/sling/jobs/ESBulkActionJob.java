package com.kpmg.integration.sling.jobs;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.integration.constants.Constants;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.services.DocumentModelService;
import com.kpmg.integration.services.IndexingService;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = JobConsumer.class,
    immediate = true,
    property = {JobConsumer.PROPERTY_TOPICS + "=" + Constants.ES_BULK_ACTION_JOBS_QUEUE})
public class ESBulkActionJob implements JobConsumer {
  private static final Logger LOG = LoggerFactory.getLogger(ESBulkActionJob.class);
  @Reference DocumentModelService documentModelService;
  @Reference IndexingService indexingService;
  @Reference ResourceResolverFactory resourceResolverFactory;

  @Override
  public JobResult process(Job job) {
    LOG.info("........entering into ES Bulk Action job.........");
    StringBuilder responseString = new StringBuilder();
    boolean isDryRun = (boolean) job.getProperty("is_dry_run");
    boolean isDeep = (boolean) job.getProperty("is_deep");
    String servletPath = (String) job.getProperty("servlet_path");
    String pagePath = (String) job.getProperty("page_path");
    try (ResourceResolver resourceResolver =
        KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory)) {
      PageManager pm = resourceResolver.adaptTo(PageManager.class);
      if (null != pm && null != indexingService) {
        Page page = pm.getContainingPage(pagePath);
        if (null != page) {
          String elasticIndex = indexingService.getIndex(page.getPath(), resourceResolver);
          LOG.debug("index name :{}", elasticIndex);
          if (("/bin/kpmg/es/bulkIndex").equals(servletPath)) {
            LOG.debug("bulk index begins");
            responseString = addToIndex(page, isDeep, isDryRun, elasticIndex);
          } else if (("/bin/kpmg/es/deleteFromIndex").equals(servletPath)) {
            LOG.debug("delete index begins");
            responseString = deleteFromIndex(page, isDeep, isDryRun, elasticIndex);
          }
          LOG.info("\n response string {} ", responseString);
        }
      }
      return JobResult.OK;

    } catch (LoginException ex) {
      LOG.error("\n Error in ES Bulk Action Job: {} ", ex.getMessage());
      return JobResult.FAILED;
    }
  }

  private StringBuilder addToIndex(
      Page page, boolean isDeep, boolean isDryRun, String elasticIndex) {
    LOG.debug("Inside add to index");
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<Page> pageIterator = page.listChildren(null, isDeep);
    PageDocument pageDocument = documentModelService.getPageDocumentModel(page);
    HashMap<Page, PageDocument> pagesMap = new HashMap<>();
    if (isValidPage(page) && null != pageDocument) {
      pagesMap.put(page, pageDocument);
      stringBuilder.append(page.getPath() + "," + isDryRun);
    }
    Page childPage;
    while (null != pageIterator && pageIterator.hasNext()) {
      childPage = pageIterator.next();
      pageDocument = documentModelService.getPageDocumentModel(childPage);
      LOG.debug("Page in the iteration: {}", childPage.getPath());
      if (isValidPage(childPage) && null != pageDocument) {
        pagesMap.put(childPage, pageDocument);
        stringBuilder.append(childPage.getPath() + ",");
        stringBuilder.append(isDryRun);
      }
    }
    LOG.debug("pages map size: {}", pagesMap.size());
    if (!isDryRun) {
      indexingService.bulkIndexDocuments(pagesMap, elasticIndex);
    }
    return stringBuilder;
  }

  private StringBuilder deleteFromIndex(
      Page page, boolean isDeep, boolean isDryRun, String elasticIndex) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<Page> pageIterator = page.listChildren(null, isDeep);
    Page childPage;
    List<String> documents = new ArrayList<>();
    if (isValidPage(page)) {
      documents.add(page.getPath());
      stringBuilder.append(page.getPath() + "," + isDryRun);
    }
    while (null != pageIterator && pageIterator.hasNext()) {
      childPage = pageIterator.next();
      LOG.debug("Page in the iteration: {}", childPage.getPath());
      if (isValidPage(childPage)) {
        documents.add(childPage.getPath());
        stringBuilder.append(childPage.getPath() + ",");
        stringBuilder.append(isDryRun);
      }
    }
    LOG.debug("documents : {}", documents);
    if (!isDryRun) {
      indexingService.bulkDeleteDocuments(documents, elasticIndex);
    }

    return stringBuilder;
  }

  private boolean isValidPage(Page page) {
    boolean isValid = true;
    if (null != page && page.getProperties() != null) {
      Object lastReplication = page.getProperties().get("cq:lastReplicationAction");
      Object hideInSearch = page.getProperties().get("hideInSearch");
      if (lastReplication == null || !lastReplication.equals("Activate")) {
        isValid = false;
      }
      if (hideInSearch != null && hideInSearch.equals("true")) {
        isValid = false;
      }
      return isValid;
    }
    return false;
  }
}
