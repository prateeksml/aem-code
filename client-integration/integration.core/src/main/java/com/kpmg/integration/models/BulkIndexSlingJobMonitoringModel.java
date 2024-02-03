package com.kpmg.integration.models;

import com.adobe.acs.commons.mcp.form.GeneratedDialog;
import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentManager;
import com.kpmg.integration.beans.JobManagerBean;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.discovery.DiscoveryService;
import org.apache.sling.discovery.InstanceDescription;
import org.apache.sling.discovery.TopologyView;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.TopicStatistics;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = {Resource.class, SlingHttpServletRequest.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BulkIndexSlingJobMonitoringModel extends GeneratedDialog {

  @OSGiService transient JobManager jobManager;

  @OSGiService private DiscoveryService discoveryService;

  @OSGiService private AgentManager agentManager;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(BulkIndexSlingJobMonitoringModel.class);

  public List<JobManagerBean> getSlingJobStatus() {

    Map<String, Agent> agents = this.agentManager.getAgents();
    LOGGER.debug("These are agents" + agents);
    Set<String> keys =
        agents.keySet().stream()
            .map(key -> "com/day/cq/replication/job/" + key)
            .collect(Collectors.toSet());
    LOGGER.debug("These are keys" + keys);
    Set<String> enabledTopics = getTopics();
    LOGGER.debug("These are topics" + enabledTopics);
    enabledTopics.addAll(keys);
    LOGGER.debug("These are topics with keys " + enabledTopics);
    List<JobManagerBean> jobManagerBeans = new ArrayList<>();
    enabledTopics.stream()
        .forEach(
            topic -> {
              if (topic.equalsIgnoreCase("elasticsearch/bulkaction/jobs")) {
                long finishedJobs = 0;
                long processedJobs = 0;
                long failedJobs = 0;
                long cancelledJobs = 0;
                String Tname = null;
                for (TopicStatistics topicNew : jobManager.getTopicStatistics()) {
                  if (topicNew.getTopic().equalsIgnoreCase("elasticsearch/bulkaction/jobs")) {
                    finishedJobs = topicNew.getNumberOfFinishedJobs();
                    processedJobs = topicNew.getNumberOfProcessedJobs();
                    failedJobs = topicNew.getNumberOfFailedJobs();
                    Tname = topicNew.getTopic();
                  }
                }
                JobManagerBean jobManagerBean = new JobManagerBean();
                jobManagerBean.setTopicNew(Tname);
                jobManagerBean.setFinishedJobs(finishedJobs);
                jobManagerBean.setProcessedJobs(processedJobs);
                jobManagerBean.setFailureJobs(failedJobs);
                long finalCancelledJobs = cancelledJobs;

                Collection<Job> cancelledJobsNew =
                    jobManager.findJobs(
                        JobManager.QueryType.CANCELLED, topic, 100, (Map<String, Object>[]) null);
                if (cancelledJobsNew.size() > 0) {
                  List<String> a = new ArrayList<>();
                  for (Job error : cancelledJobsNew) {
                    a.add(error.getId().toString() + "\n");
                  }
                  jobManagerBean.setCancelledJobsNew(a);
                } else {
                  jobManagerBean.setCancelledJobs(cancelledJobsNew.size());
                }
                jobManagerBean.setTopicNew(Tname);
                jobManagerBeans.add(jobManagerBean);
              }
            });
    return jobManagerBeans;
  }

  @SuppressWarnings("rawtypes")
  private Set<String> getTopics() {
    TopologyView topology = discoveryService.getTopology();
    LOGGER.debug("These are topology " + topology);
    Set<InstanceDescription> instances = topology.getInstances();
    LOGGER.debug("These are instances " + instances);
    Iterator instanceIt = instances.iterator();
    Set<String> enabledTopics = new TreeSet<>();
    while (instanceIt.hasNext()) {
      InstanceDescription instance = (InstanceDescription) instanceIt.next();
      LOGGER.debug("These are InstanceDescription " + instance);
      enabledTopics =
          expandCSV(instance.getProperty("org.apache.sling.event.jobs.consumer.topics"));
    }
    return enabledTopics;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static Set<String> expandCSV(String csvString) {
    Set<String> set = new TreeSet();
    if (StringUtils.isNotEmpty(csvString)) {
      set.addAll(Set.of(csvString.split(",")));
    }
    return set;
  }
}
