## Linux学习

#### 1.文件和目录

##### 1.新建目录

```
创建文件夹，即目录
[root@zhaonan ~]mkdir test          创建一个test目录

但是不能连续递归创建：
[root@zhaonan ~]mkdir test/test1/test2/test3    语法不支持
使用 -p，可以自行创建多层目录
[root@zhaonan ~]mkdir -p test/test1/test2/test3

也可以新建权限
[root@zhaonan ~]mkdir -m 711 test1

删除文件夹：
rm -rf test    彻底删除，还递归
rmdir  test    删除空的
```

##### 2.切换

```
cd:表示从当前目录切换到其他目录
cd / :表示进去到根目录
pwd：显示工作路径
```

##### 3.查看日志

```
linux查看日志文件内容命令tail、cat、tac、head、echo

1.tail -f test.log
你会看到屏幕不断有内容被打印出来. 这时候中断第一个进程Ctrl-C

2.查看日志的某几行：
   从第3000行开始，显示1000行
   cat filename | tail -n +3000 | head -n 1000
   显示1000行到3000行
   cat filename | head -n 3000 | tail -n +1000
   显示最后1000行
   cat mylog.log |tail -n 1000
   
   解释： tail -n 1000：显示最后1000行
         tail -n +1000：从1000行开始，显示1000行后面的
         head -n 1000：显示前面1000行
 3.sed -n '5,10p' filename  显示第5行到第10行       
 4.tac:从最后一行查看
 5.nl：添加行号打印
     -b  ：指定行号指定的方式，主要有两种：
       -b a ：表示不论是否为空行，也同样列出行号(类似 cat -n)；
       -b t ：如果有空行，空的那一行不要列出行号(默认值)；
     -n  ：列出行号表示的方法，主要有三种：
       -n ln ：行号在萤幕的最左方显示；
       -n rn ：行号在自己栏位的最右方显示，且不加 0 ；
       -n rz ：行号在自己栏位的最右方显示，且加 0 ；
     -w  ：行号栏位的占用的位数。
     -p 在逻辑定界符处不重新开始计算。
     
   例：常用的：nl xxx
              nl -b a xxx
 6.more 翻页查看
 
```



```
cat主要有三大功能：
1.一次显示整个文件。$ cat filename
2.从键盘创建一个文件。$ cat > filename               只能创建新文件,不能编辑已有文件.
3.将几个文件合并为一个文件： $cat file1 file2 > file

   -n 或 --number 由 1 开始对所有输出的行数编号
   -b 或 --number-nonblank 和 -n 相似，只不过对于空白行不编号
   -s 或 --squeeze-blank 当遇到有连续两行以上的空白行，就代换为一行的空白行
   -v 或 --show-nonprinting

  例：把text1de档案加上行号后输入到text2里面
      cat -n text1 > test2 
      把 textfile1 和 textfile2 的档案内容加上行号（空白行不加）之后将内容附加到 textfile3 里。
      cat -b text1 text2 >> text3
```

我们之所以可以任何地方都能用ls，是因为环境变量配置的原因

```
echo $PATH
```

##### 4.修改权限

```
linux中的每个用户必须属于一个组，不能独立于组外。在linux中每个文件有所有者、所在组、其它组的概念
  - 所有者
  - 所在组
  - 其它组

例：-rwxrw-r‐-1 root root 1213 Feb 2 09:39 abc
     --- 第一个字符代表文件（-）、目录（d），链接（l）
     --- 其余字符每3个一组（rwx），读（r）、写（w）、执行（x）
         第一组rwx：文件所有者的权限是读、写和执行
         第二组rw-：与文件所有者同一组的用户的权限是读、写但不能执行
         第三组r--：不与文件所有者同组的其他用户的权限是读不能写和执行也可用数字表示为：r=4，w=2，x=1  因此rwx=4+2+1=7
     --- 1 表示连接的文件数
     --- root 表示用户
     --- root表示用户所在的组
     --- 1213 表示文件大小（字节）
     --- Feb 2 09:39 表示最后修改日期
     --- abc 表示文件名
     
  修改文件或目录的权限
  chmod 755 xxx.yml
  
  chmod u=rwx，g=rx，o=rx abc   ：同上u=用户权限，g=组权限，o=不同组其他用户权限
  chmod u-x，g+w abc            ：给abc去除用户执行的权限，增加组写的权限
  chmod a+r abc                 ：给所有用户添加读的权限
```

