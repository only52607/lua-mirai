# Json 消息格式略解

下面我们将通过这个示例 Json 消息解释 Json 消息的构成和各参数的含义。

```json
{
	"app":"com.tencent.structmsg",
	"view":"music",
	"desc":"音乐",
	"ver":"0.0.0.1",
	"prompt":"[分享]乘着小飞机去找你",
	"config":{
		"autosize":true,
		"ctime":1596884790,
		"forward":true,
		"token":"7303915bfa20f26f2e2171813cbb020a",
		"type":"normal"
	},
	"extra":{
		"app_type":1,
		"appid":100497308,
		"msg_seq":6858567928267689455
	},
	"meta":{
		"music":{
			"action":"",
			"android_pkg_name":"",
			"app_type":1,
			"appid":100497308,
			"desc":"文橘/Cher",
			"jumpUrl":"https://i.y.qq.com/v8/playsong.html?platform=11&appshare=android_qq&appversion=9090508&hosteuin=oK4lNKcsoin5Nn**&songmid=002G9sHk2HfhfM&type=0&appsongtype=1&_wv=1&source=qq&ADTAG=qfshare",
			"musicUrl":"http://c6.y.qq.com/rsc/fcgi-bin/fcg_pyq_play.fcg?songid=&songmid=002G9sHk2HfhfM&songtype=1&fromtag=50&uin=1579863018&code=463BB",
			"preview":"http://y.gtimg.cn/music/photo_new/T002R150x150M000002zGuIB3jHDfd_1.jpg",
			"sourceMsgId":"0",
			"source_icon":"",
			"source_url":"",
			"tag":"QQ 音乐",
			"title":"乘着 小飞机去找你"
		}
	}
}
```
```
[分享]乘着小飞机去找你\n 文橘/Cher\nhttps://i.y.qq.com/v8/playsong.html?platform=11&appshare=android_qq&appversion=9090508&hosteuin=oK4lNKcsoin5Nn**&songmid=002G9sHk2HfhfM&type=0&appsongtype=1&_wv=1&source=qq&ADTAG=qfshare\n 来自: QQ 音乐
```
![音乐卡片](../../res/musicCardSample.png)

下面是对各个元素的详细介绍。
（标有*为不明确属性）

| 属性                     | 含义                                                         | 可空？  |
| :----------------------- | :----------------------------------------------------------- | :------ |
| [app](#app)              | 如何解析这段 json                                            | 不可    |
| [view](#view)            | *如何展示卡片                                                | unknown  |
| [desc](#desc)            | *卡片作者，来源                                              | unknown  |
| [ver](#ver)              | 版本号                                                       | *unknown |
| [prompt](#prompt)        | *当不能显示这个 app 时所显示的文字<br/>当连 json 都不能读取的时候需要在外面写上信息，就像上面第二个代码块中的内容其实不属于卡片但是时消息的一部分 | unknown  |
| [config](#config)        | 配置信息（我也不知道具体配置了啥）                           | unknown  |
| [extra](#extra)          | 额外信息                                                     | 可      |
| [meta.music](#metamusic) | 音乐卡片的具体信息                                           | 不可    |

### app

目前发现有下面三种格式

```
com.tencent.structmsg
com.tencent.miniapp
com.tencent.qqpay.qqmp.groupmsg
```
分别对应着

![音乐卡片](../../res/musicCardSample.png)

![【优质回答】我不知道](../../res/jsonCard2.jpg)

![欢迎入群，来了就不要走哦](../../res/jsonCard3.png)

现在只详细解析第一个

### view
### desc
### ver
### prompt

## config

下面是对各个元素的详细介绍。
（标有*为不明确）

|     属性 | 含义      | 可空？ |
| -------: | :-------- | :----- |
| autosize | *自动大小 | unknown |
|    ctime | 时间戳    | 可     |
|  forward | unknown    | unknown |
|    token | token     | 可     |
|     type | unknown    | unknown |

## extra

下面是对各个元素的详细介绍。
（标有*为不明确）

|     属性 | 含义   | 可空？ |
| -------: | :----- | :----- |
| app_type | unknown | 可     |
|    appid | unknown | 可     |
|  msg_seq | unknown | 可     |

## meta.music

下面是对各个元素的详细介绍。
（标有*为不明确）

![音乐卡片](../../res/musicCardSample.png)

|             属性 | 含义             | 可空？ |
| ---------------: | :--------------- | :----- |
|           action | *行为            | 可     |
| android_pkg_name | 包名             | 可     |
|         app_type | unknown          | 可     |
|            appid | unknown           | 可     |
|             desc | 歌手             | *不可  |
|          jumpUrl | 点击后跳转的链接 | *不可  |
|         musicUrl | 播放音乐的源     | *不可  |
|          preview | 音乐的封面       | *不可  |
|      sourceMsgId | unknown           | 可     |
|      source_icon | unknown           | 可     |
|       source_url | 来源链接         | 可     |
|              tag | 下方小字         | *不可  |
|            title | 音乐标题         | *不可  |



