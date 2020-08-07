Info={}
Info.name="复读机pro"
Info.author="ooooonly"
Info.version="1.0"
Info.description="机器人会按照概率随机复读"

--按概率随机复读
function Event.onLoad(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if math.random(1, 10) == 1 then
            group:sendMsg(msg)
        end
    end)
end