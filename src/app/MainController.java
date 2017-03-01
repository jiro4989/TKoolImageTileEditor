package app;

import util.RecentFiles;
import app.image.*;
import jiro.lib.javafx.stage.FileChooserManager;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainController {

  // 環境設定
  public static ImageStandard imageStandard;

  // 出力画像ペインのクリック時の動作を決定するインスタンス
  public static ControlOutputPaneStrategy strategy = new DeleteStrategy();

  // 出力画像パネル
  private OutputImagePane outputImagePane;

  // FXMLで指定するコンポーネント
  // メニューバー 

  // ファイルメニュー//{{{
  @FXML private Menu fileMenu;
  @FXML private MenuItem openMenuItem;
  @FXML private Menu openRecentMenu;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem saveAsMenuItem;
  @FXML private MenuItem newPresetMenuItem;
  @FXML private MenuItem editPresetMenuItem;
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

    // モード切替のラジオボタン
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
        });

      });
    });
    //}}}

    imageStandard = new ImageStandard("vxace.properties");
    outputImagePane.changeGridCells();

  }//}}}

  /**
   * 最近開いたファイルをOpenRecentMenuに登録する。 log/recent.logから１行ずつの
   * ファイルパスとして取得する。ファイルが存在しなかった場合はセットしない。ま
   * た、
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
  // メニューバー 
  @FXML private void openMenuItemOnAction() {//{{{
    FileChooserManager fcm = new FileChooserManager("Image Files", "*.png");
    Optional<List<File>> filesOpt = fcm.openFiles();
    filesOpt.ifPresent(files -> {
      for (File file : files) {

        MyFile myFile = new MyFile(file.getPath());
        fileListView.getItems().add(myFile);

        String path = file.getAbsolutePath();
        path = path.replaceAll("\\\\", "/");

        MenuItem item = new MenuItem(path);
        openRecentMenu.getItems().add(0, item);

      }
    });
  }//}}}
  @FXML private void newPresetMenuItemOnAction() {//{{{
  }//}}}
  @FXML private void editPresetMenuItemOnAction() {//{{{
  }//}}}
  @FXML private void quitMenuItemOnAction() {//{{{

    ObservableList<MenuItem> recentFiles = openRecentMenu.getItems();
    RecentFiles.writeRecentOpenedFile(recentFiles);

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