#### 2.运行

##### 1.查看进程

```
1.ps命令查找与进程相关的PID号：
    ps a 显示现行终端机下的所有程序，包括其他用户的程序。
    ps -A 显示所有程序。
    ps c 列出程序时，显示每个程序真正的指令名称，而不包含路径，参数或常驻服务的标示。
    ps -e 此参数的效果和指定"A"参数相同。
    ps e 列出程序时，显示每个程序所使用的环境变量。
    ps f 用ASCII字符显示树状结构，表达程序间的相互关系。
    ps -H 显示树状结构，表示程序间的相互关系。
    ps -N 显示所有的程序，除了执行ps指令终端机下的程序之外。
    ps s 采用程序信号的格式显示程序状况。
    ps S 列出程序时，包括已中断的子程序资料。
    ps -t<终端机编号> 指定终端机编号，并列出属于该终端机的程序的状况。
    ps u 以用户为主的格式来显示程序状况。
    ps x 显示所有程序，不以终端机来区分。
   
    最常用的方法是ps aux,然后再通过管道使用grep命令过滤查找特定的进程,然后再对特定的进程进行操作。
    ps aux | grep program_filter_word,ps -ef |grep tomcat

    ps -ef|grep java|grep -v grep 显示出所有的java进程，去处掉当前的grep进程。
 2.杀进程
    kill -9  name
```

##### 2.端口查询

```
telnet hostname ：远程登录
netstat -rn ：查看路由表
netstat -an ：查看端口是否监听

====================
查看端口是否被监听
netstat -nlp | grep beam
```

##### 3.链接文件

```
语法：ln [option] filename linkname 

-s ： 为文件或目录建立符号链接。不加-s表示为文件或目录建立硬链接 
ln -s /usr/software/pip   /usr/bin/pip
```

#### 3.文件系统的操作

##### 1.磁盘与目录的容量

```
df:列出文件系统的整体磁盘使用量
du:评估文件系统的磁盘使用量
==================df===========================
[root@zhaonan ~]# df                               --将系统内所有的文件系统累出来

文件系统         1K-块       已用       可用     已用%   挂载点
/dev/sda3        18658304   1082268   17576036   6%    /
devtmpfs         124452       0       124452     0%    /dev
tmpfs            134288       0       134288     0%    /dev/shm
tmpfs            134288     4740      129548     4%    /run
tmpfs            134288       0       134288     0%    /sys/fs/cgroup

注解：a：显示全部的档案系统和各分割区的磁盘使用情形
     i：显示i -nodes的使用量
     k：大小用k来表示 (默认值)  m:大小以m表示
     h: 以人们较易读懂的GB,MB,KB等格式自行显示
     t：显示某一个档案系统的所有分割区磁盘使用量
     x：显示不是某一个档案系统的所有分割区磁盘使用量
     T：显示每个分割区所属的档案系统名称
     
     
====================du========================
注解：
a：显示全部目录和其次目录下的每个档案所占的磁盘空间
s：只显示各档案大小的总合 
b：大小用bytes来表示
k: 大小用KB来表示
m: 大小用MB来表示
x：跳过在不同文件系统上的目录不予统计
h: 以人们较易读懂的GB,MB,KB等格式自行显示

例：[root@zhaonan ~]# du -ah bbb
     4.0K    bbb/MySQL.php
     4.0K    bbb/index.htm
     4.0K    bbb/p.php
     28K     bbb/memcache.php
     12K     bbb/.session.php.swp
```

##### 2.常见的压缩文件

