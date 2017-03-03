package app.preset;

import app.ImageStandard;
import util.JavaFXCustomizeUtils;
import util.MyProperties;
import util.PropertiesFiles;

import java.io.File;
import java.util.Optional;
import java.util.stream.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PresetEditorController {

  private File presetFile;

  // FXMLコンポーネント{{{

  // 設定入力//{{{

  @FXML private TitledPane customizeTitledPane;

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

  // プレビュー画像選択
  @FXML private Button    fileChooserButton;
  @FXML private TextField imageFileTextField;

  // プレビュー画像の横幅
  @FXML private Label     previewImageWidthLabel;
  @FXML private TextField previewImageWidthTextField;

  // プレビュー画像の縦幅
  @FXML private Label     previewImageHeightLabel;
  @FXML private TextField previewImageHeightTextField;

  // サイズ補完ボタン
  @FXML private Button resizeButton;
  @FXML private Button reRowColumnButton;

  //}}}

  @FXML private TitledPane previewTitledPane;

  // 画像描画
  @FXML private AnchorPane anchorPane;
  @FXML private ImageView imageView;

  // 終了ボタン
  @FXML private Button okButton;
  @FXML private Button cancelButton;

  //}}}

  @FXML private void initialize() {//{{{

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

    //}}}

    changeGridCells();
    setImageWidth();
    setImageHeight();

  }//}}}

  private void customizeTextField(TextField textField, int min, int max) {//{{{

    textField.textProperty().addListener((obs, oldVal, newVal) -> {

      JavaFXCustomizeUtils.setNumberOnly(textField, oldVal, newVal);
      changeGridCells();
      setImageWidth();
      setImageHeight();

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
    AnchorPane.setTopAnchor(gridPane, 0.0);
    AnchorPane.setLeftAnchor(gridPane, 0.0);

    int row    = Integer . parseInt(rowTextField    . getText());
    int column = Integer . parseInt(columnTextField . getText());
    int size   = Integer . parseInt(sizeTextField   . getText());

    IntStream.range(0, row).forEach(r -> {

      IntStream.range(0, column).forEach(c -> {

        Pane pane = createEmptyPane(size);
        gridPane.add(pane, c, r);

      });

    });

    anchorPane.getChildren().add(gridPane);

  }//}}}

  private void clearGridPane() {//{{{

    int size = anchorPane.getChildren().size();
    if (2 <= size) {
      anchorPane.getChildren().remove(1, 2);
    }

  }//}}}

  private void setImageWidth() {//{{{

    int column = Integer.parseInt(columnTextField.getText());
    int size   = Integer.parseInt(sizeTextField.getText());
    int value = column * size;
    imageWidthTextField.setText("" + value);

  }//}}}

  private void setImageHeight() {//{{{

    int row  = Integer.parseInt(rowTextField.getText());
    int size = Integer.parseInt(sizeTextField.getText());
    int value = row * size;
    imageHeightTextField.setText("" +  value);

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

  void closeRequest() {//{{{

    MyProperties mp = new MyProperties(PropertiesFiles.PREVIEW_EDITOR.FILE);
    mp.setProperties(okButton);
    mp.store();

  }//}}}

  private void storePreset() {//{{{

    MyProperties preset = new MyProperties(presetFile);

    preset.setProperty(ImageStandard.Key.ROW.TEXT          , rowTextField         . getText());
    preset.setProperty(ImageStandard.Key.COLUMN.TEXT       , columnTextField      . getText());
    preset.setProperty(ImageStandard.Key.SIZE.TEXT         , sizeTextField        . getText());
    preset.setProperty(ImageStandard.Key.IMAGE_WIDTH.TEXT  , imageWidthTextField  . getText());
    preset.setProperty(ImageStandard.Key.IMAGE_HEIGHT.TEXT , imageHeightTextField . getText());

    preset.store();

  }//}}}

  void setPresetFile(File file) {//{{{

    presetFile = file;

    MyProperties mp = new MyProperties(presetFile);
    if (mp.load()) {

      rowTextField         . setText(mp . getProperty(ImageStandard . Key . ROW          . TEXT));
      columnTextField      . setText(mp . getProperty(ImageStandard . Key . COLUMN       . TEXT));
      sizeTextField        . setText(mp . getProperty(ImageStandard . Key . SIZE         . TEXT));
      imageWidthTextField  . setText(mp . getProperty(ImageStandard . Key . IMAGE_WIDTH  . TEXT));
      imageHeightTextField . setText(mp . getProperty(ImageStandard . Key . IMAGE_HEIGHT . TEXT));

    }

  }//}}}

  @FXML private void fileChooserButtonOnAction() {//{{{

    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png"));
    fc.setInitialDirectory(new File("."));

    File file = fc.showOpenDialog(new Stage(StageStyle.UTILITY));
    if (file != null) {

      Image image = new Image("file:" + file.getPath());
      int width  = (int) image.getWidth();
      int height = (int) image.getHeight();
      imageView.setFitWidth(width);
      imageView.setFitHeight(height);
      imageView.setImage(image);

      imageFileTextField.setText(file.getName());
      previewImageWidthTextField  . setText("" + width);
      previewImageHeightTextField . setText("" + height);

      resizeButton      . setDisable(false);
      reRowColumnButton . setDisable(false);

    }

  }//}}}

  @FXML private void resizeButtonOnAction() {//{{{

    String w   = previewImageWidthTextField  . getText();
    int width  = Integer.parseInt(w);

    String c   = columnTextField.getText();
    int column = Integer.parseInt(c);

    int size = width / column;
    sizeTextField.setText("" + size);

  }//}}}

  @FXML private void reRowColumnButtonOnAction() {//{{{

    String s   = sizeTextField.getText();
    int size   = Integer.parseInt(s);

    String w   = previewImageWidthTextField.getText();
    String h   = previewImageHeightTextField.getText();
    int width  = Integer.parseInt(w);
    int height = Integer.parseInt(h);

    int row    = height / size;
    int column = width / size;
    rowTextField.setText("" + row);
    columnTextField.setText("" + column);

  }//}}}

  @FXML private void okButtonOnAction() {//{{{

    cancelButton.getScene().getWindow().hide();
    storePreset();

  }//}}}

  @FXML private void cancelButtonOnAction() {//{{{

    cancelButton.getScene().getWindow().hide();

  }//}}}

}
