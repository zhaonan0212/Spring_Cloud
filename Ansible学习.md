## Ansible学习

学习目标

```
Control Machine 和 Target Machine(Windows), 如何從Control Machine下一個playbook 指令, 自動連到target machine, Copy一個文件到指定位置. 
```

#### 1.Ansible简介

```
ansible是新出现的自动化运维工具，基于Python开发，集合了众多运维工具（puppet、cfengine、chef、func、fabric）的优点，实现了批量系统配置、批量程序部署、批量运行命令等功能。ansible是基于模块工作的，本身没有批量部署的能力。真正具有批量部署的是ansible所运行的模块，ansible只是提供一种框架。主要包括：

(1)、连接插件connection plugins：负责和被监控端实现通信；

(2)、host inventory：指定操作的主机，是一个配置文件里面定义监控的主机；

(3)、各种模块核心模块、command模块、自定义模块；

(4)、借助于插件完成记录日志邮件等功能；

(5)、playbook：剧本执行多个任务时，非必需可以让节点一次性运行多个任务。
```

#### 2.ansible-2.5.3.tar.gz的安装

```plsql
  1.找到ansible对应版本的链接
      https://releases.ansible.com/ansible/ansible-2.5.3.tar.gz
      在linux中下载，执行wget https：//resleases.ansible.com......      --此方法一直报警
      解决：curl -O -L https：//resleases......
      
  2.查看命令：ls
  
  3.解压：tar xf ansible ansible-2.3.5.tar.gz
  
  4.进到解压后的文件： cd ansible-2.5.3
  
  5.展示内容： ls
    找到 setup.py
    
  6.因为ansible是采用python开发的，所以安装python
    指令：python setup.py build | more
   
  7.如果安装成功，代码如下
    changing mode of build/scripts-2.7/ansible-vault from 644 to 755
    changing mode of build/scripts-2.7/ansible-config from 644 to 755
    changing mode of build/scripts-2.7/ansible-inventory from 644 to 755
  
  8.编译完成，执行安装
    命令：python setup.py install | more         --报错
  
  9.安装过程会自动下载ansible的依赖模块
    安装完成后提示：finished processing dependencies for ansible==2.5.3
    
  10.安装完成，调用ansible命令
    ansible --version
    
```



#### 3.**yum** 安装

下载python2,7

```
wget http://www.python.org/ftp/python/2.7.13/Python-2.7.13.tar.xz


xz -d Python-2.7.13.tar.xz

tar -xvf Python-2.7.13.tar

cd Python-2.7.13


./configure --prefix=/usr/local/python2.7 --with-threads --with-zlib=/usr/include --enable-shared
                 如果configure报警，执行yum install gcc-c++
./configure --prefix=/usr/local/python2.7 --with-zlib --enable-shared
./configure --enable-optimizations

make
make install

不明白软链是什么意思
mv /usr/bin/python /usr/bin/python2.4     --先把他移走
ln -s /usr/local/python2.7/bin/python /usr/bin/python
python --version
================第二个版本====================
创建安装路径
mkdir /usr/local/python2.7.6
编译
./configure --prefix=/usr/local/python2.7.6
安装
make
make install

===这个有什么用，我也不知道===
mv /usr/bin/python /usr/bin/python_bak
新版本的软链接
ln -s /usr/local/python2.7.6/bin/python2.7 /usr/bin/python
```

**ansible只能安装在linux系统中，主控机必须是linux，windows是可以作为控制的节点。**

```plsql
1.确保机器上安装的是 Python 2.6 或者 Python 2.7 版本：
  python --version
2.查看yum仓库中是否存在ansible的rpm包
  yum list|grep ansible  
  如果没有下载
   wget http://mirrors.aliyun.com/repo/epel-6.repo
3.安装ansible服务：
  yum install ansible -y
4.修改ansible配置和主机列表hosts 
   1)关闭第一次使用ansible连接客户端时输入命令提示：
    sed -i "s@\#host_key_checking = False@host_key_checking = False@g" /etc/ansible/ansible.cfg
    指定日志路径：
    sed -i "s@\#log_path = \/var\/log\/ansible.log@log_path = \/var\/log\/ansible.log@g" /etc/ansible/ansible.cfg
   2)将所有主机ip加入到/etc/ansible/hosts文件中：
     cat /etc/ansible/hosts      --查看IP和端口
     vim /etc/ansible/hosts      --编辑内容
         [app]
         192.168.38.128   ansible_user=root  ansible_port=22     
     cat  /etc/ansible/hosts     --查看是否添加成功
5.创建和配置 SSH 公钥认证（免密码登录）：
   ssh-keygen -t rsa
   一路回车   最好不要设密码


6.通过ansible将公钥分发至各主机上：
   ansible all -m authorized_key -a "user=root key='{{ lookup('file', '/root/.ssh/id_rsa.pub') }}' path=/root/.ssh/authorized_keys manage_dir=no" --ask-pass -c paramiko
7.修改ansible配置，指定私钥文件路径：
   sed -i "s@\#private_key_file = \/path\/to\/file@private_key_file = \/root\/.ssh\/id_rsa@g" /etc/ansible/ansible.cfg
8.测试:
   ansible all -m ping -vvvv
```

**登陆的密码**

```
账号：root
密码：zhaonan
```

#### 

#### 5.linux的文件目录结构

| 目录   | 应放置档案内容                                               |
| ------ | ------------------------------------------------------------ |
| /bin   | 系统有很多放置执行档的目录，但/bin比较特殊。因为/bin放置的是在单人维护模式下还能够被操作的指令。在/bin底下的指令可以被root与一般帐号所使用，主要有：cat,chmod(修改权限), chown, date, mv, mkdir, cp, bash等等常用的指令 |
| /boot  | 主要放置开机会使用到的档案，包括Linux核心档案以及开机选单与开机所需设定档等等。Linux kernel常用的档名为：vmlinuz ，如果使用的是grub这个开机管理程式，则还会存在/boot/grub/这个目录。 |
| /dev   | 在Linux系统上，任何装置与周边设备都是以档案的型态存在于这个目录当中。 只要通过存取这个目录下的某个档案，就等于存取某个装置。比要重要的档案有/dev/null, /dev/zero, /dev/tty , /dev/lp*, / dev/hd*, /dev/sd*等等 |
| /etc   | 系统主要的设定档几乎都放置在这个目录内，例如人员的帐号密码档、各种服务的启始档等等。 一般来说，这个目录下的各档案属性是可以让一般使用者查阅的，但是只有root有权力修改。 FHS建议不要放置可执行档(binary)在这个目录中。 比较重要的档案有：/etc/inittab, /etc/init.d/, /etc/modprobe.conf, /etc/X11/, /etc/fstab, /etc/sysconfig/等等。 另外，其下重要的目录有：/etc/init.d/ ：所有服务的预设启动script都是放在这里的，例如要启动或者关闭iptables的话： /etc/init.d/iptables start、/etc/init.d/ iptables stop/etc/xinetd.d/ ：这就是所谓的super daemon管理的各项服务的设定档目录。 |
| /home  | 这是系统预设的使用者家目录(home directory)。 在你新增一个一般使用者帐号时，预设的使用者家目录都会规范到这里来。比较重要的是，家目录有两种代号： ~ ：代表当前使用者的家目录，而 ~guest：则代表用户名为guest的家目录。 |
| /lib   | 系统的函式库非常的多，而/lib放置的则是在开机时会用到的函式库，以及在/bin或/sbin底下的指令会呼叫的函式库而已 。 什么是函式库呢？妳可以将他想成是外挂，某些指令必须要有这些外挂才能够顺利完成程式的执行之意。 尤其重要的是/lib/modules/这个目录，因为该目录会放置核心相关的模组(驱动程式)。 |
| /media | media是媒体的英文，顾名思义，这个/media底下放置的就是可移除的装置。 包括软碟、光碟、DVD等等装置都暂时挂载于此。 常见的档名有：/media/floppy, /media/cdrom等等。 |
| /mnt   | 如果妳想要暂时挂载某些额外的装置，一般建议妳可以放置到这个目录中。在古早时候，这个目录的用途与/media相同啦。 只是有了/media之后，这个目录就用来暂时挂载用了。 |
| /opt   | 这个是给第三方协力软体放置的目录 。 什么是第三方协力软体啊？举例来说，KDE这个桌面管理系统是一个独立的计画，不过他可以安装到Linux系统中，因此KDE的软体就建议放置到此目录下了。 另外，如果妳想要自行安装额外的软体(非原本的distribution提供的)，那么也能够将你的软体安装到这里来。 不过，以前的Linux系统中，我们还是习惯放置在/usr/local目录下。 |
| /root  | 系统管理员(root)的家目录。 之所以放在这里，是因为如果进入单人维护模式而仅挂载根目录时，该目录就能够拥有root的家目录，所以我们会希望root的家目录与根目录放置在同一个分区中。 |
| /sbin  | Linux有非常多指令是用来设定系统环境的，这些指令只有root才能够利用来设定系统，其他使用者最多只能用来查询而已。放在/sbin底下的为开机过程中所需要的，里面包括了开机、修复、还原系统所需要的指令。至于某些伺服器软体程式，一般则放置到/usr/sbin/当中。至于本机自行安装的软体所产生的系统执行档(system binary)，则放置到/usr/local/sbin/当中了。常见的指令包括：fdisk, fsck, ifconfig, init, mkfs等等。 |
| /srv   | srv可以视为service的缩写，是一些网路服务启动之后，这些服务所需要取用的资料目录。 常见的服务例如WWW, FTP等等。 举例来说，WWW伺服器需要的网页资料就可以放置在/srv/www/里面。呵呵，看来平时我们编写的代码应该放到这里了。 |
| /tmp   | 这是让一般使用者或者是正在执行的程序暂时放置档案的地方。这个目录是任何人都能够存取的，所以你需要定期的清理一下。当然，重要资料不可放置在此目录啊。 因为FHS甚至建议在开机时，应该要将/tmp下的资料都删除。 |

/usr里面放置的数据属于可分享的与不可变动的(shareable, static) 

/usr不是user的缩写，其实usr是Unix Software Resource的缩写， 也就是Unix操作系统软件资源所放置的目录，而不是用户的数据啦。这点要注意。 

| 目录          | 应放置的文件                                                 |
| ------------- | ------------------------------------------------------------ |
| /user/bin     | 绝大部分的用户可使用指令都放在这里。请注意到他与/bin的不同之处 |
| /usr/include/ | c/c++等程序语言的档头(header)与包含档(include)放置处，当我们以tarball方式 (*.tar.gz 的方式安装软件)安装某些数据时，会使用到里头的许多包含档。 |
| /usr/lib/     | 包含各应用软件的函式库、目标文件(object file)，以及不被一般使用者惯用的执行档或脚本(script)。 某些软件会提供一些特殊的指令来进行服务器的设定，这些指令也不会经常被系统管理员操作， 那就会被摆放到这个目录下啦。要注意的是，如果你使用的是X86_64的Linux系统， 那可能会有/usr/lib64/目录产生 |
| /usr/local/   | 统管理员在本机自行安装自己下载的软件(非distribution默认提供者)，建议安装到此目录， 这样会比较便于管理。举例来说，你的distribution提供的软件较旧，你想安装较新的软件但又不想移除旧版， 此时你可以将新版软件安装于/usr/local/目录下，可与原先的旧版软件有分别啦。 你可以自行到/usr/local去看看，该目录下也是具有bin, etc, include, lib...的次目录 |
| /usr/sbin/    | 非系统正常运作所需要的系统指令。最常见的就是某些网络服务器软件的服务指令(daemon) |
| /usr/share/   | 放置共享文件的地方，在这个目录下放置的数据几乎是不分硬件架构均可读取的数据， 因为几乎都是文本文件嘛。在此目录下常见的还有这些次目录：/usr/share/man：联机帮助文件     /usr/share/doc：软件杂项的文件说明     /usr/share/zoneinfo：与时区有关的时区文件 |
| /usr/src/     | 一般原始码建议放置到这里，src有source的意思。至于核心原始码则建议放置到/usr/src/linux/目录下。 |

#### 6.报警

```
1.no local packages or download links found for cryptography
  翻译：没有本地包或加密链接的下载链接
2.Cannot retrieve metalink for repository: epel. Please verify its path and try again
  解决方法： 一句话：把/etc/yum.repos.d/epel.repo，文件baseurl的注释放开，mirrorlist注释掉。

   打开/etc/yum.repos.d/epel.repo，将
      [epel]
      name=Extra Packages for Enterprise Linux 6 - $basearch
      #baseurl=http://download.fedoraproject.org/pub/epel/6/$basearch
      mirrorlist=https://mirrors.fedoraproject.org/metalink?repo=epel-6&arch=$basearch
   修改为
      [epel]
      name=Extra Packages for Enterprise Linux 6 - $basearch
      baseurl=http://download.fedoraproject.org/pub/epel/6/$basearch
      #mirrorlist=https://mirrors.fedoraproject.org/metalink?repo=epel-6&arch=$basearch
   再清理源，重新安装

   yum clean all
   yum install -y 需要的
   ==================
   进到配置文件中
   cd  /etc          ==进入文件夹
   cd  yum.repos.d   ==进到
   vi  epel.repo     ==修改文档
 3.安装完成，测试版本号报警
   sudo easy_install sh  （解决ImportError :No Module namesd sh问题）
   sudo yum install python-jinja2（解决Import error: No module named jinja2问题）
 4.设置hosts主机的时候，将公钥分布到主机上
   报警："msg": "Aborting, target uses selinux but python bindings (libselinux-python) aren't installed!"
   解决：yum install -y libselinux-python
 5.测试的时候报警unreachable
   ansible app -m ping -vvvv  --添加四个v会打印详细的输出，最后发现没有指定端口
 6.RuntimeError: cryptography requires setuptools 18.5 or newer, please upgrade to a newer version of setuptools
    解决： setuptool 太老了，更新下: pip install --upgrade setuptools
    
    =======ImportError: No module named sysconfig====
    此问题也有可能是 我升级的setuptools太新了，已经不再支持2.6版本的python
    其实在37版本之后就不再支持python 2.6了
    pip install setuptools==36.7.0
```

