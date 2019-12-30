## Git的安装和使用

##### 1.安装

```
1.去官网下载Git
2.安装过程:https://blog.csdn.net/wuxiao18/article/details/97782432
```

##### 2.使用

```
1.安装成功后,会有图标 Git Bash 和Git GUI
2.配置用户名和邮箱
 git config --global user.name "NAN.ZHAO"
 git config --global user.email "zhaonan0212@163.com"
3.创建repository
 cd D:
 cd 随便一个文件夹
 mkdir testgit
 cd testgit
 pwd    显示路径
 git init 把这个目录变成git可以管理的仓库
```

创建完仓库,就可以提交代码

```
1.在.git目录下创建文件
   git add abc.txt
2.git commit -m "abc.txt"
3.查看状态
   git status  
```

##### 3.工作区和暂存区

```
使用 git add 把文件添加进去，实际上就是把文件添加到暂存区。
使用git commit提交更改，实际上就是把暂存区的所有内容提交到当前分支上。

git checkout --abc.txt 意思就是，把readme.txt文件在工作区做的修改全部撤销，这里有2种情况，如下：
abce.txt自动修改后，还没有放到暂存区，使用 撤销修改就回到和版本库一模一样的状态。
另外一种是readme.txt已经放入暂存区了，接着又作了修改，撤销修改就回到添加暂存区后的状态。
```

##### 4.ssh连接

```
1.在用户下查找 nan.zhao 下面有没有.ssh ,
2.如果没有:
   $ ssh-keygen -t rsa -C "your_email@example.com"
   直接回车   =>使用默认文件名（推荐），那么就会生成 id_rsa 和 id_rsa.pub 两个秘钥文件。
3.然后输入两次密码 (push文件的时候要输入的密码，而不是github管理者的密码)   
   可以不写,直接两次回车
4.提示:Your identification has been saved in /c/Users/you/.ssh/id_rsa.
   表示ssh创建成功
```

##### 5.连接github

```
1.用户名下面有个下拉框,选择下拉框
2.点击左侧ssh keys
3.add ssh key
4.复制id_rsa.pub内容到key

$ clip < ~/.ssh/id_rsa.pub  复制内容,使用git语法
```

##### 6.远程仓库切换

```
正常情况:加入连接github
   1.git remote add origin https://......
   2.Git push -u origin master
```

```
如果修改远程仓库:
   1.git remote rm origin
   2.git remote add origin HTTP//......
         Delta私服git =>  http://deltascm.deltaww.com/Corp/Corp/git/ADD/MESA2
         
   3.git pull origin master
      如果此处报警,fatal: refusing to merge unrelated histories 
      git pull origin master –-allow-unrelated-histories
   4.git push origin master
```

7.报警

```
1.error: failed to push some refs to 'https://github.com/zhaonan0212/SoftWare.git'
表示本地与远程的仓库不一致,提示吧远程仓库同步到本地
git pull --rebase origin master
```

