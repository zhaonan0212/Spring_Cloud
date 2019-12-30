## hibernate学习

**orm**

```
对象关系映射（Object Relational Mapping，简称ORM）模式是一种为了解决面向对象与关系数据库存在的互不匹配的现象的技术。
即把我实体类的属性和表的字段一一对应，操作对象就能操作数据库。
```

#### 1.入门

**hibernate:冬眠**

```
优势:1.底层是jdbc，它是对jdbc进行封装
     2.不用写繁琐的sql语句
     3.更换数据库，修改方言
 
学的是hibernate 5.xxx 版本
```

##### 1.jdbc复习

```java
Class.forName("com.oracle.jdbc.driver");
Connection conn = DeriverManager.getConnection(url,username,password);
String sql = 'select * from emp';
//获取平台
PrepareStatement  psmt = conn.prepartStatement(sql);
//执行存储过程
CallableStatement cstmt = conn.prepareCall(存储过程)
//处理结果
Result res  =  pstm.executeQuery()
while(res.next()){
    String name = res.getString("name");
}
//释放资源
res.close()
psmt.close()
conn.close()
```

##### 2.xml的文件约束

```plsql
xml：可扩展的标记语言
xml的约束就两个：dtd和schema，dtd是老的约束，schema是新的约束
DTD的语法：1.<!DOCTYPE 根元素 SYSTEM "DTD文件路径">
例：<!DOCTYPE students SYSTEM "students.dtd">
   l 指定DTD的语法，以“<!DOCTYPE”开头，以“>”结束；
   2 students表示根元素；
   3 SYSTEM表示dtd文件在本地；
   4 "students.dtd"表示DTD文件路径。
   2.外部公共dtd
   <!DOCTYPE 根元素 PUBLIC "DTD名称" "DTD网址">
例 :<!DOCTYPE students PUBLIC "-//qdmmy6//DTD ST 1.0//ZH" "http://www.qdmmy6.com/xml/dtds/st.dtd">
```

所有Javabean都叫实体类

```
pubic class User｛

  private int uid;
  private String username；
  private String password;
  
  set/get方法
 ｝
```

```
creater table t_user（
  uid number,
  username varchar2(10),
  password varchar2(40)
）
```

##### 3.hibernate.cfg.xml

oracle链接配置:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- 方言 -->
        <property name="dialect">org.hibernate.dialect.Oracle10gDialect</property>
        <!-- 数据库连接配置 -->
        <property name="connection.driver_class">oracle.jdbc.driver.OracleDriver</property>
        <property name="connection.url">jdbc:oracle:thin:@10.148.202.14:1521:PUBMESDV</property>
        <property name="connection.username">study</property>
        <property name="connection.password">study</property>

        <!-- 控制台显示sql -->
        <property name="show_sql">true</property>

        <mapping resource="Student.hbm.xml"/>
    </session-factory>

</hibernate-configuration>
```

mysql的链接配置:

```xml
<?xml version="1.0" encoding="GBK"?>
<!-- 指定Hibernate配置文件的DTD信息 -->
<!DOCTYPE hibernate-configuration PUBLIC
    "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<!-- hibernate- configuration是连接配置文件的根元素 -->
<hibernate-configuration>
    <session-factory>
        <!-- 指定连接数据库所用的驱动 -->
        <property name="connection.driver_class">com.mysql.jdbc.Driver</property>
        <!-- 指定连接数据库的url，hibernate连接的数据库名 -->
        <property name="connection.url">jdbc:mysql://localhost/数据库名</property>
        <!-- 指定连接数据库的用户名 -->
        <property name="connection.username">root</property>
        <!-- 指定连接数据库的密码 -->
        <property name="connection.password">root</property>
        
        <!-- 指定连接池里最大连接数 -->
        <property name="hibernate.c3p0.max_size">20</property>
        <!-- 指定连接池里最小连接数 -->
        <property name="hibernate.c3p0.min_size">1</property>
        <!-- 指定连接池里连接的超时时长 -->
        <property name="hibernate.c3p0.timeout">5000</property>
        <!-- 指定连接池里最大缓存多少个Statement对象 -->
        <property name="hibernate.c3p0.max_statements">100</property>
        <property name="hibernate.c3p0.idle_test_period">3000</property>
        <property name="hibernate.c3p0.acquire_increment">2</property>
        <property name="hibernate.c3p0.validate">true</property>
        <!-- 指定数据库方言 -->
        <property name="dialect">org.hibernate.dialect.MySQLInnoDBDialect</property>
       
        <!-- 根据需要自动创建数据表 -->
        <property name="hbm2ddl.auto">update</property>
        <!-- 显示Hibernate持久化操作所生成的SQL -->
        <property name="show_sql">true</property>
        <!-- 将SQL脚本进行格式化后再输出 -->
        <property name="hibernate.format_sql">true</property>
        <!-- 罗列所有的映射文件 -->
        <mapping resource="映射文件路径/News.hbm.xml"/>
    </session-factory>
