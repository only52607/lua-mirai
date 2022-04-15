# 机器人（Bot）参考

## 属性

| 属性名    | 类型                                         | 描述                                                         |
| --------- | -------------------------------------------- | ------------------------------------------------------------ |
| id        | number                                          | bot 的 qq 号码                                               |
| nick      | string                                       | bot 的昵称                                                   |
| asFriend  | [Friend](./contact.md#好友-friend) | bot 自身的 Friend 对象                                       |
| isOnline  | boolean                                      | 是否在线                                                     |
| friends   | ContactList                                  | 好友列表                                                     |
| groups    | ContactList                                  | 群组列表                                                     |
| avatarUrl | string                                       | 头像Url                                                      |
| account   | BotAccount                                   | bot 的基本信息，包含：<br/>"id"<br/>"passwordMd5"<br/>"phoneNumber" |
| bkn       | number                                          | 使用某些接口时需要的参数，请勿滥用                           |

---

## 方法

### getFriend

#### 说明: 
获取好友

#### 参数列表：

| 参数 | 类型    | 描述     |
| ---- | ------- | -------- |
| id   | number | 好友 qq 号 |

#### 返回值：

| 类型                               | 描述             |
| ---------------------------------- | ---------------- |
| [Friend](./contact.md#好友-friend) | 获取到的好友对象 |

### getFriends

#### 说明: 

获取好友列表（耗时操作，慎用）

#### 参数列表：无

#### 返回值：

| 类型                                  | 描述             |
| ------------------------------------- | ---------------- |
| Table of [Friend](./contact.md#好友-friend) | 获取到的好友对象列表 |


### getGroup/getGroupByUin

#### 说明: 

获取群

#### 参数列表：

| 参数 | 类型    | 描述 |
| ---- | ------- | ---- |
| id   | number | 群号 |

#### 返回值：

| 类型                                | 描述           |
| ----------------------------------- | -------------- |
| [Group](./contact.md#群-group) | 获取到的群对象 |

### getGroups

#### 说明: 

获取群列表(耗时操作，慎用)

#### 参数列表：无

#### 返回值：

| 类型                                  | 描述             |
| ------------------------------------- | ---------------- |
| Table of [Group](./contact.md#群-group) | 获取到的群对象列表 |


### containsFriend

#### 说明: 

判断好友是否存在

#### 参数列表：

| 参数 | 类型    | 描述     |
| ---- | ------- | -------- |
| id   | number | 好友 qq 号 |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| boolean | 是否存在 |

### containsGroup 

#### 说明: 

判断群是否存在

#### 参数列表：

| 参数 | 类型    | 描述 |
| ---- | ------- | ---- |
| id   | number | 群号 |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| boolean | 是否存在 |



### subscribe

#### 说明: 

订阅事件

##### 详细用法见 [`事件`](../guide/event.md)

#### 参数列表：

| 参数     | 类型     | 描述     |
| -------- | -------- | -------- |
| event    | string   | 事件标识 |
| callback | Function | 回调函数 |

### launch

#### 说明: 

立即启动一个线程

#### 参数列表：

| 参数  | 类型     | 描述       |
| ----- | -------- | ---------- |
| block | Function | 线程主函数 |

### join

#### 说明: 

加入该 bot 防止退出(console下勿用)

#### 参数列表：无

### close

#### 说明: 

关闭该 bot

#### 参数列表：无