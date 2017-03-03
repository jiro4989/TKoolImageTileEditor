package app.image;

import app.ImageStandard;
import app.MainController;
import util.ImageUtils;

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
  private final AnchorPane outputAnchorPane;
  static List<StackImageView> stackImageViewList;

  public OutputImagePane(AnchorPane anchorPane) {//{{{
    outputAnchorPane = anchorPane;
    stackImageViewList = new ArrayList<>(8);
  }//}}}

  /**
   * Propertiesの設定によってレイ・アウトを変更する。
   */
  public void changeGridCells() {//{{{

    if (1 <= outputAnchorPane.getChildren().size()) {

      outputAnchorPane.getChildren().remove(0, 1);
      clearImages();

    }

    ImageStandard standard = MainController.imageStandard;
    int row    = standard.row;
    int column = standard.column;
    int size   = standard.size;
    int count  = row * column;

    double gridWidth  = (double) (standard.imageWidth);
    double gridHeight = (double) (standard.imageHeight);
    GridPane grid = new GridPane();
    grid.setGridLinesVisible(true);
    grid.setPrefSize(gridWidth, gridHeight);
    AnchorPane.setTopAnchor(grid, 5.0);
    AnchorPane.setLeftAnchor(grid, 5.0);

    IntStream.range(0, count)
      .forEach(i -> {

        final StackImageView siv = new StackImageView(i+1, size);
        stackImageViewList.add(siv);
        final int c = i % column;
        final int r = i / column;
        grid.add(siv, c, r);

      });

    outputAnchorPane.getChildren().add(grid);

  }//}}}

  public void outputImageFile() {//{{{

    Image image = stackImageViewList.get(0).getImage();
    int width   = (int) image.getWidth();
    int height  = (int) image.getHeight();

    int[] pixels = ImageUtils.readPixels(image, width, height);
    WritableImage newImage = ImageUtils.writePixels(144, 0, width, height, pixels);

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
    stackImageViewList = new ArrayList<>(8);

  }//}}}

  /**
   * 画像をImageViewに貼り付ける。
   *
   * @param imageFile 画像
   */
  public void setImage(File imageFile) {//{{{

    Image image = new Image("file:" + imageFile.getAbsolutePath());

    if (    MainController.imageStandard.imageWidth  == image.getWidth()
        &&  MainController.imageStandard.imageHeight <= image.getHeight()
       )
    {

      PixelReader pixel = image.getPixelReader();

      ImageStandard standard = MainController.imageStandard;
      int standardWidth = standard.imageWidth;
      int standardHeight = standard.imageHeight;

      int row    = standard.row;
      int column = standard.column;
      int size   = standard.size;
      int count  = row * column;

      IntStream.range(0, count).forEach(i -> {

        int x = i % column * size;
        int y = i / column * size;
        WritableImage trimmingImage = new WritableImage(pixel, x, y, size, size);
        stackImageViewList.get(i).setImage(trimmingImage);

      });

    }

  }//}}}

}
