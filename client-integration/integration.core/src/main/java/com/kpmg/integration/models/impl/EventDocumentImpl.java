package com.kpmg.integration.models.impl;

import static com.kpmg.integration.constants.Constants.EVENT;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpmg.core.caconfig.DateFormatConfig;
import com.kpmg.core.dateformat.DateFormatService;
import com.kpmg.core.dateformat.DateFormatsVO;
import com.kpmg.integration.models.PageDocument;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = Resource.class,
    adapters = {PageDocument.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EventDocumentImpl extends GenericDocumentImpl {

  @SlingObject private SlingHttpServletRequest request;

  @OSGiService DateFormatService dateFormatService;

  @Getter
  @JsonProperty("event_location")
  @ValueMapValue(name = "eventLocation")
  private String eventLocation;

  @Getter
  @JsonProperty("event_type")
  @ValueMapValue(name = "eventType")
  private String eventType;

  @JsonIgnore
  @Getter
  @ValueMapValue(name = "eventStartTimeAndDate")
  private Date eventStartTimeAndDate;

  @JsonIgnore
  @Getter
  @ValueMapValue(name = "eventEndTimeAndDate")
  private Date eventEndTimeAndDate;

  @JsonProperty("event_start_time")
  @Getter
  private String eventStartTime;

  @JsonProperty("formatted_event_start_time")
  @Getter
  private String formattedEventStartTime;

  @JsonProperty("event_end_time")
  @Getter
  private String eventEndTime;

  @JsonProperty("formatted_event_end_time")
  @Getter
  private String formattedEventEndTime;

  @Override
  public String getDocumentType() {
    return EVENT;
  }

  @PostConstruct
  protected void init() {
    super.init();
    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    Page page =
        Optional.ofNullable(pageManager).map(pm -> pm.getContainingPage(resource)).orElse(null);
    this.eventStartTime = KPMGUtilities.processDate(eventStartTimeAndDate);
    this.eventEndTime = KPMGUtilities.processDate(eventEndTimeAndDate);
    Locale locale = KPMGUtilities.getLocale(page);
    DateFormatConfig dateFormatConfig = KPMGUtilities.getDateFormatConfig(page);
    DateFormatsVO dateFormatsVO =
        Optional.ofNullable(dateFormatService)
            .map(df -> df.getDateFormatVo(dateFormatConfig))
            .orElse(null);
    if (null != dateFormatsVO) {
      this.formattedEventStartTime =
          Optional.ofNullable(dateFormatService)
              .map(df -> df.formatDate(eventStartTimeAndDate, dateFormatsVO, locale))
              .orElse(null);
      this.formattedEventEndTime =
          Optional.ofNullable(dateFormatService)
              .map(df -> df.formatDate(eventEndTimeAndDate, dateFormatsVO, locale))
              .orElse(null);
    }
  }
}
