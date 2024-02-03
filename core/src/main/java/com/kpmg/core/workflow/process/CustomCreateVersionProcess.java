package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Revision;
import com.day.cq.wcm.api.WCMException;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.lock.LockManager;
import javax.jcr.version.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class is used to create versions based on arguments provided in the process */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Custom Create Version Process"})
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class CustomCreateVersionProcess implements WorkflowProcess {
  private static final Logger LOG = LoggerFactory.getLogger(CustomCreateVersionProcess.class);
  public static final String TYPE_JCR_PATH = "JCR_PATH";
  public static final String TYPE_JCR_UUID = "JCR_UUID";

  @Reference private ResourceResolverFactory resolverFactory;

  /**
   * This method is used to create versions based on arguments if no argument is supplied default
   * version is created.
   *
   * @param workItem
   * @param workflowSession
   * @param metaDataMap
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession workflowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final Session session = resourceResolver.adaptTo(Session.class);
      final WorkflowData data = workItem.getWorkflowData();
      String versionType = StringUtils.EMPTY;
      if (null != metaDataMap.get(WorkflowConstants.PROCESS_ARGS, String.class)) {
        versionType = metaDataMap.get(WorkflowConstants.PROCESS_ARGS, String.class);
      }
      final String path = getPath(data, session);
      if (StringUtils.isNotBlank(path)) {
        final Resource resource = resourceResolver.getResource(path);
        String revStr = null;
        if (resource.adaptTo(Page.class) != null) {
          final Page page = resource.adaptTo(Page.class);
          createVersions(page, versionType, workItem, session);
        } else if (resource.adaptTo(Asset.class) != null) {
          final Asset asset = resource.adaptTo(Asset.class);
          final String versionLabel = createUniqueVersionLabel(asset.getRevisions(null), null);
          final com.day.cq.dam.api.Revision rev = asset.createRevision(versionLabel, null);
          revStr = getAssetRevision(rev, session);
        } else {
          LOG.error("Cannot create version of {} ", path);
        }
        if (StringUtils.isNotBlank(revStr)) {
          data.getMetaDataMap().put("resourceVersion", revStr);
          if (workItem.getWorkflowData().getMetaDataMap().get("absoluteTime", String.class)
              != null) {
            final Calendar cal = getTime(workItem);
            data.getMetaDataMap()
                .put("comment", "Activate version " + revStr + " on " + cal.getTime().toString());
          }
          workflowSession.updateWorkflowData(workItem.getWorkflow(), data);
        }
        final List<HistoryItem> historyList = workflowSession.getHistory(workItem.getWorkflow());
        final int listSize = historyList.size();
        final HistoryItem lastItem = historyList.get(listSize - 1);
        final String comment = lastItem.getComment();
        if (comment != null && comment.length() > 0) {
          lastItem.getWorkItem().getWorkflowData().getMetaDataMap().put("comment", comment);
          LOG.debug("Previous Workflow Comment is {}", comment);
        } else {
          LOG.debug("Previous Workflow Comment = null or ''");
        }
      }
    } catch (Exception e) {
      LOG.error("A Exception has occured while running CustomCreateVersionProcess workflow ", e);
    }
  }

  /**
   * @param property
   * @return
   * @throws RepositoryException
   */
  private boolean hasMixLockable(final javax.jcr.Property property) throws RepositoryException {
    boolean isPresent = false;
    final Value[] values = property.getValues();
    for (Value value : values) {
      final String lockable = value.getString();
      if (JcrConstants.MIX_LOCKABLE.equals(lockable)) {
        isPresent = true;
        break;
      }
    }
    return isPresent;
  }

  /**
   * @param page
   * @param pm
   * @param versionType
   * @param lockManager
   * @param workItem
   * @param session
   */
  private void createVersions(
      final Page page, final String versionType, final WorkItem workItem, final Session session) {
    final String pagePath =
        workItem.getWorkflowData().getPayload()
            + WorkflowConstants.SLASH
            + JcrConstants.JCR_CONTENT;
    try {
      if (versionType.equals(WorkflowConstants.MINOR)) {
        createMinorVersion(page, true);
      } else if (StringUtils.equalsIgnoreCase(versionType, WorkflowConstants.MAJOR)) {
        createMajorVersion(page);
      }
    } catch (WCMException e) {

      /*
       * Handling a specific case where WCMException is thrown while calling
       * createRevisions(). This may happen if a page is locked by some external user
       * or somehow by the system. In such case, if a page is locked we will simply
       * unlock the page.
       */

      try {
        final Workspace workspace = session.getWorkspace();
        final LockManager lockManager = workspace.getLockManager();
        if (lockManager.isLocked(pagePath)) {
          lockManager.unlock(pagePath);
          session.refresh(true);
          session.save();
          final Node pageNode = session.getNode(pagePath);
          if (pageNode.hasProperty(JcrConstants.JCR_MIXINTYPES)) {
            final javax.jcr.Property property = pageNode.getProperty(JcrConstants.JCR_MIXINTYPES);
            if (hasMixLockable(property)) {
              pageNode.removeMixin(JcrConstants.MIX_LOCKABLE);
              session.refresh(true);
              session.save();
            }
          }
        }
      } catch (RepositoryException e1) {
        LOG.error(
            "Exception occured while unlocking page / removing mixin types : {}",
            workItem.getWorkflowData().getPayload());
      }
    }
  }

  /**
   * Creates a major version for the page
   *
   * @param pm
   * @param page
   * @throws WCMException
   */
  private void createMajorVersion(final Page page) {
    Revision rev = null;
    try {
      final PageManager pm = page.getPageManager();
      rev = getLatestRevision(page);
      if (null != rev) {
        createMajorRevison(rev, page);
      } else {
        final String versionLabel =
            createUniqueVersionLabel(pm.getRevisions(page.getPath(), null), null);
        pm.createRevision(page, versionLabel, null);
      }
    } catch (WCMException e) {
      LOG.error("Exception while creating Major version for page : {} ", page.getPath());
    }
  }

  /**
   * Creates a minor version for the page
   *
   * @param pm
   * @param page
   * @throws WCMException
   */
  private void createMinorVersion(final Page page, boolean ifNotRolloutFlow) throws WCMException {
    Revision rev = null;
    Collection<Revision> revisionsExisting = null;
    try {
      String versionLabel = null;
      final PageManager pm = page.getPageManager();
      revisionsExisting = pm.getRevisions(page.getPath(), null);
      versionLabel = createUniqueVersionLabel(revisionsExisting, null);
      rev = getLatestRevision(page);
      if (null != rev) {
        versionLabel = getVersionLabel(versionLabel, rev.getLabel());
      }
      pm.createRevision(page, versionLabel, null);
    } catch (WCMException e) {
      if (ifNotRolloutFlow) {
        throw new WCMException(
            "Exception occured while creating minor version for page" + page.getPath());
      }
    }
  }

  /**
   * @param versionLabelLoc
   * @param vlabel
   * @return
   */
  private String getVersionLabel(final String versionLabel, final String vlabel) {
    String versionLabelLoc = versionLabel;
    if (null != vlabel && !StringUtils.isEmpty(vlabel)) {
      final String[] minorLabelArr = vlabel.split("\\.");
      if (minorLabelArr.length > 0) {
        int minorVersionValue = Integer.parseInt(minorLabelArr[1]);
        minorVersionValue++;
        versionLabelLoc = minorLabelArr[0] + "." + minorVersionValue;
      }
    }
    return versionLabelLoc;
  }

  /**
   * Return the last revision
   *
   * @param pm
   * @param page
   * @return the latest revision of the page
   */
  private Revision getLatestRevision(final Page page) {
    Revision rev = null;
    try {
      final List<Revision> pageRevisons = new ArrayList<>();
      final Collection<Revision> revisions =
          page.getPageManager().getRevisions(page.getPath(), null);
      if (revisions != null) {
        revisions.stream().filter(Revision.class::isInstance).forEach(pageRevisons::add);
        if (!pageRevisons.isEmpty()) {
          rev = pageRevisons.get(0);
        }
      }
      if (null != rev) {
        return rev;
      }
    } catch (WCMException e) {
      LOG.error("WCMException occured in getLatestRevision", e);
    }
    return rev;
  }

  /**
   * Create s major revision of the page
   *
   * @param rev
   * @param pm
   * @param page
   * @throws WCMException
   */
  private void createMajorRevison(final Revision rev, final Page page) {

    String versionLabel = rev.getLabel();
    final String[] majorLabelArr = rev.getLabel().split("\\.");
    int majorVersionValue = 0;
    if (majorLabelArr.length > 0) {
      majorVersionValue = Integer.parseInt(majorLabelArr[0]);
      majorVersionValue += 1;
      versionLabel = majorVersionValue + ".0";
    }
    try {
      page.getPageManager().createRevision(page, versionLabel, null);
    } catch (WCMException e) {
      LOG.error("Exception while creating Major version for page {}", page.getPath());
    }
  }

  /**
   * creates a unique version label based on the arguments
   *
   * @param revisions
   * @param versionLabelHint
   * @return
   * @throws RepositoryException
   */
  private String createUniqueVersionLabel(
      final Collection<?> revisions, final String versionLabelHint) {
    if (versionLabelHint == null) {
      return null;
    }
    final List<Version> versions = new LinkedList<>();
    revisions.forEach(
        (Object o) -> {
          final Version v;
          if (o instanceof Revision) {
            v = ((Revision) o).getVersion();
          } else {
            if (o instanceof com.day.cq.dam.api.Revision) {
              v = ((com.day.cq.dam.api.Revision) o).getVersion();
            } else {
              v = null;
            }
          }
          if (null != v) {
            versions.add(v);
          }
        });
    String versionLabel = versionLabelHint;
    int count = 1;
    for (Version v : versions) {
      try {
        if (v.getContainingHistory().hasVersionLabel(versionLabel)) {
          versionLabel = versionLabelHint + " (" + ++count + ")";
          break;
        }
      } catch (RepositoryException e) {
        LOG.error("Repository Exceptionn in createUniqueVersionLabel", e);
      }
    }
    return versionLabel;
  }

  /**
   * This method returns the version name
   *
   * @param revision
   * @param session
   * @return
   * @throws RepositoryException
   */
  private String getAssetRevision(
      final com.day.cq.dam.api.Revision revision, final Session session) {
    try {
      return session.getNodeByIdentifier(revision.getId()).getName();
    } catch (RepositoryException e) {
      LOG.error("Repository Exceptionn in getAssetRevision", e);
    }
    return StringUtils.EMPTY;
  }

  /**
   * return the calendar based on the work flow absolute time
   *
   * @param workItem
   * @return
   */
  private Calendar getTime(final WorkItem workItem) {
    final Long time =
        workItem
            .getWorkflowData()
            .getMetaDataMap()
            .get(WorkflowConstants.ABSOLUTE_TIME, Long.class);
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time.longValue());
    return cal;
  }

  /**
   * Gets the path.
   *
   * @param data the data
   * @param session the session
   * @return the path
   */
  private String getPath(final WorkflowData data, final Session session) {
    try {
      final String type = data.getPayloadType();
      if ((type.equals(WorkflowConstants.JCR_PATH)) && (data.getPayload() != null)) {
        final String payloadData = (String) data.getPayload();
        if (session.itemExists(payloadData)) {
          return payloadData;
        }
      } else if ((data.getPayload() != null) && (type.equals(WorkflowConstants.JCR_UUID))) {
        final Node node = session.getNodeByIdentifier((String) data.getPayload());
        return node.getPath();
      }
    } catch (RepositoryException e) {
      LOG.error("A RepositoryException has occured while fetching the path", e);
    }
    return StringUtils.EMPTY;
  }
}
