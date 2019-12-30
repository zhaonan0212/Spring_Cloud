###  Execl vba 学习

##### 1.为什么学习vba

```
1.规范用户操作，控制用户的操作行为
2.操作界面人性化，方便用户的操作
3.多个步骤的手工操作通过执行vba代码可以迅速的实现
4.实现一些vb无法实现的功能
5.用vba制作excel登录系统
6.利用vba可以excel内轻松开发功能强大的自动化程序
```

##### 2.什么是vba

```
Visual Basic for Applications（VBA）是Visual Basic的一种宏语言，是微软开发出来在其桌面应用程序中执行通用的自动化(OLE)任务的编程语言。主要能用来扩展Windows的应用程式功能，也可说是一种应用程式视觉化的Basic脚本。
```

##### 3.简单入门

```
1.新建excle
2.右键下面的sheet1，选择查看代码（检视工作码），进入到vba的编辑界面
3.左侧选择worksheet
4.右侧选择出发模式
    BeforeDoubleClick，意思就是说：在本工作表鼠标双击（之前），将触发下面的代码程序。
    activate意思是说：在sheet表被选取的时候，就触发；
    Change意思是说：在这个模式下，只要sheet（工作表）发生变化，就将触发；
    Before Right Click是指在鼠标右击之前触发；
    SelectionChange是指在鼠标焦点发生变化时，触发
    。。。。。。
5.选择完成后，会出现下面的两条代码，就是声明一个函数
6.将鼠标放置在这两行代码之间，我们写上执行语句：
   Sheet1.Cells(1, 3) = Sheet1.Cells(1, 1) + Sheet1.Cells(1, 2)
   注意的是：写语句的时候，输入法一定要是输入字母的模式。要不然你的语句会报错
7.编写后保存，输入Cells(1, 1)Cells(1, 2)值，然后双击Cells(1, 1)，就会发现Cells(1, 3)求和
```

##### 4.模块（按钮）

```
如果是要写按钮功能，那么就要在visual basic中，进入到模块模式
首先先站到哪张表，然后鼠标右键插入，选择模块，下方就会出现模块
================================
或者在开发人员处，选择插入，表单控制项，选择第一个，就会自动建模块。
```

```
只需要了解4个class：
   application：整个excel
   workbook：工作簿
   worksheet：工作表
   cells：单元格
=======================我的按钮想在某个sheet中使用===========
Dim w1 As worksheet
set w1 = worksheets(3)                            ==>设置对象，一定要set
w1.Cells(3,5) = 10

=========升级================
Dim w1 As worksheet
Dim i
 for i = 1 to Worksheets.Conut step 1
    set w1 = worksheets(i)                       ==>设置所有工作表
    w1.Cells(3,5) = 10
 next i   
========添加工作表
Worksheets.Add0
```



##### 5.cells

```
cells就是格子的意思
如果想表示E7（5，7）这个单元格，就是cells（7，5），
```

##### 6.demo

```
demo1:给半径，求周长和面积
先设变量： x = cells（3，4）
          y = 3.14 * x * x
          z = 4/3 * 3.14 * x * x
最后展示结果
          cells(3,6) = y
          cells(3,8) = z
```

```

```

##### 7.变量

```
强制声明，编辑注册，
必须写在第一行，
option explicit
========================
然后在每一个sub里面使用dim
如：dim x，y，z 如果出现没有的变量，就报警
```

```
有变量就有常量
const pi = 3.1415926
```

```
string 字符串   
例：if Cells(1,2) = "数学" or Cells(1.2)= "英语" then
         Cells(2,3) = xxxxxx
先not 然后and 最后or
```



##### 8.快捷键

```
也可以在可能出在问题的语句最左侧设置断点
然后F8 debug 每一行

如果想看变量具体的值，可以添加监视，即选择侦错，然后新增监看式   
可以F5键直接运行光标所在的子程序
```

##### 9.循环与判断

```
for i = 10 to 20 step 1 
next i
==========================
while x > 5
   xxxxxxxxx
wend
   xxxxxxx
===========================
for i = 1 to worksheets.count step 1
 set w = worksheets(i)                                ==> for each w in worksheets
next i                                                    next w 
```

```
while的例子1：
 dim i
 i = 2
 while Cells(i,1) <> ""
   If Cells(i,2) < 60 then
      Cells(i,2).Font.Color=vbRed
   Endif
   i = i + 1
 wend  
 目前比较流行的就是do while 。。。。。loop
 =======================================================
 while的例子2：
 求平均值
 Dim I，total，count
 total = 0
 count = 0
 i = 2
 do while Cells(i,2) <> "" or Cells(i,3) <> "" 
    total = Cells(i,2) + total
    count = 1 + count
    i = i + 1
 Loop
 If count > 0 then   
    Cells(x,x) = total/count
 EndIf  
 =====================嵌套循环===============================
 while 的例子3：
 dim i，j
 i = 2 
 do while cells(i,1) <> ""
   for j = 2 to 10 step 1 
     Cells(i,j) = Cells(i,j) * 0.453
   next j
   i = i + 1  
 Loop
   Cells(x,x) = "磅"
```

