## ETL构建DataFlow

#### 1.认识nifi(term)

​       DataFlow Manager：DataFlow Manager（DFM）是NiFi用户，具有添加，删除和修改NiFi数据流组件的权限。

​       FlowFile：FlowFile代表NiFi中的单个数据。FlowFile由两个组件组成：FlowFile Attributes和FlowFile Content 。

​                         Content是FlowFile表示的数据。

​                         Attributes是提供有关数据的信息或上下文的特征; 它们由键值对组成。

​       所有FlowFiles都具有以下标准属性：

​               uuid：一个通用唯一标识符，用于区分FlowFile与系统中的其他FlowFiles。

​               filename：在将数据存储到磁盘或外部服务时可以使用的可读文件名

​               path：在将数据存储到磁盘或外部服务时可以使用的分层结构值，以便数据不存储在单个目录中

​               Processor：处理器是NiFi组件，用于监听传入数据; 从外部来源提取数据; 将数据发布到外部来源; 并从FlowFiles中路由，转换或提取信息。

​               Relationship：每个处理器都为其定义了零个或多个关系。命名这些关系以指示处理FlowFile的结果。处理器处理完FlowFile后，它会将FlowFile路由（或“传输”）到其中一个关系。然后，DFM能够将这些关系中的每一个连接到其他组件，以指定每个潜在处理结果下FlowFile应该在哪里进行下一步。

#### 2.复制版本

​     1.查看variables里面设置的全局变量是否正确.不对应则修改,**名字全部是小写**

| class_main_table     | PHY_ASSET_CLASS                     |
| -------------------- | ----------------------------------- |
| class_prop_table     | PHY_ASSET_CLASS_PROP                |
| class_x_main_table   | PHY_ASSET_CLASS_X_PHY_ASSET_MAPPING |
| main_prop_table      | PHY_ASSET_PROP                      |
| main_table           | PHY_ASSET                           |
| source_pk_name       | EQUIPMENTTYPE-ACCESSORYTYPE         |
| source_schema        | MESSERIES                           |
| source_system_id     | MES-IA                              |
| source_table_comment | 设备对应模治具资料表                |
| source_table_name    | TBLEQPEQUIPMENTTYPEACCTYPE          |
| target_schema        | ISA_STAGING                         |

​                 

​     注意:

​         当我没有pk的时候:再议

​         当我只有一个pk的时候:直接填名字

| TBLEQPEQUIPMENTTYPE | source_pk_name | EQUIPMENTTYPE |
| ------------------- | -------------- | ------------- |
| TBLEQPEQUIPMENTTYPE | source_schema  | MESSERIES     |

​         当我有多个pk的时候:中间用-连接起来,例:Axxx-Bxxx-Cxxx

| TBLEQPEQUIPMENTTYPE | source_pk_name | CARRIERNO-MLAYER-MPOSITION |
| ------------------- | -------------- | -------------------------- |
| TBLEQPEQUIPMENTTYPE | source_schema  | MESSERIES                  |

​      2.修改label的名字

​          核对当前表与模板是否是同一类型,如果不同,修改名字

​           例:PHY_ASSET_CLASS => EQP_CLASS

#### 3.修改PHY_ASSET_CLASS部分的properties

​      一般情况下无需修改

#### 4.修改route路由

​      1.主要是把每一个字段都添加进去

| ACCESSORYNO    | ${SOURCE_COLUMN_NAME:equals('ACCESSORYNO')}    |
| -------------- | ---------------------------------------------- |
| ACCESSORYSTATE | ${SOURCE_COLUMN_NAME:equals('ACCESSORYSTATE')} |

​      2.然后就是配置successed后,连接的queue:

​          主要就是选择第一步配置的字段的名字

​      3.最后修改updateAttribute的属性:

​          1.主要就是依据table mapping,看看对应栏位填写的b部分都有什么属性,比如ID,DESCRIPTION,VALUE,PID,FID

​	            当table Mapping 里面是[NULL]时,这个RouteOnAttribute后面可以没有这个分支

​                    注意:要核对当前表的PID是否可以继续使用,否则修改PID

​                           例:PID_PHY_ASSET_CLASS_PROP_PHY_ASSET_CLASS_ID => PID_EQP_CLASS_PROP_EQP_CLASS_ID 

​          2.如果添加了VALUE属性,则要查看table mapping如何填写的:

​                    当value=[NULL]时,值可以不填

​                    当value=[ValueDomain]时,值填写[VALUEDOMAIN]  

#### 5.修改script脚本

​        1.前面的if..else 对应的就是prop(即值)

​        最后面的basic info 对应的就是class (即结构)

​        2.脚本里有几处要修改的地方:

​                 1.prop_col_name 后面是表的每一个字段,除主键

​                 2.main_id=row['主键名']

​                 3.if ...else  每个后面都换成表中非主键的字段

​                 4.basic_info 添加对应PHY_ASSET里面的属性,例如ID,DESCRIPTION,GRPUP_TABLE,GROUP_KEY,SYSTEM_ID

