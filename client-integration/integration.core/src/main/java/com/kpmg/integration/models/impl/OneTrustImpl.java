package com.kpmg.integration.models.impl;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.integration.models.OneTrust;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = OneTrust.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OneTrustImpl implements OneTrust {
  @Inject Page currentPage;

  @Inject SiteSettingsConfig siteSettings;

  public static <T> List<String> toOptionsGeneric(T[] array, Function<T, String> transformer) {
    if (ArrayUtils.isEmpty(array)) return Collections.emptyList();
    else {
      return Arrays.stream(array).map(transformer::apply).collect(Collectors.toList());
    }
  }

  public List<String> getContextAwareConfig(
      String currentPage, ResourceResolver resourceResolver, String attribute) {
    List<String> all = new ArrayList<>();
    String currentpath = StringUtils.isNotBlank(currentPage) ? currentPage : StringUtils.EMPTY;
    String[] a = currentpath.split("/");
    String ap = a[0] + "/" + a[1] + "/" + a[2] + "/" + a[3] + "/" + a[4];
    Resource currentresource = resourceResolver.getResource(ap);
    if (currentresource != null) {
      ConfigurationBuilder confBuilder = currentresource.adaptTo(ConfigurationBuilder.class);
      if (confBuilder != null) {
        List<String> list = getOptions(confBuilder, attribute);
        if (getOptions(confBuilder, attribute) != null
            && !(getOptions(confBuilder, attribute).isEmpty())) {
          for (String option : list) {
            all.add(option);
          }
        }

        return all;
      }
    }
    return null;
  }

  public SiteSettingsConfig getContextConfig(
      String currentPage, ResourceResolver resourceresolver) {
    String currentpath = StringUtils.isNotBlank(currentPage) ? currentPage : StringUtils.EMPTY;
    Resource currentresource = resourceresolver.getResource(currentpath);
    if (currentresource != null) {
      ConfigurationBuilder confBuilder = currentresource.adaptTo(ConfigurationBuilder.class);
      if (confBuilder != null) {
        return confBuilder.as(SiteSettingsConfig.class);
      }
    }
    return null;
  }

  public List<String> getOptions(ConfigurationBuilder configurationBuilder, String obj) {
    if (obj.equals("attribute")) {
      return Optional.ofNullable(configurationBuilder)
          .map(b -> b.as(SiteSettingsConfig.class))
          .map(SiteSettingsConfig::items)
          .map(items -> toOptionsGeneric(items, item -> String.valueOf((item.attribute()))))
          .orElse(Collections.emptyList());
    } else if (obj.equals("categoryName")) {
      return Optional.ofNullable(configurationBuilder)
          .map(b -> b.as(SiteSettingsConfig.class))
          .map(SiteSettingsConfig::items)
          .map(items -> toOptionsGeneric(items, item -> String.valueOf((item.categoryName()))))
          .orElse(Collections.emptyList());
    }
    return null;
  }

  @Inject ResourceResolver resourceResolver;

  List<String> attributeList;
  List<String> categoryList;
  String onetrustList;

  public MultiValuedMap<String, String> getMap(
      List<String> attributeList, List<String> categoryList) {

    MultiValuedMap<String, String> listMap = new ArrayListValuedHashMap<>();
    for (int i = 0; i < categoryList.size(); i++) {
      listMap.put(attributeList.get(i), categoryList.get(i));
    }
    return listMap;
  }

  MultiValuedMap<String, String> keyMap = new ArrayListValuedHashMap<>();
  ;

  @PostConstruct
  public void init() {

    attributeList = getContextAwareConfig(currentPage.getPath(), resourceResolver, "attribute");
    categoryList = getContextAwareConfig(currentPage.getPath(), resourceResolver, "categoryName");
    keyMap = getMap(attributeList, categoryList);
    siteSettings = getContextConfig(currentPage.getPath(), resourceResolver);
  }

  @Override
  public String getOneTrustScript() {
    if (siteSettings != null) {
      onetrustList = siteSettings.oneTrust();
    }
    return onetrustList;
  }

  @Override
  public List<String> getAttributeOptions() {
    return attributeList;
  }

  @Override
  public List<String> getCategoryOptions() {
    return categoryList;
  }

  @Override
  public HashMap<String, List<String>> getMapOptions() {
    HashMap<String, List<String>> mapObj = new HashMap<>();
    List<String> list = new ArrayList<>();
    for (Map.Entry<String, String> m : keyMap.entries()) {
      if (mapObj.containsKey(m.getKey())) {
        list.add(m.getValue());
      } else {
        list = new ArrayList<>();
        list.add(m.getValue());
      }
      mapObj.put(m.getKey(), list);
    }
    return mapObj;
  }
}
