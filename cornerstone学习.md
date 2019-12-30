## cornerstone学习

#### 1.rabbitmq

##### 1.代码(模式1)

```java
<dependency>
     <groupId>com.rabbitmq</groupId>
     <artifactId>amqp-client</artifactId>
     <version>5.1.2</version>
</dependency>
```

```java
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitSendMQ {
    private final static String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws IOException, Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672); //默认端口号
        factory.setUsername("guest");//默认用户名
        factory.setPassword("guest");//默认密码
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 接下来，我们创建一个channel，绝大部分API方法需要通过调用它来完成。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello world";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("Sent '" + message);
        channel.close();
        connection.close();
    }
}


新建RecvMQ类，接收端
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import org.junit.Test;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitRecvMQ {
    private final static String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws IOException, Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received '" + message);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
```

##### 2.代码(模式2)

```java
public class RabbitProducer1 {
    private static final  String EXCHANGE_NAME = "exchange_demo";
    private static final  String ROUTING_NAME = "routingkey_demo";
    private static final  String QUEUE_NAME = "queue_demo";
    private static final  String IP_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //创建一个type=direct 的交换机
            channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
            //创建一个队列
            channel.queueDeclare(QUEUE_NAME, true,false,false,null);
            //将交换机和队列绑定
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_NAME);
            //发送消息
            String message = "Welcome to Delta SMDD team to work ";
            channel.basicPublish(EXCHANGE_NAME,ROUTING_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());

            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
======================================================
public class RabbitConsumer1 {
    private static final  String EXCHANGE_NAME = "exchange_demo";
    private static final  String ROUTING_NAME = "routingkey_demo";
    private static final  String QUEUE_NAME = "queue_demo";
    private static final  String IP_ADDRESS = "127.0.0.1";
    private static final  int PORT = 5672;

    public static void main(String[] args) {

        Address[] address = new Address[]{new Address(IP_ADDRESS,PORT)};


        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("guest");
            factory.setPassword("guest");
            Connection connection = factory.newConnection(address);
            Channel channel = connection.createChannel();
            //设置最多未被接收的消息数量
            channel.basicQos(64);
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    System.out.println("received:"+ new String(body));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            };

            channel.basicConsume(QUEUE_NAME,consumer);
            TimeUnit.SECONDS.sleep(5);
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

```

##### 3.代码(模式3)

```
 
```

##### 4.四种模式

```plsql
fanout：
    将所有消息路由到与他绑定的queue。
	--即只需提供队列,使用默认的交换机,生产和消费都指定队列
deirect:
    将消息由那些binding key 与 routing key完全匹配的queue中
    --1.先声明交换机,channnel.exchangeDeclare
    --2.在声明队列,channel.queueDeclare
    --3.然后交换机绑定队列,还要设定一个routingkey(路由键                                                                                                                                                                         ):channel.queueBind
    --4.发送消息
    --channel.basicPublish(EXCHANGE_NAME,ROUTING_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes())
topic：
   binding key中可以存在两种特殊字符“*”与“#”，用于做模糊匹配
   “*”用于匹配一个单词，“#”用于匹配多个单词（可以是零个）。
header:
   Exchange不依赖于routing key与binding key的匹配规则来路由消息，而是根据发送的消息内容中的headers属性进行匹配。
```

删除队列

```java
public class DelteteExchange {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDelete("test_exchanges_queue1");
            channel.exchangeDelete("test_exchanges_queue2");
            channel.exchangeDelete("test_exchanges_queue3");
            channel.exchangeDelete("test_exchanges_queue4");

            channel.queueDelete("test_exchanges_queue4");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
```

##### 5.添加用户和角色

```
Enter the sbin folder under RabbitMQ server installation directory (e.g. D:\Application\rabbit\rabbitmq_server-3.6.12\sbin) by the cmd window. 

查询用户
rabbitmqctl.bat list_users

添加用户
rabbitmqctl.bat add_user username password

文档是这样操作
rabbitmqctl.bat add_user admin admin                                    --设置用户
rabbitmqctl.bat set_user_tags admin administrator                       --添加角色
rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"                 --添加权限
======================================================
```

```
rabbitmq用户角色可分为五类：超级管理员, 监控者, 策略制定者, 普通管理者以及其他
(1) 超级管理员(administrator)
可登陆管理控制台(启用management plugin的情况下)，可查看所有的信息，并且可以对用户，策略(policy)进行操作。
(2) 监控者(monitoring)
可登陆管理控制台(启用management plugin的情况下)，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等) 
(3) 策略制定者(policymaker)
可登陆管理控制台(启用management plugin的情况下), 同时可以对policy进行管理。
(4) 普通管理者(management)
仅可登陆管理控制台(启用management plugin的情况下)，无法看到节点信息，也无法对策略进行管理。
(5) 其他的
无法登陆管理控制台，通常就是普通的生产者和消费者。
```

