package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.IntBuffer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javax.imageio.ImageIO;

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
   */
  public static void writePixels(WritableImage wImage, int x, int y, int width , int height, int[] pixels) {//{{{

    PixelWriter writer = wImage.getPixelWriter();
    writer.setPixels(x, y, width, height, FORMAT, pixels, 0, width);

  }//}}}

  /**
   * 渡したpixel配列を書き込んだ新しい画像を返す。
   * 座標はx=0, y=0から書き込む。
   *
   * @param wImage 書き込む対象
   * @param width ピクセル横幅
   * @param height ピクセル縦幅
   * @param pixels 書き込むピクセル画素
   */
  public static void writePixels(WritableImage wImage, int width , int height, int[] pixels) {//{{{

    PixelWriter writer = wImage.getPixelWriter();
    writer.setPixels(0, 0, width, height, FORMAT, pixels, 0, width);

  }//}}}

  /**
   * 画像をファイル出力し、成否を返す。
   *
   * @param image 出力画像
   * @param outputFile 出力するファイル
   * @return 成否
   */
  public static boolean write(Image image, File outputFile) {//{{{

    BufferedImage newImage = SwingFXUtils.fromFXImage(image, null);
    try {

      ImageIO.write(newImage, "png", outputFile);
      return true;

    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;

  }//}}}

  /**
   * 画像ファイルのリストから連結した一つの画像を生成し返す。
   * 連結する画像の幅は、それぞれ渡されたリストの先頭の要素を基準とする。
   *
   * @param imageList 画像のリスト
   * @return 連結した画像
   */
  public static Image joinImageFiles(List<File> imageList) {//{{{

    // 連結する画像をフィルタリングするための基準値を取得
    File imageFile = imageList.get(0);
    Image tmpImage = new Image("file:" + imageFile.getPath());
    int width  = (int) tmpImage.getWidth();
    int height = (int) tmpImage.getHeight();

    // 基準値と等しい横幅、縦幅の画像のみを抽出したリストを生成
    List<Image> filteredImageList = imageList.stream()
      .map(f -> new Image("file:" + f.getPath()))
      .filter(img -> {

        int w = (int) img.getWidth();
        int h = (int) img.getHeight();
        if (w == width && h == height) {
          return true;
        }

        return false;

      })
    .collect(Collectors.toList());

    // 連結画像書き込み用のイメージの生成
    int count = filteredImageList.size();
    WritableImage joinedImage = new WritableImage(width, height * count);

    // 画像の連結
    AtomicInteger atom = new AtomicInteger(0);
    filteredImageList.stream().forEach(img -> {

      int i = atom.getAndIncrement();
      int x = 0;
      int y = height * i;
      int[] pixels = readPixels(img);
      writePixels(joinedImage, x, y, width, height, pixels);

    });

    return joinedImage;

  }//}}}

}
