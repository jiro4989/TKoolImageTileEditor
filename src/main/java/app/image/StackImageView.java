package app.image;

import app.MainController;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

/**
 * マウスクリック可能なImageViewを再現するためのコンポーネントクラス。 StackPaneの中に３つのレイヤーが存在し、それぞれ下から番号ラベル、ImageView、
 * 透明なボタンという順序で構成される。透明なボタンをクリックすると、透明なボタ ンの色が微妙に変化し、選択状態を表現する。
 */
class StackImageView extends StackPane {

  // パネルの選択状態
  private boolean isSelected = false;

  // 選択されたパネルのインデックスのリスト
  private static List<StackImageView> selectedInstanceList = new ArrayList<>();

  // コンポーネント//{{{

  // 番号を表示するLabel
  private final Label label;

  // 画像を表示するImageView
  private final ImageView imageView;

  // 選択状態を表すLabel
  private final Label selectedLabel;

  // クリック動作を実行するための透明なボタン
  private final Button button;

  // }}}

  // コンストラクタ

  StackImageView(int index, double size) { // {{{

    label = new Label("" + index);
    imageView = new ImageView();
    selectedLabel = new Label("");
    button = new Button();

    label.setId("number-label");
    button.setId("opacity-button");
    selectedLabel.setId("selected-label");

    int is = (int) size;
    imageView.setImage(new WritableImage(is, is));

    button.setOnAction(e -> buttonOnAction());

    this.setPrefSize(size, size);
    label.setPrefSize(size, size);
    imageView.setFitWidth(size);
    imageView.setFitHeight(size);
    selectedLabel.setPrefSize(size, size);
    button.setPrefSize(size, size);

    getChildren().add(label);
    getChildren().add(imageView);
    getChildren().add(selectedLabel);
    getChildren().add(button);
  } // }}}

  /** パネルの選択状態を切り替える。 選択されたパネルの数が2つに達すると、画像の入れ替えを実行し、パネルの選択状 を初期化する。 */
  private void buttonOnAction() { // {{{

    setSelection(!isSelected);
    selectedInstanceList.add(this);

    MainController.strategy.invoke(selectedInstanceList);
  } // }}}

  Image getImage() { // {{{

    return imageView.getImage();
  } // }}}

  int getNumber() { // {{{

    return Integer.parseInt(label.getText());
  } // }}}

  static List<StackImageView> getSelectedImageList() { // {{{

    return selectedInstanceList;
  } // }}}

  void setImage(Image image) { // {{{

    imageView.setImage(image);
  } // }}}

  void setSelection(boolean selection) { // {{{

    isSelected = selection;
    selectedLabel.setOpacity(isSelected ? 0.25 : 0.0);
  } // }}}
}
