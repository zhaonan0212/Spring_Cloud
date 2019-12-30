## Table Mapping



#### ISA95分层

| 等级   | ISA95设计到的内容                                   |                        |
| ------ | --------------------------------------------------- | ---------------------- |
| LEVEL5 | 企业接入云系统的网络,路由和存储接口                 | 企业集成               |
| LEVEL4 | PLM,ERP,CRM,QMS(月,周,天)                           | 生产现场业务规划和物流 |
| LEVEL3 | MOM,MES,CMMS(天,班,小时,分,秒)                      | 生产县城制造运营和控制 |
| LEVEL2 | DCS服务器客户端(SCADA,HMI, OPC)(小时,分,秒,毫秒)    | 工段,车间监控和控制    |
| LEVEL1 | 批量控制,离散控制,驱动控制,连续过程控制(分,秒,毫秒) | 基础控制               |
| LEVEL0 | 传感器,驱动器,执行器,机器人(分,秒,毫秒)             | 生产过程               |

#### part3部分

```
job lsit:特定时间范围内一个或多个工作中心和/或资源的工作单的工作清单集合
job order:由工作主管定义的最小执行工作单位job
job response:关于生产,维护,库存和品质活动或活动中任何组合相关的工单的作业数据
resource relationship network:两种或多种以上资源的一个或多个表达式
work alert:part3中不需要确认的事件通知
work capability:一种特定的资源组合，具有完成工作定义的一个或多个步骤所必需和足够的某些能力
work definition:收集有关与工作相关的资源和工作流定义的信息
work directive:工作定义源自工作主管，用于执行工作单
work kpi:一种商务的可测量的或者可操作的关于工作效能的值
work master:一个工单中被执行的一种工作类型的模板
work performance(效能):工作响应的集合
work master capability:资源执行工作的能力以及这些资源的能力
work request:收集工单
work schedual:工作要求的收集
work response:job response的集合
```

##### Activity model

```
operations definition:主要就是definition management
operations capability:主要就是resource management
operations request:主要就是detailed scheduling  详细的计划时间表
operations response:主要就是tracking 追踪,跟踪   包括数据的收集和信息的分析

通用活动模型定义了一个一般的请求相应周期,这个周期从请求或者时间表开始,将其转化为工作进度表,根据进度表分配任务,管理工作的执行,收集数据,并将收集的数据转换回响应,此请求响应的周期受一下支持:
     分析为改进或纠正所做的工作
     管理所执行工作中使用的资源
```

##### plan和scheduling的区别

```
plan被定义为一种活动,用于阐明为实现给定目标而采取的行动或者操作,并保留足够数量的资源以达到最低目标
scheduling被定义为一种活动,用于特定时间将各种操作和操作分配给特定资源,同事考虑到各种实际约束和几个评估参数的优化
```

```
消耗型和费消耗型
   消耗型资源:工作时间,材料等,        数量可测量,通常在生产前后发生变化
   非消耗型资源:人的技能,设备等       不会因生产而枯竭,根据能力进行调度
   
生产计划取决于可消耗和不可消耗资源的可用性。此标准中的可用容量是指可以达到的一部分生产能力，但并不承诺用于当前或将来的生产，可以定义为费率   
```

```

```



#### part4部分

##### 4.2.3通用资源定义

```
当前章节的对象模型使用的人机料法都是part2部分定义的标准,当与part3的work对象一起使用的时候,这些人,机,料,载具可能会包含一些part3活动所需要的信息和part4部分所共享的信息.
     1.在part3中所需的人员信息可能包括没有与4级人员或培训管理系统共享的详细经验和资格级别
     2.在part3的活动中材料信息的维护可能包含没有被分享到part4材料管理系统的小批量信息
     3.能量的交付,使用和释放可能被视为重要信息(会像材料信息一样被处理)
```

##### 4.2.4level 3 work models

