package com.alibaba.boot.dubbo.provider.impl;

import com.alibaba.boot.dubbo.service.IHelloService;
import com.alibaba.dubbo.config.annotation.Service;

@Service(version = "0.0.1")
public class HelloServiceImpl implements IHelloService {

  @Override
  public String hello() {
    return "Hello";
  }

  @Override
  public String welcome(String name) {
    return "Welcome ," + name;
  }
}
