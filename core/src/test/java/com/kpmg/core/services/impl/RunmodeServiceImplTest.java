package com.kpmg.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.kpmg.core.services.RunmodeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RunmodeServiceImplTest {

  @InjectMocks private RunmodeServiceImpl runmodeServiceImpl;

  @Mock private RunmodeConfig config;

  @BeforeEach
  public void setUpProps() {

    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testEmptyPreviewUrl() {
    this.runmodeServiceImpl.activate(this.config);
    assertNull(this.runmodeServiceImpl.getRunmodes(), "Empty Runmodes");
  }

  @Test
  void testisAuthor() {
    this.runmodeServiceImpl.activate(this.config);
    assertFalse(
        this.runmodeServiceImpl.isAuthor(), "Environment is not running in author run mode");
  }
}
