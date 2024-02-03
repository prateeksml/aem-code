package com.kpmg.integration.listeners;

import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.day.cq.wcm.api.constants.NameConstants.NN_TEMPLATE;
import static com.kpmg.integration.constants.Constants.*;
import static org.apache.sling.jcr.resource.api.JcrResourceConstants.NT_SLING_FOLDER;

import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;
import com.kpmg.integration.util.KPMGUtilities;
import java.security.Principal;
import java.util.Iterator;
import javax.jcr.*;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.principal.PrincipalManager;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = EventHandler.class,
    immediate = true,
    property = {
      EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC,
      EventConstants.EVENT_FILTER
          + "(&"
          + "resourceType=wcm-io/caconfig/editor/components/page/editor)"
    })
public class CAConfigPageCreationEventHandler implements EventHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CAConfigPageCreationEventHandler.class);

  @Reference ResourceResolverFactory resourceResolverFactory;

  @Override
  public void handleEvent(Event event) {
    LOG.debug("Context-Aware Configuration Template page event initiated.");
    Iterator<PageModification> pageInfo = PageEvent.fromEvent(event).getModifications();
    while (pageInfo.hasNext()) {
      PageModification pageModification = pageInfo.next();
      String pagePath = pageModification.getPath();
      if (pageModification.getType().equals(PageModification.ModificationType.CREATED)
          || pageModification.getType().equals(PageModification.ModificationType.MOVED)
          || pageModification.getType().equals(PageModification.ModificationType.ROLLEDOUT)) {
        LOG.info("New page has been created or moved or rolled out under {}", pagePath);
        LOG.debug("Checking if Context-Aware configuration nodes needs to be setup");
        if (pagePath.startsWith(SITE_ROOT)) {
          try {
            ResourceResolver resourceResolver =
                KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory);
            Resource currentResource = resourceResolver.getResource(pagePath);
            if (currentResource != null) {
              Node currentNode = currentResource.adaptTo(Node.class);
              if (currentNode != null) {
                Node currentNodeContent = currentNode.getNode(JCR_CONTENT);
                if (currentNodeContent != null && currentNodeContent.hasProperty(NN_TEMPLATE)) {
                  String templateName = currentNodeContent.getProperty(NN_TEMPLATE).getString();
                  if (WCM_IO_TEMPLATE_PATH.equalsIgnoreCase(templateName)) {
                    LOG.debug(
                        "The page template is of type WCMIO CA Config, hence validating context aware configurations");
                    autoConfigureSiteSettings(resourceResolver, currentResource);
                  }
                }
              }
            }
          } catch (LoginException | RepositoryException e) {
            LOG.error("An error occurred while creating CA Configs", e);
          }
        }
      }
    }
  }

  private void autoConfigureSiteSettings(
      ResourceResolver resourceResolver, Resource currentResource) {
    try {
      Page currentPage = currentResource.adaptTo(Page.class);
      if (currentPage != null && currentPage.getAbsoluteParent(4) != null) {
        Page countryPage = currentPage.getAbsoluteParent(2);
        Page languagePage = currentPage.getAbsoluteParent(3);
        if (countryPage != null && languagePage != null) {
          Resource contentResource = languagePage.getContentResource();
          Node languageNode = contentResource.adaptTo(Node.class);
          if (languageNode != null) {
            String configRefPath =
                buildContextAwareConfigRefForSite(
                    countryPage.getName(), languagePage.getName(), resourceResolver);
            LOG.debug(
                "CA Config Reference Path for country {} and language {} is {}",
                countryPage.getName(),
                languagePage.getName(),
                configRefPath);
            if (!configRefPath.isBlank()) {
              setConfigRefAtSiteLevel(configRefPath, languageNode);
              resourceResolver.commit();
              LOG.debug(
                  "Context-Aware configurations saved for content path: {}",
                  languageNode.getPath());
            } else {
              LOG.error("Context-Aware configurations is NULL and not saved");
            }
          }
        }
      }
    } catch (RepositoryException | PersistenceException e) {
      LOG.error(
          "Can't create context aware configurations for page {}", currentResource.getPath(), e);
    }
  }

  private void setConfigRefAtSiteLevel(String configRefPath, Node languageNode)
      throws RepositoryException {
    if (configRefPath != null) {
      languageNode.setProperty(CONTEXT_AWARE_CONFIG_NODE_NAME, configRefPath);
      LOG.debug("Context Aware Config Reference Path set at language path: {}", configRefPath);
    }
  }

  private String buildContextAwareConfigRefForSite(
      String countryCode, String languageCode, ResourceResolver resourceResolver)
      throws RepositoryException {
    Resource caConfigResource =
        resourceResolver.getResource(CONTEXT_AWARE_CONFIG_NODE_ROOT_LOCATION);
    if (caConfigResource != null) {
      Node caConfigNode = caConfigResource.adaptTo(Node.class);
      if (caConfigNode != null && caConfigNode.hasNode(countryCode)) {
        Node caConfigCountryNode = caConfigNode.getNode(countryCode);
        if (caConfigCountryNode.hasNode(languageCode)) {
          Node caConfigLanguageNode = caConfigCountryNode.getNode(languageCode);
          LOG.debug(
              "Context-Aware configuration node exists for country: {} and language: {} at path {}",
              countryCode,
              languageCode,
              caConfigLanguageNode.getPath());
          return caConfigLanguageNode.getPath();
        } else {
          Node caConfigLanguageNode = caConfigCountryNode.addNode(languageCode, NT_SLING_FOLDER);
          applyAcl(resourceResolver, caConfigLanguageNode.getPath(), languageCode);
          LOG.debug(
              "Context-Aware configuration added successfully for country: {} and language: {} at path {}",
              countryCode,
              languageCode,
              caConfigLanguageNode.getPath());
          return caConfigLanguageNode.getPath();
        }
      } else {
        LOG.debug(
            "Context-Aware configurations doesn't exists for country: {} and language: {}. Hence creating new ones.",
            countryCode,
            languageCode);

        Node caConfigCountryNode = caConfigNode.addNode(countryCode, NT_SLING_FOLDER);
        Node caConfigLanguageNode = caConfigCountryNode.addNode(languageCode, NT_SLING_FOLDER);
        LOG.debug(
            "Context-Aware configuration added successfully for country: {} and language: {} at path {}",
            countryCode,
            languageCode,
            caConfigLanguageNode.getPath());
        return caConfigLanguageNode.getPath();
      }
    }
    return StringUtils.EMPTY;
  }

  private void applyAcl(ResourceResolver resourceResolver, String nodePath, String languageCode)
      throws RepositoryException {
    Session session = resourceResolver.adaptTo(Session.class);
    AccessControlManager accessControlManager = session.getAccessControlManager();
    JackrabbitAccessControlList specificPathGroupAcl =
        AccessControlUtils.getAccessControlList(session, nodePath);
    if (null != session && specificPathGroupAcl != null) {
      JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
      PrincipalManager principalManager = jackrabbitSession.getPrincipalManager();
      Principal localPrincipal =
          principalManager.getPrincipal("aem-" + languageCode + "-master-contributor");
      Principal everyone = principalManager.getPrincipal("everyone");
      Privilege[] localPrivileges =
          AccessControlUtils.privilegesFromNames(
              session,
              Privilege.JCR_WRITE,
              Privilege.JCR_VERSION_MANAGEMENT,
              Privilege.JCR_LOCK_MANAGEMENT,
              Replicator.REPLICATE_PRIVILEGE);

      Privilege[] readPrivileges =
          AccessControlUtils.privilegesFromNames(session, Privilege.JCR_READ);
      specificPathGroupAcl.addEntry(localPrincipal, localPrivileges, true);
      specificPathGroupAcl.addEntry(everyone, readPrivileges, true);
      accessControlManager.setPolicy(specificPathGroupAcl.getPath(), specificPathGroupAcl);
      session.save();
    }
  }
}
