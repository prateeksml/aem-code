package com.kpmg.core.models.impl;

import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;

import com.day.cq.wcm.api.Page;
import com.kpmg.core.models.HeroContentStaticImageModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {HeroContentStaticImageModel.class},
    resourceType = {HeroContentStaticImageModelImpl.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroContentStaticImageModelImpl extends AbstractKPMGComponentImpl
    implements HeroContentStaticImageModel {
  protected static final String RESOURCE_TYPE = "kpmg/components/content/hero-csi";

  @Getter @ValueMapValue private String heroDescription;
  @Getter @ValueMapValue private String heroTitle;
  @Getter @ValueMapValue private String showbreadcrumb;
  @Getter @ValueMapValue private String showarticledate;
  @Getter @ValueMapValue private String enablesocialshare;

  @Self Resource resource;

  @ScriptVariable Page currentPage;

  @SlingObject public ResourceResolver resourceResolver;

  String articlePublishedDate;

  @PostConstruct
  void postConstruct() throws PersistenceException {

    String templatePath = currentPage.getTemplate().getPath();
    if (StringUtils.isEmpty(heroTitle)) {
      heroTitle =
          Optional.ofNullable(currentPage)
              .map(Page::getProperties)
              .map(props -> props.get(JCR_TITLE, String.class))
              .orElse(StringUtils.EMPTY);
    }

    if (templatePath.equalsIgnoreCase("/conf/kpmg/settings/wcm/templates/page-article")) {
      DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
      articlePublishedDate =
          Optional.ofNullable(currentPage)
              .map(Page::getProperties)
              .map(props -> props.get("articleTimeAndDate", Date.class))
              .map(dateFormat::format)
              .orElse(StringUtils.EMPTY);
    }
  }

  @Override
  public String getArticlePublishedDate() {
    return articlePublishedDate;
  }
}