```
*.z           compresses程序压缩的文件
*.gz          gzip程序压缩的文件
*.bz2         bzip2程序压缩的文件
*.zip　　　　　zip压缩文件
*.rar　　　　　rar压缩文件
*.7z　　　　　 7-zip压缩文件
*.tar         tar程序打包的数据，并没有压缩
*.tar.gz      tar打包的数据，gzip压缩
*.tar.bz2     tar打包的数据，bzip2压缩

================================================
gzip：gzip可以压缩产生后缀为 .gz 的压缩文件，也可以用于解压gzip、compress等程序压缩产生的文件。
例: [root@zhaonan ~]# gzip [Options] file1 file2 file3

注解【options】：
-c　　　　将输出写至标准输出，并保持原文件不变
-d　　　　进行解压操作
-v　　　　输出压缩/解压的文件名和压缩比等信息
-digit　　digit部分为数字(1-9)，代表压缩速度，digit越小，则压缩速度越快，但压缩效果越差，digit越大，则压缩速度越慢，压缩效果越好。默认为6.


gzip exp1.txt exp2.txt　　　//分别将exp1.txt和exp2.txt压缩，且不保留原文件。
gzip -dv exp1.gz　　　　　　 //将exp1.gz解压，并显示压缩比等信息。
gzip -cd exp1.gz > exp.1　　//将exp1.gz解压的结果放置在文件exp.1中，并且原压缩文件exp1.gz不会消失
==================================================
bzip2：是采用更好压缩算法的压缩程序，一般可以提供较之gzip更好的压缩效果。
例: [root@zhaonan ~]# bzip2 [Options] file1 file2 file3
注解【potions】
　　-c　　　　//将输出写至标准输出
　　-d　　　　//进行解压操作
　　-v　　　　//输出压缩/解压的文件名和压缩比等信息
　　-k　　　　//在压缩/解压过程中保留原文件
　　-digit　 //digit部分为数字(1-9)，代表压缩速度，digit越小，则压缩速度越快，但压缩效果越差，digit越大，压缩效果越好。默认为6.

bzip2 exp1.txt exp2.txt　　　　//分别将exp1.txt和exp2.txt压缩，且不保留原文件。
bzip2 -dv exp1.bz2　　　　　　 //将exp1.bz2解压，并显示压缩比等信息。
bzip2 -kd exp1.bz2 　　　　　 //将exp1.bz2解压，并且原压缩文件exp1.bz2不会消失
===================================================
gzip 或 bzip2 带有多个文件作为参数时，执行的操作是将各个文件独立压缩，而不是将其放在一起进行压缩。
tar 指令可以将文件打包成文件档案(archive)存储在磁盘/磁带中，打包操作一般伴随压缩操作，也可以使用 tar 指令对打包压缩后的文件解压。

tar常用参数命令如下：
[root@zhaonan ~]# tar [Options] file_archive　　  //注意tar的第一参数必须为命令选项，即不能直接接待处理文件
　　常用命令参数：
　　//指定tar进行的操作，以下三个选项不能出现在同一条命令中
　　-c　　　　　　　　//创建一个新的打包文件(archive)
　　-x　　　　　　　　//对打包文件(archive)进行解压操作
　　-t　　　　　　　　//查看打包文件(archive)的内容,主要是构成打包文件(archive)的文件名

　　//指定支持的压缩/解压方式，操作取决于前面的参数，若为创建(-c),则进行压缩，若为解压(-x),则进行解压，不加下列参数时，则为单纯的打包操作(而不进行压缩)，产生的后缀文件为.tar
　　-z　　　　　　　　//使用gzip进行压缩/解压，一般使用.tar.gz后缀
　　-j　　　　　　　　//使用bzip2进行压缩/解压，一般使用.tar.bz2后缀

　　//指定tar指令使用的文件，若没有压缩操作，则以.tar作为后缀
　　-f filename　　 //-f后面接操作使用的文件，用空格隔开，且中间不能有其他参数，推荐放在参数集最后或单独作为参数
　　　　　　　　　//文件作用取决于前面的参数，若为创建(-c),则-f后为创建的文件的名字(路径)，若为(-x/t),则-f后为待解压/查看的打包压缩文件名

　　//其他辅助选项
　　-v　　　　　　　　//详细显示正在处理的文件名
　　-C Dir　　　　　 //将解压文件放置在 -C 指定的目录下
　　-p(小写)　　　　 //保留文件的权限和属性，在备份文件时较有用
　　-P(大写)　　　　 //保留原文件的绝对路径，即不会拿掉文件路径开始的根目录，则在还原时会覆盖对应路径上的内容
　　--exclude=file //排除不进行打包的文件

tar -cvjpf etc.tar.bz2 /etc　　//打包：-c为创建打包文件，-f后面接文件的名称，使用了.tar.bz2后缀，最后面为具体的操作对象/etc目录
tar -tvjf　etc.tar.bz2　　　　　//查看，-f对应所查看的文件的名称，文件后缀显示使用bzip2进行压缩，所以加入-j选项，-v会显示详细的
tar -xvjf　etc.tar.bz2　　　　　//解压，-f指定的是解压使用的文件，文件后缀显示使用bzip2进行压缩，所以加入-j选项，即使用bzip2解压
tar -xvf etc.tar -C ~　　　　　 //将直接打包的.tar文件解压,并放置在用户主目录下
====================================================
unzip ：命令与之前的 tar 指令类似，具有对 zip 文件进行查看、测试和解压的功能。
[root@zhaonan ~]# unzip [Options] file[.zip]　　　　//不接任何Options时，默认将指定的file文件解压至当前文件夹，可同时接受多个文件参数
　　常用命令参数：
　　//压缩文件内容查看
　　-Z　　　　　　//以形如 ls -l 的格式显示目标文件内容，实际原理是命令第一个参数为-Z时，其余参数会被视为 zipinfo 的参数，并产生对应效果 
　　-Z1　　　　　//仅显示压缩文件内容的文件名，更多显示可查看 zipinfo 命令的 man 帮助
　　-l　　　　　　//显示压缩文件中包括时间、占用空间和文件名等信息，内容上较 -Z 更简单

　　//文件测试
　　-t　　　　　　　　//在内存中解压文件并进行文件的完整性校验(CRC校验)

　　//解压缩参数，注意unzip默认即为解压操作
　　-f　　　　　　　　//注意与 tar 命令不同，unzip指定 -f 参数时，则将磁盘上已经存在且内容新于对应磁盘文件的压缩内容解压出来
　　-n　　　　　　　　//解压缩时不覆盖已存在的文件(而是跳过)
　　-q　　　　　　　　//安静模式，仅解压缩而不输出详细信息
　　-d dir　　　　　 //将文件解压至dir指定的文件夹中

查看压缩文件的所有文件名(注意 -Z 选项表示之后所有的参数被视为 zipinfo 的参数并输出相应结果)：unzip -Z1 file.zip
测试文件的完整性：unzip -t file.zip
将文件解压至当前用户的主目录：unzip  -q file.zip  -d ~
```

