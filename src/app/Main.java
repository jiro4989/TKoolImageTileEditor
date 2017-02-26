package app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.ResourceBundleWithUtf8;

public class Main extends Application {
  public static final String TITLE = "TKoolFacetileEditor";
  @Override
  public void start(Stage primaryStage) {
    URL location = getClass().getResource("Main.fxml");
    ResourceBundle resources = ResourceBundle.getBundle(
        "app.res.langs.Main"
        , Locale.getDefault()
        , ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL
        );
    FXMLLoader loader = new FXMLLoader(location, resources);

    try {
      VBox root = (VBox) loader.load();
      Scene scene = new Scene(root, 200, 300);
      scene.getStylesheets().add(getClass().getResource("res/css/Basic.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle(TITLE);

      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String... args) {
    launch(args);
  }
}