```
本部分定义的对象模型:
    1.resource relationship network-资源关系网络由资源管理和定义管理活动中的任务创建。
	2.work definition
	    a.工作母版-工作母版由第1部分中定义的工程活动创建，并由定义管理活动中的任务进行管理。
	    b.工作指令-工作指令是由任务在执行管理活动中创建的。
	3.work schedule-工作时间表由任务在详细的计划活动中创建。
	4.job list-作业列表是由任务在调度活动中创建的。
	    注意在标准的这一部分中，当引用作业列表中的条目时，有时会使用术语job替代job order。
	5.work performance-工作绩效是由任务在跟踪活动中创建的。
	6.work capability-工作能力是由资源管理活动中的任务创建的。
	7.work master capability-工作主管能力是由资源管理活动中的任务创建的。
	8.work KPI-工作KPI由分析活动中的任务创建。
	9.work alert-工作警报由活动模型中的任何活动创建。The following object models are defined in this Part.
```

##### 4.5 value type

```
值的属性用于属性,参数和数据中以交换实际值
值的属性也可用于交换属性和参数中的允许和期望值
```

##### 5.1对象模型属性

```
在本部分中，已将至少一组与行业无关的信息定义为对象模型的属性。但是，根据模型的实际用法，可能不需要所有属性的值。如果需要其他信息，包括行业和特定于应用程序的信息，则应将其表示为属性对象。该解决方案通过使用标准属性来提高可用性，并通过使用属性来实现灵活性和可扩展性。
```

##### 5.2属性扩展

```
对于特定的应用程序，通过向对象类定义添加属性来扩展在对象模型中定义的对象。因此，该标准提供了根据属性建模并在模型的属性类中表示的应用程序或特定于行业的属性。例如，人员类别属性可以为人员类别定义应用程序或特定于行业的属性，而人员属性可以包含该属性的实例值。
```

##### 6.1资源关系网络 Resource Relationship Network

```
资源关系网络必须用于表明两种或两种资源以上的关系,用来表明详细的排程活动,调度活动,执行活动或者其他part3活动所必需的信息
每个资源关系网络都是一个关系集合
每一个关系都必须表明从一种资源到另一个资源的直接联系
  note1:每个关系表示一个"边缘",资源表示一个顶点
  note2:资源关系的元素属性表示网络的约束,例如流量,方向,集合和顺序的约束
```

```
属性:
    关系类型一般是逻辑类型或物理类型,都在同一区域
定义关系的形式:
    永久:操作过程中不可拆分或者变更
    瞬时:操作过程中可修改
```



##### 6.3资源网络连接 Resource Network Connection

```
在资源关系网络中两个资源之间的定向连接就是一种资源网络连接
一个资源网络连接包含如下:
       1.定义一个资源引用从关系(作为一个有向连接开始的起点或以图表的方式表示该关系的箭头的尾部)
       2.定义一个资源引用到关系(作为一个有向连接的结束点,或以图形的形式表示该关系的箭头的头部)
       3.0或多个资源网络连属性
       4.一个关联的连接的类型
```

##### 6.9 Resource Notework Connection Type 

```
资源网络连接类型可以包括零个或多个资源网络连接类型属性
```

##### 7.1 work definitionn model

```
work master:是工作的定义范本
work directive:(工作指令)才会连接到job order实际执行
```

```
执行特定工作单元所需的资源和工作流程定义的标识应定义为工作定义。工作定义可适用于生产，维护，质量测试和库存活动的定义。下面的图3是通用定义模型，在该标准的第2部分中定义了显示为灰色框的对象。
工作定义被建模为抽象类。有两种类型的工作定义被建模为非抽象类：work master和work指令。
工作主数据是与任何特定IOB订单无关的模板信息。工作指令以工作主管的副本开始，并增加了特定工作订单的信息。
工作定义可以引用操作定义。在这种情况下，工作定义定义了完成全部或部分操作所需的详细步骤。
```

##### 7.2 work master

