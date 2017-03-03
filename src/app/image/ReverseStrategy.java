package app.image;

import static util.ImageUtils.*;

import java.nio.IntBuffer;
import java.util.List;
import java.util.stream.IntStream;
import javafx.scene.image.*;

public class ReverseStrategy implements ControlOutputPaneStrategy {

  /**
   * 選択したペインの画像を左右反転する。
   *
   * @param list 選択状態にあるStackViewPaneのリスト
   */
  @Override
  public void invoke(List<StackImageView> list) {

    list.stream().forEach(siv -> {

      Image image = siv.getImage();
      int width   = (int) image.getWidth();
      int height  = (int) image.getHeight();

      int[] pixels         = readPixels(image, width, height);
      int[] reversedPixels = createReversedPixels(pixels, width);
      WritableImage wImage = writePixels(width, height, reversedPixels);

      siv.setImage(wImage);

    });

    list.clear();
    OutputImagePane.clearSelectedStackImageView();

  }

}

