package github.and777.zookeeper.base;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author edliao on 2017/6/26.
 * @description 连接ZooKeeper服务器
 */
public class ConnectionWatcher implements Watcher, AutoCloseable {

  private ZooKeeper zooKeeper;
  private CountDownLatch connectedSignal = new CountDownLatch(1);

  /**
   * 连接zookeeper服务器 ,等待连接成功
   */
  public void connect() throws IOException, InterruptedException {
    zooKeeper = new ZooKeeper(LocalConfig.getUrl(), LocalConfig.SESSION_TIMEOUT, this);
    connectedSignal.await();
  }

  /**
   * 监听连接过程中的事件 ,添加回调动作:连接成功时唤醒主线程继续
   */
  @Override
  public void process(WatchedEvent watchedEvent) {
    if (watchedEvent.getState() == KeeperState.SyncConnected) {
      connectedSignal.countDown();
    }
  }

  @Override
  public void close() throws Exception {
    zooKeeper.close();
  }

  public ZooKeeper getConnectedServer() {
    try {
      connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return zooKeeper;
  }
}