```
#TBLEMSACCESSORYSTATE_MAINTAIN
import json
from org.apache.commons.io import IOUtils
from java.nio.charset import StandardCharsets
from org.apache.nifi.processor.io import StreamCallback


class PyStreamCallback(StreamCallback):
    def __init__(self, flowfile):
        self.ff = flowfile
        pass

    def process(self, inputStream, outputStream):
        # prop_col_names = self.ff.getAttribute('prop_col_names')
        # 這裡要改
        prop_col_names = ['EQPSERIALNO', 'EQUIPMENTSTATE', 'EQUIPMENTTYPE', 'STARTTIME', 'USERNO',
                          'DESCRIPTION', 'LOTSERIAL','DESCRIPTION1']
        text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
        obj = json.loads(text)

        system_id = self.ff.getAttribute('system_id')
        src_table_name = self.ff.getAttribute('src_table_name')

        # '${SOURCE_COLUMN_NAME}'                       ID,
        # '${source_system_id}'                         SYSTEM_ID,
        # ${source_pk_value}                            GROUP_KEY,
        # '${source_schema}.${source_table_name}'       GROUP_TABLE,
        # ${SOURCE_COLUMN_NAME}                         VALUE,
        # '${source_system_id}'                         PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY,
        # '${source_schema}.${source_table_name}'       PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE

        results_prop = []
        results_basic = []
        for row in obj:
            # 這裡要改
            main_id = row['EQUIPMENTNO']    # pkey

            # main_prop_table
            for col_name in prop_col_names:
                # TODO: read from variable
                d = {}
                # 這裡要改
                if col_name == 'EQPSERIALNO':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'設備編號'
                         }
                elif col_name == 'EQUIPMENTSTATE':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'設備狀態'
                         }
                elif col_name == 'EQUIPMENTTYPE':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'設備類別'
                         }
                elif col_name == 'DESCRIPTION1':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'说明1'
                         }
                elif col_name == 'STARTTIME':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'开始时间'
                         }
                elif col_name == 'USERNO':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'使用者ID'
                         }
                elif col_name == 'DESCRIPTION':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'说明'
                         }
                elif col_name == 'LOTSERIAL':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
			 'DESCRIPTION':'生產批序號'
                         }


                results_prop.append(d)

            # TODO: read from variable
            # main_table  PHY_ASSET 
            # 這裡要改
            basic_info = {
                'ID': main_id,
                'SYSTEM_ID': system_id,
                'DESCRIPTION': row['DESCRIPTION'],
                'GROUP_TABLE': src_table_name,
                'GROUP_KEY': main_id
            }
            results_basic.append(basic_info)

        data = {
            'prop': results_prop,
            'basic': results_basic
        }
        outputStream.write(bytearray(json.dumps(data, separators=(',', ':')).encode('utf-8')))


flowFile = session.get()
if (flowFile != None):
    flowFile = session.write(flowFile, PyStreamCallback(flowFile))
    session.transfer(flowFile, REL_SUCCESS)

```

​      3.这里要注意,如果前面是联合主键的话,此时script里面的main_id=row['主键名']

```
main_id= row['主键1'] + '-' + row['主键2'] + '-' + row['主键3']
======不知道最前面的 prop_col_names后面是填null ,还是要注释掉,因为只有两个字段,还全是主键,此处不知该填什么.========
```

​      4.此时也要注意script脚本中PID对应的属性

​         保证表的属性和脚本的属性一致

#### 6.多对多关系的映射

​     1.这里的多对多指的是结构的主表和值的主表的映射.例如:PHY_ASSET_CLASS => PHY_ASSET

​        并不是PHY_ASSET_CLASS_PROP =>PHY_ASSET_CLASS或者 PHY_ASSET => PHY_ASSET_PROP

​     2.在多对多的关系中,修改UpdateAttribute的属性,这里应该都是FID

| FID_EQP_ID                | ${ID}                                 |
| ------------------------- | ------------------------------------- |
| FID_EQP_SYSTEM_ID         | ${source_system_id}                   |
| FID_EQP_GROUP_KEY         | ${ID}                                 |
| FID_EQP_GROUP_TABLE       | ${source_table_name}                  |
| FID_EQP_CLASS_ID          | ${source_table_name}                  |
| FID_EQP_CLASS_SYSTEM_ID   | ${source_system_id}                   |
| FID_EQP_CLASS_GROUP_KEY   | ${source_pk_name}                     |
| FID_EQP_CLASS_GROUP_TABLE | ${source_schema}.${source_table_name} |

#### 7.没有结构映射

​     1.如果一张表没有结构映射,只有值映射,则CLASS和CLASS_PROP就无需映射,而且多对多的关系也不存在.

​     2.如果表中只有两个字段,而且还都是主键,那么prop的映射也不需要了

#### 8.添加新规则 

​    1.script里面每个主键都要加description ,因为tableInfo 的description是描述这个表,columnInfo的description是描述这个字段

​    2.如果当前表的FID为NUILL,则Route路由处可以不为写相关的FID

​         如果其中某一个字段的属性有fid,那么就在他的UpdateAttribute中添加相应的四个fid(ID,SYSTEM_ID,GROUP_KEY,GROUP_TABLE)

​    3.route后面的每一个字段对应的UpdateAttribute里面,description都写死,直接写字段名

​    4.变量的值

| 对应的属性                  | 对应的值                                           |
| --------------------------- | -------------------------------------------------- |
| SYSTEM_ID:                  | DB的名字  => ${source_system_id}                   |
| GROUP_TABLE:                | SCHEMA.TABLENAME                                   |
| GROUP_ID:                   |                                                    |
| 栏位的时候                  | PK的名                                             |
| 值的时候                    | PK的值                                             |
| ID:                         |                                                    |
| 参考table mapping的结果填写 | 当是结构的时候,主表id =TableName,从表id = ColName  |
|                             | 当是指映射的时候,主表id =ColValue,从表id = ColName |



#### 9.新手必看

​    1.替换作用

​       GennerateTableFetch + ExecuteSQLRecord = ExecuteSQLRecord + SQL语句

​       SQL语句:SELECT  COLUMN_NAME SOURCE_COLUMN_NAME  FROM  ALL_TAB_COLUMNS
                       WHERE OWNER = '${source_schema}' AND TABLE_NAME='${source_table_name}'  => **从整个系统中查出schema对应表的所有字段**

​     2.区别

