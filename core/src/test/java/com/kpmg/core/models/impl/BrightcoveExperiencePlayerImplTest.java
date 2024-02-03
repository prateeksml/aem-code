package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BrightcoveExperiencePlayerImplTest {

  @ParameterizedTest
  @CsvSource(
      value = {"Experience [1234],1234", "Experience,null", "null,null"},
      nullValues = {"null"})
  void testGetExperienceId(String experience, String expectedId) {
    BrightcoveExperiencePlayerImpl player = new BrightcoveExperiencePlayerImpl();
    player.experience = experience;
    player.postConstruct();
    assertEquals(expectedId, player.getExperienceId());
  }
}
