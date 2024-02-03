package com.kpmg.core.services;

/**
 * ExternalEmailCommunication
 *
 * @author chay
 */
public interface ExternalEmailCommunication {

  /**
   * Method isProduction
   *
   * @return isProduction
   */
  String getProduction();

  /**
   * Method getKpmgTestEmail
   *
   * @return kpmgTestEmail
   */
  String getKpmgTestEmail();
}
