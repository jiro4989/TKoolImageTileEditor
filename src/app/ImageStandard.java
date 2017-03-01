package app;

import java.io.*;
import java.util.Properties;

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

  /**
   * 画像規格の生成。
   *
   * @param aRow    行数
   * @param aColumn 列数
   * @param aSize   パネルあたりの幅
   */
  ImageStandard(int aRow, int aColumn, int aSize) {//{{{
    row         = aRow;
    column      = aColumn;
    size        = aSize;
    imageWidth  = size * column;
    imageHeight = size * row;
  }//}}}

  /**
   * プリセットファイルから値を読み取ってインスタンスを生成する。
   *
   * @param presetName プリセットファイル名
   */
  ImageStandard(String presetName) {//{{{

    int r = 2;
    int c = 4;
    int s = 144;

    Properties prop = new Properties();
    File presetFile = new File("presets/" + presetName);
    try (InputStream in = new FileInputStream(presetFile)) {
      prop.load(new InputStreamReader(in, "UTF-8"));
      r = Integer.parseInt(prop.getProperty("row"));
      c = Integer.parseInt(prop.getProperty("column"));
      s = Integer.parseInt(prop.getProperty("size"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    row         = r;
    column      = c;
    size        = s;
    imageWidth  = size * column;
    imageHeight = size * row;

  }//}}}

  @Override
  public String toString() {
    return String.format(
        "row: %d, column: %d, size: %d, imageWidth: %d, imageHeight: %d"
        , row
        , column
        , size
        , imageWidth
        , imageHeight
        );
  }
}
