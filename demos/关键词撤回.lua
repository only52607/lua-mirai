Info={}
Info.name="关键词撤回"
Info.author="ooooonly"
Info.version="1.0"
Info.description="默认撤回关键词为‘撤回’"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

function onLoad(bot)
    bot:subscribe("GroupMessageEvent",function(event)
        if event.message:find("撤回") then --可自定义关键词
            event.message:recall()
        end
    end)
end

