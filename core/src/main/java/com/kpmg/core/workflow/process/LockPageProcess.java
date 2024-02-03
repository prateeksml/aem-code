package com.kpmg.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class LockPageWorkflow is to lock a page. */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Lock Page for KPMG"})
@ServiceDescription("Lock Page Process")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class LockPageProcess implements WorkflowProcess {

  private static final Logger LOGGER = LoggerFactory.getLogger(LockPageProcess.class);

  @Reference private ResourceResolverFactory resolverFactory;

  /**
   * The method LockPageWorkflow is to lock a page.
   *
   * @param wfItem
   * @param wfSession
   * @param args
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem wfItem, final WorkflowSession wfSession, final MetaDataMap args)
      throws WorkflowException {
    try (ResourceResolver wfResolver = wfSession.adaptTo(ResourceResolver.class)) {
      final PageManager pageMgr = wfResolver.adaptTo(PageManager.class);
      final String payloadPath = wfItem.getContentPath();
      final Page page = pageMgr.getPage(payloadPath);
      if (page != null) {
        page.lock();
      }
    } catch (WCMException we) {
      LOGGER.error(
          "WCMException {} occured while locking the page {}: ", we, wfItem.getContentPath());
    }
  }
}
