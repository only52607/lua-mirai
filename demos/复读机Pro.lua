--按概率随机复读
function Event.onLoad(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if math.random(1, 10) == 1 then
            group:sendMsg(msg)
        end
    end)
end