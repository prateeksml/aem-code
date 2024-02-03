package com.kpmg.core.workflow.process;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Revision;
import com.day.cq.wcm.api.WCMException;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import javax.jcr.*;
import javax.jcr.lock.LockManager;
import javax.jcr.version.Version;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.LoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CustomCreateVersionProcessTest {
  private CustomCreateVersionProcess testClass;
  private WorkItem workItem;
  private WorkflowSession workflowSession;
  private MetaDataMap metaData;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private JackrabbitSession session;
  private WorkflowData data;
  private Resource resource;
  private Page page;
  private PageManager pm;
  private Revision revision;
  private Collection<Revision> revisionsExisting;
  private Workflow workflow;
  private List<HistoryItem> historyList;
  private Asset asset;
  private com.day.cq.dam.api.Revision damRevision;
  private Node node;
  private Workspace workspace;
  private LockManager lockManager;
  private Property property;
  private Value[] values;
  private static String PATH = "/content/kpmg/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testClass = new CustomCreateVersionProcess();
    workItem = mock(WorkItem.class);
    workflowSession = mock(WorkflowSession.class);
    metaData = mock(MetaDataMap.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    session = mock(JackrabbitSession.class, withSettings().extraInterfaces(Session.class));
    data = mock(WorkflowData.class);
    resource = mock(Resource.class);
    page = mock(Page.class);
    pm = mock(PageManager.class);
    revisionsExisting = new ArrayList<>();
    revision = mock(Revision.class);
    workflow = mock(Workflow.class);
    historyList = new ArrayList<>();
    damRevision = mock(com.day.cq.dam.api.Revision.class);
    asset = mock(Asset.class);
    node = mock(Node.class);
    workspace = mock(Workspace.class);
    lockManager = mock(LockManager.class);
    property = mock(Property.class);
    values = new Value[1];
    addRevision();
    addHistoryItem();
    addValues();

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
  }

  // Test create version for page
  @Test
  void testCreateMinorVersionPage()
      throws WorkflowException, WCMException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflowData()).thenReturn(data);
    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_PATH);
    when(data.getPayload()).thenReturn(PATH);
    when(session.itemExists(PATH)).thenReturn(true);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.MINOR);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getPageManager()).thenReturn(pm);
    when(page.getPath()).thenReturn(PATH);
    when(pm.getRevisions(PATH, null)).thenReturn(revisionsExisting);
    when(pm.createRevision(any(), any(), any())).thenReturn(revision);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflowSession.getHistory(workflow)).thenReturn(historyList);
    when(data.getMetaDataMap()).thenReturn(metaData);

    testClass.execute(workItem, workflowSession, metaData);
  }

  // Test create version for Asset
  @Test
  void testCreateVersionAsset() throws Exception {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflowData()).thenReturn(data);
    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_PATH);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.MINOR);
    when(data.getPayload()).thenReturn(PATH);
    when(session.itemExists(PATH)).thenReturn(true);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(null);
    when(resource.adaptTo(Asset.class)).thenReturn(asset);
    when(asset.createRevision(any(), any())).thenReturn(damRevision);
    when(damRevision.getId()).thenReturn("1.1");
    when(session.getNodeByIdentifier("1.1")).thenReturn(node);
    when(node.getName()).thenReturn("revisedNode");
    when(data.getMetaDataMap()).thenReturn(metaData);
    when(metaData.get(WorkflowConstants.ABSOLUTE_TIME, String.class)).thenReturn("time");
    when(metaData.get(WorkflowConstants.ABSOLUTE_TIME, Long.class)).thenReturn(1683272458222L);
    when(workItem.getWorkflow()).thenReturn(workflow);
    when(workflowSession.getHistory(workflow)).thenReturn(historyList);
    testClass.execute(workItem, workflowSession, metaData);
  }

  // Test create version when both page and asset  are null
  @Test
  void testCreateVersion() throws WorkflowException, RepositoryException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflowData()).thenReturn(data);
    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_PATH);
    when(data.getPayload()).thenReturn(PATH);
    when(session.itemExists(anyString())).thenReturn(true);
    when(resolver.getResource(PATH)).thenReturn(resource);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testCreateMajorVersionPage()
      throws WorkflowException, RepositoryException, WCMException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflowData()).thenReturn(data);
    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_PATH);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.MAJOR);

    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_UUID);
    when(data.getPayload()).thenReturn(PATH);
    when(session.getNodeByIdentifier(anyString())).thenReturn(node);
    when(node.getPath()).thenReturn(PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getPageManager()).thenReturn(pm);
    when(page.getPath()).thenReturn(PATH);
    when(pm.getRevisions(PATH, null)).thenReturn(revisionsExisting);
    testClass.execute(workItem, workflowSession, metaData);
  }

  // Test create version when page is locked
  @Test
  void testCreateVersionLock()
      throws WorkflowException, RepositoryException, WCMException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflowData()).thenReturn(data);
    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_PATH);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.MINOR);
    when(data.getPayload()).thenReturn(PATH);
    when(session.itemExists(PATH)).thenReturn(true);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getPageManager()).thenReturn(pm);
    when(page.getPath()).thenReturn(PATH);
    when(pm.getRevisions(PATH, null)).thenThrow(WCMException.class);
    when(session.getWorkspace()).thenReturn(workspace);
    when(workspace.getLockManager()).thenReturn(lockManager);
    when(lockManager.isLocked(anyString())).thenReturn(true);
    when(session.getNode(anyString())).thenReturn(node);
    when(node.hasProperty(JcrConstants.JCR_MIXINTYPES)).thenReturn(true);
    when(node.getProperty(JcrConstants.JCR_MIXINTYPES)).thenReturn(property);
    when(property.getValues()).thenReturn(values);

    testClass.execute(workItem, workflowSession, metaData);
  }

  // Test majorVersion Creation when revision is null
  @Test
  void testMajorVersionCreation()
      throws WorkflowException, RepositoryException, WCMException, LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(workItem.getWorkflowData()).thenReturn(data);
    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_PATH);
    when(metaData.get(WorkflowConstants.PROCESS_ARGS, String.class))
        .thenReturn(WorkflowConstants.MAJOR);

    when(data.getPayloadType()).thenReturn(WorkflowConstants.JCR_UUID);
    when(data.getPayload()).thenReturn(PATH);
    when(session.getNodeByIdentifier(anyString())).thenReturn(node);
    when(node.getPath()).thenReturn(PATH);
    when(resolver.getResource(anyString())).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.getPageManager()).thenReturn(pm);
    when(page.getPath()).thenReturn(PATH);
    when(pm.getRevisions(PATH, null)).thenReturn(null);
    testClass.execute(workItem, workflowSession, metaData);
  }

  @Test
  void testLoginException() throws WorkflowException, LoginException {
    Mockito.when(resolverFactory.getServiceResourceResolver(Mockito.any()))
        .thenThrow(LoginException.class);
    testClass.execute(workItem, workflowSession, metaData);
    assertThrows(
        LoginException.class,
        () -> {
          throw new LoginException();
        });
  }

  private void addRevision() {
    revisionsExisting.add(
        new Revision() {
          @Override
          public Version getVersion() {
            return null;
          }

          @Override
          public ValueMap getProperties() {
            return null;
          }

          @Override
          public ValueMap getProperties(String relPath) {
            return null;
          }

          @Override
          public String getLabel() {
            return "1.0";
          }

          @Override
          public String getId() {
            return null;
          }

          @Override
          public boolean isDeleted() {
            return false;
          }

          @Override
          public boolean isBaseVersion() {
            return false;
          }

          @Override
          public String getName() {
            return null;
          }

          @Override
          public String getParentPath() {
            return null;
          }

          @Override
          public String getTitle() {
            return null;
          }

          @Override
          public String getDescription() {
            return null;
          }

          @Override
          public String getPageTitle() {
            return null;
          }

          @Override
          public String getNavigationTitle() {
            return null;
          }

          @Override
          public boolean isHideInNav() {
            return false;
          }

          @Override
          public boolean hasContent() {
            return false;
          }

          @Override
          public String getVanityUrl() {
            return null;
          }

          @Override
          public Calendar getCreated() {
            return null;
          }

          @Override
          public String getComment() {
            return null;
          }

          @Override
          public String getExistingPagePath() {
            return null;
          }
        });
  }

  private void addHistoryItem() {
    historyList.add(
        new HistoryItem() {
          @Override
          public String getComment() {
            return "comment from history";
          }

          @Override
          public String getAction() {
            return null;
          }

          @Override
          public Date getDate() {
            return null;
          }

          @Override
          public String getUserId() {
            return null;
          }

          @Override
          public HistoryItem getPreviousHistoryItem() {
            return null;
          }

          @Override
          public HistoryItem getNextHistryItem() {
            return null;
          }

          @Override
          public WorkItem getWorkItem() {
            return workItem;
          }
        });
  }

  private void addValues() {
    Value v =
        new Value() {
          @Override
          public String getString()
              throws ValueFormatException, IllegalStateException, RepositoryException {
            return JcrConstants.MIX_LOCKABLE;
          }

          @Override
          public InputStream getStream() throws RepositoryException {
            return null;
          }

          @Override
          public Binary getBinary() throws RepositoryException {
            return null;
          }

          @Override
          public long getLong() throws ValueFormatException, RepositoryException {
            return 0;
          }

          @Override
          public double getDouble() throws ValueFormatException, RepositoryException {
            return 0;
          }

          @Override
          public BigDecimal getDecimal() throws ValueFormatException, RepositoryException {
            return null;
          }

          @Override
          public Calendar getDate() throws ValueFormatException, RepositoryException {
            return null;
          }

          @Override
          public boolean getBoolean() throws ValueFormatException, RepositoryException {
            return false;
          }

          @Override
          public int getType() {
            return 0;
          }
        };
    values[0] = v;
  }
}
