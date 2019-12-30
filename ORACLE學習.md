### ORACLE學習

```plsql
打开oracle数据库的输出开关
set serveroutput on
```

#### 1.oracle的基本数据类型

```
char，varchar2,date,number,boolean,long
例：
    abc varchar2(20);
```

#### 2.pl/sql语法

```plsql
declare
     --此处声明变量，如果没有，可以不写declare
begin
     --dml语句
exception   
     --例外处理语句
end;
/
```

#### 3.变量

```plsql
1.引用型变量
   abc emp.salary%type
2.记录型变量
   abd emp%rowtype
=========================
例：
set serveroutput on
declare
   emp_rec emp%rowtype
begin
   select * into emp_rec from emp where empno = 7839;
   dbms_output.put_line(emp_rec.ename||'的薪资是'||emp_rec.sal);
end;
/ 
```

#### 4.if语句

```plsql
if(条件)then 语句1；
语句2；
end if；
=========================
例：判断用户从键盘输入的数字
set serveroutput on
--接收一个键盘数
accept num prompt ‘请输入一个数字’;      --此处给的是地址
declare
  pnum number :=&num                    --我要地址里面的值
begin
  if pnum=0 then dbms_output.put_line('您输入的是0')；
   elsif pnum = 1 then dbms_output.put_line('您输入的是1');
   elsif pnum = 2 then dbms_output.put_line('您输入的是2');
   elsif pnum = 3 then dbms_output.put_line('您输入的是3');
   elsif pnum = 4 then dbms_output.put_line('您输入的是4');
   else dbms_output.put_line('您输入的是其他数字')
  end if;
end;
/
 
```

```plsql
if(条件)then 语句1；
else 语句2；
end if；
```

```plsql
if(条件)then 语句；
elsif 语句 then  语句；
else 语句；
end if；
```

#### 5.例外（异常exceptin）

##### 1.系统中的例外

```
no_data_found   没有找到数据
too_many_rows   select...into语句匹配多个行
zero_divide     被零除
value_error     算术或转换错误
timeout_on_resource  在等待资源时发生超时
```

#### 6.分组查询

```
分组函数：avg，sum，min，max，count，wm_coucat(行转列)
```

#####  1.去重：distinct

**distinct必须放在所有字段的最前面**

| id   | name |
| ---- | ---- |
| 1    | a    |
| 2    | d    |
| 3    | c    |
| 1    | a    |



```
select distinct name from emp
select distinct id，name from emp
```

##### 2.count

```plsql
select count（distinct name） from emp        可以先去重，在统计
select count（distinct name， id） from emp   --这是错误的语法，不可以统计多个
```

**是否会忽略null值**

```plsql
select count(*),count(1),count(id) from enp
==============
count(字段)          -- 有null值，不统计
count（*）           -- 有null值，也统计
=====================
count（*）== count（nvl(id,0))      --空值的话就变成0，0就不是空值  
```

##### 3.行转列

|      | qlrid | qlr  | qlrzjh |
| ---- | ----- | ---- | ------ |
| 1    | 1     | 张三 | 123    |
| 2    | 1     | 李四 | 456    |
| 3    | 3     | 王五 | 789    |

执行语句

```
select qlrid，to_char(wm_concat(qlr)) qlr,to_char(wm_concat(qkrzjh)) qlrzjh from qlr group by qlrid
```

最终结果

|      | qlrid | qlr        | qlrzjh   |
| ---- | ----- | ---------- | -------- |
| 1    | 1     | 张三，李四 | 123，456 |
| 2    | 2     | 王五       | 789      |

##### 4.group by

```plsql
语法：select a,b,c，组函数(x) from emp group by a,b,c
例：select a,b,c，avg(sal) from emp group by a,b,c
==================注意================
--在select 列表中所有未包含的组函数中的列都应该在group by 子句中
--包含在group by 子句中的列不必包含在select列表中
```

group by 后面接的是having过滤

```plsql
select deptno ，avg(sal) from emp group by deptno having avg(sal) > 1500
--having后面可以使用聚合函数 
```

##### 5.group by语句的增强

