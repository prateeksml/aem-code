package com.kpmg.core.models;

public interface SocialShareModel {

  String getMetaOgTitle();

  String getPageUrl();

  boolean isFacebookEnabled();

  boolean isLinkedinEnabled();

  boolean isTwitterEnabled();

  boolean isSocialMediaEnabled();

  String getLabel();
}