```
在不参考指定的工单的情况下去执行一个工作单元所需要的资源和路由被定义为工作主管
work master include:
	标识材料类或者材料定义
	确定生产运行规模(标准的工单尺寸)
	确定工作中心和工作单元的设备类
	可以表示工单定义工作所需要的其他信息 =>例如:指令,自动化程序,sop,配方,图纸,cnc程序,包装规则,标签规则,过度规则
```

##### 7.3 work directive

```java
指定的工单去执行一个工作单元所需要的资源和指令,可以定义为一个工作指令
work directive include:
	作为一个工作主副本创建的
	用于控制一个工单或部分工单
	定义确切的批量大小或成产运行大小
	可以识别工单的物料批和物料子批
	可以为工单确定特定的工作中心或者工作单元
	
每个执行的工单都会有一个工作指令,他包含这个工单或者这个工单相关联的工作流程所需要的特定信息	
```

work master 主要是work order dependent information

work directive 主要是work order specifica information

##### 8.1work schedule

```
一个operation request 可以拆分成多个work request去执行

```

这个图解10:

​      一个工作请求由多个工单来组成,

​      多个工作请求中实现了一个操作请求

​      例如:做电脑(operation resquest) => a做显示器,b做主机,c做键盘和鼠标(work request) =>一线做模组(job order),二线做胶框(全部加起来job list)

​                                                                                                                                                               一线做软件(job order),二线做组装

​                                                                               其中job order 参考 work master,master 定义这个工作的工作流程以及人机料                                                           

##### 9.1 work performance

```
颗粒度work 比job大
颗粒度 performance 比 response 大
工作绩效是工作结果的集合,该工作结果是工作请求的报告
```

```
                                                     equipment 
                                                     personnel
work performance => work response => job response => material
                                                     physical
                                                     job response
work performance 是 work response 的集合,work response是对所请求的制造请求的报告,一个work request可以对应多个work response
```

```
一般就是id,work type,description,work schedule,starttime ,endtime,published,hierarchy scope(等级权限,管理员,作业员)
```

##### 9.2 work response

对应的是work request =>里面有很多个job order,

```
与工作请求相关的制造响应定义为工作响应
工作响应还包含请求的状态,完成,结束或者终止
一般就是id,work type,request,start time,end time,hierarchy scope(等级权限)
```

##### 9.3 job response

最小工作单元的反馈

```
与工单相关的制造响应定义为工作响应
工单还包含请求的状态,完成,终止,或者完成百分百
=================
一般就是id,work type,job order, work directive, work directive version,start time,end time, hierarchy scope
```

##### 10.1 work capability

```
选择当前和过去的时间中用于工作的所有资源信息的集合就是工作能力,他包好设备,材料,载具,人和工作中心等,工作能力描述了这个纸草控制系统的名字,术语,状态和数量.
工作能力也被定义为在给定的时间里(过去,现在和将来)人的能力,设备的能力,材料的能力,流程部分的能力的集合,并被定义为承诺的,可用的,和不可实现的
======
一般就是,id,descript,capacity type(used,unused,total,available,unattainable or committed ),reason,confidence factor(百分比),hierarchy scope(等级权限,如管理员,作业员),start time,end time ,published date(发行版日期)
```

##### 12.1 kpi model

```
具有与绩效度量相关的业务或运营价值的价值定义为KPI.
```

##### 13.work alert model

```
第3级事件的通知应定义为工作警报。并非所有事件都需要创建工作警报。工作警报不需要确认。如果需要确认，则应使用警报模型
该第4部分标准定义了在第3级内为工作警报交换的数据。工作警报定义是对可用工作警报类型的描述。
   注1：工作警报的详细配置数据，例如触发条件，接收者的注册以及接收后要采取的措施，不在本第4部分标准的范围之内。
   注2：工作警报与工作KPI的不同之处在于，工作警报的主要内容是传达事件已发生所需的上下文信息。
      示例1这类似于在发生飞机登机口变更时通过电子邮件发送给旅客的“登机口变更警报”。它表示潜在的重大事件，但不需要任何响应或采取任何措施。
      示例2：对工作KPI的计算或检查可能会触发工作警报，但是其他事件也可能会触发工作警报。
      示例3指示生产运行已完成的工作警报可能是开始执行工作流程的触发器。
      示例4工作流程事件（例如，截止期限计时器到期）可能会触发工作警报。

===========
一般包含id,descript,priority(优先事项清单作为工作报警 关联等级的重要指南),category
```

