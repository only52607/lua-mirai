function listener(event)
    local msg = event.message
    local sender = event.sender
    local rep = msg:gsub("吗",""):gsub("?","!"):gsub("？","！")
    sender:sendMessage(Quote(msg) .. rep)
end

function onLoad(bot)
    bot:subscribe("FriendMessageEvent",listener)
end

local bot = Bot(账号,"密码")
bot:login()
onLoad(bot)