### java各類日誌的使用對比

```java
我们在系统中对于记录日志的需求并不单纯。
首先，我们希望日志要能持久化到磁盘，最基本的就是要能够保存到文件中；
其次，我们希望在开发和生产环境中记录的日志并不相同，明显开发环境的日志记录会更多方便调试，但放到生产环境下大量的日志很容易会撑爆服务器，因此在生产环境我们希望只记录重要信息。
```



#### 一、log4j的相關知識介紹

#####     1.使用條件

​      先引入依賴，在添加配置文件

```xml
 <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>1.2.12</version>
 </dependency>
```

#####     2.配置文件

```properties
# priority  :debug<info<warn<error
#you cannot specify every priority with different file for log4j 
log4j.rootLogger=all,debug,info,warn,error,fatal                     //選擇日誌等級,下面的配置由此處決定
#console    
log4j.appender.stdout=org.apache.log4j.ConsoleAppender 
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout 
log4j.appender.stdout.layout.ConversionPattern= [%d{yyyy-MM-dd HH:mm:ss a}]:%p %l%m%n
#info log
log4j.logger.info=info
log4j.appender.info=org.apache.log4j.DailyRollingFileAppender          //日誌輸出目的地appender
log4j.appender.info.DatePattern='_'yyyy-MM-dd'.log'                    //日誌滾動格式：每月，每天，每時，每分等  添加在保存的文件名后面
log4j.appender.info.File=./src/com/hp/log/info.log                     //日誌最終打印的地址
log4j.appender.info.Append=true                                        //true消息增加到指定文件，false消息覆盖指定的文件内容。
log4j.appender.info.Threshold=INFO                                     //日誌消息輸出的最低等級
log4j.appender.info.layout=org.apache.log4j.PatternLayout              //指定輸出佈局，多種選擇，html，pattern，simple，ttccl等
log4j.appender.info.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss a} [Thread: %t][ Class:%c >> Method: %l ]%n%p:%m%n      //輸出格式的詳細設定
#debug log
log4j.logger.debug=debug
log4j.appender.debug=org.apache.log4j.DailyRollingFileAppender 
log4j.appender.debug.DatePattern='_'yyyy-MM-dd'.log'
log4j.appender.debug.File=./src/com/hp/log/debug.log
log4j.appender.debug.Append=true
log4j.appender.debug.Threshold=DEBUG
log4j.appender.debug.layout=org.apache.log4j.PatternLayout 
log4j.appender.debug.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss a} [Thread: %t][ Class:%c >> Method: %l ]%n%p:%m%n
#warn log
log4j.logger.warn=warn
log4j.appender.warn=org.apache.log4j.DailyRollingFileAppender      //日誌的輸出目的地，選擇最多的每天產生一個和日誌達到多大產生新的
log4j.appender.warn.DatePattern='_'yyyy-MM-dd'.log'                      //滾動規則，每天每時每分
log4j.appender.warn.File=./src/com/hp/log/warn.log                       //日誌的存儲地點
log4j.appender.warn.Append=true                                          //true消息添加到指定文件中，false將消息覆蓋指定的文件內容
log4j.appender.warn.Threshold=WARN                                       //日誌輸出的最低等級
log4j.appender.warn.layout=org.apache.log4j.PatternLayout                //日誌的佈局html，pattern，simple，ttccl等
log4j.appender.warn.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss a} [Thread: %t][ Class:%c >> Method: %l ]%n%p:%m%n //詳細設定
#error
log4j.logger.error=error
log4j.appender.error = org.apache.log4j.DailyRollingFileAppender
log4j.appender.error.DatePattern='_'yyyy-MM-dd'.log'
log4j.appender.error.File = ./src/com/hp/log/error.log 
log4j.appender.error.Append = true
log4j.appender.error.Threshold = ERROR 
log4j.appender.error.layout = org.apache.log4j.PatternLayout
log4j.appender.error.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss a} [Thread: %t][ Class:%c >> Method: %l ]%n%p:%m%n
```

##### 3.Log4j中提供的Appender主要有以下几种：

```javascript
org.apache.log4j.ConsoleAppender（控制台）， 
org.apache.log4j.FileAppender（文件）， 
org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件），                       #常用
org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件），        #常用
org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）* 
==================================================================
如果選擇了RollingFileAppender
我們就可以在配置文件中添加配置項
log4j.appender.R.MaxFileSize=20MB                    #日志文件的最大大小，当产生多个日志时，会在日志名称后面加上".1"、".2"、…
log4j.appender.R.MaxBackupIndex=10                   #表示最大日志数量为11
==================================================================
DailyRollingFileAppender特点是固定周期时间生成一个日志文件
優點：方便根据时间来定位日志位置，使日志清晰易查。
缺點：不能限制日志数量，MaxBackupIndex属性和MaxFileSize在DailyRollingFileAppender中是无效的。
```

##### 4.滾動日誌文件

```java
DatePattern='.'yyyy-MM：每月滚动一次日志文件，即每月产生一个新的日志文件。当前月的日志文件名为logging.log4j，前一个月的日志文件名为logging.log4j.yyyy-MM。
另外，也可以指定按周、天、时、分等来滚动日志文件，对应的格式如下：
1)'.'yyyy-MM：每月
2)'.'yyyy-ww：每周
3)'.'yyyy-MM-dd：每天
4)'.'yyyy-MM-dd-a：每天两次
5)'.'yyyy-MM-dd-HH：每小时
6)'.'yyyy-MM-dd-HH-mm：每分钟
```

##### 5.Log4j提供的layout有以下几种：

```
org.apache.log4j.HTMLLayout（以HTML表格形式布局）， 
org.apache.log4j.PatternLayout（可以灵活地指定布局模式）， 
org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串）， 
org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
```

##### 6.ConversionPattern各個參數的含義:

```properties
%p：输出日志信息的优先级，即DEBUG，INFO，WARN，ERROR，FATAL。
%d：输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，如：%d{yyyy/MM/dd HH:mm:ss,SSS}。
%r：输出自应用程序启动到输出该log信息耗费的毫秒数。
%t：输出产生该日志事件的线程名。
%l：输出日志事件的发生位置，相当于%c.%M(%F:%L)的组合，包括类全名、方法、文件名以及在代码中的行数。例如：test.TestLog4j.main(TestLog4j.java:10)。
%c：输出日志信息所属的类目，通常就是所在类的全名。
%M：输出产生日志信息的方法名。
%F：输出日志消息产生时所在的文件名称。
%L:：输出代码中的行号。
%m:：输出代码中指定的具体日志信息。
%n：输出一个回车换行符，Windows平台为"/r/n"，Unix平台为"/n"。
%x：输出和当前线程相关联的NDC(嵌套诊断环境)，尤其用到像java servlets这样的多客户多线程的应用中。
%%：输出一个"%"字符。
另外，还可以在%与格式字符之间加上修饰符来控制其最小长度、最大长度、和文本的对齐方式。如：
%20c：指定输出category的名称，最小的长度是20，如果category的名称长度小于20的话，默认的情况下右对齐。
%-20c："-"号表示左对齐。
%.30c：指定输出category的名称，最大的长度是30，如果category的名称长度大于30的话，就会将左边多出的字符截掉，但小于30的话也不会补空格。
```

控制台輸出

```properties
Threshold=DEBUG:           //指定日志消息的输出最低层次。
ImmediateFlush=true:       //默认值是true,意谓着所有的消息都会被立即输出。
Target=System.err：        //默认情况下是：System.out,指定输出控制台
FileAppender               //选项
RollingFileAppender        //选项
Append=false:              //默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
MaxFileSize=100KB:         //日志文件到达该大小时，将会自动滚动，即将原来的内容移到mylog.log.1文件。
MaxBackupIndex=2:          //指定可以产生的滚动文件的最大数。
log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %d{yyyy-MM-dd HH:mm:ssS} %c %m%n    //日誌佈局格式
```

##### 7.保存的模式

​    ❶保存到本地

```java
log4j.appender.LOGFILE.File=E:\system_develop_manager.log                      //保存到system_develop_manager.log裡面             
```

如果不同的包想保存到不同的日誌文件中，操作如下，修改配置文件。

```properties
要求：config包下的日志输出到config.log，业务处理的日志输出到busi.log。
==================================================
log4j.rootLogger =ALL,systemOut
#输出到控制台
log4j.appender.systemOut = org.apache.log4j.ConsoleAppender
log4j.appender.systemOut.layout = org.apache.log4j.PatternLayout
log4j.appender.systemOut.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  - [ %p ]  %m%n
log4j.appender.systemOut.Threshold = DEBUG
log4j.appender.systemOut.ImmediateFlush = TRUE
log4j.appender.systemOut.Target = System.out
==================================================
#输出com.roy.busi包下类的日志，debug级别，appender是busi
log4j.logger.com.roy.busi=debug,busi
#每日生成新的文件
log4j.appender.busi = org.apache.log4j.DailyRollingFileAppender
log4j.appender.busi.layout = org.apache.log4j.PatternLayout
log4j.appender.busi.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  - [ %p ]  %m%n
log4j.appender.busi.Threshold = DEBUG
log4j.appender.busi.ImmediateFlush = TRUE
log4j.appender.busi.Append = TRUE
#日志路径
log4j.appender.busi.File = ./logfile/log4j-busi.log
log4j.appender.busi.Encoding = UTF-8
==================================================
#输出com.roy.config包下类的日志
log4j.logger.com.roy.config=debug,config
#每日生成新的文件
log4j.appender.config = org.apache.log4j.DailyRollingFileAppender
log4j.appender.config.layout = org.apache.log4j.PatternLayout
log4j.appender.config.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  - [ %p ]  %m%n
log4j.appender.config.Threshold = DEBUG
log4j.appender.config.ImmediateFlush = TRUE
log4j.appender.config.Append = TRUE
#日志路径
log4j.appender.config.File = ./logfile/log4j-config.log
log4j.appender.config.Encoding = UTF-8
```

##### 8.日誌的自動刪除

