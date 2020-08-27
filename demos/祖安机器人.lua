Info={}
Info.name="祖安机器人"
Info.author="ooooonly"
Info.version="1.0"
Info.description="试着向机器人发送‘骂我’"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

function onLoad(bot)
    bot:subscribe("GroupMessageEvent",function(event)
        if event.message == "骂我" then
            event.group:sendMessage(Quote(msg) + At(sender) + Http.get("https://nmsl.shadiao.app/api.php?level=min&lang=zh_cn"))

        elseif event.message:find("骂他") then
            local at
            for k, v in pairs(event.message:toTable()) do
                if v:find("mirai:at") then
                    at = v
                    break
                end
            end
            event.group:sendMessage(at .. Http.get("https://nmsl.shadiao.app/api.php?lang=zh_cn"))
        end
    end)
end