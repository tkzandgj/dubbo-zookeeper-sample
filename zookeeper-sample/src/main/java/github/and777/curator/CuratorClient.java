package github.and777.curator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author edliao on 2017/6/28.
 * @description curator
 *
 * Curator 2.x -> ZooKeeper 3.4.x Curator 3.x -> ZooKeeper 3.5.x
 */
@Slf4j
public class CuratorClient {

  @Getter
  private CuratorFramework client;

  public CuratorClient(String url) {
    client = CuratorFrameworkFactory.newClient(url, new RetryNTimes(10, 5000));
    client.start();
    try {
      client.blockUntilConnected();
      log.info("已连接到ZooKeeper服务器{}!", url);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public CuratorClient(CuratorFramework client) {
    this.client = client;
  }

  public void create(String path, String data) {
    try {
      client.create().
          creatingParentsIfNeeded().
          forPath(path, data.getBytes());
      log.info("创建节点{},数据{}", path, data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<String> listPath(String path) {
    List<String> result = new ArrayList<>();
    try {
      result = client.getChildren().forPath(path);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!result.isEmpty()) {
      log.info("节点{}的子节点有{}", path, String.join(",", result));
    }
    return result;
  }

  public Optional<String> getData(String path) {
    String data = null;
    try {
      byte[] bytes = client.getData().forPath(path);
      if (bytes != null) {
        data = new String(bytes);
      }
      log.info("获取到{}的数据{}", path, data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(data);
  }

  public void setData(String path, String data) {
    try {
      client.setData().forPath(path, data.getBytes());
      log.info("重设{}的数据为{}", path, data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(String path) {
    try {
      client.delete().forPath(path);
      log.info("{}节点已删除");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