| ExeceteSQLRecord(常用)                                       | ExecuteSQL                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 执行提供的SQL select查询。查询结果将被转换为**记录写入器指定的格式**。使用流，因此支持任意大的结果集。可以使用标准的调度方法将此处理器调度为在计时器或cron表达式上运行，也可以由传入的流文件触发。如果它是由一个传入的流文件触发的，那么该流文件的属性将在计算select查询时可用，并且该查询可以使用?为了逃避参数。 | 执行提供的SQL select查询。查询结果将转换为**Avro格式**。使用流，因此支持任意大的结果集。可以使用标准的调度方法将此处理器调度为在计时器或cron表达式上运行，也可以由传入的流文件触发。如果它是由一个传入的流文件触发的，那么该流文件的属性将在计算select查询时可用，并且该查询可以使用?为了逃避参数。 |

​      其他参数

| Database Connection Pooling Service | 数据库连接池                                                 |
| ----------------------------------- | ------------------------------------------------------------ |
| SQL Pre-Query                       | 在执行主SQL查询之前执行的以分号分隔的查询列表                |
| SQL select query                    | 要执行的SQL select查询。查询可以是空的、常量值或使用表达式语言从属性构建 |
| SQL Post-Query                      | 在执行主SQL查询之后执行的以分号分隔的查询列表。例如在主查询后设置会话属性。如果没有错误，这些查询的结果/输出将被取消 |
| Max Wait Time                       | 最大等待时间                                                 |
| Record Writer (这就是差别的地方)    | 是否将列名中的非avro兼容字符更改为avro兼容字符。例如，冒号和句号将被更改为下划线 |
| Normalize Table/Column Names        | 是否将列名中的非avro兼容字符更改为avro兼容字符。例如，冒号和句号将被更改为下划线  |
| Use Avro Logical Types              | 是否对十进制/数字、日期、时间和时间戳列使用Avro逻辑类型。如果禁用，则以字符串形式写入。如果启用,逻辑类型和书面作为其基本类型 |
| Compression Format                  | 编写avro文件时使用压缩类型 ,例如: BZIP2,DEFLATE,NONE,SNAPPY,LZO |
| Default Decimal Precision           | 当一个十进制/数字值被写成“DECIMAL”Avro逻辑类型时，需要一个表示可用数字数目的特定“精度” |
| Max Rows Per Flow File              | 单个流文件中包含的最大结果行数。这将允许您将非常大的结果集分解为多个流文件。如果指定的值为零，则在单个流文件中返回所有行。 |
| Output Batch Size                   | 提交进程会话之前要排队的输出流文件的数量。当设置为0时，当处理完所有结果集行并准备好将输出流文件传输到下游关系时，将提交会话。 |

​      3.先是使用上面的processor查询数据,返回json格式,然后可以添加一些属性,比如:src_table_name,system_id,此时**名字全部是大写**

| UpdateAttribute                   | 属性                                                         |
| --------------------------------- | ------------------------------------------------------------ |
| Delete Attributes Expression      | 要从流文件中删除的属性的正则表达式。匹配的现有属性将被删除，无论它们是否被此处理器更新。 |
| Store State                       | 选择是否存储状态。选择“无状态”将提供默认的功能，以无状态的方式更新流文件的属性。选择有状态选项不仅将属性存储在流文件中，而且还将存储在处理器状态中. |
| Stateful Variables Initial Value  | 如果使用状态设置/引用变量，则此值用于设置有状态变量的初始值。只有当状态不包含变量的值时，才会在@OnScheduled方法中使用。如果是有状态运行，这是必需的，但是如果需要，也可以是空的。 |
| Cache Value Lookup Cache Size     | 指定缓存中应该存储多少规范查找值                             |

​           添加属性后,将他们转化为jsonPath

[根据流文件的内容计算一个或多个JsonPath表达式。根据处理器的配置，这些表达式的结果被分配给FlowFile属性，或者被写入到FlowFile本身的内容中。通过添加用户定义的属性来输入JsonPaths;属性的名称映射到将结果放入其中的属性名称(如果目标是flowfile-attribute;否则，属性名将被忽略)。属性的值必须是有效的JsonPath表达式。“自动检测”的返回类型将根据配置的目标进行确定。当“Destination”被设置为“flowfile-attribute”时，将使用“scalar”返回类型。当“Destination”被设置为“flowfile-content”时，将使用“JSON”返回类型。如果JsonPath的计算结果是JSON数组或JSON对象，并且返回类型设置为“scalar”，则流文件将不进行修改，并将被路由到失败。如果提供的JsonPath的计算结果为指定的值，并且将被路由为匹配，则JSON返回类型可以返回标量值。如果目的地是“FlowFile -content”，而JsonPath没有计算出已定义的路径，则将把FlowFile路由到“unmatched”，而不修改其内容。如果目的地是FlowFile -attribute，而表达式没有匹配任何内容，那么属性将被创建为空字符串作为值，而FlowFile将始终被路由到'matched '。 ]

| EvaluateJsonPath          | 属性                                                         |
| ------------------------- | ------------------------------------------------------------ |
| Destination               | 指示是否将JsonPath计算的结果写入FlowFile内容或FlowFile属性;如果使用属性，必须指定属性名属性。如果设置为flowfile-content，则只能指定一个JsonPath，并且忽略属性名。 |
| Return Type               | 指示JSON路径表达式的期望返回类型。选择“auto- detection”将会为“flowfile-content”的目的地设置返回类型为“json”，为“flowfile-attribute”的目的地设置返回类型为“scalar”。 |
| Path Not Found Behavior   | 指示如何处理目的地设置为“flowfile-attribute”时丢失的JSON路径表达式。选择“warn”将在没有找到JSON路径表达式时生成一个警告。 |
| Null Value Representation | 指示产生空值的JSON路径表达式的所需表示形式。                 |

