package com.kpmg.integration.listeners;

import static org.mockito.Mockito.*;

import com.kpmg.integration.constants.Constants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.discovery.DiscoveryService;
import org.apache.sling.discovery.InstanceDescription;
import org.apache.sling.discovery.TopologyView;
import org.apache.sling.distribution.event.DistributionEvent;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.event.Event;

@ExtendWith(MockitoExtension.class)
class InvalidationEventHandlerTest {

  @Mock JobManager jobManager;

  @Mock DiscoveryService discoveryService;

  @Mock DistributionEvent distributionEvent;

  @Mock TopologyView topologyView;

  @Mock InstanceDescription instanceDescription;

  @Mock Job job;

  @Mock ResourceResolverFactory resourceResolverFactory;

  @Mock ResourceResolver resourceResolver;

  @InjectMocks InvalidationEventHandler invalidationEventHandler;

  String[] distributionPaths = {"/content/kpmgpublic/fr/fr/test-page"};

  String path = "/content/kpmgpublic/fr/fr/test-page";

  @BeforeEach
  void setUp() throws LoginException {
    distributionEvent =
        new DistributionEvent("", "publishSubscriber", "", "ACTIVATE", distributionPaths, null);
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    when(discoveryService.getTopology()).thenReturn(topologyView);
    when(topologyView.getLocalInstance()).thenReturn(instanceDescription);
  }

  @Test
  void testHandleEventForLeader() {
    Event event = distributionEvent.toEvent(Constants.AKAMAI_CACHE_JOBS_QUEUE);

    when(instanceDescription.isLeader()).thenReturn(true);
    when(jobManager.addJob(anyString(), anyMap())).thenReturn(job);
    invalidationEventHandler.handleEvent(event);

    Map<String, Object> jobProperties = new HashMap<>();
    jobProperties.put("distribution_topic", event.getTopic());
    jobProperties.put("distribution_path", Arrays.asList(distributionPaths));
    jobProperties.put("distribution_type", "ACTIVATE");
    verify(jobManager, atLeastOnce()).addJob(Constants.AKAMAI_CACHE_JOBS_QUEUE, jobProperties);
  }

  @Test
  void testHandleNonLeaderInstance() {
    Event event = distributionEvent.toEvent(Constants.AKAMAI_CACHE_JOBS_QUEUE);
    when(instanceDescription.isLeader()).thenReturn(false);
    invalidationEventHandler.handleEvent(event);
    verify(jobManager, never()).addJob(anyString(), anyMap());
  }
}
