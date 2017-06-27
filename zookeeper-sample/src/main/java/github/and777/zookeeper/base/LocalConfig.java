package github.and777.zookeeper.base;

/**
 * @author edliao on 2017/6/26.
 * @description TODO
 */
public class LocalConfig {

  public static final String HOST = "127.0.0.1";
  public static final Integer PORT = 2181;
  public static final Integer SESSION_TIMEOUT = 5000;

  public static String getUrl(){
    return HOST+":"+PORT;
  }
}