ansible是怎么样执行命令doa远程host主机呢，主要分为以下几个步骤

```
  1.加载配置文件默认/etc/ansible/ansible.cfg

  2.查找对应的主机配置文件，找到要执行的主机  #/etc/ansible目录下有个host文件

  3.加载对应命令的模块，如ping

  4.通过ansible将模块或命令生成对应的临时py文件传到远程host主机上

  5.对应执行用户家目录的.ansible/tmp/的.py为后缀的文件

  6.给x执行权限

  7.执行并返回结果

  8.删除临时.py后缀文件，sleep 0退出   #大致过程就是这几步 
```



#### 7.ansible之playbook（剧本）

**基本组件**

| 名称        | 备注                                              |
| ----------- | ------------------------------------------------- |
| hosts       | 运行执行任务（task）的目标主机                    |
| remote_user | 在远程主机上执行任务的用户                        |
| tasks       | 任务列表                                          |
| handlers    | 任务，与tasks不同的是只有在接受到通知时才会被触发 |
| templates   | 使用模板语言的文本文件，使用jinja2语法。          |
| variable    | 变量，变量替换{{ variable_name }}                 |



**模块参数**

| 名称                | 必选 | 默认值 | 可选值 | 备注                                                         |
| ------------------- | ---- | ------ | ------ | ------------------------------------------------------------ |
| backup              | no   | no     | yes/no | 在覆盖之前将原文件备份，备份文件包含时间信息                 |
| content             | no   |        |        | 当用content代替src参数的时候，可以把文档的内容设置到特定的值 |
| dest（destination） | yes  |        |        | 目标绝对路径。如果`src`是一个目录，`dest`也必须是一个目录。如果`dest`是不存在的路径，并且如果dest以`/`结尾或者src是目录，则dest被创建。如果`src`和`dest`是文件，如果`dest`的父目录不存在，任务将失败 |
| follow              | no   | no     | yes/no | 是否遵循目的机器中的文件系统链接                             |
| force               | no   | yes    | yes/no | 当内容不同于源时，将替换远程文件。设置为`no`，则只有在目标不存在的情况下才会传输文件 |
| group               | no   |        |        | 设置文件/目录的所属组，将被馈送到chown                       |
| local_follow        | no   | yes    | yes/no | 是否遵循本地机器中的文件系统链接                             |
| mode                | no   |        |        | 设置文件权限，模式实际上是八进制数字（如0644），少了前面的零可能会有意想不到的结果。从版本1.8开始，可以将模式指定为符号模式（例如u+rwx或u=rw,g=r,o=r） |
| owner               | no   |        |        | 设置文件/目录的所属用户，将被馈送到chown                     |
| romote_src          | no   | no     | yes/no | 如果yes它会从目标机上搜索src文件                             |
| src                 | no   |        |        | 将本地路径复制到远程服务器; 可以是绝对路径或相对的。如果是一个目录，它将被递归地复制。如果路径以`/`结尾，则只有该目录下内容被复制到目的地，如果没有使用`/`来结尾，则包含目录在内的整个内容全部复制 |
| umsafe_writes       | no   |        | yes/no | 是否以不安全的方式进行，可能导致数据损坏                     |
| validate            | no   | none   |        | 复制前是否检验需要复制目的地的路径                           |



##### 1.文件编写规则

```plsql
1、大小写敏感 
2、使用缩进表示层级关系 
3、禁止使用tab缩进，只能使用空格键 
4、缩进长度没有限制，只要元素对齐就表示这些元素属于一个层级。 
5、使用#表示注释 
6、字符串可以不用引号标注
=========== script  template ============
---                                                    --以---为文件开始，固定格式
- hosts: all                                           --指定ansible对象  注意- 后有一个空格
  remote_user: root                                    --远端用户
  tasks:                                               --具体任务：
    - name: install the latest version of Apache       --name为yum人物的描述信息
      yum:                                             #- 后面跟上调用的ansible模块，注意- 后有一个空格
        name: httpd
        state: installed
    - copy:
        remote_src: true                               --指定要拷贝的文件位于远端，默认拷贝本地文件
        src: /etc/httpd/conf/httpd.conf
        dest: /etc/httpd.conf
    - lineinfile:
        path: /etc/httpd/conf/httpd.conf
        regexp: '^Listen'
        line: 'Listen 8080'
    - service:
        name: httpd
        state: started
```

##### 2.入门

```
[root@node1 ~]# vi hello.yml 
[root@node1 ~]# cat hello.yml 
---
- hosts: node2,node3
  tasks:
     - name: "helloworld"
       shell: echo "Hello World" `date` by `hostname` > /tmp/hello.log
================template=====================
[root@node1 ~]# ansible-playbook hello.yml

PLAY [node2,node3] **************************************************************************************************************************************************************************

TASK [Gathering Facts] **********************************************************************************************************************************************************************
ok: [node3]
ok: [node2]

TASK [helloworld] ***************************************************************************************************************************************************************************
changed: [node3]
changed: [node2]

PLAY RECAP **********************************************************************************************************************************************************************************
node2                      : ok=2    changed=1    unreachable=0    failed=0   
node3                      : ok=2    changed=1    unreachable=0    failed=0   
```

如果有变量

```plsql
=========ansible_update.yml==========
---
- hosts: test                   ---- test是/etc/ansible/hosts 文件中配置的host组
  serial: 1                     ---- 可有可无，打开多个tty时，控制执行顺序使用
  vars:
     files_1: '{{ files_1 }}'   ----定义变量
     user_2: '{{ user_2 }}'
  remote_user: root             ----指定远端执行时的用户，agent部署时可有可无
  tasks:
   - name: copy files
     script: /root/test_ansible_pass_parameters.sh {{ files_1 }} {{ user_2 }}
     ignore_errors: yes         ----忽略shell脚本执行返回的结果。因playbook只把shell脚本返回值为 0 的情况当做 ok，其他值全当做 fail
     register: output           ----可有可无，用来记录playbook的stdout

   - debug: msg='{{ output.stdout_lines }}'    ----打印playbook的stdout
   - debug: msg='{{ output.stderr }}'          ----打印playbook的stderr
======execute======要携带参数=========
ansible-playbook ansible_update.yml -v --extra-vars "files_1=/root/test.txt user_2=192.168.38.128" 
```

如果要使用一个task实现复制多个文件 

```
---
- name: copy files
  copy:
    src: "{{ item }}"
    dest: "/tmp/"
    remote_src: no
  with_fileglob:
    - rke_linux-amd64
    - kubectl_amd64-linux
    - helm-linux.tar.gz
  tags: rancher
```

远程copy到远程

```
---
- hosts: all
  tasks:
    - name: 复制文件
      copy:
        src: /home/test.txt
        dest: "/home/test123.txt"
        remote_src: True
        owner: root
        group: root
        mode: 0644
```

本地拷贝到远程

```
---
- hosts: all
  tasks:
    - name: 复制文件
      copy:
        src: /home/test.txt
        dest: "/home/test123.txt"
        owner: root
        group: root
        mode: 0644
```

写入内容

```
---
- hosts：all
  tasks:
     - name: 写入文件
       copy: dest=/home/test.txt content="test123"
```

##### 3.高级操作

执行多个任务并且启动触发器

```plsql
---
- hosts: local
  connection: local
  become: yes
  become_user: root
  vars:
    - docroot: /var/www/serversforhackers.com/public
  tasks:
    - name: Add Nginx Repository           --第一个任务：添加ngins仓库
      apt_repository:
        repo: ppa:nginx/stable
        state: present
      register: ppastable

    - name: Install Nginx                  --第二个任务 安装
      apt:
        pkg: nginx
        state: installed
        update_cache: true
      when: ppastable|success
      notify:                              --notify：是触发handler，当安装成功后，触发handler动作
        - Start Nginx

    - name: Create Web Root                --第三个任务  创建web跟目录
      file:
       path: '{{ docroot }}'
       mode: 775
       state: directory
       owner: www-data
       group: www-data
      notify:                              --有一个notify，就会有一个handler，后面跟的是触发器的名字                         
        - Reload Nginx

   handlers:                               --触发器
     - name: Start Nginx      
       service:
         name: nginx
         state: started

     - name: Reload Nginx
       service:
         name: nginx
         state: reloaded
```

相关联的关键字

```plsql
1.notify。。。。handler
2.gather_facts:False     --后面就可以用debug
例：[root@LeoDevops playb]# cat p.yaml
    ---
    - hosts: all
      gather_facts: False
      vars:
        key: "Ansible"
      tasks:
        - name: display host variable from hostfile
          debug: msg=" {{inventory_hostname}} value is {{key}}"
        
```



##### 4.远程操控windows

```plsql
所有安装包：H:\share\src\winserver
(5)Windows系统配置
和Linux发版版稍有区别，远程主机系统如为Windows需预先如下配置：

安装Framework 3.0+
更改powershell策略为remotesigned
升级PowerShell至3.0+
设置Windows远端管理，英文全称WS-Management（WinRM）
--系统必须是SP1 如果不是需要打sp1补丁安装(windows6.1-KB976932-X64.exe)  目录H:\share\src\winserver
  （Win7 SP1 全称Windows 7 Service Pack 1，是Windows 7的一个服务补丁版本）
5.1 安装Framework 3.0+
下载链接为：http://download.microsoft.com/download/B/A/4/BA4A7E71-2906-4B2D-A0E1-80CF16844F5F/dotNetFx45_Full_x86_x64.exe。 下载至本地后双击左键安装即可，期间可能会多次重启，电脑需正常连接Internet

5.2更改powershell策略为remotesigned
set-executionpolicy remotesigned        --powershell执行 必须要管理员运行  输入y
get-executionpolicy                     --查看是否策略修改是否成功

5.3升级PowerShell至3.0+
Window 7和Windows Server 2008 R2默认安装的有PowerShell，但版本号一般为2.0版本，所以我们需升级至3.0+

get-host                               --查看版本

#执行upgrade_to_ps3.ps1 
复制upgrade_to_ps3.ps1到桌面 右键powershell执行，执行完后重启系统

5.4 设置Windows远端管理（WS-Management，WinRM）  [备注，window10默认powershell就是5.0+ 只要执行这里的和5.2更改powershell策略就行]

powershell下执行以下:
winrm enumerate winrm/config/listener  #winrm service 默认都是未启用的状态，先查看状态；如无返回信息，则是没有启动
winrm quickconfig  #针对winrm service 进行基础配置 输入两次：y 回去
winrm e winrm/config/listener #查看winrm service listener
winrm set winrm/config/service/auth '@{Basic="true"}'      #为winrm service 配置auth 
winrm set winrm/config/service '@{AllowUnencrypted="true"}' #为winrm service 配置加密方式为允许非加密
winrm set winrm/config '@{MaxEnvelopeSizekb="150"}' 
winrm set winrm/config '@{MaxTimeoutms ="60000"}'

好了，远程Windows主机配置到此结束，我们验证配置的是否有问题
更多winrm配置: https://www.cnblogs.com/weloveshare/p/5753139.html
```

##### 5.定义变量和引用

 第一种情况：playbook里面引用有变量的文件 

```plsql
[root@LeoDevops playb]# cat p.yaml
---
- hosts: all
  gather_facts: False                                              --debug有效
  vars_files:                                                      --变量
    - var.json
  tasks:
    - name: display host
      debug: msg="The {{ inventory_hostname  }} value is {{ key }}"
[root@LeoDevops playb]# cat var.json                               --查询
{"key":"json}                                                 
```

 第二种情况：直接在playbook中定义变量

```plsql
[root@LeoDevops playb]# cat p_vars.yaml 
---
- hosts: all
  gather_facts: False 
  vars:      
    key: "Ansible"                                                 --这种方式定义，key: value的形式
  tasks:
    - name: display host variables from hostfile
      debug: msg="The {{ inventory_hostname  }} value is {{ key }}"
```

第三种情况：hosts里面定义好变量

使用正则过滤：grep -vE '^#|^$' filename 

```plsql
[root@LeoDevops playb]# grep -vE "(^$|^#)" /etc/ansible/hosts          --使用正则过滤掉空格行和以#开头的行
192.168.93.132  key=132
192.168.93.137  key=137
[root@LeoDevops playb]# grep -vE "(^$|^#)" /etc/ansible/hosts    
[nginx]
192.168.93.132
192.168.93.137
[nginx:vars]
ansible_python_interpreter=/usr/bin/python2.6
key=nginx
=========下面是执行剧本============
[root@LeoDevops playb]# cat ansible_variable.yml 
---
- hosts: all
  gather_facts: False
  tasks:
    - name: Display Host Variable From Hostfile
      debug: msg="The {{ inventory_hostname  }} Value is {{ key  }}"
      
      
```

第四种情况，命令行传输

-e参数能将变量传入进去

```
[root@LeoDevops playb]# ansible-playbook ansible_variable.yml  -e "key=hehe"
```

#### 8.ansible几大常用模块

```
-m：指定调用的模块
-a：每个模块都有自己的专有参数，指定此模块的参数
-f：指定每批处理多少台机器，默认5台

ansible-doc  -h           #查看ansible文档的帮助信息
ansible-doc  -l           #列出ansible支持的所有模块信息，如上文只能中的ping模
ansible-doc -s command    #查看指定模块的参数信息，command是一种常用的模块，默认不指定模块时就是使用command
```