​    ❶如果是log4j2，通过配置RollingFileAppender的清理策略来实现自动清理日志

```xml
<properties>
    <log4j2.version>2.7</log4j2.version>                                             //版本號
</properties>

<dependencies>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>${log4j2.version}</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>${log4j2.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>2.3.2</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

  測試代碼

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Demo {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        while (true) {
            logger.trace("trace level");
            logger.debug("debug level");
            logger.info("info level");
            logger.warn("warn level");
            logger.error("error level");
            logger.fatal("fatal level");
        }
    }
}
```

  log4j2的配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="warn">
    <Properties>
        <Property name="baseDir">logs</Property>
    </Properties>

    <Appenders>
        <RollingFile name="RollingFile" fileName="${baseDir}/app.log" filePattern="${baseDir}/$${date:dd日hh时}/app-%d{mm分ss秒}.log.gz">
            <PatternLayout pattern="%d %p %c{1.} [%t] %m%n" />
            <CronTriggeringPolicy schedule="* * * * * ?"/>
            <DefaultRolloverStrategy>
                <Delete basePath="${baseDir}" maxDepth="2">
                    <IfFileName glob="*/app-*.log.gz" />
                    <IfLastModified age="5s" />
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
    </Appenders>
  
    <Loggers>
        <Root level="all">
            <AppenderRef ref="RollingFile"/>
        </Root>
    </Loggers>
</Configuration>
```

   ❷如果是log4j，那麼操作如下

​    修改配置文件

```xml
<appender name="vstore"  
     class="ch.qos.logback.core.rolling.RollingFileAppender">  
     <file>default.log</file>  
     <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">  
          <fileNamePattern>./logs/%d/default.log.%d  
          </fileNamePattern>  
          <maxHistory>7</maxHistory>  
     </rollingPolicy>  
  
     <encoder>  
          <pattern>%d{HH:mm:ss.SSS} %level [%thread] %c{0}[%line] %msg%n  
          </pattern>  
     </encoder>  
</appender> 
```

第一種方法

```java
package logger;  
  
import java.io.File;  
import java.io.FileFilter;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InterruptedIOException;  
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Date;  
import java.util.GregorianCalendar;  
import java.util.Locale;  
import java.util.TimeZone;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipInputStream;  
import java.util.zip.ZipOutputStream;  
  
import org.apache.log4j.FileAppender;  
import org.apache.log4j.Layout;  
import org.apache.log4j.helpers.LogLog;  
import org.apache.log4j.spi.LoggingEvent;  
  
/** 
*  log4j.appender.RootAppender=com.edsdev.log4j.AdvancedDailyRollingFileAppender 
*  log4j.appender.RootAppender.DatePattern='.'yyyyMMdd 
*  log4j.appender.RootAppender.MaxNumberOfDays=60 
*  log4j.appender.RootAppender.CompressBackups=true 
*  log4j.appender.RootAppender.CompressBackupsAfterDays=31 
*  log4j.appender.RootAppender.CompressBackupsDatePattern='.'yyyyMM 
*  log4j.appender.RootAppender.RollCompressedBackups=true 
*  log4j.appender.RootAppender.CompressMaxNumberDays=365 
*/

public class AdvancedDailyRollingFileAppender extends FileAppender {  
    
    static final int TOP_OF_TROUBLE = -1;  
    static final int TOP_OF_MINUTE = 0;  
    static final int TOP_OF_HOUR = 1;  
    static final int HALF_DAY = 2;  
    static final int TOP_OF_DAY = 3;  
    static final int TOP_OF_WEEK = 4;  
    static final int TOP_OF_MONTH = 5;  
  
 
    private String compressBackups = "false";  
  
    private String rollCompressedBackups = "false";  
   
    private int maxNumberOfDays = 31;  

    private int compressBackupsAfterDays = 31;  
 
    private String compressBackupsDatePattern = "'.'yyyy-MM";  
 
    private int compressMaxNumberDays = 365;  
  
  
    private String datePattern = "'.'yyyy-MM-dd";  
  
   
    private String scheduledFilename;  
    
    private long nextCheck = System.currentTimeMillis() - 1;  
  
    Date now = new Date();  
  
    SimpleDateFormat sdf;  
  
    RollingCalendar rc = new RollingCalendar();  
  
    int checkPeriod = TOP_OF_TROUBLE;  
  
  
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");  
  
     
    public AdvancedDailyRollingFileAppender() {  
    }  
  
    
    public AdvancedDailyRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {  
        super(layout, filename, true);  
        this.datePattern = datePattern;  
        activateOptions();  
    }  
  
   
    public void setDatePattern(String pattern) {  
        datePattern = pattern;  
    }  
  
    /** Returns the value of the <b>DatePattern</b> option. */  
    public String getDatePattern() {  
        return datePattern;  
    }  
  
    public String getCompressBackups() {  
        return compressBackups;  
    }  
  
    public void setCompressBackups(String compressBackups) {  
        this.compressBackups = compressBackups;  
    }  
  
    public String getMaxNumberOfDays() {  
        return "" + maxNumberOfDays;  
    }  
  
    public void setMaxNumberOfDays(String days) {  
        try {  
            this.maxNumberOfDays = Integer.parseInt(days);  
        } catch (Exception e) {  
            // just leave it at default  
        }  
  
    }  
  
    @Override  
    public void activateOptions() {  
        super.activateOptions();  
        if (datePattern != null && fileName != null) {  
            now.setTime(System.currentTimeMillis());  
            sdf = new SimpleDateFormat(datePattern);  
            int type = computeCheckPeriod();  
            printPeriodicity(type);  
            rc.setType(type);  
            File file = new File(fileName);  
            scheduledFilename = fileName + sdf.format(new Date(file.lastModified()));  
  
        } else {  
            LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");  
        }  
    }  
  
    void printPeriodicity(int type) {  
        switch (type) {  
            case TOP_OF_MINUTE:  
                LogLog.debug("Appender [" + name + "] to be rolled every minute.");  
                break;  
            case TOP_OF_HOUR:  
                LogLog.debug("Appender [" + name + "] to be rolled on top of every hour.");  
                break;  
            case HALF_DAY:  
                LogLog.debug("Appender [" + name + "] to be rolled at midday and midnight.");  
                break;  
            case TOP_OF_DAY:  
                LogLog.debug("Appender [" + name + "] to be rolled at midnight.");  
                break;  
            case TOP_OF_WEEK:  
                LogLog.debug("Appender [" + name + "] to be rolled at start of week.");  
                break;  
            case TOP_OF_MONTH:  
                LogLog.debug("Appender [" + name + "] to be rolled at start of every month.");  
                break;  
            default:  
                LogLog.warn("Unknown periodicity for appender [" + name + "].");  
        }  
    }  
  