```
给admin用户添加超级管理员角色
rabbitmqctl.bat set_user_tags username administrator
设置权限
rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*" 
修改密码
rabbitmqctl change_password userName newPassword
删除角色信息
删除用户信息：
rabbitmqctl.bat delete_user username
```

##### 6.解析

```plsql
--声明一个交换机 ：exchangeDeclare
exchange：交换机名     type：交换机类型，如fanout（广播），direct（直连），topic（主题），head
derable：是否持久化    autoDelete：自动删除     internal：是否是内置，如果是true，只能通过交换机路由到交换机的模式
argument：参数
--声明一个队列：queueDeclare
queue：队列名          durable：是否持久化      exclusive：是否排他，
autoDelete：自动删除   argument：参数
--绑定交换机和队列：queueBind
queue：队列名          exchange：交换机名       routingkey：路由键        argument：参数
--交换机绑定交换机：exchangeBind
Demo:
   channel.exchangeDeclare("source","direct",true,false,null)
   channel.exchangeDeclare("destination","fanout",false,true,null)
   channel.exchangeBind("destination","source","exKey")              --通过路由键找到另一台匹配的交换机
   channel.queueDeclare("queue",false,false,true,null)
   channel.queueBind("queue","destination","")
   String abc = "exToExDeom"
   channel.basicPublish("source","exKey","",abc.getByte())
=====================================
                高端水平
=====================================
1.发送消息，为了更好控制，mandatory
channel.basicPublish（exchangeName，routingKey，mandatory，MessageProperties。PERSISTENT_TEXT_PLAIN,messageBodyBytes）

```

##### 7.消费消息

推模式

```java
Boolean autoAck = false;
channel.basicQos(64);
channel.basicConsume(queueName,autoAck,"myConsumerTag",new DefaultConsumer(){
   @Override
  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
       //打印消息
       System.out.println("received:"+ new String(body));
      
       String routingKey = envelope.getRoutingKey();
       String connectType = properties.getConnectType();
       long deliveryTag = envelope.getDeliveryTag()
       channel.basicAck(deliveryTag,false)
       }
})
```

拉模式

```java
通过channel.basicGet单条的获取消息
GetResponse response = channel.basicGet(queueName,false);
打印内容
System.out.println(new String（response.getBody()）);
channel.basicAcck(response.getEnvelope().getDeliveryTag(),false);
```

消息的确认和拒绝

```plsql
rabbitMQ提供了消息确认机制，消费者在订阅队列时，可以指定autoAck参数
--autoAck为False时，MQ会等待消费者显式回复确认信号后才从内存中删除消息
--autoAck为True时，MQ会直接把消息从内存中删除，不管消费之是否真的删除
```



##### 虚拟机安装rabbitMQ

```
从官网下载安装包rabbitmq-server-3.7.2-1.el7.noarch.rpm和otp_src_20.1.tar.gz =>版本要对应,不一定是这个版本
1.[root@localhost /]# tar -xvf otp_src_20.1.tar.gz
2.[root@localhost ~]# cd otp_src_20.1
3.[root@localhost  otp_src_20.1]# ./configure --prefix=/usr/local/erlang --without-javac 
4.[root@localhost  otp_src_20.1]# make && make install
5.修改环境变量
  [root@localhost /]# vim /etc/profile  

    ERL_HOME=/usr/local/erlang 
    PATH=$ERL_HOME/bin:$PATH 
    export ERL_HOME PATH
 6.配置生效
 [root@localhost /]# source /etc/profile
 7.验证erlang
 [root@localhost /]# erl
```

```
启动:以守护进程的方式在后台运行
rabbitmq-server -detached
```







##### 课外

```
在实际应用中，可能会发生消费者收到Queue中的消息，但没有处理完成就宕机（或出现其他意外）的情况?
可以要求消费者在消费完消息后发送一个回执给RabbitMQ，RabbitMQ收到消息回执（Message acknowledgment）后才将该消息从Queue中移除；如果RabbitMQ没有收到回执并检测到消费者的RabbitMQ连接断开，则RabbitMQ会将该消息发送给其他消费者（如果存在多个消费者）进行处理。这里不存在timeout概念，一个消费者处理消息时间再长也不会导致该消息被发送给其他消费者，除非它的RabbitMQ连接断开。

我们可以将Queue与Message都设置为可持久化的（durable），这样可以保证绝大部分情况下我们的RabbitMQ消息不会丢失。

Prefetch count
我们设置prefetchCount=1，则Queue每次给每个消费者发送一条消息；消费者处理完这条消息后Queue会再给该消费者发送一条消息。
==================
一般情况下：我们是通过消费者发消息给exchange，指定routing key，当binding key与routing key相匹配时，消息就会被路由到对应的queue.
```

