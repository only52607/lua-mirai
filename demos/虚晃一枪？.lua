
--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

function onLoad(bot)
    bot:subscribe("GroupMessageEvent",function(event)
        if event.message == "虚晃一枪" then
            event.group:sendMessage("看不见我"):recall()
        end
    end)
end