```
语法：
group by rollup(a,b)等价于
group by a，b
+
group by a
+
group by null
```

举例：

```plsql
select deptno,job,sum(sal) from emp group by deptno,job
select deptno,sum(sal) from emp group by deptno
select sum(sal) from emp 
====上面三条语句等于下面一条语句==========
select deptno，job，sum(sal） from emp group by rollup(deptno,job);
```

此增强的功能主要用在做报表上，sql语句

```plsql
break on deptno skip 2  --相同的部门号只显示一次，不同的部门号之间跳过两行
```

##### 6.执行顺序

```
select column from table where 条件 group by deptno having xxx order by id asc
```

#### 7.多表查询

##### 1.无脑的笛卡尔集

```plsql
最后组合成的表就是两张表的列数相加，行数相乘
--笛卡尔集里面的数据很多都是错误的，因为他是乱关联的
```

##### 2.内连接

```plsql
只显示两个表相关联的数据
语法：
SELECT * FROM t_user1,t_user2 WHERE t_user1.id = t_user2.id
```

##### 3.外连接

```plsql
主表全部显示，从表只显示与主表相关联的信息
left join   on                        --join左侧为主表
right join  on                        --join右侧为主表
full  join  on                        --全部显示，没有的字段为空
```

##### 4.自连接

```
分别给一张表命两个名字，寻找相关联的字段
```

#### 8.子查询

##### 1.注意事项

```plsql
子查询可以使用的位置：where,select,having,from
不可以使用的位置：group by                        */一定要注意
1.当子查询在selec后面的时候，必须是单行子查询(--即只返回一条记录，就叫做单行子查询0)
    select empno,ename,sal,(select job from emp where empno=7839) from emp
2.在having后面
    select deptno,avt(sal) from emp group by deptno 
    having avg(sal) > (select max(sal) from emp where deptno = 30)
```

##### 2.伪劣rownum

**只能使用<,<=;不能使用>，>=**

```plsql
--行号永远是按默认的顺序排列
--行号只能使用<,<=;不能使用>，>=
如果我要取一个表中的工资最高的三个人降序排列
select rownum,empno,ename,sal from 
(select * from emp order by sal desc)
where rownum <= 3
```

##### 3.执行顺序

```plsql
一般是执行子查询，再执行主查询，但是相关子查询除外
```

| 类编号 | 图书名      | 出版社         | 价格  |
| ------ | ----------- | -------------- | ----- |
| 2      | C#高级应用  | 圣通出版       | 23.0  |
| 2      | jsp开发应用 | 机械出版       | 45.00 |
| 3      | 高等数学    | 济南出版社     | 25.0  |
| 3      | 疯狂英语    | 清华大学出版社 | 32.0  |

测试代码

```plsql
1.返回单值
问题：查询所有价格高于平均价格的图书名，作者，出版社和价格
select 图书名，author，出版社，price
from books
where price > (select avg(price) from books )
2.返回值列表
问题：查询所有借阅图书的读者信息
select * from readers
where 读者编号 in 
(select 读者编号 from [borrow history] )
```

##### 4.单行子查询和多行子查询

```plsql
单行操作符：=     相等
           >     大于
           >=    大于等于
           <     小于
           <=    小于等于
           <>    不等于
--例1：查询员工信息，职位和7566一样，薪水大于7782的员工薪水。
select * from emp
where job = (select job from emp where empno = 7566) and sal > (select sal from emp where empno =7782)
--例2：查询最低粽子大于20号部门最低工资的部门号何部门最低工资
select deptno,min(sal) minsal from dept group by deptno having
minsal > (select min(sal) from dept where deptno =20)
```



```plsql
多行操作符：in   表示列表中的任何一个 
           any  和子查询返回的任意一个值比较
           all  和子查询返回的所有制比较
例1：查询工资比30号部门任意一个员工工资高的员工信息
select * from emp where sal > any（select sal from emp where deptno = 30）
```



#### 9.循环

 while循环

