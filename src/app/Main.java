package app;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.ResourceBundleWithUtf8;

public class Main extends Application {
  public static final String TITLE = "TKoolFacetileEditor";
  private MainController mainController;

  @Override
  public void start(Stage primaryStage) {
    URL location = getClass().getResource("Main.fxml");
    ResourceBundle resources = ResourceBundle.getBundle(
        "app.res.langs.Main"
        , Locale.getDefault()
        , ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL
        );
    FXMLLoader loader = new FXMLLoader(location, resources);

    double width = 952.0;
    double height = 565.0;
    double dividerPosition = 0.366;
    try (InputStream is = new FileInputStream(new File("properties/Preferences.properties"))) {
      Properties prop = new Properties();
      prop.load(new InputStreamReader(is, "UTF-8"));
      width = Double.parseDouble(prop.getProperty("primaryStage.width"));
      height = Double.parseDouble(prop.getProperty("primaryStage.height"));
      dividerPosition = Double.parseDouble(prop.getProperty("splitPane.divider.pos"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      VBox root = (VBox) loader.load();
      mainController = (MainController) loader.getController();
      Scene scene = new Scene(root, width, height);
      scene.getStylesheets().add(getClass().getResource("res/css/Basic.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle(TITLE);
      primaryStage.setMinWidth(480);
      primaryStage.setMinHeight(270);

      primaryStage.show();
      mainController.setDividerPosition(dividerPosition);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String... args) {
    launch(args);
  }
}
