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
import com.kpmg.core.utils.NodeUtils;
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
    property = {WorkflowConstants.PROCESS_LABEL + "=" + "Set hideInNav=true property"})
@ServiceDescription("Set hideInNav=true property workflow step")
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class SetHideInNavProperty implements WorkflowProcess {

  @Reference private KPMGTemplates templateService;

  @Reference ResourceResolverFactory resolverFactory;

  private static final Logger LOGGER = LoggerFactory.getLogger(SetHideInNavProperty.class);

  /**
   * This method will identify sling resource type and add hideInNav property if it belongs to
   * allowed list.
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
    LOGGER.info("Executing {} execute() method.", getClass());
    try (ResourceResolver resourceResolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final List<String> hideInNavTemplatesList = templateService.getHideInNavTemplates();
      final List<String> hideInSearchTemplatesList = templateService.getHideInSearchTemplates();
      final PageManager pageMgr = resourceResolver.adaptTo(PageManager.class);
      final String payloadPath = workItem.getContentPath();
      final Page currentPage = pageMgr.getPage(payloadPath);
      final String templatePath = NodeUtils.getTemplatePath(currentPage);
      final Resource pageContentResource = currentPage.getContentResource();
      final ModifiableValueMap pageProperties =
          pageContentResource.adaptTo(ModifiableValueMap.class);
      if (!hideInNavTemplatesList.isEmpty() && hideInNavTemplatesList.contains(templatePath)) {
        pageProperties.put(NameConstants.PN_HIDE_IN_NAV, Boolean.TRUE.toString());
      }
      if (!hideInSearchTemplatesList.isEmpty()
          && hideInSearchTemplatesList.contains(templatePath)) {
        pageProperties.put(WorkflowConstants.HIDE_IN_SEARCH, Boolean.TRUE.toString());
      }
      resourceResolver.commit();
    } catch (LoginException e) {
      LOGGER.error(
          "A Login Exception has occured while setting the hide in nav/search property", e);
    } catch (PersistenceException e) {
      LOGGER.error(
          "A PersistenceException has occured while setting the hide in nav/search property", e);
    }
    LOGGER.info("Completed {} execute() method.", getClass());
  }
}
