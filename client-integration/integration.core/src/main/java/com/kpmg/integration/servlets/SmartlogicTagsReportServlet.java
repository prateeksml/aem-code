package com.kpmg.integration.servlets;

import com.google.gson.JsonObject;
import com.kpmg.integration.constants.Constants;
import com.kpmg.integration.services.SmartlogicService;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.codec.CharEncoding;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/kpmg/tags/smartlogic/getreport")
public class SmartlogicTagsReportServlet extends SlingSafeMethodsServlet {
  private static final Logger LOG = LoggerFactory.getLogger(SmartlogicTagsReportServlet.class);
  private static final long serialVersionUID = 1L;
  private static final int MIN_LENGTH = "/content/kpmgpublic/xx/en".length();
  @Reference private transient SmartlogicService service;

  @Override
  protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
      throws ServletException, IOException {
    String page = req.getParameter("page");
    LOG.info("Processing request for {}", page);
    String[] categorytypes = {
      "contenttype",
      "mediaformats",
      "persona",
      "market",
      "geography",
      "industry",
      "service",
      "insight"
    };
    if (null != page && page.trim().length() >= MIN_LENGTH) {
      String lang = page.substring(Constants.SITE_ROOT.length(), MIN_LENGTH);
      resp.setHeader(
          "Content-Disposition", "attachment; filename=\"Tags-Report_" + lang + ".csv\"");
      resp.setContentType("text/csv");
      resp.setCharacterEncoding(CharEncoding.UTF_8);
      OutputStream outputStream = resp.getOutputStream();
      List<JsonObject> smartLogictags;
      StringBuilder stringBuilder = new StringBuilder();
      for (String categorytype : categorytypes) {
        smartLogictags = service.getAllTagsByCategory(page, categorytype);
        if (!smartLogictags.isEmpty()) {
          for (JsonObject json : smartLogictags) {
            String[] hierarchy = json.get("titlePath").getAsString().split("/", 2);
            String category = hierarchy[0];
            String qualifiedName = escapeComma(hierarchy[1]);
            String zthesId = json.get("tagID").getAsString();
            stringBuilder.append("\n" + category + ",");
            stringBuilder.append(qualifiedName + ",");
            stringBuilder.append(zthesId);
          }
        }
      }
      LOG.trace("string response :{}", stringBuilder);
      if (stringBuilder.length() > 0) {
        stringBuilder.insert(0, "Category,Qualified Name,ZthesIDs");
      } else {
        stringBuilder.append("No tag results");
      }
      outputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
      stringBuilder.delete(0, stringBuilder.length());
      outputStream.flush();
      outputStream.close();
    }
    LOG.info("End of doGet");
  }

  public String escapeComma(String input) {
    if (input.contains(",")) {
      return "\"" + input + "\"";
    } else {
      return input;
    }
  }
}
