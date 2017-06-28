package github.and777.curator.concurrent;

import github.and777.curator.CuratorClient;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;

/**
 * @author edliao on 2017/6/28.
 * @description leader选举
 */
@Slf4j
public class LeaderSelectorRegistry {

  /**
   * 注册选举监听
   */
  public static void registerListener(CuratorFramework client, String path,
      LeaderSelectorListener listener) {
    client.create().creatingParentContainersIfNeeded();

    LeaderSelector selector = new LeaderSelector(client, path, listener);
    selector.autoRequeue();
    selector.start();
  }

  /**
   * 构造监听器并注册
   */
  public static void registerListener(CuratorFramework client, String path,
      Consumer<CuratorFramework> takeLeadership) {
    registerListener(client, path, buildLeaderSelectorListener(takeLeadership));
  }

  /**
   * 构造监听器并注册
   */
  public static void registerListener(CuratorClient client, String path,
      Consumer<CuratorFramework> takeLeadership) {
    registerListener(client.getClient(), path, buildLeaderSelectorListener(takeLeadership));
  }

  /**
   * 构造监听器
   */
  public static LeaderSelectorListener buildLeaderSelectorListener(
      Consumer<CuratorFramework> takeLeadership) {
    return buildLeaderSelectorListener(takeLeadership, null);
  }


  /**
   * 构造监听器
   */
  public static LeaderSelectorListener buildLeaderSelectorListener(
      Consumer<CuratorFramework> takeLeadership,
      BiConsumer<CuratorFramework, ConnectionState> stateChanged) {

    return new LeaderSelectorListener() {
      @Override
      public void takeLeadership(CuratorFramework client) throws Exception {
        log.info("{} 成为新的Leader!", Thread.currentThread().getName());
        takeLeadership.accept(client);
        log.info("{} 放弃Leader!", Thread.currentThread().getName());
      }

      @Override
      public void stateChanged(CuratorFramework client, ConnectionState state) {
        if (!Objects.isNull(stateChanged)) {
          log.info("{}状态发生变化:{}", Thread.currentThread().getName(), state);
          stateChanged.accept(client, state);
        }
      }
    };
  }


}
