package jiro.javafx.stage;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Optional;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/** 
 * FileChooserのラッパークラス。
 * ファイルを開いたあとや保存した後に保存したファイルの親フォルダやファイル名を
 * 自動でセットするオプションを持つ。これらの設定はデフォルトでtrueになってい
 * る。
 */
public class MyFileChooser {

  private final FileChooser fc;
  private final boolean AUTO_SET_DIR;
  private final boolean AUTO_SET_FILE_NAME;
  private final Properties properties;
  private final String initDirKey;
  private final String initFileNameKey;

  private static final Stage STAGE_UTIL = new Stage(StageStyle.UTILITY);

  // Builder クラス

  public static class Builder {//{{{

    private final ExtensionFilter extensionFilter;
    private File initDir  = new File(".");
    private String initFileName = "";
    private boolean autoSetDir      = true;
    private boolean autoSetFileName = true;
    private Properties properties  = null;
    private String initDirKey      = null;
    private String initFileNameKey = null;

    // コンストラクタ

    /**
     * MyFileChooserインスタンスを生成するためのBuilderパターン
     *
     * @param desc 説明文
     * @param extension フィルタリングするファイル拡張子
     */
    public Builder(String desc, String extension) {//{{{

      this(new ExtensionFilter(desc, extension));

    }//}}}

    public Builder(ExtensionFilter ef) {//{{{

      extensionFilter = ef;

    }//}}}

    public Builder initDir(String dirName) {//{{{

      initDir(new File(dirName));
      return this;

    }//}}}

    public Builder initDir(File dir) {//{{{

      if (dir.exists()) initDir = dir;
      return this;

    }//}}}

    public Builder initFileName(String fileName) {//{{{

      initFileName = fileName;
      return this;

    }//}}}

    public Builder autoSetDir(boolean bool) {//{{{

      autoSetDir = bool;
      return this;

    }//}}}

    public Builder autoSetFileName(boolean bool) {//{{{

      autoSetFileName = bool;
      return this;

    }//}}}

    public Builder properties(Properties prop) {//{{{

      properties = prop;
      return this;

    }//}}}

    public Builder initDirKey(String key) {//{{{

      initDirKey = key;
      return this;

    }//}}}

    public Builder initFileNameKey(String key) {//{{{

      initFileNameKey = key;
      return this;

    }//}}}

    public MyFileChooser build() {//{{{

      return new MyFileChooser(this);

    }//}}}

  }//}}}

  // private コンストラクタ

  private MyFileChooser(Builder builder) {//{{{

    fc = new FileChooser();

    fc.getExtensionFilters().add(builder.extensionFilter);
    fc.setInitialDirectory(builder.initDir);
    fc.setInitialFileName(builder.initFileName);

    AUTO_SET_DIR       = builder.autoSetDir;
    AUTO_SET_FILE_NAME = builder.autoSetFileName;

    properties      = builder.properties;
    initDirKey      = builder.initDirKey;
    initFileNameKey = builder.initFileNameKey;

    if (   (properties == null && initDirKey != null)
        || (properties == null && initFileNameKey != null)
       )
    {

      throw new NullPointerException("propertiesが未定義の状態でinitDirKeyまたはinitFileNameKeyを設定することはできません。");

    }
  }//}}}

  // ダイアログ表示メソッド

  public Optional<File> openFile() {//{{{

    checkCanShowDialog();
    File file = fc.showOpenDialog(STAGE_UTIL);
    setInitDir(file);
    return Optional.ofNullable(file);

  }//}}}

  public Optional<List<File>> openFiles() {//{{{

    checkCanShowDialog();
    List<File> files = fc.showOpenMultipleDialog(STAGE_UTIL);
    setInitDir(files);
    return Optional.ofNullable(files);

  }//}}}

  public Optional<File> saveFile() {//{{{

    checkCanShowDialog();
    File file = fc.showSaveDialog(STAGE_UTIL);
    setInitDir(file);
    setInitFileName(file);
    return Optional.ofNullable(file);

  }//}}}

  // private メソッド

  private void setInitDir(List<File> files) {//{{{

    if (files != null)
      setInitDir(files.get(0));

  }//}}}

  private void setInitDir(File file) {//{{{

    if (file != null) {

      if (AUTO_SET_DIR) {

        File parent = file.getParentFile();
        fc.setInitialDirectory(parent != null ? parent : new File("."));
        if (initDirKey != null)
          properties.setProperty(initDirKey, parent.toString());

      }

    }

  }//}}}

  private void setInitFileName(File file) {//{{{

    if (file != null) {

      if (AUTO_SET_FILE_NAME) {

        String fileName = file.getName();
        fc.setInitialFileName(fileName);
        if (initFileNameKey != null)
          properties.setProperty(initFileNameKey, fileName);

      }

    }

  }//}}}

  private void checkCanShowDialog() {//{{{

    File file = fc.getInitialDirectory();
    if (!file.exists())
      fc.setInitialDirectory(new File("."));

  }//}}}

  // Getter

  public File getInitialDirectory() {//{{{

    return fc.getInitialDirectory();

  }//}}}

  // Setter

  public void setInitialFileName(String fileName) {//{{{

    fc.setInitialFileName(fileName);

  }//}}}

}