    int computeCheckPeriod() {  
        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());  
        // set sate to 1970-01-01 00:00:00 GMT  
        Date epoch = new Date(0);  
        if (datePattern != null) {  
            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {  
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);  
                simpleDateFormat.setTimeZone(gmtTimeZone); // do all date  
                                                           // formatting in GMT  
                String r0 = simpleDateFormat.format(epoch);  
                rollingCalendar.setType(i);  
                Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));  
                String r1 = simpleDateFormat.format(next);  
                // System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);  
                if (r0 != null && r1 != null && !r0.equals(r1)) {  
                    return i;  
                }  
            }  
        }  
        return TOP_OF_TROUBLE; // Deliberately head for trouble...  
    }  
  
    /** 
     * Rollover the current file to a new file. 
     */  
    void rollOver() throws IOException {  
  
        /* Compute filename, but only if datePattern is specified */  
        if (datePattern == null) {  
            errorHandler.error("Missing DatePattern option in rollOver().");  
            return;  
        }  
  
        String datedFilename = fileName + sdf.format(now);  
        // It is too early to roll over because we are still within the  
        // bounds of the current interval. Rollover will occur once the  
        // next interval is reached.  
        if (scheduledFilename.equals(datedFilename)) {  
            return;  
        }  
  
        // close current file, and rename it to datedFilename  
        this.closeFile();  
  
        File target = new File(scheduledFilename);  
        if (target.exists()) {  
            target.delete();  
        }  
  
        File file = new File(fileName);  
        boolean result = file.renameTo(target);  
        if (result) {  
            LogLog.debug(fileName + " -> " + scheduledFilename);  
        } else {  
            LogLog.error("Failed to rename [" + fileName + "] to [" + scheduledFilename + "].");  
        }  
  
        try {  
            // This will also close the file. This is OK since multiple  
            // close operations are safe.  
            this.setFile(fileName, true, this.bufferedIO, this.bufferSize);  
        } catch (IOException e) {  
            errorHandler.error("setFile(" + fileName + ", true) call failed.");  
        }  
        scheduledFilename = datedFilename;  
    }  
  
   
    @Override  
    protected void subAppend(LoggingEvent event) {  
        long n = System.currentTimeMillis();  
        if (n >= nextCheck) {  
            now.setTime(n);  
            nextCheck = rc.getNextCheckMillis(now);  
            try {  
                cleanupAndRollOver();  
            } catch (IOException ioe) {  
                if (ioe instanceof InterruptedIOException) {  
                    Thread.currentThread().interrupt();  
                }  
                LogLog.error("cleanupAndRollover() failed.", ioe);  
            }  
        }  
        super.subAppend(event);  
    }  
  
   
    protected void cleanupAndRollOver() throws IOException {  
        File file = new File(fileName);  
        Calendar cal = Calendar.getInstance();  
  
        cal.add(Calendar.DATE, -maxNumberOfDays);  
        Date cutoffDate = cal.getTime();  
  
        cal = Calendar.getInstance();  
        cal.add(Calendar.DATE, -compressBackupsAfterDays);  
        Date cutoffZip = cal.getTime();  
  
        cal = Calendar.getInstance();  
        cal.add(Calendar.DATE, -compressMaxNumberDays);  
        Date cutoffDelZip = cal.getTime();  
  
        if (file.getParentFile().exists()) {  
            File[] files = file.getParentFile().listFiles(new StartsWithFileFilter(file.getName(), false));  
            int nameLength = file.getName().length();  
            for (int i = 0; i < files.length; i++) {  
                String datePart = null;  
                try {  
                    datePart = files[i].getName().substring(nameLength);  
                    Date date = sdf.parse(datePart);  
                    // cutoffDate for deletion should be further back than  
                    // cutoff for backup  
                    if (date.before(cutoffDate)) {  
                        files[i].delete();  
                    } else if (getCompressBackups().equalsIgnoreCase("YES")  
                            || getCompressBackups().equalsIgnoreCase("TRUE")) {  
                        if (date.before(cutoffZip)) {  
                            zipAndDelete(files[i], cutoffZip);  
                        }  
                    }  
                } catch (ParseException pe) {  
                    // Ignore - bad parse format, not a log file, current log  
                    // file, or bad format on log file  
                } catch (Exception e) {  
                    LogLog.warn("Failed to process file " + files[i].getName(), e);  
                }  
                try {  
                    if ((getRollCompressedBackups().equalsIgnoreCase("YES") || getRollCompressedBackups()  
                            .equalsIgnoreCase("TRUE"))  
                            && files[i].getName().endsWith(".zip")) {  
                        datePart = files[i].getName().substring(nameLength, files[i].getName().length() - 4);  
                        Date date = new SimpleDateFormat(compressBackupsDatePattern).parse(datePart);  
                        if (date.before(cutoffDelZip)) {  
                            files[i].delete();  
                        }  
                    }  
                } catch (ParseException e) {  
                    // Ignore - parse exceptions mean that format is wrong or  
                    // there are other files in this dir  
                } catch (Exception e) {  
                    LogLog.warn("Evaluating archive file for rolling failed: " + files[i].getName(), e);  
                }  
            }  
        }  
        rollOver();  
    }  
  
  
    private void zipAndDelete(File file, Date cutoffZip) throws IOException {  
        if (!file.getName().endsWith(".zip")) {  
            String rootLogFileName = new File(fileName).getName();  
            String datePart = file.getName().substring(rootLogFileName.length());  
            String fileRoot = file.getName().substring(0, file.getName().indexOf(datePart));  
            SimpleDateFormat sdf = new SimpleDateFormat(getCompressBackupsDatePattern());  
            String newFile = fileRoot + sdf.format(cutoffZip);  
            File zipFile = new File(file.getParent(), newFile + ".zip");  
  
            if (zipFile.exists()) {  
                addFilesToExistingZip(zipFile, new File[] { file });  
            } else {  
  
                FileInputStream fis = new FileInputStream(file);  
                FileOutputStream fos = new FileOutputStream(zipFile);  
                ZipOutputStream zos = new ZipOutputStream(fos);  
                ZipEntry zipEntry = new ZipEntry(file.getName());  
                zos.putNextEntry(zipEntry);  
  
                byte[] buffer = new byte[4096];  
                while (true) {  
                    int bytesRead = fis.read(buffer);  
                    if (bytesRead == -1)  
                        break;  
                    else {  
                        zos.write(buffer, 0, bytesRead);  
                    }  
                }  
                zos.closeEntry();  
                fis.close();  
                zos.close();  
            }  
            file.delete();  
        }  
    }  
  
  
    public static void addFilesToExistingZip(File zipFile, File[] files) throws IOException {  
        // get a temp file  
        File tempFile = File.createTempFile(zipFile.getName(), null);  
        // delete it, otherwise you cannot rename your existing zip to it.  
        tempFile.delete();  
  
        boolean renameOk = zipFile.renameTo(tempFile);  
        if (!renameOk) {  
            throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to "  
                    + tempFile.getAbsolutePath());  
        }  
        byte[] buf = new byte[1024];  
  
        ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));  
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));  
  
        ZipEntry entry = zin.getNextEntry();  
        while (entry != null) {  
            String name = entry.getName();  
            boolean notInFiles = true;  
            for (File f : files) {  
                if (f.getName().equals(name)) {  
                    notInFiles = false;  
                    break;  
                }  
            }  
            if (notInFiles) {  
                // Add ZIP entry to output stream.  
                out.putNextEntry(new ZipEntry(name));  
                // Transfer bytes from the ZIP file to the output file  
                int len;  
                while ((len = zin.read(buf)) > 0) {  
                    out.write(buf, 0, len);  
                }  
            }  
            entry = zin.getNextEntry();  
        }  
        // Close the streams  
        zin.close();  
        // Compress the files  
        for (int i = 0; i < files.length; i++) {  
            InputStream in = new FileInputStream(files[i]);  
            // Add ZIP entry to output stream.  
            out.putNextEntry(new ZipEntry(files[i].getName()));  
            // Transfer bytes from the file to the ZIP file  
            int len;  
            while ((len = in.read(buf)) > 0) {  
                out.write(buf, 0, len);  
            }  
            // Complete the entry  
            out.closeEntry();  
            in.close();  
        }  
        // Complete the ZIP file  
        out.close();  
        tempFile.delete();  
    }  
  
    public String getCompressBackupsAfterDays() {  
        return "" + compressBackupsAfterDays;  
    }  
  
    public void setCompressBackupsAfterDays(String days) {  
        try {  
            compressBackupsAfterDays = Integer.parseInt(days);  
        } catch (Exception e) {  
            // ignore - just use default  
        }  
    }  
  
    public String getCompressBackupsDatePattern() {  
        return compressBackupsDatePattern;  
    }  
  
    public void setCompressBackupsDatePattern(String pattern) {  
        compressBackupsDatePattern = pattern;  
    }  
  
    public String getCompressMaxNumberDays() {  
        return compressMaxNumberDays + "";  
    }  
  
    public void setCompressMaxNumberDays(String days) {  
        try {  
            this.compressMaxNumberDays = Integer.parseInt(days);  
        } catch (Exception e) {  
            // ignore - just use default  
        }  
    }  
  
    public String getRollCompressedBackups() {  
        return rollCompressedBackups;  
    }  
  
    public void setRollCompressedBackups(String rollCompressedBackups) {  
        this.rollCompressedBackups = rollCompressedBackups;  
    }  
  
}  
  
class StartsWithFileFilter implements FileFilter {  
    private String startsWith;  
    private boolean inclDirs = false;  
  
    /** 
     *  
     */  
    public StartsWithFileFilter(String startsWith, boolean includeDirectories) {  
        super();  
        this.startsWith = startsWith.toUpperCase();  
        inclDirs = includeDirectories;  
    }  
  
    /* 
     * (non-Javadoc) 
     * @see java.io.FileFilter#accept(java.io.File) 
     */  
    @Override  
    public boolean accept(File pathname) {  
        if (!inclDirs && pathname.isDirectory()) {  
            return false;  
        } else  
            return pathname.getName().toUpperCase().startsWith(startsWith);  
    }  
}  
  
/** 
* RollingCalendar is a helper class to AdvancedDailyRollingFileAppender. Given a periodicity type and the current time, 
* it computes the start of the next interval. 
*/  
class RollingCalendar extends GregorianCalendar {  
    private static final long serialVersionUID = -3560331770601814177L;  
  
    int type = AdvancedDailyRollingFileAppender.TOP_OF_TROUBLE;  
  
    RollingCalendar() {  
        super();  
    }  
  
    RollingCalendar(TimeZone tz, Locale locale) {  
        super(tz, locale);  
    }  
  
    void setType(int type) {  
        this.type = type;  
    }  
  
    public long getNextCheckMillis(Date now) {  
        return getNextCheckDate(now).getTime();  
    }  
  
    public Date getNextCheckDate(Date now) {  
        this.setTime(now);  
  
        switch (type) {  
            case AdvancedDailyRollingFileAppender.TOP_OF_MINUTE:  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.MINUTE, 1);  
                break;  
            case AdvancedDailyRollingFileAppender.TOP_OF_HOUR:  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.HOUR_OF_DAY, 1);  
                break;  
            case AdvancedDailyRollingFileAppender.HALF_DAY:  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                int hour = get(Calendar.HOUR_OF_DAY);  
                if (hour < 12) {  
                    this.set(Calendar.HOUR_OF_DAY, 12);  
                } else {  
                    this.set(Calendar.HOUR_OF_DAY, 0);  
                    this.add(Calendar.DAY_OF_MONTH, 1);  
                }  
                break;  
            case AdvancedDailyRollingFileAppender.TOP_OF_DAY:  
                this.set(Calendar.HOUR_OF_DAY, 0);  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.DATE, 1);  
                break;  
            case AdvancedDailyRollingFileAppender.TOP_OF_WEEK:  
                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());  
                this.set(Calendar.HOUR_OF_DAY, 0);  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.WEEK_OF_YEAR, 1);  
                break;  
            case AdvancedDailyRollingFileAppender.TOP_OF_MONTH:  
                this.set(Calendar.DATE, 1);  
                this.set(Calendar.HOUR_OF_DAY, 0);  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.MONTH, 1);  
                break;  
            default:  
                throw new IllegalStateException("Unknown periodicity type.");  
        }  
        return getTime();  
    }  
}  
```

第二種方法

```java
package logger;  
  
import java.io.File;  
import java.io.FilenameFilter;  
import java.io.IOException;  
import java.io.InterruptedIOException;  
import java.io.Serializable;  
import java.net.URI;  
import java.text.SimpleDateFormat;  
import java.util.ArrayList;  
import java.util.Calendar;  
import java.util.Collections;  
import java.util.Date;  
import java.util.GregorianCalendar;  
import java.util.List;  
import java.util.Locale;  
import java.util.TimeZone;  
  
import org.apache.log4j.FileAppender;  
import org.apache.log4j.Layout;  
import org.apache.log4j.helpers.LogLog;  
import org.apache.log4j.spi.LoggingEvent;  
 
public class CustomDailyRollingFileAppender extends FileAppender {  
  
