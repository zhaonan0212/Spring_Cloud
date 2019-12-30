### VUE學習記錄

1.要用nodejs創建項目，然後開啟，在vscode裡面編輯。

```properties
使用vscode之前，先启动vue的文件
1.找到vue文件
cd vue01
2.安装
npm install
3.运行
npm run dev     ==>最后弹出新页面： your application is running here：http：//localhost：8080 
```

#### 1.vue的基本語法

 1.導入組件

```xml
import Counter from './Counter'
====================
./是當前目錄
../是上級目錄
後面會有路由，當導入路由的時候要“./../”
```

 2.當父組件想將值傳給子組件的時候，使用props

```xml
比如父組件是Hello，先在components裡面引入Counter
<Counter num="10"></Counter>
子組件Counter去接收 
<script>
    export default {
        props:['num']
        ｝
</script>
```

3.動態賦值，使用v-bind  縮寫就是：

```vue
<Counter v-bind:num="num"></Counter>
export default {
  name: 'HelloWorld',
  data () {
    return {
      num:10,
      msg: 'Welcome to Your Vue.js App'
    }
  }
 ｝
```

4.子組件將值傳給父組件,使用emit

```vue
父組件中定義事件
 <Counter v-bind:num="num" v-on:incret="increment" v-on:decret="decrement"></Counter>
  methods:{
    increment(){
      this.num++
    },
    decrement(){
      this.num--
    }
子組件使用
 <div>
      <button @click="increment">+</button>
      <button @click="decrement">-</button><br>
      <span>{{num}}</span>
 </div>
     methods:{
            increment(){
               this.$emit("incret");
            },
            decrement(){
               this.$emit("decret");
            }

        }
```

5.v-for

```java
第一种情况==数组
data(){
 return{
    list:[
    {
        name:'zhaonan',
        age:18,
        work:it enginer
    },
      {
        name:'zhangfei',
        age:23,
        work:cooker
    },
      {
        name:'liubei',
        age:20,
        work:worker
    }
  ]}
}

<div>
   <li v-for="(text,index) in list">{{text.name}}--{{text.age}}  -- {{text.work}} --{{index}}</li>
</div>
2.第二种情况==========对象
data(){
    return{
        list:{
            name:'zhaoyun',
            age:25
        }
    }
}
<div>
   <li v-for="(value,key) in list">｛｛value + key｝｝</li>
</div>
```

6.v-on的push

```xml
<button @click="addItem"> additem <button>
mehtods:{
    addItem(){
        this.list.push({                         #会将两条属性添加到list对象里面。
            favourite:"basketball",
            music:"星星点灯"
        })
    }
}
==================如果想直接修改================
methods:{
    addItem(){
       Vue.set(this.list,1{      #修改的是哪个对象，第几个元素，
         name:"卢布",
         age:22
        })
       }
    }
```

7.v-if和v-show

```xml
<a v-if="isPartA"> isPartA </a>             #使用v-if,没有的话，源码也没有      
<a v-show="!isPartA"> isPartB </a>          #使用v-show,不管页面展没展示，标签都在源码里
<button v-on:click="toggle"></button>
data(){
    return{
        isPartA:true
    }
}
methods :{
   toggle(){
       this.isPartA = !this.isPartA
   }
}
=====还提供了v-else====
<a v-if="isPartA"> isPartA </a> 
<a v-else> nodata </a> 
如果两个标签名一样，又没有其他区别，那么特效会消失，所以要添加区别，比如key="1",key="2".....
```

8.自定义事件

```java
1.我们先在父组件中添加子组件
import comA from './components/a.vue'
2.在template中导入
<com-a @my-event="onComaMyEvent"></com-a>
3.添加方法
methods:{
    onComaMyEvent()｛
       console.log("中华人名共和国")
    ｝
}
4.在a.vue里面定义
<button @click="emitMyEvent">emit</button>
5.添加方法
data(){
    return{
        hello:"china"
    }
}
methods:{
    emitMyEvent(){
        this.$emit('my-event',this.hello)                              #此处也可以添加参数,我添加了hello
    }
}
```

9.表单的数据绑定

```
<inpiut v-model="value" type="text">  {{value}}
   ===必须要初始化=====
data(){
    return{
        value:"xixi"
    }
}
```

 10.v-model和select一起使用，动态绑定

```java
  <select v-model="selection">
     <option v-for="item in list" v-bind:value="item.value"> {{item.text}}</option>
  </select>
  
  data(){
      return{
          selectiom:null,
          list:[
          {
              text:'apple',
              value:0
          },
            {
              text:'apple',
              value:0
          }
          ]
      }
  }
```

