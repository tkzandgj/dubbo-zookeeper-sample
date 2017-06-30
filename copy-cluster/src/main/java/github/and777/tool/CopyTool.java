package github.and777.tool;

import com.google.common.io.Files;
import github.and777.config.PathConfig;
import github.and777.config.ServerConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/29.
 * @description 对相关文件的 复制/删除/修改
 */
@Slf4j
public class CopyTool {

  TemplateGenerator templateGenerator = new TemplateGenerator();

  /**
   * 按照第一个cluster ,复制一个cluster
   */
  public void copyDirectory(Integer id) {
    String sourceDir = PathConfig.getCluster(ServerConfig.DEFAULT_ID);
    String copyDir = PathConfig.getCluster(id);

    File sourceFile = new File(sourceDir);
    copy(sourceFile, copyDir);
    copyChild(sourceFile, copyDir);
  }

  private void copyChild(File from, String toPath) {
    File[] children = from.listFiles();
    if (!Objects.isNull(children)) {
      Stream.of(children).forEach(child -> {
        String copyFilePath = toPath + "\\" + child.getName();
        copy(child, copyFilePath);
        if (child.isDirectory()) {
          copyChild(child, copyFilePath);
        }
      });
    }
  }

  private void copy(File from, String toFilePath) {
    try {
      File to = new File(toFilePath);
      if (from.isDirectory()) {
        log.info("{}是文件夹,创建复制路径的文件夹{}", from.getName(), toFilePath);
        to.mkdirs();
      } else if (from.isFile()) {
        log.info("{}是文件,复制到{}", from.getName(), toFilePath);
        to.createNewFile();
        Files.copy(from, to);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeZooCfg(Integer id, Integer serverCount) {
    String content = generateZooCfg(id, serverCount);
    File cfgFile = new File(PathConfig.getZooCfg(id));
    //log.info("生成cfg文件\n{}", content);
    log.info("cfg文件已写入{}", cfgFile.getAbsolutePath());
    write(content, cfgFile);
  }

  public void writeMyId(Integer id) {
    File myIdFile = new File(PathConfig.getMyId(id));
    log.info("生成myid - {} , 写入{}", id, myIdFile.getAbsolutePath());
    write(id.toString(), myIdFile);
  }

  private void write(String content, File file) {
    try {
      Files.write(content.getBytes(), file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String generateZooCfg(Integer id, Integer serverCount) {
    return templateGenerator.generate(template -> {
      template.binding("clientPort", ServerConfig.getClientPort(id));
      template.binding("dataDir", PathConfig.getClusterData(id).replace("\\", "\\\\"));
      template.binding("dataLogDir", PathConfig.getClusterLog(id).replace("\\", "\\\\"));

      List<ServerConfig> serverList = new ArrayList<>();
      for (int i = 1; i <= serverCount; i++) {
        serverList.add(new ServerConfig(i));//编号从1开始
      }
      template.binding("serverList", serverList);
    });
  }

  public Integer getCurrentServerCount() {
    File rootDir = new File(PathConfig.GROUP_ROOT);
    return
        Long.valueOf(
            Stream.of(rootDir.listFiles())
                .filter(file -> file.getName().startsWith(PathConfig.ZOOKEEPER_DIR_PREFIX))
                .count()
        ).intValue();
  }

  public void delete(Integer id) {
    File root = new File(PathConfig.getCluster(id));
    log.info("正在删除{}下的文件", root.getAbsolutePath());
    if (root.exists()) {
      deleteChild(root);
      root.delete();
    }
  }

  public void deleteChild(File parent) {
    File[] children = parent.listFiles();
    if (!Objects.isNull(children)) {
      Stream.of(children).forEach(file -> {
        deleteChild(file);
        log.info("正在删除{} ,{}", file.getName(), file.delete() ? "成功" : "失败");
      });
    }
  }
}
