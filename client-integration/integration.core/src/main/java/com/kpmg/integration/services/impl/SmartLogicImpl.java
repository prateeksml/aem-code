package com.kpmg.integration.services.impl;

import com.kpmg.integration.config.SmartLogicConfiguration;
import com.kpmg.integration.services.SmartLogic;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = SmartLogic.class, immediate = true)
@Designate(ocd = SmartLogicConfiguration.class)
public class SmartLogicImpl implements SmartLogic {

  private String contentTypeZthesID;
  private String mediaFormatsZthesID;
  private String personasZthesID;
  private String geopraphyZthesID;
  private String industryZthesID;
  private String serviceZthesID;
  private String insightZthesID;
  private String marketsZthesID;
  private String sesClientPath;
  private String sesPort;
  private String sesClientHostIP;
  private String sesClientProtocol;
  private String commonZthesID;
  private String[] disabledTemplates;

  @Activate
  protected void activate(SmartLogicConfiguration configuration) {
    this.contentTypeZthesID = configuration.getContentTypeZthesID();
    this.mediaFormatsZthesID = configuration.getMediaFormatsZthesID();
    this.personasZthesID = configuration.getPersonasZthesID();
    this.geopraphyZthesID = configuration.getGeopraphyZthesID();
    this.industryZthesID = configuration.getIndustryZthesID();
    this.serviceZthesID = configuration.getServiceZthesID();
    this.insightZthesID = configuration.getInsightZthesID();
    this.marketsZthesID = configuration.getMarketsZthesID();
    this.sesClientProtocol = configuration.getSesClientProtocol();
    this.sesClientHostIP = configuration.getSesClientHostIP();
    this.sesPort = configuration.getSesPort();
    this.sesClientPath = configuration.getSesClientPath();
    this.commonZthesID = configuration.getCommonZthesID();
    this.disabledTemplates = configuration.getDisabledTemplates();
  }

  @Override
  public String getContentTypeZthesID() {
    return this.contentTypeZthesID;
  }

  @Override
  public String getMediaFormatsZthesID() {
    return this.mediaFormatsZthesID;
  }

  @Override
  public String getPersonasZthesID() {
    return this.personasZthesID;
  }

  @Override
  public String getGeopraphyZthesID() {
    return this.geopraphyZthesID;
  }

  @Override
  public String getIndustryZthesID() {
    return this.industryZthesID;
  }

  @Override
  public String getServiceZthesID() {
    return this.serviceZthesID;
  }

  @Override
  public String getInsightZthesID() {
    return this.insightZthesID;
  }

  @Override
  public String getMarketsZthesID() {
    return this.marketsZthesID;
  }

  @Override
  public String getSesClientPath() {
    return this.sesClientPath;
  }

  @Override
  public String getSesPort() {
    return this.sesPort;
  }

  @Override
  public String getSesClientHostIP() {
    return this.sesClientHostIP;
  }

  @Override
  public String getSesClientProtocol() {
    return this.sesClientProtocol;
  }

  @Override
  public String getCommonZthesID() {
    return this.commonZthesID;
  }

  @Override
  public String[] getDisabledTemplates() {
    return this.disabledTemplates.clone();
  }
}
