### elasticsearch学习

#### 1.介绍

```
Elasticsearch 简称es，es是一个开源的高扩展的分布式全文检索技术，它可以近乎实时的存储，检索数据。本身扩展性很好，可以扩展到上百台服务器，处理pb级别的数据，es也可以使用java开发，并使用luence作为核心来实现所有的索引和搜索的功能，但是他是通过简单的restful风格的api来隐藏luence的复杂性，从而让全文检索更简单
```

什么是pb级别的数据

```
1PB = 1024TB
数据单位B,KB,MB,GB,TB,PB
```

elasticsearch和solr的区别

```
1.solr是利用zookeeper进行分布式管理，而elasticsearch自带分布式管理功能
2.solr支持的数据类型比较多，json，xml，html。。。而elasticsearch只支持json
3.solr官方提供的功能比较多，而elasticsearch则提供很多的第三方插件，
4.solr查询快，但更新索引时慢（即插入删除慢），多用于电商等查询多的应用
  es建立索引快（即查询慢），实时性比较好，用于facebook,新浪等搜索
5.solr更成熟，有一个更大，更成熟的用户、开发和贡献者社区
  es相对开发维护者少，更新太快，学习使用成本高
```

#### 2.安装

```
官网下载：https://www.elastic.co/cn/downloads/elasticsearch

然后进入bin，点击es.bat开启服务。（bin是可执行的二进制文件）
```

##### 1.闪退问题

```
进入config/jvm.options,修改内存大小
-Xms2g
-Xmx2g
```

##### 2.注意

```
1.端口问题
  9300是tcp通讯端口，集群间和TCPClient都执行该端口，可供java程序调用
  9200是http协议的端口，restful调用
2.版本问题
  必须要jdk8以上
```

#### 3.安装可视化工具

```
1.从github上下载可视化工具
  https://github.com/mobz/elasticsearch-head
2.然后黑窗口 shift+右键
  npm install
3.安装node.js
  全程下一步，安装完成后查看是否安装成功
  node -v
  npm -v
4.全局安装grunt
  npm install -g grunt-cli
5.安装不上就用淘宝镜像
  npm install -g cnpm –registry=https://registry.npm.taobao.org
6.修改elasticsearch/config下的配置文件：elasticsearch.yml，  
  添加： http.cors.enabled: true
        http.cors.allow-origin: "*"
        network.host: 127.0.0.1
7.进入head目录，启动head，还是黑窗口
  grunt server
8.如果启动失败，报警unable to find local grunt
  npm install grunt 
  grunt server
```

#### 4.核心概念

##### 1.索引

```
一个索引就是一个拥有几分相似特征的文档的集合，一个索引由一个名字来标识（必须全部是小写字母的），并且当我们要对对应于这个索引中的文档进行索引、搜索、更新和删除的时候，都要使用到这个名字
```

##### 2.type类型

```
在一个索引中，你可以定义一种或多种类型。一个类型是你的索引的一个逻辑上的分类/分区
```

##### 3.docunment文档

```
文档是一个可以被索引的基础信息单元，文档以json的数据形式来表示，
```

##### 4.field字段

```
相当于是表的字段，对文档数据根据不同属性分类
```

##### 5.mapping映射

```
主要用来处理es里面的数据，是否分词，是否索引，是否存储。。。。。。
```

##### 6.node节点

```
一个节点是集群中的一个服务器，作为集群的一部分，它存储数据，参与集群的索引和搜索功能。
在一个集群里，只要你想，可以拥有任意多个节点。而且，如果当前你的网络中没有运行任何Elasticsearch节点，这时启动一个节点，会默认创建并加入一个叫做“elasticsearch”的集群。
```

##### 7.分片和复制shards&replicas

```
默认情况下，Elasticsearch中的每个索引被分片5个主分片和1个复制，这意味着，如果你的集群中至少有两个节点，你的索引将会有5个主分片和另外5个复制分片（1个完全拷贝），这样的话每个索引总共就有10个分片。
```

#### 5.入门操作

