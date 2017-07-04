import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Test;

/**
 * @author edliao on 2017/7/4.
 * @description Future 异步回调
 */
public class FutureSample {

  @Test
  public void executorTest() throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newCachedThreadPool();

    Future<?> f1 = executor.submit(() -> System.out.println("i'm task 1"));
    Future<String> f2 = executor.submit(() -> "i'm task 2");

    System.out.println("task1 is completed? " + f1.isDone());
    System.out.println("task2 is completed? " + f2.isDone());

    //get will wait for completed
    f1.get();
    System.out.println("return value by task2: " + f2.get());

    System.out.println("task1 is completed? " + f1.isDone());
    System.out.println("task2 is completed? " + f2.isDone());

  }
}
