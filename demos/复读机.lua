Info={}
Info.name="复读机"
Info.author="ooooonly"
Info.version="1.0"
Info.description="复读机"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

function onLoad(bot)
    bot:subscribe("FriendMessageEvent",function(event)
        event.sender:sendMessage(event.message)
    end)

    bot:subscribe("GroupMessageEvent",function(event)
        event.group:sendMessage(event.message)
    end)
end