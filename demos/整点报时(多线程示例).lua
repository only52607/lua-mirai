Info={}
Info.name="整点报时"
Info.author="ooooonly"
Info.version="1.0"
Info.description="整点报时"

function Event.onLoad(bot)
    local isReport
    bot:subscribeGroupMsg(function(bot, msg, group, sender)
        if msg == "开启整点报时" then
            --使用bot启动一个线程
            bot:launch(function()
                isReport = true
                local remainMinutes = 60 - tonumber(os.date("%M", os.time()))
                sleep(remainMinutes * 60 * 1000)
                while isReport do
                    sender:sendMsg("整点报时，现在是：" .. tonumber(os.date("%H", os.time())) .. "时0分")
                    sleep(60 * 60 * 1000) --延迟1小时
                end
            end)

        elseif msg == "关闭整点报时" then
            isReport = false
        end
    end)
end