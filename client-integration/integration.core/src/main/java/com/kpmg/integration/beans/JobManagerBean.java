package com.kpmg.integration.beans;

import java.util.List;
import lombok.Data;

@Data
public class JobManagerBean {

  long allJobs;
  long activeJobs;
  long cancelledJobs;
  long droppedJobs;
  long errorJobs;
  long givenUpJobs;
  long historyJobs;
  long queuedJobs;
  long stoppedJobs;
  long succeededJobs;
  long finishedJobs;
  long processedJobs;
  long failureJobs;
  String jobTopic;
  String topicNew;

  List<String> errorJobsNew;
  List<String> queuedJobsNew;
  List<String> succeededJobsNew;
  List<String> cancelledJobsNew;
}
