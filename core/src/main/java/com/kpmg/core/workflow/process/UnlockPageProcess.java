package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.NodeUtils;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class UnlockPageProcess is used to unlock page. */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Unlock Page for KPMG"})
@ServiceDescription("Unlock Page Process")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class UnlockPageProcess implements WorkflowProcess {

  private static final Logger LOGGER = LoggerFactory.getLogger(UnlockPageProcess.class);

  @Reference private ResourceResolverFactory resolverFactory;

  @Reference(target = "(component.name=org.apache.sling.i18n.impl.JcrResourceBundleProvider)")
  private ResourceBundleProvider resourceBundleProvider;

  /**
   * Unlock a page if it is locked.
   *
   * @param wfItem the wf item
   * @param wfSession the wf session
   * @param args the args
   * @throws WorkflowException the workflow exception
   */
  @Override
  public void execute(
      final WorkItem wfItem, final WorkflowSession wfSession, final MetaDataMap args)
      throws WorkflowException {
    try (ResourceResolver wfResolver = wfSession.adaptTo(ResourceResolver.class)) {
      final Session session = wfSession.adaptTo(Session.class);
      final PageManager pageMgr = wfResolver.adaptTo(PageManager.class);
      final String payloadPath = wfItem.getContentPath();
      final Page page = pageMgr.getPage(payloadPath);
      if (page != null) {
        final String pagePath = page.getContentResource().getPath();
        final Locale pageLocale = page.getLanguage(false);
        final ResourceBundle resourceBundle = resourceBundleProvider.getResourceBundle(pageLocale);
        if (page.isLocked()) {
          page.unlock();
        }

        final Node pageNode = session.getNode(pagePath);
        if (NodeUtils.hasMixin(pageNode, JcrConstants.MIX_LOCKABLE)) {
          final WorkflowData workflowData = wfItem.getWorkflowData();
          pageNode.removeMixin(JcrConstants.MIX_LOCKABLE);
          session.refresh(true);
          session.save();
          final String inboxNotificationMessage =
              getInboxNotificationMessage(args.get(WorkflowConstants.PROCESS_ARGS, String.class));
          if (StringUtils.isNotBlank(inboxNotificationMessage)) {
            workflowData
                .getMetaDataMap()
                .put("workflowTitle", resourceBundle.getString(inboxNotificationMessage));
            wfSession.updateWorkflowData(wfItem.getWorkflow(), workflowData);
          }
        }
      }
    } catch (RepositoryException | WCMException e) {
      LOGGER.error("Exception occured while unlocking the page: ", e);
    }
  }

  /**
   * Gets the inbox notification message.
   *
   * @param argument the argument
   * @return the inbox notification message
   */
  private String getInboxNotificationMessage(final String argument) {
    if (StringUtils.equals("stepback-notification", argument)) {
      return "kpmg.workflow.aar.stepbackEmail.inboxNotification";
    } else if (StringUtils.equals("deactivation-stepback-notification", argument)) {
      return "kpmg.workflow.deact.stepbackEmail.inboxNotification";
    }
    return StringUtils.EMPTY;
  }
}
