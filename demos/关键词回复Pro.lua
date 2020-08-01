Info={}
Info.name="关键词回复pro"
Info.author="ooooonly"
Info.version="1.0"
Info.description="可自定义回复内容"

--支持使用lua模式匹配
local responses = {
    ["夸我"] = function(msg)
        return Net.get(Quote(msg) .. "https://chp.shadiao.app/api.php")
    end,
    ["骂我"] = function(msg)
        return Quote(msg) + At(sender) + Net.get("https://nmsl.shadiao.app/api.php?level=min&lang=zh_cn")
    end
}

local function checkResponse(msg)
    for k, v in pairs(responses) do
        if (msg:find(k)) then
            return v(msg)
        end
    end
    return nil
end

function Event.onLoad(bot)
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        local resp = checkResponse(msg)
        if resp then group:sendMsg(resp) end
    end)
end