​             然后put到数据源中

[PutDatabaseRecord处理器使用指定的RecordReader输入来自传入流文件的记录(可能有多个)。这些记录被转换成SQL语句并作为单个批处理执行。如果发生任何错误，流文件将被路由到失败或重试，如果成功传输了记录，则传入流文件将被路由到成功。处理器执行的语句类型通过statement type属性指定，该属性接受一些硬编码的值，比如INSERT、UPDATE和DELETE，以及Use语句。type属性'，它使处理器从流文件属性获取语句类型。重点:如果语句类型是UPDATE，那么传入的记录不能改变主键(或用户指定的更新键)的值。如果遇到这样的记录，则向数据库发出的UPDATE语句可能什么也不做(如果没有找到具有新主键值的现有记录)，或者可能无意中损坏现有数据(通过更改主键值为新值的记录)。 ]

| PutDatabaseRecord                   |                             属性                             |
| ----------------------------------- | :----------------------------------------------------------: |
| Record Reader                       |       指定用于解析传入数据和确定数据模式的控制器服务。       |
| Statement Type                      | 指定要生成的SQL语句的类型。如果使用的声明。类型属性'被选中，然后从语句中获取值。在流文件中键入属性。使用语句。type属性' option是唯一允许'SQL'语句类型的选项。如果指定了“SQL”，则“包含SQL的字段”属性指定的字段值将是目标数据库上的有效SQL语句，并将按原样执行。 |
| Database Connection Pooling Service |          用于获取数据库连接以发送记录的控制器服务。          |
| Catalog Name                        | 语句应该更新的目录的名称。这可能不适用于您正在更新的数据库。在这种情况下，保持字段为空 |
| Schema Name                         |                       表所属架构的名称                       |
| Table Name                          |                   语句应该影响的表的名称。                   |
| Translate Field Names               | 如果为真，处理器将尝试将字段名转换为指定表的适当列名。如果为假，字段名必须与列名完全匹配，否则将不更新该列 |
| Unmatched Field Behavior            | 如果传入记录的字段不映射到数据库表的任何列，则此属性指定如何处理这种情况 |
| Unmatched Column Behavior           | 如果传入记录没有针对数据库表的所有列的字段映射，则此属性指定如何处理这种情况 |
| Update Keys                         | 更新键一个逗号分隔的列名列表，惟一标识数据库中用于更新语句的行。如果语句类型为UPDATE且未设置此属性，则使用表的主键。在这种情况下，如果不存在主键，那么如果将不匹配的列行为设置为失败，则转换为SQL将失败。如果语句类型为INSERT，则忽略此属性 |
| Field Containing SQL                | 如果语句类型为“SQL”(在语句中设置)，则包含SQL的字段。类型属性)，此字段指示记录中的哪个字段包含要执行的SQL语句。字段的值必须是一条SQL语句。如果语句类型不是“SQL”，则忽略此字段。 |
| Quote Column identifiers            | 启用此选项将导致所有列名都加引号，从而允许使用保留字作为表中的列名。 |
| Quote Table Identifiers             |      启用此选项将导致引用表名以支持在表名中使用特殊字符      |
| Max Wait Time                       | 一个正在运行的SQL语句允许的最大时间量，0表示没有限制。小于1秒的时间等于0。 |
| Rollback On Failure                 | 指定如何处理错误。默认情况下(false)，如果在处理一个流文件时发生错误，该流文件将根据错误类型被路由到“failure”或“retry”关系，处理器可以继续处理下一个流文件。相反，您可能希望回滚当前处理的流文件并立即停止进一步的处理 |
| Table Schema Cache Size             |                    指定应该缓存多少表模式                    |
| Maximum Batch Size                  | 指定插入和更新语句的最大批大小。此参数对“语句类型”中指定的其他语句无效。0表示批大小没有限制。 |

​      4.wait和notify

​      原理就是拥有共同的名字(即Release Singnal Identifier),然后wait会把名字放到Distributed Cache Service中,notify 再去Distributed Cache Service中获取,相同就唤醒,不同就继续沉睡.

​       作用就是保证我们程序的执行顺序,先是主表,再是从表,例如class => class_prop ,然后就是多对多关联表,要保证两个主表的内容都已经导入进来,

| wait                      | attribute                                                    |
| ------------------------- | ------------------------------------------------------------ |
| Release Signal Identifier | 属性表达式语言语句的值或结果，将根据FlowFile对其进行计算，以确定释放信号缓存键 |
| Target Signal Count       | 这个处理器检查信号计数是否达到这个数字。如果指定了信号计数器的名称，此处理器将检查特定的计数器，否则将检查信号中的总计数 |
| Signal Counter Name       | 如果未指定，此处理器将检查信号中的总计数                     |
| Wait Buffer Count         | 指定可以缓冲的传入流文件的最大数量，以检查它是否可以向前移动。缓冲区越多，性能越好，因为它通过按信号标识符分组流文件减少了与缓存服务的交互次数 |
| Releasable FlowFile Count | 这指定当目标计数达到目标信号计数时可以释放多少流文件。0(0)具有特殊的含义，只要信号计数与目标匹配，就可以释放任意数量的流文件 |
| Expiration Duration       | 将等待的流文件路由到“过期”关系的持续时间                     |
| Distributed Cache Service | 用于检查来自相应通知处理器的释放信号的控制器服务             |
| Attribute Copy Mode       | 指定如何处理从进入通知处理器的流文件中复制的属性             |
| Wait Mode                 | 指定如何处理等待通知信号的流文件                             |



