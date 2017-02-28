package app.strategy;

import app.StackImageView;
import java.util.List;

/**
 * 出力画像パネルに対する操作を定義した戦略インタフェース
 */
public interface ControlOutputPaneStrategy {
  /**
   * 操作
   *
   * @param list 選択した
   */
  void invoke(List<StackImageView> list);
}
