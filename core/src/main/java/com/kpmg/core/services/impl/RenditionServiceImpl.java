package com.kpmg.core.services.impl;

import static com.day.cq.dam.scene7.api.constants.Scene7Constants.PN_S7_FILE;

import com.adobe.granite.asset.api.Asset;
import com.kpmg.core.constants.GlobalConstants;
import com.kpmg.core.constants.RenditionSize;
import com.kpmg.core.constants.Viewport;
import com.kpmg.core.services.RenditionService;
import com.kpmg.core.services.RenditionServiceConfig;
import com.kpmg.core.services.RunmodeService;
import com.kpmg.core.utils.DMUtils;
import com.kpmg.core.utils.FactoryServiceUtility;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = RenditionService.class,
    configurationPolicy = ConfigurationPolicy.OPTIONAL,
    immediate = true)
@Designate(ocd = RenditionServiceConfig.class, factory = true)
public class RenditionServiceImpl implements RenditionService {

  private static final Logger LOG = LoggerFactory.getLogger(RenditionServiceImpl.class);

  private static final String RENDITIONS = "renditions";
  private static final String COMPONENT_NAME = "componentName";

  private final Map<String, RenditionSize> RENDITION_MAP = new HashMap<>();
  private static final Pattern PATTERN = Pattern.compile(GlobalConstants.SEMICOLON);

  @Reference private ResourceResolverFactory resourceResolverFactory;

  @Reference private ConfigurationAdmin configurationAdmin;

  @Reference private RunmodeService runmodeService;

  @Override
  public Map<String, String> getRenditions(
      final String componentName,
      final String imagePath,
      final String imagePlacement,
      final ResourceResolver resolver) {
    final Asset asset = DMUtils.getAsset(resolver, imagePath);
    if (asset == null) {
      LOG.debug("Asset is null at path : {}", imagePath);
      return Collections.emptyMap();
    }
    final Map<String, String> renditions =
        getDynamicRenditions(componentName, imagePath, imagePlacement, asset);
    return renditions.isEmpty()
        ? DMUtils.getStaticRenditions(componentName, imagePath)
        : renditions;
  }

  @Activate
  @Modified
  protected void activate(final RenditionServiceConfig renditionServiceConfig) {
    final Configuration[] configurations =
        FactoryServiceUtility.getConfigurations(this.configurationAdmin, this.getClass().getName());
    if (configurations != null) {
      Arrays.stream(configurations)
          .forEach(
              configuration -> {
                final String componentName =
                    (String) configuration.getProperties().get(COMPONENT_NAME);
                final String[] renditions =
                    (String[]) configuration.getProperties().get(RENDITIONS);
                if (StringUtils.isNotBlank(componentName) && renditions != null) {
                  addRenditions(componentName, renditions);
                }
              });
    }
  }

  private Map<String, String> getDynamicRenditions(
      final String componentName,
      final String imagePath,
      final String imagePlacement,
      final Asset asset) {
    final Map<String, String> renditions = new HashMap<>(Viewport.values().length);
    if (DMUtils.validateScene7Metadata(
            resourceResolverFactory, asset, this.runmodeService.isAuthor())
        && !DMUtils.isSvg(imagePath)) {
      final String scene7Domain =
          DMUtils.getImageServerDomain(
              resourceResolverFactory, asset, this.runmodeService.isAuthor());
      Arrays.stream(Viewport.values())
          .forEach(
              viewport -> {
                final RenditionSize renditionSize =
                    lookup(componentName, viewport.getLabel(), imagePlacement);
                if (renditionSize != null) {
                  final StringBuilder imageUrl = new StringBuilder();
                  imageUrl.append(scene7Domain);
                  imageUrl.append(GlobalConstants.SCENE7_BASE_URL);
                  final ValueMap valueMap = DMUtils.getMetadataProperties(asset);
                  final String filePath = valueMap.get(PN_S7_FILE, String.class);
                  final String[] fileNames = StringUtils.split(filePath, GlobalConstants.SLASH);
                  final String encodedFileName = encodeUrl(fileNames[1]);
                  imageUrl.append(
                      StringUtils.join(fileNames[0], GlobalConstants.SLASH, encodedFileName));
                  imageUrl.append(GlobalConstants.COLON + renditionSize.getSmartCropName());
                  imageUrl.append(
                      String.format(
                          GlobalConstants.IMAGE_PRESET_FORMAT,
                          renditionSize.getWidth(),
                          renditionSize.getHeight()));
                  renditions.put(viewport.getLabel(), imageUrl.toString());
                  LOG.debug(
                      "Rendition {} : ImagePath {} - ImageUrl {}", viewport, imagePath, imageUrl);
                }
              });
    }
    return renditions;
  }

  private void addRenditions(final String componentName, final String[] renditions) {
    Arrays.stream(renditions)
        .forEach(
            rendition -> {
              try {
                final String[] renditionProperties = PATTERN.split(StringUtils.trim(rendition));
                if (renditionProperties.length >= 5) {
                  final String viewport = StringUtils.defaultIfBlank(renditionProperties[0], null);
                  final String imagePlacement =
                      StringUtils.defaultIfBlank(renditionProperties[1], null);
                  final String smartCropName =
                      StringUtils.defaultIfBlank(renditionProperties[2], null);
                  final String width = StringUtils.defaultIfBlank(renditionProperties[3], null);
                  final String height = StringUtils.defaultIfBlank(renditionProperties[4], null);
                  RENDITION_MAP.put(
                      getKey(componentName, viewport, imagePlacement),
                      new RenditionSize(
                          smartCropName, Integer.parseInt(width), Integer.parseInt(height)));
                } else {
                  LOG.info("Rendition is skipped for {}", rendition);
                }
              } catch (final NumberFormatException e) {
                LOG.error(
                    "Error during adding the renditions in map caused by : [{}] : {}",
                    rendition,
                    e.getMessage());
              }
            });
  }

  private String getKey(
      final String componentName, final String viewport, final String imagePlacement) {
    return componentName + "\t" + viewport + "\t" + imagePlacement;
  }

  private RenditionSize lookup(
      final String componentName, final String viewport, final String imagePlacement) {
    return RENDITION_MAP.get(getKey(componentName, viewport, imagePlacement));
  }

  private String encodeUrl(final String url) {
    try {
      return URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
          .replace(GlobalConstants.PLUS, GlobalConstants.ENCODED_PLUS_CHARS);
    } catch (final UnsupportedEncodingException e) {
      LOG.error("Error while encoding URL", e);
    }
    return url;
  }
}
