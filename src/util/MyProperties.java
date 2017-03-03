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

  private final Properties prop;
  private final File file;

  public MyProperties(String fileName) {//{{{
    this(new File(fileName));
  }//}}}

  public MyProperties(File aFile) {//{{{
    prop = new Properties();
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

        prop.loadFromXML(in);
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

    double x      = Double.parseDouble(prop.getProperty("x"));
    double y      = Double.parseDouble(prop.getProperty("y"));
    double width  = Double.parseDouble(prop.getProperty("width"));
    double height = Double.parseDouble(prop.getProperty("height"));

    stage.setX(x);
    stage.setY(y);
    stage.setWidth(width);
    stage.setHeight(height);

  }//}}}

  public void store(String comment) {//{{{

    try (FileOutputStream out = new FileOutputStream(file)) {
      prop.storeToXML(out, comment);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }//}}}

  /**
   * ファイルの有無を返す。
   *
   * @return 存在するかしないか
   */
  public boolean exists() {//{{{
    return file.exists();
  }//}}}

  public void store() {//{{{
    store(null);
  }//}}}

  public String getProperty(String key) {//{{{
    return prop.getProperty(key);
  }//}}}

  public void setProperty(String key, String value) {//{{{
    prop.setProperty(key, value);
  }//}}}

  public void setProperties(Node node) {//{{{
    Stage stage = (Stage) node.getScene().getWindow();
    setProperties(stage);
  }//}}}

  public void setProperties(Stage stage) {//{{{

    prop.setProperty("x"      , "" + stage.getX());
    prop.setProperty("y"      , "" + stage.getY());
    prop.setProperty("width"  , "" + stage.getWidth());
    prop.setProperty("height" , "" + stage.getHeight());

  }//}}}

}