##### 3.系统文件备份

```
dump：用来备份ext2或者ext4文件系统，可以将目录或者这个文件系统备份到制定地方，或备份成一个打文件
  语法：dump [-cnu][-0123456789][-b <区块大小>][-B <区块数目>][-d <密度>][-f <设备名称>][-h <层级>][-s <磁带长度>][-T <日期>][目录或文件系统] 或 dump [-wW]
  
  注释：    
    -0123456789 　备份的层级。
    -b<区块大小> 　指定区块的大小，单位为KB。
    -B<区块数目> 　指定备份卷册的区块数目。
    -c 　修改备份磁带预设的密度与容量。
    -d<密度> 　设置磁带的密度。单位为BPI。
    -f<设备名称> 　指定备份设备。
    -h<层级> 　当备份层级等于或大雨指定的层级时，将不备份用户标示为"nodump"的文件。
    -n 　当备份工作需要管理员介入时，向所有"operator"群组中的使用者发出通知。
    -s<磁带长度> 　备份磁带的长度，单位为英尺。
    -T<日期> 　指定开始备份的时间与日期。
    -u 　备份完毕后，在/etc/dumpdates中记录备份的文件系统，层级，日期与时间等。
    -w 　与-W类似，但仅显示需要备份的文件。
    -W 　显示需要备份的文件及其最后一次备份的层级，时间与日期。
==================================================
restore

注释：
    -t：此模式用在观察dump备份中含有什么重要数据，类似tar -t功能
    -C：此模式可以将dump内的数据拿出来跟实际的文件系统做比较，最终会列出在dump档案内有记录的，且目前文件系统不一样的档案
    -i：进入互动模式，可以仅还原部分档案，用在dump目录备份还原，进入互动模式，可以通过help命令来查看帮助手册
    -r：用在针对还原整个文件系统的dump备份
    -h：查看完整备份数中的inode与文件系统label等信息
    -f：接要处理的那个dump档案
    -D：与-C进行搭配，可以查出后面接的挂载点与dump内有不同的档
==================================================    
dump -0f  /tmp/user.bak  /home/ubuntu
    将/home/ubuntu这个目录里面的东西备份成/tmp/user.bak文件，备份层级为0。
dump -1f /tmp/user.bak /home/ubuntu
    将/home/ubuntu这个目录里面的东西备份成/tmp/user.bak文件，备份层级为1。只备份上次备份层级为0后发生过更改的部分。


dump -0f  /tmp/user.bak  /home/ubuntu       备份
restore -f  /tmp/user.bak  /home/ubuntu     还原
```

