Event.onLoad = function(bot)
    bot:subscribeFriendMsg(function(bot, msg, sender)
        sender:sendMsg(msg)
    end)

    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        group.sendMsg(msg)
    end)
end