    // The code assumes that the following constants are in a increasing  
    // sequence.  
    static final int TOP_OF_TROUBLE = -1;  
    static final int TOP_OF_MINUTE = 0;  
    static final int TOP_OF_HOUR = 1;  
    static final int HALF_DAY = 2;  
    static final int TOP_OF_DAY = 3;  
    static final int TOP_OF_WEEK = 4;  
    static final int TOP_OF_MONTH = 5;  
  
   
    private String datePattern = "'.'yyyy-MM-dd";  
   
    protected int maxBackupIndex = 1;  
  
    private String scheduledFilename;  
  
    private long nextCheck = System.currentTimeMillis() - 1;  
  
    Date now = new Date();  
  
    SimpleDateFormat sdf;  
  
    RollingCalendar rc = new RollingCalendar();  
  
    int checkPeriod = TOP_OF_TROUBLE;  
  
    // The gmtTimeZone is used only in computeCheckPeriod() method.  
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");  
  
    /** 
       The default constructor does nothing. */  
    public CustomDailyRollingFileAppender() {  
    }  
  
  
    public CustomDailyRollingFileAppender(Layout layout, String filename,  
            String datePattern) throws IOException {  
        super(layout, filename, true);  
        this.datePattern = datePattern;  
        activateOptions();  
    }  
  
   
    public void setDatePattern(String pattern) {  
        datePattern = pattern;  
    }  
  
   
    public void setMaxBackupIndex(int maxBackups) {  
        this.maxBackupIndex = maxBackups;  
    }  
  
    /** 
    Returns the value of the <b>MaxBackupIndex</b> option. 
     */  
    public int getMaxBackupIndex() {  
        return maxBackupIndex;  
    }  
  
    /** Returns the value of the <b>DatePattern</b> option. */  
    public String getDatePattern() {  
        return datePattern;  
    }  
  
    @Override  
    public void activateOptions() {  
        super.activateOptions();  
        if (datePattern != null && fileName != null) {  
            now.setTime(System.currentTimeMillis());  
            sdf = new SimpleDateFormat(datePattern);  
            int type = computeCheckPeriod();  
            printPeriodicity(type);  
            rc.setType(type);  
            File file = new File(fileName);  
            scheduledFilename = fileName  
                    + sdf.format(new Date(file.lastModified()));  
  
        } else {  
            LogLog  
                    .error("Either File or DatePattern options are not set for appender ["  
                            + name + "].");  
        }  
    }  
  
    void printPeriodicity(int type) {  
        switch (type) {  
            case TOP_OF_MINUTE:  
                LogLog.debug("Appender [" + name + "] to be rolled every minute.");  
                break;  
            case TOP_OF_HOUR:  
                LogLog.debug("Appender [" + name  
                        + "] to be rolled on top of every hour.");  
                break;  
            case HALF_DAY:  
                LogLog.debug("Appender [" + name  
                        + "] to be rolled at midday and midnight.");  
                break;  
            case TOP_OF_DAY:  
                LogLog.debug("Appender [" + name + "] to be rolled at midnight.");  
                break;  
            case TOP_OF_WEEK:  
                LogLog.debug("Appender [" + name  
                        + "] to be rolled at start of week.");  
                break;  
            case TOP_OF_MONTH:  
                LogLog.debug("Appender [" + name  
                        + "] to be rolled at start of every month.");  
                break;  
            default:  
                LogLog.warn("Unknown periodicity for appender [" + name + "].");  
        }  
    }  
  
  
    int computeCheckPeriod() {  
        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone,  
                Locale.getDefault());  
        // set sate to 1970-01-01 00:00:00 GMT  
        Date epoch = new Date(0);  
        if (datePattern != null) {  
            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {  
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(  
                        datePattern);  
                simpleDateFormat.setTimeZone(gmtTimeZone); // do all date  
                                                           // formatting in GMT  
                String r0 = simpleDateFormat.format(epoch);  
                rollingCalendar.setType(i);  
                Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));  
                String r1 = simpleDateFormat.format(next);  
                // System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);  
                if (r0 != null && r1 != null && !r0.equals(r1)) {  
                    return i;  
                }  
            }  
        }  
        return TOP_OF_TROUBLE; // Deliberately head for trouble...  
    }  
  
    /** 
       Rollover the current file to a new file. 
     */  
    void rollOver() throws IOException {  
  
        List<ModifiedTimeSortableFile> files = getAllFiles();  
        Collections.sort(files);  
        if (files.size() >= maxBackupIndex) {  
            int index = 0;  
            int diff = files.size() - (maxBackupIndex - 1);  
            for (ModifiedTimeSortableFile file : files) {  
                if (index >= diff)  
                    break;  
  
                file.delete();  
                index++;  
            }  
        }  
  
        /* Compute filename, but only if datePattern is specified */  
        if (datePattern == null) {  
            errorHandler.error("Missing DatePattern option in rollOver().");  
            return;  
        }  
        LogLog.debug("maxBackupIndex=" + maxBackupIndex);  
  
        String datedFilename = fileName + sdf.format(now);  
      
        if (scheduledFilename.equals(datedFilename)) {  
            return;  
        }  
  
        // close current file, and rename it to datedFilename  
        this.closeFile();  
  
        File target = new File(scheduledFilename);  
        if (target.exists()) {  
            target.delete();  
        }  
  
        File file = new File(fileName);  
        boolean result = file.renameTo(target);  
        if (result) {  
            LogLog.debug(fileName + " -> " + scheduledFilename);  
        } else {  
            LogLog.error("Failed to rename [" + fileName + "] to ["  
                    + scheduledFilename + "].");  
        }  
  
        try {  
            // This will also close the file. This is OK since multiple  
            // close operations are safe.  
            this.setFile(fileName, true, this.bufferedIO, this.bufferSize);  
        } catch (IOException e) {  
            errorHandler.error("setFile(" + fileName + ", true) call failed.");  
        }  
        scheduledFilename = datedFilename;  
    }  
  
    @Override  
    protected void subAppend(LoggingEvent event) {  
        long n = System.currentTimeMillis();  
        if (n >= nextCheck) {  
            now.setTime(n);  
            nextCheck = rc.getNextCheckMillis(now);  
            try {  
                rollOver();  
            } catch (IOException ioe) {  
                if (ioe instanceof InterruptedIOException) {  
                    Thread.currentThread().interrupt();  
                }  
                LogLog.error("rollOver() failed.", ioe);  
            }  
        }  
        super.subAppend(event);  
    }  
  
   
    private List<ModifiedTimeSortableFile> getAllFiles() {  
        List<ModifiedTimeSortableFile> files = new ArrayList<ModifiedTimeSortableFile>();  
        FilenameFilter filter = new FilenameFilter() {  
            @Override  
            public boolean accept(File dir, String name) {  
                String directoryName = dir.getPath();  
                LogLog.debug("directory name: " + directoryName);  
                File file = new File(fileName);  
                String perentDirectory = file.getParent();  
                if (perentDirectory != null)  
                {  
                    String localFile = fileName.substring(directoryName.length());  
                    return name.startsWith(localFile);  
                }  
                return name.startsWith(fileName);  
            }  
        };  
        File file = new File(fileName);  
        String perentDirectory = file.getParent();  
        if (file.exists()) {  
            if (file.getParent() == null) {  
                String absolutePath = file.getAbsolutePath();  
                perentDirectory = absolutePath.substring(0, absolutePath.lastIndexOf(fileName));  
  
            }  
        }  
        File dir = new File(perentDirectory);  
        String[] names = dir.list(filter);  
  
        for (int i = 0; i < names.length; i++) {  
            files.add(new ModifiedTimeSortableFile(dir + System.getProperty("file.separator") + names[i]));  
        }  
        return files;  
    }  
}  
  
/** 
* The Class ModifiedTimeSortableFile extends java.io.File class and 
* implements Comparable to sort files list based upon their modified date 
*/  
class ModifiedTimeSortableFile extends File implements Serializable, Comparable<File> {  
    private static final long serialVersionUID = 1373373728209668895L;  
  
    public ModifiedTimeSortableFile(String parent, String child) {  
        super(parent, child);  
        // TODO Auto-generated constructor stub  
    }  
  
    public ModifiedTimeSortableFile(URI uri) {  
        super(uri);  
        // TODO Auto-generated constructor stub  
    }  
  
    public ModifiedTimeSortableFile(File parent, String child) {  
        super(parent, child);  
    }  
  
    public ModifiedTimeSortableFile(String string) {  
        super(string);  
    }  
  
    @Override  
    public int compareTo(File anotherPathName) {  
        long thisVal = this.lastModified();  
        long anotherVal = anotherPathName.lastModified();  
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));  
    }  
}  
  
 
class RollingCalendar extends GregorianCalendar {  
    private static final long serialVersionUID = -3560331770601814177L;  
  
    int type = CustomDailyRollingFileAppender.TOP_OF_TROUBLE;  
  
    RollingCalendar() {  
        super();  
    }  
  
    RollingCalendar(TimeZone tz, Locale locale) {  
        super(tz, locale);  
    }  
  
    void setType(int type) {  
        this.type = type;  
    }  
  
    public long getNextCheckMillis(Date now) {  
        return getNextCheckDate(now).getTime();  
    }  
  
