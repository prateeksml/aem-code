package com.kpmg.core.models.impl;

import static com.day.cq.commons.jcr.JcrConstants.JCR_DESCRIPTION;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;

import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.core.constants.GlobalConstants;
import com.kpmg.core.models.EventCard;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = EventCard.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EventCardImpl extends AbstractKPMGComponentImpl implements EventCard {

  @ValueMapValue private String linkURL;
  @ValueMapValue private Boolean overrideTitle;
  @ValueMapValue private Boolean overrideDescription;
  @SlingObject ResourceResolver resourceResolver;
  @Self protected LinkManager linkManager;
  @Self protected SlingHttpServletRequest request;
  @ValueMapValue @Getter private String eventTitle;
  @ValueMapValue @Getter private String eventDescription;
  @Getter private Calendar eventStartTimeAndDate;
  @Getter private Calendar eventEndTimeAndDate;
  @Getter private String eventTimeZone;
  @Getter private String pageURL;
  @Getter private String imageURL;

  @PostConstruct
  protected void init() {
    if (StringUtils.isBlank(linkURL)) {
      return;
    }
    final PageManager pm = resourceResolver.adaptTo(PageManager.class);
    final Page page = pm.getPage(linkURL);
    if (page != null) {
      final Resource imageResource =
          resourceResolver.getResource(linkURL + GlobalConstants.FEATURED_IMAGE_PATH);
      pageURL = linkManager.get(page).build().getURL();
      final ValueMap pageProperties = page.getProperties();
      imageURL = imageResource.getValueMap().get(GlobalConstants.PN_FILE_REFERENCE, String.class);
      eventTitle = getValueOrOverride(pageProperties, JCR_TITLE, overrideTitle, eventTitle);
      eventDescription =
          getValueOrOverride(
              pageProperties, JCR_DESCRIPTION, overrideDescription, eventDescription);
      eventStartTimeAndDate =
          pageProperties.get(GlobalConstants.PN_EVENT_START_TIME, Calendar.class);
      eventEndTimeAndDate = pageProperties.get(GlobalConstants.PN_EVENT_END_TIME, Calendar.class);
      eventTimeZone = pageProperties.get(GlobalConstants.PN_EVENT_TIMEZONE, String.class);
    }
  }

  private String getValueOrOverride(
      ValueMap valueMap, String property, boolean override, String defaultValue) {
    return override ? defaultValue : valueMap.get(property, String.class);
  }
}
