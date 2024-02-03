package com.kpmg.core.workflow.servlets;

import com.day.cq.replication.ReplicationStatus;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.commons.ReferenceSearch;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(
    service = Servlet.class,
    property = {
      Constants.SERVICE_DESCRIPTION + "=KPMG - Servlet to get reference pages",
      "sling.servlet.methods=" + HttpConstants.METHOD_GET,
      "sling.servlet.resourceTypes=kpmg/components/structure/page",
      "sling.servlet.extensions=json",
      "sling.servlet.selectors=pagereference"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class PageReferenceServlet extends SlingSafeMethodsServlet {

  private static final long serialVersionUID = 6963268321592420649L;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    final String path = request.getParameter("pageName");
    if (StringUtils.isEmpty(path)) {
      throw new ServletException("Empty path");
    }
    final ResourceResolver resolver = request.getResourceResolver();
    final Resource resource = resolver.getResource(path);
    final StringBuilder responseString = new StringBuilder();
    boolean isActivated = false;
    if (resource != null) {
      final Page page = resource.adaptTo(Page.class);
      if (page != null) {
        final ReplicationStatus status = page.adaptTo(ReplicationStatus.class);
        isActivated = status.isActivated();
      }
    }
    if (isActivated) {
      final ReferenceSearch referenceSearch = new ReferenceSearch();
      referenceSearch.setExact(true);
      referenceSearch.setHollow(true);
      referenceSearch.setMaxReferencesPerPage(-1);
      final Collection<ReferenceSearch.Info> resultSet =
          referenceSearch.search(resolver, path).values();
      resultSet.forEach(
          info ->
              info.getProperties()
                  .forEach(
                      referencePath -> {
                        final String pagePath =
                            StringUtils.substringBefore(
                                referencePath, WorkflowConstants.JCR_CONTENT);
                        responseString.append(pagePath);
                        responseString.append("\n");
                      }));
    } else {
      responseString.append("pagenotactive");
    }
    response.setContentType("text/HTML");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(responseString.toString());
  }
}
