package github.and777.zookeeper.cluster.systemconfig;

import lombok.Data;

/**
 * @author edliao on 2017/6/27.
 * @description 节点配置
 */
@Data
public class ClusterConfig {

  Integer id;
  String host;
  Integer clientPort;
  Integer leaderPort;
  Integer messagePort;
  Integer timeout = 5000;

  public String getUrl() {
    return host + ":" + clientPort;
  }
}
