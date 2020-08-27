Info={}
Info.name="复读机pro"
Info.author="ooooonly"
Info.version="1.0"
Info.description="机器人会按照概率随机复读"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

--按概率随机复读
function onLoad(bot)
    bot:subscribe("GroupMessageEvent",function(event)
        if math.random(1, 10) == 1 then
            event.group:sendMessage(event.message)
        end
    end)
end