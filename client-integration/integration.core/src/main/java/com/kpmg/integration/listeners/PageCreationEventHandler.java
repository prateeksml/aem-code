package com.kpmg.integration.listeners;

import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.kpmg.integration.constants.Constants.SITE_ROOT;

import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;
import com.kpmg.integration.util.KPMGUtilities;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import org.apache.sling.api.resource.*;
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
    property = {EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC})
public class PageCreationEventHandler implements EventHandler {

  @Reference ResourceResolverFactory resourceResolverFactory;

  private static final Logger LOG = LoggerFactory.getLogger(PageCreationEventHandler.class);

  @Override
  public void handleEvent(Event event) {
    Iterator<PageModification> pageInfo = PageEvent.fromEvent(event).getModifications();
    while (pageInfo.hasNext()) {
      PageModification pageModification = pageInfo.next();
      String pagePath = pageModification.getPath();
      if (pageModification.getType().equals(PageModification.ModificationType.CREATED)
          && pagePath.startsWith(SITE_ROOT)) {
        addDocumentIdToPage(pagePath);
      }
    }
  }

  private void addDocumentIdToPage(String pagePath) {
    try (ResourceResolver resolver =
        KPMGUtilities.getResourceResolverFromPool(resourceResolverFactory)) {
      Optional.of(resolver)
          .map(r -> resolver.getResource(pagePath + "/" + JCR_CONTENT))
          .map(res -> res.adaptTo(ModifiableValueMap.class))
          .map(m -> m.put("documentId", UUID.randomUUID().toString()));
      resolver.commit();
      LOG.debug("Create document id for page {}", pagePath);
    } catch (PersistenceException | LoginException e) {
      LOG.error("Can't save document id on page {}", pagePath);
    }
  }
}
