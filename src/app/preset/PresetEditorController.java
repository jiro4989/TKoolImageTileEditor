package app.preset;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class PresetEditorController {

  // FXMLコンポーネント{{{

  // 設定入力//{{{

  @FXML private TitledPane customizeTitledPane;

  @FXML private Label            presetLabel;
  @FXML private ComboBox<String> presetComboBox;

  // 行
  @FXML private Label     rowLabel;
  @FXML private Button    rowDownButton;
  @FXML private TextField rowTextField;
  @FXML private Button    rowUpButton;

  // 列
  @FXML private Label     columnLabel;
  @FXML private Button    columnDownButton;
  @FXML private TextField columnTextField;
  @FXML private Button    columnUpButton;

  // サイズ
  @FXML private Label     sizeLabel;
  @FXML private Button    sizeDownButton;
  @FXML private TextField sizeTextField;
  @FXML private Button    sizeUpButton;

  // 画像の横幅
  @FXML private Label     imageWidthLabel;
  @FXML private TextField imageWidthTextField;

  // 画像の縦幅
  @FXML private Label     imageHeightLabel;
  @FXML private TextField imageHeightTextField;

  // サイズ補完ボタン
  @FXML private Button resizeButton;

  //}}}

  @FXML private TitledPane previewTitledPane;

  // 画像描画
  @FXML private ImageView imageView;
  @FXML private GridPane gridPane;

  // 終了ボタン
  @FXML private Button okButton;
  @FXML private Button cancelButton;

  //}}}

  @FXML private void initialize() {//{{{

    // TEST CODE
    Image image = new Image("file:./input/mv.png");
    int width  = (int) image.getWidth();
    int height = (int) image.getHeight();
    imageView.setFitWidth(width);
    imageView.setFitHeight(height);

    Pane col1 = createEmptyPane(144);
    Pane col2 = createEmptyPane(144);
    Pane col3 = createEmptyPane(144);
    gridPane.addColumn(1, col1);
    gridPane.addColumn(2, col2);
    gridPane.addColumn(3, col3);

    Pane row1 = createEmptyPane(144);
    gridPane.addRow(1, row1);

    imageView.setImage(image);
  }//}}}

  private Pane createEmptyPane(int width) {//{{{
    Pane pane = new Pane();
    pane.setPrefWidth(width);
    pane.setPrefHeight(width);
    return pane;
  }//}}}

}
