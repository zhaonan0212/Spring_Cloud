### kafka學習

#### 1.介紹

#####  1.特點

```properties
1.同时为分布和订阅提供高吞吐量
2.可进行持久化操作，将消息持久化到到磁盘，以日志的形式存储
3.分布式系统，易于向外拓展。所有的Producer、broker和consumer都会有多个
4.消息被处理的状态是在consumer端维护，而不是由server端维护，当失败时能自动平衡。
5.支持Online和offline的场景。
```

#####  2.應用場景

```
1.消息隊列
2.定位跟蹤
3.日誌收集
4.流處理
5.事件源
6.持久性日誌
```

##### 3.依賴

```xml
<dependency>
     <groupId>org.apache.kafka</groupId>
     <artifactId>kafka_2.11</artifactId>
     <version>1.0.1</version>
</dependency> 
<dependency>
     <groupId>org.apache.kafka</groupId>
     <artifactId>kafka-clients</artifactId>
     <version>1.0.1</version>
</dependency>
```



#### 2.kafka的基本認識

#####  1.zookeeper的安裝

```java
1.全程下一步就ok
2.修改名字，zoo.cfg
3.z修改日誌的位置： dataDir=D:\\Application\\kafka_2.11-2.1.1\\data\\logs\\zookeeper_log
```

#####  2.kafka的安裝

```properties
1.修改配置文件server.properties
log.dirs=D:\\Application\\kafka_2.11-2.1.1\\data\\logs\\kafka_log
2.添加自動刪除server.properties
  auto.create.topics.enable = false      
  delete.topic.enable=true    
3.修改日誌的位置zookeeper.properties
log.dirs=D:\\Application\\zookeeper\\data\\logs\\zookeeper_log
```

##### 3.語法    全程cmd

```java
1.啟動/運行：
.\bin\windows\kafka-server-start.bat .\config\server.properties  
2.創建主題：
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic qianqian 
3.展示全部主題
.\bin\windows\kafka-topics.bat --list --zookeeper localhost:2181
4.開啟生產者
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic qianqian
5.開啟消費者
.\bin\windows\kafka-console-consumer.bat  --bootstrap-server localhost:9092 --topic qianqian  --from-beginning
  如果沒有 --from-beginning    輸入什麼打什麼
  如果有 --from-beginning      以前的消息也會打印
6.刪除topic
.\bin\windows\kafka-topics.bat --zookeeper localhost:2183 --delete --topic qianqian
```

##### 4.關鍵字介紹

```vue
1.Broker:缓存代理，Kafka集群中的一台或多台服务器统称broker.
2.注意：
  Kafka的设计原理决定，对于一个topic，同一个group(裏面可以有多個消費者)不能多于partition个数的consumer同时消费，否则将意味着某些consumer无法得到消息
3.Consumer Group：同一個topic，會廣播給不同的group（group可以有一個或多個consumer),但是同一個group小組里，只有一個consumer可以消費該消息
4.partition是有序的，如果broker的id是0，那么就是0-1，0-2，0-3，(假設三個分區)
5.每個partition里面有多個replication(副本)，相當于多個備份
當broker挂掉的時候，系統會製動的使用replicas提供服務，
創建topic的時候，自動會默認replication的系數是1，也可以自己設定。
6.replication leader;一個partition里面可以多個replication,但是只能有一個replication leader，來
關聯producer和consumer
7.replication的特點：
   1.replication的基本單位是topic的partition
   2.所有的讀和寫操作都由leader管理，followers只是作為備份
   3.follower必須及時的複製leader的數據，如果leader當機了，那麼followers中的一台會自動變成leader。
8.作為消息隊列，模式有兩種，
   1.隊列模式：consumers可以同時從服務器讀取消息，每個消息只能被一個consumer讀到
   2.發佈訂閱模式：消息可以被所有的consumer讀到
```

##### 5.報警 

1.系統找不到主類

```
修改bin/window/kafka-run-properties的179行，在classpath上面加上雙引號，如下
set COMMAND=%JAVA% %KAFKA_HEAP_OPTS% %KAFKA_JVM_PERFORMANCE_OPTS% %KAFKA_JMX_OPTS% %KAFKA_LOG4J_OPTS% -cp "%CLASSPATH%" %KAFKA_OPTS% %*
```

2.另一个程序正在使用此文件，进程无法访问。 

