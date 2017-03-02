package app.preset;

import util.ResourceBundleWithUtf8;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class PresetEditor extends Stage {

  private PresetEditorController myController;

  public PresetEditor() {

    URL location = getClass().getResource("preset_editor.fxml");
    ResourceBundle resources = ResourceBundle.getBundle(
        "app.res.langs.preset_editor"
        , Locale.getDefault()
        , ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL
        );
    FXMLLoader loader = new FXMLLoader(location, resources);

    try {

      GridPane root = (GridPane) loader.load();
      myController = (PresetEditorController) loader.getController();

      Scene scene = new Scene(root, 1000, 450);
      scene.getStylesheets().add(getClass().getResource("/app/res/css/basic.css").toExternalForm());
      setScene(scene);

      setTitle(resources.getString("title"));
      initStyle(StageStyle.UTILITY);
      initModality(Modality.APPLICATION_MODAL);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
