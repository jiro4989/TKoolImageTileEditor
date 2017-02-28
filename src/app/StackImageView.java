package app;

import java.util.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

/**
 * マウスクリック可能なImageViewを再現するためのコンポーネントクラス。
 * StackPaneの中に３つのレイヤーが存在し、それぞれ下から番号ラベル、ImageView、
 * 透明なボタンという順序で構成される。透明なボタンをクリックすると、透明なボタ
 * ンの色が微妙に変化し、選択状態を表現する。
 */
class StackImageView extends StackPane {
  private final Label label;
  private final ImageView imageView;
  private final Button button;
  private boolean isSelected = false;

  StackImageView(int index, double size) {//{{{
    label     = new Label("" + index);
    imageView = new ImageView();
    button    = new Button();

    label.setAlignment(Pos.CENTER);

    button.setOpacity(0.0);
    button.setStyle("-fx-background-color: blue");
    button.setOnAction(e -> buttonOnAction());

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
  private void buttonOnAction() {//{{{
    isSelected = !isSelected;
    button.setOpacity(isSelected ? 0.25 : 0.0);

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
  Image getImage() {//{{{
    return imageView.getImage();
  }//}}}
  void setImage(Image image) {//{{{
    imageView.setImage(image);
  }//}}}
}
