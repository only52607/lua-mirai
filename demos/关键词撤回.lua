Info={}
Info.name="关键词撤回"
Info.author="ooooonly"
Info.version="1.0"
Info.description="默认撤回关键词为‘撤回’"

Event.onLoad = function(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if msg:find("撤回") then --可自定义关键词
            msg:recall()
        end
    end)
end

