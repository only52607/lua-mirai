Info={}
Info.name="彩虹屁"
Info.author="ooooonly"
Info.version="1.0"
Info.description="试着向机器人发送‘夸我’"

Event.onLoad = function(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if msg == "夸我" then
            group:sendMsg(Quote(msg) .. Net.get("https://chp.shadiao.app/api.php"))
        end
    end)
end

