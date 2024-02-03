package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class LockPageProcessTest {

  private LockPageProcess testClass;

  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolver resolver;
  private PageManager pageManager;

  private String payloadPath = "/content/kpmg/us/en/test";

  private Page page;

  @BeforeEach
  void setUp() {
    testClass = new LockPageProcess();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolver = mock(ResourceResolver.class);
    pageManager = mock(PageManager.class);
    page = mock(Page.class);
  }

  @Test
  void execute() throws WorkflowException {
    when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
    when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(workItem.getContentPath()).thenReturn(payloadPath);
    when(pageManager.getPage(anyString())).thenReturn(page);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testWCMException() throws WorkflowException, WCMException {
    when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
    when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    when(workItem.getContentPath()).thenReturn(payloadPath);
    when(pageManager.getPage(anyString())).thenReturn(page);
    doThrow(WCMException.class).when(page).lock();
    testClass.execute(workItem, workflowSession, metaData);
    assertThrows(
        WCMException.class,
        () -> {
          throw new WCMException("WCMException occured while locking the page");
        });
  }
}
