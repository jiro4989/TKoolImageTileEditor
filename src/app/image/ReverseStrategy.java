package app.image;

import java.nio.IntBuffer;
import java.util.List;
import java.util.stream.IntStream;
import javafx.scene.image.*;

public class ReverseStrategy implements ControlOutputPaneStrategy {
  // 画像処理の書式(ARGB)
  private static final WritablePixelFormat<IntBuffer> FORMAT = WritablePixelFormat.getIntArgbInstance();

  /**
   * 選択したペインの画像を左右反転する。
   *
   * @param list 選択状態にあるStackViewPaneのリスト
   */
  @Override
  public void invoke(List<StackImageView> list) {
    list.stream()
      .forEach(siv -> {
        Image image = siv.getImage();
        int width   = (int) image.getWidth();
        int height  = (int) image.getHeight();

        int[] pixels         = readPixels(image, width, height);
        int[] reversedPixels = createReversedPixels(pixels, width);
        WritableImage wImage = writePixels(reversedPixels, width, height);

        siv.setImage(wImage);

      });
    list.clear();
    OutputImagePane.clearSelectedStackImageView();
  }

  /**
   * 対象画像がらpixel画素配列を取得する。
   *
   * @param image 対象画像
   * @return 対象画像のint配列
   */
  private int[] readPixels(Image image, int width, int height) {//{{{
    int[] pixels = new int[width * height];
    PixelReader reader = image.getPixelReader();
    reader.getPixels(0, 0, width, height, FORMAT, pixels, 0, width);
    return pixels;
  }//}}}
  /**
   * 渡したpixel画素から左右反転したpixel画素を新たに生成して返す。
   *
   * @param pixels 左右反転させる画素
   * @param width 画像の幅
   * @return 左右反転された新たな画像
   */
  private int[] createReversedPixels(int[] pixels, int width) {//{{{
    int[] reversedPixels = new int[pixels.length];
    for (int i=0; i<pixels.length; i++) {
      int a = (i + width) / width * width;
      int b = i / width * width;
      int reverseIndex = a + b - i - 1;
      reversedPixels[reverseIndex] = pixels[i];
    }
    return reversedPixels;
  }//}}}
  /**
   * 渡したpixel配列を書き込んだ新しい画像を返す。
   *
   * @param pixels 書き込むpixel画素
   * @param width 画像の横幅
   * @param height 画像の縦幅
   * @return pixelが書き込まれた新しい画像
   */
  private WritableImage writePixels(int[] pixels, int width, int height) {//{{{
    WritableImage wImage = new WritableImage(width, height);
    PixelWriter writer   = wImage.getPixelWriter();
    writer.setPixels(0, 0, width, height, FORMAT, pixels, 0, width);
    return wImage;
  }//}}}
}

