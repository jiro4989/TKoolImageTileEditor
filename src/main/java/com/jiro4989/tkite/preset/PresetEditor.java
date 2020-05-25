package com.jiro4989.tkite.preset;

import com.jiro4989.tkite.util.MyProperties;
import com.jiro4989.tkite.util.ResourceBundleWithUtf8;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class PresetEditor extends Stage {

  private PresetEditorController controller;

  public PresetEditor(File presetFile, File previewFile) { // {{{

    URL location = getClass().getResource("preset_editor.fxml");
    ResourceBundle resources =
        ResourceBundle.getBundle(
            "com.jiro4989.tkite.res.langs.preset_editor",
            Locale.getDefault(),
            ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL);
    FXMLLoader loader = new FXMLLoader(location, resources);

    try {

      GridPane root = (GridPane) loader.load();
      controller = (PresetEditorController) loader.getController();

      final int WIDTH = 1000;
      final int HEIGHT = 600;
      Scene scene = new Scene(root, WIDTH, HEIGHT);
      scene
          .getStylesheets()
          .add(getClass().getResource("/com/jiro4989/tkite/res/css/basic.css").toExternalForm());

      setScene(scene);
      setMinWidth(WIDTH);
      setMinHeight(HEIGHT);
      setTitle(resources.getString("title"));
      initStyle(StageStyle.UTILITY);
      initModality(Modality.APPLICATION_MODAL);
      setOnCloseRequest(
          e -> {
            controller.closeRequest();
          });

      MyProperties mp = new MyProperties("properties/preview_editor.xml");
      if (mp.load()) mp.customStage(this);

      controller.setPresetFile(presetFile);
      if (previewFile != null) controller.setPreviewImage(previewFile);

    } catch (IOException e) {
      e.printStackTrace();
    }
  } // }}}

  public PresetEditor(File presetFile) { // {{{

    this(presetFile, null);
  } // }}}
}
