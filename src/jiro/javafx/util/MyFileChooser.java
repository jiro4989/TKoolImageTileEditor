package jiro.javafx.util;

import java.io.File;
import java.util.List;
import java.util.Optional;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyFileChooser {

  private final FileChooser fc;
  private static final Stage STAGE_UTIL = new Stage(StageStyle.UTILITY);

  // Builder クラス

  public static class Builder {//{{{

    private final ExtensionFilter extensionFilter;
    private File initDir  = new File(".");
    private String initFileName = "";

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

      initDir = dir;
      return this;

    }//}}}

    public Builder initFileName(String fileName) {//{{{

      initFileName = fileName;
      return this;

    }//}}}

    public MyFileChooser build() {//{{{

      return new MyFileChooser(this);

    }//}}}

  }//}}}

  // private コンストラクタ

  private MyFileChooser(Builder builder) {//{{{

    fc = new FileChooser();
    fc.setInitialDirectory(builder.initDir);
    fc.setInitialFileName(builder.initFileName);

  }//}}}

  public Optional<File> openFile() {//{{{

    File file = fc.showOpenDialog(STAGE_UTIL);
    return Optional.ofNullable(file);

  }//}}}

  public Optional<List<File>> openFiles() {//{{{

    List<File> files = fc.showOpenMultipleDialog(STAGE_UTIL);
    return Optional.ofNullable(files);

  }//}}}

  public Optional<File> saveFile() {//{{{

    File file = fc.showSaveDialog(STAGE_UTIL);
    return Optional.ofNullable(file);

  }//}}}

}
