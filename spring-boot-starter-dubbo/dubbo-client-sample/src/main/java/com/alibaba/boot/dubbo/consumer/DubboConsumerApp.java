package com.alibaba.boot.dubbo.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author edliao on 2017/6/30.
 * @description TODO
 */
@SpringBootApplication
public class DubboConsumerApp {

  public static void main(String[] args) throws InterruptedException {
    ConfigurableApplicationContext context = SpringApplication.run(DubboConsumerApp.class, args);

    HelloController controller = context.getBean(HelloController.class);
    controller.login();
  }
}
