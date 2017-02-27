package app;

import java.io.*;
import java.util.stream.IntStream;
import java.util.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainController {
  // FXMLで指定するコンポーネント{{{
  @FXML private MenuItem fileMenuItem;
  @FXML private TitledPane outputImageTitledPane;
  @FXML private GridPane outputImageGridPane;
  //}}}

  @FXML
  private void initialize() {//{{{
    // TEST_CODE//{{{
    Properties prop = new Properties();
    try (InputStream is = new FileInputStream(new File("presets/MV.properties"))) {
      prop.load(new InputStreamReader(is, "UTF-8"));

      String strRow  = prop.getProperty("row");
      String strCol  = prop.getProperty("column");
      String strSize = prop.getProperty("size");

      int row    = Integer.parseInt(strRow);
      int column = Integer.parseInt(strCol);
      int size   = Integer.parseInt(strSize);
      int count  = row * column;

      double gridWidth  = (double) (size * column);
      double gridHeight = (double) (size * row);
      outputImageGridPane.setPrefSize(gridWidth, gridHeight);

      IntStream.range(0, count)
        .forEach(i -> {
          final Button button = new Button("" + (i+1));
          double width = (double) size;
          button.setPrefWidth(width);
          button.setPrefHeight(width);

          int c = i % column;
          int r = i / column;
          outputImageGridPane.add(button, c, r);
        });

    } catch (IOException e) {
      e.printStackTrace();
    }
    //}}}
  }//}}}
}
