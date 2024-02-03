package com.kpmg.integration.services.impl;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.kpmg.integration.services.EnquiryTypeEmailMappingService;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.*;
import lombok.NonNull;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EnquiryTypeEmailMappingService.class, immediate = true)
public class EmailMappingServiceImpl implements EnquiryTypeEmailMappingService {

  private static final Logger LOG = LoggerFactory.getLogger(EmailMappingServiceImpl.class);

  @Reference ResourceResolverFactory resourceResolverFactory;

  @Override
  public String[] getEmailBasedOnEnquiryType(
      @NonNull String enquiryName, @NonNull String contentFragmentPath) {
    Map<String, String[]> emailMap = new HashMap<>();
    String[] emailArray = null;
    try (ResourceResolver resourceResolver =
        KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory)) {
      Resource parentCFResource =
          Optional.ofNullable(resourceResolver)
              .map(resolver -> resolver.getResource(contentFragmentPath))
              .orElse(null);
      List<ContentFragment> contentFragmentList = getListOfContentFragments(parentCFResource);
      contentFragmentList.forEach(
          contentFragment -> {
            if (null != contentFragment.getElement("value")
                && null != contentFragment.getElement("emailDistributionList")) {
              LOG.debug(
                  "NAME {} EMAIL  {}",
                  contentFragment.getElement("value").getContent(),
                  contentFragment.getElement("emailDistributionList").getContent());
              emailMap.put(
                  contentFragment.getElement("value").getContent(),
                  contentFragment
                      .getElement("emailDistributionList")
                      .getContent()
                      .replaceAll("\\s", "")
                      .split(","));
            }
          });
      emailArray = emailMap.get(enquiryName);

    } catch (LoginException e) {
      LOG.error("LOGIN EXCEPTION OCCURRED {}", e.getMessage());
    }
    return emailArray;
  }

  public List<ContentFragment> getListOfContentFragments(@NonNull Resource parentCFResource) {
    List<ContentFragment> cfList = new ArrayList<>();
    Iterator<Resource> iterator = parentCFResource.listChildren();
    while (iterator.hasNext()) {
      ContentFragment contentFragment = iterator.next().adaptTo(ContentFragment.class);
      if (contentFragment != null) {
        cfList.add(contentFragment);
      }
    }
    return cfList;
  }
}
