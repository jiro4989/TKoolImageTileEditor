package app.image;

import app.ImageStandard;
import app.MainController;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import util.ImageUtils;

/** 出力画像ペインの処理やフィールドをラップしたクラス。 */
public class OutputImagePane {
  private final AnchorPane outputAnchorPane;
  static List<StackImageView> stackImageViewList;

  public OutputImagePane(AnchorPane anchorPane) { // {{{
    outputAnchorPane = anchorPane;
    stackImageViewList = new ArrayList<>(8);
  } // }}}

  /** Propertiesの設定によってレイ・アウトを変更する。 */
  public void changeGridCells() { // {{{

    if (1 <= outputAnchorPane.getChildren().size()) {

      outputAnchorPane.getChildren().remove(0, 1);
      clearImages();
    }

    ImageStandard standard = MainController.imageStandard;
    int row = standard.row;
    int column = standard.column;
    int size = standard.size;
    int count = row * column;

    double gridWidth = (double) (standard.imageWidth);
    double gridHeight = (double) (standard.imageHeight);
    GridPane grid = new GridPane();
    grid.setGridLinesVisible(true);
    grid.setPrefSize(gridWidth, gridHeight);
    AnchorPane.setTopAnchor(grid, 5.0);
    AnchorPane.setLeftAnchor(grid, 5.0);

    IntStream.range(0, count)
        .forEach(
            i -> {
              final StackImageView siv = new StackImageView(i + 1, size);
              stackImageViewList.add(siv);
              final int c = i % column;
              final int r = i / column;
              grid.add(siv, c, r);
            });

    outputAnchorPane.getChildren().add(grid);
  } // }}}

  public void outputImageFile(File outputFile) { // {{{

    ImageStandard is = MainController.imageStandard;
    int column = is.column;
    int size = is.size;
    int outputWidth = is.imageWidth;
    int outputHeight = is.imageHeight;

    WritableImage outputImage = new WritableImage(outputWidth, outputHeight);

    // パネルの画像を出力画像に書き込む//{{{

    AtomicInteger atom = new AtomicInteger(0);
    stackImageViewList
        .stream()
        .forEach(
            siv -> {
              int i = atom.getAndIncrement();
              int x = i % column * size;
              int y = i / column * size;

              Image image = siv.getImage();

              int[] pixels = ImageUtils.readPixels(image, size, size);
              ImageUtils.writePixels(outputImage, x, y, size, size, pixels);
            });

    // }}}

    if (!ImageUtils.write(outputImage, outputFile)) {

      Alert alert = new Alert(AlertType.ERROR);
      alert.setHeaderText("ファイルの保存に失敗しました。");
      alert.showAndWait();
    }
  } // }}}

  /** StackImageViewのすべての選択状態をクリアする。 */
  public static void clearSelectedStackImageView() { // {{{
    stackImageViewList.stream().forEach(siv -> siv.setSelection(false));
    StackImageView.getSelectedImageList().clear();
  } // }}}

  public static void clearImages() { // {{{

    StackImageView.getSelectedImageList().clear();
    stackImageViewList = new ArrayList<>(8);
  } // }}}

  /**
   * 画像をImageViewに貼り付ける。
   *
   * @param imageFile 画像
   */
  public void setImage(File imageFile) { // {{{

    Image image = new Image("file:" + imageFile.getAbsolutePath());

    if (MainController.imageStandard.imageWidth == image.getWidth()
        && MainController.imageStandard.imageHeight <= image.getHeight()) {

      clearSelectedStackImageView();

      PixelReader pixel = image.getPixelReader();

      ImageStandard standard = MainController.imageStandard;
      int standardWidth = standard.imageWidth;
      int standardHeight = standard.imageHeight;

      int row = standard.row;
      int column = standard.column;
      int size = standard.size;
      int count = row * column;

      IntStream.range(0, count)
          .forEach(
              i -> {
                int x = i % column * size;
                int y = i / column * size;
                WritableImage trimmingImage = new WritableImage(pixel, x, y, size, size);
                stackImageViewList.get(i).setImage(trimmingImage);
              });

    } else {

      changeGridCells();
    }
  } // }}}
}
