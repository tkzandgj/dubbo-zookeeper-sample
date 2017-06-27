package github.and777.zookeeper.base.operator;

import github.and777.zookeeper.base.ConnectionWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author edliao on 2017/6/26.
 * @description 删除分组
 */
@Slf4j
public class DeleteGroup {

  public static void delete(String groupName) {
    try (ConnectionWatcher watcher = new ConnectionWatcher()) {
      ZooKeeper zooKeeper = watcher.getConnectedServer();

      String path = "/" + groupName;
      zooKeeper.getChildren(path, false)
          .forEach(s -> delete(zooKeeper, path + "/" + s));
      delete(zooKeeper, path);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void delete(ZooKeeper zooKeeper, String path) {
    try {
      zooKeeper.delete(path, -1);//需要提供版本号 ,-1表示所有版本
      log.info("Delete group:{}", path);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    delete("Test");
  }
}
