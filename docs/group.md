# 群 (Group)

## 属性

| 属性名 | 类型                            | 描述    |
| ------ | ------------------------------- | ------- |
| id     | Integer                         | 群号码  |
| bot    | [`Bot`](/docs/bot.md) | bot 对象 |
| avatarUrl | String | 群头像地址 |
| name | String | 群名称 |
| owner | [`Member`](/docs/member.md) | 群主 |
| settings | GroupSettings | 群设置 |
| botPermission | MemberPermission | 机器人在群里的权限 |
| isBotMuted | Boolean | 机器人是否被禁言 |
| botAsMember | [`Member`](/docs/member.md) | 机器人在群内的自身 member 对象 |
| botMuteRemaining | Integer | 机器人被禁言时长 |

## 方法

### sendMessage (发送消息)

#### 参数列表：

| 参数 | 类型                            | 描述     |
| ---- | ------------------------------- | -------- |
| message  | [`Message`](/docs/message.md) | 消息对象 |

#### 返回值：

| 类型                          | 描述                   |
| ----------------------------- | ---------------------- |
| [`Message`](/docs/message.md) | 消息对象，可用于撤回。 |



### sendImage(发送图片消息)

#### 参数列表：

| 参数 | 类型                            | 描述     |
| ---- | ------------------------------- | -------- |
| url  | String | 图片 URL |

#### 返回值：

| 类型                          | 描述                   |
| ----------------------------- | ---------------------- |
| [`Message`](/docs/message.md) | 消息对象，可用于撤回。 |






### getMembers (获取群所有成员)

#### 参数列表：无

#### 返回值：

| 类型                                  | 描述             |
| ------------------------------------- | ---------------- |
| Table of [`Member`](/docs/member.md) | 获取到的群成员对象列表 |



### getMember (获取群成员)

#### 参数列表：

| 参数 | 类型    | 描述       |
| ---- | ------- | ---------- |
| id   | Integer | 群成员 qq 号 |

#### 返回值：

| 类型                                            | 描述               |
| ----------------------------------------------- | ------------------ |
| [`Member`](/docs/member.md) | 获取到的群成员对象 |

### containsMember (判断是否包含群成员)

#### 参数列表：

| 参数 | 类型    | 描述       |
| ---- | ------- | ---------- |
| id   | Integer | 群成员 qq 号 |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| Boolean | 是否包含 |


### quit (退出群)

#### 参数列表：无