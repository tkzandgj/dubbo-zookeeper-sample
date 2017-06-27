package github.and777.zookeeper.base.operator;

import github.and777.zookeeper.base.ConnectionWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author edliao on 2017/6/26.
 * @description create path[] 加入Group
 */
@Slf4j
public class JoinGroup {

  public static void join(String groupName, String memberName) {
    try (ConnectionWatcher connection = new ConnectionWatcher()) {
      ZooKeeper server = connection.getConnectedServer();

      String path = "/" + groupName + "/" + memberName;
      String createdPath = server.create(path, "Hello world.".getBytes(),
          Ids.OPEN_ACL_UNSAFE,
          CreateMode.EPHEMERAL//临时的 ,没有子节点
      );
      log.info("Created:{} and Joined Group:{}", createdPath, groupName);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    join("Test", "SubTest");

  }
}
