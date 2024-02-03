package com.kpmg.core.workflow.newpage;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.constants.NameConstants;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.config.KPMGTemplates;
import com.kpmg.core.utils.ResourceResolverUtility;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.List;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This workflow process step will adds allowed templates property based on combination of template.
 */
@Component(
    service = WorkflowProcess.class,
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Add Allowed Templates"})
@ServiceDescription("Allowed templates workflow step")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class AllowedTemplate implements WorkflowProcess {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllowedTemplate.class);

  @Reference ResourceResolverFactory resolverFactory;

  @Reference private KPMGTemplates templateService;

  /**
   * This method will identify whether page belongs to master or blue print.
   *
   * @param item
   * @param session
   * @param metaDataMap
   * @throws WorkflowException
   */
  @Override
  public void execute(
      final WorkItem workItem, final WorkflowSession workFlowSession, final MetaDataMap metaDataMap)
      throws WorkflowException {
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final PageManager pageMgr = resourceResolver.adaptTo(PageManager.class);
      final String payloadPath = workItem.getContentPath();
      final Page currentPage = pageMgr.getPage(payloadPath);
      final List<String> allowedTemplates = templateService.getAllowedTemplates(currentPage);
      if (!allowedTemplates.isEmpty()) {
        final Resource pageContentResource = currentPage.getContentResource();
        final ModifiableValueMap pageProperties =
            pageContentResource.adaptTo(ModifiableValueMap.class);
        pageProperties.put(
            NameConstants.PN_ALLOWED_TEMPLATES,
            allowedTemplates.toArray(new String[allowedTemplates.size()]));
        resourceResolver.commit();
      }
    } catch (LoginException e) {
      LOGGER.error("A Login Exception has occured while setting the allowed templates property", e);
    } catch (PersistenceException e) {
      LOGGER.error(
          "A PersistenceException has occured while saving the allowed templates property", e);
    }
  }
}
