import github.and777.commom.YAMLScanner;
import github.and777.zookeeper.cluster.WatcherClient;
import github.and777.zookeeper.cluster.systemconfig.ClusterConfig;
import github.and777.zookeeper.cluster.systemconfig.SystemConfig;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author edliao on 2017/6/27.
 * @description TODO
 */
public class ZooKeeperTest {

  @Test
  public void connect() throws InterruptedException {
    URL url = ZooKeeperTest.class.getClassLoader().getResource("server.yaml");
    SystemConfig systemConfig = SystemConfig.setInstance(YAMLScanner.getConfig(url, SystemConfig.class));

    List<ClusterConfig> clusterList = systemConfig.getClusters();
    List<WatcherClient> clients = new ArrayList<>();
    for (ClusterConfig config : clusterList) {
      clients.add(new WatcherClient(config));
    }

    Thread.sleep(1000 * 5);

    clients.get(0).setData("user", "eddy");

    Thread.sleep(1000 * 5);
    System.out.println(clients.get(2).getData("user"));

    Thread.sleep(1000 * 60 * 10);
  }

  @Test
  public void sync() throws InterruptedException {
    URL url = ZooKeeperTest.class.getClassLoader().getResource("server.yaml");
    SystemConfig systemConfig = SystemConfig.setInstance(YAMLScanner.getConfig(url, SystemConfig.class));
    WatcherClient client = new WatcherClient(systemConfig.getClusters().get(0));
    Thread.sleep(1000 * 60 * 10);
  }

}
