# 在 Lua 中处理 Json

## 将 json 反序列化为 table

#### 函数名：Json.parseJson（包含在String元方法中）

#### 参数列表：

| 参数 | 类型   | 描述       | 可空  |
| ---- | ------ | ---------- | ----- |
| json | String | Json 字符串 | False |

#### 返回值：

| 类型  | 描述             |
| ----- | ---------------- |
| Table | 反序列化后的列表 |





## 将 table 序列化为 json

#### 函数名：Json.toJson（包含在Table元方法中）

#### 参数列表：

| 参数  | 类型  | 描述                                               | 可空  |
| ----- | ----- | -------------------------------------------------- | ----- |
| table | Table | 欲序列化的 table（不支持列表与字典类型混合的 table） | False |

#### 返回值：

| 类型   | 描述           |
| ------ | -------------- |
| String | 序列化后的 Json |



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

local t = Json.parseJson(json) -- 反序列为 table

print(t["title"]) -- 打印"title"项，输出"lua-mirai"

print(Json.toJson(t)) -- 将 table 转化为 json 字符串
```