    public Date getNextCheckDate(Date now) {  
        this.setTime(now);  
  
        switch (type) {  
            case CustomDailyRollingFileAppender.TOP_OF_MINUTE:  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.MINUTE, 1);  
                break;  
            case CustomDailyRollingFileAppender.TOP_OF_HOUR:  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.HOUR_OF_DAY, 1);  
                break;  
            case CustomDailyRollingFileAppender.HALF_DAY:  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                int hour = get(Calendar.HOUR_OF_DAY);  
                if (hour < 12) {  
                    this.set(Calendar.HOUR_OF_DAY, 12);  
                } else {  
                    this.set(Calendar.HOUR_OF_DAY, 0);  
                    this.add(Calendar.DAY_OF_MONTH, 1);  
                }  
                break;  
            case CustomDailyRollingFileAppender.TOP_OF_DAY:  
                this.set(Calendar.HOUR_OF_DAY, 0);  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.DATE, 1);  
                break;  
            case CustomDailyRollingFileAppender.TOP_OF_WEEK:  
                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());  
                this.set(Calendar.HOUR_OF_DAY, 0);  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.WEEK_OF_YEAR, 1);  
                break;  
            case CustomDailyRollingFileAppender.TOP_OF_MONTH:  
                this.set(Calendar.DATE, 1);  
                this.set(Calendar.HOUR_OF_DAY, 0);  
                this.set(Calendar.MINUTE, 0);  
                this.set(Calendar.SECOND, 0);  
                this.set(Calendar.MILLISECOND, 0);  
                this.add(Calendar.MONTH, 1);  
                break;  
            default:  
                throw new IllegalStateException("Unknown periodicity type.");  
        }  
        return getTime();  
    }  
  
}  
```

❸log4j自動刪除的配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>  

<configuration status="OFF">  
  <appenders>  
    <Console name="OUT" target="SYSTEM_OUT">  
      <PatternLayout pattern="[%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} %l >>> %m %n"/>  
    </Console> 
    <RollingFile name="INFO" fileName="logs/info.log" filePattern="logs/info/$${date:yyyy-MM}/info-%d{yyyy-MM-dd}-%i.log">
    <PatternLayout pattern="[%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} %l >>> %m %n"/> 
    <SizeBasedTriggeringPolicy size="100 MB" />
    <LevelRangeFilter minLevel="info" maxLevel="info" onMatch="ACCEPT" />
    </RollingFile>
    <RollingFile name="DEBUG" fileName="logs/debug.log" filePattern="logs/debug/$${date:yyyy-MM}/debug-%d{yyyy-MM-dd}-%i.log">
    <PatternLayout pattern="[%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} %l >>> %m %n"/> 
    <SizeBasedTriggeringPolicy size="100 MB" />
    <LevelRangeFilter minLevel="debug" maxLevel="debug" onMatch="ACCEPT" />
    </RollingFile>
    <RollingFile name="ERROR" fileName="logs/error.log" filePattern="logs/error/$${date:yyyy-MM}/error-%d{yyyy-MM-dd}-%i.log">
    <PatternLayout pattern="[%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} %l >>> %m %n"/> 
    <SizeBasedTriggeringPolicy size="100 MB" />
    <LevelRangeFilter minLevel="error" maxLevel="error" onMatch="ACCEPT" />
    </RollingFile>
  </appenders>  


  <loggers>  
  <logger name="org.eclipse.jetty" level="error" additivity="false">
  <appender-ref ref="ERROR"/>
  </logger>
  
  <logger name="org.apache.shiro.web.servlet" level="error" additivity="false">
  <appender-ref ref="ERROR"/>
  </logger>
  
  <logger name="org.apache.tiles.impl" level="info" additivity="false">
  <appender-ref ref="INFO"/>
  </logger>
  
  <logger name="org.apache.shiro.session.mgt" level="error" additivity="false">
  <appender-ref ref="ERROR"/>
  </logger>
  
  <logger name="org.apache.ibatis.transaction.jdbc" level="error" additivity="false">
  <appender-ref ref="ERROR"/>
  </logger>
  
  <logger name="com.alibaba.druid.support.logging" level="error" additivity="false">
  <appender-ref ref="ERROR"/>
  </logger>
  
    <root level="DEBUG">  
      <appender-ref ref="OUT"/>
        <appender-ref ref="INFO"/>
        <appender-ref ref="DEBUG"/>
        <appender-ref ref="ERROR"/>
    </root>  
  </loggers>  
</configuration>  
```

第四個試一下效果

  準備兩個jar包：log4j-1.2.17.jar,commons-logging-1.1.1.jar 

```java
package log;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
 
 
 
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
public class RoolingAndDateFileAppender extends RollingFileAppender{
	private String datePattern;
	private String dateStr="";//文件后面的日期
	private String expirDays="1";//保留最近几天
	private String isCleanLog="true";
	private String maxIndex="100";
	private File rootDir;
	public void setDatePattern(String datePattern){
		if(null!=datePattern&&!"".equals(datePattern)){
			this.datePattern=datePattern;
		}
	}
	public String getDatePattern(){
		return this.datePattern;
	}
	public void rollOver(){
		dateStr=new SimpleDateFormat(this.datePattern).format(new Date(System.currentTimeMillis()));
		File target = null;
		File file=null;
		if(qw!=null){
			long size=((CountingQuietWriter)this.qw).getCount();
			LogLog.debug("rolling over count="+size);
		}
		LogLog.debug("maxBackupIndex="+this.maxBackupIndex);
		//如果maxIndex<=0则不需命名
		if(maxIndex!=null&&Integer.parseInt(maxIndex)>0){
			//删除旧文件
			file=new File(this.fileName+'.'+dateStr+'.'+Integer.parseInt(this.maxIndex));
			if(file.exists()){
				//如果当天日志达到最大设置数量，则删除当天第一个日志，其他日志为尾号减一
				Boolean boo = reLogNum();
				if(!boo){
					LogLog.debug("日志滚动重命名失败！");
				}
			}
	}
		//获取当天日期文件个数
		int count=cleanLog();
		//生成新文件
		target=new File(fileName+"."+dateStr+"."+(count+1));
		this.closeFile();
		file=new File(fileName);
		LogLog.debug("Renaming file"+file+"to"+target);
		file.renameTo(target);
		try{
			setFile(this.fileName,false,this.bufferedIO,this.bufferSize);
		}catch(IOException e){
			LogLog.error("setFile("+this.fileName+",false)call failed.",e);
		}
	}
	public int cleanLog(){
		int count=0;//记录当天文件个数
		if(Boolean.parseBoolean(isCleanLog)){
			File f=new File(fileName);
			rootDir=f.getParentFile();
			File[] listFiles = rootDir.listFiles();
			for(File file:listFiles){
				if(file.getName().contains(dateStr)){
					count=count+1;//是当天日志，则+1
				}else{
					if(Boolean.parseBoolean(isCleanLog)){
						//清除过期日志
						String[] split=file.getName().split("\\\\")[0].split("\\.");
						//校验日志名字，并取出日期，判断过期时间
						if(split.length==4 && isExpTime(split[2])){
							file.delete();
					}
				}
				}
			}
		}
		return count;
	}
	public Boolean isExpTime(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date logTime=format.parse(time);
			Date nowTime=format.parse(format.format(new Date()));
			//算出日志与当前日期相差几天
			int days=(int)(nowTime.getTime()-logTime.getTime())/(1000*3600*24);
			if(Math.abs(days)>=Integer.parseInt(expirDays)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			LogLog.error(e.toString());
			return false;
		}
	}
	/**
	 * 如果当天日志达到最大设置数量，则每次删除尾号为1的日志，
	 * 其他日志编号依次减去1，重命名
	 * @return
	 */
	public Boolean reLogNum(){
		boolean renameTo=false;
		File startFile = new File(this.fileName+'.'+dateStr+'.'+"1");
		if(startFile.exists()&&startFile.delete()){
			for(int i=2;i<=Integer.parseInt(maxIndex);i++){
				File target = new File(this.fileName+'.'+dateStr+'.'+(i-1));
				this.closeFile();
				File file = new File(this.fileName+'.'+dateStr+'.'+i);
				renameTo=file.renameTo(target);
			}
		}
		return renameTo;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getExpirDays() {
		return expirDays;
	}
	public void setExpirDays(String expirDays) {
		this.expirDays = expirDays;
	}
	public String getIsCleanLog() {
		return isCleanLog;
	}
	public void setIsCleanLog(String isCleanLog) {
		this.isCleanLog = isCleanLog;
	}
	public String getMaxIndex() {
		return maxIndex;
	}
	public void setMaxIndex(String maxIndex) {
		this.maxIndex = maxIndex;
	}
	
  }

```

配置文件

```properties
log4j.rootLogger=ALL,R,CONSOLE
 
log4j.appender.R=log.RoolingAndDateFileAppender
#log4j.appender.R.Encoding=UTF-8
log4j.appender.R.file=../log/logs/logRecoed.log
log4j.appender.R.Append=true
log4j.appender.R.DatePattern=yyyy-MM-dd
log4j.appender.R.MaxFileSize=5KB
log4j.appender.R.maxIndex=10
log4j.appender.R.expirDays=1
log4j.appender.R.isCleanLog=true
 
log4j.appender.R.layout.ConversionPattern=[%p] %t %c %d{yyyy-MM-dd HH:mm:ss} %m  %n
log4j.appender.R.layout=org.apache.log4j.PatternLayout
 
#console
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout.ConversionPattern=[%p] %t %c %d{yyyy-MM-dd HH:mm:ss} %m  %n
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout

```

測試程序

```java

public class TextLog {
public static void main(String[] args) {
	Log log = LogFactory.getLog(TextLog.class);
	for(int i=0;i<=500;i++){
		System.out.println("循环"+"--"+i);
		try{
			System.out.println(1%0);
		}catch(Exception e){
			log.info("测试日志"+"--"+"异常信息"+i+"："+e);
		}
	}
}
}
```

##### 9.保存到文件服務器

   一、服務器端操作

   在服務器上新建目錄

```
[dtadmin@apollo~]$ cd ~ #跳转到home目录
[dtadmin@apollo~]$ sudo mkdir log4j #新建目录log4j  
```

  上傳jar包到新建的目錄

  在log4j目錄下新建配置文件，log4j-server.properties,內容如下

```xml
log4j.rootLogger = DEBUG,file
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File = ./log/message.log
log4j.appender.file.MaxFileSize = 1MB
log4j.appender.file.MaxBackupIndex = 1
log4j.appender.file.layout = org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern = [%d] [%t] [%m]%n
```

啟動服務器監聽 

```
[root@apollo log4j]# java -classpath log4j-1.2.17.jar org.apache.log4j.net.SimpleSocketServer 4712 log4j-server.properties 
```

二、客戶端操作

 配置文件

