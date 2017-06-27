package github.and777.zookeeper.cluster.systemconfig;

import java.io.FileReader;
import java.net.URL;
import org.yaml.snakeyaml.Yaml;

/**
 * @author edliao on 2017/6/23.
 * @description 解析yaml文件
 */
public class YAMLScanner {

  private static final URL CONFIG_FILE_URL;
  private static final Yaml scanner;

  private static SystemConfig serverConfig;

  static {
    CONFIG_FILE_URL = YAMLScanner.class.getClassLoader()
        .getResource("server.yaml");
    scanner = new Yaml();
    serverConfig = loadConfig(CONFIG_FILE_URL, SystemConfig.class);
  }

  private static <T> T loadConfig(URL url, Class<T> targetClass) {
    T config = null;
    try {
      config = scanner.loadAs(new FileReader(url.getFile()), targetClass);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return config;
  }

  public static SystemConfig getSystemConfig() {
    return serverConfig;
  }
}
