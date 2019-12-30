### solr學習

##### 1.下載

```
http://www.apache.org/dyn/closer.lua/lucene/solr/8.0.0
```

##### 2.windows下啟動

```
1.找到start.jar文件将其放入example目录下
  如果沒有，就把server目录下的start.jar复制了一份放进去
2.solr命令：    java -jar start.jar 也是启动
  1. 启动：bin\solr.cmd start 
  2. 停止：bin\solr.cmd stop 或bin\solr.cmd stop -all 
  3. 查看：bin\solr.cmd status
3.訪問：
  http://localhost:8983/solr/#/  
```

##### 3.部署到tomcat里

  此時安裝最新版solr-8.0.0無法部署到tomcat里，所有的低版本地址

```
地址：http://archive.apache.org/dist/lucene/solr/
```

solr3.10.4部署到tomc具體步驟

```xml
1.運行war包
把solr目錄下的D:\Application\solr-4.10.3\example\webapps   solr.war複製到一個新的tomcat的webapp中，啟動tomcat
2.啟動報警，缺少jar包
複製D:\Application\solr-4.10.3\example\lib\ext目錄下的所有jar包，給到
D:\Application\apache-tomcat-solr\webapps\solr\WEB-INF\lib目錄下，
3.配置索引庫
修改tomcat/webapp/web.xml,配置索引庫
    <env-entry>
       <env-entry-name>solr/home</env-entry-name>
       <env-entry-value>D:\Application\solr_home</env-entry-value>
       <env-entry-type>java.lang.String</env-entry-type>
    </env-entry>
4.創建索引庫
新建文件夾，叫solr_home,複製solr的example/solr裡面所有文件，collection就是solrcore
我們也可以新建文件夾，修改配置文件，core.properties
5.啟動
http://localhost:8080/solr/#/
```

##### 4.IK分詞器

```xml
1.導入jar包
把ik的jar包複製到tomcat/webapp/solr/webinfo/lib
2.ik動態分詞配置
把ikanalyzer.cfg、mydict、ext_stopword都複製到webinfo下新建的classes文件夾里
3.在配置文件schema.xml中配置Ik分詞器
    <fieldType name="text_ik" class="solr.TextField">
		<analyzer class="org.wltea.analyzer.lucene.IKAnalyzer"/>
	</fieldType>
<field name="item_title" type="text_ik" indexed="true" stored="true"></field>
======================
分词：就是整体匹配还是单独匹配。
索引：是否能够搜索到
存储：如果前端页面要展示，就要存储
```

##### 5.配置文件

1.schema.xml   ==>索引域

```xml
<!-- IK中文分词器 -->
<fieldType name="text_ik" class="solr.TextField">
    <analyzer class="org.wltea.analyzer.lucene.IKAnalyzer"/>
</fieldType>
<!-- 配置域 字段 所有name必须为小写 -->
<!-- goodsid type 为 long 表示不分词-->
<field name="item_goodsid" type="long" indexed="true" stored="true"/>
<!--  title type 为Ik 表示这个字段会按照ik分词器规则进行分词 -->
<field name="item_title" type="text_ik" indexed="true" stored="true"/>
<field name="item_price" type="double" indexed="true" stored="true"/>
<!--  type 为 String 也不分词-->
<field name="item_image" type="string" indexed="false" stored="true" />
<field name="item_category" type="string" indexed="true" stored="true" />
<field name="item_seller" type="text_ik" indexed="true" stored="true" />
<field name="item_brand" type="string" indexed="true" stored="true" />
<!-- 复制域 本身不存储数据 映射source中的数据 查询keyword(关键字)就可一起查询title category seller brand -->
<field name="item_keywords" type="text_ik" indexed="true" stored="false" multiValued="true"/>
<copyField source="item_title" dest="item_keywords"/>
<copyField source="item_category" dest="item_keywords"/>
<copyField source="item_seller" dest="item_keywords"/>
<copyField source="item_brand" dest="item_keywords"/>
<!-- 动态域 字段不确定时 定义动态域 -->
<dynamicField name="item_spec_*" type="string" indexed="true" stored="true" />	
```

详细介绍

