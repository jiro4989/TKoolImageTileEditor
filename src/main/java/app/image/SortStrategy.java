package app.image;

import java.util.List;
import javafx.scene.image.Image;

public class SortStrategy implements ControlOutputPaneStrategy {
  /** 画像を２つ選択すると選択した画像と位置を交換する。 */
  @Override
  public void invoke(List<StackImageView> list) {
    if (2 <= list.size()) {

      Image image1 = list.get(0).getImage();
      Image image2 = list.get(1).getImage();
      Image tmpImage = null;

      tmpImage = image1;
      image1 = image2;
      image2 = tmpImage;

      list.get(0).setImage(image1);
      list.get(1).setImage(image2);

      list.clear();
      OutputImagePane.clearSelectedStackImageView();
    }
  }
}