```
# 定义LOG输出级别
log4j.rootLogger=DEBUG,Console,to11HitLog
###输出到控制台 ###
log4j.appender.Console = org.apache.log4j.ConsoleAppender
log4j.appender.Console.Target = System.out
log4j.appender.Console.layout = org.apache.log4j.PatternLayout
log4j.appender.Console.layout.ConversionPattern=%d{ABSOLUTE} %5p %c:%L - %m%n


### Socket Appender
log4j.appender.to11HitLog = org.apache.log4j.net.SocketAppender
log4j.appender.to11HitLog.Port = 4712
log4j.appender.to11HitLog.RemoteHost = 192.168.56.181
log4j.appender.to11HitLog.ReconnectionDelay = 10000
```

 測試程序

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/test")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = { "/query"})
    @ResponseBody
    public void query() {
        logger.info("操作用户","操作类型","操作功能");
        logger.trace("logback的--trace日志--输出了","操作用户","操作类型","操作功能");
        logger.debug("logback的--debug日志--输出了","操作用户","操作类型","操作功能");
        logger.info("logback的--info日志--输出了","操作用户","操作类型","操作功能");
        logger.warn("logback的--warn日志--输出了","操作用户","操作类型","操作功能");
        logger.error("logback的--error日志--输出了","操作用户","操作类型","操作功能");
        System.out.println("asdfoiwef");

    }
}

```

##### 10.保存到DB中

​    兩個jar包：log4j.jar 和commons-logging.jar。 

```xml
<dependency>
	<groupId>commons-logging</groupId>
	<artifactId>commons-logging</artifactId>
	<version>1.2</version>
</dependency>
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.12</version>
</dependency>
		
```

​    配置文件

```properties
log4j.rootLogger=WARN,LOGDB2

log4j.appender.LOGDB2.bufferSize=10
log4j.appender.LOGDB2=org.apache.log4j.jdbc.JDBCAppender
log4j.appender.LOGDB2.Driver=oracle.jdbc.driver.OracleDriver
log4j.appender.LOGDB2.URL=jdbc:oracle:thin:@10.148.202.1:1521:MESDEVDB
log4j.appender.LOGDB2.user=study
log4j.appender.LOGDB2.password=study
//创建记录的sql--这里需要自己手动去创建一个记录表,注意表列名一一对应,本人测试日期类型必须为varchar2,不知道是不是oracle的问题
log4j.appender.LOGDB2.sql=INSERT INTO Log_oracle_DB (UUID,CDATED,CLOGGER,CLEVEL,CMESSAGE) VALUES('%x','%d{yyyy-MM-dd HH:mm:ss}','%C','%p','%m')
log4j.appender.LOGDB2.layout=org.apache.log4j.PatternLayout
```

新建数据库表

```sql
CREATE TABLE Log_oracle_DB (
     UUID VARCHAR2(50) NOT NULL,
     CDATED VARCHAR2(50),
     CLOGGER VARCHAR2(200),
     CLEVEL VARCHAR2(200),
     CMESSAGE CLOB       
)
drop table Log_oracle_DB
```

想哭，報警==》缺失逗號

```java
log4j.appender.LOGDB.sql=insert into  Log_oracle_DB(UUID,CDATED,CLOGGER,CLEVEL,CMESSAGE) VALUES ('%r','%d{yyyy-MM-dd HH:mm:ss}','%c','%p','%m')
================================
如果系统中的部分组件打印信息中包含"'"（单引号），系统会报“确实逗号”的错误，搞了半天也解决不了，还好打印的都是INFO，把打印级别设为“WARN”就OK了 
```

#### 二、Slf4j的相關知識介紹

##### 1.slf介紹

```
SLF4J，即简单日志门面（Simple Logging Facade for Java），不是具体的日志解决方案，它只服务于各种各样的日志系统。按照官方的说法，SLF4J是一个用于日志系统的简单Facade，允许最终用户在部署其应用时使用其所希望的日志Systey
================================
說白了就是要和log4j或者logback配合使用
```

為什麼用slf4j

```
1. slf4j是一个日志接口，自己没有具体实现日志系统，只提供了一组标准的调用api,这样将调用和具体的日志实现分离，使用slf4j后有利于根据自己实际的需求更换具体的日志系统，比如，之前使用的具体的日志系统为log4j,想更换为logback时，只需要删除log4j相关的jar,然后加入logback相关的jar和日志配置文件即可，而不需要改动具体的日志输出方法，试想如果没有采用这种方式，当你的系统中日志输出有成千上万条时，你要更换日志系统将是多么庞大的一项工程。如果你开发的是一个面向公众使用的组件或公共服务模块，那么一定要使用slf4的这种形式，这有利于别人在调用你的模块时保持和他系统中使用统一的日志输出。

2.slf4j日志输出时可以使用{}占位符，如，logger.info("testlog: {}", "test")，而如果只使用log4j做日志输出时，只能以logger.info("testlog:"+"test")这种形式，前者要比后者在性能上更好，后者采用+连接字符串时就是new 一个String 字符串，在性能上就不如前者。
```



##### 2.slf4j和log4j一起使用

引入下面的依賴，並且添加配置文件

```xml
<dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.12</version>
</dependency>

<dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.6.6</version>
</dependency>

 <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
          <version>1.6.6</version>
 </dependency>
```

3.配置文件

```
同上；
```



##### 3.slf4j和logback一起使用

相關依賴

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.21</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.1.7</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.1.7</version>
</dependency>
```

配置文件  --相對比較全的版本

```xml
<?xml version="1.0" ?>
<configuration debug="false">
    <!--控制台日志 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>      ==必须填写的属性 class，用来指定具体的实现类，如果该类的类型是 PatternLayoutEncoder ，那么 class 属性可以不填
            <pattern>
                <!--日志输出格式-->
                %d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger - %msg%n
            </pattern>
        </encoder>
    </appender>

    <!-- INFO级别日志,记录INFO级别及比INFO更高级别的日志-->
    <appender name="INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                <!--日志引文件路径-->
                C:\workspace\logs\%d{yyyy-MM-dd}\dp.log
            </fileNamePattern>
            <!-- 日志保存15天，超过15天自动删除 -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>
                <!--日志输出格式-->
                %d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger - %msg%n
            </pattern>
        </encoder>
    </appender>

    <!-- ERROR级别日志,只记录Error级别日志 -->
    <appender name="ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器,只记录ERROR级别的日志,如果日志级别等于配置级别，过滤器会根据onMath 和 onMismatch接收或拒绝日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                <!--日志文件路径-->
                C:\workspace\logs\%d{yyyy-MM-dd}\dp.error.log
            </fileNamePattern>
            <!-- 日志保存15天，超过15天自动删除 -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>
                <!--日志输出格式-->
                %d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger - %msg%n
            </pattern>
        </encoder>
    </appender>

    <root>
        <!-- 日志级别 -->
        <level value="INFO"/>
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="INFO"/>
        <appender-ref ref="ERROR"/>
    </root>
</configuration>
```

##### 

##### 4.測試

```java
//导入的包都是slf4j的包
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/taskwait")
public class TaskController{
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @RequestMapping(value = { "/queryActivity.rf" }, method = RequestMethod.POST)
    @ResponseBody
    public void queryActivity(@RequestBody OTCActivityConditionVO conditionVO) {
        logger.trace("logback的--trace日志--输出了");
        logger.debug("logback的--debug日志--输出了");
        logger.info("logback的--info日志--输出了");
        logger.warn("logback的--warn日志--输出了");
        logger.error("logback的--error日志--输出了");
    }
}
```



#### 三、LogBack的相關知識介紹

##### 1.介紹

```
Logback是由log4j创始人设计的又一个开源日志组件。                              ----既生瑜，何生亮

logback当前分成三个模块：logback-core,logback- classic和logback-access。logback-core是其它两个模块的基础模块。logback-classic是log4j的一个 改良版本。此外logback-classic完整实现SLF4J API使你可以很方便地更换成其它日志系统如log4j或JDK14 Logging。logback-access访问模块与Servlet容器集成提供通过Http来访问日志的功能。
```

##### 2.依賴

```xml
<dependency>  
       <groupId>ch.qos.logback</groupId>  
       <artifactId>logback-classic</artifactId>  
    <version>1.0.11</version>  
</dependency>

//其他依賴包全都自動下載了，就是下面兩個
<!--
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.6.4</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>1.0.6</version>
    </dependency>
-->

```

##### 3.logback的優點

```
1、更快的实现  Logback的内核重写了，在一些关键执行路径上性能提升10倍以上。而且logback不仅性能提升了，初始化内存加载也更小了。

2、非常充分的测试  Logback经过了几年，数不清小时的测试。Logback的测试完全不同级别的。

3、Logback-classic非常自然实现了SLF4j    Logback-classic实现了 SLF4j。在使用SLF4j中，你都感觉不到logback-classic。而且因为logback-classic非常自然地实现了SLF4J，  所以切换到log4j或者其他日志框架非常容易，只需要提供成另一个jar包就OK，根本不需要去动那些通过SLF4JAPI实现的代码。

4、非常充分的文档  官方网站有两百多页的文档。

5、自动重新加载配置文件  当配置文件修改了，Logback-classic能自动重新加载配置文件。扫描过程快且安全，它并不需要另外创建一个扫描线程。这个技术充分保证了应用程序能在JEE环境里面跑得很欢。

6、Lilith   Lilith是log事件的观察者，和log4j的chainsaw类似。而lilith还能处理大数量的log数据 。

7、谨慎的模式和非常友好的恢复  在谨慎模式下，多个FileAppender实例跑在多个JVM下，能够安全地写到同一个日志文件。RollingFileAppender会有些限制。Logback的FileAppender和它的子类包括RollingFileAppender能够非常友好地从I/O异常中恢复。

8、配置文件可以处理不同的情况   开发人员经常需要判断不同的Logback配置文件在不同的环境下（开发，测试，生产）。而这些配置文件仅仅只有一些很小的不同，可以通过,和来实现，这样一个配置文件就可以适应多个环境。

9、Filters（过滤器）  有些时候，需要诊断一个问题，需要打出日志。在log4j，只有降低日志级别，不过这样会打出大量的日志，会影响应用性能。在Logback，你可以继续保持那个日志级别而除掉某种特殊情况，如alice这个用户登录，她的日志将打在DEBUG级别而其他用户可以继续打在WARN级别。要实现这个功能只需加4行XML配置。可以参考MDCFIlter 。

10、SiftingAppender（一个非常多功能的Appender）  它可以用来分割日志文件根据任何一个给定的运行参数。如，SiftingAppender能够区别日志事件跟进用户的Session，然后每个用户会有一个日志文件。

11、自动压缩已经打出来的log  RollingFileAppender在产生新文件的时候，会自动压缩已经打出来的日志文件。压缩是个异步过程，所以甚至对于大的日志文件，在压缩过程中应用不会受任

12、堆栈树带有包版本  Logback在打出堆栈树日志时，会带上包的数据。

13、自动去除旧的日志文件  通过设置TimeBasedRollingPolicy或者SizeAndTimeBasedFNATP的maxHistory属性，你可以控制已经产生日志文件的最大数量。如果设置maxHistory 12，那那些log文件超过12个月的都会被自动移除
```

