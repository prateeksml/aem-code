package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class UnlockPageProcessTest {

  private UnlockPageProcess testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap args;
  private ResourceResolver wfResolver;
  private Session session;
  private PageManager pageMgr;
  private Page page;
  private Resource resource;
  private Locale pageLocale;
  private ResourceBundleProvider resourceBundleProvider;
  private ResourceBundle resourceBundle;
  private Node node;
  private NodeType mixin;
  private NodeType[] nodeTypes;
  private WorkflowData workflowData;
  private static final String TEST_PAGE_PATH = "/content/kpmgpublic/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testClass = new UnlockPageProcess();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    args = mock(MetaDataMap.class);
    wfResolver = mock(ResourceResolver.class);
    session = mock(Session.class);
    pageMgr = mock(PageManager.class);
    page = mock(Page.class);
    resource = mock(Resource.class);
    pageLocale = new Locale("en", "us");
    resourceBundleProvider = mock(ResourceBundleProvider.class);
    resourceBundle =
        new ResourceBundle() {
          @Override
          protected Object handleGetObject(@NotNull String key) {
            return "test-value";
          }

          @NotNull
          @Override
          public Enumeration<String> getKeys() {
            return null;
          }
        };
    node = mock(Node.class);
    mixin = mock(NodeType.class);
    nodeTypes = new NodeType[1];
    nodeTypes[0] = mixin;
    workflowData = mock(WorkflowData.class);

    PrivateAccessor.setField(testClass, "resourceBundleProvider", resourceBundleProvider);
  }

  @Test
  void execute() throws WorkflowException, RepositoryException {
    when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(wfResolver);
    when(workflowSession.adaptTo(Session.class)).thenReturn(session);
    when(wfResolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
    when(workItem.getContentPath()).thenReturn(TEST_PAGE_PATH);
    when(pageMgr.getPage(TEST_PAGE_PATH)).thenReturn(page);
    when(page.getContentResource()).thenReturn(resource);
    when(resource.getPath()).thenReturn(TEST_PAGE_PATH);
    when(page.getLanguage(false)).thenReturn(pageLocale);
    when(resourceBundleProvider.getResourceBundle(pageLocale)).thenReturn(resourceBundle);
    when(page.isLocked()).thenReturn(true);
    when(session.getNode(TEST_PAGE_PATH)).thenReturn(node);
    when(node.getMixinNodeTypes()).thenReturn(nodeTypes);
    when(mixin.getName()).thenReturn(JcrConstants.MIX_LOCKABLE);
    when(workItem.getWorkflowData()).thenReturn(workflowData);
    when(args.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn("stepback-notification");
    when(workflowData.getMetaDataMap()).thenReturn(args);
    testClass.execute(workItem, workflowSession, args);
  }
}
