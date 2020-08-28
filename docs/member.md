# 群成员 (Member)

## 属性

| 属性名 | 类型                                | 描述         |
| ------ | ----------------------------------- | ------------ |
| id     | Integer                             | 群员的 qq 号码 |
| bot    | [`Bot`](/docs/bot.md)     | bot 对象      |
| group  | [`Group`](/docs/group.md) | 群员所在群   |
| nick | String | 昵称 |
| nameCard | String | 群名片 |
| specialTitle | String | 头衔 |
| isAdministrator | Boolean | 是否为管理员 |
| isOwner | Boolean | 是否为群主 |
| isFriend | Boolean | 是否为好友 |
| muteTimeRemaining | Integer | 被禁言时长 |
| isMuted | Boolean | 是否被禁言 |
| permission | MemberPermission | 成员权限 |

## 方法

### mute (禁言)

#### 参数列表：

| 参数 | 类型    | 描述     |
| ---- | ------- | -------- |
| id   | Integer | 禁言时间 |

### unMute (解除禁言)

### kick (移除群聊)


### asFriend (转为 Friend 对象)

#### 返回值：

| 类型                                  | 描述                    |
| ------------------------------------- | ----------------------- |
| [`Friend`](/docs/friend.md) | 转换后的 Friend 对象 |

### sendMessage (发送私聊消息)

#### 参数列表：

| 参数 | 类型                            | 描述     |
| ---- | ------------------------------- | -------- |
| message  | [`Message`](/docs/message.md) | 消息对象 |

#### 返回值：

| 类型                          | 描述                   |
| ----------------------------- | ---------------------- |
| [`Message`](/docs/message.md) | 消息对象，可用于撤回。 |



### sendImage (发送图片消息)

#### 参数列表：

| 参数 | 类型                            | 描述     |
| ---- | ------------------------------- | -------- |
| url  | String | 图片 URL |

#### 返回值：

| 类型                          | 描述                   |
| ----------------------------- | ---------------------- |
| [`Message`](/docs/message.md) | 消息对象，可用于撤回。 |

