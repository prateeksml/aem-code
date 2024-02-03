package com.kpmg.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.constants.NameConstants;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import com.kpmg.core.workflow.constants.WorkflowConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.nodetype.NodeType;
import org.apache.commons.lang3.StringUtils;

/** The Class NodeUtils. */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public class NodeUtils {

  protected static final String CONTENT_PATTERN = "/content/kpmgpublic/([^/]+)/([^/]+)/";

  private NodeUtils() {}

  /**
   * Checks for mixin.
   *
   * @param node the node
   * @param mixinNodeType the mixin node type
   * @return true, if successful
   * @throws RepositoryException the repository exception
   */
  public static boolean hasMixin(final Node node, final String mixinNodeType)
      throws RepositoryException {
    for (final NodeType mixin : node.getMixinNodeTypes()) {
      if (mixin.getName().equals(mixinNodeType)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the template path.
   *
   * @param page the page
   * @return the template path
   */
  public static String getTemplatePath(final Page page) {
    if (page != null) {
      return page.getTemplate() != null ? page.getTemplate().getPath() : StringUtils.EMPTY;
    }
    return StringUtils.EMPTY;
  }

  public static String geti18NLocale(String pagePath, Session session) throws RepositoryException {
    Node node = session.getNode(pagePath + WorkflowConstants.JCR_CONTENT);
    String language = "en";
    if (node.hasProperty("language")) {
      language = node.getProperty("language").getValue().getString();
    }
    return language;
  }

  public static String getTemplatePath(String pagePath, Session session)
      throws PathNotFoundException, RepositoryException, ValueFormatException {
    Node node = session.getNode(pagePath + WorkflowConstants.JCR_CONTENT);
    String tempaltePath = null;
    if (node.hasProperty(NameConstants.PN_TEMPLATE)) {
      tempaltePath = node.getProperty(NameConstants.PN_TEMPLATE).getValue().getString();
    }
    return tempaltePath;
  }

  public static String extractCountryLang(final String path, final int index) {
    final Matcher matcher = Pattern.compile(CONTENT_PATTERN).matcher(path);
    return matcher.find() ? matcher.group(index) : "";
  }
}