#####  1.ping

```
测试是否连接
ansible all -m ping
```

#####  2.commond模块

注意： 在command的命令中含有像`$ HOME'这样的变量和像``<“'，`”>“， `“”“”，“”;“”和“”＆“'将无法正常工作（如果需要这些功能，请使用[shell]模块） 

```
command ：作为ansible的默认模块，可以允许远程主机范围内的所有shell命令。
    chdir # 在执行命令之前，先切换到该目录
    creates # 一个文件名，当这个文件存在，则该命令不执行,可以用来做判断
    executable # 切换shell来执行命令，需要使用命令的绝对路径
    free_form # 要执行的Linux指令，一般使用Ansible的-a参数代替。
    removes # 一个文件名，这个文件不存在，则该命令不执行,与creates相反的判断  
    

-------------------------------------------------
ansible windows -m command -a "uptime"
[root@centos6 ansible]# ansible web -m command -a " cat 1.sh " 
[root@centos6 ansible]# ansible web -m command -a " creates="1.sh" cat 1.sh "
```

#####   3.sehll模块

 但是和command不同的是，此模块可以支持**命令管道**，同时还有另一个模块也具备此功能，raw

```
功能：执行远程主机的shell脚本文件

[root@localhost ~]# ansible all -m shell -a "/home/test.sh"
```

demo:

```
1.先在本地创建shell脚本
wim /tmp/rocketzhao_test.sh
!/bin/sh
date +%F_%H:%M:%S

chmod +x /tmp/rocketzhao_test.sh

2.将创建的脚本分发到远程
ansible windows -m copy -a "src=/tmp/rocketzhang_test.sh dest=/tmp/rocketzhang_test.sh owner=root group=root mode=0755"
3.远程执行
ansible windows -m shell -a "/tmp/rocketzhao_test.sh"
```



#####  4.copy模块

```plsql
功能： 实现主控端向目标主机copy文件。

[root@localhost ~]# ansible all -m copy -a "src=/home/test.sh dest=/tmp/ owner=root group=root mode=0755"    
--src 主控端文件位置
--dest 被控端目标位置
--owner 文件复制过去后的所有者
--group 文件复制过去后的所属组
--mode  文件的权限设定，执行a+x这种方式
--backup：拷贝文件时若目的文件存在，且内容不相同则先备份目的文件，然后进行拷贝（系统自动生成备份的文件名，贴心！）
--directory_mode:递归权限设置
--force：为yes强制覆盖（内容不同时，相同覆盖也没意义），为no时只有文件不存在才会拷贝。
--content：直接修改文件内容
--others；可以使用file模块的选项在此处可以使用
```

#####  5.file模块

```plsql
file模块主要用于远程主机上的文件操作，file模块包含如下选项： 
– force：需要在两种情况下强制创建软链接，一种是源文件不存在但之后会建立的情况下；另一种是目标软链接已存在,需要先取消之前的软链，然后创建新的软链，有两个选项：yes|no 
-- group：定义文件/目录的属组 
-- mode：定义文件/目录的权限 
-- owner：定义文件/目录的属主 
-- path：必选项，定义文件/目录的路径 
-- recurse：递归的设置文件的属性，只对目录有效 
-- src：要被链接的源文件的路径，只应用于state=link的情况 
-- dest：被链接到的路径，只应用于state=link的情况 
-- state： 
     directory：如果目录不存在，创建目录 
     file：即使文件不存在，也不会被创建 
     link：创建软链接 
     hard：创建硬链接 
     touch：如果文件不存在，则会创建一个新的文件，如果文件或目录已存在，则更新其最后修改时间 
     absent：删除目录、文件或者取消链接文件
```

#####  6.fetch模块

```
fetch：#从远程把文件拉到本地，

[root@centos6 u01]# ansible web -m fetch -a "src=/app/123.txt dest=./"
```

#####  7.yum模块

```
功能： 安装软件包。

[root@localhost ~]# ansible all -m yum -a "name=httpd state=latest disable_gpg_check=yes enablerepo=epel"
#name 包名
#state (Choices: present, installed, latest, absent, removed)[Default: present]
#disable_gpg_check:禁止gpg检查
#enablerepo：只启动指定的repo
```

#####  8.user/group模块

```plsql
先看user有哪些参数；
    comment：用户描述信息
    createhome；是否创建家目录
    group/groups：组和附加组
    home：指定家目录
    move_home：上面home指定家目录时可以移动家目录
    name；指定用户名
    password：指定密码（这个选项有点问题，有明白的可以评论回复我感谢大神。指定密码到shadow文件不经过加密，难道需要MD5加密之后在指定吗？没试过，在这赐教一波）
    shell：指定shell类型
    state：不指定为创建，指定absent为删除
    system：指定为系统用户，已存在不能指定。
    uid：指定uid

   现在看看group的参数：
    gid：指定gid
    name：管理组的名称
    state：状态默认创建，指定为absent未删除  #state=absent
    system：指定为系统组
    
 -------------------------------
[root@localhost ~]# ansible all -m user -a "name=jerry comment=' doubi jerry'"      --添加用户 
[root@localhost ~]# ansible all -m user -a "name=jerry state=absent remove=yes"     --删除用户
```

#####  9.script模块

```
[root@centos6 u01]# ansible web -m script -a "/bin/bash /app/123.sh"
```

#### 9.linux操作

```
1.将abc.txt 改为bcd.txt
  mv abc.txt bcd.txt
2.将 目录a改为b
  mv a b
3.将a.txt 移到b改成c.txt
  mv a.txt /b/c.txt
4.强制退出
  ctrl + c
5.返回上级目录
  cd ..
  
  
```



#### 10.测试

```
-m：指定调用的模块
-a：每个模块都有自己的专有参数，指定此模块的参数
```

#####   1.简单测试

```plsql
1.首先我们看一下，我们端口配置的有哪些
cat /etc/ansible/hosts                                      --查看端口的名称，我的名称是app                   
2.测试
ansible app -m command -a 'update' 
```

##### 2.查看远程主机的详细信息

```
ansible app -m setup
```

##### 3.查看远程主机的运行状态

```
ansible app -m ping
```

##### 4.file操作

​    关键字的注解：

```
force：需要在两种情况下强制创建软链接，一种是源文件不存在，但之后会建立的情况下，另一种是目标软链接已存在，需要先取消之前的链接，然后创建新的链接，有两个选项yes/no
group：定义文件/目录的属组
mode：定义文件/目录的权限
owner：定义文件/目录的属主
path：必选项，定义文件/目录的路径
recurse：递归设置文件的属性，只对目录有效
src：被链接的源文件路径，只应用于state=link的情况
dest：被链接到的路径，只应用于state=link的情况
state：
   directory：如果目录不存在，就创建目录
   file：即使文件不存在，也不会被创建
   link：创建软链接
   hard：创建硬链接
   touch：如果文件不存在，则会创建一个新的文件，如果文件或目录已存在，则更新其最后修改时间
   absent：删除目录、文件或者取消链接文件
```

  远程文件符号链接创建

```
ansible app -m file -a "src=/etc/resolv.conf dest=/tmp/resolv.conf state=link"
```

  远程文件信息查看

```
ansible app -m command -a "ls –al /tmp/resolv.conf"
```

远程文件符号链接删除

```
ansible app -m file -a "path=/tmp/resolv.conf state=absent"
```

##### 5.复制文件到远程主机

​    关键字的注解：

```
backup：在覆盖之前，将源文件备份，备份文件包含时间信息，有两个选项yes/no
content: 用于代替“src”，可以直接设定指定文件的值
dest：必选项，要将源文件复制到远程主机的绝对路径，如果源文件是一个目录，那么该路径也必须是个目录
directory_mode:递归设定目录的权限，默认为系统默认权限
force：如果目标主机包含该文件，但内容不同，如果设置为yes，则强制覆盖，如果为no。则只有当目标主机的目标位置不存在该文件时，才复制，默认为yes
others：所有的file模块里的选项都可以在这里使用
src：被复制到远程主机的本地文件，可以是绝对路径，也可以是相对路径。如果路径是一个目录，它将递归复制。在这种情况下，如果路径使用“/”来结尾，则只复制目录里的内容，如果没有使用“/”来结尾，则包含目录在内的整个内容全部复制，类似于rsync。
```

   测试：

```
复制到远程服务器
ansible app -m copy -a "src=/etc/ansible/ansible.conf dest=/tmp/ansible.cfg owner=root group=root mode=0644"
查看远程文件信息
ansible app -m command -a "ls -al /tmp/ansible.cfg"
```

##### 



##### 8.托管节点的要求

```
通常我们使用ssh与托管节点通信，默认使用sftp，如果sftp不可用，可在ansible.cfg配置文件中配置成scp的方式，在托管节点上也需要安装python2.4或者以上版本，如果低于python2.5，还需要安装一个模块
python-simplejson

注意：
1.没安装python-simplejson,也可以使用Ansible的”raw”模块和script模块,因此从技术上讲,你可以通过Ansible的”raw”模块安装python-simplejson,之后就可以使用Ansible的所有功能了.
2.如果托管节点上开启了SElinux,你需要安装libselinux-python,这样才可使用Ansible中与copy/file/template相关的函数.你可以通过Ansible的yum模块在需要的托管节点上安装libselinux-python.
```

##### 9.对管理主机的要求

```
只要机器上安装了 Python 2.6 或 Python 2.7 (windows系统不可以做控制主机),都可以运行Ansible.

主机的系统可以是 Red Hat, Debian, CentOS, OS X, BSD的各种版本,等等.
```



更多模块参考，

```
#ansible-doc –l

网站：
http://docs.ansible.com/modules_by_category.html
http://www.ansible.cn/docs/
```

学习资料

```html
http://blog.xiaorui.cc/category/ansible/

http://lixcto.blog.51cto.com/4834175/d-4

https://github.com/ansible/ansible-examples

http://rfyiamcool.blog.51cto.com/1030776/d-51

http://dl528888.blog.51cto.com/2382721/d-4/p-1

http://edu.51cto.com/course/course_id-2220.html

http://edu.51cto.com/course/course_id-2032.html

http://www.shencan.net/index.php/category/%e8%87%aa%e5%8a%a8%e5%8c%96%e8%bf%90%e7%bb%b4/ansible/
```



ansible-root;

```
打包war包
部署数据库脚本


```

#### 11.ansible管理windows

#####  1.windows下ansible工作模式

```
Ansible 从1.7+版本开始支持Windows，但前提是管理机必须为Linux系统，远程主机的通信方式也由SSH变更为PowerShell，同时管理机必须预安装Python的Winrm模块，方可和远程Windows主机正常通信，但PowerShell需3.0+版本且Management Framework 3.0+版本，实测Windows 7 SP1和Windows Server 2008 R2及以上版本系统经简单配置可正常与Ansible通信。简单总结如下：

（1）    管理机必须为Linux系统且需预安装Python Winrm模块
（2）    底层通信基于PowerShell，版本为3.0+，Management Framework版本为3.0+
（3）    远程主机开启Winrm服务

```

##### 2.重点：测试是否联通

```plsql
连接linux，是否联通
ansible linux -m ping
连接windows 是否联通                       --win_ping —Windows系统下的ping模块，常用来测试主机是否存活
ansible windows -m win_ping
```

##### 3.复制的playbook

```
---
- name: windows module example
  hosts: windows
  tasks:
     - name:move file on remote windows server from one location to another
       win_file: src=/etc/passwd dest=F:\file\passwd
```

也可以用命令直接复制

```
传输/etc/passwd文件至远程F:\file\目录下
ansible windows -m win_copy -a 'src=/etc/passwd dest=F:\file\passwd'
```

##### 4.windows server操作

```plsql
1.安装framework3.0+
http://download.microsoft.com/download/B/A/4/BA4A7E71-2906-4B2D-A0E1-80CF16844F5F/dotNetFx45_Full_x86_x64.exe。
2.修改powershell的策略
  先进到 powershell
  get-executionpolicy                                 --查询
  set-executionpolicy remotesigned                    --设置
3.查询powershell版本
  get-host
4.设置windows远端管理（ws-management，winrm）          --ansible主控端（一般为linux server）必须安装pywinrm，必须是0.2.2及以上版本
  winrm quickconfig
```

##### 5.windows被控制机满足2个条件：

```
1）必须开启WinRM支持远程管理；
2）确保PowerShell版本是3.0或更高版本
WinRM即windows远程管理，监听5985/5986端口
windows启动服务
PS C:\Users\Administrator> powershell Enable-PSRemoting -Force
在此计算机上设置了 WinRM 以接收请求。
在此计算机上设置了 WinRM 以进行远程管理。
```

```plsql
ansible主控端(linux server)必须条件：
   ansible主控端（一般为linux server）必须安装pywinrm，必须是0.2.2及以上版本

