package github.and777.zookeeper.cluster.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author edliao on 2017/6/27.
 * @description 事件处理链
 */
@Slf4j
public class EventHandlerChain {

  List<EventHandler> eventHandlers = new ArrayList<>();

  public void dealEvent(WatchedEvent event) {
    eventHandlers.forEach(eventHandler -> eventHandler.dealEvent(event));
  }

  public void addEventHandler(EventHandler eventHandler) {
    eventHandlers.add(eventHandler);
  }

  public void setEventHandlers(List<EventHandler> eventHandlers) {
    this.eventHandlers.clear();
    this.eventHandlers.addAll(eventHandlers);
  }

  class EventHandler {

    String name;
    Consumer<WatchedEvent> consumer;

    public EventHandler(String name, Consumer<WatchedEvent> consumer) {
      this.name = name;
      this.consumer = consumer;
    }

    void dealEvent(WatchedEvent event) {
      log.info("{} 开始处理 {}", name, event.toString());
      consumer.accept(event);
    }
  }
}