</hibernate-configuration>
```





```xml

<!--标准的XML文件的起始行，version='1.0'表明XML的版本，encoding='gb2312'表明XML文件的编码方式-->   
<?xml version='1.0' encoding='gb2312'?>   
<!--表明解析本XML文件的DTD文档位置，DTD是Document Type Definition 的缩写,即文档类型的定义,XML解析器使用DTD文档来检查XML文件的合法性。hibernate.sourceforge.net/hibernate-configuration-3.0dtd可以在Hibernate3.1.3软件包中的src\org\hibernate目录中找到此文件-->   
<!DOCTYPE hibernate-configuration PUBLIC   
          "-//Hibernate/Hibernate Configuration DTD 3.0//EN"   
          "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">   
    <!--声明Hibernate配置文件的开始-->        
    <hibernate-configuration>   
    <!--表明以下的配置是针对session-factory配置的，SessionFactory是Hibernate中的一个类，这个类主要负责保存HIbernate的配置信息，以及对Session的操作-->   
      <session-factory>      
      <!--配置数据库的驱动程序，Hibernate在连接数据库时，需要用到数据库的驱动程序-->   
          <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver </property>   
      <!--设置数据库的连接url:jdbc:mysql://localhost/hibernate,其中localhost表示mysql服务器名称，此处为本机，    hibernate是数据库名-->    
            <property name="hibernate.connection.url">jdbc:mysql://localhost/hibernate</property>
    <!--连接数据库是用户名-->   
          <property name="hibernate.connection.username">root </property>   
          <!--连接数据库是密码-->   
          <property name="hibernate.connection.password">123456 </property>          
          <!--数据库连接池的大小-->   
          <property name="hibernate.connection.pool.size">20 </property>          
        <!--是否在后台显示Hibernate用到的SQL语句，开发时设置为true，便于差错，程序运行时可以在Eclipse的控制台显示Hibernate的执行Sql语句。项目部署后可以设置为false，提高运行效率-->   
        <property name="hibernate.show_sql">true </property>   
        <!--jdbc.fetch_size是指Hibernate每次从数据库中取出并放到JDBC的Statement中的记录条数。Fetch Size设的越大，读数据库的次数越少，速度越快，Fetch Size越小，读数据库的次数越多，速度越慢-->   
        <property name="jdbc.fetch_size">50 </property>   
        <!--jdbc.batch_size是指Hibernate批量插入,删除和更新时每次操作的记录数。Batch Size越大，批量操作的向数据库发送Sql的次数越少，速度就越快，同样耗用内存就越大-->   
        <property name="jdbc.batch_size">23 </property>   
        <!--jdbc.use_scrollable_resultset是否允许Hibernate用JDBC的可滚动的结果集。对分页的结果集。对分页时的设置非常有帮助-->   
        <property name="jdbc.use_scrollable_resultset">false </property>   
        <!--connection.useUnicode连接数据库时是否使用Unicode编码-->   
        <property name="Connection.useUnicode">true </property>   
        <!--connection.characterEncoding连接数据库时数据的传输字符集编码方式，最好设置为gbk，用gb2312有的字符不全-->   
    <property name="connection.characterEncoding">gbk </property>        
          
        <!--hibernate.dialect 只是Hibernate使用的数据库方言,就是要用Hibernate连接那种类型的数据库服务器。-->   
          <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect </property>   
        <!--指定映射文件为“hibernate/ch1/UserInfo.hbm.xml”-->          
          <mapping resource="org/mxg/UserInfo.hbm.xml">   
  </session-factory>   
  </hibernate-configuration>      
     
  <bean id="dataSource"    class="org.apache.commons.dbcp.BasicDataSource"  destroy-method="close">    
     //连接驱动      
     <property name="driverClassName" value="${jdbc.driverClassName}" />    
     //连接url,      
     <property name="url" value="${jdbc.url}" />    
     //连接用户名      
     <property name="username" value="${jdbc.username}" />    
     //连接密码      
     <property name="password" value="${jdbc.password}" />    
 </bean>    
    
