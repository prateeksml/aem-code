package com.kpmg.core.email.internal;

import com.day.cq.commons.jcr.JcrConstants;
import com.kpmg.core.annotations.WorkflowCodeExcludeFromCodeCoverageReportGenerated;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Class AbstractEmailBuilder. */
@WorkflowCodeExcludeFromCodeCoverageReportGenerated
public abstract class AbstractEmailBuilder implements EMailBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmailBuilder.class);
  protected Properties props = new Properties();

  protected AbstractEmailBuilder(String path, Session session) {
    loadDefaultsFromRepo(path, session);
  }

  /**
   * Load defaults from repo.
   *
   * @param path the path
   * @param session the session
   * @throws EmailException the email exception
   */
  private void loadDefaultsFromRepo(final String path, final Session session) {

    try {
      if ((path != null) && (session.itemExists(path))) {
        final Node templateNode = (Node) session.getItem(path);
        if (templateNode.isNodeType(JcrConstants.NT_FILE)) {
          getPropertyAsStream(path, templateNode);
        }
      }
    } catch (PathNotFoundException e) {
      LOGGER.error("PathNotFoundException {} occured while reading mail template {} ", e, path);
    } catch (RepositoryException e) {
      LOGGER.error("RepositoryException {} occured while reading mail template {} ", e, path);
    }
  }

  /**
   * Gets the property as stream.
   *
   * @param path the path
   * @param templateNode the template node
   * @return the property as stream
   */
  private void getPropertyAsStream(final String path, final Node templateNode) {

    try (InputStream is =
        templateNode.getProperty("jcr:content/jcr:data").getBinary().getStream(); ) {
      final Properties rawProps = new Properties();
      rawProps.load(is);
      final Enumeration<?> e = rawProps.propertyNames();

      while (e.hasMoreElements()) {
        final String key = (String) e.nextElement();
        final String rawValue = rawProps.getProperty(key);
        final String value =
            new String(rawValue.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        props.put(key, value);
      }
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Loaded mail template for {} ", path);
      }
    } catch (RepositoryException | IOException e) {
      LOGGER.error("Error {} occured while reading mail template {} ", e, path);
    }
  }
}
