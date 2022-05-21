---
home: true
title: Lua Mirai
heroImage: /images/logo.png
actions:
  - text: 快速开始
    link: /guide/introduce.md
    type: primary
  - text: Github
    link: https://github.com/only52607/lua-mirai
    type: secondary
features:
  - title: 简单、轻便
    details: 几行代码即可实现bot
  - title: 零配置
    details: 无需繁琐配置
  - title: 快速上手
    details: 10分钟即可上手
 
footer:  Copyright © 2021-present Lua Mirai
---


#### 实现"AI"仅需4行
```lua
Event.subscribe("FriendMessageEvent", function(event)
    local rep = tostring(event.message):gsub("吗",""):gsub("?","!"):gsub("？","！")
    event.sender:sendMessage(Quote(event.message) .. rep)
end)
```