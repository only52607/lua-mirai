# String扩展支持库

### 截取子字符串

##### 函数名：string.substring

##### 参数列表：

| 参数       | 类型    | 描述           | 可空  |
| ---------- | ------- | -------------- | ----- |
| self       | String  | 被处理的string | False |
| startIndex | Integer | 开始截取位置   | False |
| length     | Integer | 截取长度       | True  |

##### 返回值：

| 类型   | 描述           |
| ------ | -------------- |
| String | 处理后的String |

### 字符串长度

##### 函数名：string.length

##### 参数列表：

| 参数 | 类型   | 描述           | 可空  |
| ---- | ------ | -------------- | ----- |
| self | String | 被处理的string | False |

##### 返回值：

| 类型    | 描述       |
| ------- | ---------- |
| Integer | 字符串长度 |

### 字符串编码转换

##### 函数名：string.encode

##### 参数列表：

| 参数          | 类型   | 描述           | 可空  |
| ------------- | ------ | -------------- | ----- |
| self          | String | 被处理的string | False |
| rawCharset    | String | 源字符串编码   | False |
| targetCharset | String | 转换后的编码   | False |

##### 返回值：

| 类型   | 描述           |
| ------ | -------------- |
| String | 处理后的String |

### 字符串URL编码

##### 函数名：string.encodeURL

##### 参数列表：

| 参数    | 类型   | 描述           | 可空  |
| ------- | ------ | -------------- | ----- |
| self    | String | 被处理的string | False |
| charset | String | 源字符串编码   | False |

##### 返回值：

| 类型   | 描述           |
| ------ | -------------- |
| String | 编码后的String |

### 字符串URL解码

##### 函数名：string.decodeURL

##### 参数列表：

| 参数    | 类型   | 描述           | 可空  |
| ------- | ------ | -------------- | ----- |
| self    | String | 被处理的string | False |
| charset | String | 目标字符串编码 | False |

##### 返回值：

| 类型   | 描述           |
| ------ | -------------- |
| String | 解码后的String |

#### 示例：

```lua
local a = "你好hello"
print(a:sub(0,3)) -- 你
print(a:substring(0,2)) --你好
b = a:encode("UTF-8","GBK")
c = b:encode("GBK","UTF-8")
print(b) -- 浣犲ソhello
print(c) -- 你好hello
print(a:length()) -- 7
print(a:len()) -- 11
d = a:encodeURL("UTF-8") 
e = d:decodeURL("UTF-8") 
print(d) -- %E4%BD%A0%E5%A5%BDhello
print(e) -- 你好hello
```
