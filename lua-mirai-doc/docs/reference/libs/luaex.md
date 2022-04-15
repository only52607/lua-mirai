# lua扩展支持库
为了更流畅地使用lua mirai开发，lua mirai提供了若干语言特性。

## userdata元属性
lua mirai的所有对象基于userdata设计，但userdata不同于普通table，不支持对成员的遍历，因此，lua mirai内提供了以下方法
### __functions 元属性
`__functions`是一个表，包含了该userdata内所有的方法。
### __properties 元属性
`__properties`是一个表，包含了该userdata内所有的属性。

示例：

``` lua
for k,v in pairs(a.__functions) do
    print(v)
end
print()
for k,v in pairs(a.__properties) do
    print(k .. ":" .. tostring(v))
end
print()
```

## 原生线程
### 启动

#### 函数名：thread

#### 参数列表：

| 参数    | 类型   | 描述       | 可空  |
| ------- | ------ | ---------- | ----- |
| body     | function | 线程函数体   | False |

#### 函数名：thread

### 休眠

#### 函数名：sleep

#### 参数列表：

| 参数    | 类型   | 描述       | 可空  |
| ------- | ------ | ---------- | ----- |
| time     | number | 延迟毫秒   | False |

示例:
```lua
thread(function ()
    sleep(3000)
    print("3秒后输出结果")
end)
```