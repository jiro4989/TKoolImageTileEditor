package jiro.javafx.stage;

import java.io.IOException;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class AboutStage extends Stage {

  public AboutStage(String title, String version, String css) {//{{{

    FXMLLoader loader = new FXMLLoader(getClass().getResource("about_stage.fxml"));
    try {

      BorderPane root = (BorderPane) loader.load();
      Scene scene = new Scene(root);
      scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
      setScene(scene);

      setResizable(false);
      initModality(Modality.APPLICATION_MODAL);
      initStyle(StageStyle.UTILITY);
      changeTitle();
      sizeToScene();

      AboutStageController controller = (AboutStageController) loader.getController();
      controller.setAppName(title + " - " + version);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }//}}}

  private void changeTitle() {//{{{

    Locale locale = Locale.getDefault();
    Langs lang = locale == Locale.JAPAN ? Langs.JP : Langs.EN;
    setTitle(lang.STAGE_TITLE);

  }//}}}

}
