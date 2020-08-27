Info={}
Info.name="整点报时"
Info.author="ooooonly"
Info.version="1.0"
Info.description="整点报时"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]

function Event.onLoad(bot)
    local isReport
    bot:subscribe("GroupMessageEvent",function(event)
        if event.message == "开启整点报时" then
            --使用bot启动一个线程
            bot:launch(function()
                isReport = true
                local remainMinutes = 60 - tonumber(os.date("%M", os.time()))
                sleep(remainMinutes * 60 * 1000)
                while isReport do
                    event.sender:sendMessage("整点报时，现在是：" .. tonumber(os.date("%H", os.time())) .. "时0分")
                    sleep(60 * 60 * 1000) --延迟1小时
                end
            end)

        elseif event.message == "关闭整点报时" then
            isReport = false
        end
    end)
end