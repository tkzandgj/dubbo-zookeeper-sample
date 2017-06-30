package github.and777.tool;

import github.and777.config.PathConfig;
import github.and777.config.ServerConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/29.
 * @description 对Zookeeper集群的操作
 */
@Slf4j
public class ZooKeeperGroupTool {

  CopyTool copyTool = new CopyTool();
  Integer serverCount = copyTool.getCurrentServerCount();

  public List<Process> startGroup() {
    List<Process> processes = new ArrayList<>();

    doWithServerCount(integer -> processes.add(startCMDInDOS(PathConfig.getZkServer(integer))));
    startCMDInDOS(PathConfig.getZkCli(ServerConfig.DEFAULT_ID));

    return processes;
  }

  public Process startCMDInDOS(String cmdPath) {
    return startCMD(cmdPath, "/k start");
  }

  public Process startCMD(String cmdPath) {
    return startCMD(cmdPath, "/k");
  }

  private Process startCMD(String cmdPath, String option) {
    try {
      String command = "cmd " + option + " " + cmdPath;
      log.info("执行cmd命令{}", command);
      return Runtime.getRuntime().exec(command);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void setServerCount(Integer count) {
    if (count == serverCount) {
      updateCfg();
      return;
    }

    if (count > serverCount) {
      addServer();
    } else {
      deleteServer();
    }
    setServerCount(count);
  }

  public void appendServer() {
    addServer();
    updateCfg();
  }

  public void deleteLatestServer() {
    deleteServer();
    updateCfg();
  }

  private void addServer() {
    //复制新server
    copyTool.copyDirectory(serverCount);
    //写入编号
    copyTool.writeMyId(serverCount);
    //更新serverCount
    serverCount++;
  }

  private void deleteServer() {
    serverCount--;
    copyTool.delete(serverCount);
  }

  public void updateMyId() {
    //更新各server的cfg
    doWithServerCount(integer -> copyTool.writeMyId(integer));
  }

  public void updateCfg() {
    //更新各server的cfg
    doWithServerCount(integer -> copyTool.writeZooCfg(integer, serverCount));
  }

  private void doWithServerCount(Consumer<Integer> consumer) {
    for (int i = 1; i <= serverCount; i++) {
      consumer.accept(i);
    }
  }
}