```
while的例子4：我要把表2，表3。。。。里面的姓名和总成绩都总结到表1中
Dim i,name ，score,k
Dim w1 As WorkSheet,w2 As WorkSheet
k = 2
  for i = 2 to WorkSheets.Count step 1
      Set w1 = WorkSheets(i)
      name = w1.Cells(2,1)                            ==>先把名字和总成绩存起来
      score = w1.Cells(3,4)
      
      w2.Cells(k,1) = name
      w2.Cells(k,2) = score                           ==>放到表1中，然后建一行
      k = k + 1
  next i

```

##### 10.录制宏

下面的例子中i是一个循环的值

for i = 10 to 20 step 1

```
1.清空某一个单元格内容
    Range("M3").Select                                   cells(i,5).Select
    Selection.ClearContents   ==》                       Selection.ClearContents
```

```
2.删除一行
    Rows("15:15").Select                                 Rows(i&":"&i).Select
    Selection.Delete Shift:=xlUp  ==》                   Selection.Delete Shift:=xlUp
```

```
检测数据，删除数据最好用倒序，因为从后往前都是被处理过的，
如果用正序，假如第五行，六行七行都有问题，删除第五行的同时，第六行会自动变成第五行，但是第五行已经检查过了，不会再检查。。。
```

##### 11.调用（封装）

```
在一个sub里面可以调用另一个sub
call abcxxx                           ==》call还可以省略
```

##### 12.函数

如果想弹出文本框，则使用MsgBox

```
Len(s)：长度gai
Trim(s)：返回一个新的字符串，但是两边都会过滤掉空格
LCase(s)：返回一个新的字符串，并把s中所有字母都小写
UCase(s)：返回一个新的字符串，并把s中所有字母都大写

Right(s,x):从s的右边取出x个字母
Left(s,x):从s的左边取出x个字母
Mid(s，x，y):从s的第x个开始，取出y个

Replace(s," ","")：把s字符串中空格都去掉
Instr(x,s,"苹果"):从s字符串的第x个字符开始，是否包含苹果
```

```
例1：判断是不是月度报表
dim i
dim w As WorkSheet，r As WorkSheet
for i = 1 to WorkSheets.Count step 1                             ==>循环所有的报表
  set w = WorkSheet(i)
  if Right(w.name,1) == "月" then
     xxxxx
     xxxxx
next i
```

##### 13.workbook

```
1.可以用vba打开excel，使用workbook
Dim wb As WorkBook
set wb = WorkBooks.open(""D:\季度汇总\4yue.xlsx)
2.新建一个新的excel
set wb = WorkBooks.Add
3.保存
wb.SaveAs"D:\测试。xlsx"
4.
```

```
例1：假设D:/季度汇总/里面有4，5，6月报表
Dim i,filename
Dim wb As WorkBook,w As WorkSheet
for i = 4 to 6 step 1
	filename = i & "月.xlsx"
	Set wb = WorkBooks.open("D:\季度汇总\" & filename)
	Set w = wb.WorkSheets(1)
next i  

```

##### 14.range

```
例1：range区域全部变黄
Dim r As Range
set r = Range("A5:E11")
r.Interior.Color = vbYellow
=============================
set r = Range（Cells（4，7），Cells（11，9））
=============================
缩写
r.font.size=15                                ==> with r.font
r.font.color=RGB(255,255,0)                           .size = 15
r.font.bold=true                                      .color = RGB(255,255,0)
r.font.italic=true                                    .Bold = True
                                                  End with
```

15.处理第11功能的逻辑

```
1. 第一个for 取出每一个单元格的内容
2. 把单元格切割,取出每一行的内容
3. 如果是六段式,则过
   如果是正常,则判断x,y的值
   	 如果x = 1, 那么有两种可能,要么就没有,要么左侧漏写
   	 	如果没有,则过
   	 	如果漏写,报警
   	 如果 x <> 1 ,那么也是两种
        如果y = 0 ,右侧括号漏写
        如果y > 0, 判断里面内容
             如果内容与变数吻合,则ok
             如果与变数不吻合,报警,变红.
```

##### 15.锁住单元格

```
当前单元格的锁定和解锁
Sub onlock()
    ThisWorkbook.ActiveSheet.Protect Password:="8888", DrawingObjects:=True, Contents:=True, Scenarios:=True, AllowFiltering:=True
End Sub

Sub unlocked()
    ThisWorkbook.ActiveSheet.Unprotect
End Sub
```

```
workbook里面的sheets全锁
Private Sub Workbook_Open()
Dim sh1 As Worksheet
For Each sh1 In Worksheets
sh1.Protect Password:="1", UserInterfaceOnly:=True
sh1.EnableOutlining = True
Next
End Sub
```

```
如果一部分要锁死,一部分要编辑,就用下面的方法
Sub onLockTabAndCol()

    ThisWorkbook.Sheets("TableInfo").Select
    ActiveSheet.Protect DrawingObjects:=True, Contents:=True, Scenarios:=True, AllowFormattingCells:=True,     AllowFormattingRows:=True
    
    ThisWorkbook.Sheets("ColumnInfo").Select
    ActiveSheet.Protect DrawingObjects:=True, Contents:=True, Scenarios:=True,AllowFormattingCells:=True,AllowFormattingRows:=True
    
End Sub
```

