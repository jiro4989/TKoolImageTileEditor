package app;

import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.stream.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

class OutputImagePane {//{{{
  private final GridPane outputImageGridPane;
  private List<StackImageView> stackImageViewList;

  OutputImagePane(GridPane aGridPane) {
    outputImageGridPane = aGridPane;
    stackImageViewList = new ArrayList<>(8);
  }

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
  void setImage(File imageFile) {//{{{
    Image image = new Image("file:" + imageFile.getAbsolutePath());
    PixelReader pixel = image.getPixelReader();

    Properties prop = MainController.prop;
    int size = Integer.parseInt(prop.getProperty("size"));
    WritableImage trimmingImage = new WritableImage(pixel, 0, 0, size, size);
    stackImageViewList.get(0).setImage(trimmingImage);
  }//}}}
}//}}}
/**
 * マウスクリック可能なImageViewを再現するためのコンポーネントクラス。
 * StackPaneの中に３つのレイヤーが存在し、それぞれ下から番号ラベル、ImageView、
 * 透明なボタンという順序で構成される。透明なボタンをクリックすると、透明なボタ
 * ンの色が微妙に変化し、選択状態を表現する。
 */
class StackImageView extends StackPane {//{{{
  private final Label label;
  private final ImageView imageView;
  private final Button button;

  StackImageView(int index, double size) {//{{{
    label     = new Label("" + index);
    imageView = new ImageView();
    button    = new Button();

    label.setAlignment(Pos.CENTER);

    button.setOpacity(0.0);
    button.setOnAction(e -> buttonOnAction(label));

    this      .setPrefSize(size, size);
    label     .setPrefSize(size, size);
    imageView .setFitWidth(size);
    imageView .setFitHeight(size);
    button    .setPrefSize(size, size);

    this.getChildren().add(label);
    this.getChildren().add(imageView);
    this.getChildren().add(button);
  }//}}}

  private List<Integer> buttonIndexList = new ArrayList<>();
  private void buttonOnAction(Label label) {//{{{
    int number = Integer.parseInt(label.getText());
    buttonIndexList.add(number);
    for (int i : buttonIndexList) {
      System.out.print("list: " + i + " ");
    }
    System.out.println("");
    if (2 <= buttonIndexList.size()) {
      reverse();
      buttonIndexList.clear();
    }
  }//}}}
  private void reverse() {//{{{
    System.out.println("reverse.");
  }//}}}
  void setImage(Image image) {
    imageView.setImage(image);
  }
}//}}}
