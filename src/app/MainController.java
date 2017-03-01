package app;

import jiro.lib.javafx.stage.FileChooserManager;
import app.image.*;
import java.io.*;
import java.util.*;
import java.util.stream.IntStream;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.application.Platform;

public class MainController {
  // 環境設定
  public static Properties prop;
  // 出力画像ペインのクリック時の動作を決定するインスタンス
  public static ControlOutputPaneStrategy strategy = new DeleteStrategy();
  // 出力画像パネル
  private OutputImagePane outputImagePane;
  // FXMLで指定するコンポーネント{{{
  // メニューバー {{{
  // ファイルメニュー//{{{
  @FXML private Menu fileMenu;
  @FXML private MenuItem openMenuItem;
  @FXML private Menu openRecentMenu;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem saveAsMenuItem;
  @FXML private MenuItem preferencesMenuItem;
  @FXML private MenuItem quitMenuItem;
  //}}}
  //}}}
  // 中央のレイ・アウト//{{{
  @FXML private SplitPane splitPane;
  // ファイルリスト//{{{
  @FXML private TitledPane fileListTitledPane;
  @FXML private ListView<File> fileListView;
  @FXML private Button clearImagesButton;
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
  //}}}
  //}}}

  @FXML private void initialize() {//{{{
    outputImagePane = new OutputImagePane(outputImageGridPane);

    // イベント登録{{{
    deleteModeRadioButton.setOnAction(e -> {
      strategy = new DeleteStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;
    deleteNonEmptyModeRadioButton.setOnAction(e -> {
      strategy = new DeleteNonEmptyStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;
    sortModeRadioButton.setOnAction(e -> {
      strategy = new SortStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;
    reverseModeRadioButton.setOnAction(e -> {
      strategy = new ReverseStrategy();
      OutputImagePane.clearSelectedStackImageView();
    }) ;
    //}}}
    // TEST_CODE//{{{
    // プリセットファイルから出力画像のレイ・アウトを変更する。//{{{
    prop = new Properties();
    try (InputStream is = new FileInputStream(new File("presets/mv.properties"))) {
      prop.load(new InputStreamReader(is, "UTF-8"));
      outputImagePane.changeGridCells();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //}}}
    //}}}
  }//}}}
  // FXMLイベントメソッド//{{{
  // メニューバー//{{{
  @FXML private void openMenuItemOnAction() {//{{{

    // ファイルをリストビューに追加する//{{{
    File file1 = new MyFile("input/actor1.png");
    File file2 = new MyFile("input/actor2.png");
    File file3 = new MyFile("input/actor3.png");
    File file4 = new MyFile("input/actor4.png");

    fileListView.getItems().add(file1);
    fileListView.getItems().add(file2);
    fileListView.getItems().add(file3);
    fileListView.getItems().add(file4);

    //}}}
  }//}}}
  @FXML private void quitMenuItemOnAction() {//{{{
    Platform.exit();
  }//}}}
  //}}}
  // ファイルリスト//{{{
  @FXML private void clearImagesButtonOnAction() {//{{{
  }//}}}
  @FXML private void deleteListButtonOnAction() {//{{{
    ObservableList<File> selectedItems = fileListView.getSelectionModel().getSelectedItems();
    fileListView.getItems().removeAll(selectedItems);
  }//}}}
  @FXML private void clearListButtonOnAction() {//{{{
    fileListView.getItems().clear();
  }//}}}
  @FXML private void fileListViewOnMouseClicked(MouseEvent event) {//{{{
    SelectionModel<File> model = fileListView.getSelectionModel();
    if (!model.isEmpty()) {
      File imageFile = model.getSelectedItem();
      outputImagePane.setImage(imageFile);
    }
  }//}}}
  @FXML private void fileListViewOnDragOver(DragEvent event) {//{{{
    System.out.println("dragover.");
  }//}}}
  @FXML private void fileListViewOnDragDropped(DragEvent event) {//{{{
    System.out.println("dragover.");
  }//}}}
  //}}}
  //}}}
  void setDividerPosition(double position) {//{{{
    splitPane.setDividerPosition(0, position);
  }//}}}
}
