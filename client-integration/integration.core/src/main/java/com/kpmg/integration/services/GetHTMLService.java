package com.kpmg.integration.services;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.sling.api.resource.ResourceResolver;

public interface GetHTMLService {

  String getHTML(String Path, ResourceResolver resourceResolver)
      throws IOException, ServletException;
}
