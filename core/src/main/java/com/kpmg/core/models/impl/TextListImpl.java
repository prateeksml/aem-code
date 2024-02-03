package com.kpmg.core.models.impl;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Component;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpmg.core.models.TextList;
import com.kpmg.core.models.TextListItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {TextList.class, Component.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = TextListImpl.RESOURCE_TYPE)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class TextListImpl extends AbstractKPMGComponentImpl implements TextList {

  public static final String RESOURCE_TYPE = "kpmg/components/content/textlist";
  public static final String ITEMS_CHILD = "items";
  public static final String DEFAULT_LIST_TYPE = "decimal-leading-zero";
  List<TextListItem> items = new ArrayList<>();

  @Getter @ValueMapValue String listType;

  @PostConstruct
  void postConstruct() {
    Optional.ofNullable(resource)
        .map(r -> r.getChild(ITEMS_CHILD))
        .map(Resource::listChildren)
        .ifPresent(
            iterator ->
                streamIterator(iterator)
                    .map(child -> child.adaptTo(TextListItem.class))
                    .filter(Objects::nonNull)
                    .forEach(items::add));
    listType = Optional.ofNullable(listType).orElse(DEFAULT_LIST_TYPE);
  }

  @Override
  @JsonProperty(ITEMS_CHILD)
  public List<TextListItem> getListItems() {
    return Collections.unmodifiableList(items);
  }

  private Stream<Resource> streamIterator(Iterator<Resource> iterator) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
  }
}
