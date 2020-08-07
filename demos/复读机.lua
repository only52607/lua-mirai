Info={}
Info.name="复读机"
Info.author="ooooonly"
Info.version="1.0"
Info.description="复读机"

Event.onLoad = function(bot)
    bot:subscribeFriendMsg(function(bot, msg, sender)
        sender:sendMsg(msg)
    end)

    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        group.sendMsg(msg)
    end)
end