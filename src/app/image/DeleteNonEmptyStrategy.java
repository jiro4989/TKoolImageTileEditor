package app.image;

import java.util.List;
import java.util.stream.IntStream;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class DeleteNonEmptyStrategy implements ControlOutputPaneStrategy {
  /**
   * 選択した画像を削除し、出来た空白を埋める。
   *
   * @param list 選択状態にあるStackViewPaneのリスト
   */
  @Override
  public void invoke(List<StackImageView> list) {
    list.get(0).setImage(new WritableImage(144, 144));

    // 選択したペイン以降の画像を現在のペインの位置まで移動する。//{{{
    int num = list.get(0).getNumber();
    num--;
    List<StackImageView> siv = OutputImagePane.stackImageViewList;
    IntStream.range(num, siv.size()-1)
      .forEach(i -> {
        Image nextImage = siv.get(i+1).getImage();
        siv.get(i).setImage(nextImage);
      });
    //}}}
    // 末尾のStackImageViewの画像を空白にする//{{{
    int lastIndex = siv.size()-1;
    siv.get(lastIndex).setImage(new WritableImage(144, 144));
    //}}}

    list.clear();
    OutputImagePane.clearSelectedStackImageView();
  }
}
