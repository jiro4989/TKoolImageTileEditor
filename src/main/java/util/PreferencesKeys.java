package util;

public enum PreferencesKeys {

  DIV_POS          ( "splitPane.divider.pos"     )
  ,FONT_SIZE       ( "fontSize"                  )
  ,IMAGE_INIT_DIR  ( "imageFileChooser.initDir"  )
  ,PRESET_INIT_DIR ( "presetFileChooser.initDir" )
  ,PRESET_PATH     ( "presetPath"                )
  ,LANGS           ( "langs"                     ) ;

  public final String KEY;

  private PreferencesKeys(String key) {

    KEY = key;

  }

}
