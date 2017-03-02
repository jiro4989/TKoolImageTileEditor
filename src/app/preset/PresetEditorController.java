package app.preset;

import util.JavaFXCustomizeUtils;

import java.util.stream.*;
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
  @FXML private StackPane stackPane;
  @FXML private ImageView imageView;

  // 終了ボタン
  @FXML private Button okButton;
  @FXML private Button cancelButton;

  //}}}

  @FXML private void initialize() {//{{{

    // TEST CODE//{{{

    Image image = new Image("file:./input/icon_set.png");
    int width  = (int) image.getWidth();
    int height = (int) image.getHeight();
    imageView.setFitWidth(width);
    imageView.setFitHeight(height);

    imageView.setImage(image);

    //}}}

    // イベント登録//{{{

    rowUpButton    . setOnAction(e -> incrementValueOf(rowTextField    , 1));
    columnUpButton . setOnAction(e -> incrementValueOf(columnTextField , 1));
    sizeUpButton   . setOnAction(e -> incrementValueOf(sizeTextField   , 1));

    rowDownButton    . setOnAction(e -> incrementValueOf(rowTextField    , -1));
    columnDownButton . setOnAction(e -> incrementValueOf(columnTextField , -1));
    sizeDownButton   . setOnAction(e -> incrementValueOf(sizeTextField   , -1));

    customizeTextField(rowTextField);
    customizeTextField(columnTextField);
    customizeTextField(sizeTextField, 1, 1000);
    sizeTextField.setText("" + 144);

    //}}}

  }//}}}

  private void customizeTextField(TextField textField, int min, int max) {//{{{

    textField.setText("" + min);
    textField.textProperty().addListener((obs, oldVal, newVal) -> {
      JavaFXCustomizeUtils.setNumberOnly(textField, oldVal, newVal);
      changeGridCells();
    });
    JavaFXCustomizeUtils.setMaxDigitOption(textField, min, max);
    JavaFXCustomizeUtils.setScrollValueOption(textField, 5, 10);

  }//}}}

  private void customizeTextField(TextField textField) {//{{{
    customizeTextField(textField, 1, 100);
  }//}}}

  private void changeGridCells() {//{{{

    clearGridPane();
    GridPane gridPane = new GridPane();
    gridPane.setGridLinesVisible(true);

    int row    = Integer . parseInt(rowTextField    . getText());
    int column = Integer . parseInt(columnTextField . getText());
    int size   = Integer . parseInt(sizeTextField   . getText());

    IntStream.range(0, row).forEach(r -> {

      IntStream.range(0, column).forEach(c -> {

        Pane pane = createEmptyPane(size);
        gridPane.add(pane, c, r);

      });

    });

    stackPane.getChildren().add(gridPane);

  }//}}}

  private void clearGridPane() {//{{{

    int size = stackPane.getChildren().size();
    if (2 <= size) {
      stackPane.getChildren().remove(1, 2);
    }

  }//}}}

  private void incrementValueOf(TextField textField, int gain) {//{{{
    String text = textField.getText();
    int value = Integer.parseInt(text);
    value += gain;
    textField.setText("" + value);
  }//}}}

  private Pane createEmptyPane(int width) {//{{{
    Pane pane = new Pane();
    pane.setPrefWidth(width);
    pane.setPrefHeight(width);
    return pane;
  }//}}}

}
