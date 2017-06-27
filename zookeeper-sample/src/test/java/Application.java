import github.and777.zookeeper.cluster.client.Client;
import github.and777.zookeeper.cluster.systemconfig.ClusterConfig;
import github.and777.zookeeper.cluster.systemconfig.SystemConfig;
import github.and777.zookeeper.cluster.systemconfig.YAMLScanner;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author edliao on 2017/6/27.
 * @description TODO
 */
public class Application {

  @Test
  public void connect() throws InterruptedException {
    SystemConfig systemConfig = YAMLScanner.getSystemConfig();
    List<ClusterConfig> clusterList = systemConfig.getClusters();
    List<Client> clients = new ArrayList<>();
    for (ClusterConfig config : clusterList) {
      clients.add(new Client(config));
    }

    Thread.sleep(1000 * 5);

    clients.get(0).setData("user", "eddy");

    Thread.sleep(1000 * 5);
    System.out.println(clients.get(2).getData("user"));

    Thread.sleep(1000 * 60 * 10);
  }

  @Test
  public void sync() throws InterruptedException {
    SystemConfig systemConfig = YAMLScanner.getSystemConfig();
    Client client = new Client(systemConfig.getClusters().get(0));
    Thread.sleep(1000 * 60 * 10);
  }

}