11.计算属性和数据监听

```java
<template>
   <div>
      <input type="text" v-model="myValue"> {{myValueWithoutNum}}
   </div>
</template>
=====数据=======
data(){
 return｛
  myValue：''
 ｝
},
computed:{
    myValueWithoutNum(){
        return this.myValue.replace(/\d/g,'')         //正则表达式 过滤掉数字
    }
}
========数据监听watch======================================
watch:{
    myValue:function(val,oldVal){
        console.log(val,oldVal )  
    }        
 }
```

12.is的使用（动态组件）

```java
1.引入两个组件 comA，comB
2.在components中导入
 export default｛
   components：｛
      comA，comB
    ｝
 ｝
3.如果想引入组件的时候有效果
<div>
  <transition name="fade" mode="out-in">                  //mode:out-in 先出后进，切换的效果
    <div :is="currentView">                               //<div is="com-a"></div>==<com-a></com-a>两个相等
    </div>
  </transition>
</div>
<button v-on:click="toggleCom">切换</button>

data(){
  return{
    currentView:"com-a" 
  }
}，
methods:{
  toggleCom(){
    if(this.currentView=="com-a"){
       this.currentView="com-b"
       }else{
       this.currentView="com-a"
       }
  }
}
```

13.slot插槽的使用

```java
1.我的父组件导入子组件、
2.子组件的div标签里添加slot标签
<div>
  <slot name="header"></slot>
  <p>乱七八糟</p>
  <slot name="footer"></slot>
</div>
3.要想使用插槽
  <com-a>
    <p slot="header">我是头</p>
    <p slot="footer">我是脚</p>
  </com-a>

```

#### 2.vue的高级使用

1.过度

```java
1.过度就是四个阶段
v-enter（进来）， v-enter-active,  v-leave,  v-leave-active（出去）
2.然后在style里面配置效果
<style>
  .fade-enter-active, .fade-leave-active{
      transition:opacity  1s ease-out;
  } 
  .fade-enter, .fade-leave-active{
      opacity:0;                                                                //opacity:0;透明度 
  }
</style>
3.在template里面使用
 <div>
   <transition name="fade">
     <p v-show:"show">i am  show</p>
   </transition>
 </div>  
 <button v-on:click="show=!show">切换</button>
```

2.

```

```

3.

```

```



#### 3.路由的配置route

```
安装：
npm install vue-router --save
```



1.如果要切換頁面

```java
import HelloWorld from '@/components/HelloWorld'       ==導入需要的組件

export default new Router({
  mode:'history',
  routes: [
    { 
      path: '/',                                       ==動態路由
      name: 'HelloWorld',                              ==修改名字
      component: HelloWorld                            ==修改組件
    }
  ]
})
```

2.動態路由，就是配置訪問路徑

```java
想要訪問goodsList，就要按下面的路徑才能訪問
routes: [
    {
      path: '/goodsList/:goodsId',
      name: 'GoodsList',
      component: GoodsList
    }
  ]
====================================
也可以傳兩個參數
 path: '/goodsList/:goodsId/username/:name',
====================================
獲取我們的url信息
 <span>{{$route.params.goodsId}}</span><br>
  <span>{{$route.params.name}}</span>
```

3.訪問路徑帶#號

```properties
在index裡面配置 mode:'history',
1.當mode：'history'   ==>訪問路徑不帶#號
2.當mode：'hash'      ==>f訪問路徑帶#號
```

4.嵌套路由   ====**子路由的地址是绝对路径，不存在拼接和相对路径**  

```java
1.先引入
import Title from '@/views/Title'
import Image from './../views/Image'
2.配置子路由  路由时GoodsList  子路由是child
export default new Router({
  mode:'history',
  routes: [
    {
      path: '/goodsList',
      name: 'GoodsList',
      component: GoodsList,
      children:[
        {
          path:"title",
          name:'title',
          component:Title
        },
        {
          path:"img",
          name:'img',
          component:Image
        }
      ]
    }
  ]
})
========================================    
3.分别在路由中引入两个子路由
  to就是当前路径下
  <router-link to="/apple"></router-link>
  如果是直接拼接在8080后面
  <router-link :to="{path:'apple'}">to apple</router-link>
  <router-link :to="{path:'banana'}">to banana</router-link>
  
  如果是子路由，要把前面的路径也写上
  <router-link to="/GoodsList/title">顯示標題</router-link>
  <router-link to="/GoodsList/img">顯示圖片</router-link>
  
  <router-link :to="{path:''GoodsList/img'}">顯示圖片</router-link>  
```

