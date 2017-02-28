package app;

import java.io.*;
import java.util.*;
import java.util.Properties;
import java.util.stream.IntStream;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.application.Platform;

public class MainController {
  public static Properties prop;
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
  @FXML private Button sortExecuteButton;
  @FXML private RadioButton reverseModeRadioButton;
  @FXML private Button reverseExecuteButton;
  //}}}
  // 出力画像パネル//{{{
  @FXML private TitledPane outputImageTitledPane;
  @FXML private GridPane outputImageGridPane;
  //}}}
  //}}}
  //}}}
  // 出力画像パネル
  private OutputImagePane outputImagePane;

  @FXML private void initialize() {//{{{
    outputImagePane = new OutputImagePane(outputImageGridPane);
    // TEST_CODE//{{{
    // ファイルをリストビューに追加する//{{{
    File file1 = new MyFile("input/Actor1.png");
    File file2 = new MyFile("input/Actor2.png");
    File file3 = new MyFile("input/Actor3.png");

    fileListView.getItems().add(file1);
    fileListView.getItems().add(file2);
    fileListView.getItems().add(file3);

    //}}}
    // プリセットファイルから出力画像のレイ・アウトを変更する。//{{{
    prop = new Properties();
    try (InputStream is = new FileInputStream(new File("presets/MV.properties"))) {
      prop.load(new InputStreamReader(is, "UTF-8"));
      outputImagePane.changeGridCells();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //}}}
    //}}}
  }//}}}
  // イベントメソッド//{{{
  // メニューバー//{{{
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