##### 4.**Logback的配置介绍** 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="/home" />  
    <!-- 控制台输出 -->   
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
             <!--格式化输出,%d:日期;%thread:线程名;%-5level：级别,从左显示5个字符宽度;%msg:日志消息;%n:换行符--> 
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>   
        </encoder>
    </appender>
    <!-- 按照每天生成日志文件 -->   
    <appender name="FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender">   
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/TestWeb.log.%d{yyyy-MM-dd}.log</FileNamePattern> 
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>   
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
            <!--格式化输出,%d:日期;%thread:线程名;%-5level：级别,从左显示5个字符宽度;%msg:日志消息;%n:换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>   
        </encoder> 
        <!--日志文件最大的大小-->
       <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
         <MaxFileSize>10MB</MaxFileSize>
       </triggeringPolicy>
    </appender> 
   <!-- show parameters for hibernate sql 专为 Hibernate 定制 -->      ==logger設置詳細的打印
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder"  level="TRACE" />  
    <logger name="org.hibernate.type.descriptor.sql.BasicExtractor"  level="DEBUG" />  
    <logger name="org.hibernate.SQL" level="DEBUG" />  
    <logger name="org.hibernate.engine.QueryParameters" level="DEBUG" />
    <logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG" />  
    
    <!--myibatis log configure--> 
    <logger name="com.apache.ibatis" level="TRACE"/>               ==可以在logger裡面配置additivity，是否向上级loger传递打印信息 
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>
    
    <!-- 日志输出级别 -->
    <root level="INFO">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="FILE" />
    </root> 
     <!--日志异步到数据库 -->  
    <appender name="DB" class="ch.qos.logback.classic.db.DBAppender">
        <!--日志异步到数据库 --> 
        <connectionSource class="ch.qos.logback.core.db.DriverManagerConnectionSource">
           <!--连接池 --> 
           <dataSource class="com.mchange.v2.c3p0.ComboPooledDataSource">
              <driverClass>oracle.jdbc.driver.OracleDriver</driverClass>
              <url>jdbc:oracle:thin:@10.148.202.1:1521:MESDEVDB</url>
              <user>study</user>
              <password>study</password>
            </dataSource>
        </connectionSource>
    </appender>
</configuration>
===================================
必須要為root，logger指定打印機別
TRACE < DEBUG < INFO < WARN < ERROR 
```

```properties
#控制台输出格式
stdout.pattern=[lf-1][service][%d{yyyy-MM-dd HH\:mm\:ss.SSS}][%-5level][%thread]%msg%n
#日志输出编码
stdout.charset=UTF-8
#日志输出级别
stdout.level=info


###正常日志文件
#日志输出格式
normal.pattern=[lf-1][service][%d{yyyy-MM-dd HH:mm:ss.SSS}][%-5level][%thread]%msg%n
#日志输出级别
normal.level=info
#日志文件路径
normal.file=/home/logs/service/service.log
#日志文件名称格式
normal.fileNamePattern=/home/logs/service/service.log.%d{yyyy-MM-dd}
#日志文件保留天数
normal.maxHistory=7

###异常日志文件
#日志输出格式
error.pattern=[lf-1][service][%d{yyyy-MM-dd HH:mm:ss.SSS}][%-5level][%thread]%msg%n
#日志输出级别
error.level=warn
#日志文件路径
error.file=/home/logs/service/service-error.log
#日志文件名称格式
error.fileNamePattern=/home/logs/service/service-error.log.%d{yyyy-MM-dd}
#日志文件保留天数
error.maxHistory=10
```

##### 5.各參數的含義，針對以xml配置文件

```xml
scan:当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。

scanPeriod:设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。

debug:当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。
===============================
<configuration scan="true" scanPeriod="60 seconds" debug="false">  
      <!-- 其他配置省略-->  
</configuration> 
===============================
當情況是带有多个loger的配置，指定级别，指定appender  
<configuration>   
   <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">   
    <!-- encoder 默认配置为PatternLayoutEncoder -->   
    <encoder>   
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>   
    </encoder>   
  </appender>   
  
<!-- =======================================這裡是重點======================================== -->   
  <!-- logback为java中的包 -->   
  <logger name="logback"/>        ==logback包下的所有类的日志的打印，但是并没用设置打印级别，所以继承他的上级<root>的日志级别“DEBUG”；
                                  ==没有设置additivity，默认为true，将此loger的打印信息向上级传递；
                                  ==没有设置appender，此loger本身不打印任何信息。
  <!--logback.LogbackDemo：类的全路径 -->   
  <logger name="logback.LogbackDemo" level="INFO" additivity="false">     ==當開啟后，控制台會打印兩邊日誌
    <appender-ref ref="STDOUT"/>  
  </logger>   
<!-- =======================================這裡是重點========================================= -->       
  <root level="ERROR">             
    <appender-ref ref="STDOUT" />   
  </root>     
</configuration>  
    
  Logger 可以被分配级别。级别包括：TRACE、DEBUG、INFO、WARN 和 ERROR，定义于 ch.qos.logback.classic.Level类。如果 logger没有被分配级别，那么它将从有被分配级别的最近的祖先那里继承级别。root logger 默认级别是 DEBUG。

