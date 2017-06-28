package github.and777.curator.concurrent;

import github.and777.curator.CuratorClient;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @author edliao on 2017/6/28.
 * @description 争抢锁
 */
@Slf4j
public class ClientLockRegistry {

  /**
   * 分布式锁
   */
  public static void registerLock(CuratorClient client, String lockPath,
      Consumer<CuratorClient> consumer) {
    InterProcessMutex lock = new InterProcessMutex(client.getClient(), lockPath);
    try {
      log.info("{}线程等待锁", Thread.currentThread().getName());
      if (lock.acquire(10 * 1000, TimeUnit.SECONDS)) {
        log.info("{}线程已得到锁", Thread.currentThread().getName());
        consumer.accept(client);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        log.info("{}线程执行完毕 ,释放锁", Thread.currentThread().getName());
        lock.release();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
