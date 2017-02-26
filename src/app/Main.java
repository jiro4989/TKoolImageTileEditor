package app;

import javafx.application.Application;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
    try {
      VBox root = (VBox) loader.load();
      Scene scene = new Scene(root, 200, 300);
      scene.getStylesheets().add(getClass().getResource("./res/Basic.css").toExternalForm());
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
