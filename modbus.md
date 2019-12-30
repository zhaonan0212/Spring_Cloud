### modbus

1.概念
①Coil和Register
　　Modbus中定义的两种数据类型。Coil是位（bit）变量；Register是整型（Word，即16-bit）变量。
②Slave和Master与Server和Client
　　同一种设备在不同领域的不同叫法。
　　Slave： 工业自动化用语；响应请求；
　　Master：工业自动化用语；发送请求；
　　Server：IT用语；响应请求；
　　Client：IT用语；发送请求；
　　在Modbus中，Slave和Server意思相同，Master和Client意思相同。

2.Modbus数据模型
　　Modbus中，数据可以分为两大类，分别为Coil和Register，每一种数据，根据读写方式的不同，又可细分为两种（只读，读写）。
　　Modbus四种数据类型：
　　Discretes Input　　　　位变量　　　　只读
　　Coils　　　　　　　　　 位变量　　　　读写
　　Input Registers　　　　16-bit整型	　 只读
　　Holding Registers	　　  16-bit整型	　 读写
　　通常，在Slave端中，定义四张表来实现四种数据。

 

3.Modbus地址范围对应表

设备地址	　　　　Modbus地址　　   	描述	　　               	功能	　　R/W
1~10000	　　    address-1           Coils（Output）       0          R/W
10001~20000   address-10001    Discrete Inputs        01        R
30001~40000   address-30001    Input Registers        04        R
40001~50000   address-40001    Holding Registers     03        R/W

4.Modbus变量地址
映射地址             Function Code         地址类型          R/W          描述
0xxxx               01,05,15                 Coil                R/W          -
1xxxx               02                          离散输入          R              -
2xxxx               03,04,06,16            浮点寄存器       R/W          两个连续16-bit寄存器表示一个浮点数（IEEE754）
3xxxx               04                          输入寄存器       R              每个寄存器表示一个16-bit无符号整数（0~65535）
4xxxx               03,06,16                保持寄存器        R/W          -
5xxxx               03,04,06,16           ASCII字符         R/W          每个寄存器表示两个ASCII字符





1.概念
①Coil和Register
　　Modbus中定义的两种数据类型。Coil是位（bit）变量；Register是整型（Word，即16-bit）变量。
②Slave和Master与Server和Client
　　同一种设备在不同领域的不同叫法。
　　Slave： 工业自动化用语；响应请求；
　　Master：工业自动化用语；发送请求；
　　Server：IT用语；响应请求；
　　Client：IT用语；发送请求；
　　在Modbus中，Slave和Server意思相同，Master和Client意思相同。

2.Modbus数据模型
　　Modbus中，数据可以分为两大类，分别为Coil和Register，每一种数据，根据读写方式的不同，又可细分为两种（只读，读写）。
　　Modbus四种数据类型：
　　Discretes Input　　　　位变量　　　　只读
　　Coils　　　　　　　　　 位变量　　　　读写
　　Input Registers　　　　16-bit整型	　 只读
　　Holding Registers	　　  16-bit整型	　 读写
　　通常，在Slave端中，定义四张表来实现四种数据。

 

3.Modbus地址范围对应表

设备地址	　　　　Modbus地址　　   	描述	　　               	功能	　　R/W
1~10000	　　    address-1           Coils（Output）       0          R/W
10001~20000   address-10001    Discrete Inputs        01        R
30001~40000   address-30001    Input Registers        04        R
40001~50000   address-40001    Holding Registers     03        R/W

4.Modbus变量地址
映射地址             Function Code         地址类型          R/W          描述
0xxxx               01,05,15                 Coil                R/W          -
1xxxx               02                          离散输入          R              -
2xxxx               03,04,06,16            浮点寄存器       R/W          两个连续16-bit寄存器表示一个浮点数（IEEE754）
3xxxx               04                          输入寄存器       R              每个寄存器表示一个16-bit无符号整数（0~65535）
4xxxx               03,06,16                保持寄存器        R/W          -
5xxxx               03,04,06,16           ASCII字符         R/W          每个寄存器表示两个ASCII字符



**Modbus协议两种传输方式**

　　常用的MODBUS通讯规约有两种，一种是MODBUS ASCII，一种是MODBUS RTU。每个设备必须都有相同的传输模式。所有设备都支持RTU模式，ASCII传输模式是选项。

　　(1)ASCII传输方式

　　Modbus串行链路的设备被配置为使用ASCII模式通信时，报文中的每8位字节以两个ASCII字符发送。例：字节0X5B会被编码为两个字符：0x35和0x42进行传送(ASCII编码0x35="5"，0x42="B")，这样传输效率会降低。

　　在ASCII模式，报文用特殊的字符区分帧起始和帧结束。一个报文必须以一个‘冒号’(：)(ASCII十六进制3A)起始，以‘回车-换行’(CRLF)对(ASCII十六进制0D和0A)结束。设备连续的监视总线上的‘冒号’字符。当收到这个字符后，每个设备解码后续的字符一直到帧结束。报文中字符间的时间间隔可以达一秒。如果有更大的间隔，则接受设备认为发生了错误。

　　(2)[RTU传输](http://www.four-faith.com/2018/industry_0817/666.html)方式

　　当设备使用RTU(RemoteTerminalUnit)模式在Modbus串行链路通信，报文中每个8位字节含有两个4位十六进制字符。这种模式的主要优点是较高的数据密度，在相同的波特率下比ASCII模式有更高的传输效率。每个报文必须以连续的字符流传送。