下载pip：
 1.wget "https://pypi.python.org/packages/source/p/pip/pip-1.5.4.tar.gz#md5=834b2904f92d46aaa333267fb1c922bb" --no-check-certificate
 2.tar -zxvf pip-1.5.4.tar.gz
 3.cd pip-1.5.4
 4.python setup.py install
 
 pip install isodate
 pip install xmlwitch
 
 --此时安装pip install pywinrm  会报警  please upgrade to a newer version of setuptools 跟新seruptools
 --更新到36.7.0
  pip install setuptools==36.7.0
  pip install pywinrm 
 
 
 =====================================
 2、在window服务器上的操作
   1）、用管理员权限打开windows powershell
   2）、首先查看winrm service的运行状态，默认情况是没有开启的；执行命令为空是没有启动。
    winrm enumerate winrm/config/listener

    // 快速在服务端运行winrm  
    winrm quickconfig                   
    // 查看winrm的运行情况  
    winrm e winrm/config/listener     
    // 查看winrm的配置。  这个过程中网络不能是公网的，会报错，根据提示自己搜索改动一下就好
    winrm get winrm/config 
    // 将service中的allowUnencrypted设置为true，允许未加密的通讯  
    winrm set winrm/config/service '@{AllowUnencrypted="true"}'   
    // 将client中的基本身份验证设置为true，允许  
    winrm set winrm/config/client/auth '@{Basic="true"}' 
    // 将client中的allowUnencrypted设置为true，允许未加密的通讯  
    winrm set winrm/config/client '@{AllowUnencrypted="true"}' 
    // 设置主机信任的客户端地址，这里可以填你所在的客户端机器的ip或者主机名  
    winrm set winrm/config/client '@{TrustedHosts="127.0.0.1, localhost, 192.168.3.109"}' 


```



#### 12.学习目标，实现功能

```
在部署過程中，主要有以下的場景，請參考：
  文件類(.war、.jar、.exe)：
  分發到一個地區的1臺Server上
  分發到一個地區的多臺Server上
  分發到不同地區的多臺Server上
數據庫腳本類（SQL、PL/SQL）：
  在一個地區DB Server的一個Schema中執行
  在一個地區DB Server的不同Schema中執行
  在不同地區DB Server的多個Schema中執行
服務啟動或調整配置類腳本(Web服務引擎啟動腳本、緩存服務器或MQ服務器啟動腳本)
  單一服務節點
  集群節點
```

##### 1.部署文件

```
schema是数据对象的集合，包括像表、视图、索引、同义词等等都可以说是schema的对象。
一般情况下，schema和user同名，user在schema里面建表，字段，存储工程，触发器。。。。
一个用户不能访问其他的schema。除非schema的用户给当前用户设置了权限。
```

Demo：ansible分发部署tomcat，整个war包tomcat-deploy.yml

```properties
编辑： cat tomcat-deploy.yml
---
- hosts: webservers
  vars:
    tomcat: /data/local/tomcat7
    remote_user: root
  tasks:
   - name: "创建备份目录backup"
     file: dest={{ tomcat }}/backup owner=root group=root state=directory mode=0755
     ignore_errors: True
   - name: "创建新war包所在的目录newwar"
     file:
       dest={{ tomcat }}/newwar owner=root group=root state=directory mode=0755
     ignore_errors: True
   - name: "备份旧的 war"
     shell: cp -r {{ tomcat }}/webapps/test.war  {{ tomcat }}/backup/test-`date '+%F'`.war
   - name: "拷贝新的war包到远程服务器"
     copy:
       src=/root/jenkins/workspace/tomcat_test/test.war
       dest={{ tomcat }}/newwar
   - name: "停止tomcat服务"
     shell: ps -ef | grep tomcat | grep {{ tomcat }} | grep -v grep | awk '{print $2}' | xargs kill -9
     tags: stop
     ignore_errors: True
   - name: "删除webapps下旧war包"
     file: dest={{ tomcat }}/webapps/test.war state=absent
   - name: "拷贝新的war包到webapps"
     shell: mv {{ tomcat }}/newwar/test.war {{ tomcat }}/webapps

   - name: "删除临时文件"
     shell: rm -rf {{ tomcat }}/temp/*
   - name: "删除项目缓存"
     shell: rm -rf {{ tomcat }}/work/Catalina/localhost/test
   - name: "启动tomcat服务"
     shell: nohup {{ tomcat }}/bin/startup.sh &
```

##### 2.ansible-playbook的常用参数命令

```plsql
-u REMOTE_USER, --user=REMOTE_USER  
＃ ssh 连接的用户名
 -k, --ask-pass    
＃ssh登录认证密码
 -s, --sudo           
＃sudo 到root用户，相当于Linux系统下的sudo命令
 -U SUDO_USER, --sudo-user=SUDO_USER    
＃sudo 到对应的用户
 -K, --ask-sudo-pass     
＃用户的密码（—sudo时使用）
 -T TIMEOUT, --timeout=TIMEOUT 
＃ ssh 连接超时，默认 10 秒
 -C, --check      
＃ 指定该参数后，执行 playbook 文件不会真正去执行，而是模拟执行一遍，然后输出本次执行会对远程主机造成的修改

 -e EXTRA_VARS, --extra-vars=EXTRA_VARS    
＃ 设置额外的变量如：key=value 形式 或者 YAML or JSON，以空格分隔变量，或用多个-e

 -f FORKS, --forks=FORKS    
＃ 进程并发处理，默认 5
 -i INVENTORY, --inventory-file=INVENTORY   
＃ 指定 hosts 文件路径，默认 default=/etc/ansible/hosts
 -l SUBSET, --limit=SUBSET    
＃ 指定一个 pattern，对- hosts:匹配到的主机再过滤一次
 --list-hosts  
＃ 只打印有哪些主机会执行这个 playbook 文件，不是实际执行该 playbook
 --list-tasks   
＃ 列出该 playbook 中会被执行的 task

 --private-key=PRIVATE_KEY_FILE   
＃ 私钥路径
 --step    
＃ 同一时间只执行一个 task，每个 task 执行前都会提示确认一遍
 --syntax-check  
＃ 只检测 playbook 文件语法是否有问题，不会执行该 playbook 
 -t TAGS, --tags=TAGS   
＃当 play 和 task 的 tag 为该参数指定的值时才执行，多个 tag 以逗号分隔
 --skip-tags=SKIP_TAGS   
＃ 当 play 和 task 的 tag 不匹配该参数指定的值时，才执行
 -v, --verbose   
＃输出更详细的执行过程信息，-vvv可得到所有执行过程信息。
```

##### 3.使用ansible-playbook实现自动打包上线

```
运行.sh文件
一、文件具备x权限       chmod u+x zhaonan.sh
 1.在任何路径下，输入绝对路径就可执行该文件
  /usr/local/zhaonan.yml
 2.进到文件目录下，执行
  cd /usr/local
  ./zhaonan.sh
  
二、文件不具备x权限  
  1.进到该文件=目录下
  cd /usr/local
  sh zhaonan.jsh
  2.在任意路径，sh加上文件路径和文件名
  sh /usr/local/zhaonan.yml
```



```properties
---
- hosts: xxxx
  remote_user: root
  tasks:
  - name: "192.168.55.14主机打包cms线上环境的war包"
    script: /data/script/build_xianshang_cms.sh

  - name: "从192.168.55.14拷贝war包到jenkins上"
    fetch:
      src=/buildwar/xianshang/cms/cms.war
      dest=/var/www/html/deploy/packages/cms/

- hosts: xianshang_01:xianshang_02
  remote_user: root
  tasks:
  - name: "从jenkins上传输到(xianshang_01),(xianshang_02)"
    copy:
      src=/var/www/html/deploy/packages/cms/192.168.55.14/buildwar/xianshang/cms/cms.war
      dest=/data/cms/war/cms.war

  - name: "得到cms进程号"
    command: APP_PID=$(ps -ef|grep cms|grep 'java'|awk '{print $2}')
  - name: "杀掉cms进程"
    command: kill $APP_PID 
  - name: "进入cms的bak目录"
    command: cd /data/cms/bak
  - name: "移动cms文件夹到/tmp目录下"
    command: mv cms /tmp/
  - name: "进入cms的webapps目录"
    command: cd /data/cms/webapps/
  - name: "移动cms文件夹到/data/cms/bak备份"
    command: mv cms /data/cms/bak/
  - name: "移动cms.war包到/tmp目录下"
    command: mv cms.war /tmp/
  - name: "进入/data/cms/war 线上包目录"
    command: cd /data/cms/war/
  - name: "将cms.war包移动到/data/cms/wepapps"
    command: mv cms.war /data/cms/webapps/
  - name: "进入/data/cms/bin"
    command: cd /data/cms/bin
  - name: "启动cms工程"
    command: bash startup.sh
```

##### 4.ansible-playbook 批量部署，更新war包，

```properties
前提是我已经有一个test.war在tomcat的webapps里面

- name: install tomcat admin 
  hosts: all
  sudo: True
  vars:
    war_file: /root/test.war 
    tomcat_root: /datas/tomcat/webapps/test
  tasks:
    - name: 1 stop tomcat.
      action: shell nohup {{ tomcat_root }}/../../bin/shutdown.sh                1...返回到上级目录
    - name: 2 bakconfig.sh 3ge /datas/scritps/bakconfig*/
      shell: /datas/scritps/bakconfig/bakconfig.sh                               2.不明白这个配置问价是干嘛用的
    - name: 3 rm rf test.
      file:
        state: absent                                                            3.移除解压后的test
        dest: "{{ tomcat_root }}"
    - name: 4 rm rf test.war
      file:                                                                      4.移除源test.war
        state: absent
        dest: "/datas/tomcat/webapps/test.war"
    - name: 5 from 23/root/war copy to /datas/soft/tomcat/webapps.               5.把新的war包复制到tomcat/webapps/       
      copy:
        src: "{{ war_file }}"
        dest: "/datas/tomcat/webapps/"
    - name: 6 waitting copy war.                                                 6.睡眠
      shell: sleep 15
    - name: 7 start tomcat.                                                      7.启动tomcat
      action: shell nohup /datas/tomcat/bin/startup.sh
    - name: 8 waitting webapps/test.
      shell: sleep 1                                                             8.睡眠
    - name: 9 stop tomcat.
      action: shell nohup {{ tomcat_root }}/../../bin/shutdown.sh                9.停止tomcat
    - name: 10 bakconfig2.sh 2ge wenjian
      shell: /datas/scritps/bakconfig/bakconfig2.sh                              10.启动backconfig.sh文件
    - name: 11 start tomcat.
      action: shell nohup {{ tomcat_root }}/../../bin/startup.sh                 11.启动tomcat 
时间和保存文件可以根据自己具体要求再修改
```

##### 5.最全的部署tomcat

 1.三个主机

```
# vim /etc/ansible/hosts
[javaserver]
192.168.30.128
192.168.30.129
192.168.30.130
```

2.创建管理目录   使用roles

```
mkdir -p tomcat/roles/tomcat_install/{files,handlers,meta,tasks,templates,vars}        在/etc/ansible下面
-p   确保目录的名称存在，不存在就创建一个
-----------目录结构-------------
├── roles
│   └── tomcat_install
│       ├── files
│       │   ├── apache-tomcat-8.5.39.tar.gz
│       │   └── jdk-8u191-linux-x64.tar.gz
│       ├── handlers
│       ├── meta
│       ├── tasks
│       │   ├── copy.yml
│       │   ├── install.yml
│       │   ├── main.yml
│       │   └── prepare.yml
│       ├── templates
│       │   ├── jdk_PATH
│       │   └── tomcat
│       └── vars
│           └── main.yml
└── tomcat.yml

```

3.编写tomcat_install文件

```
# vim tomcat.yml 

#用于批量安装Tomcat
- hosts: javaserver
  remote_user: root
  gather_facts: True

  roles:
    - tomcat_install
```

4.配置变量，

```
# vim roles/tomcat_install/vars/main.yml

#定义tomcat安装中的变量   
JDK_VER: 191
TOMCAT_VER: 8.5.39
TOMCAT_VER_MAIN: "{{ TOMCAT_VER.split('.')[0] }}"
DOWNLOAD_URL: https://mirrors.shu.edu.cn/apache/tomcat/tomcat-{{ TOMCAT_VER_MAIN }}/v{{ TOMCAT_VER }}/bin/apache-tomcat-{{ TOMCAT_VER }}.tar.gz

TOMCAT_URL: tomcat
TOMCAT_PORT: 8080
SOURCE_DIR: /software
JDK_DIR: /usr/local/jdk
TOMCAT_DIR: /usr/local/tomcat
DATA_DIR: /data/tomcat
```

5.tomcat启动脚本

```
# vim roles/tomcat_install/templates/tomcat
1
#!/bin/sh
# chkconfig: 345 99 10
# description: Auto-starts tomcat
# /etc/init.d/tomcatd
# Tomcat auto-start
# Source function library.
#. /etc/init.d/functions
# source networking configuration.
#. /etc/sysconfig/network
prog="tomcat"
RETVAL=0

CATALINA_HOME={{ TOMCAT_DIR }}

start()
{
        if [ -f $CATALINA_HOME/bin/startup.sh ];
          then
            echo $"Starting $prog"
                $CATALINA_HOME/bin/startup.sh
            RETVAL=$?
            echo " OK"
            return $RETVAL
        fi
}
stop()
{
        if [ -f $CATALINA_HOME/bin/shutdown.sh ];
          then
            echo $"Stopping $prog"
                $CATALINA_HOME/bin/shutdown.sh
            RETVAL=$?
            #sleep 1
            ps -ef |grep $CATALINA_HOME |grep -v grep |grep -v PID | awk '{print $2}'|xargs kill -9
            echo " OK"
            # [ $RETVAL -eq 0 ] && rm -f /var/lock/...
            return $RETVAL
        fi
}
case "$1" in
 start)
        start
        ;;
 stop)
        stop
        ;;
 restart)
         echo $"Restaring $prog"
         $0 stop && sleep 1 && $0 start
         ;;
 *)
        echo $"Usage: $0 {start|stop|restart}"
        exit 1
        ;;
esac
exit $RETVAL
```

6.环境准备

```
 vim roles/tomcat_install/tasks/prepare.yml

- name: 关闭firewalld 
  service: name=firewalld state=stopped enabled=no
  
