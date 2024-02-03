package com.kpmg.core.workflow.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.day.cq.replication.ReplicationStatus;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PageReferenceServletTest {

  private PageReferenceServlet testClass;
  private SlingHttpServletRequest request;
  private SlingHttpServletResponse response;
  private ResourceResolver resolver;
  private Resource resource;
  private Page page;
  private ReplicationStatus status;
  private PageManager manager;
  private Iterator<Resource> iter;
  private PrintWriter writer;
  private static final String PAGE_PATH = "/content/kmpg/us/en/test";

  @BeforeEach
  void setUp() throws IOException {
    testClass = new PageReferenceServlet();
    request = mock(SlingHttpServletRequest.class);
    response = mock(SlingHttpServletResponse.class);
    resolver = mock(ResourceResolver.class);
    resource = mock(Resource.class);
    page = mock(Page.class);
    status = mock(ReplicationStatus.class);
    manager = mock(PageManager.class);
    iter = mock(Iterator.class);
    writer = mock(PrintWriter.class);
  }

  @Test
  void doGet() throws ServletException, IOException {
    when(request.getParameter("pageName")).thenReturn(PAGE_PATH);
    when(request.getResourceResolver()).thenReturn(resolver);
    when(resolver.getResource(PAGE_PATH)).thenReturn(resource);
    when(resource.adaptTo(Page.class)).thenReturn(page);
    when(page.adaptTo(ReplicationStatus.class)).thenReturn(status);
    when(status.isActivated()).thenReturn(true);
    when(resolver.adaptTo(PageManager.class)).thenReturn(manager);
    when(resolver.findResources(any(), any())).thenReturn(iter);
    when(response.getWriter()).thenReturn(writer);
    testClass.doGet(request, response);
  }

  @Test
  void testServletException() throws ServletException, IOException {
    when(request.getParameter("pageName")).thenReturn(null);
    assertThrows(
        ServletException.class,
        () -> {
          testClass.doGet(request, response);
        });
  }
}