```xml
schema.xml
===================================================================
solr域字段配置：
<field name="id" type="string" indexed="true" stored="true" required="true" multiValued="false" /> 
indexed：是否索引。含义：是否可以根据该字段进行搜索操作。true可以根据该字段搜索，false，不能基于该字段进去全文检索。
stored：是否存储
============================================
<dynamicField name="*_i"  type="int"    indexed="true"  stored="true"/>
动态域：作用：方便配置域字段。方便为不确定的域字段动态赋值。
<dynamicField name="item_i"  type="int"    indexed="true"  stored="true"/>
<dynamicField name="item1_i"  type="int"    indexed="true"  stored="true"/>
<dynamicField name="item2_i"  type="int"    indexed="true"  stored="true"/>
=========================================================
<copyField source="cat" dest="text"/>
复制域：解决多字段搜索功能。
<field name="item_keywords" type="text_ik" indexed="true" stored="false" multiValued="true"/>
		<copyField source="item_title" dest="item_keywords"/>
		<copyField source="item_category" dest="item_keywords"/>
		<copyField source="item_seller" dest="item_keywords"/>
		<copyField source="item_brand" dest="item_keywords"/>
翻译过来就是:搜索的是 dest="item_keywords"
结果来源是:source="item_title"
          source="item_category"
          source="item_seller"
          source="item_brand"
```

##### 6.springdata整合solr

1.applicationContext-solr.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:solr="http://www.springframework.org/schema/data/solr"
       xsi:schemaLocation="http://www.springframework.org/schema/data/solr 
                 http://www.springframework.org/schema/data/solr/spring-solr-1.0.xsd
                           http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring-context.xsd">
    
    <!-- solr服务器地址 -->
    <solr:solr-server id="solrServer" url="http://127.0.0.1:8080/solr/collection1" />
    
    <!-- solr模板，使用solr模板可对索引库进行CRUD的操作 -->
    <bean id="solrTemplate" class="org.springframework.data.solr.core.SolrTemplate">
        <constructor-arg ref="solrServer" />
    </bean>
    
</beans>

```

2.依赖

```
<dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-solr</artifactId>
        <version>1.5.5.RELEASE</version>
 </dependency>

```

3.pojo ==》将数据库的数据存到索引库

```java
public class TbItem implements Serializable{
	//对应主键 id
    @Field
    private Long id;
	//对应item_title
    @Field("item_title")
    private String title;

    @Field("item_price")
    private BigDecimal price;

    @Field("item_image")
    private String image;

    @Field("item_goodsid")
    private Long goodsId;

    @Field("item_category")
    private String category;

    @Field("item_brand")
    private String brand;

    @Field("item_seller")
    private String seller;
    
