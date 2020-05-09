Event.onLoad = function(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if msg == "夸我" then
            group:sendMsg(Quote(msg) .. Net.get("https://chp.shadiao.app/api.php"))
        end
    end)
end