<bean id="hbSessionFactory"  class="org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean">    
  <property name="dataSource" ref="dataSource" />    
  <property name="configLocation">    
//hibernate配置文件位置      
  <value>WEB-INF/hibernate.cfg.xml </value>    
  </property>    
  <property name="configurationClass"    
  value="org.hibernate.cfg.AnnotationConfiguration" />    
  <property name="hibernateProperties">    
  <props>    
  //针对oracle数据库的方言,特定的关系数据库生成优化的SQL      
    <prop key="hibernate.dialect">    
    org.hibernate.dialect.OracleDialect      
    </prop>    
  //选择HQL解析器的实现      
    <prop key="hibernate.query.factory_class">    
    org.hibernate.hql.ast.ASTQueryTranslatorFactory      
    </prop>    
    //是否在控制台打印sql语句      
    <prop key="hibernate.show_sql">true </prop>    
    //在Hibernate系统参数中hibernate.use_outer_join被打开的情况下,该参数用来允许使用outer join来载入此集合的数据。      
    <prop key="hibernate.use_outer_join">true </prop>    
  //默认打开，启用cglib反射优化。cglib是用来在Hibernate中动态生成PO字节码的，打开优化可以加快字节码构造的速度      
  <prop key="hibernate.cglib.use_reflection_optimizer">true </prop>    
  //输出格式化后的sql,更方便查看      
  <prop key="hibernate.format_sql">true </prop>    
  //“useUnicode”和“characterEncoding”决定了它是否在客户端和服务器端传输过程中进行Encode，以及如何进行Encode      
  <prop key="hibernate.connection.useUnicode">true </prop>    
  //允许查询缓存, 个别查询仍然需要被设置为可缓存的.      
  <prop key="hibernate.cache.use_query_cache">false </prop>    
  <prop key="hibernate.default_batch_fetch_size">16 </prop>    
    //连接池的最大活动个数      
    <prop key="hibernate.dbcp.maxActive">100 </prop>    
  //当连接池中的连接已经被耗尽的时候，DBCP将怎样处理(0 = 失败,1 = 等待,2  =  增长)      
    <prop key="hibernate.dbcp.whenExhaustedAction">1 </prop>    
    //最大等待时间      
    <prop key="hibernate.dbcp.maxWait">1200 </prop>    
    //没有人用连接的时候，最大闲置的连接个数      
    <prop key="hibernate.dbcp.maxIdle">10 </prop>    
    ##以下是对prepared statement的处理，同上。      
    <prop key="hibernate.dbcp.ps.maxActive">100 </prop>    
    <prop key="hibernate.dbcp.ps.whenExhaustedAction">1 </prop>    
    <prop key="hibernate.dbcp.ps.maxWait">1200 </prop>    
    <prop key="hibernate.dbcp.ps.maxIdle">10 </prop>    
  </props>    
  </property>    
</bean>
```

##### 4.XXX.hbm.xml

```xml
<?xml version="1.0"?>  
<!DOCTYPE hibernate-mapping PUBLIC   
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"  
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">  
      
<!--   
    <hibernate-mapping>一般不去配置，采用默认即可。  
    default-cascade="none"：默认的级联风格，表与表联动。  
    default-lazy="true"：默认延迟加载  
 -->  
