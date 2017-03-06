package jiro.javafx.stage;

import java.io.IOException;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * <p>About(バージョン情報)ウィンドウクラス。</p>
 *
 * <p>
 * アプリ名、作者名、作者ブログ、アプリアイコン、作者アイコンなどをオプション
 * で表示非表示にするBuilderメソッドを提供する。<br>
 * インスタンス生成はBuilderクラスを経由して行う。
 * </p> <p>
 * また、Hyperlinkをクリックしたときに、関連付けブラウザで開く処理を実装している
 * ため、Builder#blogHyperlink(String)メソッドにURL文字列を渡すだけで、Hyperlink
 * として機能する。
 * </p> <p>
 * この時、メソッドチェーンで各種オプションを変更することができる。
 * </p> <p>
 * AboutStage about = new AbouotStage.Builder(title, version)<br>
 *    .author("author")<br>
 *    .blog("blog")<br>
 *    .build();<br>
 * <br>
 * about.showAndWait();
 * </p> <p>
 * 基本的にAboutウィンドウでは特殊な動作を必要としないので、本クラスを利用する場
 * 合、インスタンス生成時のオプション変更後のウィンドウ呼び出し以外に何かメソッ
 * ドを呼び出したり設定を変更する必要はない。
 * </p>
 */
public class AboutStage extends Stage {

  /**
   * AboutStageのインスタンス生成を行うためのBuilderクラス
   *
   * @param title       アプリ名
   * @param author      作者名
   * @param blog        ブログ名
   * @param blogUrl     ブログURL
   * @param css         StageのレイアウトCSSのURL
   * @param appIcon     アプリのアイコンURL
   * @param authorIcon  作者のアイコンURL
   */
  public static class Builder {//{{{

    private final String title;
    private String author     = null;
    private String blog       = null;
    private String blogUrl    = null;
    private String css        = null;
    private String appIcon    = null;
    private String authorIcon = null;

    /**
     * <p>Builderインスタンス生成</p>
     *
     * @param aTitle    アプリ名         ( null, 空文字列不可 )
     * @param aVersion  アプリバージョン ( null, 空文字列不可 )
     * @return Builderインスタンス
     */
    public Builder(String aTitle, String aVersion) {//{{{

      if (aTitle == null || aVersion == null)
        throw new NullPointerException("aTitle, aVersionにnullを使用することはできません。");

      if ("".equals(aTitle) || "".equals(aVersion))
        throw new IllegalArgumentException("aTitle, aVersionに空文字列を使用することはできません。");

      title = aTitle + " - " + aVersion;
    }//}}}

    /**
     * <p>作者情報のセット</p>
     * @param anAuthor 作者名
     * @return Builderインスタンス
     */
    public Builder author(String anAuthor) {//{{{
      author = anAuthor; return this;
    }//}}}

    /**
     * <p>作者ブログ名のセット</p>
     * @param aBlog 作者ブログ名
     * @return Builderインスタンス
     */
    public Builder blog(String aBlog) {//{{{
      blog = aBlog; return this;
    }//}}}

    /**
     * <p>作者ブログURLのセット</p>
     * @param aBlogUrl 作者ブログ名
     * @return Builderインスタンス
     */
    public Builder blogUrl(String aBlogUrl) {//{{{
      blogUrl = aBlogUrl; return this;
    }//}}}

    /**
     * <p>レイアウト定義用CSSのURL.<br>
     * この時、URL文字列は/appから始めること。
     * </p>
     * @param aCss レイアウト定義用CSSのURL
     * @return Builderインスタンス
     */
    public Builder css(String aCss) {//{{{
      css = aCss; return this;
    }//}}}

    /**
     * <p>アプリアイコン画像のURL.<br>
     * この時、URL文字列は/appから始めること。
     * </p>
     * @param anAppIcon アプリアイコン画像(75x75 px)
     * @return Builderインスタンス
     */
    public Builder appIcon(String anAppIcon) {//{{{
      appIcon = anAppIcon; return this;
    }//}}}

    /**
     * <p>作者アイコン画像のURL.<br>
     * この時、URL文字列は/appから始めること。
     * </p>
     * @param anAuthorIcon 作者アイコン画像(17x17 px)
     * @return Builderインスタンス
     */
    public Builder authorIcon(String anAuthorIcon) {//{{{
      authorIcon = anAuthorIcon; return this;
    }//}}}

    /**
     * <p>AboutStageインスタンスの生成</p>
     * @return AboutStageインスタンス
     */
    public AboutStage build() {//{{{
      return new AboutStage(this);
    }//}}}

  }//}}}

  /**
   * <p> private コンストラクタ.<br>
   * {@link Builder}クラスから呼び出される。
   * </p>
   *
   * @param Builderインスタンス
   */
  private AboutStage(Builder builder) {//{{{

    String title      = builder.title;
    String author     = builder.author;
    String blog       = builder.blog;
    String blogUrl    = builder.blogUrl;
    String css        = builder.css;
    String appIcon    = builder.appIcon;
    String authorIcon = builder.authorIcon;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("about_stage.fxml"));
    try {

      BorderPane root = (BorderPane) loader.load();
      Scene scene = new Scene(root);
      if (css != null) scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
      setScene(scene);

      setResizable(false);
      initModality(Modality.APPLICATION_MODAL);
      initStyle(StageStyle.UTILITY);
      changeTitle();
      sizeToScene();

      AboutStageController controller = (AboutStageController) loader.getController();
      controller.setAppName(title);

      if (author != null) {
        controller.setAuthor(author);
        if (authorIcon != null) controller.setAuthorIcon(authorIcon);
      } else controller.removeAuthorPane();

      if (blog != null) {
        controller.setBlog(blog);
        controller.setBlogUrl(blogUrl);
      }
      else controller.removeBlogPane();

      if (appIcon != null) controller.setAppIcon(appIcon);
      else controller.removeAppIcon();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }//}}}


  /**
   * <p>タイトルを言語環境に合わせたテキストに変更する</p>
   */
  private void changeTitle() {//{{{

    Locale locale = Locale.getDefault();
    Langs lang = locale == Locale.JAPAN ? Langs.JP : Langs.EN;
    setTitle(lang.STAGE_TITLE);

  }//}}}

}
