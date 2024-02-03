package com.kpmg.core.models.impl;

import com.adobe.cq.export.json.ExporterConstants;
import com.kpmg.core.models.BrightcoveExperiencePlayer;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {BrightcoveExperiencePlayer.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = BrightcoveExperiencePlayerImpl.RESOURCE_TYPE)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class BrightcoveExperiencePlayerImpl implements BrightcoveExperiencePlayer {

  protected static final String RESOURCE_TYPE = "/kpmg/components/brightcove/brightcoveexperiences";
  static Pattern ID_PATTERN = Pattern.compile("\\[(.*?)\\]");

  @Inject String experience;

  String experienceId;

  @PostConstruct
  void postConstruct() {
    experienceId = getIdFromTitle(experience);
  }

  @Override
  public String getExperienceId() {
    return experienceId;
  }

  String getIdFromTitle(String title) {
    return Optional.ofNullable(title)
        .map(ID_PATTERN::matcher)
        .filter(Matcher::find)
        .map(m -> m.group(1))
        .orElse(null);
  }
}
