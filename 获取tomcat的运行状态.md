### 获取tomcat的运行状态

#### 1.用浏览器查看tomcat的运行状态

 修改配置文件，%Tomcat_Home%/conf/tomcat-user.xml文件 

```xml
<role rolename="manager-status"/>
<role rolename="manager"/>  
<role rolename="manager-jmx"/> 
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="admin"/>
 
<user username="tomcat" password="tomcat" roles="tomcat"/> 
<user username="admin" password="tomcat" roles="manager,manager-gui,admin,manager-status,manager-jmx,manager-script"/>
```

两种发放获取：

```
http://localhost:8080/manager/status?XML=true
     --输入用户名：admin,密码：tomcat. 出现以下页面，即Tomcat的运行状态。
http://admin:tomcat@localhost :8080/manager/status?XML=true     
```

2.用java访问

```
@Test
    public void test1() throws IOException {
        URL url = null;
        InputStream is = null;
        StringBuffer resultBuffer = new StringBuffer();
        BufferedReader breader = null;
        try {
            url = new URL(
                    "http://admin:tomcat@localhost:8080/manager/status?XML=true");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
 
            breader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = breader.readLine()) != null) {
                resultBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (breader != null)
                breader.close();
            if (is != null)
                is.close();
        }
        System.out.println(resultBuffer.toString());
    }
```

如果报警

```
java.io.IOException: Server returned HTTP response code: 401 for URL: http://admin:tomcat@localhost:8080/manager/status?XML=true
    at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1625)
    at com.merit.monitor.tomcat.TestTomcatStatus.test1(TestTomcatStatus.java:57)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:606)
    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:44)
    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:15)
    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:41)
    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:20)
    at org.junit.runners.BlockJUnit4ClassRunner.runNotIgnored(BlockJUnit4ClassRunner.java:79)
    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:71)
    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:49)
    at org.junit.runners.ParentRunner$3.run(ParentRunner.java:193)
    at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:52)
    at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:191)
    at org.junit.runners.ParentRunner.access$000(ParentRunner.java:42)
    at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:184)
    at org.junit.runners.ParentRunner.run(ParentRunner.java:236)
    at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:50)
    at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:467)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:683)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:390)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:197)
```

使用如下方法

```

/**
      * @Description: 获取指定URL的内容
      * @Version1.0 2014-7-23 下午02:18:22 by xuqiang（xuqiang@merit.com）创建
      * @param tempurl url地址
      * @param username tomcat 管理用户名
      * @param password tomcat 管理用户密码
      * @return
      * @throws IOException
      */
    public static String getHtmlContext(String tempurl, String username, String password) throws IOException {
        URL url = null;
        BufferedReader breader = null;
        InputStream is = null;
        StringBuffer resultBuffer = new StringBuffer();
        try {
            url = new URL(tempurl);
            String userPassword = username + ":" + password;
            String encoding = new sun.misc.BASE64Encoder().encode (userPassword.getBytes());//在classpath中添加rt.jar包，在%java_home%/jre/lib/rt.jar
 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            is = conn.getInputStream();
            breader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = breader.readLine()) != null) {
                resultBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if(breader != null)
                breader.close();
            if(is != null)
                is.close();
        }
        return resultBuffer.toString();
    }
```

```

@Test
    public void test() {
        String result = "";
        Document document = null;//引入org.dom4j包
        try {
            result = GetHtmlContext.getHtmlContext("http://localhost:8080/manager/status?XML=true", "admin", "tomcat");
            document = DocumentHelper.parseText(result);//将字符串转化为XML的Document
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println(document.asXML());
    }
```

返回的结果格式

```
go_gc_duration_seconds{quantile="0"} 0
```

