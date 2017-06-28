package github.and777.curator.watcher;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * @author edliao on 2017/6/28.
 * @description 注册监听
 */
@Slf4j
public class CacheWatcherRegistry {

  /**
   * Path Cache ,监听path下所有子节点的 增删改 ,删除时保有缓存
   */
  public static void registerPathCache(CuratorFramework client, String path,
      PathChildrenCacheListener listener) {
    PathChildrenCache watcher = new PathChildrenCache(client, path, true);
    watcher.getListenable().addListener(listener);
    try {
      watcher.start(StartMode.BUILD_INITIAL_CACHE);
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("Path Cache监听已注册到{}", path);
  }

  /**
   * Node Cache ,监听path节点的 增删改
   */
  public static void registerNodeCache(CuratorFramework client, String path,
      BiConsumer<CuratorFramework, NodeCacheEvent> listener) {
    NodeCache watcher = new NodeCache(client, path);
    watcher.getListenable().addListener(
        () -> listener.accept(client, new NodeCacheEvent(path, watcher.getCurrentData())));
    try {
      watcher.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("Node Cache监听已注册到{}", path);
  }

  /**
   * Tree = Node +Path
   * 用起来更简单一些
   */
  public static void registerTreeChche(CuratorFramework client, String path,
      TreeCacheListener listener) {
    TreeCache watcher = new TreeCache(client, path);
    watcher.getListenable().addListener(listener);
    try {
      watcher.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("Tree Cache监听已注册到{}", path);
  }

  /**
   * 默认调用
   */
  public static void registerPathCache(CuratorFramework client, String path) {
    registerPathCache(client, path, DEFAULT_PATH_LISTENER);
  }

  public static void registerNodeCache(CuratorFramework client, String path) {
    registerNodeCache(client, path, DEFAULT_NODE_LISTENER);
  }

  public static void registerTreeChche(CuratorFramework client, String path) {
    registerTreeChche(client, path, DEFAULT_TREE_LISTENER);
  }

  /**
   * 默认的listener实现
   */
  private static PathChildrenCacheListener DEFAULT_PATH_LISTENER = (client, event) ->
      Optional.ofNullable(event.getData()).
          ifPresent(childData ->
              log.info("子节点变化: "
                  + "type=[" + event.getType() + "]"
                  + ", path=[" + childData.getPath() + "]"
                  + ", data=[" + new String(childData.getData()) + "]"
                  + ", stat=[" + childData.getStat() + "]")
          );


  private static BiConsumer<CuratorFramework, NodeCacheEvent> DEFAULT_NODE_LISTENER = (client, event) -> {
    if (Objects.isNull(event.getData())) {
      log.info("{}节点已删除", event.getPath());
    } else {
      log.info("{}节点创建或更新", event.getPath());
    }
  };

  private static TreeCacheListener DEFAULT_TREE_LISTENER = (client, event) ->
      Optional.ofNullable(event.getData()).
          ifPresent(childData ->
              log.info("树节点变化: "
                  + "type=[" + event.getType() + "]"
                  + ", path=[" + childData.getPath() + "]"
                  + ", data=[" + new String(childData.getData()) + "]"
                  + ", stat=[" + childData.getStat() + "]")
          );

  /**
   * 补充的node事件 ,包含变化的node的path和属性
   */
  static class NodeCacheEvent {

    private final String path;
    private final ChildData data;

    public NodeCacheEvent(String path, ChildData data) {
      this.path = path;
      this.data = data;
    }

    public String getPath() {
      return path;
    }

    public ChildData getData() {
      return data;
    }
  }
}



