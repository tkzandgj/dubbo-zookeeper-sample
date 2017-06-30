package com.alibaba.boot.dubbo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring boot默认使用tomcat8，所以需要jdk7以上版本
 *
 * @author xionghui
 * @email xionghui.xh@alibaba-inc.com
 * @since 1.0.0
 */
@SpringBootApplication
public class DubboProviderApp {

  public static void main(String[] args) {
    SpringApplication.run(DubboProviderApp.class, args);
  }
}