#### 4.文本编辑器

##### 1.vi/vim

文件命令：

```

  同时打开多个文件
  vim file1,file2,file2....
  在vim窗口打开新文件
  :open file
  
```

查找命令

```
/text     查找text，按n键查找下一个，按N键查找上一个
?text     查找text，按n键查找下一个，按N键查找上一个
:set ignorecase　　忽略大小写的查找
:set noignorecase　　不忽略大小写的查找
查找很长的词，如果一个词很长，键入麻烦，可以将光标移动到该词上，按*或#键即可以该单词进行搜索，相当于/搜索。而#命令相当于?搜索。
:set hlsearch　　高亮搜索结果，所有结果都高亮显示，而不是只显示一个匹配。
:set nohlsearch　　关闭高亮搜索显示
:nohlsearch　　关闭当前的高亮显示，如果再次搜索或者按下n或N键，则会再次高亮。
:set incsearch　　逐步搜索模式，对当前键入的字符进行搜索而不必等待键入完成。
:set wrapscan　　重新搜索，在搜索到文件头或尾时，返回继续搜索，默认开启
```

替换命令

```
ra 将当前字符替换为a，当期字符即光标所在字符。
s/old/new/ 用old替换new，替换当前行的第一个匹配
s/old/new/g 用old替换new，替换当前行的所有匹配
%s/old/new/ 用old替换new，替换所有行的第一个匹配
%s/old/new/g 用old替换new，替换整个文件的所有匹配

:1,$s/word1/word2/g   从第一行到最后一行，查找work1,并用word2替换
:1,$s/word1/word2/g   从第一行到最后一行，查找work1,并用word2替换，替换前显示提示字符串用户确认是否替换

:10,20 s/^/    /g 在第10行知第20行每行前面加四个空格，用于缩进。
ddp 交换光标所在行和其下紧邻的一行。
```

移动命令

```
h 左移一个字符
l 右移一个字符，这个命令很少用，一般用w代替。
k 上移一个字符
j 下移一个字符
以上四个命令可以配合数字使用，比如20j就是向下移动20行，5h就是向左移动5个字符，在Vim中，很多命令都可以配合数字使用，比如删除10个字符10x，在当前位置后插入3个！，3a！<Esc>，这里的Esc是必须的，否则命令不生效。

w 向前移动一个单词（光标停在单词首部），如果已到行尾，则转至下一行行首。此命令快，可以代替l命令。

b 向后移动一个单词 2b 向后移动2个单词

e，同w，只不过是光标停在单词尾部

ge，同b，光标停在单词尾部。

^ 移动到本行第一个非空白字符上。

0（数字0）移动到本行第一个字符上，

<HOME> 移动到本行第一个字符。同0健。

移动到行尾3 移动到下面3行的行尾

gg 移动到文件头。 = [[

G（shift + g） 移动到文件尾。 = ]]

f（find）命令也可以用于移动，fx将找到光标后第一个为x的字符，3fd将找到第三个为d的字符。

F 同f，反向查找。

跳到指定行，冒号+行号，回车，比如跳到240行就是 :240回车。另一个方法是行号+G，比如230G跳到230行。

Ctrl + e 向下滚动一行

Ctrl + y 向上滚动一行

Ctrl + d 向下滚动半屏

Ctrl + u 向上滚动半屏

Ctrl + f 向下滚动一屏

Ctrl + b 向上滚动一屏
```

