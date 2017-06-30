import github.and777.config.PathConfig;
import github.and777.tool.ZooKeeperGroupTool;
import org.junit.Test;

/**
 * @author edliao on 2017/6/29.
 * @description 启动
 */
public class Application {

  ZooKeeperGroupTool tool = new ZooKeeperGroupTool();

  @Test
  public void startGroup() {
    tool.startGroup();
  }

  @Test
  public void startServer() {
    tool.startCMD(PathConfig.getZkServer(4));
  }

  @Test
  public void startCli() {
    tool.startCMDInDOS(PathConfig.getZkCli(0));
  }

  @Test
  public void checkConf() {
    //echo conf|nc 127.0.0.1 2184
  }
}
