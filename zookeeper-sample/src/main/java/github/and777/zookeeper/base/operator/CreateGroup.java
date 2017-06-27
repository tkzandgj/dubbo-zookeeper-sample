package github.and777.zookeeper.base.operator;

import github.and777.zookeeper.base.ConnectionWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;


/**
 * @author edliao on 2017/6/26.
 * @description create [path] ,创建/zoo的组节点
 */
@Slf4j
public class CreateGroup {

  /**
   * 创建节点
   */
  public static void create(String groupName) {
    try (ConnectionWatcher connection = new ConnectionWatcher()) {
      ZooKeeper server = connection.getConnectedServer();

      String path = "/" + groupName;
      String createdPath = server.create(path, "Hello world.".getBytes(),
          Ids.OPEN_ACL_UNSAFE,
          CreateMode.PERSISTENT//持久化的
      );
      log.info("Created:{}", createdPath);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    create("Test");
  }
}
