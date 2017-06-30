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
   *
   * 原理:
   * 需要争抢锁的应用在lockpath下新建序列节点 ,如果应用中的序列 等于 序列最小的节点 ,则获得锁
   * 其他应用订阅lockpath ,等待锁的释放(删除序列最小的节点)
   *
   * 优化 (from 阿里中间件) :当集群比较大的时候,订阅lockpath后每次锁的释放 ,大多数都是得不到锁的 1/n
   * 判断最小节点(min)是否是该应用生成的民(x) ,是则获得锁 ; 不是则订阅比自己小(x-1)的节点 ,当(x-1)节点删除时 ,就轮到(x)应用获得锁了
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
