package com.kpmg.integration.sling.jobs;

import static com.kpmg.integration.constants.Constants.SITE_ROOT;
import static com.kpmg.integration.util.KPMGUtilities.getHideInSearch;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.integration.constants.Constants;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.services.DocumentModelService;
import com.kpmg.integration.services.IndexingService;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.distribution.DistributionRequestType;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = JobConsumer.class,
    immediate = true,
    property = {JobConsumer.PROPERTY_TOPICS + "=" + Constants.ELASTIC_SEARCH_JOBS_QUEUE})
public class ElasticSearchJobConsumer implements JobConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchJobConsumer.class);

  @Reference ResourceResolverFactory resourceResolverFactory;

  @Reference DocumentModelService documentModelService;

  @Reference IndexingService indexingService;

  @Override
  public JobResult process(Job job) {
    try (ResourceResolver resourceResolver =
        KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory)) {
      List<String> distributionPath = (List<String>) job.getProperty("distribution_path");
      String type = (String) job.getProperty("distribution_type");
      for (String path : distributionPath) {
        LOG.debug("Processing Elastic Search Job Of Type {} For {}", type, path);
        if (StringUtils.equalsIgnoreCase(type, DistributionRequestType.ADD.name())) {
          PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
          Page page = Optional.ofNullable(pageManager).map(pm -> pm.getPage(path)).orElse(null);
          String elasticIndex = indexingService.getIndex(path, resourceResolver);
          if (getHideInSearch(page)) {
            LOG.debug("Hiding {} from search ", path);
            indexingService.deleteDocumentById(path, elasticIndex);
          } else {
            PageDocument pageDocument = documentModelService.getPageDocumentModel(page);
            if (null != pageDocument) {
              LOG.debug("Start indexing the page {} to {}", path, elasticIndex);
              indexingService.indexDocument(page, elasticIndex, pageDocument);
            }
          }
        } else if (StringUtils.equalsIgnoreCase(type, DistributionRequestType.DELETE.name())) {
          String elasticIndex = getElasticIndex(path);
          if (null != elasticIndex) {
            indexingService.deleteDocumentById(path, elasticIndex);
          }
        }
      }
      return JobResult.OK;
    } catch (LoginException ex) {
      LOG.error("\n Error in Elastic Search Job Consumer: {} ", ex.getMessage());
      return JobResult.FAILED;
    }
  }

  private String getElasticIndex(String path) {
    return Optional.of(path)
        .map(t -> t.split(SITE_ROOT)[1])
        .map(t -> t.split("/", 2))
        .map(st -> String.join("_", st))
        .map(s -> s.split("/")[0])
        .orElse(null);
  }
}
