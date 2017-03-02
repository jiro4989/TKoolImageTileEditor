package app.image;

import app.MainController;
import app.ImageStandard;
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

    ImageStandard standard = MainController.imageStandard;
    int row    = standard.row;
    int column = standard.column;
    int size   = standard.size;
    int count  = row * column;

    double gridWidth  = (double) (standard.imageWidth);
    double gridHeight = (double) (standard.imageHeight);
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

    ImageStandard standard = MainController.imageStandard;
    int standardWidth = standard.imageWidth;
    int standardHeight = standard.imageHeight;

    int row    = standard.row;
    int column = standard.column;
    int size   = standard.size;
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

  public static void clearImages() {//{{{
    StackImageView.getSelectedImageList().clear();

    stackImageViewList.stream()
      .forEach(siv -> {
        siv.setSelection(false);
        siv.clearImage();
      });
  }//}}}
}