| notify                    | attribute                                                    |
| ------------------------- | ------------------------------------------------------------ |
| Release Signal Identifier | 属性表达式语言语句的值或结果，将根据FlowFile对其进行计算，以确定释放信号缓存键 |
| Signal Counter Name       | 属性表达式语言语句的值或结果，将根据FlowFile对其求值以确定信号计数器的名称。当相应的等待处理器需要知道不同类型事件(如成功或失败、或目标数据源名称等)的出现次数时，信号计数器名称非常有用。 |
| Signal Counter Delta      | 属性表达式语言语句的值或结果，将根据流文件对其进行计算，以确定信号计数器增量。指定计数器应该增加多少。例如，如果以面向批处理的方式在上游流中处理多个信号事件，则可以使用此属性一次性通知处理的事件数量。0(0)有一个特殊的含义，它清除目标计数为0，这是特别有用的时候，用于等待可发布的FlowFile count = 0(0)模式，以提供“开-关-门”类型的流控制。一个(1)可以打开一个对应的等待处理器，而0(0)可以像关闭一个门一样对其进行否定。  |
| Signal Buffer Count       | 指定在通知信号缓存服务之前可以缓存的传入流文件的最大数量。缓冲区越多，性能越好，因为当多个传入流文件共享相同的信号标识符时，通过按信号标识符分组信号，减少了与缓存服务的交互次数。 |
| Distributed Cache Service | 用于缓存释放信号以释放在相应的等待处理器上排队的文件的控制器服务 |
| Attribute Cache Regex     | 名称与此regex匹配的任何属性都将存储在分布式缓存中，并将其复制到从相应的等待处理器释放的任何流文件中。注意，无论这个值是多少，uuid属性都不会被缓存。如果为空，则不缓存任何属性。 |

 

​     5.script脚本中的python语句具体是做什么用的?

​        相当于class_prop中间routed的那些分支

​        **脚本还要在研究**

​    6.当我们获取到数据以后,先查询 => 再添加(UpdateAttribute) =>转换json EvaluateJsonPath => PutDatabaseRecord

​    7.多对多的时候,使用merge功能

​      AttributesToJson:生成输入流文件属性的JSON表示。生成的JSON可以被写入一个新的属性“JSONAttributes”，也可以作为内容写入到FlowFile中。 

| AttributesToJSON              | 属性                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| Attributes List               | 逗号分隔的属性列表将包含在结果JSON中。如果此值为空，则将包含所有现有属性。此属性列表区分大小写。如果在列表中指定的属性没有找到，它将被发送到结果JSON，其中包含空字符串或空值。 |
| Attributes Regular Expression | 将根据流文件属性计算的正则表达式，以选择匹配的属性。此属性可与属性列表属性组合使用。 |
| **Destination**               | 控制是否将JSON值写入新的flowfile属性“JSONAttributes”或写入flowfile内容。写入flowfile内容将覆盖任何现有的flowfile内容。 |
| **Include Core Attributes**   | 确定FlowFile org.apache.nifi.flowfile.attributes。每个FlowFile中包含的CoreAttributes应该包含在最终生成的JSON值中。 |
| **Null Value**                | 如果为真，则结果JSON中不存在或为空的属性将为NULL。如果为false, JSON中将放置空字符串 |

   MergeRecord:该处理器将多个面向记录的流文件合并到一个流文件中，该流文件包含输入流文件的所有记录。这个处理器的工作方式是创建“箱子”，然后向这些箱子中添加流文件，直到箱子装满为止。一旦一个bin被填满，所有的FlowFile将被合并到一个单独的输出FlowFile中，该FlowFile将被路由到“合并”关系。一个bin可能包含许多类似于flowfile的文件。为了使两个流文件被认为“类似于流文件”，它们必须具有相同的模式(由记录读取器标识)，并且，如果设置了<Correlation属性名>属性，则指定属性的值必须相同 

| MergeRecord                   | attribute                                                    |
| ----------------------------- | ------------------------------------------------------------ |
| **Record Reader**             | 指定用于读取传入数据的控制器服务                             |
| **Record Writer**             | 指定用于读取写出数据的控制器服务                             |
| **Merge Strategy**            | 指定用于合并记录的算法。“碎片整理”算法将由属性关联的碎片组合到一个内聚的流文件中。“装箱算法”生成一个由任意选择的流文件填充的流文件 |
| Correlation Attribute Name    | 如果指定了，只有当两个流文件具有相同的属性值时，才会将它们绑定在一起。如果没有指定，则按从队列中取出流文件的顺序绑定它们。 |
| **Attribute Strategy**        | 确定应该将哪些FlowFile属性添加到包中。如果‘Keep All Unique Attributes’被选中，那么任何被绑定的流文件上的任何属性都会被保留，除非它的值与另一个流文件的值冲突。如果选择“仅保留通用属性”，则仅保留包中所有流文件上存在的具有相同值的属性。 |
| **Minimum Number of Records** | 一个容器中包含的记录的最小数量                               |
| Maximum Number of Records     | 一个容器中包含的最大记录数。这是一个“软限制”，因为如果一个流文件被添加到一个bin中，那么该流文件中的所有记录都将被添加，所以这个限制可能会被最后一个输入流文件中的记录数量超过。 |
| **Minimum Bin Size**          | 箱子的最小尺寸                                               |
| Maximum Bin Size              | 包的最大大小。如果没有指定，就没有最大值。这是一个“软限制”，因为如果一个流文件被添加到一个bin中，那么该流文件中的所有记录都会被添加，所以这个限制可能会被最后一个输入流文件中的字节数超过。 |
| Max Bin Age                   | 将触发一个容器完成的容器的最大年龄。预期格式为<duration> <time unit>，其中<duration>为正整数，时间单位为秒、分、时 |
| **Maximum Number of Bins**    | 指定在同一时间可以在内存中保存的容器的最大数量。这个数目不应该小于这个处理器当前线程的最大数目，否则创建的箱子通常只包含一个传入的流文件。 |

