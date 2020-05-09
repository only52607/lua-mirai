Event.onLoad = function(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if msg:find("撤回") then --可自定义关键词
            msg:recall()
        end
    end)
end

