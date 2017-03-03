package util;

import java.io.File;

public enum PresetFiles {

    MV       ( "presets/mv.xml"      )
    ,VXACE   ( "presets/vxace.xml"   )
    ,ICONSET ( "presets/iconset.xml" )
    ,DIR     ( "presets"             );

  public final File FILE;

  private PresetFiles(String fileName) {
    FILE = new File(fileName);
  }
}