5.编程时路由

```vue
1.点击直接通过路由跳转
<router-link to='/cart'>跳转到购物车</router-link>
2.添加按钮
  <button @click="jump">button-购物车</button>
  然后再方法里面添加功能
   methods:{
           jump(){
               this.$router.push("/cart")
           }
        }
3.就是使用path路径跳转并且传参
  methods:{
           jump(){
               this.$router.push({path：'/cart？goodsList=123'})
           }
        }
4.回退
 methods:{
           jump(){
               this.$router.go（-2）
           }
        }
5.頁面獲取參數
  主路由：<span>{{$route.params.goodsId}}</span><br>
  子路由：<span>{{$route.query.goodsId}}</span>
```

6.命名路由和命名視圖

```java
1.使用命名路由跳轉，先設定好條件
{
     path: '/cart/:cartId',
     name: 'cart',
     component: Cart
    }
2.跳轉（傳參和不傳參）
 <router-link v-bind:to="{name:'cart'}">跳转到购物车用命名路由</router-link>
 <router-link v-bind:to="{name:'cart',params:{cartId:123}}">跳转到购物车用命名路由</router-link>
```



```java
2.命名視圖
先有視圖
 routes: [
    {
      path: '/goodsList',
      name: 'GoodsList',
      components: {
        default: GoodsList,
        title: Title,
        img:Image
      }
    
    }
  ｝
  再調用
  <router-view class='main'></router-view>
  <router-view class='left' name='title'></router-view>
  <router-view class='right' name='img'></router-view>
  可以添加樣式
  .left,.right{
  float: left;
  width: 49%;
  border:1px solid  gray;
}
```

7.重定向路由

```java
routes：[
    {
        path:'/',
        redirect:'/apple'
    }
]
```

8.keepalive标签 ，生命周期

```vue
我们可以样式里面包着keepalive，然后里面是路由
<transition>
    <keep-alive>
        <router-view></router-view>
    </keep-alive>
</transition>
这样的话我切换路由就会缓存，如果没有的话，每次去请求组件很消耗资源。
```



#### 4.Vue-Resource基礎介紹

1.vue-resource的7中請求，以及簡化寫法

```properties
get(url,[options])
head(url,[options])
delete(url,[options])
jsonp(url,[options])
post(url,[body],[options])
put(url,[body],[options])
patch(url,[body],[options])
============兩者寫法==========
#传统写法
this.$http.get('/someUrl', [options]).then(function(response){
    this.msg = response.data;
}, function(error){
    this.msg = error
});

#Lambda写法
this.$http.get('/someUrl', [options]).then(res => {
   this.msg = res.data;
}, error => {
    this.msg =error;
});

```

2.安裝vue-resource

```java
查看我們的項目目錄，package.json裡面就是我們這個項目的所有依賴
 "dependencies": {                           //上面是項目依賴，下面是開發依賴
    "vue": "^2.5.2",
    "vue-router": "^3.0.1"
  },                                         //項目依賴是上線后還要使用的依賴，開發依賴是開發是使用的依賴
  "devDependencies": {
    "autoprefixer": "^7.1.2",
    "babel-core": "^6.22.1"
   }
=====================================================================
安裝
cnpm i vue-resource --save            //--save   就是安裝到我的項目依賴
```

3.實例

```

```

#### 5.axios的安裝和使用

1.安裝

```
npm install --save  axios
```

2.使用

```

```

#### 6.ES6的使用

1.全句安裝解析插件

```
npm install standard -g
npm install babel-eslint -g
```

2.vscode安裝StandardJS,安裝

```
在setting文件里面添加配置
"standard.options.parser": "babel-eslint"
 "javascript.implicitProjectConfig.experimentalDecorators": true
```

3.擴展  ...

```
1.rest參數，動態參數
 function sum（...m）{
     let total = 0;
     for(var i in m ){
         total += i;
     }
     console.log(`total:${total});
 }
 sum(4,7,8,9,10)  ==>38
2.數組的擴展   ==>當...與數組結合的時候，就是拆分數組
var [x,y] = [4,8];
console.log(...[4,8])
==》最後控制台打印 4 8
3.函數的擴展
let arr1=[1,3];
let arr2=[4,8];
console.log(arr1.concat(arr2));
console.log(...arr1,...arr2);
```

4.impor和export引入和導出

```

