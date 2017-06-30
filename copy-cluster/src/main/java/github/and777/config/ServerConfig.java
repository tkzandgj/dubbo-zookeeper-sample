package github.and777.config;

import lombok.Getter;

/**
 * @author edliao on 2017/6/29.
 * @description 服务端集群的端口号POJO和设置
 */
public class ServerConfig {

  private static final String LOCAL_HOST = "127.0.0.1";
  private static final Integer CLIENT_PORT_START = 2180;
  private static final Integer LEADER_PORT_START = 2880;
  private static final Integer MESSAGE_PORT_START = 3880;
  public static final Integer DEFAULT_ID = 1;

  public ServerConfig(Integer id) {
    this.id = id;
    clientPort = CLIENT_PORT_START + id;
    leaderPort = LEADER_PORT_START + id;
    messagePort = MESSAGE_PORT_START + id;
    innerUrl = LOCAL_HOST + ":" + leaderPort + ":" + messagePort;
  }

  @Getter
  Integer id;
  Integer clientPort;
  Integer leaderPort;
  Integer messagePort;
  @Getter
  String innerUrl;

  public static Integer getClientPort(Integer id) {
    return CLIENT_PORT_START + id;
  }
}