- name: 临时关闭 selinux
  shell: "setenforce 0"
  failed_when: false

- name: 永久关闭 selinux
  lineinfile:
    dest: /etc/selinux/config
    regexp: "^SELINUX="
    line: "SELINUX=disabled"

- name: 添加EPEL仓库
  yum: name=epel-release state=latest

- name: 安装常用软件包
    yum:
      name:
        - vim
        - lrzsz
        - net-tools
        - wget
        - curl
        - bash-completion
        - rsync
        - gcc
        - unzip
        - git
      state: latest

- name: 更新系统
  shell: "yum update -y"
  args:
    warn: False
```

7.文件拷贝copy.yml

```
 vim roles/tomcat_install/tasks/copy.yml

- name: 创建software目录
  file: name={{ SOURCE_DIR }} state=directory mode=0755 recurse=yes

#本地files目录下要准备好jdk包
- name: 拷贝jdk包
  copy: src=jdk-8u{{ JDK_VER }}-linux-x64.tar.gz dest={{ SOURCE_DIR }} owner=root group=root

- name: 解压jdk包
  unarchive: src={{ SOURCE_DIR }}/jdk-8u{{ JDK_VER }}-linux-x64.tar.gz dest={{ SOURCE_DIR }} owner=root group=root

- name: 目录重命名
  shell: "if [ ! -d {{ JDK_DIR }} ]; then mv {{ SOURCE_DIR }}/jdk1.8.0_{{ JDK_VER }}/ {{ JDK_DIR }}; fi"

- name: 拷贝环境变量jdk_PATH
  template: src=jdk_PATH dest={{ SOURCE_DIR }} owner=root group=root

- name: 加入环境变量jdk_PATH
  shell: "if [ `grep {{ JDK_DIR }}/bin /etc/profile |wc -l` -eq 0 ]; then cat {{ SOURCE_DIR }}/jdk_PATH >> /etc/profile && source /etc/profile; fi"

```

8.下载安装

```
vim roles/tomcat_install/tasks/install.yml

#当前主机下没有tomcat包
- name: 下载tomcat包
  get_url: url={{ DOWNLOAD_URL }} dest={{ SOURCE_DIR }} owner=root group=root

#当前主机files目录下已有tomcat包
#- name: 拷贝现有tomcat包到所有主机
#  copy: src=apache-tomcat-{{ TOMCAT_VER }}.tar.gz dest={{ SOURCE_DIR }}

- name: 解压tomcat包
  unarchive: src={{ SOURCE_DIR }}/apache-tomcat-{{ TOMCAT_VER }}.tar.gz dest={{ SOURCE_DIR }} owner=root group=root

- name: 目录重命名
  shell: "if [ ! -d {{ TOMCAT_DIR }} ]; then mv {{ SOURCE_DIR }}/apache-tomcat-{{ TOMCAT_VER }}/ {{ TOMCAT_DIR }}; fi"

- name: 加入环境变量_1
  lineinfile:
    dest: "{{ TOMCAT_DIR }}/bin/catalina.sh"
    insertbefore: "cygwin=false"
    line: "CATALINA_HOME={{ TOMCAT_DIR }}"

- name: 加入环境变量_2
  lineinfile:
    dest: "{{ TOMCAT_DIR }}/bin/catalina.sh"
    insertbefore: "cygwin=false"
    line: "JAVA_HOME={{ JDK_DIR }}"

- name: 加入环境变量_3
  lineinfile:
    dest: "{{ TOMCAT_DIR }}/bin/catalina.sh"
    insertbefore: "cygwin=false"
    line: "JRE_BIN={{ JDK_DIR }}/bin"

- name: 加入环境变量_4
  lineinfile:
    dest: "{{ TOMCAT_DIR }}/bin/catalina.sh"
    insertbefore: "cygwin=false"
    line: "JRE_HOME={{ JDK_DIR }}/jre"

- name: 加入环境变量_5
  lineinfile:
    dest: "{{ TOMCAT_DIR }}/bin/catalina.sh"
    insertbefore: "cygwin=false"
    line: "CLASSPATH={{ JDK_DIR }}/jre/lib:{{ JDK_DIR }}/lib:{{ JDK_DIR }}/jre/lib/charsets.jar"

- name: 拷贝tomcat启动脚本_1
  template: src=tomcat dest=/usr/bin/ owner=root group=root mode=0755

- name: 拷贝tomcat启动脚本_2
  template: src=tomcat dest=/etc/init.d/ owner=root group=root mode=0755

- name: 启动tomcat并开机启动
  service:
    name: tomcat
    state: restarted
    enabled: yes

```

9.引用=文件，最后测试

```
# vim roles/tomcat_install/tasks/main.yml

#引用prepare、copy、install模块
- include: prepare.yml
- include: copy.yml
- include: install.yml


# ansible-playbook tomcat.yml
```

##### 6.ansible批量部署nginx、mysql、tomcat

1.编辑主机

```
vi /etc/ansible/hosts

[linux]
192.168.38.134 ansible_user=root ansible_port=22

[windows]
10.136.59.225       
[windows:vars]
ansible_user='DELTA\TONY.IC.LIN'
ansible_password=xxxxx
ansible_port=5986
ansible_connection=winrm
ansible_winrm_server_cert_validation=ignore
ansible_winrm_scheme='https'
ansible_winrm_transport=ntlm

[hao]
10.149.77.60      
[hao:vars]
ansible_user='DELTA\HAO.ZHAO'
ansible_password=xxxxxx
ansible_port=5985
ansible_connection=winrm
ansible_winrm_server_cert_validation=ignore
ansible_winrm_scheme='http'
ansible_winrm_transport=ntlm
```

2.构建roles

```
mkdir -p /usr/script/test/roles/{nginx,mysql,tomcat,db}/{defaults,files,handlers,meta,tasks,templates,vars}
创建了四个角色nginx,mysql,tomcat,db
```

3.树型结构

```
tree /usr/script/test/roles/
├── db
│ ├── defaults
│ ├── files
│ │ └── stu.sql       #要导入的sql
│ ├── handlers
│ ├── meta
│ ├── tasks
│ │ └── main.yml     #创建数据库和导入sql
│ ├── templates
│ └── vars
├── mysql
│ ├── defaults
│ ├── files
│ │ ├── mysql-5.6.27.tar.gz
│ │ └── mysql_install.sh       #mysql源码安装脚本
│ ├── handlers
│ ├── meta
│ ├── tasks
│ │ └── main.yml               #安装mysql
│ ├── templates
│ └── vars
├── nginx
│ ├── defaults
│ ├── files
│ │ ├── index.html             #nginx测试主页面
│ │ ├── install_nginx.sh       #nginx安装脚本
│ │ ├── nginx-1.8.0.tar.gz
│ │ ├── nginx.conf             #nginx主配置文件
│ │ └── test.conf              #nginx测试虚拟主机配置文件
│ ├── handlers
│ ├── meta
│ ├── tasks
│ │ └── main.yml               #安装nginx
│ ├── templates
│ └── vars
└── tomcat
├── defaults
├── files
│ ├── apache-tomcat-7.0.29.tar.gz
│ ├── install_java.sh        #java安装脚本
│ ├── install_tomcat.sh      #tomcat安装脚本
│ ├── jdk1.7.0_79.tar.gz
│ ├── server.xml             #tomcat配置文件
│ └── start.sh               #tomcat服务启动脚本
├── handlers
├── meta
├── tasks
│ └── main.yml               #安装java、tomcat
├── templates
└── vars
```

4.创建playbook

```
vi /usr/script/test/install.yml

hosts: hao                                             hosts: hao，windows
remote_user: root                                      remote_user: root
roles:                                                 roles:  
    nignx                                                 nignx
                                                          mysql
                                                          tomcat
                                                          db
```

5.编写tasks

```
1.先安装mysql_db模块
     python2下：pip intall MySql-python
     python3下：pip install mysqlclient

2.安装过程报警可能是需要编译
     yum install mysql-devel

3.然后就可以使用mysql_db模块
```



```
/usr/script/test/roles/db/tasks/main.yml

name: create db
mysql_db:
  name=student 
  state=present 
  login_password=root 
  login_user=root 
  login_unix_socket=/data/mysql/data/mysql.sock
  
name: copy sql file
copy: src=stu.sql dest=/tmp
name: import sql
mysql_db: 
  name=student 
  state=import 
  target=/tmp/stu.sql 
  login_password=bingoclo123 
  login_user=root 
  login_unix_socket=/data/mysql/data/mysql.sock
```



```
/usr/script/test/roles/db/files/stu.sql
 create table profile(name varchar(20),age int);
 insert into profile(name,age) values('张三',12);
```



```
usr/script/test/roles/nginx/tasks/main.yml

name: copy nginx_tar_gz to client
copy: src=nginx-1.8.0.tar.gz dest=/tmp/nginx-1.8.0.tar.gz
name: copy install_shell to client
copy: src=install_nginx.sh dest=/tmp/install_nginx.sh
name: copy nginx.conf to client
copy: src=nginx.conf dest=/tmp/nginx.conf
name: copy test.conf to client
copy: src=test.conf dest=/tmp/test.conf
name: copy index.html to client
copy: src=index.html dest=/tmp/index.html
name: install nginx
shell: /bin/bash /tmp/install_nginx.sh
```



```
/ansible/roles/mysql/tasks/main.yml

name: copy mysql_tar_gz to client
copy: src=mysql-5.6.27.tar.gz dest=/tmp/mysql-5.6.27.tar.gz
name: copy install_script to client
copy: src=mysql_install.sh dest=/tmp/mysql_install.sh owner=root group=root mode=755
name: install mysql
shell: /bin/bash /tmp/mysql_install.sh
```



```
/ansible/roles/tomcat/tasks/main.yml

name: install java
yum: name=java-1.7.0-openjdk state=present
name: group
group: name=tomcat
name: user
user: name=tomcat group=tomcat home=/usr/tomcat
sudo: True
name: copy tomcat_tar_gz
copy: src=apache-tomcat-7.0.65.tar.gz dest=/tmp/apache-tomcat-7.0.65.tar.gz
name: Extract archive
command: /bin/tar xf /tmp/apache-tomcat-7.0.65.tar.gz -C /opt/
name: Symlink install directory
file: src=/opt/apache-tomcat-7.0.65/ dest=/usr/share/tomcat state=link
name: Change ownership of Tomcat installation
file: path=/usr/share/tomcat/ owner=tomcat group=tomcat state=directory recurse=yes
name: Configure Tomcat users
template: src=tomcat-users.xml dest=/usr/share/tomcat/conf/
notify: restart tomcat
name: Install Tomcat init script
copy: src=catalina.sh dest=/etc/init.d/tomcat mode=0755
name: Start Tomcat
service: name=tomcat state=started enabled=yes
```



```
/ansible/roles/nginx/files/install_nginx.sh
#!/bin/bash
yum -y install zlib zlib-devel openssl openssl-devel pcre-devel gcc pcre pcre-devel automake GeoIP GeoIP-devel GeoIP-data
groupadd -r nginx
useradd -s /sbin/nologin -g nginx -r nginx
cd /tmp
tar xf nginx-1.8.0.tar.gz;cd nginx-1.8.0
mkdir /var/run/nginx/;chown nginx.nginx /var/run/nginx/
./configure \
--prefix=/usr/local/nginx \
#--sbin-path=/usr/sbin/nginx \
#--conf-path=/etc/nginx/nginx.conf \
#--error-log-path=/var/log/nginx/error.log \
#--pid-path=/var/run/nginx/nginx.pid \
--user=nginx \
--group=nginx \
--with-http_ssl_module \
--with-stream \
--with-http_stub_status_module \
--with-http_v2_module \
--with-http_gzip_static_module \
--with-ipv6 \
--with-http_sub_module \
--with-http_flv_module \
--with-http_geoip_module \
--with-pcre
make && make install
#sed "/^\sindex / i proxy_pass http://localhost:8080;" /etc/nginx/nginx.conf
/bin/rm -f /usr/local/nginx/conf/nginx.conf
/bin/cp /tmp/nginx.conf /usr/local/nginx/conf/nginx.conf
/bin/mkdir -p /usr/local/nginx/conf/extra
/bin/cp /tmp/test.conf /usr/local/nginx/conf/extra/
/usr/local/nginx/sbin/nginx &
```



```
/ansible/roles/mysql/files/mysql_install.sh
#!/bin/bash```

