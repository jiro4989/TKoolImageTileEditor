package com.jiro4989.tkite.util;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;

/** 最近開いたファイルを操作するためのユーティリティクラス。 */
public class RecentFiles {
  private static final File DIR = new File("log");
  private static final File LOG_FILE = new File("log/recent.log");
  private static final int MAX = 20;

  /**
   * 最近開いたファイルのMenuItemのSetを返却する。 この時、logフォルダが存在しなかった場合は作成する。また、ファイルが存在しな
   * かった場合は、空のファイルを作成し、Optional.empty()を返却する。
   *
   * @return 最近開いたファイルのMenuItem
   */
  public static Optional<List<MenuItem>> createRecentOpenedMenuItems() { // {{{

    DIR.mkdirs();

    if (LOG_FILE.exists()) {

      // recent.logファイルが存在した場合は、１行ずつファイルパスを取得し、ファ
      // イルパスをテキストとして保持するMenuItemに変換したLinkedHashSet(重複な
      // しコレクション)に変換して返却する。
      Path path = LOG_FILE.toPath();
      try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {

        List<MenuItem> list =
            br.lines()
                .distinct()
                .filter(f -> new File(f).exists())
                .map(MenuItem::new)
                .collect(Collectors.toList());
        return Optional.ofNullable(list);

      } catch (IOException e) {
        e.printStackTrace();
      }

    } else {

      // ファイルが存在しなかった場合はからのファイルを生成し、Optional.empty()
      // を返す。
      try {
        LOG_FILE.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return Optional.empty();
  } // }}}

  /** メニューから最近開いたファイルの情報を更新する。 */
  public static void writeRecentOpenedFile(ObservableList<MenuItem> recentList) { // {{{

    Path path = LOG_FILE.toPath();
    try (BufferedWriter br =
        Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {

      List<String> list =
          recentList.stream()
              .map(item -> item.getText())
              .map(text -> text.replaceAll("\\\\", "/"))
              .distinct()
              .collect(Collectors.toList());

      int i = 1;
      for (String text : list) {
        br.write(text + System.lineSeparator());
        if (MAX <= i) break;
        i++;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  } // }}}
}
