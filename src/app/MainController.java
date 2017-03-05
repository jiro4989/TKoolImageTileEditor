package app;

import jiro.javafx.stage.MyFileChooser;
import jiro.javafx.stage.MyFileChooser.Builder;

import app.image.*;
import app.preset.PresetEditor;
import util.MyProperties;
import util.PresetFiles;
import util.PropertiesFiles;
import util.RecentFiles;
import util.ImageUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainController {

  /** 画像規格 */
  public static ImageStandard imageStandard;

  /** 出力画像ペインのクリック時の動作を決定する戦略 */
  public static ControlOutputPaneStrategy strategy = new DeleteStrategy();

  // リストが選択中のファイル。保存対象でも有る。
  private Optional<File> selectedFileOpt = Optional.empty();

  // 環境設定ファイル
  private MyProperties preferences;

  // 出力画像パネル
  private OutputImagePane outputImagePane;

  // 画像選択ダイアログ
  private MyFileChooser imageFileChooser;

  // プリセット選択ダイアログ
  private MyFileChooser presetFileChooser;

  // FXMLで指定するコンポーネント//{{{

  @FXML private VBox root;

  // ファイルメニュー
  @FXML private Menu     fileMenu;
  @FXML private MenuItem openMenuItem;
  @FXML private Menu     openRecentMenu;
  @FXML private MenuItem openJoiningMenuItem;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem saveAsMenuItem;
  @FXML private MenuItem newPresetMenuItem;
  @FXML private MenuItem openPresetMenuItem;
  @FXML private MenuItem editPresetMenuItem;
  @FXML private MenuItem preferencesMenuItem;
  @FXML private MenuItem quitMenuItem;
  @FXML private MenuItem forceQuitMenuItem;

  // 編集メニュー
  @FXML private Menu     editMenu;
  @FXML private MenuItem reloadMenuItem;
  @FXML private MenuItem deleteListMenuItem;
  @FXML private MenuItem clearListMenuItem;

  // フォントサイズ変更メニュー
  @FXML private Menu     fontMenu;
  @FXML private ToggleGroup fontGroup;
  @FXML private RadioMenuItem fontSize8RadioMenuItem;
  @FXML private RadioMenuItem fontSize9RadioMenuItem;
  @FXML private RadioMenuItem fontSize10RadioMenuItem;
  @FXML private RadioMenuItem fontSize11RadioMenuItem;
  @FXML private RadioMenuItem fontSize12RadioMenuItem;

  // ヘルプメニュー
  @FXML private Menu     helpMenu;
  @FXML private MenuItem aboutMenuItem;

  // 中央のレイ・アウト
  @FXML private SplitPane splitPane;

  // ファイルリスト
  @FXML private TitledPane fileListTitledPane;
  @FXML private ListView<File> fileListView;
  @FXML private Button reloadButton;
  @FXML private Button deleteListButton;
  @FXML private Button clearListButton;

  // パネル操作変更
  @FXML private TitledPane controlPanelTitledPane;

  @FXML private ToggleGroup toggleGroup;
  @FXML private RadioButton deleteModeRadioButton;
  @FXML private RadioButton deleteNonEmptyModeRadioButton;
  @FXML private RadioButton sortModeRadioButton;
  @FXML private RadioButton reverseModeRadioButton;

  // 出力画像パネル
  @FXML private TitledPane outputImageTitledPane;
  @FXML private AnchorPane outputAnchorPane;

  //}}}

  // 初期化

  @FXML private void initialize() {//{{{

    // イベント登録{{{

    // モード切替のラジオボタン
    deleteModeRadioButton         . setOnAction ( e -> changeMode ( new DeleteStrategy         ( ) ) ) ;
    deleteNonEmptyModeRadioButton . setOnAction ( e -> changeMode ( new DeleteNonEmptyStrategy ( ) ) ) ;
    sortModeRadioButton           . setOnAction ( e -> changeMode ( new SortStrategy           ( ) ) ) ;
    reverseModeRadioButton        . setOnAction ( e -> changeMode ( new ReverseStrategy        ( ) ) ) ;

    // フォントサイズ変更メニュー
    fontSize8RadioMenuItem.setOnAction(e -> changeFontSize(fontSize8RadioMenuItem));
    fontSize9RadioMenuItem.setOnAction(e -> changeFontSize(fontSize9RadioMenuItem));
    fontSize10RadioMenuItem.setOnAction(e -> changeFontSize(fontSize10RadioMenuItem));
    fontSize11RadioMenuItem.setOnAction(e -> changeFontSize(fontSize11RadioMenuItem));
    fontSize12RadioMenuItem.setOnAction(e -> changeFontSize(fontSize12RadioMenuItem));

    // ファイルリストの選択アイテムの変更
    fileListView.getSelectionModel().selectedItemProperty().addListener(item -> {//{{{
      drawSelectedFile();
    });//}}}

    //}}}

    // 最近開いたファイルの情報からRecentMenuItemを更新する。//{{{

    RecentFiles.createRecentOpenedMenuItems().ifPresent(list -> {

      list.stream().forEach(menuItem -> {

        openRecentMenu.getItems().add(menuItem);

        menuItem.setOnAction(e -> {

          String path = menuItem.getText();
          MyFile myFile = new MyFile(path);
          fileListView.getItems().add(myFile);
          fileListView.getSelectionModel().selectFirst();

        });

      });

    });

    //}}}

    // 環境設定プロパティファイルを読み取り、画像規格を設定する。//{{{
    // ファイルが存在しなかった場合は各種プリセットを生成し、mv.xmlを規格に設定
    // する。

    preferences = new MyProperties(PropertiesFiles.PREFERENCES.FILE);
    if (preferences.load()) {

      imageStandard = new ImageStandard(preferences.getProperty("presetPath"));

      // フォントサイズを変更し、メニューのラジオメニューも変更する
      String fontSize = preferences.getProperty("fontSize");
      selectToggleWithIndex(fontSize);
      changeFontSize(fontSize);

    } else {

      PresetFiles     . DIR . FILE . mkdirs();
      PropertiesFiles . DIR . FILE . mkdirs();
      createInitialFiles();
      imageStandard = new ImageStandard(PresetFiles.MV.FILE.getPath());

      // フォントサイズを変更し、メニューのラジオメニューも変更する
      String fontSize = "10";
      selectToggleWithIndex(fontSize);
      changeFontSize(fontSize);

    }

    //}}}

    imageFileChooser  = new MyFileChooser.Builder("Image Files", "*.png").build();
    presetFileChooser = new MyFileChooser.Builder(PresetFiles.DESCRIPTION, PresetFiles.EXTENSION)
      .initFileName("new_preset").build();

    outputImagePane = new OutputImagePane(outputAnchorPane);
    updateOutputImageTitlePane();

  }//}}}

  private void changeMode(ControlOutputPaneStrategy aStrategy) {//{{{

    strategy = aStrategy;
    OutputImagePane.clearSelectedStackImageView();

  }//}}}

  private void createInitialFiles() {//{{{

    MyProperties mv      = new MyProperties(PresetFiles.MV.FILE);
    MyProperties vxace   = new MyProperties(PresetFiles.VXACE.FILE);
    MyProperties iconset = new MyProperties(PresetFiles.ICONSET.FILE);

    createInitialFile(mv      , 2  , 4  , 144);
    createInitialFile(vxace   , 2  , 4  , 96);
    createInitialFile(iconset , 20 , 16 , 32);

  }//}}}

  private void createInitialFile(MyProperties mp, int row, int column, int size) {//{{{

    if (!mp.exists()) {

      mp . setProperty(ImageStandard . Key . ROW          . TEXT, "" + row);
      mp . setProperty(ImageStandard . Key . COLUMN       . TEXT, "" + column);
      mp . setProperty(ImageStandard . Key . SIZE         . TEXT, "" + size);
      mp . setProperty(ImageStandard . Key . IMAGE_WIDTH  . TEXT, "" + size * column);
      mp . setProperty(ImageStandard . Key . IMAGE_HEIGHT . TEXT, "" + size * row);

      mp.store();

    }

  }//}}}

  private void updateOutputImageTitlePane() {//{{{

    String name = imageStandard.getPresetName();
    String outputTitle = outputImageTitledPane.getText();
    outputTitle = outputTitle.split(" - ")[0];
    outputImageTitledPane.setText(outputTitle + " - " + name);

    outputImagePane.changeGridCells();

  }//}}}

  private void selectToggleWithIndex(String fontSize) {//{{{

    fontGroup.getToggles().stream()
      .map(t -> (RadioMenuItem) t)
      .filter(t -> t.getText().equals(fontSize))
      .forEach(t -> t.setSelected(true));

  }//}}}

  private void changeFontSize(RadioMenuItem fontSizeMenuItem) {//{{{

    changeFontSize(fontSizeMenuItem.getText());

  }//}}}

  private void changeFontSize(String text) {//{{{

    root.setStyle("-fx-font-size: " + text + "pt;");
    preferences.setProperty("fontSize", text);

  }//}}}

  /**
   * 最近開いたファイルをOpenRecentMenuに登録する。
   * log/recent.logから１行ずつのファイルパスとして取得する。ファイルが存在しな
   * かった場合はセットしない。
   */
  private void setOpenRecentMenuItems() {//{{{

    File logDir = new File("log");
    logDir.mkdirs();

    File logFile = new File("log/recent.log");
    if (logFile.exists()) {

      Path path = logFile.toPath();
      try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {

        br.lines()
          .collect(Collectors.toCollection(LinkedHashSet::new))
          .stream()
          .forEach(line -> {
            setOpenRecentMenuItem(line);
          });

      } catch (IOException e) {
        e.printStackTrace();
      }

    } else {

      try {
        logFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }

  }//}}}

  private void setOpenRecentMenuItem(String path) {//{{{

    File file = new File(path);
    if (file.exists()) {

      MenuItem item = new MenuItem(path);
      openRecentMenu.getItems().add(item);

      item.setOnAction(e -> {
        MyFile myFile = new MyFile(item.getText());
        fileListView.getItems().add(myFile);
      });

    }

  }//}}}

  // FXMLイベントメソッド

  // ファイルメニュー

  @FXML private void openMenuItemOnAction() {//{{{

    imageFileChooser.openFiles().ifPresent(files -> {

      files.stream().forEach(file -> {

        MyFile myFile = new MyFile(file);
        fileListView.getItems().add(myFile);
        fileListView.getSelectionModel().selectFirst();

        String path = file.getAbsolutePath();
        path = path.replaceAll("\\\\", "/");

        MenuItem item = new MenuItem(path);
        openRecentMenu.getItems().add(0, item);

      });

    });

  }//}}}

  @FXML private void openJoiningMenuItemOnAction() {//{{{

    imageFileChooser.openFiles().ifPresent(files -> {

      imageFileChooser.setInitialFileName("JoinedImage");

      imageFileChooser.saveFile().ifPresent(file -> {

        Image joinedImage = ImageUtils.joinImageFiles(files);
        if (ImageUtils.write(joinedImage, file)) {

          fileListView.getItems().add(new MyFile(file));
          fileListView.getSelectionModel().selectFirst();
          newPresetMenuItemOnAction();

        } else {

          Alert alert = new Alert(AlertType.ERROR);
          alert.setHeaderText("ファイルの保存に失敗しました。");
          alert.showAndWait();

        }

      });

    });

  }//}}}

  @FXML private void saveMenuItemOnAction() {//{{{

    if (!selectedFileOpt.isPresent())
      saveAsMenuItemOnAction();

    selectedFileOpt.ifPresent(file -> {

      outputImagePane.outputImageFile(file);

    });

  }//}}}

  @FXML private void saveAsMenuItemOnAction() {//{{{

    imageFileChooser.saveFile().ifPresent(file -> {

      outputImagePane.outputImageFile(file);
      fileListView.getItems().add(new MyFile(file));
      fileListView.getSelectionModel().selectLast();

    });

  }//}}}

  @FXML private void newPresetMenuItemOnAction() {//{{{

    presetFileChooser.saveFile().ifPresent(file -> {

      if (!file.exists()) {

        MyProperties newPreset = new MyProperties(file);
        createInitialFile(newPreset, 2, 4, 144);

      }

      File selectedFile = fileListView.getSelectionModel().getSelectedItem();
      PresetEditor editor = new PresetEditor(file, selectedFile);
      editor.showAndWait();

      imageStandard = new ImageStandard(file.getPath());
      updateOutputImageTitlePane();

    });

  }//}}}

  @FXML private void openPresetMenuItemOnAction() {//{{{

    presetFileChooser.openFile().ifPresent(file -> {

      imageStandard = new ImageStandard(file);
      updateOutputImageTitlePane();

    });

  }//}}}

  @FXML private void editPresetMenuItemOnAction() {//{{{

    File file = imageStandard.presetFile;
    if (file != null) {

      File selectedFile = fileListView.getSelectionModel().getSelectedItem();
      PresetEditor editor = new PresetEditor(imageStandard.presetFile, selectedFile);
      editor.showAndWait();
      imageStandard = new ImageStandard(file.getPath());
      updateOutputImageTitlePane();

    }

  }//}}}

  @FXML private void quitMenuItemOnAction() {//{{{

    closeRequest();
    Platform.exit();

  }//}}}

  @FXML private void forceQuitMenuItemOnAction() {//{{{

    String sp = System.lineSeparator();

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setHeaderText(Main.resources.getString("forceQuitHeader"));
    alert.setContentText(
        Main.resources.getString("forceQuitContenr1") + sp
        + Main.resources.getString("forceQuitContenr2")
        );

    Optional<ButtonType> result = alert.showAndWait();
    result.ifPresent(r -> {

      if (r == ButtonType.OK)
        Platform.exit();

    });

  }//}}}

  // ヘルプメニュー

  @FXML private void aboutMenuItemOnAction() {//{{{

  }//}}}

  // ファイルリスト

  @FXML private void reloadButtonOnAction() {//{{{

    drawSelectedFile();

  }//}}}

  private void drawSelectedFile() {//{{{

    SelectionModel<File> model = fileListView.getSelectionModel();
    if (!model.isEmpty()) {

      File imageFile = model.getSelectedItem();
      outputImagePane.setImage(imageFile);

      String fileName = imageFile.getName();
      Stage stage = (Stage) fileListView.getScene().getWindow();
      stage.setTitle(fileName + " - " + Main.TITLE);

      selectedFileOpt = Optional.ofNullable(imageFile);

    }

  }//}}}

  @FXML private void deleteListButtonOnAction() {//{{{

    ObservableList<File> selectedItems = fileListView.getSelectionModel().getSelectedItems();
    fileListView.getItems().removeAll(selectedItems);

    if (fileListView.getItems().isEmpty()) {

      OutputImagePane.clearImages();
      outputImagePane.changeGridCells();

    }

  }//}}}

  @FXML private void clearListButtonOnAction() {//{{{

    fileListView.getItems().clear();
    OutputImagePane.clearImages();
    outputImagePane.changeGridCells();

  }//}}}

  @FXML private void fileListViewOnDragOver(DragEvent event) {//{{{

    Dragboard board = event.getDragboard();
    if (board.hasFiles()) {

      event.acceptTransferModes(TransferMode.COPY);

    }

  }//}}}

  @FXML private void fileListViewOnDragDropped(DragEvent event) {//{{{

    Dragboard board = event.getDragboard();
    if (board.hasFiles()) {

      // 大文字小文字を区別せずにpngファイルのみをファイルリストに追加
      Pattern p = Pattern.compile("^.*\\.((?i)png)");
      board.getFiles().stream()
        .filter(f -> p.matcher(f.getName()).matches())
        .forEach(file -> {

          fileListView.getItems().add(new MyFile(file));
          fileListView.getSelectionModel().selectFirst();

        });

    }

  }//}}}

  /**
   * アプリケーション終了時に実行される処理。
   * 各種設定ファイルを保存する。
   */
  void closeRequest() {//{{{

    ObservableList<MenuItem> recentFiles = openRecentMenu.getItems();
    RecentFiles.writeRecentOpenedFile(recentFiles);

    MyProperties mainMp = new MyProperties(PropertiesFiles.MAIN.FILE);
    mainMp.setProperties(reloadButton);
    mainMp.store();

    double[] poses = splitPane.getDividerPositions();
    preferences.setProperty("splitPane.divider.pos" , "" + poses[0]);
    preferences.setProperty("presetPath", imageStandard.getPresetPath());
    preferences.store();

  }//}}}

  // Setter

  void setDividerPosition() {//{{{

    String val = preferences.getProperty("splitPane.divider.pos");
    val = val == null ? "0.366" : val;
    double pos = Double.parseDouble(val);
    splitPane.setDividerPosition(0, pos);

  }//}}}

}