##### 14.object and models

| from resource reference                   | resource relationship network model |
| ----------------------------------------- | ----------------------------------- |
| from resource reference property          | resource relationship network model |
| job list                                  | work schedule model                 |
| job order                                 | work schedule model                 |
| job order parameter                       | work schedule model                 |
| job response                              | work performance model              |
| job response data                         | work performance model              |
| resource network connection               | resource relationship network model |
| resource network connection property      | resource relationship network model |
| resource network connection type          | resource relationship network model |
| resource network connection type property | resource relationship network model |
| rsource relationship network              | resource relationship network model |
| to resource reference                     | resource relationship network model |
| to resource reference property            | resource relationship network model |
| work alert                                | work alert model                    |
| work alert definition                     | work alert model                    |
| work alert definition property            | work alert model                    |
| work alert property                       | work alert model                    |
| work capability                           | worl capability mdoel               |
| work definition                           | work definition model               |
| work directive                            | work definiton model                |
| work master                               | work definition model               |
| work master capability                    | work master capability model        |
| work performance                          | work performance model              |
| work request                              | work shcedule model                 |
| work response                             | work performance model              |
| work schedule                             | work schedule model                 |

##### 15.总结

```
job list 就是 job order list
job order 是工单,工站,工站内的工序
job response 是执行 job order 的结果
work alert:
work capability:resource capability + work master capability
work definition: resource + workflow
```

##### 16,开会汇总

```
1.resource relationship nerwork connection
物料用在哪台设备
物料之间的运输,个数,速度

2.
WORK MASTER理想中的图层

3.work schedule:工作排程
job list:特定的时间,特定的工单的反馈
work request:合并工单
job order:要生产什么样的产品,最小的生产单位
resource requierment:这个工单领的料 ,理论上的领料

4.work performance:全都是实际的结果
work response:几种工单的反馈
job response:一个工序或一个工站的反馈
resource actual:这个工单真实使用的物料,resources

5.
work directive:实际中使用的
```



#### 单词

```
in addition to 除...之外     =>a in addition to b :a+b
be composed of 由...组成
dispatching:调度,配送
tracking:跟踪,追踪
perform:执行,完成,机器运转
scheduling:排程,调度,时间安排,行程安排
reference:参考,参照,设计,提及
          引用,参考文献
directive:指示,指令   
identification:鉴定,识别,认同,身份
make up of:组成
schedule:时间表,进程表
schema:模式,格式,样板
       图表,图解,示意图,线路图,系统图
published:出版的,发行的
generated:产出,产生
obtained:获取,获得
train:培训,陶冶,耕作
      火车,长队,培养
```

