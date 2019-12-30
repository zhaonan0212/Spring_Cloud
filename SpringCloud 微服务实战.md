## SpringCloud 微服务实战

### springboot

#### 1.理解注解的作用

**@RequestBody和@ResponseBody**

```
@RequestMapping(value = "person/login")
@ResponseBody                       //将方法返回的对象转换为json对象
public Person login(@RequestBody Person person) {   // 将请求中的 json数据 写入 Person 对象中
    return person;    // 不会被解析为跳转路径，而是直接写入 HTTP 响应正文中
}
```

```
方法的注入@Bean
类的注入@Component
对象的使用@Autowired
```

```
@Controller 声明此类是MVC的类,与@RequestMapping一起使用
@service  声明此类是业务处理类,与@Transactional一起使用
@RestController  用于RestFul服务
@Component  controller,service,dao除外的类
@Configuration   配置类,通常与@bean配合使用

```

**Aop的使用**

```java
第一种情况
@Configuration                 //引起spring的注意
@Aspect                        //这是一个AOP类
public class xxxx{
    
    @Around("within(@org.springframework.stereotype.controller)")    //可以理解为所有使用controller注解的类使用Aop
    public void xxxx(){
        return xxxx
    }
}
```

```java
第二种情况
@Aspect
@Component                                    //定义为组件,注入到容器
public class WeixinServiceAspect {

    @Pointcut("execution(public String hello())")
    public void shareCut(){
        System.out.println("========切123=========");                      //这个信息不回打印
    }

    @AfterReturning("shareCut()")
    public void log(){
        System.out.println("=========切456==========");                   //切点
    }

    @AfterReturning("execution(public String hello())")
    public void log1(){
        System.out.println("=========qie79==========");
    }

}
```

**SpringMVC流程图**

```
1.用户发请求到前端控制器Dispatcherservlet
2.收到请求后,通过handler mapping去查找handler
3.handler Mapping 根据url找到具体的handler,生成处理器对象和处理拦截器,一并返回给dispatcherservlet
4.dispatcher通过handleradapter适配器,调用具体的处理器handler,返回moudleandview
5.dispatcher将结果给到view Resolver视图解析器进行解析,返回view
6.dispatcher将视图渲染,
7.最后响应前端页面
```

**@RequestMappling中value**

**PageInfo分页**

```java
==================controller======================
@RequestMapping(value="/user/{id}")
@ResponseBody
public String getUser(@PathVariable Long id){                            // @PathVariable作用在方法参数上,表示来自于url,参数是假的
        PageInfo<SysDevelopManaUser> pageInfo = userService.pageHelperList(page, size);
      //  model.addAttribute(pageInfo);
        model.addAttribute("user",userInfo);
        return "user-list";                          //返回到前端页面
}
==================serivce==========================
public PageInfo<SysDevelopManaUser> pageHelperList(int page, int size) {
        PageHelper.startPage(page, size);
        List<SysDevelopManaUser> userList = userDao.findAll();
        PageInfo<SysDevelopManaUser> pageInfo = new PageInfo<>(userList);
        return pageInfo;
    }
```

**锁定http的javaBean参数**

```java
public String getUsers(@RequestParam(name="id",reuqierd=true) Integer id,String name){
    return "success";
}
```

**Transactionnl事务**

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
            <scope>test</scope>
        </dependency>
```

**修改springboot的启动图片**

```
第一种方法
添加banner.txt在resources下面
${AnsiColor.BRIGHT_YELLOW}  
////////////////////////////////////////////////////////////////////  
//                          _ooOoo_                               //  
//                         o8888888o                              //  
//                         88" . "88                              //  
//                         (| ^_^ |)                              //  
//                         O\  =  /O                              //  
//                      ____/`---'\____                           //  
//                    .'  \\|     |//  `.                         //  
//                   /  \\|||  :  |||//  \                        //  
//                  /  _||||| -:- |||||-  \                       //  
//                  |   | \\\  -  /// |   |                       //  
//                  | \_|  ''\---/''  |   |                       //  
//                  \  .-\__  `-`  ___/-. /                       //  
//                ___`. .'  /--.--\  `. . ___                     //  
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //  
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //  
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //  
//      ========`-.____`-.___\_____/___.-`____.-'========         //  
//                           `=---='                              //  
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //  
//            佛祖保佑       永不宕机      永无BUG                　　//
//////////////////////////////////////////////////////////////////// 
第二种方法,
添加banner.png图片到resources下面

application.properties:
banner.charset=UTF-8
banner.location=classpath:banner.txt
banner.image.location=classpath:banner.png
banner.image.width=76
banner.image.height=76
banner.image.margin=2
```

### SpringCloud微服务

#### 1.eureka服务

##### pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <groupId>com.delta</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>Spring_Eureka_Server</artifactId>
    <packaging>war</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.7.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Brixton.SR5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
   ......

</project>
```

##### 端口占用

```
查看8080端口是否被占用,如果被占用,无法启动
netstat -ano | findstr "8080"
查找到pid后,进入service,关掉服务.
也可以用命令关掉:taskkill /pid 8756 /f
```