撤销命令

```
u 撤销（Undo）
U 撤销对整行的操作
Ctrl + r 重做（Redo），即撤销的撤销。
```

删除命令

```
x 删除当前字符

3x 删除当前光标开始向后三个字符

X 删除当前字符的前一个字符。X=dh

dl 删除当前字符， dl=x

dh 删除前一个字符

dd 删除当前行

dj 删除上一行

dk 删除下一行

10d 删除当前行开始的10行。

D 删除当前字符至行尾。D=d$

d$ 删除当前字符之后的所有字符（本行）

kdgg 删除当前行之前所有行（不包括当前行）

jdG（jd shift + g）   删除当前行之后所有行（不包括当前行）

:1,10d 删除1-10行

:11,$d 删除11行及以后所有的行

:1,$d 删除所有行

J(shift + j)　　删除两行之间的空行，实际上是合并两行。
```

拷贝和粘贴

```
yy 拷贝当前行

nyy 拷贝当前后开始的n行，比如2yy拷贝当前行及其下一行。

p  在当前光标后粘贴,如果之前使用了yy命令来复制一行，那么就在当前行的下一行粘贴。

shift+p 在当前行前粘贴

:1,10 co 20 将1-10行插入到第20行之后。

:1,co 将整个文件复制一份并添加到文件尾部。

正常模式下按v（逐字）或V（逐行）进入可视模式，然后用jklh命令移动即可选择某些行或字符，再按y即可复制

ddp交换当前行和其下一行

xp交换当前字符和其后一个字符
```

剪切命令

```
正常模式下按v（逐字）或V（逐行）进入可视模式，然后用jklh命令移动即可选择某些行或字符，再按d即可剪切

ndd 剪切当前行之后的n行。利用p命令可以对剪切的内容进行粘贴

:1,10d 将1-10行剪切。利用p命令可将剪切后的内容进行粘贴。

:1, 10 m 20 将第1-10行移动到第20行之后。
```

退出命令

```
:w 将编辑的数据写入到磁盘文件中
:wq 保存并退出
:q! 强制退出并忽略所有更改
ZZ 保存并退出
:e! 放弃所有修改，并打开原来文件。

:w[filename]   将编辑的数据保存成另一个文件
:r[filename]   在编辑的数据中，读入另一个文件的数据，即将filename这个文件内容加到光标所在的行后面
:n1,n2 w [filename]   将n1n2de内容保存成filename这个文件
:! command    暂时离开vi模式执行command的显示结果
    例如： :! ls /home  跳出编辑，展示home的内容
:set nu     显示行号，会在每一行的前缀显示该行的行号
:set nonu   取消行号
```

窗口命令

```
:split或new 打开一个新窗口，光标停在顶层的窗口上

:split file或:new file 用新窗口打开文件

split打开的窗口都是横向的，使用vsplit可以纵向打开窗口。

Ctrl+ww 移动到下一个窗口

Ctrl+wj 移动到下方的窗口

Ctrl+wk 移动到上方的窗口

关闭窗口

:close 最后一个窗口不能使用此命令，可以防止意外退出vim。

:q 如果是最后一个被关闭的窗口，那么将退出vim。

ZZ 保存并退出。
```

注释命令

```
perl程序中#开始的行为注释，所以要注释某些行，只需在行首加入#
3,5 s/^/#/g 注释第3-5行
3,5 s/^#//g 解除3-5行的注释
1,$ s/^/#/g 注释整个文档。
:%s/^/#/g 注释整个文档，此法更快。
```