​     8.使用wait和notify的时候,系统会把id存放到外部服务**DistributedMapCacheServer** 中,然后通过**DistributedMapCacheClient service** 去连接获取存放到里面的值(也就是我们在wait和notify中设定的clientServer),这里经常会出问题.

#### 10.如何关联

   主要思路就是:先将内容转换成属性,在属性上添加key/value,然后以json的格式转换成内容,put到DB中.

   Demo:先创建flowfile(GenerateFlowfile) => 事件触发 => 添加一个时间 => 查询表中所有字段名(并重命名source_column_name) => 将内容转化成属性(Evaluate JsonPath) => 然后就是判断,是哪个字段(RouteOnAttribute) => 根据table Mappijng 添加一些属性(如PID) => 最后转换成内容(AttributeToJson) => 最终放到DB中(PutDatabaseRecord) 

#### 11.数据量过万,dataflow优化

当我数据量很大时,如果5000一笔,要运行三笔,都要先保证先跑完主表再跑从表,所以此时在wait和notify中Release Singnal Identifier中添加新的配置 ${source_schema}.${source_table_name}.col_val.${Filename}.${fragment.index}.

因为前面的processor都会write一个fragment.index.所以拿此数据作为wait和notify的键.

| UpdateAttribute              |                        |
| ---------------------------- | ---------------------- |
| original.fragment.identifier | ${fragment.identifier} |
| original.fragment.index      | ${fragment.index}      |

上面配置就是防止splitJson切割后会随机分配,所以先保存

| MergeRecord                |                                            |
| -------------------------- | ------------------------------------------ |
| Merge Strategy             | Bin-Packing Algorithm (使用碎片法)         |
| Correlation Attribute Name | ${original.fragment.identifier}            |
| Attribute Strategy         | Keep Only Common Attributes 仅保留通用属性 |
| Minimum Number of Records  | 1                                          |
| Maximum Number of Records  | 10000                                      |

这里要区分

| wait1                     |                                                              |
| ------------------------- | ------------------------------------------------------------ |
| Release Signal Identifier | ${source_schema}.${source_table_name}.col_val.${segment.original.filename}.${original.fragment.index} |

| wait2                     | 这个是没有切割之前的wait                                     |
| ------------------------- | ------------------------------------------------------------ |
| Release Signal Identifier | ${source_schema}.${source_table_name}.col_val.${filename}.${fragment.index} |

| notify                    |                                                              |
| ------------------------- | ------------------------------------------------------------ |
| Release Signal Identifier | ${source_schema}.${source_table_name}.col_val.${filename}.${fragment.index} |

此时,其实split并没有切割filename,所以segment.original.filename == filename

#### 12.debug

  发现错误具体排查,使用logmessage处理器

| logmessage  |                                                              |
| ----------- | ------------------------------------------------------------ |
| log level   | info                                                         |
| log perfix  | 例如:   text=                                                |
| log message | ${source_schema}.${source_table_name}.col_val.${segment.original.filename}.${original.fragment.index} |



```
#TBLEMSACCESSORYSTATE_MAINTAIN
import json
from org.apache.commons.io import IOUtils
from java.nio.charset import StandardCharsets
from org.apache.nifi.processor.io import StreamCallback


class PyStreamCallback(StreamCallback):
    def __init__(self, flowfile):
        self.ff = flowfile
        pass

    def process(self, inputStream, outputStream):
        # prop_col_names = self.ff.getAttribute('prop_col_names')
        # 這裡要改
        prop_col_names = ['NODENO', 'NODETYPE', 'PROCESSNO', 'GROUPNO', 'CREATOR', 'CREATEDATE',
                          'DESCRIPTION', 'PROCESSVERSION','NODEVERSION','STAGENO','SEQUENCE','ENGNO']
        text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
        obj = json.loads(text)

        system_id = self.ff.getAttribute('system_id')
        src_table_name = self.ff.getAttribute('src_table_name')

        # '${SOURCE_COLUMN_NAME}'                       ID,
        # '${source_system_id}'                         SYSTEM_ID,
        # ${source_pk_value}                            GROUP_KEY,
        # '${source_schema}.${source_table_name}'       GROUP_TABLE,
        # ${SOURCE_COLUMN_NAME}                         VALUE,
        # '${source_system_id}'                         PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY,
        # '${source_schema}.${source_table_name}'       PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE

        results_prop = []
        results_basic = []
        for row in obj:
            # 這裡要改
            main_id = row['NODEID']    # pkey

            # main_prop_table
            for col_name in prop_col_names:
                # TODO: read from variable
                d = {}
                # 這裡要改
                if col_name == 'NODENO':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'設備序號'
					   'COL_NAME': col_name
                         }
                elif col_name == 'EQUIPMENTSTATE':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'設備狀態'
					   'COL_NAME': col_name						 
                         }
                elif col_name == 'EQUIPMENTTYPE':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'設備類別'
					   'COL_NAME': col_name						 
                         }
                elif col_name == 'STARTTIME':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'开始时间'
					   'COL_NAME': col_name						 
                         }
                elif col_name == 'USERNO':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'使用者ID'
					   'COL_NAME': col_name						
                         }
                elif col_name == 'DESCRIPTION':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'说明'
					   'COL_NAME': col_name						 
                         }
                elif col_name == 'LOTSERIAL':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'生產批序號'
					   'COL_NAME': col_name						 
                         }
                elif col_name == 'DESCRIPTION1':
                    d = {'Value': row[col_name],
                         'ID': col_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_ID': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE': src_table_name,
                         'PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY': main_id,
						 'DESCRIPTION':'说明1'
					   'COL_NAME': col_name						 
                         }

                results_prop.append(d)

            # TODO: read from variable
            # main_table  PHY_ASSET 
            # 這裡要改
            basic_info = {
                'ID': main_id,
                'SYSTEM_ID': system_id,
                'DESCRIPTION': row['DESCRIPTION'],
                'GROUP_TABLE': src_table_name,
                'GROUP_KEY': main_id
            }
            results_basic.append(basic_info)

        data = {
            'prop': results_prop,
            'basic': results_basic
        }
        outputStream.write(bytearray(json.dumps(data, separators=(',', ':')).encode('utf-8')))


flowFile = session.get()
if (flowFile != None):
    flowFile = session.write(flowFile, PyStreamCallback(flowFile))
    session.transfer(flowFile, REL_SUCCESS)


```