##### 配置文件

```
server.port=1111

eureka.client.fetch-registry=false
eureka.client.register-with-eureka=false
eureka.instance.hostname=localhost
```

##### 代码

```
开启注册中心
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication{
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class,args);
    }
}
==========================
访问:localhost:1111
```

#### 2.生产者

##### pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">


    <groupId>com.delta</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>Hello_Server</artifactId>
    <packaging>war</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.7.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Brixton.SR5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ......
</project>    
```

##### 配置文件

```
server.port=1112

spring.application.name=hello-service
eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/
```

##### 代码

```
public class User {
    private String name;
    private Integer age;

  ......
}
```

```
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
```

```
@EnableDiscoveryClient
@SpringBootApplication
public class HelloApplication{
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class,args);
    }
}

```

#### 3.ribbon消费者

##### pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <groupId>com.delta</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>Ribbon_Consumer</artifactId>
    <packaging>war</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.7.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>

    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Brixton.SR5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ......
</project>    
```

##### 配置文件

```
server.port=8083

spring.application.name=ribbon-service
eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/
```

##### 代码

不用关注hello服务的具体端口,我直接去注册中心拿

```
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/ribbon-consumer",method = RequestMethod.GET)
    public String helloConsumer(){
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello",String.class).getBody();
    }
}
```

```
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class,args);
    }

    @Bean            //注入到容器中,给controller使用
    @LoadBalanced   //主要用来负载均衡
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }
}
```

#### 4.Feign调用

##### pom.xml

```java
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <groupId>com.delta</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>Spring_Feign_Consumer</artifactId>
    <packaging>war</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.7.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Brixton.SR5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ......
</project>             
```

##### 配置文件

```properties
server.port=8085

spring.application.name=feign-service
eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/
```

##### 代码

1.feign一般和hystrix一起使用,防止调用过程中服务宕机

2.还需要复制被调用服务的domain

```xml
//重点,此处是需要调用的服务代码

@FeignClient("hello-service")
public interface FeignService {

    @RequestMapping("/hello")
    String hello();

    @RequestMapping(value = "/hello1",method = RequestMethod.GET)
    String hello(@RequestParam("name") String name);

    @RequestMapping(value = "/hello2",method = RequestMethod.GET)
    User hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age);

    @RequestMapping(value = "/hello3",method = RequestMethod.POST)
    String hello(@RequestBody User user);
}
```

```
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
```

```
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class FeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class,args);
    }
}

```

#### 5.hystrix熔断器

##### pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">


    <groupId>com.delta</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>Spring_Hystrix_server</artifactId>
    <packaging>war</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.7.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Brixton.SR5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ......
</project>
```

##### 配置文件

```
server.port=8084

spring.application.name=hystrix-service
eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/
```

##### 代码

```
@Service
public class HystrixService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")       //如果服务宕机,调用其他服务
    public String helloService(){
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello",String.class).getBody();
    }

    public String helloFallback(){
        return "error";
    }
}
```

```
@RestController
public class HystrixController {

    @Autowired
    private HystrixService hystrixService;

    @RequestMapping(value = "/hystrix",method = RequestMethod.GET)
    public String helloConsumer(){
        return    hystrixService.helloService();
    }
}
```

```
@SpringBootApplication
@EnableCircuitBreaker          //hystrix的注解
@EnableDiscoveryClient
public class HystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class,args);
    }

    @Bean                      //注入服务
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

#### 6.zuul服务-网关

##### pom.xml

```yaml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <groupId>com.delta</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>Spring_API_Zuul</artifactId>
    <packaging>war</packaging>


    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.7.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>

    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Brixton.SR5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ......
</project>

```

##### 配置文件

这个服务就是更换了我的请求路径

```yaml
server.port=8086
spring.application.name=api-zuul

zuul.routes.api-a.path=/api-a/**                         =>**后面就是controller里面的RequestMapping,具体的方法
zuul.routes.api-a.service-id=hello-service               结果:/api-a/hello => /hello-service/hello

zuul.routes.api-b.path=/api-b/**
zuul.routes.api-b.service-id=feign-service

eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/
```

##### 代码

```java
@SpringCloudApplication
@EnableZuulProxy
public class ZuulApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
    }
}
```

##### zuul还可以做过滤器

请求的时候最后面必须添加token       /hello?accessToken=Token

###### 修改启动类

```
@SpringCloudApplication
@EnableZuulProxy
public class ZuulApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
    }

    //添加过滤器
    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }
}
```

###### controller添加过滤器

```
public class AccessFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        log.info("send {} request to {}",request.getMethod(),request.getRequestURL().toString());

        System.out.println(request.getRequestURL().toString());
        Object accessToken = request.getParameter("accessToken");
        if (accessToken == null){
            log.warn("access token is empty");
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(401);
            return  null;
        }

        log.info ("access token ok");
        return  null;
    }

}
```

#### 7.config

#### 8.bus





##### 