案例联系

```
1.请在/tmp目录下新建一个名为vitest的目录
2.进入vitest目录当中
3.将/etc/man.config复制到本目录下
4.使用vi编辑器打开
5.在vi中设置一下行号
6.移动到58行，想右移动40个字符，请问你看到的双引号的内容
7.移动到第一行，并且向下查找一下"bzip2"这个字符串，他在第几行
8.将50行到100行的man转化为MAN，并且一个一个挑选是否需要修改，一直按y，结果会显示出现了几个man
9.修改完以后，突然反悔了，要全部还原，有哪些方法
10.复制65-73这9行的内容，并且粘贴到最后一行
11.21-42行之间的开头以“#”的批注我不要了，要怎么删除
12.经文件另存为一个man.text.config
13.去到第27行，并且删除15个字符，
14.在第一行新增一行，该行内容输入 I am a student
15.保存后离开
==============实现上面需求=====================
1.mkdir vitest
2.cd vitest
3.cd /etc/man.config .
4.vi man.config
5.:set nu
6.先按下58G ，在按下40->
7.先按下1G或者gg，输入/bzip2
8.:50,100s/man/MAN/gc
9.第一个方法：一直按u
  第二个:不保存退出:q!,在进入
10.输入65G，在输入9yy，之后最后一行会出现复制九行之类的说明字样，按下G，走到最后一行，在按p粘贴
11.因为21到42行共有22行，因此用22G ，22dd，就能删除22行，
12.:w man.test.config
13.输入27G，在输入15x
14.输入1G，按下大写的o便新增一行，且在插入模式 输入 i am  a student
15.:wq
```

##### 2.块选择

```
=========勾选块=============
v	光标经过的字符反白
V	光标经过的行反白
[Ctrl]+v	块选择
=========选中后处理==========
y: yank 复制， P/p: （光标前/后）粘贴
P/p: 替换为buffer内容
r : 字符替换
U/u ：字符大小写变换
I： 块前插入字符
A：块后插入字符
d：将反白的地方删除


复制块的操作：
光标走到位置，ctrl+v，然后上下左右，选择要复制的块，按下y，然后 走到要放的位置，按p
```

#### 5.环境变量

```
1.查看path值
[root@zhaonan ~]exprot
declare -x CVS_RSH="ssh"
declare -x DISPLAY=":0.0"
declare -x G_BROKEN_FILENAMES="1"
declare -x HISTSIZE="1000"
declare -x HOME="/root"
declare -x HOSTNAME="localhost"
declare -x INPUTRC="/etc/inputrc"
declare -x LANG="zh_CN.UTF-8"
declare -x LESSOPEN="|/usr/bin/lesspipe.sh %s"
declare -x LOGNAME="root"
declare -x .......
2.查看单独的环境变量
[root@zhaonan ~]#echo $PATH
/usr/kerberos/sbin:/usr/kerberos/bin:/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin
3.添加环境变量
  #vim ~/.bashrc
  export PATH="/opt/STM/STLinux-2.3/devkit/sh4/bin:$PATH"
 

   # vim /etc/profile
export PATH="/opt/STM/STLinux-2.3/devkit/sh4/bin:$PATH"
保存，退出，然后运行：
#source /etc/profile
```

#### 6.安装rpm包

Linux系统中，软件包一般可以分为2种，一种是源代码包，例外一种就是RPM包。 

```
安装软件可以先下载rpm包，在安装
rpm -i example.rpm                            安装rpm包
rpm -iv example.rpm                   安装包并在安装过程中显示安装的文件信息
rpm -ivh example.rpm                  安装包并在安装过程中显示安装的文件信息和安装进度
```

##### 1.rpm常用命令

