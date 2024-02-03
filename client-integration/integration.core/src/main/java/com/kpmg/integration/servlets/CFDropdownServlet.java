package com.kpmg.integration.servlets;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.kpmg.integration.models.FormOptions;
import java.util.*;
import javax.servlet.Servlet;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/kpmg/cfdropdown")
public class CFDropdownServlet extends SlingSafeMethodsServlet {

  private static final Logger LOG = LoggerFactory.getLogger(CFDropdownServlet.class);

  transient Resource pathResource;
  transient ValueMap valueMap;
  transient List<Resource> resourceList;
  private static final String NT_UNSTRUCTURED = "nt:unstructured";

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

    ResourceResolver resourceResolver;
    resourceResolver = request.getResourceResolver();
    pathResource = request.getResource();
    resourceList = new ArrayList<>();
    HashMap vm1 = new HashMap();
    TreeMap vm2 = new TreeMap<>();

    FormOptions model = request.adaptTo(FormOptions.class);
    String Path = model.getFragmentPath();
    // assert Path != null;
    try {

      Resource resource = resourceResolver.getResource(Path);
      if (resource.hasChildren()) {
        Iterable<Resource> iterator = resource.getChildren();
        for (Resource childcfs : iterator) {
          valueMap = new ValueMapDecorator(new TreeMap<>());
          ContentFragment contentfragments = childcfs.adaptTo(ContentFragment.class);
          assert contentfragments != null;
          if (contentfragments != null) {
            if (contentfragments.getElement("title").getValue().getValue() != null
                && contentfragments.getElement("value").getValue().getValue() != null) {
              String sortOrder =
                  contentfragments.getElement("sortOrder").getValue().getValue().toString();
              String key = contentfragments.getElement("title").getValue().getValue().toString();
              String val = contentfragments.getElement("value").getValue().getValue().toString();
              valueMap.put("text", key);
              valueMap.put("value", val);
              if (sortOrder.equalsIgnoreCase("0")) {
                val = "default";
                vm1.put(key, val);
                vm2.put(sortOrder, key);
              } else {
                vm1.put(key, val);
                vm2.put(sortOrder, key);
              }
            }
          }
        }
      }

    } catch (NullPointerException e) {

      LOG.error("Exception occured:::::" + e.toString());
    }
    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    Iterator<Map.Entry<String, String>> itr = vm2.entrySet().iterator();
    while (itr.hasNext()) {
      Map.Entry<String, String> entry = itr.next();
      map.put(entry.getValue(), (String) vm1.get(entry.getValue()));
    }
    /*Create a DataSource that is used to populate the drop-down control*/
    //  DataSource dataSource = new SimpleDataSource(resourceList.iterator());
    // request.setAttribute(DataSource.class.getName(), dataSource);
    // Creating the data source object
    @SuppressWarnings({"unchecked", "rawtypes"})
    DataSource ds =
        new SimpleDataSource(
            new TransformIterator<>(
                map.keySet().iterator(),
                (Transformer)
                    o -> {
                      String dropValue = (String) o;
                      ValueMap vm = new ValueMapDecorator(new HashMap<>());
                      vm.put("text", dropValue);
                      vm.put("value", map.get(dropValue));
                      return new ValueMapResource(
                          resourceResolver,
                          new ResourceMetadata(),
                          JcrConstants.NT_UNSTRUCTURED,
                          vm);
                    }));
    request.setAttribute(DataSource.class.getName(), ds);
  }
}