#### 13.导入数据

1.oracle导入csv文件： 

​       1、建好对应的表和字段；

​        2、新建test.ctl文件,用记事本编辑写入:

```
load data

infile 'e:\TB_KC_SERV.csv'   --修改对应的文件路径

into table "TB_KC_SERV"　　　　--修改对应的表名

fields terminated by ','              

optionally enclosed by '"'                 

(SERV_ID,ACC_NBR,PROD_ID)     --修改对应的字段
```

​         3.输入：sqlldr userid=username/password@host control=e:\test.ctl log=e:\test.log 

```
sqlldr userid=MESSERIES/messeries@10.148.206.12/DGRAMES control=e:\test.ctl log=e:\test.log 
```

2.使用plsql导入数据

```
create table TBLPRSNODERELATION(
	FROMNODEID varchar2(100),
	TONODEID varchar2(100),
	LINKNAME varchar2(30),
	FROMNODENO varchar2(100),
	TONODENO varchar2(100),
	PROCESSNO varchar2(100),
	PROCESSVERSION varchar2(3)
)
```

#### 14.特殊情况

##### 第二次ETL的表

```
#TBLEMSACCESSORYSTATE_MAINTAIN
import json
from org.apache.commons.io import IOUtils
from java.nio.charset import StandardCharsets
from org.apache.nifi.processor.io import StreamCallback


class PyStreamCallback(StreamCallback):
    def __init__(self, flowfile):
        self.ff = flowfile
        pass

    def process(self, inputStream, outputStream):
        # prop_col_names = self.ff.getAttribute('prop_col_names')
        # 這裡要改
        prop_col_names = ['NODENO', 'NODETYPE', 'PROCESSNO', 'GROUPNO', 'CREATOR', 'CREATEDATE', 'DESCRIPTION', 'PROCESSVERSION', 'NODEVERSION', 'STAGENO', 'SEQUENCE', 'ENGNO']
        text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
        obj = json.loads(text)

        system_id = self.ff.getAttribute('system_id')
        src_table_name = self.ff.getAttribute('src_table_name')

        # '${SOURCE_COLUMN_NAME}'                       ID,
        # '${source_system_id}'                         SYSTEM_ID,
        # ${source_pk_value}                            GROUP_KEY,
        # '${source_schema}.${source_table_name}'       GROUP_TABLE,
        # ${SOURCE_COLUMN_NAME}                         VALUE,
        # '${source_system_id}'                         PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY,
        # '${source_schema}.${source_table_name}'       PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE

        results_prop = []
        results_basic = []
        for row in obj:
            # 這裡要改
            main_id = row['NODEID']    # pkey
            #main_id= row['PROCESSNO'] + '-' + row['PROPERTYNO'] + '-' + row['PROCESSVERSION']                      
            # main_prop_table
            for col_name in prop_col_names:
                # TODO: read from variable
                d = {}
                # 這裡要改
                if col_name == 'NODENO':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'節點編號',
                          'PK_VALUE': main_id
                         }
                elif col_name == 'NODETYPE':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'節點類別',
                          'PK_VALUE': main_id
                         }
                elif col_name == 'PROCESSNO':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'製程編號',
                          'PK_VALUE': main_id
                         }
                elif col_name == 'GROUPNO':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'群組編號',
                          'PK_VALUE': main_id
                         }
                elif col_name == 'CREATOR':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'建立人',
                          'PK_VALUE': main_id
                         }
                elif col_name == 'CREATEDATE':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'建立日',
                          'PK_VALUE': main_id
                         } 
                elif col_name == 'DESCRIPTION':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'說明',
                          'PK_VALUE': main_id
                         }                         
                elif col_name == 'PROCESSVERSION':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'流程版本',
                          'PK_VALUE': main_id
                         }
                elif col_name == 'NODEVERSION':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'節點版本',
                          'PK_VALUE': main_id
                         }
                elif col_name == 'STAGENO':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'製造層別編號',
                          'PK_VALUE': main_id
                         }                         
                elif col_name == 'SEQUENCE':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
						 'DESCRIPTION':'次序',
                          'PK_VALUE': main_id
                         }       
                elif col_name == 'ENGNO':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
					   'DESCRIPTION':'xxx',
                         'PK_VALUE': main_id,
                         }                           
                results_prop.append(d)

            # TODO: read from variable
            # main_table  PHY_ASSET 
            # 這裡要改
            basic_info = {
                'ID': main_id,
                'SYSTEM_ID': system_id,
                'DESCRIPTION': row['DESCRIPTION'],
                'GROUP_TABLE': src_table_name,
                'GROUP_KEY': main_id,
                'NODE_TYPE':row['NODETYPE']
            }
            results_basic.append(basic_info)

        data = {
            'prop': results_prop,
            'basic': results_basic
        }
        outputStream.write(bytearray(json.dumps(data, separators=(',', ':')).encode('utf-8')))


flowFile = session.get()
if (flowFile != None):
    flowFile = session.write(flowFile, PyStreamCallback(flowFile))
    session.transfer(flowFile, REL_SUCCESS)

```

