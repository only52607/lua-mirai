Info={}
Info.name="祖安机器人"
Info.author="ooooonly"
Info.version="1.0"
Info.description="试着向机器人发送‘骂我’"

Event.onLoad = function(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if msg == "骂我" then
            group:sendMsg(Quote(msg) + At(sender) + Net.get("https://nmsl.shadiao.app/api.php?level=min&lang=zh_cn"))

        elseif msg:find("骂他") then
            local at
            for k, v in pairs(msg:toTable()) do
                if v:find("mirai:at") then
                    at = v
                    break
                end
            end
            group:sendMsg(at .. Net.get("https://nmsl.shadiao.app/api.php?lang=zh_cn"))
        end
    end)
end