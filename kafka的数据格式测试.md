## kafka的数据格式测试

#### 1.kafka的安装

**解压**

```
tar -xf kafka_2.11-2.1.1.tar
tar -zxvf kafka_2.11-2.1.1.tar.gz
```

**启动zookeeper**

```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

**启动kafka**

```
bin/kafka-server-start.sh config/server.properties
```

**创建主题**

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Deltaman

查询
bin/kafka-topics.sh --list --zookeeper localhost:2181
```

**生产者**

```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Deltaman
```

**消费者**

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Deltaman --from-beginning

============================================
 如果沒有 --from-beginning    輸入什麼打什麼
 如果有 --from-beginning      以前的消息也會打印
```

#### 2.kafka属性

**集群**

```
1.修改配置文件的名字
  server-1.properties   
  server-2.properties
2.修改配置文件內容
   config/server-1.properties                          config/server-2.properties
   broker.id = 1                                       broler.id = 2
   listeners = PLANTEXT://:9093                        listeners = PLANTEXT://:9094 
   log.dir = D:\\appllication\\kafka 2.11\\data\\log1
3.语句：一个分区三个副本
   bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic zhaonan
```

**关键字介绍**

```
1.Broker:缓存代理，一个broker对应一个kafka实例，Kafka集群中的一台或多台服务器统称broker.
2.注意：
  Kafka的设计原理决定，对于一个topic，同一个group(裏面可以有多個消費者)不能多于partition个数的consumer同时消费，否则将意味着某些consumer无法得到消息，其实partition就相当于负载均衡
  当partition和consumer数量相等时，效能最高，同一个partition不能被多个consumer消费
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

#### 3.数据测试

**数据**

```
{"PRODUCTNO" : "VFD037V23A","PRODUCTVERSION" : "0","PRODUCTNAME" : "AC MOTOR DRIVES 5HP 230","PRODUCTTYPE" : "V","ISSUESTATE" : "0","CREATOR" : "IMES","CREATEDATE" : "1249011691000","DESCRIPTION" : null,"CURVERSION" : "0","UNITNO" : "pcs","UNITTYPE" : null,
"SPECNO" : "N/A","PACKOIPATH" : null}
{"PRODUCTNO" : "VFD037V23A-2","PRODUCTVERSION" : "0","PRODUCTNAME" : "AC MOTOR DRIVES 5HP 230",
"PRODUCTTYPE" : null,"ISSUESTATE" : "0","CREATOR" : "IMES.PcbNo","CREATEDATE" : "1249011692000",
"DESCRIPTION" : null,"CURVERSION" : "0","UNITNO" : "pcs","UNITTYPE" : null,"SPECNO" : "N/A",
"PACKOIPATH" : "\\\\10.6.64.32\\Encoder  無紙OI 公用資料夾\\包裝\\MH4&NH4&MH3包裝110830-A4.xls"}
=======报警数据==================
{"PRODUCTNO" : "MH4-25LN65C3D","PRODUCTVERSION" : "00","PRODUCTNAME" : " INCREMENTAL ENCODER UVW 2500 PPR P10 36","PRODUCTTYPE" : "MH4","ISSUESTATE" : "0","CREATOR" : "48068959","CREATEDATE" : "1247465533000","DESCRIPTION" : "Encoder自製件版本","CURVERSION" : "0","UNITNO" : "pcs","UNITTYPE" : null,"SPECNO" : "N/A","PACKOIPATH" : "\\Iabu-sus\生技資料\ALL_OI\FI\B\VFD-B FRAME C.xls"}
=========基本全部包含===================
{"PRODUCTNO" : "VFD03:7V2==3--A","PRODUCTVERSION" : "0","PRODUCTNAME" : "AC=MOT'O'R @5HP$% 5H-_=P 230","PRODUCTTYPE" : "V","ISSUESTATE" : "(0)+[123]+{123}+<123>+'123'","CREATOR" : "IMES","CREATEDATE" : "12490116*^&#!~91000","DESCRIPTION" : "sdflsdfs<d>f","CURVERSION" : "0","UNITNO" : "pcs","UNITTYPE" : "dsff?|","SPECNO" : "N/A","PACKOIPATH" : " dsfff ;:',."}
```

**如何使用confluent的生产者制造消息**

```
#创建topic
bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic deltaman

#生产消息
bin/kafka-console-producer --broker-list localhost:9092 --topic deltaman 

#消费消息  直接捆绑消费者组
bin/kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --group test-consumer-group --topic deltaman 

#清空offset
bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group test-consumer-group --topic deltaman --reset-offsets --to-earliest --execute

bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group test-consumer-group --describe
bin/kafka-topics --delete --zookeeper localhost:2181 --topic deltaman
```

**消费者组**

```
consumer group是kafka提供的可扩展且具有容错性的消费者机制，既是一个组，必然包含多个消费者或者消费者实例，他们共享一个id，即group ID，
组内的所有消费者协调在一起来消费订阅主题（subscribed topics）的所有分区 partition
每个分区只能由同一个消费者组的一个consumer来消费
```

```
kafka server：
  IP:10.148.206.21
  用户：addmes
        mesrearchi123
目录		
/home/addmes/confluent-5.3.1		
#消费者组列表
bin/kafka-consumer-groups --bootstrap-server localhost:9092 --list 
#消费者消费的topic详情
bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group nifi-devcluster --describe
#清空offset
bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group nifi-devcluster --topic MES-IT__DGRAMES__DG_MES__C_EQUIPMENT_SYNC_T --reset-offsets --to-earliest --execute  
#使用的两个消费者组
--group nifi-dev-49
--group nifi-devcluster 
```






