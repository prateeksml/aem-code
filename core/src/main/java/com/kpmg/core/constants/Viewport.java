package com.kpmg.core.constants;

public enum Viewport {
  DESKTOP_S("desktopsmall"),
  DESKTOP_M("desktopmedium"),
  DESKTOP_HD("desktophd"),
  TABLET("tablet"),
  MOBILE("mobile"),
  RETINA_DESKTOP_S("retinadesktopsmall"),
  RETINA_DESKTOP_M("retinadesktopmedium"),
  RETINA_TABLET("retinatablet"),
  RETINA_MOBILE("retinamobile");

  private String label;

  Viewport(final String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
