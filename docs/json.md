# 在Lua中处理Json

## 将json反序列化为table

#### 函数名：Json.parseJson

#### 参数列表：

| 参数 | 类型   | 描述       | 可空  |
| ---- | ------ | ---------- | ----- |
| json | String | Json字符串 | False |

#### 返回值：

| 类型  | 描述             |
| ----- | ---------------- |
| Table | 反序列化后的列表 |

## 将table序列化为json

#### 函数名：Json.toJson

#### 参数列表：

| 参数  | 类型  | 描述                                               | 可空  |
| ----- | ----- | -------------------------------------------------- | ----- |
| table | Table | 欲序列化的table（不支持列表与字典类型混合的table） | False |

#### 返回值：

| 类型   | 描述           |
| ------ | -------------- |
| String | 序列化后的Json |



### 调用示例：

```lua
local json = [[
{
	"title":"lua-mirai",
	"json.url":"https://github.com/only52607/lua-mirai/",
	"lang":"lua",
	"keywords":["qq","bot","mirai","lua"]
}
]]

local t = Json.parseJson(json) -- 反序列为table

print(t["title"]) -- 打印"title"项，输出"lua-mirai"

print(Json.toJson(t)) -- 将table转化为json字符串
```


