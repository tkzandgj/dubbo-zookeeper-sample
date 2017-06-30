package github.and777.config;

/**
 * @author edliao on 2017/6/29.
 * @description 相关的路径
 *
 * 我本地是在ZookeeperGroup\下并列放置的zookeeper
 * @C:\CodingTool\ZookeeperGroup\service0\data\myid
 * @C:\CodingTool\ZookeeperGroup\service0\log
 * @C:\CodingTool\ZookeeperGroup\service0\zookeeper-3.4.10\
 * @...
 * @C:\CodingTool\ZookeeperGroup\service5\data\myid
 * @C:\CodingTool\ZookeeperGroup\service5\log
 * @C:\CodingTool\ZookeeperGroup\service5\zookeeper-3.4.10\
 */
public class PathConfig {

  public static final String GROUP_ROOT = "C:\\CodingTool\\ZookeeperGroup\\";

  public static final String DATA_DIR = "data";
  public static final String LOG_DIR = "log";
  public static final String ZOOKEEPER_DIR_PREFIX = "service";

  public static final String ZOOKEEPER_ROOT = "zookeeper-3.4.10";

  public static final String ZK_CFG = ZOOKEEPER_ROOT + "\\conf\\zoo.cfg";
  public static final String ZK_CLI = ZOOKEEPER_ROOT + "\\bin\\zkCli.cmd";
  public static final String ZK_SERVER = ZOOKEEPER_ROOT + "\\bin\\zkServer.cmd";

  public static String getCluster(Integer id) {
    return GROUP_ROOT + ZOOKEEPER_DIR_PREFIX + id;
  }

  public static String getClusterData(Integer id) {
    return getCluster(id) + "\\" + DATA_DIR;
  }

  public static String getClusterLog(Integer id) {
    return getCluster(id) + "\\" + LOG_DIR;
  }

  public static String getMyId(Integer id) {
    return getClusterData(id) + "\\myid";
  }

  public static String getZooCfg(Integer id) {
    return getCluster(id) + "\\" + ZK_CFG;
  }

  public static String getZkCli(Integer id) {
    return getCluster(id) + "\\" + ZK_CLI;
  }

  public static String getZkServer(Integer id) {
    return getCluster(id) + "\\" + ZK_SERVER;
  }
}