**我们使用的jar包还有分词都要 和es对应上，我用的都是5.6.16**

##### 1.创建索引

```java
 //创建索引
    @Test
    public void createIndex() throws Exception {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300)
        );

        //第二种：使用elasticsearch的api
        XContentBuilder bui1der = XContentFactory.jsonBuilder().startObject().field("id",1)
                .field("title","台达电子是世界IT产业世界500强之一")
                .field("content","台达是全球最大的电源供应器制造商，市场占有率达25%以上。公司每年以40%的速度快速成长，2003年全球营业额超过240亿人民币。目前在上海、天津、广东东莞、江苏吴江等地设有多座工厂，公司已在上海浦东成立电力电子研发中心，并在高频电源转换技术、创新线路拓扑技术、计算器辅助设计软件平台等领域取得了重大的进展。")
                
                .endObject();
        client.prepareIndex("blog1","article","1").setSource(bui1der).get();
        client.close();
    }
```

##### 2.查询全部

```java
//查询全部
    @Test
    public void findAll() throws Exception{
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300)
        );
        SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
                .setQuery(QueryBuilders.matchAllQuery()).get();
        SearchHits hits = searchResponse.getHits();
        System.out.println("共查询"+hits.getTotalHits()+"条");

        Iterator<SearchHit> ite = hits.iterator();
        while(ite.hasNext()){
            SearchHit searchHit = ite.next();
            System.out.println(searchHit.getSourceAsString());
            System.out.println(searchHit.getSource().get("title"));
        }
        client.close();
    }
```

##### 3.查询字符串

```java
 //字符串查询
    @Test
    public void searchString() throws Exception {
        //创建客户端访问对象
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );
        //设置查询条件
        SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
                .setQuery(QueryBuilders.queryStringQuery("世界500强").field("title"))
                .get();
        //处理结果
        SearchHits hits = searchResponse.getHits();
        System.out.println("共查询"+hits.getTotalHits()+"条");
        Iterator<SearchHit> ite = hits.iterator();
        while(ite.hasNext()){
            SearchHit searchHit = ite.next();
            System.out.println(searchHit.getSourceAsString());
            System.out.println(searchHit.getSource().get("title"));
        }
        //关闭资源
        client.close();
    }
```

##### 4.词条查询

```java
//词条查询
    @Test
    public void testCiTiao() throws  Exception{
        //创建客户端访问对象
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        //设置查询条件
        SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
                .setQuery(QueryBuilders.termQuery("title","台达")).get();

        //处理结果
        SearchHits hits = searchResponse.getHits();
        System.out.println("共查询"+hits.getTotalHits()+"条");
        Iterator<SearchHit> ite = hits.iterator();
        while(ite.hasNext()){
            SearchHit searchHit = ite.next();
            System.out.println(searchHit.getSourceAsString());
            System.out.println(searchHit.getSource().get("title"));
        }
        //关闭资源
        client.close();

    }
```

##### 5.模糊查询

```java
 //模糊查询
    public void searchLike() throws  Exception{
        //创建客户端访问对象
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        //设条件
        SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
                .setQuery(QueryBuilders.wildcardQuery("title","*台达*")).get();

        //处理结果
        SearchHits hits = searchResponse.getHits();
        System.out.println("共查询"+hits.getTotalHits()+"条");
        Iterator<SearchHit> ite = hits.iterator();
        while(ite.hasNext()){
            SearchHit searchHit = ite.next();
            System.out.println(searchHit.getSourceAsString());
            System.out.println(searchHit.getSource().get("title"));
        }
        //关闭资源
        client.close();
    }
```

6.删除索引

```

```

##### 7.下载IK分词器

```
https://github.com/medcl/elasticsearch-analysis-ik/releases

需要配置，IKAnalyzer.cfg.xml：配置扩展词典和扩展停止词典
```









### 后面需要用到的东西

1.自动化部署Ansible，jenkins

2.Logging 跟監控的framework 

3.ELK

ElasticSearch, Logstash, Kibana

 

