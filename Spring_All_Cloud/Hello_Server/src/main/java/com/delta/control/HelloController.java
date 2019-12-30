package com.delta.control;

import com.delta.domain.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;


@RestController
public class HelloController {
    private  final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public  String index(){
        ServiceInstance instance = client.getLocalServiceInstance();
        logger.info("/hello,host:" + instance.getHost() + ",service_id:" + instance.getServiceId());
        return  "hello world";
    }

    @RequestMapping(value = "/hello1",method = RequestMethod.GET)
    public  String hello(@RequestParam String name){

        return  "hello world"+ name;
    }

    @RequestMapping(value = "/hello2",method = RequestMethod.GET)
    public  User hello(@RequestHeader String name,@RequestHeader Integer age){

        return  new User(name,age);
    }

    @RequestMapping(value = "/hello3",method = RequestMethod.POST)
    public  String hello(@RequestBody User user){

        return  "hello world"+user.getName()+user.getAge();
    }
}
