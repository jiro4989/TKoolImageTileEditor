package util;

import java.io.File;

public enum PresetFiles {
  MV("presets/mv.preset"),
  VXACE("presets/vxace.preset"),
  ICONSET("presets/iconset.preset"),
  DIR("presets");

  public final File FILE;

  public static final String DESCRIPTION = "Preset Files";
  public static final String EXTENSION = "*.preset";

  private PresetFiles(String fileName) {
    FILE = new File(fileName);
  }
}
