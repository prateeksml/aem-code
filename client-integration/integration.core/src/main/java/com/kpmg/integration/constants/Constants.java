package com.kpmg.integration.constants;

public class Constants {

  private Constants() {}

  public static final String ELASTIC_SEARCH_JOBS_QUEUE = "elasticsearch/index/jobs";

  public static final String ES_BULK_ACTION_JOBS_QUEUE = "elasticsearch/bulkaction/jobs";

  public static final String AKAMAI_CACHE_JOBS_QUEUE = "akamaicache/flush/jobs";

  public static final String PUBLISH_AGENT_NAME = "publishSubscriber";

  public static final String SITE_ROOT = "/content/kpmgpublic/";

  public static final String EVENT = "EVENT";

  public static final String GENERIC = "GENERIC";

  public static final String INSIGHT = "INSIGHT";

  public static final String LOCATION = "LOCATION";

  public static final String CONTACT = "CONTACT";

  public static final String WCM_IO_TEMPLATE_PATH =
      "/apps/wcm-io/caconfig/editor-package/templates/editor";

  public static final String CONTEXT_AWARE_CONFIG_NODE_NAME = "sling:configRef";
  public static final String CONTEXT_AWARE_CONFIG_NODE_ROOT_LOCATION = "/conf/kpmg";
}