```sql
--料號--（發料記錄）--發料工單 --CS SMT板序號--（link）---CS半成品序號---（組裝）----CS序號---（組裝）---SMT板---（link）---半成品---（組裝）---成品---出貨
select distinct aa.starttime,aa.materialno,aa.datecode,aa.mono,a.baselotno,b.tolotno,
c.unitid,L.mono,L.productno,d.unitid,e.tolotno,g.mono,g.productno,f.unitid,h.mono,h.productno,
n.shippingdate,n.customerno
from 
(select A.starttime, C.materialno, C.datecode, D.mono
from tblinvpickupmaterialbasis A
join tblinvpickupmateriallistdetail C on A.combinepickupno = c.combinepickupno
join tbloemocombine D on A.mocombineno = D.mocombineno
join tbloemomateriallist E on d.mono = e.mono and c.materialno = e.materialno
join tbloemobasis F on e.mono = f.mono
where  c.materialno = '254045162Y'
and  a.starttime >= to_date('2015/11/01', 'yyyy/mm/dd')
and a.starttime < to_date('2015/12/01', 'yyyy/mm/dd'))aa --發料記錄
left join  tblwiplotbasis a on a.mono=aa.mono 
left join tblWIPSplitContent b on b.fromlotno=a.baselotno --link  --从邵杰处拿来一个批次的产品就是split,一次12pcs,分成12分
left join tblwipunitlog_assembly c on c.serialno=b.tolotno --組裝 --
left join tblwipunitlog_assembly d on d.serialno=c.unitid and d.unitid <> d.serialno --組裝
left join tblWIPSplitContent e on e.fromlotno=d.unitid  --link
left join tblwipunitlog_assembly f on f.serialno=e.tolotno  --組裝
left join  tblwiplotbasis L on c.unitid=L.baselotno  --加CS序號的工單機種
left join  tblwiplotbasis g on e.tolotno=g.baselotno --加半成品的工單機種
left join  tblwiplotbasis h on f.unitid =h.baselotno  --加成品的工單機種
Left Join Tblwipcont_Packinglistdetail m on  f.unitid = m.Snno
left join tblwippackinglistbasis n on m.packinglistno = n.packinglistno
where  b.fromlotno is not null--CS SMT板link記錄不為空
and f.unitid is not null --成品序號要存在
and h.lotstate=106; 
```



```sql
SELECT FROMlotno,BASELOTNO FROM
(SELECT  BASIS.*,SPLIT.*,rownum rw
FROM MESSERIES.TBLWIPLOTBASIS basis INNER JOIN MESSERIES.TBLWIPSPLITCONTENT split 
ON  BASIS.BASELOTNO = SPLIT.FROMLOTNO)  WHERE rw <= 100
=======================================
SELECT
aa.*, BB.*
FROM (SELECT FROMlotno,BASELOTNO ,tolotno FROM
(SELECT  BASIS.*,SPLIT.*,rownum rw
FROM MESSERIES.TBLWIPLOTBASIS basis INNER JOIN MESSERIES.TBLWIPSPLITCONTENT split 
ON  BASIS.BASELOTNO = SPLIT.FROMLOTNO)  WHERE rw <= 100)  aa INNER JOIN MESSERIES.TBLWIPUNITLOG_ASSEMBLY bb 
ON aa.TOLOTNO = bb.SERIALNO WHERE aa.fromlotno ='000201191600001'
```

#### 如何填表

##### 1.有主键

###### 1.1EQP为例tableinfo

```
EQP_CLASS.ID=[TableName]
EQP_CLASS.Description=[TableComment]
```

###### 1.2EQP为例columninfo结构

```
主键部分为[NULL]
================================
EQP_CLASS_PROP.ID=[ColName]
EQP_CLASS_PROP.DESCRIPTION=[TableComment]
EQP_CLASS_PROP.Value=[NULL]
EQP_CLASS_PROP.PID_EQP_CLASS_PROP_EQP_CLASS_ID=[TableName]
EQP_CLASS_PROP.FID_EQP_CLASS_PROP_EQP_CLASS_PROP_ID=[NULL]
```

###### 1.3EQP为例columninfo值

```
EQP.ID=[ColValue]
==================================
EQP_PROP.ID=[ColName]
EQP_PROP.Value=[ColValue]
EQP_PROP.PID_EQP_PROP_EQP_ID=[MES-IT].[MESDEVDB].[MESDV].[C_EQUIP_TYPE_T].[TYPE_ID].[ColValue]
EQP_PROP.FID_EQP_PROP_EQP_PROP_ID=[NULL]
```

###### 1.4EQP为例columninfo多对多PXP

```
主要是对应主键字段
EQP_CLASS_X_EQP_MAPPING.FID_EQP_CLASS_ID=[TableName]
EQP_CLASS_X_EQP_MAPPING.FID_EQP_ID=[ColValue]
```

###### 1.5EQP为例columninfo多对多PCP

