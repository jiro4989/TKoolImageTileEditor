package app;

import java.io.File;
import java.util.*;
import java.util.stream.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

/**
 * 出力画像ペインの処理やフィールドをラップしたクラス。
 */
class OutputImagePane {
  private final GridPane outputImageGridPane;
  private List<StackImageView> stackImageViewList;

  OutputImagePane(GridPane aGridPane) {//{{{
    outputImageGridPane = aGridPane;
    stackImageViewList = new ArrayList<>(8);
  }//}}}

  /**
   * Propertiesの設定によってレイ・アウトを変更する。
   */
  void setGridCells() {//{{{
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
  void setImage(File imageFile) {//{{{
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
   * 選択した２つのImageViewの画像を交換する。
   */
  void exchangeImage() {//{{{
    Properties prop = MainController.prop;
    int size = Integer.parseInt(prop.getProperty("size"));
    Image image1 = stackImageViewList.get(0).getImage();
    Image image2 = stackImageViewList.get(1).getImage();
    Image tmpImage = new WritableImage(size, size);
  }//}}}
}
