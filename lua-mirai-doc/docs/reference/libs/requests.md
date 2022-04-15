# requests库

::: warning
已弃用，请使用`http`支持库
:::

**基于okhttp3，整体上模仿Python的[requests](https://requests.readthedocs.io/zh_CN/latest/)库。目前仅实现了部分功能。**

## 发送Http请求

**函数名**: requests.request

**参数列表**

| 参数                                     | 类型   | 描述                 | 可空  |
|------------------------------------------|--------|----------------------|-------|
| method                                   | String | 要使用的Http方法     | False |
| url                                      | String | 请求地址             | False |
| [kwargs](/guide/libs/requests.md#kwargs) | Table  | 各类附加参数，见下表 | True  |

**返回值**

| 类型     | 描述   |
|----------|--------|
| Response | 响应体 |

###### kwargs
| 参数              | 类型                                             | 描述                                                  | 可空 |
|-------------------|--------------------------------------------------|-------------------------------------------------------|------|
|   params          | Table                                            | 请求的查询字符串                                      | True |
|   data            | String                                           | 上传给服务器的数据，纯字符串形式。与data和files冲突。 | True |
| + form            | Table                                            | 需要上传的表单。与data和files冲突。                   | True |
|   headers         | Table                                            | 请求头                                                | True |
|   files           | [Table](/guide/libs/requests.md#kwargsfiles)     | 需要上传的文件。与data和files冲突。                   | True |
| + auth            | [Table](/guide/libs/requests.md#kwargsauth)      | HTTP身份验证                                          | True |
|   timeout         | [Table](/guide/libs/requests.md#kwargstimeout)   | 链接超时时间                                          | True |
|   allow_redirects | Boolean                                          | 是否允许自动重定向                                    | True |
| - verify          | String                                           | [见下表](/guide/libs/requests.md#kwargsverify)        | True |
| + cert            | String                                           | [见下表](/guide/libs/requests.md#kwargsverify)        | True |

本表中，类型为`Table`且没有特殊说明的参数，均为KV对组成的表。[示例](/guide/libs/requests.md#示例1)  
本表中，带`+`的为未实现的功能，带`-`的为实现了部分的功能。另有部分功能(均未实现)暂时没有写到本表中。

###### kwargs.files
| 参数   | 类型   | 描述                 | 可空  |
|--------|--------|----------------------|-------|
| filename | String | 文件名     | False |

###### kwargs.auth
| 参数   | 类型   | 描述                 | 可空  |
|--------|--------|----------------------|-------|
| user | String | 用户名     | False |
| password | String | 密码     | False |

###### kwargs.timeout
| 参数           | 类型    | 描述                       | 可空  |
|----------------|---------|----------------------------|-------|
| connectTimeout | Integer | 连接超时时间，单位ms       | False |
| readTimeout    | Integer | 网络流读取超时时间，单位ms | True  |
| writeTimeout   | Integer | 网络流写入超时时间，单位ms | True  |

###### kwargs.verfy

verify和cert在reques库中都是和证书相关的参数，特别是verify参数在自定义证书路径时，和cert参数看上去是很相似的。  
在没搞懂这两个参数的具体实现前，cert参数的功能不提供，verify仅提供 *是否信任不安全证书* 的功能。

verify设置为`"true"`时，为**仅信任安全证书**，为`"false"`时，为**信任不安全证书**

###### 示例1
``` lua
requests.request("POST", "http://example.com", {
    params={
        ["key1"]="value1",
        key2="value2"
    },
    data="example for plaintext",   -- 注意: 这里仅作为演示，实际使用中data form files三个参数不能共存。
    form={                          -- 注意: 这里仅作为演示，实际使用中data form files三个参数不能共存。
        query="114514",
        ["token"]="19260817"
    },
    headers={                       -- header中的键建议使用["<key>"]的形式，其他地方的键如果带除下划线以外的字符，同样需要用这种格式
        ["User-Agent"]="Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0"
    },
    files={                         -- 注意: 这里仅作为演示，实际使用中data form files三个参数不能共存。
        "example.csv"               -- files auth timeout三个参数实际为数组，可以不用也不建议设置键。标注其内部的参数仅为了表述方便
    },
    auth={"admin", "pa$$wd"},       -- files auth timeout三个参数实际为数组，可以不用也不建议设置键。标注其内部的参数仅为了表述方便
    timeout={1000, 2000},           -- files auth timeout三个参数实际为数组，可以不用也不建议设置键。标注其内部的参数仅为了表述方便
                                    -- 注意: timeout参数是有顺序的，必须先设置connectTimeout，再设置readTimeout，最终才能设置writeTimeout
    allow_redirects=false,
    verify="false"
})
```


## 发送HEAD请求

**函数名**: requests.head

**参数列表**

| 参数                                     | 类型   | 描述                 | 可空  |
|------------------------------------------|--------|----------------------|-------|
| url                                      | String | 请求地址             | False |
| [kwargs](/guide/libs/requests.md#kwargs) | Table  | 各类附加参数，见下表 | True  |

**返回值**

| 类型     | 描述   |
|----------|--------|
| Response | 响应体 |


## 发送GET请求

**函数名**: requests.get

**参数列表**

| 参数                                     | 类型   | 描述                 | 可空  |
|------------------------------------------|--------|----------------------|-------|
| url                                      | String | 请求地址             | False |
| [kwargs](/guide/libs/requests.md#kwargs) | Table  | 各类附加参数，见下表 | True  |

**返回值**

| 类型     | 描述   |
|----------|--------|
| Response | 响应体 |


## 发送POST请求

**函数名**: requests.post

**参数列表**

| 参数                                     | 类型   | 描述                 | 可空  |
|------------------------------------------|--------|----------------------|-------|
| url                                      | String | 请求地址             | False |
| [kwargs](/guide/libs/requests.md#kwargs) | Table  | 各类附加参数，见下表 | True  |

**返回值**

| 类型     | 描述   |
|----------|--------|
| Response | 响应体 |


## 发送PUT请求

**函数名**: requests.put

**参数列表**

| 参数                                     | 类型   | 描述                 | 可空  |
|------------------------------------------|--------|----------------------|-------|
| url                                      | String | 请求地址             | False |
| [kwargs](/guide/libs/requests.md#kwargs) | Table  | 各类附加参数，见下表 | True  |

**返回值**

| 类型     | 描述   |
|----------|--------|
| Response | 响应体 |


## 发送PATCH请求

**函数名**: requests.patch

**参数列表**

| 参数                                     | 类型   | 描述                 | 可空  |
|------------------------------------------|--------|----------------------|-------|
| url                                      | String | 请求地址             | False |
| [kwargs](/guide/libs/requests.md#kwargs) | Table  | 各类附加参数，见下表 | True  |

**返回值**

| 类型     | 描述   |
|----------|--------|
| Response | 响应体 |


## 发送DELETE请求

**函数名**: requests.delete

**参数列表**

| 参数                                     | 类型   | 描述                 | 可空  |
|------------------------------------------|--------|----------------------|-------|
| url                                      | String | 请求地址             | False |
| [kwargs](/guide/libs/requests.md#kwargs) | Table  | 各类附加参数，见下表 | True  |

**返回值**

| 类型     | 描述   |
|----------|--------|
| Response | 响应体 |


## Response

#### 成员
|   成员名                | 成员类型         | 描述                                        |
| ----------------------- | ---------------- | ------------------------------------------- |
|   content               |  Byte[]          | 响应内容的原始数据                          |
| + cookies               |  CookieJar       | 服务器返回的Cookies                         |
|   elapsed               |  Long            | 从发送到接收总共花费的时间                  |
|   encoding              |  String          | text所用的编码                              |
|   headers               |  Table           | 响应头                                      |
|   is_permanent_redirect |  Boolean         | 是否为永久重定向(302)                       |
|   is_redirect           |  Boolean         | 是否为重定向(301, 302)                      |
| + links                 |  String          | 返回响应的解析头链接                        |
| + next                  |  PreparedRequest | 返回重定向链中下一个请求的PreparedRequest   |
|   ok                    |  Boolean         | status_code < 400 时为true                  |
|   raw                   |  Byte[]          | 响应内容的原始流                            |
|   reason                |  String          | 文字形式的HTTP状态，例如"Not Found"或者"OK" |
| + request               |  PreparedRequest | 响应的PreparedRequest对象                   |
|   status_code           |  Integer         | 数字形式的HTTP状态，例如"404"或者"200"      |
|   text                  |  String          | 响应内容，字符编码为encoding                |
|   url                   |  String          | 最终的URL                                   |
|                         |                  |                                             |
|   close()               |                  | 关闭该响应体以释放资源                      |
|   setEncoding(String)   |                  | 重新设置编码格式，同时刷新text              |

本表中，带`+`的为未实现的功能，带`-`的为实现了部分的功能。另有部分功能(均未实现)暂时没有写到本表中。