```plsql
while total<= 25000 
loop
  --循环体
  total := total+salary;
end loop;
====================
例：
set serveroutput on
declare
  pnum number:=1;
begin
  while pnum < 10 loop
  
  dbms_output.put_line(pnum);
  pnum := pnum + 1;
  end loop;
end;
/
```

loop循环 ,

```plsql
loop
exit [when 条件]
。。。。。。
end loop；
=================
例：
set serveroutput on
declare
  pnum numhber:=1;
begin
  loop
   exit when pnum >10;     --退出条件
   dbms_output.out_line(pnum)
   pnum :=pnum+1;
  end loop;
end；
/
```

for循环

```plsql
for i in 1..6 loop
 --循环语句
end loop;
====================
例：
set serveroutput on
decalre
  i number:=1;
begin
  for i in 1..10 loop
    dbms_output.put_line(i)
  end loop;
end;
/
```

#### 10.游标

在plsql中使用光标来代表一个集合

```
游标的属性
1.%found     变量最后从游标中获取记录的时候，在结果集中找到了记录
2.%notfound  变量最后从游标中获取记录的时候，在结果集中没有找到记录
3.%rowcount  影响的行数，当cursor中有100条，先在读取10条，那么%rowcount就是10
4.%isopen    判断光标是否打开

```

##### 1.语法:**cursor**

```plsql
cursor 光标name [(参数名  数据类型 [参数名，数据类型])]
is select 语句；

cursor c1 is select ename from emp   --创建游标

open c1   colse c1       --打开和关闭游标
fetch c1 into pename     --取游标中的每一条
```

##### 2.demo

1.查询并打印员工的姓名和薪水

```plsql
set serveroutput on
declare
  cursor  cemp is select ename,salary from emp
  pename  emp.ename%type 
  psal    emp.salary%type
begin;
  open cemp;
   loop
     fetch cemp into pename,psal;
     --此时考虑什么时候退出循环，fetch不一定能取到值
     exit when cemp%notfound;
     dbms_output.out_line(pename||'的薪水是'||psal)
   end loop;
  colse cemp;
end；
/
```

2.给员工涨工资

```plsql
set serveroutput on
declare
  cursor cemp is select empno,emojob from emp
  pempno emp.empno%type
  pempjob emp.empjob%type
begin
  open cemp;
   loop
    fetch cemp into pempno,pempjob
    exit when cemp%notfound
    if pempjob = 'PRESIDENT' then update emp set sal = sal +1000 where empno = pempno; 
     elsif pemp = 'MANAGER' then update emp set sal = sal +800 where empno = pempno; 
     else update emp set sal = sal +400 where empno = pempno
    end if;
   end loop;
  close cemp;
  dbms_output.put_line('涨工资完成');
end;
/
==========================
上面的代码执行后没有效果，考虑oracle事务的特性
--修改数据要提交
 end cemp；
 commit；
 dbms_output.put_line('涨工资完成');
end;
/
```

3.测试另外两个属性

```plsql
set serveroutput on
declare 
  cursor pemp is select empmo,salary from emp
begin
  open cemp;
  
  if cemp%isopen then 
    dbms_output.put_line('光标已经打开')；
  else
    dbms_output.put_line('光标没有开启')；
  end if;  
  close cemp;
end;
/
```



```plsql
set serveroutput on
declare
  cursor cemp is select empno,salary from emp
  pempno emp.empno%type
  psal emp.salary%type
begin
  open cemp;
   loop
    --取出游标的值
    fetch cemp into pempno,psal;
    exit when cemp%notfound;
    dbms_output.put_line(cemp%rowcount)
   end loop;
  close cemp;  
end;
/
```

##### 3.游标的限制

```
1.同一个会话只能打开300个游标
  修改的话：alter system set open_cursors = 400 scope=both；
  
  其中scope的取值为：
　　memory：仅修改内存，只会影响当前的使用，重启数据库就会失效。
　　spfile：仅修改配置文件，不会影响本次使用，重启数据库才生效。
　　both：两个都修改。
2.查看Oracle游标最大打开数
　show parameter cursor;　
```

##### 4.带参数的光标

