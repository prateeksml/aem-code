package com.kpmg.integration.servlets;

import com.google.gson.JsonObject;
import com.kpmg.integration.services.SmartlogicService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/kpmg/tags/smartlogic")
public class SmartlogicServlet extends SlingSafeMethodsServlet {
  private static final Logger LOG = LoggerFactory.getLogger(SmartlogicServlet.class);
  private static final long serialVersionUID = 1L;
  @Reference private transient SmartlogicService service;

  @Override
  protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
      throws ServletException, IOException {
    String page = req.getParameter("page");
    String categorytype = req.getParameter("category");
    LOG.debug("page path :{} category : {}", page, categorytype);
    List<JsonObject> smartLogictags = new ArrayList<>();
    if (null != categorytype) {
      smartLogictags = service.getAllTagsByCategory(page, categorytype);
      StringBuilder stringBuilder = new StringBuilder();
      if (!smartLogictags.isEmpty()) {
        for (JsonObject json : smartLogictags) {
          String[] categoryTitlePath;
          if (json.has("titlePath")) {
            categoryTitlePath = json.get("titlePath").toString().split("/", 2);
            stringBuilder.append("\n " + categoryTitlePath[1]);
          } else {
            stringBuilder.append("no title path or tag ID");
          }
        }
      }
      LOG.trace("smartlogic tags :{}", stringBuilder);
    }
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(smartLogictags.toString());
  }
}
