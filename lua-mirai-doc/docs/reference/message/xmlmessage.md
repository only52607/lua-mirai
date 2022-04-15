# XML 消息格式详解

下面我们将通过这个示例 XML 消息解释 XML 消息的构成和各参数的含义。

```xml
<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
<msg 
     templateID='1' 
     serviceID='1' 
     action='plugin' 
     actionData='' 
     brief='' 
     flag='3' 
     url=''>
    
    <item bg='0' layout='4'>
        <title size='25' color='#000000'>"标题"</title>
    	<summary color='#000000'>"摘要"</summary>
    	<picture cover='http://xxxx/xxx.png'/>
    </item>
    
    <item bg='0' layout='4'>
        <title size='25' color='#000000'>"标题"</title>
    	<summary color='#000000'>"摘要"</summary>
    	<picture cover='http://xxxx/xxx.png'/>
    </item>
    
     <source name='来自 XXX' icon='http://xxxx/xxx.png'/>
</msg>
```



由以上示例可见，该 XML 将 msg 元素作为根元素，msg 元素内部嵌套多个 item 元素，以及一个 source 元素。

下面是对各个元素的详细介绍。



## msg

##### 属性：（标有*为不明确属性）

| 属性名     | 含义                           |
| ---------- | ------------------------------ |
| templateID | 消息样式模板，默认为1          |
| serviceID  | *未知，默认为1                 |
| action     | *消息动作，默认为 plugin       |
| actionData | 一般为点击这条消息后跳转的链接 |
| brief      | *摘要                          |
| flag       | *标志，默认为3                 |
| url        | *跳转 URL                      |



## source

##### 属性：（标有*为不明确属性）

| 属性名 | 含义                         |
| ------ | ---------------------------- |
| name   | 在消息下方显示的来源名称     |
| icon   | 在消息下方显示的来源图标 url |



## item

##### 属性：（标有*为不明确属性）

| 属性名 | 含义              |
| ------ | ----------------- |
| bg     | 背景样式，默认为0 |
| layout | 布局样式，默认为4 |



## title

##### 属性：（标有*为不明确属性）

| 属性名 | 含义     |
| ------ | -------- |
| size   | 字体大小 |
| color  | 显示颜色 |



## summary

##### 属性：（标有*为不明确属性）

| 属性名 | 含义     |
| ------ | -------- |
| color  | 显示颜色 |



## picture

##### 属性：（标有*为不明确属性）

| 属性名 | 含义     |
| ------ | -------- |
| cover  | 图片 url |