```plsql
例:某部门员工的工资
set serveroutput on
declare
  cursor cemp(dno number) is select salary from emp
  psal emp.salary%type
begin
  open cemp(10);
   loop
    fetch cemp into psal;
    exit when cemp%notfound;
    dbms_output.put_line(psal);
   end loop;
  close cemp;
end;
/
```

#### 11.变量的声明和赋值

#####    1.声明变量的三种方式

```plsql
方式一：直接声明式
  v_start varchar2(200);
  v_num number;
方式二：使用%type声明
  v_orgsen emp.sal%type                             --该变量的数据类型与指定表的指定字段的数据类型一致
方式三：使用%rowtype声明，记录型变量
  v_row_virtual vietual%rowtype                     --该变量的数据类型与指定表的指定行记录的数据类型一致 
  分量的引用：
  v_row_virtual.ename := 'admins';
===========================
--在存储过程中，声明变量时，不需要使用关键字“declare”
```

#####    2.赋值变量的三种方式

```plsql
方式一：直接赋值 ,使用 :=
  v-orasen := '110';
方式二：select column into 变量 from table
  select sal into psal from emp where empNo = 10001;
方式三：execute immediate sql语句 into 变量  
 declare 
    v_sql varchar2(20);
    v_orgsen varchar2(40);
 begin
    v_sql := 'select orgsen from base_orgsen where orgcode = to_char(41053346535)';
    --赋值
    execute immediate v_sql into v_orgsen
    --打印结果
    dbms_output.put_line(v_orgsen)
 end;
 /
```



#### 12.时间函数

#####    1.24小时的形式显示出来，要用HH24

```
select to_char(sysdate,'yyyy-MM-dd,HH14:mi:ss') to dual
select to_date('2005-11-5 13:15:52','yyyy-MM-dd GG24:mi:ss') from dual
```

#####    2.日期/字符串参数说明

​     to_date( )

```
DD月中的第几天 
DDD年中的第几天 
DAY天,缩写是DY
=================
当前时间减去7分钟的时间
select sysdate，sysdate - interval'7' MINUTE  from dual
当前时间减去7天
select sysdate，sysdate - interval'7' day frmo dual
```

​     to_char( )

```sql
1.处理数字
select to_char(88877) from dual;                                              ==88877
select to_char(1234567890,'099999999999999')  from dual;                      ==000001234567890
select to_char(12345678,'999,999,999,999')  from dual;                        ==12,345,678
select to_char(123456,'99.999')  from dual;                                   ==
select to_char(1234567890,'999,999,999,999.9999')  from dual;                 ==1,234,567,890.0000
2.工资
select TO_CHAR(123,'$99,999.9') from dual;                                    ==$123.0
```



#####    3.日期到字符

```
select sysdate，to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') from dual
```

#####     4.字符到日期

```
select to_date('2003-10-7 21:15:42','yyyy-MM-dd hh:mi:ss' ) from dual
```



#### 13.复制

```sql
1.复制表结构及其数据
  create table table_name as select * from table_name_old
2.只复制表结构
  create table table_name as select * from table_name_old where 1=2;
  或者 create table table_new like table_name_old
3.只复制表数据
  如果两个表的数据一样
  insert into table_name select * from table_name
  如果两个表的数据不一样
  insert into table_name_new(column1,column2....) select column1,column2...from table_name_old
```



#### 14.序列

#####    1.創建序列

```
create sequence seq_zhaonan
increment by 1
start with 1
maxvalue 99999
```

#####   2.查詢序列

```
select seq_zhaonan.nextval from dual
```

#####   3.刪除序列

```
drop sequence seq_zhaonan
```

#### 15.外鍵刪除

#####    1.建表的時候創建了外鍵，沒有指定名字，系統自動命名

