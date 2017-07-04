package github.and777.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author edliao on 2017/7/4.
 * @description 编译Proto文件
 */
public class ProtoCompile {

  public static final String masterProjectPath = System.getProperty("user.dir");

  private static String protocPath = masterProjectPath + "/libs/protoc.exe";
  private static String projectPath =
      System.getProperty("user.dir") + "/netty-sample/protobuf-sample";

  private static String srcDir = getSourceDir();
  private static String outDir = projectPath + "/src/main/java";

  private static String getSourceDir() {
    String path = ProtoCompile.class.getClassLoader().getResource("").getPath();
    return path.substring(1, path.length());
  }

  private void compile(String fileName) throws IOException, InterruptedException {
    String command = new StringBuilder()
        .append(protocPath)//protoc.exe
        .append(" -I=").append(srcDir)// -I=resources
        .append(" --java_out=").append(outDir) // --java_out=/src/main/java
        .append(" ").append(srcDir + "/" + fileName) // resources/xxx.proto
        .toString();

    Process p = Runtime.getRuntime().exec(command);

    new BufferedReader(new InputStreamReader(p.getInputStream())).lines()
        .forEach(s -> System.out.println(s));

    new BufferedReader(new InputStreamReader(p.getErrorStream())).lines()
        .forEach(s -> System.out.println(s));

    p.waitFor();
    p.destroy();
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    ProtoCompile compile = new ProtoCompile();
    compile.compile("AddressBookProtos.proto");
  }
}