```java
Suppressed: java.nio.file.FileSystemException: D:\Application\kafka_2.11-2.1.1\data\logs\kafka_log\__consumer_offsets-9\00000000000000000000.timeindex.cleaned -> D:\Application\kafka_2.11-2.1.1\data\logs\kafka_log\__consumer_offsets-9\00000000000000000000.timeindex.swap: 另一个程序正在使用此文件，进程无法访问。
=============解決方法===============
 D:\Application\kafka_2.11-2.1.1\data\logs\kafka_log里裡面所有文件全部刪除
```



#### 3.kafka集群

##### 1.配置文件

```java
1.複製三份kafka的配置文件，server.properties，修改名字
server.properties   server-1.properties   server-2.properties
2.分別修改對應的內容
   config/server-1.properties                           config/server-2.properties
   broker.id = 1                                        broler.id = 2
   listeners = PLANTEXT://:9093                         listeners = PLANTEXT://:9094 
   log.dir = D:\\appllication\\kafka 2.11\\data\\log1   log.dir = D:\\appllication\\kafka 2.11\\data\\log2 
```

##### 2.啟動kafka集群 全程cmd

```properties
1.首先啟動zookeeper
 zkserver                    ==binding to port 0.0.0.0/0.0.0.0:2181，啟動成功
2.啟動kafka
 .\bin\windows\kafka-server-start.bat .\config\server.properties  
 .\bin\windows\kafka-server-start.bat .\config\server-1.properties  
 .\bin\windows\kafka-server-start.bat .\config\server-2.properties  
 
 [KafkaServer id=0] started (kafka.server.KafkaServer)    ==即開啟成功
3.創建主題  一個分區，三個副本
 .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic zhaonan
4.查看主題
 .\bin\windows\kafka-topics.bat --describe --zookeeper localhost:2181 --topic zhaonan
  注解：leader 負責給定分區的所有讀寫操作的節點
               replicas 複製此分區的日誌的節點列表
               isr 是一組同步副本
5.開啟生產者
 .\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic zhaonan
6.開啟消費者
 .\bin\windows\kafka-console-consumer.bat  --bootstrap-server localhost:9092 --topic zhaonan  --from-beginning
 .\bin\windows\kafka-console-consumer.bat  --bootstrap-server localhost:9093 --topic zhaonan  --from-beginning
 .\bin\windows\kafka-console-consumer.bat  --bootstrap-server localhost:9094 --topic zhaonan  --from-beginning
```

##### 3.測試程序

```java
//先創建接口，配置信息
public interface KafkaProperties
{
    final static String zkConnect = "127.0.0.1:2181";
    final static String groupId = "group1";
    final static String topic = "zhaonan";
    final static String kafkaServerURL = "127.0.0.1";
    final static int kafkaServerPort = 9092;
    final static int kafkaProducerBufferSize = 64 * 1024;
    final static int connectionTimeOut = 20000;
    final static int reconnectInterval = 10000;
   // final static String topic2 = "topic2";
   // final static String topic3 = "topic3";
    final static String clientId = "SimpleConsumerDemoClient";
}
```

生產者和消費者

```java
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @author leicui bourne_cui@163.com
 */
public class KafkaConsumer extends Thread
{
    private final ConsumerConnector consumer;
    private final String topic;

    public KafkaConsumer(String topic)
    {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig());
        this.topic = topic;
    }

    private static ConsumerConfig createConsumerConfig()
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", KafkaProperties.zkConnect);
        props.put("group.id", KafkaProperties.groupId);
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }

    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            System.out.println("receive：" + new String(it.next().message()));
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```



```java
import java.util.Properties;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * @author leicui bourne_cui@163.com
 */
public class KafkaProducer extends Thread
{
    private final kafka.javaapi.producer.Producer<Integer, String> producer;
    private final String topic;
    private final Properties props = new Properties();

    public KafkaProducer(String topic)
    {
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "127.0.0.1:9092");
        producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
    }

    @Override
    public void run() {
        int messageNo = 1;
        while (true)
        {
            String messageStr = new String("Message_" + messageNo);
            System.out.println("Send:" + messageStr);
            producer.send(new KeyedMessage<Integer, String>(topic, messageStr));
            messageNo++;
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
```

測試類

```java
public class KafkaConsumerProducerDemo{
    public static void main(String[] args){
        KafkaProducer producerThread = new KafkaProducer(KafkaProperties.topic);
        producerThread.start();

        KafkaConsumer consumerThread = new KafkaConsumer(KafkaProperties.topic);
        consumerThread.start();
    }
}
```

#### 4.沒有安裝可視化工具

