Info={}
Info.name="关键词回复pro"
Info.author="ooooonly"
Info.version="1.0"
Info.description="可自定义回复内容"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

local responses = {
    ["夸我"] = function(msg)
        return Http.get(Quote(msg) .. "https://chp.shadiao.app/api.php")
    end,
    ["骂我"] = function(msg)
        return Quote(msg) + At(sender) + Http.get("https://nmsl.shadiao.app/api.php?level=min&lang=zh_cn")
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

function onLoad(bot)
    bot:subscribe("GroupMessageEvent",function(event)
        local resp = checkResponse(event.message)
        if resp then event.group:sendMessage(resp) end
    end)
end