```



##### 6.測試

萬年不變： Logger logger = LoggerFactory.getLogger(xxx.class);

```java
package com.stu.system.action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogAction{
     //定义一个全局的记录器，通过LoggerFactory获取
    private final static Logger logger = LoggerFactory.getLogger(BlogAction.class);
    public static void main(String[] args) {
        logger.info("logback 成功了");
        logger.error("logback 成功了");
    }
}
```



##### 7.更全面的配置文件 

```xml
<?xml version="1.0" encoding="UTF-8"?>    
<configuration>  
     
    <!-- 引入配置文件中的变量 logback.properties 定义了一个值log_path-->
    <property resource="logback.properties" />
      
    <!-- 自己声明一个变量 -->
    <!-- <property name="log_path" value="${catalina.home}" /> -->
     
     
    <!-- 控制台输出 -->
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%date{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
     
    <!-- 按分钟时间滚动输出 level为 INFO 日志 -->
    <appender name="file—debug" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${log_path}/logs/debug.log</File>
        <!--控制台只输出level级别的信息（onMatch），其他的直接拒绝（onMismatch） -->  
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY </onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${log_path}/logs/debug.%d{yyyy-MM-dd_HH-mm}.log</FileNamePattern>
            <!-- 保留的归档文件的最大数量，超出数量就删除旧文件，此处是按分钟滚动，30则代表30分钟 -->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%date [%thread] %-5level %logger{80} - %msg%n</pattern>
        </encoder>
    </appender>
     
    <!-- 特定过滤含有某字符串的日志 -->
    <appender name="file-str" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${log_path}/logs/contains.log</File>
        <filter class="ch.qos.logback.core.filter.EvaluatorFilter">
            <evaluator>
                <!-- 日志信息中需要包含指定的字符串 -->
                <expression>message.contains("abc")</expression>
            </evaluator>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${log_path}/logs/contains.%d{yyyy-MM-dd}.log </FileNamePattern>
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%date [%thread] %-5level %logger{80} - %msg%n</pattern>
        </encoder>
    </appender>
     
    <!-- 按照每天和每个日志文件大小生成日志文件 -->   
    <appender name="audioDist"  class="ch.qos.logback.core.rolling.RollingFileAppender">   
        <File>${log_path}/logs/audioDist.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${log_path}/logs/audioDist.%d{yyyy-MM-dd}.%i.log</FileNamePattern> 
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
            <!-- 单个日志文件的大小 单位KB,MB,GB -->
            <maxFileSize>1KB</maxFileSize> 
            <!-- 所有日志文件的总大小空间。当日志文件的空间超过了设置的最大空间数量，就会删除旧的文件。注意：这个标签必须和maxHistory标签一起使用。 -->
            <totalSizeCap>10MB</totalSizeCap> 
        </rollingPolicy>   
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符--> 
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>   
        </encoder> 
    </appender> 
     
    <!-- 指定包输出指定级别日志 -->
    <logger name="com.gavin">
        <level value="warn" />
    </logger>
         
    <root level="DEBUG">
        <appender-ref ref="stdout" />
        <appender-ref ref="audioDist"/>
        <appender-ref ref="file—debug"/>
        <appender-ref ref="file-str"/>
    </root>   
</configuration>  
```

##### 8.持久化到數據庫

课外知识

```
表的创建sql可以到Logback在GutHup中的源码库里拿，支持Oracle、mysql等
目录为：/logback-classic/src/main/resources/ch/qos/logback/classic/db/script
```

```xml
<dependency>
	<groupId>commons-dbcp</groupId>
	<artifactId>commons-dbcp</artifactId>
</dependency>
====================================
<dependency>
    <groupId>com.oracle</groupId>
    <artifactId>ojdbc6</artifactId>
    <version>11.2.0.3</version>
</dependency>
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5</version>
</dependency>

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
</dependency>
```

ORACLE建表语句

```
-- Logback: the reliable, generic, fast and flexible logging framework.
-- Copyright (C) 1999-2010, QOS.ch. All rights reserved.
--
-- See http://logback.qos.ch/license.html for the applicable licensing 
-- conditions.


-- This SQL script creates the required tables by ch.qos.logback.classic.db.DBAppender
--
-- It is intended for Oracle 9i, 10g and 11g databases. Tested on version 9.2, 
-- 10g and 11g.

-- The following lines are useful in cleaning any previously existing tables 

--drop TRIGGER logging_event_id_seq_trig; 
--drop SEQUENCE logging_event_id_seq; 
--drop table logging_event_property; 
--drop table logging_event_exception; 
--drop table logging_event; 


CREATE SEQUENCE logging_event_id_seq MINVALUE 1 START WITH 1;

CREATE TABLE logging_event 
  (
    timestmp         NUMBER(20) NOT NULL,
    formatted_message  VARCHAR2(4000) NOT NULL,
    logger_name       VARCHAR(254) NOT NULL,
    level_string      VARCHAR(254) NOT NULL,
    thread_name       VARCHAR(254),
    reference_flag    SMALLINT,
    arg0              VARCHAR(254),
    arg1              VARCHAR(254),
    arg2              VARCHAR(254),
    arg3              VARCHAR(254),
    caller_filename   VARCHAR(254) NOT NULL,
    caller_class      VARCHAR(254) NOT NULL,
    caller_method     VARCHAR(254) NOT NULL,
    caller_line       CHAR(4) NOT NULL,
    event_id          NUMBER(10) PRIMARY KEY
  );


-- the / suffix may or may not be needed depending on your SQL Client
-- Some SQL Clients, e.g. SQuirrel SQL has trouble with the following
-- trigger creation command, while SQLPlus (the basic SQL Client which
-- ships with Oracle) has no trouble at all.

CREATE TRIGGER logging_event_id_seq_trig
  BEFORE INSERT ON logging_event
  FOR EACH ROW  
  BEGIN  
    SELECT logging_event_id_seq.NEXTVAL 
    INTO   :NEW.event_id 
    FROM   DUAL;  
  END;
/


CREATE TABLE logging_event_property
  (
    event_id	      NUMBER(10) NOT NULL,
    mapped_key        VARCHAR2(254) NOT NULL,
    mapped_value      VARCHAR2(1024),
    PRIMARY KEY(event_id, mapped_key),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );
  
CREATE TABLE logging_event_exception
  (
    event_id         NUMBER(10) NOT NULL,
    i                SMALLINT NOT NULL,
    trace_line       VARCHAR2(254) NOT NULL,
    PRIMARY KEY(event_id, i),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );
```



```xml
<?xml version="1.0" encoding="UTF-8"?>
 
<configuration debug="false">
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="E:/Code/log" />
    <!--将日志写入文件的配置信息在上节中，本节就不展示了，只展示重点配置-->
    <!-- 彩色日志 -->
    <!-- 按照每天生成日志文件 -->
    <!--info-->
    <!--error appender-->
 
    <!-- 没见过的连接池将日志写入数据库 -->
    <appender name="DB-CLASSIC-MYSQL-POOL" class="ch.qos.logback.classic.db.DBAppender">
        <connectionSource class="ch.qos.logback.core.db.DataSourceConnectionSource">
            <dataSource class="org.apache.commons.dbcp.BasicDataSource">
                <driverClassName>oracle.jdbc.driver.OracleDriver</driverClassName>
                <url>jdbc:oracle:thin:@10.148.202.1:1521:MESDEVDB</url>
                <username>study</username>
                <password>study</password>
            </dataSource>
        </connectionSource>
        <!--这里设置日志级别为error-->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>error</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>
 
    <!-- 日志输出级别 TRACE < DEBUG < INFO < WARN < ERROR < FATAL-->
    <root level="INFO">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="DAYINFO" />
        <appender-ref ref="DAYERROR" />
        <appender-ref ref="DB-CLASSIC-MYSQL-POOL" />
    </root>
</configuration>
```

=======添加了方言，而且驅動的名稱也換了=============================

```xml
<!-- 将日志存储到oracle数据库中-->
<appender name="db-classic-oracle" class="ch.qos.logback.classic.db.DBAppender">  
    <connectionSource class="ch.qos.logback.core.db.DataSourceConnectionSource">
        <dataSource class="com.alibaba.druid.pool.DruidDataSource">
              <driverClassName>oracle.jdbc.driver.OracleDriver</driverClassName>
              <url>jdbc:oracle:thin:@10.148.202.1:1521:MESDEVDB</url>
              <username>study</username>
              <password>study</password>
              <sqlDialect class="ch.qos.logback.core.db.dialect.OracleDialect" />
        </dataSource>
   </connectionSource>
</appender> 
```

这个是c3p0连接池的配置

```xml
<appender name="db-classic-oracle" class="ch.qos.logback.classic.db.DBAppender">  
        <connectionSource class="ch.qos.logback.core.db.DriverManagerConnectionSource">  
            <dataSource class="com.mchange.v2.c3p0.ComboPooledDataSource">                 
                <driverClass>oracle.jdbc.driver.OracleDriver</driverClass>  
                <url>jdbc:oracle:thin:@10.148.202.1:1521:MESDEVDB</url>  
                <user>study</user>  
                <password>study</password>
                <sqlDialect class="ch.qos.logback.core.db.dialect.OracleDialect" />   
            </dataSource>  
        </connectionSource> 
    </appender> 
```



```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/test")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = { "/query"})
    @ResponseBody
    public void query() {
        logger.info("操作用户","操作类型","操作功能");
        logger.trace("logback的--trace日志--输出了","操作用户","操作类型","操作功能");
        logger.debug("logback的--debug日志--输出了","操作用户","操作类型","操作功能");
        logger.info("logback的--info日志--输出了","操作用户","操作类型","操作功能");
        logger.warn("logback的--warn日志--输出了","操作用户","操作类型","操作功能");
        logger.error("logback的--error日志--输出了","操作用户","操作类型","操作功能");
        System.out.println("asdfoiwef");

    }
}
```

**终于翻到源码了,為什麼不需要寫sql語句**

```java
//程序启动，调用start方法
@Override
    public void start() {
        if (dbNameResolver == null)
            dbNameResolver = new DefaultDBNameResolver();
        insertExceptionSQL = SQLBuilder.buildInsertExceptionSQL(dbNameResolver);
        insertPropertiesSQL = SQLBuilder.buildInsertPropertiesSQL(dbNameResolver);
        insertSQL = SQLBuilder.buildInsertSQL(dbNameResolver);
        super.start();
    }
//然后再找
  insertSQL = SQLBuilder.buildInsertSQL(dbNameResolver);
//最后sql语句
 static String buildInsertSQL(DBNameResolver dbNameResolver) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(dbNameResolver.getTableName(TableName.LOGGING_EVENT)).append(" (");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.TIMESTMP)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.FORMATTED_MESSAGE)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.LOGGER_NAME)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.LEVEL_STRING)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.THREAD_NAME)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.REFERENCE_FLAG)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG0)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG1)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG2)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG3)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_FILENAME)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_CLASS)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_METHOD)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_LINE)).append(") ");
        sqlBuilder.append("VALUES (?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        return sqlBuilder.toString();
    }
}

```

##### 9.自動清理日誌

```xml
就是配置<maxhistory>5<maxhistory>
1.如果首次项目启动时，超出maxHistory定义的时间的64天之前的日志是不会被清理的
2.如果当天日志的编号超出3位数后缀，也将不会被清理
```



#### 四、搭建項目需要的資源

#####   1. POM文件

```xml
<dependencies>
        <!-- spring -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.6.8</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <!--spring包-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <!--用于SpringMVC-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <!--用于数据库源相关操作-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <!--ServletAPI-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>2.5</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.0</version>
            <scope>provided</scope>
        </dependency>

        <!--jstl标签-->
        <dependency>
            <groupId>jstl</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <!--Oracle数据库驱动-->
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ojdbc14</artifactId>
            <version>10.2.0.4.0</version>
        </dependency>

        <!--测试框架-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>compile</scope>
        </dependency>
        <!-- log start -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.6.6</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.6.6</version>
        </dependency>
        <!-- log end -->

        <!--mybatis-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.5</version>
        </dependency>

        <!--MyBatis集成Spring-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.3.0</version>
        </dependency>

        <!--数据源-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.9</version>
        </dependency>
        <!--分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.1.2</version>
        </dependency>
    </dependencies>

    <build>
        <!--插件-->
        <plugins>
            <!--tomcat插件-->
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <version>2.2</version>
                <!--插件使用的相关配置-->
                <configuration>
                    <!--端口号-->
                    <port>8081</port>
                    <!--写当前项目的名字(虚拟路径),如果写/，那么每次访问项目就不需要加项目名字了-->
                    <path>/</path>
                    <!--解决get请求乱码-->
                    <uriEncoding>UTF-8</uriEncoding>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

##### 2.以前的配置文件

```properties
1.日誌文件
# Set root category priority to INFO and its only appender to CONSOLE.
#log4j.rootCategory=INFO, CONSOLE            debug   info   warn error fatal
log4j.rootCategory=debug, CONSOLE, LOGFILE                               ---也可以一起寫，級別和輸出目的地，控制台和日誌文件

# Set the enterprise logger category to FATAL and its only appender to CONSOLE.
log4j.logger.org.apache.axis.enterprise=FATAL, CONSOLE

# CONSOLE is set to be a ConsoleAppender using a PatternLayout.
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{ISO8601} %-6r [%15.15t] %-5p %30.30c %x - %m\n

# LOGFILE is set to be a File appender using a PatternLayout.
log4j.appender.LOGFILE=org.apache.log4j.FileAppender
log4j.appender.LOGFILE.File=c:\ssm.log
log4j.appender.LOGFILE.Append=true
log4j.appender.LOGFILE.layout=org.apache.log4j.PatternLayout
```

```
appender：附加器，附屬物
fatal：致命的
layout：佈局，設計
```





