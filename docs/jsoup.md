# Jsoup支持库

#### 支持库基于jsoup，详细api文档请查询[jsoup](https://www.open-open.com/jsoup/)，用法与java类似。

### 解析XML/HTML内容

##### 函数名：Jsoup.parse

##### 参数列表：

| 参数    | 类型   | 描述         | 可空  |
| ------- | ------ | ------------ | ----- |
| content | String | XML/HTML内容 | False |

##### 返回值：

| 类型     | 描述                 |
| -------- | -------------------- |
| Document | 解析后的Document对象 |

#### 示例：

```lua
local html = [[
        <html>
            <head>
                <title>First parse</title>
            </head>
            <body>
                <p id = "content" >Parsed HTML into a doc.</p>
            </body>
        </html>
]]
local doc = Jsoup.parse(html)
local ele = doc:getElementById("content")
print(ele:text())
```
