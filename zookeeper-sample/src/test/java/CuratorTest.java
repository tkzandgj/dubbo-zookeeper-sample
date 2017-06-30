import github.and777.curator.CuratorClient;
import github.and777.curator.concurrent.ClientLockRegistry;
import github.and777.curator.concurrent.LeaderSelectorRegistry;
import github.and777.curator.watcher.CacheWatcherRegistry;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * @author edliao on 2017/6/28.
 * @description TODO
 */
public class CuratorTest {

  @Test
  public void test() {
    String ROOT_PATH = "/curator";

    CuratorClient curatorClient = new CuratorClient("localhost:2181");
    curatorClient.create(ROOT_PATH, "hello");
    curatorClient.listPath("/");
    curatorClient.getData(ROOT_PATH);
    curatorClient.setData(ROOT_PATH, "world");
    curatorClient.getData(ROOT_PATH);
    curatorClient.delete(ROOT_PATH);
    curatorClient.listPath("/");
  }

  @Test
  public void testPathWatcher() throws InterruptedException {
    String ROOT_PATH = "/curator";

    CuratorClient curatorClient = new CuratorClient("localhost:2181");
    CacheWatcherRegistry.registerPathCache(curatorClient.getClient(), ROOT_PATH);

    Thread.sleep(1000 * 60 * 10);
  }

  @Test
  public void testNodeWatcher() throws InterruptedException {
    String ROOT_PATH = "/curator";

    CuratorClient curatorClient = new CuratorClient("localhost:2181");
    CacheWatcherRegistry.registerNodeCache(curatorClient.getClient(), ROOT_PATH);

    Thread.sleep(1000 * 60 * 10);
  }

  @Test
  public void testTreeWatcher() throws InterruptedException {
    String ROOT_PATH = "/curator";

    CuratorClient curatorClient = new CuratorClient("localhost:2185");
    CacheWatcherRegistry.registerTreeChche(curatorClient.getClient(), ROOT_PATH);

    Thread.sleep(1000 * 60 * 10);
  }

  @Test
  public void testLock() {
    /**
     * 3个client 连接2个server ,争抢锁并修改一个值
     */
    CuratorClient curatorClient = new CuratorClient("localhost:2182");
    curatorClient.create("/data/test1", "1");

    List<Thread> threads = Arrays.asList(getLockThread("localhost:2180"),
        getLockThread("localhost:2180"),
        getLockThread("localhost:2181"));

    threads.forEach(thread -> thread.start());

    try {
      Thread.sleep(1000 * 5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    curatorClient.getData("/data/test1");
  }

  Thread getLockThread(String url) {
    return new Thread(() ->
        ClientLockRegistry.registerLock(
            new CuratorClient(url), "/lock",
            curatorFramework ->
                curatorFramework
                    .getData("/data/test1")
                    .ifPresent(s -> curatorFramework
                        .setData("/data/test1", String.valueOf(Integer.valueOf(s) + 1))))
    );
  }

  @Test
  public void testLeader() {
    CuratorClient curatorClient = new CuratorClient("localhost:2181");

    List<Thread> threads = Arrays.asList(getLeaderThread("localhost:2181"),
        getLeaderThread("localhost:2182"),
        getLeaderThread("localhost:2183"));

    threads.forEach(thread -> thread.start());

    try {
      Thread.sleep(1000 * 30);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    curatorClient.getData("/leader");
  }

  Thread getLeaderThread(String url) {
    return new Thread(
        () -> LeaderSelectorRegistry.registerListener(
            new CuratorClient(url), "/leader",
            curatorFramework -> {
              System.out.println(Thread.currentThread().getName()+"我选上leader了 ~~~~~~~~");
              CuratorClient wrapper = new CuratorClient(curatorFramework);
              wrapper.setData("/leader",
                  wrapper.getData("/leader") + Thread.currentThread().getName());
              try {
                Thread.sleep(5 * 1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
    );
  }

}
