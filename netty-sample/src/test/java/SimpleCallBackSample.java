import java.util.function.Consumer;
import org.junit.Test;

/**
 * @author edliao on 2017/7/4.
 * @description 基本回调
 *
 * 回调就是在既定的任务完成以后 ,给予使用者一个可以接受/处理结果的位置
 */
public class SimpleCallBackSample {

  Consumer<String> callback;

  public void setCallback(Consumer<String> callback) {
    this.callback = callback;
  }

  public void doWork(String name) {
    System.out.println("Doing work:" + name);
    callback.accept(name);
  }

  @Test
  public void callback() {
    SimpleCallBackSample test = new SimpleCallBackSample();
    test.setCallback(s -> System.out.println("I'm callback"));
    test.doWork("Simple Task");
  }
}
