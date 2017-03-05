package jiro.javafx.stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.image.*;

public class AboutStageController {

  // FXML Component//{{{

  @FXML private ImageView appIconImageView;

  @FXML private Label appDescLabel;
  @FXML private Label authorDescLabel;
  @FXML private Label blogDescLabel;

  @FXML private Label appLabel;
  @FXML private Hyperlink blogHyperlink;

  @FXML private Button closeButton;

  //}}}

  @FXML private void initialize() {//{{{

    Locale locale = Locale.getDefault();
    if (locale == Locale.JAPAN) {

      appDescLabel    . setText(Langs . JP . APP);
      authorDescLabel . setText(Langs . JP . AUTHOR);
      blogDescLabel   . setText(Langs . JP . BLOG);

    } else {

      appDescLabel    . setText(Langs . EN . APP);
      authorDescLabel . setText(Langs . EN . AUTHOR);
      blogDescLabel   . setText(Langs . EN . BLOG);

    }

  }//}}}

  @FXML private void closeButtonOnAction() {//{{{

    closeButton.getScene().getWindow().hide();

  }//}}}

  @FXML private void blogHyperlinkOnAction() {//{{{

    Desktop desktop = Desktop.getDesktop();
    try {

      URI uri = new URI(blogHyperlink.getText());
      desktop.browse(uri);

    } catch (URISyntaxException e) {

      e.printStackTrace();

    } catch (IOException e) {

      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setHeaderText("関連付けられたブラウザがありません。");
      alert.setContentText("お手数ですが、リンクをコピーして手動で移動してください。");
      alert.showAndWait();

    }

  }//}}}

  void setAppName(String appName) {//{{{

    appLabel.setText(appName);

  }//}}}

}
