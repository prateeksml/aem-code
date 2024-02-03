package com.kpmg.core.workflow.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import junitx.util.PrivateAccessor;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class GroupListServletTest {

  private GroupListServlet testClass;
  private SlingHttpServletRequest request;
  private SlingHttpServletResponse response;
  private ResourceResolverFactory resolverFactory;
  private ResourceResolver resolver;
  private Session session;
  private UserManager userManager;
  private QueryBuilder queryBuilder;
  private Query query;
  private SearchResult searchResult;
  List<Hit> hits;
  private Hit hit;
  private Authorizable auth;
  private Principal principal;
  private PrintWriter writer;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testClass = new GroupListServlet();
    request = mock(SlingHttpServletRequest.class);
    response = mock(SlingHttpServletResponse.class);
    resolverFactory = mock(ResourceResolverFactory.class);
    resolver = mock(ResourceResolver.class);
    session = mock(Session.class);
    userManager = mock(UserManager.class);
    queryBuilder = mock(QueryBuilder.class);
    query = mock(Query.class);
    searchResult = mock(SearchResult.class);
    hit = mock(Hit.class);
    hits = new ArrayList<>();
    hits.add(hit);
    auth = mock(Authorizable.class);
    principal = mock(Principal.class);
    writer = mock(PrintWriter.class);

    PrivateAccessor.setField(testClass, "resolverFactory", resolverFactory);
    PrivateAccessor.setField(testClass, "queryBuilder", queryBuilder);
  }

  @Test
  void doGet() throws LoginException, RepositoryException, IOException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
    when(request.getParameter("site")).thenReturn("kpmg");
    when(resolver.adaptTo(Session.class)).thenReturn(session);
    when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
    when(queryBuilder.createQuery(any(), any())).thenReturn(query);
    when(query.getResult()).thenReturn(searchResult);
    when(searchResult.getHits()).thenReturn(hits);
    when(hit.getPath()).thenReturn(PAGE_PATH);
    when(userManager.getAuthorizableByPath(PAGE_PATH)).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn(principal);
    when(principal.getName()).thenReturn("admin");
    when(response.getWriter()).thenReturn(writer);
    testClass.doGet(request, response);
  }

  @Test
  void testLoginException() throws LoginException {
    when(resolverFactory.getServiceResourceResolver(anyMap())).thenThrow(LoginException.class);
    testClass.doGet(request, response);
  }
}
