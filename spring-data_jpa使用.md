### spring-data_jpa使用

#### 1.jpa介绍

​        jpa就是ORM思想,操作对象就能操作数据库,它是一个接口,一个规范,用hibernate实现

​        **JpaRepository** => **PagingAndStoringRepository** => **CurdReposlitory** => **Repository**:他们都是一次继承的关系,所以JpaRepository功能最强大

​       Dao要实现的接口                分页功能                                      增删改查                   仅仅只是一个标识

#### 2.配置文件

```properties
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:p="http://www.springframework.org/schema/p" 
    xmlns:aop="http://www.springframework.org/schema/aop" 
    xmlns:tx="http://www.springframework.org/schema/tx" 
    xmlns:context="http://www.springframework.org/schema/context" 
    xmlns:mongo="http://www.springframework.org/schema/data/mongo"
    xmlns:jpa="http://www.springframework.org/schema/data/jpa"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/aop     
           http://www.springframework.org/schema/aop/spring-aop-3.0.xsd   
           http://www.springframework.org/schema/tx
           http://www.springframework.org/schema/tx/spring-tx-3.0.xsd
           http://www.springframework.org/schema/context     
           http://www.springframework.org/schema/context/spring-context-3.0.xsd
           http://www.springframework.org/schema/data/mongo
           http://www.springframework.org/schema/data/mongo/spring-mongo-1.0.xsd
           http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">
 
    <!-- 数据库连接 -->
    <context:property-placeholder location="classpath:your-config.properties" ignore-unresolvable="true" />
    <!-- service包 -->
    <context:component-scan base-package="your service package" />
    <!-- 使用cglib进行动态代理 -->
    <aop:aspectj-autoproxy proxy-target-class="true" />
    <!-- 支持注解方式声明式事务 -->
    <tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true" />
    <!-- dao -->
    <jpa:repositories base-package="your dao package" repository-impl-postfix="Impl" entity-manager-factory-ref="entityManagerFactory" transaction-manager-ref="transactionManager" />
    <!-- 实体管理器 -->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="packagesToScan" value="your entity package" />
        <property name="persistenceProvider">
            <bean class="org.hibernate.ejb.HibernatePersistence" />
        </property>
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
                <property name="generateDdl" value="false" />
                <property name="database" value="MYSQL" />
                <property name="databasePlatform" value="org.hibernate.dialect.MySQL5InnoDBDialect" />
                <!-- <property name="showSql" value="true" /> -->
            </bean>
        </property>
        <property name="jpaDialect">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaDialect" />
        </property>
        <property name="jpaPropertyMap">
            <map>
                <entry key="hibernate.query.substitutions" value="true 1, false 0" />
                <entry key="hibernate.default_batch_fetch_size" value="16" />
                <entry key="hibernate.max_fetch_depth" value="2" />
                <entry key="hibernate.generate_statistics" value="true" />
                <entry key="hibernate.bytecode.use_reflection_optimizer" value="true" />
                <entry key="hibernate.cache.use_second_level_cache" value="false" />
                <entry key="hibernate.cache.use_query_cache" value="false" />
            </map>
        </property>
    </bean>
    
    <!-- 事务管理器 -->
    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory"/>
    </bean>
    
    <!-- 数据源 -->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <property name="driverClassName" value="${driver}" />
        <property name="url" value="${url}" />
        <property name="username" value="${userName}" />
        <property name="password" value="${password}" />
        <property name="initialSize" value="${druid.initialSize}" />
        <property name="maxActive" value="${druid.maxActive}" />
        <property name="maxIdle" value="${druid.maxIdle}" />
        <property name="minIdle" value="${druid.minIdle}" />
        <property name="maxWait" value="${druid.maxWait}" />
        <property name="removeAbandoned" value="${druid.removeAbandoned}" />
        <property name="removeAbandonedTimeout" value="${druid.removeAbandonedTimeout}" />
        <property name="timeBetweenEvictionRunsMillis" value="${druid.timeBetweenEvictionRunsMillis}" />
        <property name="minEvictableIdleTimeMillis" value="${druid.minEvictableIdleTimeMillis}" />
        <property name="validationQuery" value="${druid.validationQuery}" />
        <property name="testWhileIdle" value="${druid.testWhileIdle}" />
        <property name="testOnBorrow" value="${druid.testOnBorrow}" />
        <property name="testOnReturn" value="${druid.testOnReturn}" />
        <property name="poolPreparedStatements" value="${druid.poolPreparedStatements}" />
        <property name="maxPoolPreparedStatementPerConnectionSize" value="${druid.maxPoolPreparedStatementPerConnectionSize}" />
        <property name="filters" value="${druid.filters}" />
    </bean>
    
    <!-- 事务 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="*" />
            <tx:method name="get*" read-only="true" />
            <tx:method name="find*" read-only="true" />
            <tx:method name="select*" read-only="true" />
            <tx:method name="delete*" propagation="REQUIRED" />
            <tx:method name="update*" propagation="REQUIRED" />
            <tx:method name="add*" propagation="REQUIRED" />
            <tx:method name="insert*" propagation="REQUIRED" />
        </tx:attributes>
    </tx:advice>
    <!-- 事务入口 -->
    <aop:config>
        <aop:pointcut id="allServiceMethod" expression="execution(* your service implements package.*.*(..))" />
        <aop:advisor pointcut-ref="allServiceMethod" advice-ref="txAdvice" />
    </aop:config>
 
</beans>
```

​        entityManagerFactory要配置对应的实体类

```java
<property name="packagesToScan" value="com.delta.domain"/>
==============================================
@Entity
@Table(name = "DEMO_STUDENT")
public class DemoStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    private String father;
    private String favourite;

    public DemoStudent() {
    }

    public DemoStudent(int id, String name, String father, String favourite) {
        this.id = id;
        this.name = name;
        this.father = father;
        this.favourite = favourite;
    }
```

#### 3.dao代码

```
public interface UserRepository extends JpaRepository<User, Integer>{}
```

```
${fragment.index}
${fragment.index}
```

