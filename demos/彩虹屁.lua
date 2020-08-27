Info={}
Info.name="彩虹屁"
Info.author="ooooonly"
Info.version="1.0"
Info.description="试着向机器人发送‘夸我’"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

function onLoad(bot)
    bot:subscribe("GroupMessageEvent",function(event)
        if msg == "夸我" then
            group:sendMsg(Quote(msg) .. Http.get("https://chp.shadiao.app/api.php"))
        end
    end)
end

