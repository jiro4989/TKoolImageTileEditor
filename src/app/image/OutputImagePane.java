package app.image;

import app.MainController;
import java.io.File;
import java.util.*;
import java.util.stream.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

/**
 * 出力画像ペインの処理やフィールドをラップしたクラス。
 */
public class OutputImagePane {
  private final GridPane outputImageGridPane;
  static List<StackImageView> stackImageViewList;

  public OutputImagePane(GridPane aGridPane) {//{{{
    outputImageGridPane = aGridPane;
    stackImageViewList = new ArrayList<>(8);
  }//}}}

  /**
   * Propertiesの設定によってレイ・アウトを変更する。
   */
  public void changeGridCells() {//{{{
    Properties prop = MainController.prop;
    int row    = Integer.parseInt(prop.getProperty("row"));
    int column = Integer.parseInt(prop.getProperty("column"));
    int size   = Integer.parseInt(prop.getProperty("size"));
    int count  = row * column;

    double gridWidth  = (double) (size * column);
    double gridHeight = (double) (size * row);
    outputImageGridPane.setPrefSize(gridWidth, gridHeight);

    IntStream.range(0, count)
      .forEach(i -> {
        final StackImageView siv = new StackImageView(i+1, size);
        stackImageViewList.add(siv);
        final int c = i % column;
        final int r = i / column;
        outputImageGridPane.add(siv, c, r);
      });

  }//}}}
  /**
   * 画像をImageViewに貼り付ける。
   *
   * @param imageFile 画像
   */
  public void setImage(File imageFile) {//{{{
    Image image = new Image("file:" + imageFile.getAbsolutePath());
    PixelReader pixel = image.getPixelReader();

    Properties prop = MainController.prop;
    int row    = Integer.parseInt(prop.getProperty("row"));
    int column = Integer.parseInt(prop.getProperty("column"));
    int size   = Integer.parseInt(prop.getProperty("size"));
    int count  = row * column;

    IntStream.range(0, count)
      .forEach(i -> {
        int x = i % column * size;
        int y = i / column * size;
        WritableImage trimmingImage = new WritableImage(pixel, x, y, size, size);
        stackImageViewList.get(i).setImage(trimmingImage);
      });
  }//}}}
  /**
   * StackImageViewのすべての選択状態をクリアする。
   */
  public static void clearSelectedStackImageView() {//{{{
    stackImageViewList.stream()
      .forEach(siv -> siv.setSelection(false));
    StackImageView.getSelectedImageList().clear();
  }//}}}
}
