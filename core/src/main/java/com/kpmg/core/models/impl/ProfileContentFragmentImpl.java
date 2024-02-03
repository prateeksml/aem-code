package com.kpmg.core.models.impl;

import static com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragment.PN_PATH;
import static com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragment.PN_VARIATION_NAME;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentVariation;
import com.adobe.cq.dam.cfm.FragmentData;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpmg.core.models.ProfileContentFragment;
import com.kpmg.core.models.SocialLink;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {ComponentExporter.class, ProfileContentFragment.class},
    resourceType = ProfileContentFragmentImpl.RESOURCE_TYPE)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProfileContentFragmentImpl extends AbstractKPMGComponentImpl
    implements ProfileContentFragment {

  public static final String RESOURCE_TYPE = "kpmg/components/content/profile";
  public static final String MASTER_VARIATION = "master";

  @SlingObject ResourceResolver resourceResolver;

  Optional<ContentFragment> contentFragment;

  @ValueMapValue(name = PN_PATH, injectionStrategy = InjectionStrategy.OPTIONAL)
  String fragmentPath;

  @ValueMapValue(name = PN_VARIATION_NAME, injectionStrategy = InjectionStrategy.OPTIONAL)
  String variationName;

  @ScriptVariable private Page currentPage;

  private static final Logger LOG = LoggerFactory.getLogger(ProfileContentFragmentImpl.class);

  @PostConstruct
  void init() {
    if (StringUtils.isBlank(fragmentPath)) {
      ValueMap pageProperties = currentPage.getProperties();
      fragmentPath = pageProperties.get("fragmentPath", String.class);
    }

    contentFragment =
        Optional.ofNullable(fragmentPath)
            .map(resourceResolver::getResource)
            .map(r -> r.adaptTo(ContentFragment.class));
    if (StringUtils.isBlank(variationName)) {
      variationName = MASTER_VARIATION;
    }
  }

  private @Nullable Object safeGet(String name) {
    if (MASTER_VARIATION.equals(variationName)) {
      return contentFragment
          .map(cf -> cf.getElement(name))
          .map(ContentElement::getValue)
          .map(FragmentData::getValue)
          .orElse(StringUtils.EMPTY);
    } else {
      return contentFragment
          .map(cf -> cf.getElement(name))
          .map(ce -> ce.getVariation(variationName))
          .map(ContentVariation::getValue)
          .map(FragmentData::getValue)
          .orElse(StringUtils.EMPTY);
    }
  }

  @Override
  public String getImage() {
    return (String) safeGet("image");
  }

  @Override
  public String getImageDescription() {
    return (String) safeGet("imageDescription");
  }

  @Override
  public String getImageAltText() {
    return (String) safeGet("imageAltText");
  }

  @Override
  public String getContactType() {
    return (String) safeGet("contactType");
  }

  @Override
  public String getMemberFirm() {
    return (String) safeGet("memberFirm");
  }

  @Override
  public String getBusinessOwnerGroup() {
    return (String) safeGet("businessOwnerGroup");
  }

  @Override
  public String getSalutation() {
    return (String) safeGet("salutation");
  }

  @Override
  public String getFirstName() {
    return (String) safeGet("firstName");
  }

  @Override
  public String getMiddleInitial() {
    return (String) safeGet("middleInitial");
  }

  @Override
  public String getLastName() {
    return (String) safeGet("lastName");
  }

  @Override
  public String getSuffix() {
    return (String) safeGet("suffix");
  }

  @Override
  public String getJobTitle() {
    return (String) safeGet("jobTitle");
  }

  @Override
  public String getEmail() {
    return (String) safeGet("email");
  }

  @Override
  public String getPhoneNumber() {
    return (String) safeGet("phoneNumber");
  }

  @Override
  public String getCity() {
    return (String) safeGet("city");
  }

  @Override
  public String getCountry() {
    return (String) safeGet("country");
  }

  @Override
  public String getBiography() {
    return (String) safeGet("biography");
  }

  public String getTitle() {
    return StringUtils.joinWith(
        StringUtils.SPACE,
        getSalutation(),
        getFirstName(),
        getMiddleInitial(),
        getLastName(),
        getSuffix());
  }

  public ComponentData getPhoneData() {
    return DataLayerBuilder.forComponent()
        .withId(
            () ->
                ComponentUtils.generateId(
                    getId() + ComponentUtils.ID_SEPARATOR + "phoneLink", getPhoneNumber()))
        .withLinkUrl(this::getPhoneNumber)
        .withParentId(this::getId)
        .build();
  }

  public ComponentData getEmailData() {
    return DataLayerBuilder.forComponent()
        .withId(
            () ->
                ComponentUtils.generateId(
                    getId() + ComponentUtils.ID_SEPARATOR + "emailLink", getEmail()))
        .withLinkUrl(this::getEmail)
        .withParentId(this::getId)
        .build();
  }

  @Override
  public List<SocialLink> getSocialLinks() {
    return Arrays.stream((String[]) safeGet("socialLinks"))
        .map(link -> new SocialLinkImpl(link, getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Object getDatalayerExtension() {
    return new ProfileContentFragmentDataLayer(
        fragmentPath, getTitle(), getJobTitle(), getMemberFirm());
  }

  @Getter
  @AllArgsConstructor
  static class SocialLinkImpl implements SocialLink {
    private final String link;
    private final String parentId;

    @JsonIgnore
    public ComponentData getData() {
      return DataLayerBuilder.forComponent()
          .withId(
              () ->
                  ComponentUtils.generateId(
                      parentId + ComponentUtils.ID_SEPARATOR + "sociallink", link))
          .withLinkUrl(this::getLink)
          .withParentId(() -> parentId)
          .build();
    }

    public String getHostName() {
      try {
        String hostname = new URL(link).toURI().getHost();
        return (hostname.split("\\.").length == 2)
            ? hostname.split("\\.")[0]
            : hostname.split("\\.")[1];
      } catch (MalformedURLException | URISyntaxException e) {
        LOG.error("Malformed social link authored in social CF @ {}", link);
      }
      return StringUtils.EMPTY;
    }
  }

  @Getter
  @AllArgsConstructor
  static class ProfileContentFragmentDataLayer {
    private String fragmentPath;
    private String title;
    private String jobTitle;
    private String memberFirm;
  }
}