##### 新分的第三次ETL的表

```
#TBLEMSACCESSORYSTATE_MAINTAIN
import json
from org.apache.commons.io import IOUtils
from java.nio.charset import StandardCharsets
from org.apache.nifi.processor.io import StreamCallback


class PyStreamCallback(StreamCallback):
    def __init__(self, flowfile):
        self.ff = flowfile
        pass

    def process(self, inputStream, outputStream):
        # prop_col_names = self.ff.getAttribute('prop_col_names')
        # 這裡要改
        prop_col_names = ['HOSTID', 'STATION_IDX', 'STATION_NAME', 'LINE_NAME', 'GROUP_NAME', 'SECTION_NAME',
                          'TASK_CODE', 'CYCLE_TIME','STATION_ALIAS','OFFLINE_TYPE','EQUIPMENT_ID','DEAL_FLAG','DAC_TYPE','SIDE','USE_EQUIP_QTY_FLAG','MAINTAIN_TIME','MAINTAIN_EMP','VALID_FLAG']
        text = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
        obj = json.loads(text)

        system_id = self.ff.getAttribute('system_id')
        src_table_name = self.ff.getAttribute('src_table_name')
        source_schema = self.ff.getAttribute('source_schema')

        # '${SOURCE_COLUMN_NAME}'                       ID,
        # '${source_system_id}'                         SYSTEM_ID,
        # ${source_pk_value}                            GROUP_KEY,
        # '${source_schema}.${source_table_name}'       GROUP_TABLE,
        # ${SOURCE_COLUMN_NAME}                         VALUE,
        # '${source_system_id}'                         PID_PHY_ASSET_PROP_PHY_ASSET_SYSTEM_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_ID,
        # ${source_pk_value}                            PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_KEY,
        # '${source_schema}.${source_table_name}'       PID_PHY_ASSET_PROP_PHY_ASSET_GROUP_TABLE

        results_prop = []
        results_basic = []
        for row in obj:
            # 這裡要改
            main_id = row['STATION_NUMBER']    # pkey

            # main_prop_table
            for col_name in prop_col_names:
                # TODO: read from variable
                d = {}
                # 這裡要改
                if col_name == 'HOSTID':
                    d = {'VALUE': row[col_name],                
			 'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'預留欄位'
                         }
                elif col_name == 'STATION_IDX':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'站點排序'
                         }
                elif col_name == 'STATION_NAME':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'站點名稱'
                         }
                elif col_name == 'LINE_NAME':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'線體名稱'
                         }
                elif col_name == 'GROUP_NAME':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'組別名稱'
                         }
                elif col_name == 'SECTION_NAME':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'段別名稱'
                         }
                elif col_name == 'TASK_CODE':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'PQM3.0工位顯示標志位'
                         }
                elif col_name == 'CYCLE_TIME':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'TDC_DLL by 站記log的標志位'
                         }
                elif col_name ==  'STATION_ALIAS':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'站點別名'
                         }
	        elif col_name == 'OFFLINE_TYPE':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'XXXX'
                         }		 
                elif col_name == 'EQUIPMENT_ID':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'站點下的設備編碼'
                         }
	        elif col_name == 'DEAL_FLAG':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'直通率計算標志'
                         }	
	        elif col_name == 'DAC_TYPE':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'DAC進行途程更新状态'
                         }
                elif col_name == 'SIDE':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'制程面別'
                         }		
                elif col_name == 'USE_EQUIP_QTY_FLAG':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'設備採集的數量塞入STATION_REC'
                         }
                elif col_name == 'MAINTAIN_TIME':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': source_schema.src_table_name,
                         'GROUP_TABLE': src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'維護時間'
                         }
                elif col_name == 'MAINTAIN_EMP':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'維護人員'
                         }
                elif col_name == 'VALID_FLAG':
                    d = {'VALUE': row[col_name],
                         'ID': col_name,
                         'PID_EQP_PROP_EQP_ID': main_id,
                         'PID_EQP_PROP_EQP_SYSTEM_ID': system_id,
                         'SYSTEM_ID': system_id,
                         'GROUP_TABLE': source_schema.src_table_name,
                         'GROUP_KEY': main_id,
                         'PID_EQP_PROP_EQP_GROUP_TABLE': src_table_name,
                         'PID_EQP_PROP_EQP_GROUP_KEY': main_id,
			 'DESCRIPTION':'有效性'
                         }                         
                results_prop.append(d)

            # TODO: read from variable
            # main_table  PHY_ASSET 
            # 這裡要改，描述的部分
            basic_info = {
                'ID': main_id,
                'SYSTEM_ID': system_id,
                'DESCRIPTION': row['STATION_NAME'],
                'GROUP_TABLE': src_table_name,
                'GROUP_KEY': main_id
            }
            results_basic.append(basic_info)
            
            #hierarchy_scope
            scope_info = {
                'ID': main_id,
                'FID': row[GROUP_NAME],
                'SYSTEM_ID': system_id,
                'DESCRIPTION': row['STATION_NAME'],
                'GROUP_TABLE': src_table_name,
                'GROUP_KEY': main_id
		   }
		   results_scope.append(scope_info)

        data = {
            'prop': results_prop,
            'basic': results_basic,
            'scope': results_scope
        }
        outputStream.write(bytearray(json.dumps(data, separators=(',', ':')).encode('utf-8')))


flowFile = session.get()
if (flowFile != None):
    flowFile = session.write(flowFile, PyStreamCallback(flowFile))
    session.transfer(flowFile, REL_SUCCESS)

```