```sql
SELECT
  USER_CONS_COLUMNS.CONSTRAINT_NAME AS 约束名,
  USER_CONS_COLUMNS.TABLE_NAME AS 子表名,
  USER_CONS_COLUMNS.COLUMN_NAME AS 子表列名,
  USER_CONS_COLUMNS.POSITION AS 位置,
  USER_INDEXES.TABLE_NAME AS 主表名,
  USER_IND_COLUMNS.COLUMN_NAME AS 主表列名
FROM
  USER_CONSTRAINTS
    JOIN USER_CONS_COLUMNS
    ON (USER_CONSTRAINTS.CONSTRAINT_NAME
        = USER_CONS_COLUMNS.CONSTRAINT_NAME)
    JOIN USER_INDEXES
    ON (USER_CONSTRAINTS.R_CONSTRAINT_NAME
        = USER_INDEXES.INDEX_NAME)
    JOIN USER_IND_COLUMNS
    ON (USER_INDEXES.INDEX_NAME = USER_IND_COLUMNS.INDEX_NAME)
WHERE
  CONSTRAINT_TYPE = 'R'; 
```

##### 2.刪除

```
ALTER TABLE   表名   DROP CONSTRAINT   外键约束名字;
```

  3.如果知道外鍵名字，直接刪除

```
同上。
```

#### 16.觸發器

#####    1.分类

```
一条sql语句，功能是insert3行，那么久就触发语句级触发器一次(实际只操作一次表)，行级触发器3次
 1.行级触发器：针对的是行      ==for each row，后面可以接条件，when
                             通过:old 和 :new来识别值的状态
 2.语句级触发器：针对的是表
```

#####    2.創建觸發器

```
CREATE TRIGGER logging_event_id_seq_trig
  BEFORE INSERT ON logging_event
  FOR EACH ROW  
  BEGIN  
    SELECT logging_event_id_seq.NEXTVAL 
    INTO   :NEW.event_id 
    FROM   DUAL;  
  END;
/
```

#####     3.刪除觸發器

```
DROP TRIGGER logging_event_id_seq_trig
```

#####     4.应用场景

  1.禁止非工作时间插入新员工（语句级，）

```sql
首先写出非工作时间：to_cahr(sysdate,"DAY") in('星期六','星期日')
    to_number(to_char(sysdate,'hh24')not between 9 and 18)
2.执行语句        注意：自定义报警代码在（-20000~-29999)
create  or replace trigger securityEmp
before insert
on emp
declare
begin
   if to_char(sysdae,'DAY') in ('星期六','星期日') or
    to_number(to_char(sysdate,'hh24'))not between 9 and 18 then
    raise_application_error(-20001,'禁止非工作时间操作数据库')
end;
/
```

  2.涨工资不能越长越少（行级）

```sql
:old  表示操作之前,这一行的值
:new  表示操作之后,这一行的值
create OR replace trigger checkSalary
brfore update
on emp
for each row
begin
 if :new.sal < :old.sal then
  raise_application_error(-20002,'涨后的工资小于涨前的薪资')
end;
/

```

   3.实现数据库的备份(同步备份)

```sql
员工涨工资后，自动备份到新的工资备份表中
create or replace trigger sync_salary
after update
on emp
for each row
begin
   update emp_back set sal=:new.sal where empNo = :new.empNo
end;
/
```

#### 17.存储过程

```sql&#39;
存储在数据库中供所有用户程序调用的子程序叫存储过程，存储函数
存储函数可以通过一个return子句返回函数得值
存储过程不能通过return子句返回函数的值
```

#####  1.创建和使用

```sql
第一个存储过程，打印hello word
create or replace procedure sayhelloworld
as
   -说明部分
begin
  dbms_output.put_line('HelloWorld')
end；
/
```

   as的使用

```
引用行变量
```

##### 2.调用存储过程

```
如果想屏幕上显示，就要先执行set serveroutput on
与dbms_output.put_line对应使用
============================================
1.execute sayhelloworld();            ==缩写exec sayhelloworld();
2.使用pl/sql语法
begin
   sayhelloworld();
   sayheoolworkd();
end;
/
```

##### 3.带参数的存储过程

一般不会在存储过程中使用commit，都是调用的使用，一起commit

