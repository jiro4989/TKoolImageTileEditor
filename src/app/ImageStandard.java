package app;

import jiro.java.util.MyProperties;
import java.io.*;
import javafx.scene.image.*;

/**
 * 画像の規格を保持するクラス。
 */
public class ImageStandard {

  /** 行数 */
  public final int row;

  /** 列数 */
  public final int column;

  /** パネル１つあたりの幅 */
  public final int size;

  /** パネル画像をまとめたイメージ全体の横幅 */
  public final int imageWidth;

  /** パネル画像をまとめたイメージ全体の縦幅 */
  public final int imageHeight;

  /** パネル初期化用のインスタンス */
  public final Image emptyImage;

  /** 画像規格のキー */
  public static enum Key {//{{{

    ROW          ( "row"         ) ,
    COLUMN       ( "column"      ) ,
    SIZE         ( "size"        ) ,
    IMAGE_WIDTH  ( "imageWidth"  ) ,
    IMAGE_HEIGHT ( "imageHeight" );

    public final String TEXT;

    private Key(String aKey) {

      TEXT = aKey;

    }

  }//}}}

  /** 画像規格を取得するプリセットファイル */
  final File presetFile;

  public ImageStandard(
        int  aRow
      , int  aColumn
      , int  aSize
      , int  aImageWidth
      , int  aImageHeight
      , File aPresetFile
      ) {//{{{

    row         = aRow;
    column      = aColumn;
    size        = aSize;
    imageWidth  = aImageWidth;
    imageHeight = aImageHeight;
    presetFile  = aPresetFile;
    emptyImage = new WritableImage(size, size);

  }//}}}

  ImageStandard(String presetPath) {//{{{

    this(new File(presetPath));

  }//}}}

  ImageStandard(File aPresetFile) {//{{{

    int r = 2;
    int c = 4;
    int s = 144;
    int w = s * c;
    int h = s * r;

    MyProperties mp = new MyProperties(aPresetFile);
    if (mp.load()) {

      r = Integer . parseInt(mp . getProperty(Key . ROW          . TEXT).orElse("" + r));
      c = Integer . parseInt(mp . getProperty(Key . COLUMN       . TEXT).orElse("" + c));
      s = Integer . parseInt(mp . getProperty(Key . SIZE         . TEXT).orElse("" + s));
      w = Integer . parseInt(mp . getProperty(Key . IMAGE_WIDTH  . TEXT).orElse("" + w));
      h = Integer . parseInt(mp . getProperty(Key . IMAGE_HEIGHT . TEXT).orElse("" + h));

    }

    row         = r;
    column      = c;
    size        = s;
    imageWidth  = w;
    imageHeight = h;
    presetFile = aPresetFile;
    emptyImage = new WritableImage(size, size);

  }//}}}

  public String getPresetPath() {//{{{
    return presetFile.getPath();
  }//}}}

  public String getPresetName() {//{{{
    return presetFile.getName();
  }//}}}

  @Override
  public String toString() {//{{{

    return String.format(
        "row: %d, column: %d, size: %d, imageWidth: %d, imageHeight: %d"
        , row
        , column
        , size
        , imageWidth
        , imageHeight
        );

  }//}}}

}
