package com.kpmg.core.services;

/** Run mode configuration service for the sling run modes of each environment. */
public interface RunmodeService {

  /**
   * Gets the sling run modes of the environment.
   *
   * @return the sling run mode array.
   */
  String[] getRunmodes();

  /**
   * Checks if the environment is running on author run mode.
   *
   * @return {@code true} if environment is running on author run mode, else {@code false}
   */
  boolean isAuthor();
}
