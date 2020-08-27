Info={}
Info.name="音乐卡片"
Info.author="thebadzhang"
Info.version="1.0"
Info.description="可以发送网易云热歌榜的音乐卡片"

--[[ 单独运行时启用此代码
    local bot = Bot(123456,"abcderf","device.json") -- 替换为自己的账号密码
    bot:login()
    onLoad(bot)
]]


local formAMusicShare = function (title, srcUrl, desc, preview, jmpUrl, tag)
	-- 从左到右依次为，标题，音源，歌手，封面，跳转链接，小字
		if not tag then tag = "QQbot 音乐" end
		-- 默认的 bot 名字
		if not jmpUrl then jmpUrl = "https://music.163.com/" end
		-- 若没设置跳转链接，则跳转到 网易云音乐
		if not preview then preview = "" end
		-- preview 专辑封面可有可无啦
		assert (title, "对不起啦，我希望你必须给一个标题啦")
		assert (srcUrl, "你没给音乐你放什么？？？")
		assert (desc, "对不起啦，我还是希望你放上作者啦")
	
		local __format__ = [[
{"app":"com.tencent.structmsg","config":{"autosize":true,"forward":true,"type":"normal"},
"desc":"音乐","meta":{"music":{"desc":"%s","jumpUrl":"%s","musicUrl":"%s","preview":"%s",
"tag":"%s","title":"%s"}},"prompt":"[%s]%s","view":"music"}]]
		return string.format (__format__:gsub("\n", ""),   -- 这一步在去掉换行，其实不去也无所谓
				desc, jmpUrl, srcUrl, preview, tag, title, tag, title
		)
end

-- 下面的代码抄了一点隔壁的关键词撤回

local responses = {
	["网易云"] = function (bot, msg, group, sender)
		local boards = { ['热歌榜'] = true, ['新歌榜'] = true, ['飙升榜'] = true, ['抖音榜'] = true, ['电音榜'] = true }
		-- 这个 API 提供的五种榜单
		local board = msg:match ("网易云(...)")
		-- 获取我选择的榜单
		local ret = ""
		-- 获取 API 返回的 json
		if boards [board] then
		-- 若我选择的某一榜单，而且榜单存在，则返回这个榜单的 json
			ret = Http.get ("https://api.uomg.com/api/rand.music?format=json&sort="..board)
		else
		-- 没有选择，则默认为 热歌榜
		ret = Http.get ("https://api.uomg.com/api/rand.music?format=json&sort=热歌榜")
		end
		if ret then
			return App (formAMusicShare (
				ret:match ("\"name\":\"(.-)\""),
				ret:match ("\"url\":\"(.-)\""),
				ret:match ("\"artistsname\":\"(.-)\""),
				ret:match ("\"picurl\":\"(.-)\"")
			))
		else
			return "错误！出现致命错误！"
		end
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