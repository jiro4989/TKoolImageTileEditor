package com.jiro4989.tkite;

import java.io.File;

/** リストビューで扱う拡張Fileクラス。リストビューで表示されるテキストを変更する 。 */
public class MyFile extends File {

  MyFile(File file) {
    super(file.getPath());
  }

  MyFile(String path) {
    super(path);
  }

  /**
   * リストビューに表示されるテキストを変更する。
   *
   * @return ファイル名
   */
  @Override
  public String toString() {
    return super.getName();
  }
}
