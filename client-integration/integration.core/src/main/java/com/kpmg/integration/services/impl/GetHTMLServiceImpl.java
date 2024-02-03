package com.kpmg.integration.services.impl;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.WCMMode;
import com.kpmg.integration.services.GetHTMLService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = GetHTMLService.class, immediate = true)
public class GetHTMLServiceImpl implements GetHTMLService {

  private static final Logger LOG = LoggerFactory.getLogger(GetHTMLServiceImpl.class);

  @Reference private RequestResponseFactory requestResponseFactory;

  @Reference private SlingRequestProcessor requestProcessor;

  @Override
  public String getHTML(String path, ResourceResolver resourceResolver) {

    String htmlResult = "";

    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      HttpServletRequest request =
          requestResponseFactory.createRequest(HttpConstants.METHOD_GET, path);
      request.setAttribute(WCMMode.REQUEST_ATTRIBUTE_NAME, WCMMode.DISABLED);
      HttpServletResponse response = requestResponseFactory.createResponse(out);
      requestProcessor.processRequest(request, response, resourceResolver);
      htmlResult = out.toString(response.getCharacterEncoding());
    } catch (ServletException | IOException e) {
      LOG.error("AN ERROR OCCURRED WHILE GETTING HTML CONTENT - {}", e.getMessage());
    }
    return htmlResult;
  }
}
