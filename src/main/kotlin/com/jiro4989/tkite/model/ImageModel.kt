package com.jiro4989.tkite.model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.image.WritablePixelFormat
import java.nio.IntBuffer

private val format: WritablePixelFormat<IntBuffer> = WritablePixelFormat.getIntArgbInstance()

internal fun createFlippedPixels(srcPixels: IntArray, width: Int): IntArray {
    // PixelはIntの1次元配列。
    // 例えば4行3列のPixelは以下のようになっている。
    // [
    //   1, 2, 3,
    //   4, 5, 6,
    //   7, 8, 9,
    // ]
    //
    // この配列を左右反転するには、以下のような配置にする必要がある。
    // [
    //   3, 2, 1,
    //   6, 5, 4,
    //   9, 8, 7,
    // ]
    val dstPixels = IntArray(srcPixels.size)
    (0 until dstPixels.size).forEach { i ->
        val a = (i + width) / width * width;
        val b = i / width * width;
        val flippedIndex = a + b - i - 1;
        dstPixels[flippedIndex] = srcPixels[i];
    }
    return dstPixels
}

class ImageModel(private val width: Int, private val height: Int) {
    private val image: Image

    init {
        image = WritableImage(width, height)
    }

    fun flipImage() {
        val reader = image.pixelReader
        val srcPixels = IntArray(width * height)
        reader.getPixels(0, 0, width, height, format, srcPixels, 0, width);

        val dstPixels = createFlippedPixels(srcPixels, width)
        if (image is WritableImage) {
            val writer = image.pixelWriter
            writer.setPixels(0, 0, width, height, format, dstPixels, 0, width);
        }
    }
}