```plsql
例：为指定的员工，张100块钱的工资，并且打印涨前和涨后的薪水
create or  replace procedure raisesalary(eno number)
as
  psal emp.sal%type
begin
  --得到员工涨前的薪水
  select sal into psal from emp where empno=eno;
  --涨工资
  update emp set sal=sal+100 where empno=emo;
  --打印
  dbms_output.put_line('涨前工资:'||psal||'涨后工资：'||(psal+100))；
end;
/
==========调用=============
begin
   raisesalary(7856)
   raisesalary(8745)
   commit;
end;
/
```

##### 4.存储函数

**注意**：参数有一个类型，是输入还是输出in/out

```plsql
例：某个员工的年收入
create or replace function queryempincome(eno in number)
return number
as
   psal emp.sal%type
   pcomm emp.comm%type
begin
  --先查出当前的薪资和奖金
  select sal into psa,comm into pcomm from emp where empno = eno;
  --直接返回
  return psal*12 + pcomm;       --此处有问题，当我奖金为0时，我的年薪是0，怎么办
end;
/
===============坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑=====
  我们的return语句中如果有空，那么返回的值就是空
  所以要遇空判断
  return psal*12+nvl(pcomm,0)   --如果为空就是0
```

**如果只用一个返回值，就用存储函数，如果有多个返回值，就用存储过程**

```plsql
create or replace procedure queryempinfo(eno in number,
                                        pename out varchar2,
                                        psal out number,
                                        pjob out varchar2)
as
begin
   --查询
   select name,sal,job into pename,psal,pjob from emp where empno = emo;
end;
/
```

##### 5.实战

```java
//回顾jdbc连接，创建工具类
public calss JDBCUtils{
    private static String driver = "oracle.jdbc.OracleDriver";
    private static String url ="jdbc:oracle:thin:@192.168.56.101:1521:oroacle";
    private static String username= "study" ;
    private static String password= "study";
    
    static{
        Class.forName(driver);
        然后 try...catch
    }
    
    public static Connection getConnection(){
        return DriverManager.getConnection(url.username,password);
        也是要 try...catch
        return null;   
    }
    
    public static  void release(Connection con,Statement st,RestltSet rs){
        if(rs != null){
            rs.colse();       try..catch
        }
        if(st != null){
            st.close();       try..catch
        }
        if(con != null){
            com.colse();      try..catch
        }
    }
}

```

**java调用存储过程和存储函数**

```properties
存储过程：{call <procedure-name>[(<arg1>,<arg2>, ...)]}
函数：{?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
```

**测试：调用存储过程**

```java
public class test｛
    //调用存储过程
    String sql = "{?=call queryempinfo(?)}";

    Connection conn = null;
    CallabelStatament call = null;
   
    try{
        //获取连接
        conn = JDBCUtils.getConnection();
        //创建平台
        call = conn.prepareCall(sql);
        //对输入参数赋值
        call.setInt(1,7879);
        //对输出参数赋值
        call.registerOutPpameter(2,OracleTyple.VARCHAR);
        call.registerOutPpameter(2,OracleTyple.NUMBER);
        call.registerOutPpameter(2,OracleTyple.VARCHAR);
        
        //执行调用
        call.execute();
        
        //也可以取出来看看
        String name = call.getString（2）
     }catch(Exception e){
        e.printStackTrace();
    }finally{
        JDBCUtils.release(conn,call,null);
    }

｝
```

**测试：调用存储函数**

```java
public class test｛
    //调用存储过程
    String sql = "{?=call queryempincome(?)}";

    Connection conn = null;
    CallabelStatament call = null;
   
    try{
        //获取连接
        conn = JDBCUtils.getConnection();
        //创建平台
        call = conn.prepareCall(sql);
        
        //对输出参数赋值   
        call.registerOutPpameter(1,OracleTyples.NUMBER);
        //对输入参数赋值
        call.setInt(2,7839);
        
        //执行调用
        call.execute();
        //结果可以取出来看看
        double income = call.getDouble(1);
        system,out.printLn("年薪是"+income)；
     }catch(Exception e){
        e.printStackTrace();
    }finally{
        JDBCUtils.release(conn,call,null);
    }

｝
```

#### 18.oracle函数

##### 1.数值函数

