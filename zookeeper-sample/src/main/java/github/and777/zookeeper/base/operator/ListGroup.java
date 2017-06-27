package github.and777.zookeeper.base.operator;

import github.and777.zookeeper.base.ConnectionWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author edliao on 2017/6/26.
 * @description ls [path]
 */
@Slf4j
public class ListGroup {

  public static void list(ZooKeeper zooKeeper, String path)
      throws KeeperException, InterruptedException {
    zooKeeper.getChildren(path, false).forEach(s -> log.info("Have child:{}", s));
  }

  public static void list(String groupName) {
    try (ConnectionWatcher watcher = new ConnectionWatcher()) {
      ZooKeeper zooKeeper = watcher.getConnectedServer();
      list(zooKeeper, "/" + groupName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    list("");
    list("Test");
  }
}
