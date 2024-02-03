package com.kpmg.core.constants;

public class RenditionSize {

  private final String smartCropName;
  private final int width;

  private final int height;

  public RenditionSize(final String smartCropName, final int width, final int height) {

    this.smartCropName = smartCropName;
    this.width = width;
    this.height = height;
  }

  public String getSmartCropName() {

    return this.smartCropName;
  }

  public int getHeight() {

    return this.height;
  }

  public int getWidth() {

    return this.width;
  }
}
