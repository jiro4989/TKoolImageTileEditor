package app.image;

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
    // TODO
    list.stream()
      .forEach(siv -> {
        Image image        = siv.getImage();
        PixelReader reader = image.getPixelReader();

        int width  = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage wImage = new WritableImage(width, height);
        PixelWriter writer   = wImage.getPixelWriter();

        WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
        int[] pixels = new int[width * height];
        reader.getPixels(0, 0, width, height, format, pixels, 0, width);
      });
  }
}
