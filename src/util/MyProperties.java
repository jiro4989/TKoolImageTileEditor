package util;

import app.ImageStandard;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * プロパティファイルの操作を簡単にするためのラッパークラス。
 * 扱うプロパティファウrはすべてXML形式である。
 */
public class MyProperties {

  private final Properties properties;
  private final File file;

  public MyProperties(String fileName) {//{{{
    this(new File(fileName));
  }//}}}

  public MyProperties(File aFile) {//{{{
    properties = new Properties();
    file = aFile;
  }//}}}

  /**
   * プロパティファイルをロードし、成功失敗を返す。
   *
   * @return 成功:true, 失敗:false
   */
  public boolean load() {//{{{

    boolean success = false;
    if (file.exists()) {

      try (InputStream in = new FileInputStream(file)) {

        properties.loadFromXML(in);
        success = true;

      } catch (IOException e) {
        e.printStackTrace();
      }

    }

    return success;

  }//}}}

  /**
   * Stsgrの座標と幅の設定を行う。
   * 事前にload()を成功させていないといけない。
   */
  public void customStage(Stage stage) {//{{{

    double x      = Double.parseDouble(properties.getProperty("x"));
    double y      = Double.parseDouble(properties.getProperty("y"));
    double width  = Double.parseDouble(properties.getProperty("width"));
    double height = Double.parseDouble(properties.getProperty("height"));

    boolean isMaximized = Boolean.valueOf(properties.getProperty("isMaximized"));

    stage.setX(x);
    stage.setY(y);
    stage.setWidth(width);
    stage.setHeight(height);
    stage.setMaximized(isMaximized);

  }//}}}

  public void store(String comment) {//{{{

    try (FileOutputStream out = new FileOutputStream(file)) {
      properties.storeToXML(out, comment);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }//}}}

  public void store() {//{{{
    store(null);
  }//}}}

  /**
   * ファイルの有無を返す。
   *
   * @return 存在するかしないか
   */
  public boolean exists() {//{{{
    return file.exists();
  }//}}}

  public Optional<String> getProperty(String key) {//{{{

    return Optional.ofNullable(properties.getProperty(key));

  }//}}}

  public Properties getProperties() {//{{{

    return properties;

  }//}}}

  public void setProperty(String key, String value) {//{{{
    properties.setProperty(key, value);
  }//}}}

  public void setProperties(Node node) {//{{{
    Stage stage = (Stage) node.getScene().getWindow();
    setProperties(stage);
  }//}}}

  public void setProperties(Stage stage) {//{{{

    boolean isMaximized = stage.isMaximized();
    if (isMaximized)
      stage.setMaximized(false);

    properties.setProperty("x"           , "" + stage.getX());
    properties.setProperty("y"           , "" + stage.getY());
    properties.setProperty("width"       , "" + stage.getWidth());
    properties.setProperty("height"      , "" + stage.getHeight());
    properties.setProperty("isMaximized" , "" + isMaximized);

  }//}}}

}
