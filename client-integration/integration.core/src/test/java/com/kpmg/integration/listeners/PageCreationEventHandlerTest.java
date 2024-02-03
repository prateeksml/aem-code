package com.kpmg.integration.listeners;

import static org.mockito.Mockito.*;

import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;
import com.kpmg.integration.services.ElasticClientService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventProperties;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class PageCreationEventHandlerTest {

  private static final String INDEXING_ROOT = "/content/myapp";
  private static final String PAGE_PATH = "/content/myapp/homepage";
  private static final String JCR_CONTENT = "jcr:content";

  @Mock private ElasticClientService elasticClientService;

  @Mock private ModifiableValueMap modifiableValueMap;

  @Mock Event event;

  @Mock PageEvent pageEvent;

  @Mock PageModification pageModification;

  @Mock Iterator<PageModification> pageInfo;

  @Mock Iterator<Map<String, Object>> mapIterator;

  @InjectMocks private PageCreationEventHandler pageCreationEventHandler;

  @Mock List<Map<String, Object>> modProps;

  @Mock Map<String, Object> map;

  @Mock Map<String, Object> props;

  @Mock EventProperties eventProperties;

  @Mock PageModification.ModificationType modificationType;

  @Mock ResourceResolverFactory resourceResolverFactory;

  @Mock ResourceResolver resourceResolver;

  private Logger logger;

  @Test
  void testHandleEvent() {

    when(event.getTopic()).thenReturn(PageEvent.EVENT_TOPIC);
    when(PageEvent.fromEvent(event)).thenReturn(pageEvent);

    lenient().when(pageInfo.hasNext()).thenReturn(true, false);
    lenient().when(pageInfo.next()).thenReturn(pageModification);
    pageCreationEventHandler.handleEvent(event);
  }

  @Test
  void addDocumentTest()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
          LoginException {
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
    Method method =
        PageCreationEventHandler.class.getDeclaredMethod("addDocumentIdToPage", String.class);
    method.setAccessible(true);
    method.invoke(pageCreationEventHandler, "/content/myapp/homepage");
  }
}
