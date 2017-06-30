package com.alibaba.boot.dubbo.consumer;

import com.alibaba.boot.dubbo.service.IHelloService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;

/**
 * @author edliao on 2017/6/30.
 * @description TODO
 */
@Controller
public class HelloController {

  @Reference(version = "0.0.1")
  IHelloService iHelloService;

  public void login() {
    System.out.println(iHelloService.hello()+" "+iHelloService.welcome("eddy"));
  }
}
