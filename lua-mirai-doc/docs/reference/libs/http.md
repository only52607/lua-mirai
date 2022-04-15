# http支持库

## 基本请求

#### 函数名：

| 方法    | 函数     |
| ------- | -------- |
| GET  | Http.get |
| POST | Http.post |
| DELETE | Http.delete   |
| PUT  | Http.put     |
| PATCH  | Http.patch     |


#### 参数列表：

| 参数    | 类型   | 描述       | 可空  |
| ------- | ------ | ---------- | ----- |
| url     | string | 请求地址   | False |
| body  | nil  | 请求体（使用GET时，应该为nil）   | True  |
| config | table  | 参数 | True  |

#### config类型

| 名称    | 类型   | 描述       |
| ------- | ------ | ---------- |
| headers     | table | headers表   |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| string  | 响应主体 |
| table | Response |

### 调用示例：

```lua
local body, data = Http.get("https://github.com/")
print(body)
for k,v in pairs(data) do
    print(k .. ":" .. tostring(v))
end
```

```lua
local body, data = Http.post(
    "https://xxxx.com/login", 
    "user=xxx&pwd=xxx" , 
    {
        headers = {
            xxx = xxx
        }
    }
)
print(body)
for k,v in pairs(data) do
    print(k .. ":" .. tostring(v))
end
```

## 快速获取重定向地址

#### 函数名：Http.getRedirectUrl

#### 参数列表：

| 参数    | 类型   | 描述             | 可空  |
| ------- | ------ | ---------------- | ----- |
| url     | string | 欲被重定向的地址 | False |
| referer | string | referer 头       | True  |

#### 返回值：

| 类型   | 描述           |
| ------ | -------------- |
| string | 重定向后的地址 |
