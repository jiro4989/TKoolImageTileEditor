package app.image;

import java.util.List;
import javafx.scene.image.WritableImage;

public class DeleteStrategy implements ControlOutputPaneStrategy {
  /**
   * 選択したパネルの画像を削除する。
   */
  @Override
  public void invoke(List<StackImageView> list) {
    list.get(0).setImage(new WritableImage(144, 144));
    list.clear();
    OutputImagePane.clearSelectedStackImageView();
  }
}
