--按概率随机复读
function Event.onLoad(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if msg == "虚晃一枪" then
            group:sendMsg("看不见我"):recall()
        end
    end)
end