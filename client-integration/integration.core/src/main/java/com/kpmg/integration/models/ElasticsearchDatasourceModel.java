package com.kpmg.integration.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class ElasticsearchDatasourceModel {

  @Inject private SlingHttpServletRequest request;

  @Inject private Resource resource;

  @Inject private ResourceResolver resourceResolver;

  @Inject @Optional private String type;
  private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchDatasourceModel.class);

  @PostConstruct
  protected void init() {
    request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());

    Resource datasource = resource.getChild("datasource");
    ValueMap dsProperties = datasource.getValueMap();
    String itemResourceType = dsProperties.get("itemResourceType", String.class);

    List<String> elasticsearchDocuments = getElasticsearchDocuments();

    List<Resource> fakeResourceList = new ArrayList<>();
    int i = 0;
    for (String document : elasticsearchDocuments) {
      i++;
      ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
      vm.put("suggestion_text", document);
      fakeResourceList.add(new ValueMapResource(resourceResolver, "", itemResourceType, vm));
      LOGGER.debug("Elasticsearch documents: {}", elasticsearchDocuments);
    }

    DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
    request.setAttribute(DataSource.class.getName(), ds);
  }

  private List<String> getElasticsearchDocuments() {
    List<String> list = new ArrayList<>();
    list.add("sample item 1");
    list.add("sample item 2");
    return list;
  }
}
