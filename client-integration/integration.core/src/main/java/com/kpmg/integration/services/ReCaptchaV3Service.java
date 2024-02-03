package com.kpmg.integration.services;

public interface ReCaptchaV3Service {

  String getCaptchaEnabled();

  String getCaptchaEndpoint();

  String getSiteKey();

  String getSiteSecret();

  double getScoreTolerance();

  boolean isCaptchaVerified(String token);
}