<hibernate-mapping>  
  
    <!--   
        <class>：使用class元素定义一个持久化类。  
        name="cn.javass.user.vo.UserModel"：持久化类的java全限定名；  
        table="tbl_user"：对应数据库表名；  
        mutable="true"：默认为true，设置为false时则不可以被应用程序更新或删除；  
        dynamic-insert="false"：默认为false，动态修改那些有改变过的字段，而不用修改所有字段；  
        dynamic-update="false"：默认为false，动态插入非空值字段；  
        select-before-update="false"：默认为false，在修改之前先做一次查询，与用户的值进行对比，有变化都会真正更新；  
        optimistic-lock="version"：默认为version(检查version/timestamp字段)，取值：all(检查全部字段)、dirty(只检查修改过的字段)、  
                                   none(不使用乐观锁定)，此参数主要用来处理并发，每条值都有固定且唯一的版本，版本为最新时才能执行操作；  
     -->  
    <class name="cn.javass.user.vo.UserModel" table="tbl_user" dynamic-insert="true" dynamic-update="true" optimistic-lock="version">  
          
        <!--   
            <id>：定义了该属性到数据库表主键字段的映射。  
            name="userId"：标识属性的名字；   
            column="userId"：表主键字段的名字，如果不填写与name一样；  
         -->  
        <id name="userId" type="int" colun="stuId">  
            <!-- <generator>：指定主键由什么生成，推荐使用uuid（随机生成唯一通用的表示符，实体类的ID必须是String），
							   native（让数据库自动选择用什么生成（根据底层数据库的能力选择identity，sequence或hilo中的一种）），
							   assigned（指用户手工填入，默认,无类型限制）。
                                  increment:类型必须是数值型,先查表中最大id select max(id) from t_user,基础上+1  
                                  sequence:只能用于提供序列支持的数据库,如oracle      -->  
            <generator class="uuid"/>  
        </id>  
          
        <!--   
            <version/>：使用版本控制来处理并发，要开启optimistic-lock="version"和dynamic-update="true"。  
            name="version"：持久化类的属性名，column="version"：指定持有版本号的字段名；  
         -->  
        <version name="version" column="version"/>  
          
        <!--   
            <property>：为类定义一个持久化的javaBean风格的属性。  
            name="name"：标识属性的名字，以小写字母开头；  
            column="name"：表主键字段的名字，如果不填写与name一样；  
            update="true"/insert="true"：默认为true，表示可以被更新或插入；  
         -->  
        <property name="name" column="name" />  
        <property name="sex" column="sex"/>  
        <property name="age" column="age"/>  
          
        <!--   
            组件映射：把多个属性打包在一起当一个属性使用，用来把类的粒度变小。  
            <component name="属性，这里指对象">  
                <property name="name1"></property>  
                <property name="name2"></property>  
            </component>  
         -->  
           
         <!--   
            <join>:一个对象映射多个表，该元素必须放在所有<property>之后。  
            <join table="tbl_test：子表名">  
                <key column="uuid：子表主键"></key>  
            <property name="name1：对象属性" column="name：子表字段"></property>  
         </join>  
          -->  
           
    </class>  
      
</hibernate-mapping> 
```

一对一映射配置(一夫一妻制)

```xml
<?xml version="1.0"?>  
<!DOCTYPE hibernate-mapping PUBLIC   
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"  
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">  
      
<hibernate-mapping package="com.delta.domain">  
    <class name="Husband" table="HUSBAND">
    	<id name="husId" type="int" column="HUSID">
        	<generator class="native"/>
        </id>   
        <property name="husName" type="String" column="HUSNAME"/>
        <many-to-one name="wife" class="com.delta.domain.Wife" >
        	<column unique="true" name="WIFEID">
        </many-to-one>          
    </class>      
</hibernate-mapping> 
在many-to-one标签中，name对应关系wife实体类的对象名，class对应关系wife实体类的类名，column：数据库中外键字段名，unique：标识唯一性，这样many-to-one就变成了唯一的。    
===========================================================
<?xml version="1.0"?>  
<!DOCTYPE hibernate-mapping PUBLIC   
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"  
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">  
      
