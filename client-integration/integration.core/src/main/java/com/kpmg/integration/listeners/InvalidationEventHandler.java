package com.kpmg.integration.listeners;

import static org.apache.sling.distribution.event.DistributionEventProperties.DISTRIBUTION_COMPONENT_NAME;
import static org.apache.sling.distribution.event.DistributionEventProperties.DISTRIBUTION_PATHS;
import static org.apache.sling.distribution.event.DistributionEventTopics.IMPORTER_PACKAGE_IMPORTED;

import com.kpmg.integration.constants.Constants;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.discovery.DiscoveryService;
import org.apache.sling.distribution.event.DistributionEvent;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This event handler will be called in author instance during invalidation
 */
@Component(
    immediate = true,
    service = EventHandler.class,
    property = {
      EventConstants.EVENT_TOPIC + "=" + IMPORTER_PACKAGE_IMPORTED,
      EventConstants.EVENT_FILTER + "=(|(distribution.type=ADD)(distribution.type=DELETE))"
    })
public class InvalidationEventHandler implements EventHandler {

  private static final Logger log = LoggerFactory.getLogger(InvalidationEventHandler.class);

  @Reference JobManager jobManager;

  @Reference private DiscoveryService discoveryService;

  @Reference ResourceResolverFactory resourceResolverFactory;

  @Override
  public void handleEvent(Event event) {
    try (ResourceResolver resourceResolver =
        KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory)) {
      if (event.getProperty(DISTRIBUTION_PATHS) != null) {
        String[] distributionPaths = (String[]) event.getProperty(DISTRIBUTION_PATHS);
        String distributionType = DistributionEvent.fromEvent(event).getDistType();
        String agentName = (String) event.getProperty(DISTRIBUTION_COMPONENT_NAME);
        log.debug(
            "\n Distribution Event Triggered for Agent Name {}  Distribution Type {} For Paths {} .",
            agentName,
            distributionType,
            Arrays.toString(distributionPaths));
        List<String> pagePath =
            Arrays.asList(StringUtils.stripAll(distributionPaths)).stream()
                .filter(paths -> paths.startsWith("/content"))
                .collect(Collectors.toList());
        boolean isLeader = discoveryService.getTopology().getLocalInstance().isLeader();
        if (!pagePath.isEmpty()
            && StringUtils.equalsIgnoreCase(agentName, Constants.PUBLISH_AGENT_NAME)) {
          Map<String, Object> jobProperties = new HashMap<>();
          jobProperties.put("distribution_topic", event.getTopic());
          jobProperties.put("distribution_path", pagePath);
          jobProperties.put("distribution_type", distributionType);
          if (isLeader) {
            Job job = jobManager.addJob(Constants.AKAMAI_CACHE_JOBS_QUEUE, jobProperties);
            log.debug(
                "Sling Event Job {} created successfully at queue {} with payload {}",
                job.getCreatedInstance(),
                job.getQueueName(),
                pagePath.toString());
          } else {
            log.debug("Skipping creating job for this node ");
          }
        }
      }
    } catch (LoginException ex) {
      log.error("\n Error in Akamai Event Handler: {} ", ex.getMessage());
    }
  }
}
