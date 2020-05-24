package util;

import java.io.File;

public enum PropertiesFiles {
  PREFERENCES("properties/preferences.xml"),
  MAIN("properties/main.xml"),
  PREVIEW_EDITOR("properties/preview_editor.xml"),
  DIR("properties");

  public final File FILE;

  public static final String DESCRIPTION = "Properties Files";
  public static final String EXTENSION = "*.xml";

  private PropertiesFiles(String fileName) {
    FILE = new File(fileName);
  }
}
