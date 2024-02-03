package com.kpmg.core.utils;

import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.day.cq.dam.api.DamConstants.ACTIVITY_TYPE_METADATA;
import static com.day.cq.dam.scene7.api.S7ConfigResolver.PREVIEW_SERVER;
import static com.day.cq.dam.scene7.api.S7ConfigResolver.PUBLISH_SERVER;
import static com.day.cq.dam.scene7.api.S7ConfigResolver.ROOT_PATH;
import static com.day.cq.dam.scene7.api.constants.Scene7Constants.PN_S7_DOMAIN;
import static com.day.cq.dam.scene7.api.constants.Scene7Constants.PN_S7_FILE;

import com.adobe.granite.asset.api.Asset;
import com.drew.lang.annotations.NotNull;
import com.kpmg.core.constants.GlobalConstants;
import com.kpmg.core.constants.KPMGConstants;
import com.kpmg.core.constants.Viewport;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMUtils {

  private DMUtils() {}

  private static final Logger LOG = LoggerFactory.getLogger(DMUtils.class);

  @NotNull
  public static ValueMap getMetadataProperties(final Resource resource) {
    if (resource == null) {
      return new ValueMapDecorator(new HashMap<>());
    }
    final Resource assetMetadataResource =
        resource.getChild(JCR_CONTENT + KPMGConstants.SLASH + ACTIVITY_TYPE_METADATA);
    return ResourceUtil.getValueMap(assetMetadataResource);
  }

  public static String getImageServerDomain(
      final ResourceResolverFactory resourceResolverFactory,
      final Asset asset,
      final boolean isAuthor) {
    if (isAuthor) {
      return StringUtils.join(
          getDynamicMediaPreviewUrl(resourceResolverFactory), GlobalConstants.SLASH);
    } else {
      final ValueMap valueMap = getMetadataProperties(asset);
      return valueMap.containsKey(PN_S7_DOMAIN)
          ? valueMap.get(PN_S7_DOMAIN, String.class)
          : StringUtils.EMPTY;
    }
  }

  public static Map<String, String> getStaticRenditions(
      final String componentName, final String imagePath) {
    final Map<String, String> renditions = new HashMap<>(Viewport.values().length);
    LOG.debug("Retrieving static renditions for {}", componentName);
    for (final Viewport viewport : Viewport.values()) {
      renditions.put(viewport.getLabel(), Text.escapePath(imagePath));
      LOG.debug("Rendition {} : {}", viewport, imagePath);
    }

    return renditions;
  }

  public static boolean isSvg(final String imagePath) {

    return StringUtils.endsWithIgnoreCase(imagePath, GlobalConstants.SVG_EXTENSION);
  }

  public static boolean validateScene7Metadata(
      final ResourceResolverFactory resourceResolverFactory,
      final Asset asset,
      final boolean isAuthor) {
    final ValueMap valueMap = getMetadataProperties(asset);
    return StringUtils.isNotBlank(valueMap.get(PN_S7_DOMAIN, String.class))
        && StringUtils.isNotBlank(valueMap.get(PN_S7_FILE, String.class))
        && validatePreviewServerUrl(resourceResolverFactory, isAuthor);
  }

  public static boolean validatePreviewServerUrl(
      final ResourceResolverFactory resourceResolverFactory, final boolean isAuthor) {
    return !isAuthor || StringUtils.isNotBlank(getDynamicMediaPreviewUrl(resourceResolverFactory));
  }

  public static Asset getAsset(final ResourceResolver resolver, final String imagePath) {
    Asset asset = null;
    if (StringUtils.isNotBlank(imagePath)) {
      final Resource imageRes = resolver.getResource(imagePath);
      if (imageRes != null) {
        asset = imageRes.adaptTo(Asset.class);
      }
    }
    return asset;
  }

  public static String getDynamicMediaPreviewUrl(
      final ResourceResolverFactory resourceResolverFactory) {
    String dynamicMediaPreviewUrl = StringUtils.EMPTY;
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver(
            "userpermissions", resourceResolverFactory)) {
      final Resource resource = resourceResolver.getResource(GlobalConstants.DM_CONFIG_PATH);
      if (null != resource) {
        dynamicMediaPreviewUrl = resource.getValueMap().get(PREVIEW_SERVER, String.class);
      }
    } catch (final LoginException e) {
      LOG.error(
          "Error while fetching resourceResolver in getDynamicMediaPreviewUrl: {}", e.getMessage());
    }
    return dynamicMediaPreviewUrl;
  }

  public static String getDynamicMediaPublishUrl(
      final ResourceResolverFactory resourceResolverFactory) {
    String dynamicMediaPreviewUrl = StringUtils.EMPTY;
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver(
            "userpermissions", resourceResolverFactory)) {
      final Resource resource = resourceResolver.getResource(GlobalConstants.DM_CONFIG_PATH);
      if (null != resource) {
        dynamicMediaPreviewUrl = resource.getValueMap().get(PUBLISH_SERVER, String.class);
      }
    } catch (final LoginException e) {
      LOG.error(
          "Error while fetching resourceResolver in getDynamicMediaPreviewUrl: {}", e.getMessage());
    }
    return dynamicMediaPreviewUrl;
  }

  public static String getDynamicMediaRootPath(
      final ResourceResolverFactory resourceResolverFactory) {
    String dynamicMediaRootPath = StringUtils.EMPTY;
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver(
            "userpermissions", resourceResolverFactory)) {
      final Resource resource = resourceResolver.getResource(GlobalConstants.DM_CONFIG_PATH);
      if (null != resource) {
        dynamicMediaRootPath = resource.getValueMap().get(ROOT_PATH, String.class);
      }
    } catch (final LoginException e) {
      LOG.error(
          "Error while fetching resourceResolver in getDynamicMediaRootPath: {}", e.getMessage());
    }
    return dynamicMediaRootPath;
  }

  public static String appendSlash(final String dynamicMediaUrl) {
    return StringUtils.isNotEmpty(dynamicMediaUrl)
        ? dynamicMediaUrl.concat(GlobalConstants.SLASH)
        : dynamicMediaUrl;
  }
}