```plsql
ROUND：四舍五入 
例： select round(23.3),round(23.45,1),round(23.45,-1) from dual
     --    23                    23.5          20
CEIL：最大值
FLOOR:最小值
例：select ceil(23.45),floor(23.45) from dual
    --    24                   23
ABS:绝对值
MOD(m，n)：取余              --如果m和n有一个null值，那么结果就是null     m % n 取余
POWER(m,n):m的n次幂
SQRT(n）:求平方根
```

##### 2.字符函数

```plsql
upper：大写
lower: 小写
initcap：首字母大写        --用户注册
substr：截取 
例： select substr('abcde',2,3),substr('abcde',2),substr('abcde',-2,1) from dual;
  --   从第二个开始取，取三个：bcd   从第二个开始取： bcde     从后面数第二个，只取一个：d
concat:字符串拼接
||：也是字符串拼接

```

##### 3.转换函数

```
to_cahr:日期转化字符串   to_char(sysdate,'yyyy-MMM-dd hh24:mi:ss')
to_date:字符串转日期     to_date('20152-5-21','yyyy-MM-dd')

```

##### 4.日期函数

```plsql
ADD_MONTHS(date，i）    --表示月份加减
例：select add_months(sysdate,3) from dual;
NEXT_DAY(date,char)     --可以查下一个日期是几号
例：select next_day(sysdate,'星期一') from dual.               --下一个星期一是几号
last_day(date)
months_between(date1,data2)         --两个日期之间间隔的月份
select months_between('20-5-15','10-1-15') from dual           
 =============
extract(date from datetime)
例：select extract(year from sysdate） from dual
```

#### 19.案例实战

##### 1.统计每年入职的员工人数

| total | 1980 | 1981 | 1982 | 1987 |
| ----- | ---- | ---- | ---- | ---- |
| 14    | 1    | 10   | 1    | 2    |

分析过程

```plsql
首先，我们需要查询出员工入职的时间，这是一个集合  select to_char(hiredate,'yyyy') from emp
==》cursor  ==》循环  ==》退出条件
然后定义几个计数器，conut80 number := 0;
===========================
set serveroutput on
declare
  cursor cemp is select to_cahr(hiredate,'yyyy') from emp;
  phiredate varchar2(10);
  couont80 number:=0;
  couont81 number:=0;
  couont82 number:=0;
  couont87 number:=0;
begin
  open cemp
    loop
      fetch cemp into phiredate;
      exit when cemp%notfound;
      
      if hiredate = '1980' then
       count80 := count80 + 1；
      elsif hiredate = '1981'  then
       count81 := count81 + 1；      
      elsif hiredate = '1982'  then
       count82 := count81 + 1;
      else
       count87:= count81 + 1；
      end if;
    end loop;
  colse cemp;
  
   dbms_output.put_line('80年加入的员工人数'||count80);
   dbms_output.put_line('81年加入的员工人数'||count81);
   dbms_output.put_line('82年加入的员工人数'||count82); 
   dbms_output.put_line('87年加入的员工人数'||count87);  
end;
/
```

##### 2.计算涨工资

```
为员工涨工资，从最低工资涨起每人长10%，但是工资总额不能超过五万，请计算涨工资的人数和涨工资后的工资总额，并输出涨工资人数及工资总额？？？
分析：我们从工资最低的涨起 select empno from emp order by salary asc 
  ==》光标   ==》循环  ==》退出    其中某个员工已经超过5W
   还有一种情况 全都涨完了还没有超过5万的  %notfound
   涨工资人数 countEmp number :=0
   涨后的工资：salTatal number
   1.select sum（sal） into salTotal from emp
   2.涨后的工资 = 涨前的工资 + sal*0.1
```

代码

