package com.kpmg.core.workflow.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.utils.ResourceResolverUtility;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = Servlet.class,
    property = {
      Constants.SERVICE_DESCRIPTION + "=KPMG - GroupListServlet",
      "sling.servlet.methods=" + HttpConstants.METHOD_GET,
      "sling.servlet.resourceTypes=kpmg/components/structure/page",
      "sling.servlet.extensions=json",
      "sling.servlet.selectors=grouplist"
    })
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class GroupListServlet extends SlingAllMethodsServlet {

  /** Unique identifier for this service */
  private static final long serialVersionUID = 8797021L;

  private static final Logger LOG = LoggerFactory.getLogger(GroupListServlet.class);

  @Reference private transient QueryBuilder queryBuilder;

  @Reference private transient ResourceResolverFactory resolverFactory;

  @Override
  public void doGet(
      final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
    try (ResourceResolver resolver =
        ResourceResolverUtility.getServiceResourceResolver("userpermissions", resolverFactory)) {
      final String site = request.getParameter("site");
      final String contentApprovers = "aem-" + site + "-livecopy-admin";
      final Session session = resolver.adaptTo(Session.class);
      final UserManager userManager = resolver.adaptTo(UserManager.class);
      final Map<String, String> map = new HashMap<>();
      map.put("path", "/home/groups");
      map.put("type", "rep:Group");
      map.put("group.p.or", "true");
      map.put("group.1_fulltext", contentApprovers);
      map.put("p.limit", "-1");
      final Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
      final SearchResult searchResult = query.getResult();
      final List<Hit> hits = searchResult.getHits();
      final Set<String> groupNames = new TreeSet<>();
      for (final Hit result : hits) {
        final Authorizable auth = userManager.getAuthorizableByPath(result.getPath());
        groupNames.add(auth.getPrincipal().getName());
      }
      if (!groupNames.isEmpty()) {
        final String groupName = String.join(",", groupNames);
        response.getWriter().write(groupName);
      }
    } catch (LoginException | RepositoryException | IOException e) {
      LOG.error("A Exception has occured while running GroupListServlet ", e);
    }
  }
}