<hibernate-mapping package="com.delta.domain">  
    <class name="Wife" table="WIFE">
    	<id name="wifeId" type="int" column="WIFEID">
        	<generator class="native"/>
        </id>   
        <property name="wifeName" type="String" column="WIFENAME"/>
        <one-to-one name="hus" property-ref="wife" class="com.delta.domain.Huaband" >
        </one-to-one>          
    </class>      
</hibernate-mapping>   
one-to-one标签中name对应关系hus实体类对象名，class对应关系hus实体类的类名，property-ref：一对一关系中wife实体类的对象名。     
```

一对多映射配置(部门和员工)

```xml
<?xml version="1.0"?>  
<!DOCTYPE hibernate-mapping PUBLIC   
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"  
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">  
      
<hibernate-mapping package="com.delta.domain">  
    <class name="Dept" table="T_DEPT">
    	<id name="deptId" type="int" column="DEPTID">
        	<generator class="native"/>
        </id>   
        <property name="deptName" type="String" column="DEPTNAME"/>
        <set name="emps" table="t_emp" inverse="true" lazy="true">
            <key column="DEPTID"></key>
            <one-to-many class="com.delta.domain.EMP" />
        </set>          
    </class>      
</hibernate-mapping> 
inverse为true表示放弃维护关系能力，lazy表示控制关联数据延迟加载，key标签中column对应关联表（t_emp）指向本表的外键字段名，one-to-many标签中的class对应关联集合中元素的类型。
=========================================
<?xml version="1.0"?>  
<!DOCTYPE hibernate-mapping PUBLIC   
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"  
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">  
      
<hibernate-mapping package="com.delta.domain">  
    <class name="Emps" table="T_EMP">
    	<id name="empId" type="int" column="EMPID">
        	<generator class="native"/>
        </id>   
        <property name="empName" type="String" column="EMPNAME"/>
        <many-to-one name="dept" class="com.delta.domain.Dept">
            <column name="DEPTID" />
        </many-to-one>          
    </class>      
</hibernate-mapping> 
```

```xml
多对多,老师和学生
在student映射关系配置文件中，除了配置student的主键和普通属性的映射关系，还要配置与老师teacher的多对多关系映射，set标签表名当前实体对应多个数据的关系，其中name对应关联关系的属性名，table是对应关系的表名，也就是中间表，fetch为join表示左外连接，key标签中的column是当前表（t_student）在中间表中的外键字段名，many-to-many中的column属性是关联表（t_teacher）在中间表中的外键字段名，class对应关联属性类型. 

<hibernate-mapping package="com.delta.domain">  
    <class name="Student" table="T_STUDENT">
    	<id name="stuId" type="int" column="STUID">
        	<generator class="native"/>
        </id>   
        <property name="stuName" type="String" column="STUNAME"/>
        <set name="teas" table="t_stu_tea" fetch="join">
            <key column="stu_id"></key>
            <many-to-many column="tea_id" class="com.delta.domain.Teacher"></many-to-many>
        </set>          
    </class>      
</hibernate-mapping> 
========================================================
在teacher映射关系配置文件中，除了配置teacher的主键和普通属性的映射关系，还要配置与学生student的多对到关系映射，set标签表示当前实体对应多个数据的关系，其中name对应关联关系的属性名，table是对应关系的表名，也就是中间表，inverse为true表示放弃维护关系能力，key标签中的column是当前表（t_teacher）在中间表的外键字段名，many-to-many中的column属性是关联表（t_student）在中间表的外键字段名，class对应关联属性类型。

<hibernate-mapping package="com.delta.domain">  
    <class name="Teacjer" table="T_TEACHER">
    	<id name="teaId" type="int" column="TEAID">
        	<generator class="native"/>
        </id>   
        <property name="teaName" type="String" column="TEANAME"/>
        <set name="stus" table="t_stu_tea" inverse="true">
            <key column="tea_id"></key>
            <many-to-many column="stu_id" class="com.delta.domain.Student"></many-to-many>
        </set>             
    </class>      
</hibernate-mapping> 
```

5.测试

```

```





#### 英语

```plsql
 reserve：预定，保留          please reserve your metting time  --预留会议时间
```



