```

#### 7.Vuex的使用

1.安装

```
npm install vuex --save
npm run dev
```

2.



####  8.商品列表

​       **公用的组件方法component里面，自己的放到views**

##### 1.拆分商品列表（slot）

```java
1.template的标签必须要有一个根元素
 <template>
  <div>
    <header class="header"。。。>              //都是缩写
    <div class="accessory-list-wrap"...>
    <footer class="footer"...>
  </div>
</template>
2.导入样式
 import './../assets/css/base.css'
 import './../assets/css/protected.css'
3.新建组件Header.vue
 把header复制过来，然后把GoodsList里面的header删掉
 在GoodsList里面引入header，并且使用header组件
 import NavHeader from './../components/Header.vue'
 然后在template里面添加 <nav-header></nav-header>     ==作用是引入
4.我们的static静态资源和src在同级目录下
  比如我现在要在src/conponents/Header.vue里面引入static的图片
  src="/static/logo.png"
5.导入的快捷方式
  import NAVHeader from './../components/Header.vue'
  import NavHeader from '@/components/Header.vue'
6.使用slot插槽
  如果我们在header、footer、bread里面配置了slot，就可以使用
  <slot name="bread"></slot>
  <slot name="b"></slot>
  某个页面引入了header。。。。
  <nav-header>
      <span slot="bread">传参或者其他的</span>
  </nav-header>
```

##### 2.导入数据

```json
我们会新建一个文件夹，如mock，然后创数据，新建goods.json
{
   "status":"0",
   "msg":"",
   "result":[
      {
        "productId":"100001",
        "productName":"手机",
        "productImg":"1.png",
        "porductPrice":5999 
      },
       {
         "productId": "100002",
         "productName": "电视",
         "productImg": "2.png",
         "porductPrice": 999
       },
        {
          "productId": "100003",
          "productName": "音响",
          "productImg": "3.png",
          "porductPrice": 2400
        }
   ]
}
```

##### 3.vue识别数据

```java
找到webpack.dev.conf.js，引入node的express框架，代码如下：
1.首先定义express
const express = require('express')
const app= express()
2.var router = express.Router();
var goodsData = require('./../mock/goods.json');
router.get("/goods",function(req,res,next){
   res.json(goodsData);
});
app.use(router);
3.重启
npm instsall
npm run dev

```

##### 4.安装vue-devtools，F12查报警

```properties
1.下载
https://github.com/vuejs/vue-devtools     geihub clone
2.安装
cd vue-devtools
npm install
3.修改配置文件，vue-devtools/shells/chrome/manifest.json
“persistent”：false改为true
4.编译代码
npm run build
5.扩展chrome插件
chrome浏览器 > 更多程序 > 扩展程序
选择vue-devtools > shells > chrome
```

##### 5.导入数据

```

```

添加价格

```xml
priceFilter：[
    {
        startPrice:'0.00',
        endPrice:'500.00'
    }
    { 
        startPrice:'500.00',
        endPrice:'1000.00'
    }
]
=================循环==========
<div v-for="price in priceFilter">
   <a>{{price.startPrice}}-{{price.endPrice}}</a>
</div>   
```





#### *vscode快捷鍵

```properties
(1)如何快速生成html代碼
   1.先输入一个 !
   2.点击 tab 键
   3.自动生成标准的html代码（见图）
(2)如何快速複製
   shift+alt+向下的箭頭
(3)代碼格式化
   shift+alt+f
(4)自動補全功能實現
   點擊文件 ==》首選頁 ==》設置    setting添加 "files.associations": {"*.vue":"html"}
(5)注释和取消注释
   注释 CTRL+K CTRL+C
   取消注释 CTRL+K CTRL+U
(6)
(7)
```

##### 

#### *學習過程中遇到的問題

```vue
1.我新增的html頁面怎麼瀏覽
在插件裡面查找open in browser，然後安裝就可以用瀏覽器查看該頁面了
2.es6的語法有用到反引號，怎麼打反引號
輸入法切換中/英文標點，點擊eac下面的按鈕
3.npm run dev 报警
npm ERR! code ELIFECYCLE
npm ERR! errno 1
npm ERR! myproject@1.0.0 dev: `webpack-dev-server --inline --progress --config build/webpack.dev.conf.js`
npm ERR! Exit status 1
npm ERR!
npm ERR! Failed at the myproject@1.0.0 dev script.
npm ERR! This is probably not a problem with npm. There is likely additional logging output above.

npm ERR! A complete log of this run can be found in:
npm ERR!     D:\Application\nodejs\node-cache\_logs\2019-04-11T03_27_10_953Z-debug.log
=========================解决==================
第一种方法：npm install之后 运行npm i -D webpack-dev-server@2.9.7     无效
第二种：
4.

```

#### 

