package github.and777.commom;

import java.io.FileReader;
import java.net.URL;
import org.yaml.snakeyaml.Yaml;

/**
 * @author edliao on 2017/6/23.
 * @description 解析yaml文件
 */
public class YAMLScanner {

  private static final Yaml scanner = new Yaml();

  /**
   * 按class类型加载yaml配置
   */
  private static <T> T loadConfig(URL url, Class<T> targetClass) {
    T config = null;
    try {
      config = scanner.loadAs(new FileReader(url.getFile()), targetClass);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return config;
  }

  public static <T> T getConfig(URL url, Class<T> targetClass) {
    return loadConfig(url, targetClass);
  }

  public static <T> T getConfigInClassPath(String path, Class<T> targetClass) {
    return getConfig(YAMLScanner.class.getClassLoader().getResource(path), targetClass);
  }
}
