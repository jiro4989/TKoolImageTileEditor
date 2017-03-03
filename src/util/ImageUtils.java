package util;

import java.nio.IntBuffer;
import javafx.scene.image.*;

/**
 * 画像処理用のユーティリティクラス。
 */
public class ImageUtils {

  // 画像処理の書式(ARGB)
  private static final WritablePixelFormat<IntBuffer> FORMAT = WritablePixelFormat.getIntArgbInstance();

  /**
   * 対象画像がらpixel画素配列を取得する。
   *
   * @param image 対象画像
   * @param width 読み取る幅
   * @param height 読み取る高さ
   * @return 対象画像のint配列
   */
  public static int[] readPixels(Image image, int width, int height) {//{{{

    int[] pixels = new int[width * height];
    PixelReader reader = image.getPixelReader();
    reader.getPixels(0, 0, width, height, FORMAT, pixels, 0, width);
    return pixels;

  }//}}}

  /**
   * 対象画像がらpixel画素配列を取得する。
   *
   * @param image 対象画像
   * @return 対象画像のint配列
   */
  public static int[] readPixels(Image image) {//{{{

    int width  = (int) image.getWidth();
    int height = (int) image.getHeight();
    return readPixels(image, width, height);

  }//}}}

  /**
   * 渡したpixel画素から左右反転したpixel画素を新たに生成して返す。
   *
   * @param pixels 左右反転させる画素
   * @param width 画像の幅
   * @return 左右反転された新たな画像
   */
  public static int[] createReversedPixels(int[] pixels, int width) {//{{{

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
   * @param x x座標
   * @param y y座標
   * @param width 画像の横幅
   * @param height 画像の縦幅
   * @param pixels 書き込むpixel画素
   * @return pixelが書き込まれた新しい画像
   */
  public static WritableImage writePixels(int x, int y, int width, int height, int[] pixels) {//{{{

    WritableImage wImage = new WritableImage(width, height);
    PixelWriter writer   = wImage.getPixelWriter();
    writer.setPixels(x, y, width, height, FORMAT, pixels, 0, width);
    return wImage;

  }//}}}

  /**
   * 渡したpixel配列を書き込んだ新しい画像を返す。
   * 渡した画像の書き込む位置は x=0, y=0 からとなる。
   *
   * @param width 画像の横幅
   * @param height 画像の縦幅
   * @param pixels 書き込むpixel画素
   * @return pixelが書き込まれた新しい画像
   */
  public static WritableImage writePixels(int width, int height, int[] pixels) {//{{{

    return writePixels(0, 0, width, height, pixels);

  }//}}}

}