DBDIR='/data/mysql/data'
PASSWD='bingoclo123'
[ -d $DBDIR ] || mkdir $DBDIR -p
yum install cmake make gcc-c++ bison-devel ncurses-devel -y
id mysql &> /dev/null
if [ $? -ne 0 ];then
useradd mysql -s /sbin/nologin -M
fi
chown -R mysql.mysql $DBDIR
cd /tmp/
tar xf mysql-5.6.27.tar.gz
cd mysql-5.6.27
cmake . -DCMAKE_INSTALL_PREFIX=/usr/local/mysql \
-DMYSQL_DATADIR=$DBDIR \
-DMYSQL_UNIX_ADDR=$DBDIR/mysql.sock \
-DDEFAULT_CHARSET=utf8 \
-DEXTRA_CHARSETS=all \
-DENABLED_LOCAL_INFILE=1 \
-DWITH_READLINE=1 \
-DDEFAULT_COLLATION=utf8_general_ci \
-DWITH_EMBEDDED_SERVER=1
if [ $? != 0 ];then
echo "cmake error!"
exit 1
fi
make && make install
if [ $? -ne 0 ];then
echo "install mysql is failed!" && /bin/false
fi
sleep 2
ln -s /usr/local/mysql/bin/* /usr/bin/
cp -f /usr/local/mysql/support-files/my-default.cnf /etc/my.cnf
cp -f /usr/local/mysql/support-files/mysql.server /etc/init.d/mysqld
chmod 700 /etc/init.d/mysqld
/usr/local/mysql/scripts/mysql_install_db --basedir=/usr/local/mysql --datadir=$DBDIR --user=mysql
if [ $? -ne 0 ];then
echo "install mysql is failed!" && /bin/false
fi
/etc/init.d/mysqld start
if [ $? -ne 0 ];then
echo "install mysql is failed!" && /bin/false
fi
chkconfig --add mysqld
chkconfig mysqld on
/usr/local/mysql/bin/mysql -e "update mysql.user set password=password('$PASSWD') where host='localhost' and user='root';"
/usr/local/mysql/bin/mysql -e "update mysql.user set password=password('$PASSWD') where host='127.0.0.1' and user='root';"
/usr/local/mysql/bin/mysql -e "delete from mysql.user where password='';"
/usr/local/mysql/bin/mysql -e "flush privileges;"
if [ $? -eq 0 ];then
echo "ins_done"
fi
```

最后执行 ansible-playbook install.yml

##### 7.批量安装musql

```
1.构建roles结构：
mkdir -p roles/mysql_install/{files,handlers,meta,tasks,templates,vars}
2.文件mysql.yml和roles同一级
 [root@test  ansible]# vim mysql.yml
 - hosts: test
   remote_user: root
   gather_facts: False
   roles:
     - mysql_instal
```

##### 8.tomcat部署jar

```
首先，把jar包放在webapp下面
然后java -jar xxx.jar
   一直运行的话：nohup java -jar xxx.jar
最后访问xxxx:8080/xxxx/base   
```

环境变量

```
export JAVA_HOME=/usr/software/jdk1.8.0_211
export JRE_HOME=${JAVA_HOME}/jre
```



#### 13.远程控制

```
---
- hosts: xxx
  gather_facts:no       --关闭自动采集

```

##### 控制豪的电脑

```
启动tomcat报警：
 1.'utf8' codec can't decode byte 0xc3 in position 85: invalid continuation
   方法：/usr/local/python2.7/lib/python2.7/encodings/utf-8.py
   
   def decode(input, errors='strict'):       改成ignore
    return codecs.utf_8_decode(input, errors, True)
```



#### 14.操作数据库

##### 1.这个是什么意思，都是置顶的。

```
而且都是在template中使用，置顶
#!/usr/bin/env bash        #在不同的系统上提供了一些灵活性。
#!/usr/bin/bash            #将对给定的可执行文件系统进行显式控制。

大部分情况下，/usr/bin/env是优先选择的，因为它提供了灵活性，特别是你想在不同的版本下运行这个脚本；而指定具体位置的方式#!/usr/bin/bash，在某些情况下更安全，因为它限制了代码注入的可能。
```

##### 2.create DB user

```
playbook调用roles

---
- hosts: local
  gather_facts: yes
  vars: 
    PATH_TO_TARGET_NAMING_RULE: "{{ lookup('vars', 'cli_path_to_target_naming_rule', default= lookup('vars', 'inventory_dir') + '/02-GenDDL/roles/build/defaults/main.yml') }}"
    DB_USER: "{{ lookup('vars', 'cli_new_db_user') }}" 
  roles:
    - { role: build} # build artifacts and put into PATH_TO_TARGET_NAMING_RULE
```

   /roles/build/file，tasks，template，default

```
default/main.yml

---
#Connection Level Settings
DB_HOST : 10.1.2.3  
SYS_USER : SYS  
SYS_PASSWORD : sys123  
DB_PASSWORD : mesdb123 
DB_SCHEMA : MES
```

```
tasks/main.yml

---
- name: location of inventory_dir
  debug: 
    msg: "{{ inventory_dir }}"


- name: "Setting Fact Deployment Folder {{ deployment_path_dbusers }}"
  set_fact: 
    deployment_path_dbusers : "{{ inventory_dir }}/deployment-dbusers"    

- name: generate create sql files
  template:
    src: "{{ item }}"
    dest: "{{ deployment_path_dbusers }}/{{ item | basename | regex_replace('{{DB_USER}}',lookup('vars', 'DB_USER') ) | regex_replace('.j2','') }}"
  with_fileglob:
    - "../templates/*.sql.j2"   

- name: copy utility files/scripts
  template:
    src: "{{ item }}"
    dest: "{{ deployment_path_dbusers }}/{{ item | basename | regex_replace('{{DB_USER}}',lookup('vars', 'DB_USER') ) | regex_replace('.j2','') }}"
    mode: 0755
  with_fileglob:
    - "../templates/*.sh.j2"        

- name: "This command will create the database user {{ deployment_path_dbusers }}"
  command: ./reset-remote-users.sh
  args:
    chdir: "{{ deployment_path_dbusers }}"
  register: runoutput_create_dbusers

- name: debug message - runoutput_create_dbusers
  debug: 
    msg: "{{ runoutput_create_dbusers | to_nice_json(indent=2) }}"   
```

```
templates/两个sql ，一个sh文件

========sh文件======
#!/usr/bin/env bash

#set path to sqlplus
export PATH=../bin/instantclient_19_3:$PATH
export LD_LIBRARY_PATH="$PATH":$LD_LIBRARY_PATH

#set encoding code
export NLS_LANG=.AL32UTF8

exit | sqlplus -S "{{ SYS_USER }}/{{ SYS_PASSWORD }}@{{ DB_HOST }}/xe" as sysdba @create-users.sql

=========sql文件======
drop user {{ DB_USER }};
drop tablespace {{ DB_USER }}_tablespace;
drop tablespace {{ DB_USER }}_tablespace_temp;
create tablespace {{ DB_USER }}_tablespace datafile '{{ DB_USER }}_tabspace_dev.dat' size 10M REUSE autoextend on;
create temporary tablespace {{ DB_USER }}_tablespace_temp tempfile '{{ DB_USER }}_tabspace_temp_dev.dat' size 5M REUSE autoextend on;
CREATE USER {{ DB_USER }} IDENTIFIED BY {{ DB_PASSWORD }} default tablespace {{ DB_USER }}_tablespace temporary tablespace {{ DB_USER }}_tablespace_temp;
GRANT UNLIMITED TABLESPACE, CONNECT, RESOURCE, CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE SYNONYM, CREATE SEQUENCE, CREATE PROCEDURE, CREATE TRIGGER TO {{ DB_USER }};
exec dbms_goldengate_auth.grant_admin_privilege('{{ DB_USER }}');
grant select any dictionary to {{ DB_USER }};
grant insert any table to {{ DB_USER }};
grant update any table to {{ DB_USER }};
grant delete any table to {{ DB_USER }};

=========sql文件======
SET ECHO OFF
SET TERMOUT ON
@create-user-{{DB_USER}}.sql
```

##### 3.GenNamingRole

```
playbook调用roles
---
- hosts: local
  gather_facts: yes
  vars: 
    PATH_TO_TARGET_NAMING_RULE: "{{ lookup('vars', 'cli_path_to_target_naming_rule', default= lookup('vars', 'inventory_dir') + '/02-GenDDL/roles/build/defaults/main.yml|'+lookup('vars', 'inventory_dir') + '/02-GenDML/roles/build/defaults/main.yml', wantlist=False) }}"
    DB_USER: "{{ lookup('vars', 'cli_db_user') }}"
    DB_HOST: "{{ lookup('vars', 'cli_db_host') }}"
    DB_SCHEMA: "{{ lookup('vars', 'cli_db_schema') }}"
    DB_PASSWORD: "{{ lookup('vars', 'cli_db_password') }}"
  roles:
    - { role: build} # build artifacts and put into PATH_TO_TARGET_NAMING_RULE
```

/roles/build/file，tasks，templates，default

```
===default/main.yml====
---
#Connection Level Settings
DB_HOST : mesdb  
DB_USER : mesuser  
DB_PASSWORD : mesdb123 
DB_SCHEMA : MES

===tasks/main.yml=======
---
- name: location of inventory_dir
  debug: 
    msg: "{{ inventory_dir }}"

- name: ensure file exists
  template:
    src: "../files/dd_header.yml"
    dest: "{{ item }}"
    force: yes
    mode: 0644
  with_items:
  - "{{ PATH_TO_TARGET_NAMING_RULE.split('|') }}"        

- name: esssnsure file exists
  blockinfile:
    path: "{{ PATH_TO_TARGET_NAMING_RULE.split('|')[0] }}"
    block: |
      COLUMN_WORD_{{ item.split(':')[0] }} : "{{ item.split(':')[1] }}"
      COMMENT_WORD_{{ item.split(':')[0] }} : "{{ item.split(':')[2] }}"
      COLUMN_TYPE_{{ item.split(':')[0] }} : "{{ item.split(':')[3] }}"
    marker: "# {mark} BLOCK ({{ item.split(':')[0] }})"
  with_lines: 
    - "cat {{ role_path }}/files/dd_columns.csv" 
    
 ===files/dd_header.yml======
#dd_header: Connection Level Settings
DB_HOST : {{ DB_HOST }}  
DB_USER : {{ DB_USER }}  
DB_PASSWORD : {{ DB_PASSWORD }}  
DB_SCHEMA : {{ DB_SCHEMA }}  

#dd_header: Filename Level Settings
TABLE_NAME_PREFIX : TB_
LOG_TABLE_NAME_SUFFIX : _LOG

#dd_header: Content Level Settings
TABLE_PREFIX : TB_
LOG_TABLE_SUFFIX : _LOG
UNIQUE_KEY_PREFIX : UK_
FOREIGN_KEY_PREFIX : FK_
INDEX_PREFIX : IX_
VIEW_PREFIX : VW_
PRIMARY_KEY_PREFIX : PK_
TRIGGER_PREFIX : TR_
STORED_PROCEDURE_PREFIX : SP_
PACKAGE_PREFIX : PG_
FUNCTION_PREFIX : FN_
DIRECTORY_PREFIX : DR_
SEQUENCE_PREFIX : SQ_
TYPE_AS_OBJECT_PREFIX : TO_
TYPE_AS_TABLE_PREFIX : TT_
JOB_PREFIX : JB_

#dd_header: Comment Level Settings
LOG_TABLE_SUFFIX_COMMENT : "暫存檔"

```

##### 4.Gen-DDL

```
playbook 调用 roles

---
- hosts: local
  gather_facts: yes
  vars: 
    ansible_python_interpreter: "/usr/bin/env python"
    IS_CREATE_FOREIGN_KEY: "{{ lookup('vars', 'cli_is_create_foreign_key', default=True) }}" #True/False
    IS_GENERATE_LOG_TABLE: "{{ lookup('vars', 'cli_is_generate_log_table', default=False) }}" #False/True
    SQLPLUS_TERMOUT: "{{ lookup('vars', 'cli_sqlplus_termout', default='OFF') }}" #OFF/ON
  roles:
    - { role: build} # build artifacts and put into deployment folder
```

roles/build/files，tasks，template，defaults

```
===defaults/main.yml=====
#dd_header: Connection Level Settings
DB_HOST : 127.0.0.1  
DB_USER : MES  
DB_PASSWORD : mesdb123  
DB_SCHEMA : 127.
6464646

#dd_header: Filename Level Settings
TABLE_NAME_PREFIX : TB_
LOG_TABLE_NAME_SUFFIX : _LOG

#dd_header: Content Level Settings
TABLE_PREFIX : TB_
LOG_TABLE_SUFFIX : _LOG
UNIQUE_KEY_PREFIX : UK_
FOREIGN_KEY_PREFIX : FK_
INDEX_PREFIX : IX_
VIEW_PREFIX : VW_
PRIMARY_KEY_PREFIX : PK_
TRIGGER_PREFIX : TR_
STORED_PROCEDURE_PREFIX : SP_
PACKAGE_PREFIX : PG_
FUNCTION_PREFIX : FN_
DIRECTORY_PREFIX : DR_
SEQUENCE_PREFIX : SQ_
TYPE_AS_OBJECT_PREFIX : TO_
TYPE_AS_TABLE_PREFIX : TT_
JOB_PREFIX : JB_

#dd_header: Comment Level Settings
LOG_TABLE_SUFFIX_COMMENT : "暫存檔"

# BEGIN BLOCK (SEQ_NO)
COLUMN_WORD_SEQ_NO : "SEQ_NO"
COMMENT_WORD_SEQ_NO : "流水號"
COLUMN_TYPE_SEQ_NO : "NUMBER(19,0)"
# END BLOCK (SEQ_NO)
# BEGIN BLOCK (TIME)
COLUMN_WORD_TIME : "TIME"
COMMENT_WORD_TIME : "時間"
COLUMN_TYPE_TIME : "DATE"
# END BLOCK (TIME)
# BEGIN BLOCK (DATE)
COLUMN_WORD_DATE : "DATE"
COMMENT_WORD_DATE : "日期"
COLUMN_TYPE_DATE : "DATE"
# END BLOCK (DATE)
# BEGIN BLOCK (PREV_SYSTEM_NO)
COLUMN_WORD_PREV_SYSTEM_NO : "PREV_SYSTEM_NO"
COMMENT_WORD_PREV_SYSTEM_NO : "MES對接系統編號"
COLUMN_TYPE_PREV_SYSTEM_NO : "VARCHAR2(10)"
# END BLOCK (PREV_SYSTEM_NO)
# BEGIN BLOCK (FLAG)
COLUMN_WORD_FLAG : "FLAG"
COMMENT_WORD_FLAG : "標誌"
COLUMN_TYPE_FLAG : "VARCHAR2(1)"
# END BLOCK (FLAG)
# BEGIN BLOCK (IND)
COLUMN_WORD_IND : "IND"
COMMENT_WORD_IND : "註記"
COLUMN_TYPE_IND : "VARCHAR2(1)"
# END BLOCK (IND)
# BEGIN BLOCK (FILENAME)
COLUMN_WORD_FILENAME : "FILENAME"
COMMENT_WORD_FILENAME : "檔名"
COLUMN_TYPE_FILENAME : "VARCHAR2(255)"
# END BLOCK (FILENAME)
# BEGIN BLOCK (WIP_GUID)
COLUMN_WORD_WIP_GUID : "WIP_GUID"
COMMENT_WORD_WIP_GUID : "全域WIP序號"
COLUMN_TYPE_WIP_GUID : "VARCHAR2(100)"
# END BLOCK (WIP_GUID)
# BEGIN BLOCK (VERSION)
COLUMN_WORD_VERSION : "VERSION"
COMMENT_WORD_VERSION : "異動版本號"
COLUMN_TYPE_VERSION : "VARCHAR2(5)"
# END BLOCK (VERSION)

=====tasks/main.yml====
---
- name: create foreign key or not?
  debug: 
    msg: "{{ IS_CREATE_FOREIGN_KEY }}"

- name: location of inventory_dir
  debug: 
    msg: "{{ inventory_dir }}"

- name: "Removing Dir"
  file:
    path: "{{ inventory_dir }}/deployment"
    state: absent

- name: "create multiple deployment folders"
  block:
  - name: "Setting Fact Deployment Folder {{ deployment_path }}"
    set_fact: 
      deployment_path : "{{ inventory_dir }}/deployment"
  - name: "Checking deployment folders"
    stat:
      path: "{{ item }}"
    register: folder_stats
    with_items:
    - ["{{ deployment_path }}/mescore","{{ deployment_path }}/meslog"]
  - name: "Creating multiple folders without disturbing previous permissions"
    file:
      path: "{{item.item}}"
      state: directory
      mode: 0755
    when: item.stat.exists == false
    with_items:
    - "{{folder_stats.results}}"

- name: generate mescore SQL files
  vars:
    IS_GENERATE_LOG_TABLE: False
  template:
    src: "{{ item }}"
    dest: "{{ deployment_path }}/mescore/{{ item | basename | regex_replace('.j2','') }}"
  with_fileglob:
    - "../templates/mescore/*.j2"

- name: generate meslog SQL files
  vars:
    IS_GENERATE_LOG_TABLE: False
  template:
    src: "{{ item }}"
    dest: "{{ deployment_path }}/meslog/{{ item | basename | regex_replace('.j2','') }}"
  with_fileglob:
    - "../templates/meslog/*.j2"       

- name: copy utility files/scripts
  template:
    src: "{{ item }}"
    dest: "{{ deployment_path }}/{{ item | basename | regex_replace('.j2','') }}"
    mode: 0755
  with_fileglob:
    - "../templates/*.j2"
    
=====file ======
"DELETE_{{ COLUMN_WORD_FLAG }}" VARCHAR2(1) DEFAULT ON NULL 'N' NOT NULL,

COMMENT ON COLUMN "{{ DB_SCHEMA }}"."{{ TABLE_PREFIX }}{{ TABLE_NAME }}"."DELETE_{{ COLUMN_WORD_FLAG }}" IS '資料刪除註記';

"{{ COLUMN_WORD_TASK }}_{{ COLUMN_WORD_SEQ_NO }}" NUMBER(19,0) NOT NULL,
"{{ COLUMN_WORD_TASK }}_ACTION" VARCHAR2(10) NOT NULL ,
"{{ COLUMN_WORD_TASK }}_ACTION_UPDATED_{{ COLUMN_WORD_TIME }}" DATE NOT NULL,

{{ LOG_TABLE_SUFFIX_COMMENT if LOG_TABLE_IND else '' }}

{% macro header_updated() %}
    {% set SUPPORT_COLUMN_ENABLED_RELATED_FLAG = False %}
    {% set SUPPORT_COLUMN_CREATED_RELATED_FLAG = False %}
    {% set SUPPORT_COLUMN_UPDATED_RELATED_FLAG = SUPPORT_COLUMN_UPDATED_RELATED_FLAG %}
    {% set SUPPORT_COLUMN_DELETE_FLAG = False %}
    {% set SUPPORT_LOG_TABLE = False %}
    {% set LOG_TABLE_IND = (IS_GENERATE_LOG_TABLE and SUPPORT_LOG_TABLE) %}
    {% set TABLE_NAME = TABLE_NAME + LOG_TABLE_NAME_SUFFIX if LOG_TABLE_IND else TABLE_NAME %}
    {% set FK_COUNT = 0 %}
    {{ caller() }}
{% endmacro %}

===template=======
很多内容，待研究

```

##### 5.Gen-DML

```
---
- hosts: local
  gather_facts: yes
  roles:
    - { role: build} # build artifacts and put into deployment folder
```

roles/build/files，templates，tasks，defaults

```
====default/main.yml=====
#dd_header: Connection Level Settings
DB_HOST : 127.0.0.1  
DB_USER : MES  
DB_PASSWORD : mesdb123  
DB_SCHEMA : MESDB  

#dd_header: Filename Level Settings
TABLE_NAME_PREFIX : TB_
LOG_TABLE_NAME_SUFFIX : _LOG

#dd_header: Content Level Settings
TABLE_PREFIX : TB_
LOG_TABLE_SUFFIX : _LOG
UNIQUE_KEY_PREFIX : UK_
FOREIGN_KEY_PREFIX : FK_
INDEX_PREFIX : IX_
VIEW_PREFIX : VW_
PRIMARY_KEY_PREFIX : PK_
TRIGGER_PREFIX : TR_
STORED_PROCEDURE_PREFIX : SP_
PACKAGE_PREFIX : PG_
FUNCTION_PREFIX : FN_
DIRECTORY_PREFIX : DR_
SEQUENCE_PREFIX : SQ_
TYPE_AS_OBJECT_PREFIX : TO_
TYPE_AS_TABLE_PREFIX : TT_
JOB_PREFIX : JB_

#dd_header: Comment Level Settings
LOG_TABLE_SUFFIX_COMMENT : "暫存檔"

=====tashs/main.yml====
---
- name: location of inventory_dir
  debug: 
    msg: "{{ inventory_dir }}"

- name: "create multiple deployment folders"
  block:
  - name: "Setting Fact Deployment Folder {{ deployment_path }}"
    set_fact: 
      deployment_path : "{{ inventory_dir }}/deployment-dml"
  - name: "Checking deployment folders"
    stat:
      path: "{{ item }}"
    register: folder_stats
    with_items:
    - ["{{ deployment_path }}"]
  - name: "Creating multiple folders without disturbing previous permissions"
    file:
      path: "{{item.item}}"
      state: directory
      mode: 0755
    when: item.stat.exists == false
    with_items:
    - "{{folder_stats.results}}"    

- name: copy utility files/scripts
  template:
    src: "{{ item }}"
    dest: "{{ deployment_path }}/{{ item | basename | regex_replace('.j2','') }}"
    mode: 0755
  with_fileglob:
    - "../templates/*.j2"
    
 =====  template========
 1.
#!/usr/bin/env bash

./initial-load.sh
./initial-load-test-data.sh

2.
#!/usr/bin/env bash

#set path to sqlplus
export PATH=../bin/instantclient_19_3:$PATH
export LD_LIBRARY_PATH="$PATH":$LD_LIBRARY_PATH

#set encoding code
export NLS_LANG=.AL32UTF8

#Execute All DMLs
echo  "Performing Initial Load"
exit | sqlplus "{{ DB_USER }}/{{ DB_PASSWORD }}@{{ DB_HOST }}/xe" @initial-load.sql

3.
#!/usr/bin/env bash

#set path to sqlplus
export PATH=../bin/instantclient_19_3:$PATH
export LD_LIBRARY_PATH="$PATH":$LD_LIBRARY_PATH

#set encoding code
export NLS_LANG=.AL32UTF8

#Execute All DMLs
echo  "Performing Initial Load"
exit | sqlplus "{{ DB_USER }}/{{ DB_PASSWORD }}@{{ DB_HOST }}/xe" @initial-load-test-data.sql

4.5.6......
```

##### 6.Copy to svn

```
---
- hosts: local
  gather_facts: yes
  roles:
    - { role: copy_to_svn} # copy generated artifacts to svn
```

roles/copy_to_svn

```
1===roles/copy_to_svn/default=====
---
deployment_path_ddl: 'deployment'
deployment_path_dml: 'deployment-dml'

2===roles/copy_to_svn/includes=====
- name: debug_level_block
  block:
  - name: "debug msg copy by folder (folder_name = {{ folder_name }} )"
    debug: 
      msg: "{{ folder_name }}"

  - name: "debug msg var : deployment_path_real"
    debug: 
      msg: "{{ deployment_path_real }}"

  - name: "debug msg var : svn_path_real"
    debug: 
      msg: "{{ svn_path_real }}"    

- name: "Creating {{ svn_path_real }}/{{ folder_name }} folder"
  file:
    path: "{{ svn_path_real }}/{{ folder_name }}"
    state: directory
    mode: 0755

- name: copy {{ folder_name }} .sql files
  # debug: 
  #   msg: "{{ deployment_path }}_stg/{{ item | basename }}"
  copy:
    src: "{{ item }}"
    dest: "{{ svn_path_real }}/{{ folder_name }}/{{ item | basename }}"
    mode: 0644
  with_fileglob:
    - "{{ deployment_path_real | realpath }}/{{ folder_name }}/*.sql"   

3===roles/copy_to_svn/tasks=====
---
- name: "Setting Fact SVN Folder"
  set_fact: 
    svn_path : "{{ lookup('vars', 'inventory_dir') + '/svn/mesdb/' | realpath }}"

- name: "Setting Fact SVN sub Folder"
  set_fact: 
    svn_path_ddl : "{{ lookup('vars', 'svn_path') + '/ddl/tables' | realpath }}"    
    svn_path_dml : "{{ lookup('vars', 'svn_path') + '/dml' | realpath }}"    

- name: location of svn_path (target)
  debug: 
    msg: "{{ svn_path }}"

- name: location of svn_path_ddl (target)
  debug: 
    msg: "{{ svn_path_ddl }}"    

- name: location of svn_path_dml (target)
  debug: 
    msg: "{{ svn_path_dml }}"        

- name: location of deployment_path_ddl (source)
  debug: 
    msg: "{{ deployment_path_ddl | realpath }}"

- name: "include subplaybook - includes/copy_to_target_by_folder.yml for *.sql files"
  include: includes/copy_to_target_by_folder.yml
  vars:
    deployment_path_real: "{{ deployment_path_ddl | realpath }}"
    svn_path_real: "{{ svn_path_ddl }}"
  with_items: 
    - [ 'mescore', 'meslog'] 
  loop_control:
    loop_var: folder_name

- name: copy utility .sh files to {{ svn_path_ddl }}
  copy:
    src: "{{ item }}"
    dest: "{{ svn_path_ddl | realpath }}/{{ item | basename }}"
    mode: 0755
  with_fileglob:
    - "{{ deployment_path_ddl | realpath }}/*.sh"   

- name: copy utility .sql files to {{ svn_path_ddl }}
  copy:
    src: "{{ item }}"
    dest: "{{ svn_path_ddl | realpath }}/{{ item | basename }}"
    mode: 0644
  with_fileglob:
    - "{{ deployment_path_ddl | realpath }}/*.sql"     

- name: "Creating {{ svn_path_dml | realpath }} folder"
  file:
    path: "{{ svn_path_dml | realpath }}"
    state: directory
    mode: 0755

- name: copy utility .sh files to {{ svn_path_dml }}
  copy:
    src: "{{ item }}"
    dest: "{{ svn_path_dml | realpath }}/{{ item | basename }}"
    mode: 0755
  with_fileglob:
    - "{{ deployment_path_dml | realpath }}/*.sh"   

- name: copy utility .sql files to {{ svn_path_dml }}
  copy:
    src: "{{ item }}"
    dest: "{{ svn_path_dml | realpath }}/{{ item | basename }}"
    mode: 0644
  with_fileglob:
    - "{{ deployment_path_dml | realpath }}/*.sql"           

- name: This command will reset the database and export the derived csv files
  command: ./run.sh
  args:
    chdir: "{{ svn_path_ddl }}"
  register: runoutput_ddl

- name: debug message - runoutput_ddl
  debug: 
    msg: "{{ runoutput_ddl | to_nice_json(indent=2) }}"  

- name: This command will initial load the database 
  command: ./run.sh
  args:
    chdir: "{{ svn_path_dml }}"
  register: runoutput_dml

- name: debug message - runoutput_dml
  debug: 
    msg: "{{ runoutput_dml | to_nice_json(indent=2)}}"      

- name: "Assertion on the execution result as expected"
  block:
  - name: initialize empty list 'sqlfile_list_for_assert' and 'table_list_for_assert'
    set_fact:
      sqlfile_list_for_assert: []
      table_list_for_assert: []
  
  - name: "This command will retrive the number of tables and register variable 'runoutput_ddl_assert' "
    command: ./run-assert-count.sh
    args:
      chdir: "{{ svn_path_ddl }}"
    register: runoutput_ddl_assert
  
  - name: "set_fact 'number_of_tables' from variable 'runoutput_ddl_assert.stdout'"
    set_fact: 
      number_of_tables: "{{ runoutput_ddl_assert.stdout  | regex_search('\\d+') }}"
  
  - name: This command will get the list of tables and register variable 'runoutput_ddl_assert_tablelist'
    command: ./run-assert-tablelist.sh
    args:
      chdir: "{{ svn_path_ddl }}"
    register: runoutput_ddl_assert_tablelist  
  
  - name: "set_fact 'table_list_for_assert'  from variable 'runoutput_ddl_assert_tablelist.stdout_lines'"
    set_fact: 
       table_list_for_assert: "{{ table_list_for_assert }} + ['{{ item | regex_replace('TB_','') }}']"    
    with_items: "{{ runoutput_ddl_assert_tablelist.stdout_lines }}"
    loop_control:
      label: "{{ item }}"  
  
  - name: "Count the number of *.sql files in subfolders in path: {{deployment_path_ddl}} and register variable 'runoutput_ddl_file_count_assert'"
    find:
      paths: "{{ deployment_path_ddl | realpath }}/{{ item }}"
      patterns: "*.sql"
      use_regex: no
    with_items: 
      - [ 'mescore', 'meslog']     
    register: runoutput_ddl_file_count_assert  
  
  - name: "set_fact 'number_of_sql_files'  from variable 'runoutput_ddl_file_count_assert.results'"
    set_fact:
      number_of_sql_files: "{{ number_of_sql_files|default(0)|int + item.matched|int }}" 
    loop: "{{ runoutput_ddl_file_count_assert.results }}"
    loop_control:
      label: "item.matched : {{ item.matched }}"  
  
  - name: "set_fact 'sqlfile_list_for_assert' from variable 'runoutput_ddl_file_count_assert.results' with json_query"
    set_fact:
      sqlfile_list_for_assert: "{{ sqlfile_list_for_assert }} + ['{{ item.path | basename |  regex_replace('.sql$','') }}'] " 
    with_items: "{{ runoutput_ddl_file_count_assert | json_query('results[*].files[*]') }}"
    loop_control:
      label: "{{ item.path | basename |  regex_replace('.sql$','') }}"
  
  - name: debug message - sqlfile_list_for_assert | difference(table_list_for_assert)
    debug:
     msg: "{{ sqlfile_list_for_assert | difference(table_list_for_assert) }}"
    
  - name: Assert 'Number of SQL Files' should equal to 'Number of Tables' 
    fail:
      msg: " Number of SQL Files : ({{ number_of_sql_files }}) !=  Number of Tables : ({{ number_of_tables }})"
    when: number_of_sql_files != number_of_tables
```

#### 15.ansible操作windows

```
获取window主机信息：
  ansible windows -m setup
执行ps脚本：
  ansible windows -m script -a "E://test.ps1"
查看文件状态：
  ansible windows -m win_stat -a "path='C://Windows/win.ini'"
结束程序：
  ansible windows-m raw -a "taskkill /F /IM QQ.exe /T"
如果window主机传回来的中文是乱码，则修改ansible控制机上的python编码：
  sed -i "s#tdout_buffer.append(stdout)#tdout_buffer.append(stdout.decode('gbk').encode('utf-8'))#g" /usr/lib/python2.6/site-packages/winrm/protocol.py

  sed -i "s#stderr_buffer.append(stderr)#stderr_buffer.append(stderr.decode('gbk').encode('utf-8'))#g" /usr/lib/python2.6/site-packages/winrm/protocol.py
指定配置文件获取win网卡信息
  ansible -i win_hosts windows -m raw -a "ipconfig"
使用默认的配置文件获取网卡信息
  ansible windows -m raw -a "ipconfig"
拷贝文件到远程Windows主机
  ansible windows -m win_copy -a 'src=/etc/passwd dest=F:\file\passwd'
  ansible windows -m win_copy -a "src=/usr/local/src/PayChannels20.35.zip dest=D:\Tomcat8630\webapps\PayChannels20.35.zip"
  ansible windows -m win_copy -a "src=/usr/local/src/SupplierPay.zip dest=D:\SupplierPay.zip
将.zip解压到远程Windows主机，远程主机上必须存在需要解压的源文件
  ansible windows -m win_unzip -a"creates=no src=D:\Tomcat8620\webapps\PayChannels-8630.zip dest=D:\Tomcat8620\webapps"
解压到D盘：ansible windows -m win_unzip -a"creates=no src=D:\SupplierPay.zip dest=D:"
重启远程windows主机的服务
  ansible windows -m win_service -a "name=Tomcat8630 state=restarted"
重启node.js(.bat命令)
  ansible windows -m win_command -a "chdir=D:\SupplierPay .\http_restart.bat"
```

##### 2.win_command模块命令

```
启动redis
  ansible windows -m win_command -a "chdir=D:\Redis server-start.bat "
  ansible win -m win_command -a "chdir=C:\ a.bat "
  ps:"chdir=C:\ a.bat " 之前有空格
删除文件或者目录
  ansible windows -m win_file -a "dest=D:\Tomcat8630\log\ state=absent"
  ansible windows -m win_file -a "dest=D:\Tomcat8630\logs\ state=absent"
创建用户
  ansible windows -m win_user -a "name=aa passwd=123456"
创建一个名叫user1的管理员用户，要求能够远程访问
  ansible windows -m win_user -a "name=user1 password=123 groups='Administrators,Remote Desktop Users'"
重启的第一种方式
  ansible windows -m win_shell -a "shutdown -r -t 1"
重启的第二种方式
  ansible windows -m win_reboot
获取ip地址
  ansible windows -m raw -a "ipconfig"
获取身份
  ansible windows -m win_command -a "whoami"
移动文件
  ansible windows -m raw -a "cmd /c 'move /y D:\Ansible\product\DBFPlus.exe D:\Ansible\back\'"
移动文件目标端也需要制定到文件，而不能只制定到所在目录位置
  ansible windows -m raw -a "cmd /c 'move /y D:\Ansible\product D:\Ansible\back'"
移动文件夹源端和目标端目录都不能带反斜杠/。且将源的整个目录移到目的端目录里。

创建文件夹
  ansible windows -m raw -a "md d:\Ansible\justin"
删除文件或目录
  ansible windows -m win_file -a "path=d:\Ansible\justin state=absent"
结束某程序
  ansible windows -m raw -a "taskkill /F /IM snmp.exe /T"
文件传输
  ansible windows -m win_copy -a 'src=/app/svn/127_Client/118919/zjcfg.zip dest=D:\soft\'
 
```

3.win的modul

```
win_acl - 為系統用戶或組設置文件/目錄/註冊表權限
win_acl_inheritance - 更改ACL繼承
win_audit_policy_system - 用於更改系統範圍的審核策略
win_audit_rule - 向文件，文件夾或註冊表項添加審核規則
win_certificate_store - 管理證書庫
win_chocolatey - 使用chocolatey管理包
win_chocolatey_config - 管理Chocolatey配置設置
win_chocolatey_facts - 為Chocolatey創建一個事實集合
win_chocolatey_feature - 管理Chocolatey功能
win_chocolatey_source - 管理Chocolatey來源

win_command - 在遠程Windows節點上執行命令
win_copy - 將文件複製到Windows主機上的遠程位置
win_credential - 在Credential Manager中管理Windows憑據
win_defrag - 合併本地捲上的碎片文件
win_disk_facts - 顯示目標主機的附加磁盤和磁盤信息
win_disk_image - 在Windows主機上管理ISO / VHD / VHDX安裝
win_dns_client - 在Windows主機上配置DNS查找
win_dns_record - 管理Windows Server DNS記錄
win_domain - 確保存在Windows域
win_domain_computer - 管理Active Directory中的計算機
win_domain_controller - 管理Windows主機的域控制器/成員服務器狀態
win_domain_group - 創建，修改或刪除域組
win_domain_group_membership - 管理Windows域組成員身份
win_domain_membership - 管理Windows主機的域/工作組成員身份
win_domain_user - 管理Windows Active Directory用戶帳戶
win_dotnet_ngen - 在.NET更新後運行ngen以重新編譯DLL
win_dsc - 調用PowerShell DSC配置
win_environment - 修改Windows主機上的環境變量
win_eventlog - 管理Windows事件日誌
win_eventlog_entry - 將條目寫入Windows事件日誌
win_feature - 在Windows Server上安裝和卸載Windows功能
win_file - 創建，觸摸或刪除文件或目錄
win_file_version - 獲取DLL或EXE文件構建版本
win_find - 根據特定條件返回文件列表
win_firewall - 啟用或禁用Windows防火牆
win_firewall_rule - Windows防火牆自動化
win_format - 在Windows上的現有分區上格式化現有捲或新卷
win_get_url - 將文件從HTTP，HTTPS或FTP下載到節點
win_group - 添加和刪除本地組
win_group_membership - 管理Windows本地組成員身份
win_hostname - 管理本地Windows計算機名稱
win_hosts - 管理Windows上的主機文件條目
win_hotfix - 安裝和卸載Windows修補程序
win_http_proxy - 管理WinHTTP的代理設置
win_iis_virtualdirectory - 在IIS中配置虛擬目錄
win_iis_webapplication - 配置IIS Web應用程序
win_iis_webapppool - 配置IIS Web應用程序池
win_iis_webbinding - 配置IIS網站綁定
win_iis_website - 配置IIS網站
win_inet_proxy - 管理WinINet和Internet Explorer的代理設置
win_lineinfile - 確保特定行位於文件中，或使用反向引用的正則表達式替換現有行
win_mapped_drive - 為用戶映射網絡驅動器
win_msg - 向Windows主機上的登錄用戶發送消息
win_nssm - 使用NSSM安裝服務
win_optional_feature - 管理可選的Windows功能
win_owner - 設置所有者
win_package - 安裝/卸載可安裝的軟件包
win_pagefile - 查詢或更改頁面文件配置
win_partition - 在Windows Server上創建，更改和刪除分區
win_path - 管理Windows路徑環境變量
win_pester - 在Windows主機上運行Pester測試
win_ping - 經典ping模塊的Windows版本
win_power_plan - 更改Windows系統的電源計劃
win_product_facts - 提供Windows產品和許可證信息
win_psexec - 作為另一個（特權）用戶運行命令（遠程）
win_psmodule - 添加或刪除Windows PowerShell模塊
win_psrepository - 添加，刪除或更新Windows PowerShell存儲庫
win_rabbitmq_plugin - 管理RabbitMQ插件
win_rds_cap - 管理遠程桌面網關服務器上的連接授權策略（CAP）
win_rds_rap - 在遠程桌面網關服務器上管理資源授權策略（RAP）
win_rds_settings - 管理遠程桌面網關服務器的主要設置
win_reboot - 重啟Windows機器
win_reg_stat - 獲取有關Windows註冊表項的信息
win_regedit - 添加，更改或刪除註冊表項和值
win_region - 設置區域和格式設置
win_regmerge - 將註冊表文件的內容合併到Windows註冊表中
win_robocopy - 使用Robocopy同步兩個目錄的內容
win_route - 添加或刪除靜態路由
win_say - 用於Windows的文本到語音模塊，可以說出消息並可選擇播放聲音
win_scheduled_task - 管理計劃任務
win_scheduled_task_stat - 獲取有關Windows計劃任務的信息
win_security_policy - 更改本地安全策略設置
win_service - 管理和查詢Windows服務
win_share - 管理Windows共享
win_shell - 在目標主機上執行shell命令
win_shortcut - 在Windows上管理快捷方式
win_snmp - 配置Windows SNMP服務
win_stat - 獲取有關Windows文件的信息
win_tempfile - 創建臨時文件和目錄
win_template - 將文件模板到遠程服務器
win_timezone - 設置Windows機器時區
win_toast - 向Windows 10或更高版本主機上的登錄用戶發送Toast窗口通知
win_unzip - 在Windows節點上解壓縮壓縮文件和存檔
win_updates - 下載並安裝Windows更新
win_uri - 與webservices交互
win_user - 管理本地Windows用戶帳戶
win_user_profile - 管理Windows用戶配置文件
win_user_right - 管理Windows用戶權限
win_wait_for - 在繼續之前等待條件
win_wait_for_process - 在繼續之前等待進程存在或不存在
win_wakeonlan - 發送魔術網絡喚醒（WoL）廣播包
win_webpicmd - 使用Web Platform Installer命令行安裝軟件包
win_whoami - 獲取有關當前用戶和進程的信息
win_xml - 將XML片段添加到XML父級
```



#### 16.文件重命名

```
- name: rename the {{ source_name }} to  {{ target_name }}
  win_command: "cmd.exe /c rename {{ destination_folder }}\\{{ source_name }} {{ target_name }}" 
```

好玩的东西

```
获取ip地址
ansible windows -m raw -a "ipconfig"
获取身份
ansible windows -m win_command -a "whoami"

重启远程windows主机的服务
ansible windows -m win_service -a "name=Tomcat8630 state=restarted"
重启node.js(.bat命令)
ansible windows -m win_command -a "chdir=D:\SupplierPay .\http_restart.bat"
执行win_command模块命令
启动redis
ansible windows -m win_command -a "chdir=D:\Redis server-start.bat 
ansible win -m win_command -a "chdir=C:\ a.bat "重启远程windows主机的服务
ansible windows -m win_service -a "name=Tomcat8630 state=restarted"


 ansible windows_51 -m win_command -a "E:\software\apache-tomcat-8.5.32\bin\startup.bat "
```



#### 翻译：

```

subscribe：定期订购，定期交纳，认购
argument：理由，论点，争辩，争吵
content：内容
register：记录
persent：目前
license：许可证
host iventory:主机的列表    host：主人，主办方       inventory:清单，存货，库存

breakdown：(车辆或机器的) 故障，损坏; (关系的) 破裂; (讨论、系统的) 失败; 数字细目; 分类
```





❶❷❸❹❺❻❼

  mkdir -p /etc/ansible/roles/rename/{defaults,files,handlers,meta,tasks,templates,vars}