package github.and777.zookeeper.cluster.systemconfig;

import java.util.List;
import lombok.Data;

/**
 * @author edliao on 2017/6/27.
 * @description 读取配置文件
 */
@Data
public class SystemConfig {

  List<ClusterConfig> clusters;
  String rootPath;
  String dataPath;
  String childPath;
}
