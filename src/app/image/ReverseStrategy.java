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
    PixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
    int offset = 0;
    list.stream()
      .forEach(siv -> {
        Image image = siv.getImage();
        int width  = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage reversedImage = new WritableImage(width, height);

        PixelReader reader  = image.getPixelReader();
        PixelWriter writer  = reversedImage.getPixelWriter();

        int scanlineStride = height;
        for (int x=width-1, y = 0, w = 1, h = height; 0<=x; x--) {
          int[] buffer = new int[height];
          reader.getPixels(x, y, w, h, format, buffer, offset, scanlineStride);
        }
        writer.setPixel();
      });
  }
}