    //@Dynamic + @Field 表示配置动态域
    //需要一个map属性 map中的key代替* map中的value对应域的值
    //例如 {"内存":"8g","尺寸":"5.5寸"}
    //item_spec_内存:8g
    //item_spec_尺寸:5.5寸
    //适合不确定的数据 因为SKU的规格不确定
    @Dynamic
    @Field("item_spec_*")
    private Map<String,String> specMap;
===================
    下面是set/get方法
```

4.测试

log4j.properties

```
# Set root category priority to INFO and its only appender to CONSOLE.
#log4j.rootCategory=INFO, CONSOLE            debug   info   warn error fatal
log4j.rootCategory=debug, CONSOLE                            

# Set the enterprise logger category to FATAL and its only appender to CONSOLE.
log4j.logger.org.apache.axis.enterprise=FATAL, CONSOLE

# CONSOLE is set to be a ConsoleAppender using a PatternLayout.
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{ISO8601} %-6r [%15.15t] %-5p %30.30c %x - %m\n
```

代码

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext*.xml")

public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 添加索引
     */
    @Test
    public void addItem() {

        TbItem item = new TbItem();
        item.setId(2L);
        item.setTitle("华为荣耀20 移动3G 64G");
        item.setBrand("华为");
        item.setSeller("华为旗舰店1");

        //保存数据到索引库
        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    /**
     * 删除一个
     */
    @Test
    public void deleteById() {
        solrTemplate.deleteById("1L");
        solrTemplate.commit();
    }

    /**
     * 删除全部
     */
    @Test
    public void deleteAll() {
        SolrDataQuery solrDataQuery = new SimpleQuery("*:*");
        solrTemplate.delete(solrDataQuery);
        solrTemplate.commit();
    }
     /**
     * 循环插入100条
     */
    @Test
    public void addBatchItem() {
        List<TbItem> itemList = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            TbItem tbItem = new TbItem();
            tbItem.setId(i);
            tbItem.setTitle(i + "苹果手机质量杠杠滴");
            tbItem.setBrand("苹果");
            tbItem.setSeller("华为" + i + "旗舰店");
            tbItem.setCategory("电子产品");
            tbItem.setPrice(new BigDecimal(5600));
            itemList.add(tbItem);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    /**
     * 分页查询
     */
    @Test
    public void pageQueryTest() {
        Query query = new SimpleQuery("*:*");
        query.setOffset(1);    //开始索引
        query.setRows(30);     //每页记录数

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        List<TbItem> itemList = page.getContent();

        System.out.println("满足条件的总页数" + page.getTotalPages());
        System.out.println("满足条件的总记录" + page.getTotalElements());

        for (TbItem tbItem : itemList) {
            System.out.println(tbItem.getId() + " " + tbItem.getBrand() + " " + tbItem.getPrice() + " " + tbItem.getSeller() + " " + tbItem.getCategory());

        }
    }

    /**
     * 查询高亮
     */
    @Test
    public void searchHightLight() {
        //高亮查詢
        HighlightQuery query = new SimpleHighlightQuery();
        //封裝查詢條件
        //Criteria criteria = new Criteria("item_title").and("item_seller").contains("9");
        Criteria criteria = new Criteria("item_title").contains("蘋果");
        query.addCriteria(criteria);

        //高亮
        HighlightOptions highlightOptions = new HighlightOptions();
        //設置高亮字段
        highlightOptions.addField("item_title");
        //添加前後綴
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");

        query.setHighlightOptions(highlightOptions);

        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        List<TbItem> itemList = page.getContent();
        for (TbItem item : itemList) {
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);
            if (highlights != null && highlights.size()>0){
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();
                if (snipplets != null && snipplets.size()>0){
                    item.setTitle(snipplets.get(0));
                }
            }
            System.out.println(item.getId()+""+item.getBrand()+""+item.getSeller()+""+item.getPrice());
        }

    }

```

##### 7.數據庫存到索引域

这个是前面张宪讲的

```
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 将满足条件商品数据导入索引库
     * 1、上架商品导入索引库  tb_goods  is_marketable='1'
     2、商品状态为1，正常状态 tb_item   status='1'
     */
    public void dataImport(){
        List<TbItem> itemList = itemMapper.findAllGrounding();
        //处理动态域字段赋值
        for (TbItem item : itemList) {
            String spec = item.getSpec();
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            item.setSpecMap(specMap);
        }

        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }
}
```

这个是自己查的

```
@Component
public class SolrUtil {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SolrTemplate solrTemplate;

   
    public void importGoodsData() {
        List<Goods> list = goodsMapper.findAll();
        System.out.println("====商品列表====");
        for (Goods goods : list) {
            System.out.println(goods.getTitle());
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit(); //提交
        System.out.println("====结束====");
    }
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importGoodsData();
    }
}

```











返回首页：

```java

@RequestMapping("/")
public String index(){
    return "index";
}
```



##### 报警异常

```xml
1.版本问题，
org.junit.internal.builders.AllDefaultPossibilitiesBuilder.runnerForClass(AllDefaultPossibilitiesBuilder.java:26)
	at org.junit.runners.model.RunnerBuilder.safeRunnerForClass(RunnerBuilder.java:59)
	at org.junit.internal.requests.ClassRequest.getRunner(ClassRequest.java:26)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:49)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
Caused by: java.lang.IllegalStateException: SpringJUnit4ClassRunner requires JUnit 4.12 or higher.
	at org.springframework.util.Assert.state(Assert.java:73)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.<clinit>(SpringJUnit4ClassRunner.java:104)
	... 14 more
========================================
 SpringJUnit4ClassRunner requires JUnit 4.12 or higher.
 需要用高等级的junit
2.包扫描问题        
Error creating bean with name 'com.delta.solrTest': Unsatisfied dependency expressed through field 'solrTemplate'; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.data.solr.core.SolrTemplate' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}   
 ===============================
修改前：@ContextConfiguration(locations = "classpath:applicationContext*.xml")
修改后：@ContextConfiguration(locations = "classpath*:applicationContext*.xml")       
3.Document is missing mandatory uniqueKey field: id
 添加一條是  solrTemplate.saveBean(item);
 添加多條是  solrTemplate.saveBeans(itemList);    
4.
5.        
```



##### 英語單詞

```
common：通常的，常見的
detail：詳述，瑣碎，更重細節
pattern：模式，方案
leverage：影響力，槓桿  駕馭
major：主修，

batch：一批，很多
如果我要循环插入100条 方法名addBatchItem(){}
```

