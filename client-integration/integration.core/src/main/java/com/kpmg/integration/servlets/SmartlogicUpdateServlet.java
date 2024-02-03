package com.kpmg.integration.servlets;

import com.google.gson.JsonArray;
import com.kpmg.integration.services.SmartlogicService;
import java.io.IOException;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/kpmg/updateAction")
public class SmartlogicUpdateServlet extends SlingSafeMethodsServlet {
  /** */
  private static final long serialVersionUID = 6316929383571900358L;

  @Reference private transient SmartlogicService service;

  private static final Logger LOG = LoggerFactory.getLogger(SmartlogicUpdateServlet.class);

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
    JsonArray jsonUpdatedResults = new JsonArray();
    try {
      if (service != null) {
        jsonUpdatedResults = service.getUpdatedTags(request);
      }
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      response.getWriter().write(jsonUpdatedResults.toString());
    } catch (IOException e) {
      LOG.error("IO Exception occured in doGet() of SmartlogicUpdateServlet" + e.getMessage(), e);
    }
  }
}
