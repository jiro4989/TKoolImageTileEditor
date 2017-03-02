package app.preset;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class PresetEditor extends Stage {

  private PresetEditorController myController;

  public PresetEditor() {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("preset_editor.fxml"));
    try {

      GridPane root = (GridPane) loader.load();
      myController = (PresetEditorController) loader.getController();

      Scene scene = new Scene(root, 600, 450);
      scene.getStylesheets().add(getClass().getResource("/app/res/css/basic.css").toExternalForm());
      setScene(scene);

      initStyle(StageStyle.UTILITY);
      initModality(Modality.APPLICATION_MODAL);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
