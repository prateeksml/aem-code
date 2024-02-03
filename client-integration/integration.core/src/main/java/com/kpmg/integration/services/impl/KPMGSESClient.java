//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kpmg.integration.services.impl;

import com.smartlogic.ses.client.SESClient;
import com.smartlogic.ses.client.Semaphore;
import com.smartlogic.ses.client.exceptions.SESException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.log4j.Log4j;
import org.apache.http.ssl.SSLContextBuilder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class is a copy of the SESClient class from the SES SDK. The only difference is that it uses
 * the standard Java HTTP client instead of the Apache HTTP client.
 *
 * <p>This is because the Apache HTTP client does not use AEMaaCS dedicated egress ip address. and
 * we have to use the standard Java HTTP client to use the AEMaaCS dedicated egress ip address.
 */
@Log4j
public class KPMGSESClient extends SESClient {

  HttpClient KpmgHttpClient = null;

  // override init function to use Standard Java HTTP client instead of Apache HTTP Client.
  protected void initHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
    if (this.KpmgHttpClient == null) {

      KpmgHttpClient =
          HttpClient.newBuilder()
              .connectTimeout(Duration.ofMillis(this.getSocketTimeoutMS()))
              .sslContext(SSLContextBuilder.create().build())
              .build();
    }
  }

  // override  this method to use Standard Java HTTP client instead of Apache HTTP Client.
  protected Semaphore getSemaphore(URL url) throws SESException {
    if (log.isInfoEnabled()) {
      log.info("getSemaphore - entry: '" + url.toExternalForm() + "'");
    }

    Semaphore semaphore = null;
    HttpRequest.Builder httpGet = null;

    try {
      this.initHttpClient();
      if (log.isDebugEnabled()) {
        log.debug("About to make HTTP request: " + url.toExternalForm());
      }
      URI uri = URI.create(url.toExternalForm());
      httpGet =
          HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMillis(this.getSocketTimeoutMS()));

      if (this.getApiToken() != null) {
        httpGet.header("Authorization", this.getApiToken());
      }
      HttpResponse<String> response =
          KpmgHttpClient.send(httpGet.build(), HttpResponse.BodyHandlers.ofString());

      if (log.isDebugEnabled()) {
        log.debug("HTTP request complete: " + url.toExternalForm());
      }

      if (response == null) {
        throw new SESException("Null response from http client: " + url.toExternalForm());
      }

      int statusCode = response.statusCode();
      if (log.isDebugEnabled()) {
        log.debug("HTTP request complete: " + statusCode + " " + url.toExternalForm());
      }

      if (statusCode != 200) {
        throw new SESException(
            "Status code " + statusCode + " received from URL: " + url.toExternalForm());
      }

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byteArrayOutputStream.write(response.body().getBytes(StandardCharsets.UTF_8));
      InputSource inputSource =
          new InputSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setValidating(false);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document xmlDocument = documentBuilder.parse(inputSource);

      semaphore = new KPMGSemaphore(xmlDocument.getDocumentElement());

    } catch (ParserConfigurationException
        | IOException
        | SAXException
        | KeyManagementException
        | NoSuchAlgorithmException e) {
      log.error("Semaphore Exception getting: " + url.toExternalForm(), e);
      throw new SESException("Semaphore Exception" + e.getMessage());
    } catch (InterruptedException e) {
      log.error("Semaphore Request Interrupted: " + url.toExternalForm(), e);
      throw new SESException("Semaphore Exception" + e.getMessage());
    }

    return semaphore;
  }

  // Stangely the element constructor of Semaphore is protected, so we do this as a.. hack.
  static class KPMGSemaphore extends Semaphore {
    KPMGSemaphore(org.w3c.dom.Element element) throws SESException {
      super(element);
    }
  }
}