```
对应每一个非主键字段
M_RESOURCE_RELATIONSHIP_NETWORK_S_RESOURCE_RELATIONSHIP_NETWORK_PROP_BY_RESOURCE_RELATIONSHIP_NETWORK_PROP.PID_RESOURCE_RELATIONSHIP_NETWORK_ID=[MES-IA].[WJAIRPT].[MESSERIES].[TBLUSRSHIFTEXCEPTIONTIME].[DEPARTMENTNO].[ColValue]+[MES-IA].[WJAIRPT].[MESSERIES].[TBLUSRSHIFTEXCEPTIONTIME].[SHIFTNO].[ColValue]+[MES-IA].[WJAIRPT].[MESSERIES].[TBLUSRSHIFTEXCEPTIONTIME].[EXCEPTIONITEMNO].[ColValue]
M_RESOURCE_RELATIONSHIP_NETWORK_S_RESOURCE_RELATIONSHIP_NETWORK_PROP_BY_RESOURCE_RELATIONSHIP_NETWORK_PROP.FID_RESOURCE_RELATIONSHIP_NETWORK_PROP_ID=[ColName]
```

##### 2.无主键

###### 2.1某个字段关联另一个表的主键

columnInfo的结构

```
OTHER_PARAMETER_CLASS_PROP.ID=[ColName]
OTHER_PARAMETER_CLASS_PROP.Value=[ValueDomain] 
OTHER_PARAMETER_CLASS_PROP.PID_OTHER_PARAMETER_CLASS__ID=[TableName]
OTHER_PARAMETER_CLASS_PROP.FID_OTHER_PARAMETER_CLASS_ID=[NULL]
OTHER_PARAMETER_CLASS_PROP.DESCRIPTION=[ColComment]
```

columnInfo的值

```
OTHER_PARAMETER.ID=[UUID]
OTHER_PARAMETER_PROP.ID=[ColName]
OTHER_PARAMETER_PROP.Value=[ColValue]
OTHER_PARAMETER_PROP.PID_OTHER_PARAMETER_ID=[MES-IT].[DGRAMES].[DG_MES].[C_AGGREGATOR_PARAM_BOM_T].[AGGREGATOR_ID].[ColValue] 
OTHER_PARAMETER_CLASS_PROP.FID_OTHER_PARAMETER_ID=[NULL]
THER_PARAMETER_PROP.DESCRIPTION=[ColComment]
```

多对多PXP

```
OTHER_PARAMETER_CLASS_X_OTHER_PARAMETER_MAPPING.FID_OTHER_PARAMETER_CLASS_ID=[TableName]
OTHER_PARAMETER_CLASS_X_OTHER_PARAMETER_MAPPING.FID_OTHER_PARAMETER_ID=[UUID]
```

###### 2.2是主档的扩展表

注意：此时已经不再关注自己的主键，只有group_key填自己的主键。

columnInfo的结构

```
PHY_ASSET_CLASS_PROP.ID=[ColName]
PHY_ASSET_CLASS_PROP.DESCRIPTION=[ColComment]
PHY_ASSET_CLASS_PROP.Value=[NULL]
PHY_ASSET_CLASS_PROP.PID_PHY_ASSET_CLASS_PROP_PHY_ASSET_CLASS_ID=[MES-IT].[DGRAMES].[DG_MES].[C_EQUIPMENT_BASIC_T].[EQUIPMENT_ID].[TableName]
PHY_ASSET_CLASS_PROP.FID_PHY_ASSET_CLASS_PROP_PHY_ASSET_CLASS_PROP_ID=[NULL]
```

columnInfo的值

```
PHY_ASSET_PROP.ID=[ColName]
PHY_ASSET_PROP.DESCRIPTION=[ColComment]
PHY_ASSET_PROP.Value=[ColValue]
PHY_ASSET_PROP.PID_PHY_ASSET_PROP_PHY_ASSET_ID=[MES-IT].[DGRAMES].[DG_MES].[C_EQUIPMENT_BASIC_T].[EQUIPMENT_ID].[ColValue]
PHY_ASSET_PROP.FID_PHY_ASSET_PROP_PHY_ASSET_PROP_ID=[NULL]
```

