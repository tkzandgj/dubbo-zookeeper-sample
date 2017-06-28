package github.and777.zookeeper.cluster;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import github.and777.commom.FormatTool;
import github.and777.zookeeper.cluster.systemconfig.ClusterConfig;
import github.and777.zookeeper.cluster.systemconfig.SystemConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author edliao on 2017/6/27.
 * @description Client连接
 */
@Slf4j
public class WatcherClient implements Watcher {

  private ZooKeeper server;
  private ClusterConfig config;

  /**
   * 事件处理链
   */
  @Setter
  @Getter
  private EventHandlerChain eventHandlerChain = new EventHandlerChain();

  /**
   * 连接ZooKeeper服务器
   */
  public WatcherClient(ClusterConfig config) {
    this.config = config;
    try {
      this.server = new ZooKeeper(config.getUrl(), config.getTimeout(), this);
      synchronized (this) {
        this.wait();
      }
      initRoot();
      initChild();
      initData();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 事件处理
   */
  @Override
  public void process(WatchedEvent watchedEvent) {
    log.info("接受到事件:状态[{}],路径[{}]", watchedEvent.getState().toString(), watchedEvent.getPath());
    String path = watchedEvent.getPath();

    if (watchedEvent.getState() == KeeperState.SyncConnected) {
      switch (watchedEvent.getType()) {
        case None:
          synchronized (this) {
            this.notifyAll();
          }
          break;

        case NodeDeleted:
          log.info("删除节点{}", path);
          break;

        case NodeCreated:
          log.info("创建节点{}", path);
          break;

        case NodeDataChanged:
          log.info("节点{}数据变更:{}", path, listenData(path));
          break;

        case NodeChildrenChanged:
          log.info("节点{}下子节点发生变化", path);
          if (path.startsWith(ROOT_PATH + DATA_PATH)) {
            listenPath(path);
          }
          break;
      }
    }
    eventHandlerChain.dealEvent(watchedEvent);
  }


  private static final String ROOT_PATH = "/" + SystemConfig.getInstance().getRootPath();
  private static final String CHILD_PATH =
      ROOT_PATH + "/" + SystemConfig.getInstance().getChildPath();
  private static final String DATA_PATH =
      ROOT_PATH + "/" + SystemConfig.getInstance().getDataPath();

  /**
   * 初始化根节点
   */
  private void initRoot() throws KeeperException, InterruptedException {
    log.info("检查Root节点");

    if (!existsNode(ROOT_PATH)) {
      server
          .create(ROOT_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 根节点只作顶层结构 ,不包含数据也不作任何修改
     * 根节点下分 数据节点/data 和 集群节点/server-n
     * 需要对子节点进行监听
     */
    listenChildren(ROOT_PATH);
  }

  /**
   * 初始化子节点
   */
  private void initChild() throws KeeperException, InterruptedException {
    log.info("检查服务节点");
    String childPath = CHILD_PATH + config.getId();
    /**
     * 根据不同Zookeeper Cluster建立的节点主要是用来检测是否有Cluster掉线
     */
    if (!existsNode(childPath)) {
      server.create(childPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }
  }

  /**
   * 初始化数据节点
   */
  private void initData() throws KeeperException, InterruptedException {
    log.info("检查数据节点");
    String dataPath = DATA_PATH;

    if (!existsNode(dataPath)) {
      server.create(dataPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 数据节点需要监听所有子节点 ,和子节点的数据变化
     */
    listenChildren(dataPath).forEach(childName -> listenPath(dataPath + "/" + childName));
  }


  /**
   * 获取groupNode的情况
   */
  private Optional<Stat> listenNode(String nodePath) {
    Stat stat = null;
    try {
      stat = server.exists(nodePath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(stat);
  }

  private Boolean existsNode(String nodePath) {
    Boolean exists = listenNode(nodePath).isPresent();
    if (exists) {
      log.info("获取到节点{}\n{}", nodePath, formatStatInfo(listenNode(nodePath).get()));
    } else {
      log.info("节点{}不存在", nodePath);
    }
    return exists;
  }

  /**
   * 获取groupNode下的数据 ,并监听其变化
   */
  private Optional<String> listenData(String nodePath) {
    String data = null;
    try {
      byte[] bytes = server.getData(nodePath, true, null);
      if (bytes != null) {
        data = new String(bytes);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!Strings.isNullOrEmpty(data)) {
      log.info("获取到{}的值:{}", nodePath, data);
    }
    return Optional.ofNullable(data);
  }

  /**
   * 获取groupNode下的子节点 ,并监听子节点的变化
   */
  private List<String> listenChildren(String nodePath) {
    List<String> children = new ArrayList<>();
    try {
      children = server.getChildren(nodePath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!children.isEmpty()) {
      log.info("获取到{}的子节点:{}", nodePath, String.join(",", children));
    }
    return children;
  }

  /**
   * 监听节点/数据和子节点的节点/数据变化
   */
  private void listenPath(String nodePath) {
    listenNode(nodePath);
    listenData(nodePath);
    listenChildren(nodePath).forEach(childName -> listenPath(nodePath + "/" + childName));
  }

  /**
   * 对外接口 ,存取数据节点
   */
  public String getData(String key) {
    String path = DATA_PATH + "/" + key;
    return listenData(path).orElse(null);
  }

  public void setData(String key, String value) {
    String path = DATA_PATH + "/" + key;
    try {
      Optional<Stat> stat = listenNode(path);
      if (stat.isPresent()) {
        server.setData(path, value.getBytes(), stat.get().getVersion());
      } else {
        server.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Test
   */
  public String formatStatInfo(Stat stat) {
    Map<String, String> map = FormatTool.formatArray2Map(
        new String[]{"czxid", "mzxid", "ctime", "mtime", "version", "cversion", "aversion",
            "ephemeralOwner", "dataLength", "numChildren", "pzxid"},
        stat.toString().trim().split(","));

    return "\t"+Joiner.on(",\n\t").withKeyValueSeparator("=").join(map);
  }
}
