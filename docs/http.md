# 在 Lua 中执行 http 请求

## get 请求

#### 函数名：Http.get

#### 参数列表：

| 参数    | 类型   | 描述       | 可空  |
| ------- | ------ | ---------- | ----- |
| url     | String | 请求地址   | False |
| config  | Table  | 配置参数   | True  |
| headers | Table  | 请求头信息 | True  |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| String  | 响应主体 |
| Boolean | 是否成功 |
| Integer | 状态码   |
| String  | 消息     |

### 调用示例：

```lua
local body,isSuccessful,code,message = Http.get(
    "https://github.com/",

    {
        connectTimeout = 5000,
        readTimeout = 5000,
        followRedirects = true,
        writeTimeout = 5000
    },

    {   -- 请求头
        token = "xxxx"
    }
)
print(body)
```



## post 请求

#### 函数名：Http.post

#### 参数列表：

| 参数    | 类型         | 描述       | 可空  |
| ------- | ------------ | ---------- | ----- |
| url     | String       | 请求地址   | False |
| params  | String/Table | 请求参数   | False |
| config  | Table        | 配置参数   | True  |
| headers | Table        | 请求头信息 | True  |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| String  | 响应主体 |
| Boolean | 是否成功 |
| Integer | 状态码   |
| String  | 消息     |

### 调用示例：

```lua
local body,isSuccessful,code,message = Http.post(
    "https://xxxxxx/login",
    
	"user=xxx&pwd=xxx", 
    -- 也可以使用 { type = "text/plain;chaset=utf-8" ,data = "user=xxx&pwd=xxx" }
    
    {  
        connectTimeout = 5000,
        readTimeout = 5000,
        followRedirects = true,
        writeTimeout = 5000
    }, 

    {   -- 请求头
        token = "xxxx"
    }
)
print(body)
```

## 快速获取重定向地址

#### 函数名：Http.getRedirectUrl

#### 参数列表：

| 参数    | 类型         | 描述       | 可空  |
| ------- | ------------ | ---------- | ----- |
| url     | String       | 欲被重定向的地址   | False |
| referer  | String | referer 头   | True |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| String  | 重定向后的地址 |