```
1.安装一个包
rpm -ivh xxx.rpm
2.升级一个包
rpm -Uvh 
3.卸载一个包
rpm -e
4.安装参数
--force     如果覆盖属于其他包的文件也强迫安装
--nodeps    如果该rpm包的安装依赖其他包，即使其他包没有安装，也强迫安装
5.查询一个包是否被安装
rpm -q <rpm package name>
6.得到被安装包的信息
rpm -qi  <rpm package name>
7.列出该包中有哪些文件
rpm -ql  <rpm package name>
8.累出服务器上的一个文件属于哪一个rpm包
rpm -qf  
9.列出所有被安装的包rpm package
rpm -qa
```

#### 7.yum

##### 1.配置yum源

```
1.配置阿里源
  找到源配置文件 /etc/yum.repos.d/
  删旧的，新建一个以.repo结尾的文件
  编辑：
    [base]
    name=CentOS-$releasever - Base - mirrors.aliyun.com
    failovermethod=priority
    baseurl=http://mirrors.aliyun.com/centos/7/os/x86_64/
    gpgcheck=1
    enabled=1
    gpgkey=http://mirrors.aliyun.com/centos/7/os/x86_64/RPM-GPG-KEY-CentOS-7
    
2.查看Redhat自带的yum软件包
  rpm -qa | grep yum
  卸载这些软件包
  rpm -qa | grep yum | xargs rpm -e --nodeps       不检查依赖关系，直接卸载
  然后现在需要的rpm包，放到固定的文件夹里
  统一安装，rpm -ivh *
  查询是否安装好  rpm -qa | grep yum
  如上，修改源配置  
  最后清除缓存 yum clean all 再 yum repolist

```

#### 8.ansible

```
[linux]
10.148.200.49 ansible_user=deltaman  ansible_port=22

[windows]
10.136.59.225
        
[windows:vars]
ansible_user='DELTA\TONY.IC.LIN'
ansible_password=Ddjbmyrtx1!
ansible_port=5986
ansible_connection=winrm
ansible_winrm_server_cert_validation=ignore
ansible_winrm_scheme='https'
ansible_winrm_transport=ntlm

[hao]
10.149.77.60
        
[hao:vars]
ansible_user='DELTA\HAO.ZHAO'
ansible_password=Yukee1314v
ansible_port=5985
ansible_connection=winrm
ansible_winrm_server_cert_validation=ignore
ansible_winrm_scheme='http'
ansible_winrm_transport=ntlm

[dbserver]
10.148.202.1
#ansible_user=study ansible_port=1521 ansible_password=study

[windows_50]
10.148.200.50
        
[windows_50:vars]
ansible_user='DELTA\NAN.ZHAO'
ansible_password=Tomcat1234
ansible_port=5985
ansible_connection=winrm
ansible_winrm_server_cert_validation=ignore
ansible_winrm_scheme='http'
ansible_winrm_transport=ntlm
```

#### 9.pip

```
更换阿里的源
sudo pip install isodate -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com

sudo pip install urlgrabber -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com
sudo pip install ansible --upgrade  -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com
软链
/anaconda/python/bin/ansible
```

#### 10.管道符

```
管道符使用"丨"代表。管道符也是用来连接多条命令的，如"命令1丨命令2"。不过和多命令顺序执行不同的是，用管道符连接的命令，命令 1 的正确输出作为命令 2 的操作对象。这里需要注意，命令 1 必须有正确输出，而命令 2 必须可以处理命令 1 的输出结果；而且命令 2 只能处理命令 1 的正确输出，而不能处理错误输出。
```

如果内容很多，可以用more来分屏显示



```plsql
ll命令是用来查看文件的长格式
ll -a /etc/ >/home/deltaman/testfile           --将结果放到testfile中
more testfile                                  --将结果分屏

使用管道
netstat -an | gerp  8080 
```

#### 11.playbook遇到的问题

```
1.一口气创建角色
  mkdir -p /etc/ansible/roles/{a,b,c,d}/{defaults,files,handlers,meta,tasks,templates,vars}
2.没有公钥，设置不检测
  sudo yum install telnet –y --nogpgcheck
3.pip安装更换源
  pip install telnet -i http://mirrors.aliyun.com/pypi/simple/  --trusted-host mirrors.aliyun.com
```

12.上传的命令

```
rz
```

