package app;

import app.image.*;
import jiro.lib.javafx.stage.FileChooserManager;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainController {
  // 環境設定
  public static Properties prop;
  // 出力画像ペインのクリック時の動作を決定するインスタンス
  public static ControlOutputPaneStrategy strategy = new DeleteStrategy();
  // 出力画像パネル
  private OutputImagePane outputImagePane;
  // 最近開いたファイルとして記録する上限数
  private int max = 20;

  // FXMLで指定するコンポーネント
  // メニューバー 
  // ファイルメニュー//{{{
  @FXML private Menu fileMenu;
  @FXML private MenuItem openMenuItem;
  @FXML private Menu openRecentMenu;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem saveAsMenuItem;
  @FXML private MenuItem preferencesMenuItem;
  @FXML private MenuItem quitMenuItem;
  //}}}

  // 中央のレイ・アウト
  @FXML private SplitPane splitPane;
  // ファイルリスト//{{{
  @FXML private TitledPane fileListTitledPane;
  @FXML private ListView<File> fileListView;
  @FXML private Button clearImagesButton;
  @FXML private Button reloadButton;
  @FXML private Button deleteListButton;
  @FXML private Button clearListButton;
  //}}}
  // パネル操作変更//{{{
  @FXML private TitledPane controlPanelTitledPane;

  @FXML private ToggleGroup toggleGroup;
  @FXML private RadioButton deleteModeRadioButton;
  @FXML private RadioButton deleteNonEmptyModeRadioButton;
  @FXML private RadioButton sortModeRadioButton;
  @FXML private RadioButton reverseModeRadioButton;
  //}}}
  // 出力画像パネル//{{{
  @FXML private TitledPane outputImageTitledPane;
  @FXML private GridPane outputImageGridPane;
  //}}}

  // 初期化処理
  @FXML private void initialize() {//{{{
    outputImagePane = new OutputImagePane(outputImageGridPane);

    // イベント登録{{{
    deleteModeRadioButton.setOnAction(e -> {//{{{
      strategy = new DeleteStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;//}}}
    deleteNonEmptyModeRadioButton.setOnAction(e -> {//{{{
      strategy = new DeleteNonEmptyStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;//}}}
    sortModeRadioButton.setOnAction(e -> {//{{{
      strategy = new SortStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;//}}}
    reverseModeRadioButton.setOnAction(e -> {//{{{
      strategy = new ReverseStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;//}}}
    fileListView.getSelectionModel().selectedItemProperty().addListener(item -> {//{{{
      drawSelectedFile();
    });//}}}
    //}}}

    File logDir = new File("log");
    logDir.mkdirs();

    // 最近開いたファイルの読み込み
    File logFile = new File("log/recent.log");
    if (logFile.exists()) {
      Path path = logFile.toPath();
      try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
        br.lines().forEach(line -> {
          MenuItem item = new MenuItem(line);
          openRecentMenu.getItems().add(item);

          item.setOnAction(e -> {
            MyFile myFile = new MyFile(item.getText());
            fileListView.getItems().add(myFile);
          });
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

    // TEST_CODE
    // プリセットファイルから出力画像のレイ・アウトを変更する。//{{{
    prop = new Properties();
    try (InputStream in = new FileInputStream(new File("presets/mv.properties"))) {
      prop.load(new InputStreamReader(in, "UTF-8"));
      outputImagePane.changeGridCells();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //}}}
  }//}}}

  // FXMLイベントメソッド
  // メニューバー 
  @FXML private void openMenuItemOnAction() {//{{{
    FileChooserManager fcm = new FileChooserManager("Image Files", "*.png");
    Optional<List<File>> filesOpt = fcm.openFiles();
    filesOpt.ifPresent(files -> {
      for (File file : files) {
        MyFile myFile = new MyFile(file.getPath());
        fileListView.getItems().add(myFile);
      }
      writeOpenRecentLog(files);
    });
  }//}}}
  @FXML private void writeOpenRecentLog(List<File> files) {//{{{
    File logFile = new File("log/recent.log");

    Path path = logFile.toPath();
    try (BufferedWriter br = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
      for (File file : files) {
        String text = file.getAbsolutePath();
        text = text.replaceAll("\\\\", "\\\\\\\\");
        br.write(text + System.lineSeparator());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}
  @FXML private void quitMenuItemOnAction() {//{{{
    Platform.exit();
  }//}}}

  // ファイルリスト
  @FXML private void clearImagesButtonOnAction() {//{{{
  }//}}}
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
    }
  }//}}}
  @FXML private void deleteListButtonOnAction() {//{{{
    ObservableList<File> selectedItems = fileListView.getSelectionModel().getSelectedItems();
    fileListView.getItems().removeAll(selectedItems);

    if (fileListView.getItems().isEmpty()) {
      OutputImagePane.clearImages();
    }
  }//}}}
  @FXML private void clearListButtonOnAction() {//{{{
    fileListView.getItems().clear();
    OutputImagePane.clearImages();
  }//}}}
  @FXML private void fileListViewOnDragOver(DragEvent event) {//{{{
    System.out.println("dragover.");
  }//}}}
  @FXML private void fileListViewOnDragDropped(DragEvent event) {//{{{
    System.out.println("dragover.");
  }//}}}

  // Setter
  void setDividerPosition(double position) {//{{{
    splitPane.setDividerPosition(0, position);
  }//}}}
}
