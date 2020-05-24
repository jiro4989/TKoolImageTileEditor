package app.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javax.imageio.ImageIO;

/** Imageクラスをラップしたクラス。 */
class MyImage {

  /** 画像 */
  public final Image image;

  /** 横幅 */
  public final int width;

  /** 縦幅 */
  public final int height;

  // 画像処理の書式(ARGB)
  private static final WritablePixelFormat<IntBuffer> FORMAT =
      WritablePixelFormat.getIntArgbInstance();

  // コンストラクタ

  public MyImage(File file) { // {{{

    this(new Image("file:" + file.getPath()));
  } // }}}

  public MyImage(Image img) { // {{{

    image = img;
    width = (int) image.getWidth();
    height = (int) image.getHeight();
  } // }}}

  // メソッド

  public MyImage toReversedImage() { // {{{

    MyImage myimg = new MyImage(image);
    int[] pixels = myimg.getPixels();

    int[] newPixels = new int[pixels.length];
    for (int i = 0; i < pixels.length; i++) {

      int a = (i + width) / width * width;
      int b = i / width * width;
      int reverseIndex = a + b - i - 1;
      newPixels[reverseIndex] = pixels[i];
    }

    WritableImage wImage = new WritableImage(width, height);
    PixelWriter writer = wImage.getPixelWriter();
    writer.setPixels(0, 0, width, height, FORMAT, newPixels, 0, width);

    return new MyImage(wImage);
  } // }}}

  public MyImage write(int x, int y, Image img) { // {{{

    return write(x, y, new MyImage(img));
  } // }}}

  public MyImage write(int x, int y, MyImage myimg) { // {{{

    int w = myimg.width;
    int h = myimg.height;
    int[] pixels = myimg.getPixels();

    WritableImage newImage = new WritableImage(width, height);
    PixelWriter writer = newImage.getPixelWriter();
    writer.setPixels(x, y, w, h, FORMAT, pixels, 0, w);

    return new MyImage(newImage);
  } // }}}

  public boolean output(File outputFile) { // {{{

    BufferedImage newImage = SwingFXUtils.fromFXImage(image, null);
    try {

      ImageIO.write(newImage, "png", outputFile);
      return true;

    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  } // }}}

  public MyImage toExpand(int widthRate, int heightRate) { // {{{

    WritableImage newImage = new WritableImage(width * widthRate, height * heightRate);
    PixelWriter writer = newImage.getPixelWriter();
    int[] pixels = getPixels();
    writer.setPixels(0, 0, width, height, FORMAT, pixels, 0, width);

    return new MyImage(newImage);
  } // }}}

  private int[] getPixels() { // {{{

    PixelReader reader = image.getPixelReader();
    int[] pixels = new int[width * height];
    reader.getPixels(0, 0, width, height, FORMAT, pixels, 0, width);

    return pixels;
  } // }}}
}
