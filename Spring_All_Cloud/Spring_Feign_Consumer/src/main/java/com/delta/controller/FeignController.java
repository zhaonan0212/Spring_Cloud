package com.delta.controller;

import com.delta.domain.User;
import com.delta.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {

    @Autowired
    private FeignService feignService;

    @RequestMapping(value = "/feign-consumer", method = RequestMethod.GET)
    public String helloConsumer() {
        return feignService.hello();
    }

    @RequestMapping(value = "/feign-consumer2", method = RequestMethod.GET)
    public String helloConsumer2() {
        StringBuilder sb = new StringBuilder();
        sb.append(feignService.hello()).append("\n");
        sb.append(feignService.hello("张飞")).append("\n");
        sb.append(feignService.hello("关羽",22)).append("\n");
        sb.append(feignService.hello(new User("卢布",18))).append("\n");
        return  sb.toString();
    }
}