```plsql
set serveroutput on
declare
  cursor cemp is select empno,sal from emp order bu sal asc;
  pempno emp.empno%type
  psal emp.sal%tyype
  countEmp number:=0;                         --涨工资人数
  salTotal number;                         --总工资
begin
  select sum(sal) into salTotal from emp
  open cemp;
    loop
      --首先判断员工工资是否过了5W
      exit when salTotal > 50000;
      fetch cemp into pempno,psal;
      --在判断没有值了就退出循环
      exit when cemp%notfound
      
      --还要判断涨后会不会超过5W
      if salTotal + psal*.01  < 50000 then
         --涨工资
         update emp set sal = sal*1.1 where empno = pempno;
         countEmp := countEmp + 1;
         --涨后的总工资
         saltal := salTotal + psal*0.1;
         else exit;
      end if;
    end loop;
  close cemp;
end;
/
--有问题，第八个人的总工资超过了5W，所以还要加一个判断，判断涨后会不会超过5W。
```

##### 3.部门分段，统计工资

```
实现功能：按部门分段（6000以上，（6000，3000），3000以下）统计各工资段的职工人数，以及各部门的工资总额（工资总额中不包含奖金）
分析：我要知道有多少部门 select deptno from dept  ==>光标  ==》循环  ==》退出条件 notfound
   还要知道各部门的薪水 select sal from emp where deptno = ？ ==》光标   ==》循环  ==》退出条件
   然后就是统计人数，定义三个计数器
   count1 number；
```

代码

```plsql
set serveroutput on
declare
  cursor cdept is select deptno from dept
  pdeptno dept.deptnp%type;
  
  cursor cenp(dno number) is select sal from emp ehere deptno = dno
  psal emp.sal%type;
 
  count1 number;
  count2 number;
  count3 number;
  --工资zonge
  saltotal number;
begin
  open cdept
  loop
  --循环部门，
  fetch cdept into pdeptno 
  exit when cdept%notfound
  
  --初始化
  conut1:=0; conut2:=0; conut3:=0;
  --得到部门工资总额
  select sum(sal) into saltotal from emp where deptno = pdeptno;
     open cemp(pdeptno)
     loop
       fetch cemp into psal 
       exit when cemp%notfound
       if psal>6000  then
        count1 := count1+1;
       elsif psal>3000&& psal<6000 then
         count2 := coun2t+1;
       else
          count3 := count3+1;
       end if;   
     end loop;
     close cemp;
     --保存当前部门的结果
     insert into msg values（deptno，couont1,count2,count3,nvl(saltotal));    --工资中可能有0，遇空判断
   end loop;  
  close cdept;
  commit；
  --此处可以打印一个提示，告诉用户测试完成
end;
/
======================建表===========
create table msg（
 deptno number，
 count1 number，
 count2 number，
 count3 number，
 saltotal number
）
```

##### 4.分页

```plsql
分页查询显示员工的信息：显示员工号，姓名，月薪
          每页显示四条
          显示第二页的员工
          按照月薪降序排列
 ====一般选择第一种，因为效率高=========         
 select * from
 (select rownum ，empno，pname，sal from
 (select * from emp order by sal desc) t
 where table.rownum < 8) t2
 where rownum > 5 
 =====第二种写法========
 select * from 
 (select rownum ,empno,ename ,sal from
 (select * from emp  order by sal desc）e)e2
  where rownum > 5 and rownum < 8
```

##### 5.执行计划

```
来分辨sql的复杂度，占用CPU大小
plsql直接按F5
```

##### 6.decode的使用

```plsql
第一种情况：
decode(条件,值1,返回值1,值2,返回值2,...值n,返回值n,缺省值)

该函数的含义如下：
IF 条件=值1 THEN
　　　　RETURN(翻译值1)
ELSIF 条件=值2 THEN
　　　　RETURN(翻译值2)
　　　　......
ELSIF 条件=值n THEN
　　　　RETURN(翻译值n)
ELSE
　　　　RETURN(缺省值)
END IF

例：如果是1981年入职，就是1，其他就是0
decode(hiredate,'1981',1,0)
```



#### 英语练习

```
explore：探索，研究，探讨
invitation：邀请
below：在下面
dialog：对话
dashboard：仪表盘
manufactury 制造业
standard 标准
defect 瑕疵，毛病
issue 问题，发行，发布
risk  危险，风险，会带来风险的事物，
specific 明确的，具体的，特有的
schedule 工作计划，日程安排
```

