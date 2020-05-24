package app.image;

import app.MainController;
import java.util.List;
import javafx.scene.image.Image;

public class DeleteStrategy implements ControlOutputPaneStrategy {
  /** 選択したパネルの画像を削除する。 */
  @Override
  public void invoke(List<StackImageView> list) {

    int size = MainController.imageStandard.size;
    final Image emptyImage = MainController.imageStandard.emptyImage;
    list.get(0).setImage(emptyImage);
    list.clear();
    OutputImagePane.clearSelectedStackImageView();
  }
}
