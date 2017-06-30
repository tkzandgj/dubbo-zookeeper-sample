import github.and777.tool.CopyTool;
import github.and777.tool.ZooKeeperGroupTool;
import java.io.File;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author edliao on 2017/6/29.
 * @description TODO
 */
public class BootTest {

  private static Long count = 0l;

  @Test
  public void testCopy() {
    CopyTool copyTool = new CopyTool();
    copyTool.copyDirectory(1);
  }

  @Test
  public void delete() {
    try {
      File file = new File("C:\\CodingTool\\ZookeeperGroup\\service1");
      findChild(file);
      System.out.println(count);
    } catch (Exception e) {
      System.out.println(count);
    }
  }

  public void findChild(File file) {
    count++;
    Stream.of(file.listFiles()).forEach(file1 -> {
      findChild(file1);
      System.out.println("删除?" + file1.delete());
    });
  }

  @Test
  public void configTest() {
    CopyTool copyTool = new CopyTool();
    System.out.println(copyTool.getCurrentServerCount());
    copyTool.writeMyId(0);
    copyTool.writeMyId(1);
    copyTool.writeZooCfg(0, 2);
    copyTool.writeZooCfg(1, 2);
  }

  @Test
  public void toolTest() {
    ZooKeeperGroupTool tool = new ZooKeeperGroupTool();
    //tool.addServer();
    //tool.deleteServer();
    //tool.setServerCount(5);
    //tool.updateMyId();
    //tool.updateCfg();
    //tool.startCMDInDOS(PathConfig.getZkCli(0));
  }

}
